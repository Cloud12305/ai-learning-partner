<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl w-full max-w-2xl flex flex-col shadow-2xl max-h-[90vh]">
      <!-- 对话框头部 -->
      <div class="px-8 py-6 bg-gradient-to-r from-blue-500 to-cyan-500 text-white flex-shrink-0">
        <div class="flex items-center justify-between">
          <h2 class="text-2xl font-bold">
            <i class="fas fa-edit mr-3"></i>
            编辑课程成绩
          </h2>
          <button
              @click="close"
              class="text-white hover:text-gray-200 transition-colors"
          >
            <i class="fas fa-times text-2xl"></i>
          </button>
        </div>
      </div>

      <!-- 表单内容（可滚动） -->
      <div class="flex-1 overflow-y-auto p-8">
        <div v-if="loading" class="text-center py-12">
          <i class="fas fa-spinner fa-spin text-4xl text-indigo-500"></i>
          <p class="mt-4 text-slate-600">加载中...</p>
        </div>

        <form v-else @submit.prevent="save" class="space-y-6">
          <!-- 基本信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学号（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-id-card mr-2"></i>
                学号
              </label>
              <input
                  :value="getSafeValue(formData.student_id, formData.studentId)"
                  type="text"
                  readonly
                  class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
              />
            </div>

            <!-- 课程编号（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-hashtag mr-2"></i>
                课程编号
              </label>
              <input
                  :value="getSafeValue(formData.course_id, formData.courseId)"
                  type="text"
                  readonly
                  class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
              />
            </div>
          </div>

          <!-- 课程信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 课程名称（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-book mr-2"></i>
                课程名称
              </label>
              <div class="flex">
                <input
                    :value="getSafeValue(formData.course_name, formData.courseName)"
                    type="text"
                    readonly
                    class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
                />
              </div>
            </div>

            <!-- 学分（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-star mr-2"></i>
                学分
              </label>
              <div class="flex">
                <input
                    :value="getSafeNumber(formData.credit_earned, formData.creditEarned)"
                    type="number"
                    readonly
                    class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
                />
              </div>
            </div>
          </div>

          <!-- 成绩警告 -->
          <div v-if="getSafeNumber(formData.score) < 60 && getSafeNumber(formData.credit_earned, formData.creditEarned) > 0"
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
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
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
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
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
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
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
              'px-8 py-3 bg-gradient-to-r from-blue-500 to-cyan-500 text-white font-semibold rounded-xl transition-all duration-300',
              isSaving || !isFormValid ? 'opacity-50 cursor-not-allowed' : 'hover:shadow-lg hover:scale-105'
            ]"
          >
            <i v-if="isSaving" class="fas fa-spinner fa-spin mr-2"></i>
            <i v-else class="fas fa-check mr-2"></i>
            {{ isSaving ? '保存中...' : '保存修改' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'

const props = defineProps({
  visible: Boolean,
  course: Object
})

const emit = defineEmits(['close', 'save'])

// 状态
const loading = ref(true)
const isSaving = ref(false)

// 表单数据
const formData = reactive({
  record_id: '',
  student_id: '',
  course_id: '',
  course_name: '',
  academic_year: '',
  semester: '',
  credit_earned: '',
  score: '',
  remark: '',
  created_at: '',
  updated_at: ''
})

// 表单验证
const errors = reactive({})

// 表单验证规则
const validationRules = {
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
    if (field === 'score') {
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
  const num = parseFloat(score)
  if (isNaN(num)) return 0
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
  const numScore = parseFloat(score)
  if (isNaN(numScore)) return 'bg-gray-100 text-gray-700'
  if (numScore >= 90) return 'bg-emerald-100 text-emerald-700'
  if (numScore >= 80) return 'bg-blue-100 text-blue-700'
  if (numScore >= 70) return 'bg-purple-100 text-purple-700'
  if (numScore >= 60) return 'bg-amber-100 text-amber-700'
  return 'bg-red-100 text-red-700'
}

// 获取成绩文本
const getScoreText = (score) => {
  const numScore = parseFloat(score)
  if (isNaN(numScore)) return '--'
  if (numScore >= 90) return '优秀'
  if (numScore >= 80) return '良好'
  if (numScore >= 70) return '中等'
  if (numScore >= 60) return '及格'
  return '不及格'
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '--'
  try {
    return new Date(dateString).toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch {
    return dateString
  }
}

// 关闭对话框
const close = () => {
  emit('close')
}

// 保存修改
const save = async () => {
  // 简单验证
  if (!formData.academic_year || !formData.semester) {
    alert('请选择学年和学期')
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
    // 自动处理学分
    const finalCredit = scoreNum < 60 ? 0 : getSafeNumber(formData.credit_earned)

    const courseData = {
      recordId: getSafeValue(formData.record_id, formData.recordId),
      studentId: getSafeValue(formData.student_id, formData.studentId),
      courseId: getSafeValue(formData.course_id, formData.courseId),
      courseName: getSafeValue(formData.course_name, formData.courseName),
      academicYear: formData.academic_year,
      semester: parseInt(formData.semester),
      score: scoreNum,
      creditEarned: finalCredit,
      remark: formData.remark
    }

    console.log('保存课程数据:', courseData)
    emit('save', courseData)
  } catch (err) {
    console.error('保存失败:', err)
    alert('保存失败')
  } finally {
    isSaving.value = false
  }
}
// 监听课程数据变化
watch(() => props.course, (course) => {
  if (course) {
    console.log('收到课程数据:', course)
    loading.value = false

    // 映射字段
    formData.record_id = getSafeValue(course.record_id, course.recordId, course.id)
    formData.student_id = getSafeValue(course.student_id, course.studentId)
    formData.course_id = getSafeValue(course.course_id, course.courseId, course.courseCode, course.course_code)
    formData.course_name = getSafeValue(course.course_name, course.courseName)
    formData.academic_year = getSafeValue(course.academic_year, course.academicYear, course.year)
    formData.semester = getSafeValue(course.semester, course.term)
    formData.score = getSafeNumber(course.score, course.final_score)
    formData.credit_earned = getSafeNumber(course.credit_earned, course.creditEarned, course.credits)
    formData.remark = getSafeValue(course.remark, course.note)
    formData.created_at = getSafeValue(course.created_at, course.createdAt)
    formData.updated_at = getSafeValue(course.updated_at, course.updatedAt)

    console.log('映射后的表单数据:', formData)
  } else {
    loading.value = true
  }
}, {immediate: true})

// 监听对话框关闭时重置
watch(() => props.visible, (visible) => {
  if (!visible) {
    loading.value = true
    Object.keys(formData).forEach(key => {
      formData[key] = ''
    })
    Object.keys(errors).forEach(key => {
      delete errors[key]
    })
  }
})
</script>