<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <h3 class="text-xl font-bold text-slate-800">成绩管理</h3>
      <div class="flex space-x-3">
        <button
            @click="$emit('add-course')"
            class="px-5 py-2 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300"
        >
          <i class="fas fa-plus mr-2"></i>
          添加课程成绩
        </button>
      </div>
    </div>

    <!-- 筛选区域 -->
    <div class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-2xl border border-slate-200 p-6 shadow-sm">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <!-- 学号筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-id-card mr-1"></i>
            学号
          </label>
          <input
              v-model="filters.studentId"
              type="text"
              placeholder="输入学号"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @input="handleFilter"
          />
        </div>

        <!-- 课程编号筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-hashtag mr-1"></i>
            课程编号
          </label>
          <input
              v-model="filters.courseId"
              type="text"
              placeholder="输入课程编号"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @input="handleFilter"
          />
        </div>

        <!-- 学年学期筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-calendar mr-1"></i>
            学年
          </label>
          <select
              v-model="filters.academicYear"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @change="handleFilter"
          >
            <option value="">全部学年</option>
            <option v-for="year in academicYears" :key="year" :value="year">
              {{ year }}学年
            </option>
          </select>
        </div>

        <!-- 成绩范围筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-chart-line mr-1"></i>
            成绩范围
          </label>
          <select
              v-model="filters.scoreRange"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @change="handleFilter"
          >
            <option value="">全部成绩</option>
            <option value="90-100">优秀 (90-100)</option>
            <option value="80-89">良好 (80-89)</option>
            <option value="70-79">中等 (70-79)</option>
            <option value="60-69">及格 (60-69)</option>
            <option value="0-59">不及格 (&lt;60)</option>
          </select>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="flex justify-between items-center mt-4">
        <div class="text-sm text-slate-600">
          共找到 {{ filteredCourses.length }} 条记录
        </div>
        <div class="flex space-x-2">
          <button
              @click="resetFilters"
              class="px-4 py-2 border border-slate-300 text-slate-700 rounded-lg hover:bg-slate-50 transition-colors"
          >
            <i class="fas fa-redo mr-1"></i>
            重置筛选
          </button>
        </div>
      </div>
    </div>

    <!-- 课程成绩表格 -->
    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full min-w-[800px]">
          <thead>
          <tr class="bg-gradient-to-r from-slate-50 to-slate-100">
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">学号</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">课程编号</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">课程名称</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">学年学期</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">学分</th>
            <th class="px-6 py-4 text-left pl-14 text-slate-600 font-semibold">成绩</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">绩点</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">操作</th>
          </tr>
          </thead>
          <tbody>
          <tr
              v-for="(course, index) in paginatedCourses"
          :key="getCourseKey(course)"
          :class="[
          'border-b border-slate-100 transition-colors duration-200',
          index % 2 === 0 ? 'bg-white' : 'bg-slate-50',
          'hover:bg-gradient-to-r hover:from-blue-50 hover:to-indigo-50'
          ]"
          >
            <td class="px-6 py-4 font-medium text-center">
              {{ getSafeValue(course.student_id, course.studentId, '--') }}
            </td>
            <td class="px-6 py-4 text-center">
              <span class="bg-blue-50 text-blue-700 px-2 py-1 rounded text-sm">
                {{ getSafeValue(course.course_id, course.courseId, '--') }}
              </span>
            </td>
            <td class="px-6 py-4 text-center">
              <!-- 使用新的课程名称获取方法 -->
              <div v-if="getCourseDisplayName(course)" class="flex flex-col">
                <span class="text-slate-800 font-medium">
                  {{ getCourseDisplayName(course) }}
                </span>
              </div>
              <div v-else class="text-gray-400 italic text-sm">
                无课程名称
                <div class="text-xs text-gray-500">({{ getCourseId(course) }})</div>
              </div>
            </td>
            <td class="px-6 py-4 text-center">
              <span class="text-slate-600">
                {{ formatAcademicYear(course) }}
              </span>
            </td>
            <td class="px-6 py-4 text-center">
              <span :class="['px-3 py-1 rounded-full text-sm', getCreditClass(course)]">
                {{ getSafeNumber(course.credit_earned, course.creditEarned, 0) }} 学分
              </span>
            </td>
            <td class="px-6 py-4 text-center">
              <div class="flex items-center space-x-2">
                <span :class="['px-3 py-1 rounded-full text-sm font-medium', getScoreClass(getSafeNumber(course.score, course.final_score, 0))]">
                  {{ getSafeNumber(course.score, course.final_score, 0) }}
                </span>
                <span class="text-sm text-slate-500">
                  ({{ getScoreText(getSafeNumber(course.score, course.final_score, 0)) }})
                </span>
              </div>
            </td>
            <td class="px-6 py-4 text-center">
              <span :class="['px-3 py-1 rounded-full text-sm font-medium', getGradePointClass(getSafeNumber(course.grade_point, course.gradePoint, 0))]">
                {{ getSafeNumber(course.grade_point, course.gradePoint, 0).toFixed(1) }}
              </span>
            </td>
            <td class="px-6 py-4 ">
              <div class="flex space-x-2 justify-center ">
                <button
                    @click="$emit('edit-course', course)"
                    class="px-4 py-2 bg-gradient-to-r from-blue-500 to-indigo-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300 text-sm"
                >
                  <i class="fas fa-edit mr-1"></i>
                  编辑
                </button>
                <button
                    @click="$emit('delete-course', getSafeValue(course.record_id, course.recordId, course.id))"
                    class="px-4 py-2 bg-gradient-to-r from-red-500 to-pink-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300 text-sm"
                >
                  <i class="fas fa-trash-alt mr-1"></i>
                  删除
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredCourses.length === 0" class="text-center py-12">
        <i class="fas fa-graduation-cap text-4xl text-slate-300 mb-4"></i>
        <p class="text-slate-500">暂无课程成绩数据</p>
        <div class="mt-6 space-y-4">
          <button
              @click="$emit('add-course')"
              class="px-6 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300"
          >
            <i class="fas fa-plus mr-2"></i>
            添加第一条课程成绩记录
          </button>
          <p v-if="isFiltered && originalCourses.length > 0" class="text-slate-400">
            或 <button @click="resetFilters" class="text-indigo-500 hover:text-indigo-600 underline">重置筛选条件</button> 查看所有记录
          </p>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="filteredCourses.length > 0" class="px-6 py-4 border-t border-slate-100">
        <div class="flex items-center justify-between">
          <div class="text-sm text-slate-600">
            显示第 {{ (currentPage - 1) * pageSize + 1 }} - {{ Math.min(currentPage * pageSize, filteredCourses.length) }} 条，共 {{ filteredCourses.length }} 条
          </div>
          <div class="flex space-x-2">
            <button
                @click="prevPage"
                :disabled="currentPage === 1"
                :class="[
                'px-4 py-2 rounded-lg transition-colors',
                currentPage === 1
                  ? 'bg-slate-100 text-slate-400 cursor-not-allowed'
                  : 'bg-slate-50 text-slate-700 hover:bg-slate-100'
              ]"
            >
              上一页
            </button>
            <span class="px-4 py-2 bg-indigo-50 text-indigo-600 rounded-lg">
              第 {{ currentPage }} 页
            </span>
            <button
                @click="nextPage"
                :disabled="currentPage * pageSize >= filteredCourses.length"
                :class="[
                'px-4 py-2 rounded-lg transition-colors',
                currentPage * pageSize >= filteredCourses.length
                  ? 'bg-slate-100 text-slate-400 cursor-not-allowed'
                  : 'bg-slate-50 text-slate-700 hover:bg-slate-100'
              ]"
            >
              下一页
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, computed, watch, onMounted} from 'vue'

const props = defineProps({
  courses: {
    type: Array,
    default: () => []
  }
})

defineEmits(['add-course', 'edit-course', 'delete-course'])

// 筛选条件
const filters = reactive({
  studentId: '',
  courseId: '',
  academicYear: '',
  scoreRange: ''
})

// 分页
const currentPage = ref(1)
const pageSize = 5

// 课程名称映射表（前端备用的映射）
const courseNameMapping = {
  'MA101': '高等数学',
  'CS101': '计算机科学导论',
  'ENG101': '大学英语',
  'PHY101': '大学物理',
  'CHM101': '大学化学',
  'BIO101': '生物学基础',
  'PE101': '体育',
  'POL101': '思想政治',
  'ART101': '艺术欣赏',
  'HIST101': '中国近现代史纲要'
}

// 安全获取值函数
const getSafeValue = (value1, value2, defaultValue = '') => {
  if (value1 !== null && value1 !== undefined && value1 !== '') {
    return value1
  }
  if (value2 !== null && value2 !== undefined && value2 !== '') {
    return value2
  }
  return defaultValue
}

// 安全获取数字
const getSafeNumber = (value1, value2, defaultValue = 0) => {
  const val1 = parseFloat(value1)
  const val2 = parseFloat(value2)

  if (!isNaN(val1)) {
    return val1
  }
  if (!isNaN(val2)) {
    return val2
  }
  return defaultValue
}

// 获取唯一键
const getCourseKey = (course) => {
  const recordId = getSafeValue(course.record_id, course.recordId, course.id)
  if (recordId) return String(recordId)

  // 使用组合键
  const studentId = getSafeValue(course.student_id, course.studentId)
  const courseId = getSafeValue(course.course_id, course.courseId)
  const academicYear = getSafeValue(course.academic_year, course.academicYear, course.year)
  const semester = getSafeValue(course.semester, course.term)

  return `${studentId}-${courseId}-${academicYear}-${semester}` || Math.random().toString()
}

// 格式化学年显示
const formatAcademicYear = (course) => {
  const year = getSafeValue(course.academic_year, course.academicYear, course.year)
  const semester = getSafeValue(course.semester, course.term)

  if (year && semester) {
    return `${year} 第${semester}学期`
  } else if (year) {
    return `${year}`
  } else if (semester) {
    return `第${semester}学期`
  }
  return '--'
}

// 学分样式
const getCreditClass = (course) => {
  const credit = getSafeNumber(course.credit_earned, course.creditEarned, 0)
  const score = getSafeNumber(course.score, course.final_score, 0)

  if (score < 60 && credit > 0) {
    return 'bg-red-100 text-red-700'
  }
  if (credit === 0) return 'bg-gray-100 text-gray-700'
  return 'bg-amber-100 text-amber-700'
}

// 获取课程显示名称
const getCourseDisplayName = (course) => {
  // 1. 首先尝试直接获取课程名称
  let courseName = getSafeValue(course.course_name, course.courseName, '')

  // 如果有名称，直接返回
  if (courseName && courseName.trim()) {
    return courseName.trim()
  }

  // 2. 尝试从映射表中获取
  const courseId = getSafeValue(course.course_id, course.courseId, '')
  if (courseId && courseNameMapping[courseId]) {
    return courseNameMapping[courseId]
  }

  // 3. 如果没有，根据课程ID生成一个友好的名称
  if (courseId) {
    // 尝试从课程ID中提取信息
    const prefix = courseId.substring(0, 2).toUpperCase()
    const courseNumber = courseId.substring(2) || ''

    const prefixMap = {
      'MA': '数学',
      'CS': '计算机科学',
      'EN': '英语',
      'PH': '物理',
      'CH': '化学',
      'BI': '生物',
      'PE': '体育',
      'PO': '政治',
      'AR': '艺术',
      'HI': '历史'
    }

    const prefixName = prefixMap[prefix] || prefix
    return `${prefixName}${courseNumber} 课程`
  }

  // 4. 如果什么都没有，返回默认值
  return ''
}

// 获取课程编号
const getCourseId = (course) => {
  return getSafeValue(course.course_id, course.courseId, '未知')
}

// 是否需要显示课程编号（当课程名称不包含课程编号时显示）
const showCourseCode = (course) => {
  const courseName = getCourseDisplayName(course)
  const courseId = getCourseId(course)

  if (!courseName || !courseId || courseId === '未知') {
    return false
  }

  // 如果课程名称中已经包含了课程编号，就不需要单独显示
  return !courseName.includes(courseId)
}

// 获取所有学年（去重）
const academicYears = computed(() => {
  const years = []
  props.courses.forEach(course => {
    const year = getSafeValue(course.academic_year, course.academicYear, course.year)
    if (year && !years.includes(year)) {
      years.push(year)
    }
  })
  return years.sort((a, b) => b.localeCompare(a))
})

// 判断是否正在筛选
const isFiltered = computed(() => {
  return Object.values(filters).some(value => value !== '')
})

// 原始课程数据备份
const originalCourses = computed(() => props.courses)

// 筛选后的课程
const filteredCourses = computed(() => {
  let result = [...originalCourses.value]

  // 学号筛选
  if (filters.studentId) {
    result = result.filter(course => {
      const studentId = getSafeValue(course.student_id, course.studentId, '')
      return studentId.toString().includes(filters.studentId)
    })
  }

  // 课程编号筛选
  if (filters.courseId) {
    result = result.filter(course => {
      const courseId = getSafeValue(course.course_id, course.courseId, '')
      return courseId.toLowerCase().includes(filters.courseId.toLowerCase())
    })
  }

  // 学年筛选
  if (filters.academicYear) {
    result = result.filter(course => {
      const academicYear = getSafeValue(course.academic_year, course.academicYear, course.year)
      return academicYear === filters.academicYear
    })
  }

  // 成绩范围筛选
  if (filters.scoreRange) {
    const [min, max] = filters.scoreRange.split('-').map(Number)
    result = result.filter(course => {
      const score = getSafeNumber(course.score, course.final_score, 0)
      return score >= min && score <= max
    })
  }

  // 排序：按学年倒序，再按学期
  result.sort((a, b) => {
    const yearA = getSafeValue(a.academic_year, a.academicYear, a.year, '')
    const yearB = getSafeValue(b.academic_year, b.academicYear, b.year, '')
    const semesterA = getSafeNumber(a.semester, a.term, 0)
    const semesterB = getSafeNumber(b.semester, b.term, 0)

    const yearCompare = yearB.localeCompare(yearA)
    if (yearCompare !== 0) return yearCompare
    return semesterB - semesterA
  })

  return result
})

// 分页后的数据
const paginatedCourses = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return filteredCourses.value.slice(start, end)
})

// 处理筛选
const handleFilter = () => {
  currentPage.value = 1 // 重置到第一页
}

// 重置筛选
const resetFilters = () => {
  Object.keys(filters).forEach(key => {
    filters[key] = ''
  })
  currentPage.value = 1
}

// 分页控制
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

const nextPage = () => {
  if (currentPage.value * pageSize < filteredCourses.value.length) {
    currentPage.value++
  }
}

// 工具函数
const getScoreClass = (score) => {
  const numScore = parseFloat(score)
  if (numScore >= 90) return 'bg-emerald-100 text-emerald-700'
  if (numScore >= 80) return 'bg-blue-100 text-blue-700'
  if (numScore >= 70) return 'bg-purple-100 text-purple-700'
  if (numScore >= 60) return 'bg-amber-100 text-amber-700'
  return 'bg-red-100 text-red-700'
}

const getScoreText = (score) => {
  const numScore = parseFloat(score)
  if (numScore >= 90) return '优秀'
  if (numScore >= 80) return '良好'
  if (numScore >= 70) return '中等'
  if (numScore >= 60) return '及格'
  return '不及格'
}

// 绩点样式
const getGradePointClass = (gradePoint) => {
  const numGradePoint = parseFloat(gradePoint)
  if (numGradePoint >= 4.0) return 'bg-gradient-to-r from-emerald-500 to-teal-500 text-white'
  if (numGradePoint >= 3.5) return 'bg-emerald-100 text-emerald-700'
  if (numGradePoint >= 3.0) return 'bg-blue-100 text-blue-700'
  if (numGradePoint >= 2.5) return 'bg-purple-100 text-purple-700'
  if (numGradePoint >= 2.0) return 'bg-amber-100 text-amber-700'
  return 'bg-red-100 text-red-700'
}

// 监听原始数据变化重置分页
watch(() => props.courses, () => {
  currentPage.value = 1
})

</script>