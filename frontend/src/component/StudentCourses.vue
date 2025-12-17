<template>
  <div class="bg-white/90 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-blue-100">
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-bold text-blue-800">
        <i class="fas fa-graduation-cap mr-3 text-blue-500"></i>
        课程成绩
      </h2>
      <div class="flex items-center space-x-3">
        <!-- 学年学期筛选 -->
        <select
            v-if="availableSemesters.length > 0"
            v-model="selectedSemester"
            class="px-3 py-2 bg-white border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        >
          <option value="">全部学期</option>
          <option v-for="semester in availableSemesters" :key="semester.key" :value="semester.key">
            {{ semester.academicYear }} 第{{ semester.semester }}学期
          </option>
        </select>

        <button
            @click="$emit('show-all')"
            class="px-4 py-2 bg-gradient-to-r from-blue-500 to-indigo-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300 text-sm"
        >
          查看全部
        </button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="text-center py-8">
      <i class="fas fa-spinner fa-spin text-3xl text-blue-500 mb-3"></i>
      <p class="text-slate-500">正在加载课程数据...</p>
    </div>

    <!-- 数据内容 -->
    <div v-else class="space-y-4">
      <div
          v-for="course in filteredCourses"
          :key="course.courseId + '-' + course.academicYear + '-' + course.semester"
          class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl p-4 hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1"
      >
        <div class="flex items-center justify-between mb-2">
          <div>
            <span class="font-semibold text-slate-800">{{ course.courseId }}</span>
            <span class="ml-2 text-sm text-slate-500">
              {{ formatAcademicYearDisplay(course.academicYear) }} 第{{ course.semester }}学期
            </span>
          </div>
          <span :class="[
            'px-3 py-1 rounded-full text-sm font-medium',
            getScoreClass(course.score)
          ]">
            {{ course.score }}
            <span v-if="course.gradePoint" class="ml-1 text-xs">
              ({{ formatGPA(course.gradePoint) }})
            </span>
          </span>
        </div>
        <div class="flex items-center justify-between text-sm text-slate-500">
          <span>学分：{{ course.creditEarned || 0 }}</span>
          <span v-if="course.gradePoint">绩点：{{ formatGPA(course.gradePoint) }}</span>
        </div>
      </div>

      <!-- 无数据提示 -->
      <div v-if="filteredCourses.length === 0" class="text-center py-8 text-slate-400">
        <i class="fas fa-graduation-cap text-3xl mb-3"></i>
        <p v-if="selectedSemester">该学期暂无课程成绩</p>
        <p v-else>暂无课程成绩</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, onMounted, watch} from 'vue'
import {getAllCourses} from '@/services/api/academic'

defineEmits(['show-all'])

// 数据状态
const allCourses = ref([])
const selectedSemester = ref('')
const loading = ref(false)

// 格式化学年显示
const formatAcademicYearDisplay = (year) => {
  if (!year) return '--'
  if (year.includes('-')) return year
  if (year.length === 4) return `${year}-${parseInt(year) + 1}`
  return year
}

// 格式化绩点
const formatGPA = (gpa) => {
  if (gpa === undefined || gpa === null) return '0.00'
  if (typeof gpa === 'number') return gpa.toFixed(2)
  if (typeof gpa === 'string') return parseFloat(gpa).toFixed(2)
  return '0.00'
}

// 获取所有课程
const fetchAllCourses = async () => {
  try {
    loading.value = true
    console.log('开始获取所有课程数据...')

    const response = await getAllCourses()
    console.log('课程数据响应:', response)

    if (response && response.success === true && response.data) {
      allCourses.value = Array.isArray(response.data) ? response.data : []
      console.log(`获取到 ${allCourses.value.length} 条课程记录`)

      // 调试：打印课程信息
      if (allCourses.value.length > 0) {
        console.log('课程样例:', allCourses.value[0])
      }
    } else {
      console.warn('课程数据接口返回异常:', response)
      allCourses.value = []
    }
  } catch (error) {
    console.error('获取课程数据失败:', error)
    allCourses.value = []
  } finally {
    loading.value = false
  }
}

// 获取可用的学年学期选项
const availableSemesters = computed(() => {
  if (!allCourses.value || allCourses.value.length === 0) return []

  const semesterSet = new Set()
  const semesters = []

  allCourses.value.forEach(course => {
    if (!course.academicYear || course.semester === undefined || course.semester === null) {
      return
    }

    const key = `${course.academicYear}-${course.semester}`
    if (!semesterSet.has(key)) {
      semesterSet.add(key)
      semesters.push({
        key,
        academicYear: formatAcademicYearDisplay(course.academicYear),
        semester: course.semester
      })
    }
  })

  // 按学年学期倒序排序
  return semesters.sort((a, b) => {
    if (a.academicYear !== b.academicYear) {
      return b.academicYear.localeCompare(a.academicYear)
    }
    return b.semester - a.semester
  })
})

// 根据筛选条件过滤课程
const filteredCourses = computed(() => {
  try {
    if (!allCourses.value || allCourses.value.length === 0) return []

    if (!selectedSemester.value) {
      // 如果没有选择学期，显示所有课程
      return [...allCourses.value]
          .filter(course => course.academicYear && course.semester !== undefined)
          .sort((a, b) => {
            const yearA = a.academicYear || ''
            const yearB = b.academicYear || ''
            const semA = a.semester || 0
            const semB = b.semester || 0

            if (yearA !== yearB) {
              return yearB.localeCompare(yearA)
            }
            if (semA !== semB) {
              return semB - semA
            }
            return (b.courseId || '').localeCompare(a.courseId || '')
          })
    }

    // 修改这里：正确处理包含连字符的学年字符串
    const parts = selectedSemester.value.split('-')

    // 如果格式是 "2024-2025-1"
    let academicYear, semester
    if (parts.length === 3) {
      // 格式：2024-2025-1
      academicYear = `${parts[0]}-${parts[1]}`  // 合并成 "2024-2025"
      semester = parts[2]  // "1"
    } else if (parts.length === 2) {
      // 格式：2024-1（备用情况）
      academicYear = parts[0]
      semester = parts[1]
    } else {
      console.error('学期格式错误:', selectedSemester.value)
      return []
    }

    console.log(' 筛选条件解析:', {
      selectedSemester: selectedSemester.value,
      academicYear,
      semester,
      parts
    })

    // 打印所有课程数据用于调试
    console.log('所有课程数据:')
    allCourses.value.forEach((course, index) => {
      console.log(`课程${index}:`, {
        id: course.courseId,
        year: course.academicYear,
        yearType: typeof course.academicYear,
        semester: course.semester,
        semesterType: typeof course.semester
      })
    })

    return allCourses.value
        .filter(course => {
          if (!course.academicYear || course.semester === undefined) {
            return false
          }

          // 调试日志
          const matches = course.academicYear === academicYear &&
              String(course.semester) === semester

          if (matches) {
            console.log('匹配成功:', {
              courseId: course.courseId,
              courseYear: course.academicYear,
              courseSemester: course.semester,
              filterYear: academicYear,
              filterSemester: semester
            })
          }

          return matches
        })
        .sort((a, b) => (b.courseId || '').localeCompare(a.courseId || ''))
  } catch (error) {
    console.error('过滤课程数据时出错:', error)
    return []
  }
})

// 成绩颜色分类
const getScoreClass = (score) => {
  const scoreNum = Number(score)
  if (isNaN(scoreNum)) return 'bg-slate-100 text-slate-700'
  if (scoreNum >= 90) return 'bg-emerald-100 text-emerald-700'
  if (scoreNum >= 80) return 'bg-blue-100 text-blue-700'
  if (scoreNum >= 70) return 'bg-purple-100 text-purple-700'
  if (scoreNum >= 60) return 'bg-amber-100 text-amber-700'
  return 'bg-red-100 text-red-700'
}

onMounted(() => {
  console.log('课程成绩组件挂载')
  fetchAllCourses()
})

// 监听筛选变化
watch(selectedSemester, (newVal) => {
  console.log('学期筛选变化:', newVal)
  console.log('过滤后的课程数:', filteredCourses.value.length)
})
</script>