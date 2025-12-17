<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl w-full max-w-2xl flex flex-col shadow-2xl max-h-[90vh]">
      <!-- 对话框头部 -->
      <div class="px-8 py-6 bg-gradient-to-r from-blue-500 to-indigo-500 text-white flex-shrink-0">
        <div class="flex items-center justify-between">
          <h2 class="text-2xl font-bold">
            <i class="fas fa-edit mr-3"></i>
            编辑学生信息
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
          <!-- 学号和姓名 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学号 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-id-card mr-2"></i>
                学号
              </label>
              <input
                  :value="studentId || '--'"
                  type="text"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                  readonly
                  disabled
              />
            </div>

            <!-- 姓名 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-user mr-2"></i>
                姓名
              </label>
              <input
                  :value="name || '--'"
                  type="text"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                  readonly
                  disabled
              />
            </div>
          </div>

          <!-- 用户名和角色 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 用户名 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-user-circle mr-2"></i>
                用户名
              </label>
              <input
                  :value="username || '--'"
                  type="text"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                  readonly
                  disabled
              />
            </div>

            <!-- 角色 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-user-tag mr-2"></i>
                角色
              </label>
              <input
                  value="学生"
                  type="text"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                  readonly
                  disabled
              />
            </div>
          </div>

          <!-- 学院和专业 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学院 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-building mr-2"></i>
                学院 *
              </label>
              <select
                  v-model="collegeInput"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                  :class="errors.college ? 'border-red-500' : ''"
                  @change="onCollegeChange"
              >
                <option value="">请选择学院</option>
                <option v-for="college in collegeOptions" :key="college" :value="college">{{ college }}</option>
              </select>
              <p v-if="errors.college" class="mt-2 text-sm text-red-600">{{ errors.college }}</p>
            </div>

            <!-- 专业 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-graduation-cap mr-2"></i>
                专业 *
              </label>
              <select
                  v-model="majorInput"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                  :class="errors.major ? 'border-red-500' : ''"
                  @change="validateField('major')"
                  :disabled="!collegeInput"
              >
                <option value="">请先选择学院</option>
                <option v-for="major in filteredMajorOptions" :key="major" :value="major">{{ major }}</option>
              </select>
              <p v-if="errors.major" class="mt-2 text-sm text-red-600">{{ errors.major }}</p>
            </div>
          </div>

          <!-- 班级和性别 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 班级 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-users mr-2"></i>
                班级
              </label>
              <input
                  v-model="classNameInput"
                  type="text"
                  placeholder="请输入班级"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
              />
            </div>

            <!-- 性别 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-venus-mars mr-2"></i>
                性别
              </label>
              <select
                  v-model="genderInput"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
              >
                <option value="">请选择性别</option>
                <option value="M">男</option>
                <option value="F">女</option>
              </select>
            </div>
          </div>

          <!-- 联系信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 邮箱 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-envelope mr-2"></i>
                邮箱
              </label>
              <input
                  v-model="emailInput"
                  type="email"
                  placeholder="请输入邮箱"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                  :class="errors.email ? 'border-red-500' : ''"
                  @input="validateField('email')"
              />
              <p v-if="errors.email" class="mt-2 text-sm text-red-600">{{ errors.email }}</p>
            </div>

            <!-- 电话 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-phone mr-2"></i>
                电话
              </label>
              <input
                  v-model="phoneInput"
                  type="tel"
                  placeholder="请输入电话"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                  :class="errors.phone ? 'border-red-500' : ''"
                  @input="validateField('phone')"
              />
              <p v-if="errors.phone" class="mt-2 text-sm text-red-600">{{ errors.phone }}</p>
            </div>
          </div>

          <!-- 入学日期 -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              <i class="fas fa-calendar-alt mr-2"></i>
              入学日期
            </label>
            <input
                v-model="enrollmentDateInput"
                type="date"
                class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                :class="errors.enrollmentDate ? 'border-red-500' : ''"
                @change="validateField('enrollmentDate')"
            />
            <p v-if="errors.enrollmentDate" class="mt-2 text-sm text-red-600">{{ errors.enrollmentDate }}</p>
          </div>

          <!-- 账户状态 -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              <i class="fas fa-tasks mr-2"></i>
              账户状态 *
            </label>
            <select
                v-model="accountStatusInput"
                class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                :class="errors.accountStatus ? 'border-red-500' : ''"
                @change="validateField('accountStatus')"
            >
              <option value="ACTIVE">正常</option>
              <option value="INACTIVE">禁用</option>
              <option value="PENDING">待审核</option>
            </select>
            <p v-if="errors.accountStatus" class="mt-2 text-sm text-red-600">{{ errors.accountStatus }}</p>
          </div>

          <!-- 提示信息 -->
          <div class="bg-blue-50 border-2 border-blue-100 rounded-xl p-6">
            <h4 class="font-semibold text-blue-800 mb-3">
              <i class="fas fa-lightbulb mr-2"></i>
              编辑提示
            </h4>
            <ul class="space-y-2 text-sm text-blue-600">
              <li class="flex items-start">
                <i class="fas fa-info-circle mt-1 mr-2 text-blue-500"></i>
                学号、姓名、用户名和角色不可更改
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                先选择学院，专业会根据学院自动筛选
              </li>
              <li class="flex items-start">
                <i class="fas fa-exclamation-circle mt-1 mr-2 text-amber-500"></i>
                禁用账户后，学生将无法登录系统
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                带有 * 号的字段为必填项
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
                'px-8 py-3 bg-gradient-to-r from-blue-500 to-indigo-500 text-white font-semibold rounded-xl transition-all duration-300',
                isSaving || !isFormValid ? 'opacity-50 cursor-not-allowed' : 'hover:shadow-lg hover:scale-105'
              ]"
          >
            <i v-if="isSaving" class="fas fa-spinner fa-spin mr-2"></i>
            <i v-else class="fas fa-save mr-2"></i>
            {{ isSaving ? '保存中...' : '保存修改' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const showError = (message) => {
  console.error('Error:', message)
  alert(`错误: ${message}`)
}

const props = defineProps({
  visible: Boolean,
  studentData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['close', 'save'])

// 状态
const isSaving = ref(false)

// 直接从 props 提取数据
const studentId = computed(() => props.studentData?.studentId || props.studentData?.student_id || '')
const name = computed(() => props.studentData?.name || '')
const username = computed(() => props.studentData?.username || '')

// 可编辑的字段
const collegeInput = ref('')
const majorInput = ref('')
const classNameInput = ref('')
const genderInput = ref('')
const emailInput = ref('')
const phoneInput = ref('')
const enrollmentDateInput = ref('')
const accountStatusInput = ref('ACTIVE')

// 表单验证
const errors = ref({})

// 学院和专业映射数据
const collegeMajorMapping = {
  // 工学院
  '01-计算机科学与技术学院': [
    '01-计算机科学与技术',
    '02-软件工程',
    '03-网络工程',
    '04-信息安全'
  ],
  '02-电子信息学院': [
    '01-电子信息工程',
    '02-通信工程',
    '03-电子科学与技术',
    '04-光电信息科学与工程'
  ],
  '03-机械工程学院': [
    '01-机械工程',
    '02-机械设计制造及其自动化',
    '03-车辆工程',
    '04-工业工程'
  ],
  '04-电气工程学院': [
    '01-电气工程及其自动化',
    '02-自动化',
    '03-测控技术与仪器',
    '04-智能电网信息工程'
  ],
  '05-建筑工程学院': [
    '01-土木工程',
    '02-建筑学',
    '03-城乡规划',
    '04-工程管理'
  ],
  '06-材料科学与工程学院': [
    '01-材料科学与工程',
    '02-高分子材料与工程',
    '03-金属材料工程',
    '04-材料物理'
  ],
  '07-化学化工学院': [
    '01-化学工程与工艺',
    '02-应用化学',
    '03-制药工程',
    '04-材料化学'
  ],
  '08-环境与资源学院': [
    '01-环境工程',
    '02-资源循环科学与工程',
    '03-环境科学',
    '04-安全工程'
  ],

  // 理学院
  '09-数学与统计学院': [
    '01-数学与应用数学',
    '02-信息与计算科学',
    '03-统计学',
    '04-数据科学与大数据技术'
  ],
  '10-物理学院': [
    '01-物理学',
    '02-应用物理学',
    '03-光电信息科学与工程',
    '04-核物理'
  ],
  '11-化学学院': [
    '01-化学',
    '02-应用化学',
    '03-材料化学',
    '04-化学生物学'
  ],

  // 人文社科
  '12-经济管理学院': [
    '01-经济学',
    '02-金融学',
    '03-国际经济与贸易',
    '04-工商管理',
    '05-会计学',
    '06-财务管理',
    '07-市场营销',
    '08-人力资源管理'
  ],
  '13-法学院': [
    '01-法学',
    '02-知识产权',
    '03-社会学',
    '04-社会工作'
  ],
  '14-外国语学院': [
    '01-英语',
    '02-日语',
    '03-德语',
    '04-法语',
    '05-翻译',
    '06-商务英语'
  ],
  '15-文学院': [
    '01-汉语言文学',
    '02-汉语国际教育',
    '03-新闻学',
    '04-广播电视学',
    '05-广告学'
  ],
  '16-教育学院': [
    '01-教育学',
    '02-学前教育',
    '03-小学教育',
    '04-教育技术学'
  ],

  // 其他学院
  '17-艺术学院': [
    '01-音乐学',
    '02-美术学',
    '03-设计学',
    '04-舞蹈学',
    '05-戏剧影视文学'
  ],
  '18-医学院': [
    '01-临床医学',
    '02-口腔医学',
    '03-护理学',
    '04-药学',
    '05-医学检验技术'
  ],
  '19-体育学院': [
    '01-体育教育',
    '02-运动训练',
    '03-社会体育指导与管理',
    '04-运动康复'
  ]
}

// 学院选项
const collegeOptions = computed(() => {
  return Object.keys(collegeMajorMapping).sort()
})

// 根据所选学院筛选专业选项
const filteredMajorOptions = computed(() => {
  if (!collegeInput.value || !collegeMajorMapping[collegeInput.value]) {
    return []
  }
  return collegeMajorMapping[collegeInput.value].sort()
})

// 学院变更处理
const onCollegeChange = () => {
  // 清除已选择的专业
  majorInput.value = ''
  // 验证学院字段
  validateField('college')
}

// 格式化日期为输入框格式
const formatDateForInput = (dateValue) => {
  if (!dateValue || dateValue === 'null' || dateValue === 'NULL') {
    return ''
  }

  try {
    // 如果已经是YYYY-MM-DD格式，直接返回
    if (/^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
      return dateValue
    }

    const date = new Date(dateValue)
    if (isNaN(date.getTime())) {
      return ''
    }

    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  } catch (error) {
    console.error('日期格式化错误:', error, '原始值:', dateValue)
    return ''
  }
}

// 表单验证规则
const validationRules = {
  college: (value) => {
    if (!value) return '请选择学院'
    return null
  },
  major: (value) => {
    if (!value) return '请选择专业'
    return null
  },
  email: (value) => {
    if (value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
      return '请输入有效的邮箱地址'
    }
    return null
  },
  phone: (value) => {
    if (value && !/^1[3-9]\d{9}$/.test(value)) {
      return '请输入有效的手机号码'
    }
    return null
  },
  enrollmentDate: (value) => {
    if (value) {
      const date = new Date(value)
      const now = new Date()
      if (date > now) {
        return '入学日期不能晚于当前日期'
      }
    }
    return null
  },
  accountStatus: (value) => {
    if (!value) return '请选择账户状态'
    return null
  }
}

// 验证字段
const validateField = (field) => {
  const rule = validationRules[field]
  if (rule) {
    const value = eval(`${field}Input.value`)
    const error = rule(value)
    if (error) {
      errors.value[field] = error
    } else {
      delete errors.value[field]
    }
  }
}

// 表单是否有效
const isFormValid = computed(() => {
  return Object.keys(validationRules).every(field => {
    // 邮箱和电话不是必填
    if (field === 'email' || field === 'phone' || field === 'enrollmentDate') return true

    const value = eval(`${field}Input.value`)
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
  // 验证
  Object.keys(validationRules).forEach(field => {
    validateField(field)
  })

  if (Object.keys(errors.value).length > 0) {
    showError('请填写完整且正确的信息')
    return
  }

  isSaving.value = true
  try {
    const studentData = {
      id: props.studentData?.id,
      studentId: studentId.value,
      name: name.value,
      username: username.value,
      college: collegeInput.value,
      major: majorInput.value,
      className: classNameInput.value || null,
      gender: genderInput.value || null,
      email: emailInput.value || null,
      phone: phoneInput.value || null,
      enrollmentDate: enrollmentDateInput.value || null,
      accountStatus: accountStatusInput.value
    }

    emit('save', studentData)
  } catch (err) {
    console.error('更新失败:', err)
    isSaving.value = false
  }
}

// 初始化表单数据
const initFormData = () => {
  if (props.studentData) {
    collegeInput.value = props.studentData?.college || ''
    majorInput.value = props.studentData?.major || ''
    classNameInput.value = props.studentData?.className || props.studentData?.class_name || ''
    genderInput.value = props.studentData?.gender || ''
    emailInput.value = props.studentData?.email || ''
    phoneInput.value = props.studentData?.phone || ''
    enrollmentDateInput.value = formatDateForInput(
        props.studentData?.enrollmentDate || props.studentData?.enrollment_date
    )
    accountStatusInput.value = props.studentData?.accountStatus || props.studentData?.account_status || 'ACTIVE'
  }
}

// 监听对话框打开时初始化数据
watch(() => props.visible, (visible) => {
  if (visible) {
    initFormData()
  } else {
    // 重置数据
    collegeInput.value = ''
    majorInput.value = ''
    classNameInput.value = ''
    genderInput.value = ''
    emailInput.value = ''
    phoneInput.value = ''
    enrollmentDateInput.value = ''
    accountStatusInput.value = 'ACTIVE'
    errors.value = {}
  }
})

// 监听studentData变化
watch(() => props.studentData, (newStudentData) => {
  if (props.visible && newStudentData) {
    initFormData()
  }
}, { deep: true })

// 立即执行一次初始化
if (props.visible && props.studentData) {
  initFormData()
}
</script>