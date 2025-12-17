<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl w-full max-w-2xl flex flex-col shadow-2xl max-h-[90vh]">
      <!-- 对话框头部 -->
      <div class="px-8 py-6 bg-gradient-to-r from-blue-500 to-cyan-500 text-white flex-shrink-0">
        <div class="flex items-center justify-between">
          <h2 class="text-2xl font-bold">
            <i class="fas fa-edit mr-3"></i>
            编辑实验室记录
          </h2>
          <button
              @click="close"
              class="text-white hover:text-gray-200 transition-colors"
          >
            <i class="fas fa-times text-2xl"></i>
          </button>
        </div>
        <p class="text-blue-100 mt-2">修改实验室记录信息</p>
      </div>

      <!-- 表单内容 -->
      <div class="flex-1 overflow-y-auto p-8">
        <div v-if="loading" class="text-center py-12">
          <i class="fas fa-spinner fa-spin text-4xl text-blue-500"></i>
          <p class="mt-4 text-slate-600">加载中...</p>
        </div>

        <form v-else @submit.prevent="save" class="space-y-6">
          <!-- 基本信息（只读） -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学号（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-id-card mr-2"></i>
                学号
              </label>
              <input
                  :value="formData.student_id || '--'"
                  type="text"
                  readonly
                  class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
              />
            </div>

            <!-- 实验室ID（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-hashtag mr-2"></i>
                实验室ID
              </label>
              <input
                  :value="formData.lab_id || '--'"
                  type="text"
                  readonly
                  class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
              />
            </div>
          </div>

          <!-- 实验室信息（只读） -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 实验室名称（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-flask mr-2"></i>
                实验室名称
              </label>
              <input
                  :value="formData.lab_name || '--'"
                  type="text"
                  readonly
                  class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
              />
            </div>

            <!-- 实验项目（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-vial mr-2"></i>
                实验项目
              </label>
              <input
                  :value="formData.experiment_name || '--'"
                  type="text"
                  readonly
                  class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
              />
            </div>
          </div>

          <!-- 时间信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 签到时间（只读） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-sign-in-alt mr-2"></i>
                签到时间
              </label>
              <input
                  :value="formatDateTimeDisplay(formData.check_in_time) || '--'"
                  type="text"
                  readonly
                  class="w-full px-4 py-3 bg-slate-50 border-2 border-slate-200 rounded-xl text-slate-500"
              />
            </div>

            <!-- 签退时间（可编辑） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-sign-out-alt mr-2"></i>
                签退时间
              </label>
              <input
                  v-model="formData.check_out_time"
                  type="datetime-local"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                  :class="errors.check_out_time ? 'border-red-500' : ''"
                  @change="validateCheckOutTime"
              />
              <p v-if="errors.check_out_time" class="mt-2 text-sm text-red-600">{{ errors.check_out_time }}</p>
              <p class="mt-2 text-sm text-blue-600">
                <i class="fas fa-pencil-alt mr-1"></i>
                可修改此字段
              </p>
            </div>
          </div>

          <!-- 时长和备注 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 预期时长（可编辑） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-clock mr-2"></i>
                预期时长（分钟）
              </label>
              <input
                  v-model.number="formData.duration_minutes"
                  type="number"
                  min="15"
                  max="240"
                  step="15"
                  placeholder="如：90"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                  :class="errors.duration_minutes ? 'border-red-500' : ''"
                  @input="validateField('duration_minutes')"
              />
              <p v-if="errors.duration_minutes" class="mt-2 text-sm text-red-600">{{ errors.duration_minutes }}</p>
              <p class="mt-2 text-sm text-blue-600">
                <i class="fas fa-pencil-alt mr-1"></i>
                可修改此字段
              </p>
            </div>

            <!-- 备注（可编辑） -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-sticky-note mr-2"></i>
                备注
              </label>
              <input
                  v-model="formData.notes"
                  type="text"
                  placeholder="可填写实验注意事项、设备要求等信息"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
              />
              <p class="mt-2 text-sm text-blue-600">
                <i class="fas fa-pencil-alt mr-1"></i>
                可修改此字段
              </p>
            </div>
          </div>

          <!-- 状态信息 -->
          <div class="bg-blue-50 border-2 border-blue-100 rounded-xl p-6">
            <h4 class="font-semibold text-blue-800 mb-3">
              <i class="fas fa-chart-bar mr-2"></i>
              状态统计
            </h4>
            <div class="space-y-4">
              <!-- 当前状态 -->
              <div class="flex justify-between items-center">
                <span class="text-blue-600">当前状态：</span>
                <span :class="['px-3 py-1 rounded-full text-sm font-medium', getStatusClass(formData)]">
                  {{ getStatusText(formData) }}
                </span>
              </div>

              <!-- 时长统计 -->
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="bg-white p-4 rounded-lg border border-blue-200">
                  <div class="text-sm text-blue-600 mb-1">预期时长</div>
                  <div class="text-2xl font-bold text-blue-700">
                    {{ (formData.duration_minutes || 0) }} 分钟
                  </div>
                </div>
                <div class="bg-white p-4 rounded-lg border border-blue-200">
                  <div class="text-sm text-blue-600 mb-1">实际时长</div>
                  <div class="text-2xl font-bold text-blue-700">
                    {{ calculateActualDuration() }} 分钟
                  </div>
                </div>
              </div>

              <!-- 超时警告 -->
              <div v-if="calculateOvertime() > 0" class="mt-4 p-4 bg-red-50 border border-red-200 rounded-lg">
                <div class="flex items-center">
                  <i class="fas fa-exclamation-triangle text-red-500 mr-3 text-xl"></i>
                  <div>
                    <h4 class="font-semibold text-red-700">实验已超时</h4>
                    <p class="text-red-600 text-sm mt-1">
                      实际用时超出预期 {{ calculateOvertime() }} 分钟
                    </p>
                  </div>
                </div>
              </div>

              <!-- 时间建议 -->
              <div v-if="!formData.check_out_time" class="mt-4 p-4 bg-amber-50 border border-amber-200 rounded-lg">
                <div class="flex items-center">
                  <i class="fas fa-info-circle text-amber-500 mr-3 text-xl"></i>
                  <div>
                    <h4 class="font-semibold text-amber-700">实验进行中</h4>
                    <p class="text-amber-600 text-sm mt-1">
                      填写签退时间可将状态更新为"已完成"
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>

      <!-- 对话框底部 -->
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

const showError = (message) => {
  console.error(' Error:', message)
  alert(`错误: ${message}`)
}

const props = defineProps({
  visible: Boolean,
  lab: Object
})

const emit = defineEmits(['close', 'save'])

// 状态
const loading = ref(false)
const isSaving = ref(false)

// 表单数据 - 简化版，只包含数据库字段
const formData = reactive({
  attendance_id: '',
  student_id: '',
  lab_id: '',
  lab_name: '',
  experiment_name: '',
  check_in_time: '',
  check_out_time: '',
  duration_minutes: 90,
  notes: '',
  created_at: ''
})

// 表单验证
const errors = reactive({})

// 安全获取数字
const getSafeNumber = (value, defaultValue = 0) => {
  const val = parseFloat(value)
  return !isNaN(val) ? val : defaultValue
}

// 格式化日期时间到datetime-local格式
const formatDateTimeForInput = (dateTime) => {
  if (!dateTime) return ''
  try {
    const date = new Date(dateTime)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day}T${hours}:${minutes}`
  } catch {
    return ''
  }
}

// 格式化日期显示（用于只读字段）
const formatDateTimeDisplay = (dateTime) => {
  if (!dateTime) return ''
  console.log('格式化显示的时间:', dateTime)

  try {
    const date = new Date(dateTime)
    if (isNaN(date.getTime())) {
      return dateTime  // 返回原始值
    }

    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false
    })
  } catch {
    console.log('格式化失败，返回原始值:', dateTime)
    return dateTime
  }
}

// 格式化日期显示
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

// 计算实际时长
const calculateActualDuration = () => {
  if (!formData.check_in_time || !formData.check_out_time) return 0

  try {
    const checkInTime = new Date(formData.check_in_time)
    const checkOutTime = new Date(formData.check_out_time)
    const durationMs = checkOutTime - checkInTime
    return Math.round(durationMs / (1000 * 60))
  } catch {
    return 0
  }
}

// 计算超时时间
const calculateOvertime = () => {
  if (!formData.check_in_time || !formData.check_out_time || !formData.duration_minutes) return 0

  try {
    const checkInTime = new Date(formData.check_in_time)
    const checkOutTime = new Date(formData.check_out_time)
    const expectedEndTime = new Date(checkInTime.getTime() + (formData.duration_minutes * 60 * 1000))

    if (checkOutTime > expectedEndTime) {
      const overtimeMs = checkOutTime - expectedEndTime
      return Math.round(overtimeMs / (1000 * 60))
    }
  } catch {
    return 0
  }

  return 0
}

// 获取状态样式
const getStatusClass = (lab) => {
  const checkOutTime = lab.check_out_time
  if (checkOutTime) {
    const overtime = calculateOvertime()
    if (overtime > 0) {
      return 'bg-gradient-to-r from-red-400 to-pink-500 text-white'
    }
    return 'bg-gradient-to-r from-emerald-400 to-teal-500 text-white'
  }
  return 'bg-gradient-to-r from-blue-400 to-indigo-500 text-white'
}

// 获取状态文本
const getStatusText = (lab) => {
  const checkOutTime = lab.check_out_time
  if (checkOutTime) {
    const overtime = calculateOvertime()
    if (overtime > 0) {
      return '已超时'
    }
    return '已完成'
  }
  return '进行中'
}

// 表单验证规则（只验证可编辑字段）
const validationRules = {
  check_out_time: (value) => {
    if (!value) return null // 签退时间可以为空

    if (!formData.check_in_time) {
      return '请先填写签到时间'
    }

    const checkInTime = new Date(formData.check_in_time)
    const checkOutTime = new Date(value)

    if (checkOutTime < checkInTime) {
      return '签退时间不能早于签到时间'
    }

    const now = new Date()
    if (checkOutTime > now) {
      return '签退时间不能晚于当前时间'
    }

    return null
  },
  duration_minutes: (value) => {
    if (!value && value !== 0) return '预期时长不能为空'
    const num = parseFloat(value)
    if (isNaN(num) || num < 15 || num > 240) return '请输入15-240分钟之间的时长'
    return null
  }
}

// 验证签退时间
const validateCheckOutTime = () => {
  const rule = validationRules.check_out_time
  const error = rule(formData.check_out_time)

  if (error) {
    errors.check_out_time = error
  } else {
    delete errors.check_out_time
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
    if (field === 'check_out_time') {
      validateCheckOutTime()
    } else {
      validateField(field)
    }
  })
  return Object.keys(errors).length === 0
}

// 表单是否有效
const isFormValid = computed(() => {
  return Object.keys(validationRules).every(field => {
    if (field === 'check_out_time') return true // 签退时间不是必填

    const value = formData[field]
    if (!value && value !== 0) return false

    return !validationRules[field](value)
  })
})

// 关闭对话框
const close = () => {
  emit('close')
}

// 保存数据
const save = async () => {
  if (!validateForm()) {
    showError('请填写正确的信息')
    return
  }

  isSaving.value = true
  try {
    console.log('保存前的表单数据:', formData)

    const labData = {
      attendanceId: formData.attendance_id,
      studentId: formData.student_id,
      labId: formData.lab_id,
      labName: formData.lab_name,
      experimentName: formData.experiment_name,
      checkInTime: formData.check_in_time, // 使用原始值
      checkOutTime: formData.check_out_time || null,
      durationMinutes: formData.duration_minutes,
      notes: formData.notes || ''
    }

    console.log('准备保存的数据:', labData)
    console.log('签退时间:', labData.checkOutTime)

    // 验证关键字段
    if (!labData.attendanceId) {
      showError('缺少记录ID')
      isSaving.value = false
      return
    }

    if (!labData.checkInTime) {
      showError('签到时间不能为空')
      isSaving.value = false
      return
    }

    emit('save', labData)
  } catch (err) {
    console.error('更新失败:', err)
    showError('保存失败: ' + err.message)
    isSaving.value = false
  }
}

// 监听传入的实验室数据
watch(() => props.lab, (lab) => {
  if (lab) {
    loading.value = false

    console.log('收到的原始数据:', lab)

    // 映射字段 - 处理可能的驼峰/下划线命名
    formData.attendance_id = lab.attendanceId || lab.attendance_id || lab.id || ''
    formData.student_id = lab.studentId || lab.student_id || ''
    formData.lab_id = lab.labId || lab.lab_id || ''
    formData.lab_name = lab.labName || lab.lab_name || ''
    formData.experiment_name = lab.experimentName || lab.experiment_name || ''

    // 处理时间字段
    let checkInTime = lab.checkInTime || lab.check_in_time || ''
    let checkOutTime = lab.checkOutTime || lab.check_out_time || ''

    // 确保时间字段不为空
    console.log('签到时间:', checkInTime)
    console.log('签退时间:', checkOutTime)

    // 格式化时间为datetime-local格式
    const formatForDateTimeLocal = (timeString) => {
      if (!timeString) return ''

      try {
        const date = new Date(timeString)
        // 检查日期是否有效
        if (isNaN(date.getTime())) {
          console.warn('无效的日期:', timeString)
          return ''
        }

        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')

        return `${year}-${month}-${day}T${hours}:${minutes}`
      } catch (error) {
        console.error('时间格式化错误:', error)
        return ''
      }
    }

    formData.check_in_time = checkInTime  // 保持原始值用于显示
    formData.check_out_time = formatForDateTimeLocal(checkOutTime)
    formData.duration_minutes = getSafeNumber(lab.durationMinutes || lab.duration_minutes, 90)
    formData.notes = lab.notes || ''
    formData.created_at = lab.createdAt || lab.created_at || ''

    console.log('加载的表单数据:', formData)
  } else {
    loading.value = true
  }
}, { immediate: true })

// 监听对话框关闭时重置表单
watch(() => props.visible, (visible) => {
  if (!visible) {
    loading.value = true
    Object.keys(formData).forEach(key => {
      if (key === 'duration_minutes') {
        formData[key] = 90
      } else {
        formData[key] = ''
      }
    })
    Object.keys(errors).forEach(key => {
      delete errors[key]
    })
  }
})
</script>