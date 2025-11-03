<template>
  <!-- 毛玻璃登录/注册悬浮窗 -->
  <div
      v-if="visible"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-dark/30 backdrop-blur-sm transition-opacity duration-300"
      @click="handleClose"
  >
    <div
        class="w-full max-w-md bg-white/90 backdrop-blur-xl rounded-2xl p-8 shadow-2xl border border-gray-200 transition-all duration-500 hover:shadow-primary/20 hover:border-primary/30 relative"
        @click.stop
    >
      <!-- 关闭按钮 -->
      <button
          @click="handleClose"
          class="absolute top-4 right-4 text-gray-600 hover:text-gray-900 transition-colors"
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
      <div v-if="currentMode === 'login'">
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

        <form @submit.prevent="handleRegister" class="space-y-5">
          <div class="form-group">
            <label class="block text-gray-700 mb-2">学号/工号</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-user"></i>
              </span>
              <input
                  v-model="registerForm.username"
                  type="text"
                  placeholder="请输入学号或工号"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  required
              >
            </div>
          </div>

          <div class="form-group">
            <label class="block text-gray-700 mb-2">姓名</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-id-card-o"></i>
              </span>
              <input
                  v-model="registerForm.name"
                  type="text"
                  placeholder="请输入您的姓名"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  required
              >
            </div>
          </div>

          <div class="form-group">
            <label class="block text-gray-700 mb-2">邮箱</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-envelope-o"></i>
              </span>
              <input
                  v-model="registerForm.email"
                  type="email"
                  placeholder="请输入您的邮箱"
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
                  v-model="registerForm.password"
                  :type="showRegisterPassword ? 'text' : 'password'"
                  placeholder="请设置密码"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  required
                  minlength="6"
              >
              <button
                  type="button"
                  class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-900 transition-colors"
                  @click="showRegisterPassword = !showRegisterPassword"
              >
                <i :class="showRegisterPassword ? 'fa fa-eye' : 'fa fa-eye-slash'"></i>
              </button>
            </div>
          </div>

          <div class="form-group">
            <label class="block text-gray-700 mb-2">确认密码</label>
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-lock"></i>
              </span>
              <input
                  v-model="registerForm.confirmPassword"
                  :type="showConfirmPassword ? 'text' : 'password'"
                  placeholder="请再次输入密码"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  required
              >
              <button
                  type="button"
                  class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-900 transition-colors"
                  @click="showConfirmPassword = !showConfirmPassword"
              >
                <i :class="showConfirmPassword ? 'fa fa-eye' : 'fa fa-eye-slash'"></i>
              </button>
            </div>
          </div>

          <div class="flex items-center">
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

          <div class="form-group">
            <button
                type="submit"
                class="w-full py-3 px-4 bg-gradient-to-r from-primary to-secondary rounded-lg text-white font-medium hover:opacity-90 transform hover:-translate-y-1 active:translate-y-0 transition-all duration-300 shadow-lg shadow-primary/20"
                :disabled="!agreeTerms || loading"
            >
              {{ loading ? '注册中...' : '注册' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
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
  emits: ['close', 'mode-change'],
  setup(props, { emit }) {
    const router = useRouter()

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
      username: '',
      name: '',
      email: '',
      password: '',
      confirmPassword: ''
    })

    // 设置登录/注册模式
    const setMode = (mode) => {
      currentMode.value = mode
      emit('mode-change', mode)
      loginError.value = '' // 切换模式时清除错误信息
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
        const result = await apiService.login({
          username: loginForm.username,
          password: loginForm.password
        })

        if (result.success) {
          localStorage.setItem('isAuthenticated', 'true')
          localStorage.setItem('user', JSON.stringify(result.data))
          localStorage.setItem('token', 'mock-jwt-token') // 实际项目中应该是后端返回的token

          console.log('登录成功:', result.data)
          handleClose()
          router.push('/profile')
        } else {
          loginError.value = result.message || '登录失败'
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
        const result = await apiService.register({
          username: registerForm.username,
          password: registerForm.password,
          email: registerForm.email,
          name: registerForm.name,
          major: '计算机科学与技术',
          grade: '2023',
          learningGoal: '提升学习效率'
        })

        if (result.success) {
          console.log('注册成功:', result.data)

          // 清空注册表单
          Object.keys(registerForm).forEach(key => {
            registerForm[key] = ''
          })
          agreeTerms.value = false

          // 切换到登录模式
          setMode('login')
          loginError.value = '' // 清除错误信息
        } else {
          loginError.value = result.message || '注册失败'
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
  margin-bottom: 1.25rem;
}

input:focus {
  outline: none;
  ring: 2px;
}
</style>