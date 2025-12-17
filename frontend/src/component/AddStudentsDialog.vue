<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl w-full max-w-2xl flex flex-col shadow-2xl max-h-[90vh]">
      <!-- 对话框头部 -->
      <div class="px-8 py-6 bg-gradient-to-r from-indigo-500 to-purple-500 text-white flex-shrink-0">
        <div class="flex items-center justify-between">
          <h2 class="text-2xl font-bold">
            <i class="fas fa-plus-circle mr-3"></i>
            添加学生
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
          <!-- 基本信息和账户信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学号 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-id-card mr-2"></i>
                学号 *
              </label>
              <input
                  v-model="formData.studentId"
                  type="text"
                  placeholder="请输入学号"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.studentId ? 'border-red-500' : ''"
                  @input="validateField('studentId')"
              />
              <p v-if="errors.studentId" class="mt-2 text-sm text-red-600">{{ errors.studentId }}</p>
            </div>

            <!-- 姓名 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-user mr-2"></i>
                姓名 *
              </label>
              <input
                  v-model="formData.name"
                  type="text"
                  placeholder="请输入姓名"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.name ? 'border-red-500' : ''"
                  @input="validateField('name')"
              />
              <p v-if="errors.name" class="mt-2 text-sm text-red-600">{{ errors.name }}</p>
            </div>
          </div>

          <!-- 用户名和密码 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 用户名 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-user-circle mr-2"></i>
                用户名 *
              </label>
              <input
                  v-model="formData.username"
                  type="text"
                  placeholder="请输入用户名"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.username ? 'border-red-500' : ''"
                  @input="validateField('username')"
              />
              <p v-if="errors.username" class="mt-2 text-sm text-red-600">{{ errors.username }}</p>
            </div>

            <!-- 密码 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-lock mr-2"></i>
                初始密码 *
              </label>
              <input
                  v-model="formData.password"
                  type="password"
                  placeholder="请输入初始密码"
                  autocomplete="new-password"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.password ? 'border-red-500' : ''"
                  @input="validateField('password')"
              />
              <p v-if="errors.password" class="mt-2 text-sm text-red-600">{{ errors.password }}</p>
              <p class="mt-1 text-xs text-slate-500">密码长度6-20位，建议包含字母和数字</p>
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
                  v-model="formData.college"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
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
                  v-model="formData.major"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.major ? 'border-red-500' : ''"
                  @change="validateField('major')"
                  :disabled="!formData.college"
              >
                <option value="">请先选择学院</option>
                <option v-for="major in filteredMajorOptions" :key="major" :value="major">{{ major }}</option>
              </select>
              <p v-if="errors.major" class="mt-2 text-sm text-red-600">{{ errors.major }}</p>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 班级 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-users mr-2"></i>
                班级
              </label>
              <input
                  v-model="formData.className"
                  type="text"
                  placeholder="请输入班级"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
              />
            </div>

            <!-- 年级 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-graduation-cap mr-2"></i>
                年级 *
              </label>
              <select
                  v-model="formData.grade"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.grade ? 'border-red-500' : ''"
                  @change="validateField('grade')"
              >
                <option value="">请选择年级</option>
                <option value="大一">大一</option>
                <option value="大二">大二</option>
                <option value="大三">大三</option>
                <option value="大四">大四</option>
                <option value="大五">大五</option>
              </select>
              <p v-if="errors.grade" class="mt-2 text-sm text-red-600">{{ errors.grade }}</p>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 性别 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-venus-mars mr-2"></i>
                性别
              </label>
              <select
                  v-model="formData.gender"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
              >
                <option value="">请选择性别</option>
                <option value="M">男</option>
                <option value="F">女</option>
              </select>
            </div>
            <!-- 邮箱 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-envelope mr-2"></i>
                邮箱
              </label>
              <input
                  v-model="formData.email"
                  type="email"
                  placeholder="请输入邮箱"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.email ? 'border-red-500' : ''"
                  @input="validateField('email')"
              />
              <p v-if="errors.email" class="mt-2 text-sm text-red-600">{{ errors.email }}</p>
            </div>
          </div>

          <!-- 联系信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 电话 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-phone mr-2"></i>
                电话
              </label>
              <input
                  v-model="formData.phone"
                  type="tel"
                  placeholder="请输入电话"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.phone ? 'border-red-500' : ''"
                  @input="validateField('phone')"
              />
              <p v-if="errors.phone" class="mt-2 text-sm text-red-600">{{ errors.phone }}</p>
            </div>
            <!-- 入学日期 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-calendar-alt mr-2"></i>
                入学日期
              </label>
              <input
                  v-model="formData.enrollmentDate"
                  type="date"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.enrollmentDate ? 'border-red-500' : ''"
                  @change="validateField('enrollmentDate')"
              />
              <p v-if="errors.enrollmentDate" class="mt-2 text-sm text-red-600">{{ errors.enrollmentDate }}</p>
            </div>
          </div>

          <!-- 账户状态 -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              <i class="fas fa-tasks mr-2"></i>
              账户状态 *
            </label>
            <select
                v-model="formData.accountStatus"
                class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
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
              操作提示
            </h4>
            <ul class="space-y-2 text-sm text-blue-600">
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                带有 * 号的字段为必填项
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                先选择学院，专业会根据学院自动筛选
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                学号不能重复，请确保学号的唯一性
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
            {{ isSaving ? '添加中...' : '添加学生' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'

const props = defineProps({
  visible: Boolean
})

const emit = defineEmits(['close', 'save'])

// 状态
const isSaving = ref(false)

// 表单数据
const formData = reactive({
  studentId: '',
  name: '',
  username: '',
  password: '',
  college: '',
  major: '',
  className: '',
  grade: '',
  gender: '',
  email: '',
  phone: '',
  enrollmentDate: '',
  accountStatus: 'ACTIVE'
})

// 表单验证
const errors = reactive({})

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

// 学院选项（从映射数据的键生成）
const collegeOptions = computed(() => {
  return Object.keys(collegeMajorMapping).sort()
})

// 根据所选学院筛选专业选项
const filteredMajorOptions = computed(() => {
  if (!formData.college || !collegeMajorMapping[formData.college]) {
    return []
  }
  return collegeMajorMapping[formData.college].sort()
})

// 学院变更处理
const onCollegeChange = () => {
  // 清除已选择的专业
  formData.major = ''
  // 验证学院字段
  validateField('college')
}

// 表单验证规则
const validationRules = {
  studentId: (value) => {
    if (!value) return '学号不能为空'
    if (!/^\d{8,12}$/.test(value)) return '请输入8-12位数字学号'
    return null
  },
  name: (value) => {
    if (!value) return '姓名不能为空'
    if (value.length < 2 || value.length > 20) return '姓名长度应为2-20位'
    return null
  },
  username: (value) => {
    if (!value) return '用户名不能为空'
    if (!/^[a-zA-Z0-9_]{4,20}$/.test(value)) return '用户名应为4-20位字母、数字或下划线'
    return null
  },
  password: (value) => {
    if (!value) return '密码不能为空'
    if (value.length < 6 || value.length > 20) return '密码长度应为6-20位'
    return null
  },
  college: (value) => {
    if (!value) return '请选择学院'
    return null
  },
  major: (value) => {
    if (!value) return '请选择专业'
    return null
  },
  grade: (value) => {
    if (!value) return '请选择年级'
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
    validateField(field)
  })
  return Object.keys(errors).length === 0
}

// 表单是否有效
const isFormValid = computed(() => {
  return Object.keys(validationRules).every(field => {
    // 邮箱和电话不是必填
    if (field === 'email' || field === 'phone') return true

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
    const studentData = {
      studentId: formData.studentId,
      name: formData.name,
      username: formData.username,
      password: formData.password,
      college: formData.college,
      major: formData.major,
      className: formData.className || null,
      grade: formData.grade,
      gender: formData.gender || null,
      email: formData.email || null,
      phone: formData.phone || null,
      enrollmentDate: formData.enrollmentDate || null,
      accountStatus: formData.accountStatus,
      role: 'STUDENT'
    }

    emit('save', studentData)
  } catch (err) {
    console.error('添加失败:', err)
    isSaving.value = false
  }
}

// 显示错误
const showError = (message) => {
  console.error('Error:', message)
  alert(`错误: ${message}`)
}

// 监听对话框关闭时重置表单
watch(() => props.visible, (visible) => {
  if (!visible) {
    // 重置表单数据
    Object.keys(formData).forEach(key => {
      if (key === 'accountStatus') {
        formData[key] = 'ACTIVE'
      } else if (key === 'enrollmentDate') {
        // 设置默认入学日期为当前年份的9月1日
        const currentYear = new Date().getFullYear()
        formData[key] = `${currentYear}-09-01`
      } else {
        formData[key] = ''
      }
    })

    // 清除错误信息
    Object.keys(errors).forEach(key => {
      delete errors[key]
    })
  } else {
    // 打开对话框时设置默认入学日期
    const currentYear = new Date().getFullYear()
    formData.enrollmentDate = `${currentYear}-09-01`
  }
})
</script>