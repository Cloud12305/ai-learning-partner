<template>
  <!-- 毛玻璃登录/注册悬浮窗 -->
  <div
      v-if="visible"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-dark/30 backdrop-blur-sm transition-opacity duration-300"
      @click="handleClose"
  >
    <div
        class="w-full max-w-2xl bg-white/90 backdrop-blur-xl rounded-2xl p-6 shadow-2xl border border-gray-200 transition-all duration-500 hover:shadow-primary/20 hover:border-primary/30 relative"
        @click.stop
    >
      <!-- 关闭按钮 -->
      <button
          @click="handleClose"
          class="absolute top-4 right-4 text-gray-600 hover:text-gray-900 transition-colors z-10"
      >
        <i class="fa fa-times text-xl"></i>
      </button>

      <!-- 切换标签 -->
      <div class="flex border-b border-gray-200 mb-6">
        <button
            @click="setMode('login')"
            class="flex-1 py-4 text-center font-medium transition-colors"
            :class="currentMode === 'login' ? 'text-primary border-b-2 border-primary' : 'text-gray-600 hover:text-gray-900'"
        >
          登录
        </button>
        <button
            @click="setMode('register')"
            class="flex-1 py-4 text-center font-medium transition-colors"
            :class="currentMode === 'register' ? 'text-primary border-b-2 border-primary' : 'text-gray-600 hover:text-gray-900'"
        >
          注册
        </button>
      </div>

      <!-- 登录表单 -->
      <div v-if="currentMode === 'login'" class="max-h-[70vh] overflow-y-auto">
        <div class="text-center mb-6">
          <h3 class="text-2xl font-bold text-gray-900 mb-2">欢迎回来</h3>
          <p class="text-gray-700">请输入账号密码 登录AI 智能·学习搭子系统</p>
        </div>

        <!-- 错误提示 -->
        <div v-if="loginError" class="bg-red-50 border border-red-200 rounded-lg p-3 mb-4">
          <p class="text-red-700 text-sm text-center">{{ loginError }}</p>
        </div>

        <form @submit.prevent="handleLogin" class="space-y-5">
          <div class="form-group">
            <label class="block text-gray-700 mb-2">学号/工号</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-user"></i>
              </span>
              <input
                  v-model="loginForm.username"
                  type="text"
                  placeholder="请输入账号"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  required
              >
            </div>
          </div>

          <div class="form-group">
            <label class="block text-gray-700 mb-2">密码</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-lock"></i>
              </span>
              <input
                  v-model="loginForm.password"
                  :type="showLoginPassword ? 'text' : 'password'"
                  placeholder="请输入密码"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  required
              >
              <button
                  type="button"
                  class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-900 transition-colors"
                  @click="showLoginPassword = !showLoginPassword"
              >
                <i :class="showLoginPassword ? 'fa fa-eye' : 'fa fa-eye-slash'"></i>
              </button>
            </div>
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <input
                  type="checkbox"
                  id="rememberMe"
                  v-model="rememberMe"
                  class="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded"
              >
              <label for="rememberMe" class="ml-2 text-sm text-gray-700">记住我</label>
            </div>
            <a href="#" class="text-sm text-primary hover:text-primary/80">忘记密码？</a>
          </div>

          <div class="form-group">
            <button
                type="submit"
                class="w-full py-3 px-4 bg-gradient-to-r from-primary to-secondary rounded-lg text-white font-medium hover:opacity-90 transform hover:-translate-y-1 active:translate-y-0 transition-all duration-300 shadow-lg shadow-primary/20"
                :disabled="loading"
            >
              {{ loading ? '登录中...' : '登录' }}
            </button>
          </div>
        </form>
      </div>

      <!-- 注册表单 -->
      <div v-if="currentMode === 'register'">
        <div class="text-center mb-6">
          <h3 class="text-2xl font-bold text-gray-900 mb-2">创建账号</h3>
          <p class="text-gray-700">开始使用学业画像系统，助力你的学习</p>
        </div>

        <!-- 错误提示 -->
        <div v-if="loginError" class="bg-red-50 border border-red-200 rounded-lg p-3 mb-4">
          <p class="text-red-700 text-sm text-center">{{ loginError }}</p>
        </div>

        <!-- 注册表单容器，添加滚动 -->
        <div class="max-h-[60vh] overflow-y-auto pr-2 -mr-2">
          <form @submit.prevent="handleRegister" class="space-y-4">
            <!-- 第一行：学号、用户名 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <!-- 学号 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">学号 <span class="text-red-500">*</span></label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-id-card text-sm"></i>
                  </span>
                  <input
                      v-model="registerForm.studentId"
                      type="text"
                      placeholder="请输入学号"
                      class="w-full pl-10 pr-4 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300 text-sm"
                      required
                  >
                </div>
              </div>

              <!-- 用户名 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">用户名 <span class="text-red-500">*</span></label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-user text-sm"></i>
                  </span>
                  <input
                      v-model="registerForm.username"
                      type="text"
                      placeholder="请输入用户名"
                      class="w-full pl-10 pr-4 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300 text-sm"
                      required
                  >
                </div>
              </div>
            </div>

            <!-- 第二行：姓名、邮箱 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <!-- 姓名 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">姓名 <span class="text-red-500">*</span></label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-id-card-o text-sm"></i>
                  </span>
                  <input
                      v-model="registerForm.name"
                      type="text"
                      placeholder="请输入您的姓名"
                      class="w-full pl-10 pr-4 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300 text-sm"
                      required
                  >
                </div>
              </div>

              <!-- 邮箱 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">邮箱</label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-envelope-o text-sm"></i>
                  </span>
                  <input
                      v-model="registerForm.email"
                      type="email"
                      placeholder="请输入您的邮箱"
                      class="w-full pl-10 pr-4 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300 text-sm"
                  >
                </div>
              </div>
            </div>

            <!-- 第三行：密码、确认密码 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <!-- 密码 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">密码 <span class="text-red-500">*</span></label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-lock text-sm"></i>
                  </span>
                  <input
                      v-model="registerForm.password"
                      :type="showRegisterPassword ? 'text' : 'password'"
                      placeholder="请设置密码（至少6位）"
                      class="w-full pl-10 pr-10 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300 text-sm"
                      required
                      minlength="6"
                  >
                  <button
                      type="button"
                      class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-900 transition-colors"
                      @click="showRegisterPassword = !showRegisterPassword"
                  >
                    <i :class="showRegisterPassword ? 'fa fa-eye text-sm' : 'fa fa-eye-slash text-sm'"></i>
                  </button>
                </div>
              </div>

              <!-- 确认密码 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">确认密码 <span class="text-red-500">*</span></label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-lock text-sm"></i>
                  </span>
                  <input
                      v-model="registerForm.confirmPassword"
                      :type="showConfirmPassword ? 'text' : 'password'"
                      placeholder="请再次输入密码"
                      class="w-full pl-10 pr-10 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300 text-sm"
                      required
                  >
                  <button
                      type="button"
                      class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-900 transition-colors"
                      @click="showConfirmPassword = !showConfirmPassword"
                  >
                    <i :class="showConfirmPassword ? 'fa fa-eye text-sm' : 'fa fa-eye-slash text-sm'"></i>
                  </button>
                </div>
              </div>
            </div>

            <!-- 第四行：专业、学院、年级 -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <!-- 专业 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">专业 <span class="text-red-500">*</span></label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-graduation-cap text-sm"></i>
                  </span>
                  <select
                      v-model="registerForm.major"
                      class="w-full pl-10 pr-4 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 transition-all duration-300 text-sm appearance-none"
                      required
                  >
                    <option value="">请选择专业</option>
                    <option value="计算机科学与技术">计算机科学与技术</option>
                    <option value="软件工程">软件工程</option>
                    <option value="人工智能">人工智能</option>
                    <option value="数据科学与大数据技术">数据科学与大数据技术</option>
                    <option value="网络工程">网络工程</option>
                    <option value="信息安全">信息安全</option>
                  </select>
                  <i class="fa fa-chevron-down absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 pointer-events-none text-sm"></i>
                </div>
              </div>

              <!-- 学院 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">学院 <span class="text-red-500">*</span></label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-university text-sm"></i>
                  </span>
                  <select
                      v-model="registerForm.college"
                      class="w-full pl-10 pr-4 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 transition-all duration-300 text-sm appearance-none"
                      required
                  >
                    <option value="">请选择学院</option>
                    <option value="计算机学院">计算机学院</option>
                    <option value="软件学院">软件学院</option>
                    <option value="人工智能学院">人工智能学院</option>
                    <option value="数据科学与工程学院">数据科学与工程学院</option>
                    <option value="网络空间安全学院">网络空间安全学院</option>
                  </select>
                  <i class="fa fa-chevron-down absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 pointer-events-none text-sm"></i>
                </div>
              </div>

              <!-- 年级 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">年级 <span class="text-red-500">*</span></label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-calendar text-sm"></i>
                  </span>
                  <select
                      v-model="registerForm.grade"
                      class="w-full pl-10 pr-4 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 transition-all duration-300 text-sm appearance-none"
                      required
                  >
                    <option value="">请选择年级</option>
                    <option value="2021">2021级</option>
                    <option value="2022">2022级</option>
                    <option value="2023">2023级</option>
                    <option value="2024">2024级</option>
                  </select>
                  <i class="fa fa-chevron-down absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 pointer-events-none text-sm"></i>
                </div>
              </div>
            </div>

            <!-- 第五行：班级、性别 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <!-- 班级 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">班级</label>
                <div class="relative">
                  <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                    <i class="fa fa-users text-sm"></i>
                  </span>
                  <input
                      v-model="registerForm.className"
                      type="text"
                      placeholder="请输入班级（如：计科2301）"
                      class="w-full pl-10 pr-4 py-2.5 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300 text-sm"
                  >
                </div>
              </div>

              <!-- 性别 -->
              <div class="form-group">
                <label class="block text-gray-700 mb-2 text-sm font-medium">性别</label>
                <div class="flex space-x-4 mt-1">
                  <label class="flex items-center">
                    <input type="radio" v-model="registerForm.gender" value="M" class="h-3.5 w-3.5 text-primary focus:ring-primary border-gray-300">
                    <span class="ml-2 text-sm text-gray-700">男</span>
                  </label>
                  <label class="flex items-center">
                    <input type="radio" v-model="registerForm.gender" value="F" class="h-3.5 w-3.5 text-primary focus:ring-primary border-gray-300">
                    <span class="ml-2 text-sm text-gray-700">女</span>
                  </label>
                  <label class="flex items-center">
                    <input type="radio" v-model="registerForm.gender" value="U" class="h-3.5 w-3.5 text-primary focus:ring-primary border-gray-300">
                    <span class="ml-2 text-sm text-gray-700">未知</span>
                  </label>
                </div>
              </div>
            </div>

            <!-- 同意条款 -->
            <div class="flex items-center mt-2">
              <input
                  type="checkbox"
                  id="agreeTerms"
                  v-model="agreeTerms"
                  required
                  class="h-4 w-4 text-primary focus:ring-primary border-gray-300 rounded"
              >
              <label for="agreeTerms" class="ml-2 text-sm text-gray-700">
                我已阅读并同意<a href="#" class="text-primary hover:text-primary/80">服务条款</a>和<a href="#" class="text-primary hover:text-primary/80">隐私政策</a>
              </label>
            </div>
          </form>
        </div>

        <!-- 注册按钮固定在底部 -->
        <div class="mt-6 pt-4 border-t border-gray-200">
          <button
              type="submit"
              @click="handleRegister"
              class="w-full py-3 px-4 bg-gradient-to-r from-primary to-secondary rounded-lg text-white font-medium hover:opacity-90 transform hover:-translate-y-1 active:translate-y-0 transition-all duration-300 shadow-lg shadow-primary/20"
              :disabled="!agreeTerms || loading"
          >
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import apiService from '../services/api'

export default {
  name: 'LoginRegister',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    mode: {
      type: String,
      default: 'login'
    }
  },
  emits: ['close', 'mode-change', 'login-success'],
  setup(props, { emit }) {
    // 移除 const router = useRouter() 这行

    // 表单状态
    const rememberMe = ref(false)
    const agreeTerms = ref(false)
    const loginError = ref('')
    const currentMode = ref(props.mode)
    const loading = ref(false)

    // 密码显示状态
    const showLoginPassword = ref(false)
    const showRegisterPassword = ref(false)
    const showConfirmPassword = ref(false)

    // 登录表单数据
    const loginForm = reactive({
      username: '',
      password: ''
    })

    // 注册表单数据
    const registerForm = reactive({
      studentId: '',
      username: '',
      name: '',
      email: '',
      password: '',
      confirmPassword: '',
      major: '',
      college: '',
      grade: '',
      className: '',
      gender: 'U'
    })

    // 设置登录/注册模式
    const setMode = (mode) => {
      currentMode.value = mode
      emit('mode-change', mode)
      loginError.value = ''
    }

    // 关闭弹窗
    const handleClose = () => {
      emit('close')
    }

    // 处理登录
    const handleLogin = async () => {
      if (!loginForm.username || !loginForm.password) {
        loginError.value = '请输入账号和密码'
        return
      }

      loading.value = true
      loginError.value = ''

      try {
        // 首先检查后端服务是否可用
        const isBackendHealthy = await apiService.healthCheck()
        if (!isBackendHealthy) {
          loginError.value = '后端服务不可用，请确保Spring Boot应用已启动'
          loading.value = false
          return
        }

        const result = await apiService.login({
          username: loginForm.username,
          password: loginForm.password
        })

        if (result.success) {
          localStorage.setItem('isAuthenticated', 'true')

          const userData = result.data
          const userInfo = {
            id: userData.id,
            username: userData.username,
            name: userData.name,
            student_id: userData.studentId,
            email: userData.email,
            major: userData.major,
            grade: userData.grade,
            college: userData.college,
            class_name: userData.className,
            avatar_url: userData.avatarUrl || 'https://picsum.photos/100/100?random=1',
            learning_goal: userData.learningGoal
          }

          localStorage.setItem('user', JSON.stringify(userInfo))
          localStorage.setItem('token', 'mock-jwt-token')

          console.log('登录成功，存储的用户信息:', userInfo)
          handleClose()

          message.success('登录成功！')
          emit('login-success')

          // 直接刷新页面并跳转到首页
          window.location.href = '/'
        } else {
          loginError.value = result.message || '用户名/学号或密码错误'
        }
      } catch (error) {
        console.error('登录错误:', error)
        loginError.value = error.message || '网络错误，请检查后端服务是否启动'
      } finally {
        loading.value = false
      }
    }

    // 处理注册
    const handleRegister = async () => {
      // 验证表单
      if (!registerForm.studentId) {
        loginError.value = '请输入学号'
        return
      }
      if (!registerForm.username) {
        loginError.value = '请输入用户名'
        return
      }
      if (!registerForm.name) {
        loginError.value = '请输入姓名'
        return
      }
      if (!registerForm.major) {
        loginError.value = '请选择专业'
        return
      }
      if (!registerForm.college) {
        loginError.value = '请选择学院'
        return
      }
      if (!registerForm.grade) {
        loginError.value = '请选择年级'
        return
      }
      if (registerForm.password !== registerForm.confirmPassword) {
        loginError.value = '两次输入的密码不一致'
        return
      }
      if (registerForm.password.length < 6) {
        loginError.value = '密码长度不能少于6位'
        return
      }

      loading.value = true
      loginError.value = ''

      try {
        // 检查后端服务
        const isBackendHealthy = await apiService.healthCheck()
        if (!isBackendHealthy) {
          loginError.value = '后端服务不可用，请确保Spring Boot应用已启动'
          loading.value = false
          return
        }

        const result = await apiService.register({
          studentId: registerForm.studentId,
          username: registerForm.username,
          password: registerForm.password,
          name: registerForm.name,
          email: registerForm.email,
          major: registerForm.major,
          college: registerForm.college,
          grade: registerForm.grade,
          className: registerForm.className,
          gender: registerForm.gender
        })

        if (result.success) {
          console.log('注册成功:', result.data)

          // 清空表单
          Object.keys(registerForm).forEach(key => {
            registerForm[key] = ''
          })
          // 重置选择字段
          registerForm.gender = 'U'
          agreeTerms.value = false

          setMode('login')
          loginError.value = ''
          message.success('注册成功，请登录！')
        } else {
          loginError.value = result.message || '注册失败，请检查输入信息'
        }
      } catch (error) {
        console.error('注册错误:', error)
        loginError.value = error.message || '网络错误，请稍后重试'
      } finally {
        loading.value = false
      }
    }

    return {
      currentMode,
      rememberMe,
      agreeTerms,
      loginError,
      loading,
      showLoginPassword,
      showRegisterPassword,
      showConfirmPassword,
      loginForm,
      registerForm,
      setMode,
      handleClose,
      handleLogin,
      handleRegister
    }
  }
}
</script>

<style scoped>
.form-group {
  margin-bottom: 0.5rem;
}

input:focus, select:focus {
  outline: none;
  ring: 2px;
}

/* 自定义滚动条样式 */
.max-h-\[60vh\]::-webkit-scrollbar {
  width: 6px;
}

.max-h-\[60vh\]::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.max-h-\[60vh\]::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.max-h-\[60vh\]::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>