<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl w-full max-w-2xl flex flex-col shadow-2xl max-h-[90vh]">
      <!-- 对话框头部 -->
      <div class="px-8 py-6 bg-gradient-to-r from-indigo-500 to-purple-500 text-white flex-shrink-0">
        <div class="flex items-center justify-between">
          <h2 class="text-2xl font-bold">
            <i class="fas fa-plus-circle mr-3"></i>
            添加课程成绩
          </h2>
          <button
              @click="close"
              class="text-white hover:text-gray-200 transition-colors"
          >
            <i class="fas fa-times text-2xl"></i>
          </button>
        </div>
        <p class="text-indigo-100 mt-2">为学生添加新的课程成绩记录</p>
      </div>

      <!-- 表单内容（可滚动） -->
      <div class="flex-1 overflow-y-auto p-8">
        <form @submit.prevent="save" class="space-y-6">
          <!-- 基本信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学号 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-id-card mr-2"></i>
                学号 *
              </label>
              <input
                  v-model="formData.student_id"
                  type="text"
                  placeholder="请输入学生学号"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.student_id ? 'border-red-500' : ''"
                  @input="validateField('student_id')"
              />
              <p v-if="errors.student_id" class="mt-2 text-sm text-red-600">{{ errors.student_id }}</p>
            </div>

            <!-- 课程编号 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-hashtag mr-2"></i>
                课程编号 *
              </label>
              <input
                  v-model="formData.course_id"
                  type="text"
                  placeholder="如：MA101"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.course_id ? 'border-red-500' : ''"
                  @input="handleCourseIdInput"
                  list="courseSuggestions"
              />
              <!-- 自动完成建议 -->
              <datalist id="courseSuggestions">
                <option v-for="course in availableCourses" :key="course.id" :value="course.id">
                  {{ course.name }}
                </option>
              </datalist>
              <p v-if="errors.course_id" class="mt-2 text-sm text-red-600">{{ errors.course_id }}</p>

              <!-- 自动填充提示 -->
              <div v-if="autoFillInfo.show" class="mt-2 text-sm text-blue-600 animate-pulse">
                <i class="fas fa-magic mr-1"></i>
                自动填充：{{ autoFillInfo.courseName }}（{{ autoFillInfo.credits }}学分）
              </div>
            </div>
          </div>

          <!-- 课程信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 课程名称 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-book mr-2"></i>
                课程名称 *
              </label>
              <input
                  v-model="formData.course_name"
                  type="text"
                  placeholder="如：高等数学A"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.course_name ? 'border-red-500' : ''"
                  @input="validateField('course_name')"
                  readonly
              />
              <p v-if="errors.course_name" class="mt-2 text-sm text-red-600">{{ errors.course_name }}</p>
            </div>

            <!-- 学分 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-star mr-2"></i>
                学分 *
              </label>
              <input
                  v-model.number="formData.credit_earned"
                  type="number"
                  min="0"
                  max="10"
                  step="0.5"
                  placeholder="如：4.0"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.credit_earned ? 'border-red-500' : ''"
                  @input="validateCreditEarned"
                  readonly
              />
              <p v-if="errors.credit_earned" class="mt-2 text-sm text-red-600">{{ errors.credit_earned }}</p>
            </div>
          </div>

          <!-- 成绩警告 -->
          <div v-if="getSafeNumber(formData.score) < 60 && getSafeNumber(formData.credit_earned) > 0"
               class="bg-red-50 border-2 border-red-200 rounded-xl p-4 mb-4">
            <div class="flex items-center">
              <i class="fas fa-exclamation-triangle text-red-500 mr-3 text-xl"></i>
              <div>
                <h4 class="font-semibold text-red-700">成绩与学分不匹配警告</h4>
                <p class="text-red-600 text-sm mt-1">
                  成绩低于60分（不及格），系统将自动将学分设为0
                </p>
              </div>
            </div>
          </div>

          <!-- 学年学期 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学年 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-calendar-alt mr-2"></i>
                学年 *
              </label>
              <select
                  v-model="formData.academic_year"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.academic_year ? 'border-red-500' : ''"
                  @change="validateField('academic_year')"
              >
                <option value="">请选择学年</option>
                <option v-for="year in generateAcademicYears()" :key="year" :value="year">
                  {{ year }}学年
                </option>
              </select>
              <p v-if="errors.academic_year" class="mt-2 text-sm text-red-600">{{ errors.academic_year }}</p>
            </div>

            <!-- 学期 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-calendar mr-2"></i>
                学期 *
              </label>
              <select
                  v-model="formData.semester"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.semester ? 'border-red-500' : ''"
                  @change="validateField('semester')"
              >
                <option value="">请选择学期</option>
                <option value="1">第一学期</option>
                <option value="2">第二学期</option>
              </select>
              <p v-if="errors.semester" class="mt-2 text-sm text-red-600">{{ errors.semester }}</p>
            </div>
          </div>

          <!-- 成绩 -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              <i class="fas fa-chart-line mr-2"></i>
              成绩 *
            </label>
            <div class="relative">
              <input
                  v-model.number="formData.score"
                  type="number"
                  min="0"
                  max="100"
                  step="0.5"
                  placeholder="0-100"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.score ? 'border-red-500' : ''"
                  @input="validateScore"
              />
              <div class="absolute right-3 top-3">
                <span :class="['px-3 py-1 rounded-full text-sm font-medium', getScoreClass(getSafeNumber(formData.score))]">
                  {{ getScoreText(getSafeNumber(formData.score)) }}
                </span>
              </div>
            </div>
            <p v-if="errors.score" class="mt-2 text-sm text-red-600">{{ errors.score }}</p>
          </div>

          <!-- 绩点计算 -->
          <div class="bg-blue-50 border-2 border-blue-100 rounded-xl p-6">
            <h4 class="font-semibold text-blue-800 mb-3">
              <i class="fas fa-calculator mr-2"></i>
              绩点计算
            </h4>
            <div class="flex items-center space-x-4">
              <div class="flex-1">
                <div class="text-sm text-blue-600 mb-1">成绩</div>
                <div class="text-2xl font-bold text-blue-700">
                  {{ getSafeNumber(formData.score) || 0 }}
                </div>
              </div>

              <div class="flex-1">
                <div class="text-sm text-blue-600 mb-1">绩点</div>
                <div class="text-2xl font-bold text-blue-700">
                  {{ calculateGradePoint(getSafeNumber(formData.score)).toFixed(1) }}
                </div>
              </div>
            </div>

            <div v-if="getSafeNumber(formData.score) < 60"
                 class="mt-4 p-3 bg-red-50 border border-red-200 rounded-lg">
              <div class="flex items-center text-red-600">
                <i class="fas fa-exclamation-circle mr-2"></i>
                <span class="text-sm font-medium">成绩不及格，学分绩为0</span>
              </div>
            </div>
          </div>
        </form>
      </div>

      <!-- 对话框底部（固定） -->
      <div class="px-8 py-6 border-t border-slate-200 bg-white flex-shrink-0">
        <div class="flex justify-end space-x-4">
          <button
              type="button"
              @click="close"
              class="px-8 py-3 border-2 border-slate-300 text-slate-700 font-semibold rounded-xl hover:bg-slate-100 transition-all duration-300"
          >
            <i class="fas fa-times mr-2"></i>
            取消
          </button>
          <button
              @click="save"
              :disabled="isSaving || !isFormValid"
              :class="[
              'px-8 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl transition-all duration-300',
              isSaving || !isFormValid ? 'opacity-50 cursor-not-allowed' : 'hover:shadow-lg hover:scale-105'
            ]"
          >
            <i v-if="isSaving" class="fas fa-spinner fa-spin mr-2"></i>
            <i v-else class="fas fa-save mr-2"></i>
            {{ isSaving ? '保存中...' : '保存课程成绩' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, computed, watch} from 'vue'

const props = defineProps({
  visible: Boolean
})

const emit = defineEmits(['close', 'save'])

// 状态
const isSaving = ref(false)

// 表单数据（使用下划线命名，与数据库一致）
const formData = reactive({
  student_id: '',
  course_id: '',
  course_name: '',
  academic_year: '',
  semester: '',
  credit_earned: '',
  score: '',
  remark: ''
})

// 表单验证
const errors = reactive({})

// 根据图片内容设置的课程信息映射表
const courseInfoMap = {
  // 数学类
  'MA101': { name: '高等数学A', credits: 4.5 },
  'MA102': { name: '线性代数', credits: 3.0 },

  // 计算机类
  'CS101': { name: '计算机导论', credits: 2.0 },
  'CS102': { name: 'C语言程序设计', credits: 4.0 },
  'CS201': { name: '数据结构', credits: 4.0 },
  'CS202': { name: '操作系统', credits: 3.5 },

  // 软件工程类
  'SE101': { name: '软件工程导论', credits: 4.5 },
  'SE201': { name: 'Java程序设计', credits: 4.0 },
  'SE301': { name: 'Web开发技术', credits: 3.0 },

  // 通识教育类
  'GE101': { name: '大学英语', credits: 2.0 },
  'GE102': { name: '大学物理', credits: 3.0 },
}

// 可用的课程列表（用于自动完成）
const availableCourses = computed(() => {
  return Object.keys(courseInfoMap).map(id => ({
    id,
    name: courseInfoMap[id].name
  }))
})

// 自动填充信息
const autoFillInfo = reactive({
  show: false,
  courseName: '',
  credits: 0
})

// 安全获取数字
const getSafeNumber = (value, defaultValue = 0) => {
  const val = parseFloat(value)
  return !isNaN(val) ? val : defaultValue
}

// 处理课程编号输入
const handleCourseIdInput = () => {
  const courseId = formData.course_id.trim().toUpperCase()

  if (courseId && courseInfoMap[courseId]) {
    // 自动填充课程信息
    const courseInfo = courseInfoMap[courseId]
    formData.course_name = courseInfo.name
    formData.credit_earned = courseInfo.credits

    // 显示自动填充提示
    autoFillInfo.show = true
    autoFillInfo.courseName = courseInfo.name
    autoFillInfo.credits = courseInfo.credits

    // 3秒后隐藏提示
    setTimeout(() => {
      autoFillInfo.show = false
    }, 3000)
  } else if (courseId) {
    // 清除其他字段
    formData.course_name = ''
    formData.credit_earned = ''
    autoFillInfo.show = false
  }
}

// 表单验证规则
const validationRules = {
  student_id: (value) => {
    if (!value) return '学号不能为空'
    if (!/^\d{8,10}$/.test(value)) return '请输入8-10位数字学号'
    return null
  },
  course_id: (value) => {
    if (!value) return '课程编号不能为空'
    const courseId = value.trim().toUpperCase()
    if (!courseInfoMap[courseId]) {
      return `未找到课程"${courseId}"，请检查课程编号是否正确`
    }
    return null
  },
  course_name: (value) => {
    if (!value) return '课程名称不能为空'
    return null
  },
  academic_year: (value) => {
    if (!value) return '请选择学年'
    return null
  },
  semester: (value) => {
    if (!value) return '请选择学期'
    return null
  },
  score: (value) => {
    if (!value && value !== 0) return '成绩不能为空'
    const num = parseFloat(value)
    if (isNaN(num) || num < 0 || num > 100) return '请输入0-100之间的成绩'
    return null
  }
}

// 验证学分
const validateCreditEarned = () => {
  // 如果成绩小于60，学分自动设为0，不需要验证
  const score = getSafeNumber(formData.score)
  if (score < 60) {
    formData.credit_earned = 0
    delete errors.credit_earned
    return
  }

  const rule = validationRules.credit_earned
  if (rule) {
    const error = rule(formData.credit_earned)
    if (error) {
      errors.credit_earned = error
    } else {
      delete errors.credit_earned
    }
  }
}

// 验证成绩
const validateScore = () => {
  const rule = validationRules.score
  const error = rule(formData.score)

  if (error) {
    errors.score = error
  } else if (getSafeNumber(formData.score) < 60 && getSafeNumber(formData.credit_earned) > 0) {
    errors.score = '成绩低于60分，该课程无法获得学分，系统将自动处理'
  } else {
    delete errors.score
  }
}

// 验证字段
const validateField = (field) => {
  const rule = validationRules[field]
  if (rule) {
    const error = rule(formData[field])
    if (error) {
      errors[field] = error
    } else {
      delete errors[field]
    }
  }
}

// 验证整个表单
const validateForm = () => {
  Object.keys(validationRules).forEach(field => {
    if (field === 'credit_earned') {
      validateCreditEarned()
    } else if (field === 'score') {
      validateScore()
    } else {
      validateField(field)
    }
  })
  return Object.keys(errors).length === 0
}

// 表单是否有效
const isFormValid = computed(() => {
  return Object.keys(validationRules).every(field => {
    const value = formData[field]
    if (!value && value !== 0) return false

    // 成绩必须有效
    if (field === 'score') {
      const num = parseFloat(value)
      return !isNaN(num) && num >= 0 && num <= 100
    }

    return !validationRules[field](value)
  })
})

// 生成学年选项
const generateAcademicYears = () => {
  const currentYear = new Date().getFullYear()
  const years = []
  for (let i = 5; i >= 0; i--) {
    years.push(`${currentYear - i}-${currentYear - i + 1}`)
  }
  return years
}

// 计算绩点
const calculateGradePoint = (score) => {
  const num = getSafeNumber(score)
  if (num == 100) return 5.0
  if (num >= 95) return 4.5
  if (num >= 90) return 4.0
  if (num >= 85) return 3.5
  if (num >= 80) return 3.0
  if (num >= 75) return 2.5
  if (num >= 70) return 2.0
  if (num >= 65) return 1.5
  if (num >= 60) return 1.0
  return 0.0
}

// 获取成绩样式
const getScoreClass = (score) => {
  const numScore = getSafeNumber(score)
  if (numScore >= 90) return 'bg-emerald-100 text-emerald-700'
  if (numScore >= 80) return 'bg-blue-100 text-blue-700'
  if (numScore >= 70) return 'bg-purple-100 text-purple-700'
  if (numScore >= 60) return 'bg-amber-100 text-amber-700'
  return 'bg-red-100 text-red-700'
}

// 获取成绩文本
const getScoreText = (score) => {
  const numScore = getSafeNumber(score)
  if (numScore >= 90) return '优秀'
  if (numScore >= 80) return '良好'
  if (numScore >= 70) return '中等'
  if (numScore >= 60) return '及格'
  return '不及格'
}

// 关闭对话框
const close = () => {
  emit('close')
}

// 保存数据
const save = async () => {
  // 简单验证必填字段
  if (!formData.student_id) {
    alert('请输入学号')
    return
  }

  if (!formData.course_id) {
    alert('请输入课程编号')
    return
  }

  if (!formData.academic_year) {
    alert('请选择学年')
    return
  }

  if (!formData.semester) {
    alert('请选择学期')
    return
  }

  if (formData.score === '' || formData.score === null || formData.score === undefined) {
    alert('请输入成绩')
    return
  }

  const scoreNum = parseFloat(formData.score)
  if (isNaN(scoreNum) || scoreNum < 0 || scoreNum > 100) {
    alert('请输入0-100之间的有效成绩')
    return
  }

  isSaving.value = true
  try {
    // 根据成绩自动处理学分
    const finalCredit = scoreNum < 60 ? 0 : getSafeNumber(formData.credit_earned)

    const courseData = {
      studentId: formData.student_id,
      courseId: formData.course_id,
      courseName: formData.course_name,
      academicYear: formData.academic_year,
      semester: parseInt(formData.semester),
      score: scoreNum,
      creditEarned: finalCredit,
      remark: formData.remark || ''
    }

    console.log('添加课程数据:', courseData)
    emit('save', courseData)
  } catch (err) {
    console.error('保存失败:', err)
    alert('保存失败')
  } finally {
    isSaving.value = false
  }
}

// 监听对话框关闭时重置表单
watch(() => props.visible, (visible) => {
  if (!visible) {
    Object.keys(formData).forEach(key => {
      formData[key] = ''
    })
    Object.keys(errors).forEach(key => {
      delete errors[key]
    })
    autoFillInfo.show = false
    autoFillInfo.courseName = ''
    autoFillInfo.credits = 0
  }
})
</script>