<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl w-full max-w-2xl flex flex-col shadow-2xl max-h-[90vh]">
      <!-- 对话框头部 -->
      <div class="px-8 py-6 bg-gradient-to-r from-indigo-500 to-purple-500 text-white flex-shrink-0">
        <div class="flex items-center justify-between">
          <h2 class="text-2xl font-bold">
            <i class="fas fa-plus-circle mr-3"></i>
            添加实验室记录
          </h2>
          <button
              @click="close"
              class="text-white hover:text-gray-200 transition-colors"
          >
            <i class="fas fa-times text-2xl"></i>
          </button>
        </div>
      </div>

      <!-- 表单内容 -->
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

            <!-- 实验室ID -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-hashtag mr-2"></i>
                实验室ID *
              </label>
              <div class="relative">
                <input
                    v-model="formData.lab_id"
                    type="text"
                    placeholder="请输入或选择实验室ID"
                    class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                    :class="errors.lab_id ? 'border-red-500' : ''"
                    @input="handleLabIdInput"
                    list="labSuggestions"
                />
                <!-- 自动完成建议 -->
                <datalist id="labSuggestions">
                  <option v-for="lab in labInfoMapArray" :key="lab.lab_id" :value="lab.lab_id">
                    {{ lab.lab_name }}
                  </option>
                </datalist>
              </div>
              <p v-if="errors.lab_id" class="mt-2 text-sm text-red-600">{{ errors.lab_id }}</p>
            </div>
          </div>

          <!-- 实验室信息（自动填充） -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 实验室名称 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-flask mr-2"></i>
                实验室名称 *
              </label>
              <input
                  v-model="formData.lab_name"
                  type="text"
                  placeholder="将根据实验室ID自动填充"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.lab_name ? 'border-red-500' : ''"
                  readonly
              />
              <p v-if="errors.lab_name" class="mt-2 text-sm text-red-600">{{ errors.lab_name }}</p>
            </div>

            <!-- 实验项目 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-vial mr-2"></i>
                实验项目
              </label>
              <input
                  v-model="formData.experiment_name"
                  type="text"
                  placeholder="将根据实验室ID自动填充"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.experiment_name ? 'border-red-500' : ''"
                  readonly
              />
              <p v-if="errors.experiment_name" class="mt-2 text-sm text-red-600">{{ errors.experiment_name }}</p>
            </div>
          </div>

          <!-- 时间信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 签到时间 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-sign-in-alt mr-2"></i>
                签到时间 *
              </label>
              <input
                  v-model="formData.check_in_time"
                  type="datetime-local"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.check_in_time ? 'border-red-500' : ''"
                  @change="validateField('check_in_time')"
              />
              <p v-if="errors.check_in_time" class="mt-2 text-sm text-red-600">{{ errors.check_in_time }}</p>
            </div>

            <!-- 签退时间 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-sign-out-alt mr-2"></i>
                签退时间
              </label>
              <input
                  v-model="formData.check_out_time"
                  type="datetime-local"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.check_out_time ? 'border-red-500' : ''"
                  @change="validateCheckOutTime"
              />
              <p v-if="errors.check_out_time" class="mt-2 text-sm text-red-600">{{ errors.check_out_time }}</p>
              <p class="mt-2 text-sm text-slate-500">
                * 签退时间可不填，表示实验仍在进行中
              </p>
            </div>
          </div>

          <!-- 时长和备注 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 预期时长 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-clock mr-2"></i>
                预期时长（分钟） *
              </label>
              <input
                  v-model.number="formData.duration_minutes"
                  type="number"
                  min="15"
                  max="240"
                  step="15"
                  placeholder="如：90"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.duration_minutes ? 'border-red-500' : ''"
                  @input="validateField('duration_minutes')"
              />
              <p v-if="errors.duration_minutes" class="mt-2 text-sm text-red-600">{{ errors.duration_minutes }}</p>
            </div>

            <!-- 备注 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-sticky-note mr-2"></i>
                备注
              </label>
              <input
                  v-model="formData.notes"
                  type="text"
                  placeholder="可填写实验注意事项、设备要求等信息"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
              />
            </div>
          </div>

          <!-- 提示信息 -->
          <div class="bg-blue-50 border-2 border-blue-100 rounded-xl p-6">
            <h4 class="font-semibold text-blue-800 mb-3">
              <i class="fas fa-lightbulb mr-2"></i>
              操作提示
            </h4>
            <ul class="space-y-2 text-sm text-blue-600">
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                输入实验室ID后，系统会自动填充实验室名称和实验项目
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                签退时间不填时，系统会自动标记为"进行中"状态
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                系统会根据签到和签退时间自动计算实际实验时长
              </li>
            </ul>
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
              'px-8 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl transition-all duration-300',
              isSaving || !isFormValid ? 'opacity-50 cursor-not-allowed' : 'hover:shadow-lg hover:scale-105'
            ]"
          >
            <i v-if="isSaving" class="fas fa-spinner fa-spin mr-2"></i>
            <i v-else class="fas fa-save mr-2"></i>
            {{ isSaving ? '添加中...' : '添加记录' }}
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
  visible: Boolean
})

const emit = defineEmits(['close', 'save'])

// 状态
const isSaving = ref(false)
const showInfoMap = ref(false)

// 根据图片内容设置的实验室信息映射表
const labInfoMap = {
  'LAB001': {
    lab_name: '计算机基础实验室',
    experiment_name: '数据结构实验'
  },
  'LAB002': {
    lab_name: '网络工程实验室',
    experiment_name: '网络协议分析实验'
  },
  'LAB003': {
    lab_name: '软件工程实验室',
    experiment_name: 'Web应用开发实验'
  },
  'LAB004': {
    lab_name: '人工智能实验室',
    experiment_name: '机器学习实验'
  },
  'LAB005': {
    lab_name: '数据库实验室',
    experiment_name: '数据库设计实验'
  }
}

// 映射表数组形式，用于表格显示
const labInfoMapArray = computed(() => {
  return Object.keys(labInfoMap).map(labId => ({
    lab_id: labId,
    lab_name: labInfoMap[labId].lab_name,
    experiment_name: labInfoMap[labId].experiment_name
  })).sort((a, b) => a.lab_id.localeCompare(b.lab_id))
})

// 自动填充信息
const autoFillInfo = reactive({
  show: false,
  labName: '',
  experimentName: ''
})

// 表单数据 - 简化版，只包含数据库字段
const formData = reactive({
  student_id: '',
  lab_id: '',
  lab_name: '',
  experiment_name: '',
  check_in_time: '',
  check_out_time: '',
  duration_minutes: 90,
  notes: ''
})

// 表单验证
const errors = reactive({})

// 安全获取数字
const getSafeNumber = (value, defaultValue = 0) => {
  const val = parseFloat(value)
  return !isNaN(val) ? val : defaultValue
}

// 处理实验室ID输入
const handleLabIdInput = () => {
  validateField('lab_id')

  const labId = formData.lab_id.trim().toUpperCase()

  if (labId && labInfoMap[labId]) {
    // 自动填充实验室信息
    const labInfo = labInfoMap[labId]
    formData.lab_name = labInfo.lab_name
    formData.experiment_name = labInfo.experiment_name

    // 显示自动填充提示
    autoFillInfo.show = true
    autoFillInfo.labName = labInfo.lab_name
    autoFillInfo.experimentName = labInfo.experiment_name

    // 3秒后隐藏提示
    setTimeout(() => {
      autoFillInfo.show = false
    }, 3000)

    // 自动验证相关字段
    validateField('lab_name')
  } else if (labId) {
    // 清除其他字段
    formData.lab_name = ''
    formData.experiment_name = ''
    autoFillInfo.show = false
  }
}

// 从映射表选择实验室
const selectLab = (lab) => {
  formData.lab_id = lab.lab_id
  formData.lab_name = lab.lab_name
  formData.experiment_name = lab.experiment_name

  // 显示自动填充提示
  autoFillInfo.show = true
  autoFillInfo.labName = lab.lab_name
  autoFillInfo.experimentName = lab.experiment_name

  // 3秒后隐藏提示
  setTimeout(() => {
    autoFillInfo.show = false
  }, 3000)

  // 自动验证相关字段
  validateField('lab_id')
  validateField('lab_name')

  // 折叠映射表
  showInfoMap.value = false
}

// 表单验证规则
const validationRules = {
  student_id: (value) => {
    if (!value) return '学号不能为空'
    if (!/^\d{8,10}$/.test(value)) return '请输入8-10位数字学号'
    return null
  },
  lab_id: (value) => {
    if (!value) return '实验室ID不能为空'
    return null
  },
  lab_name: (value) => {
    if (!value) return '实验室名称不能为空'
    return null
  },
  check_in_time: (value) => {
    if (!value) return '签到时间不能为空'

    const checkInTime = new Date(value)
    const now = new Date()

    if (checkInTime > now) {
      return '签到时间不能晚于当前时间'
    }

    return null
  },
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
    showError('请填写完整且正确的信息')
    return
  }

  isSaving.value = true
  try {
    const labData = {
      studentId: formData.student_id,
      labId: formData.lab_id,
      labName: formData.lab_name,
      experimentName: formData.experiment_name,
      checkInTime: formData.check_in_time,
      checkOutTime: formData.check_out_time || null,
      durationMinutes: formData.duration_minutes,
      notes: formData.notes || ''
    }

    console.log('添加实验室数据:', labData)
    emit('save', labData)
  } catch (err) {
    console.error('添加失败:', err)
    isSaving.value = false
  }
}

// 监听对话框关闭时重置表单
watch(() => props.visible, (visible) => {
  if (!visible) {
    // 重置表单数据
    Object.keys(formData).forEach(key => {
      if (key === 'duration_minutes') {
        formData[key] = 90
      } else {
        formData[key] = ''
      }
    })

    // 清除错误信息
    Object.keys(errors).forEach(key => {
      delete errors[key]
    })

    // 重置状态
    autoFillInfo.show = false
    showInfoMap.value = false
  } else {
    // 打开对话框时设置默认签到时间为当前时间
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')
    const hours = String(now.getHours()).padStart(2, '0')
    const minutes = String(now.getMinutes()).padStart(2, '0')

    formData.check_in_time = `${year}-${month}-${day}T${hours}:${minutes}`
  }
})
</script>