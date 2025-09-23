<template>
  <!-- 毛玻璃登录/注册悬浮窗 -->
  <div
      v-if="visible"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-dark/30 backdrop-blur-sm transition-opacity duration-300"
      @click="handleClose"
  >
    <!-- 阻止内部点击事件冒泡 -->
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
        <p class="text-red-700 mb-4 text-sm text-center" v-if="loginError">{{ loginError }}</p>

        <Form layout="vertical" class="space-y-5" @finish="handleLogin">
          <Form.Item
              label="学号/工号"
              name="username"
              :rules="loginRules.username"
              class="text-gray-700"
          >
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-user"></i>
              </span>
              <Input
                  placeholder="请输入账号"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
              />
            </div>
          </Form.Item>

          <Form.Item
              label="密码"
              name="password"
              :rules="loginRules.password"
              class="text-gray-700"
          >
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-lock"></i>
              </span>
              <InputPassword
                  placeholder="请输入密码"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  :visibilityToggle="false"
                  ref="loginPassword"
              />
              <button
                  type="button"
                  class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-900 transition-colors"
                  @click="togglePassword('loginPassword')"
              >
                <i :class="showLoginPassword ? 'fa fa-eye' : 'fa fa-eye-slash'"></i>
              </button>
            </div>
          </Form.Item>

          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <Checkbox
                  id="rememberMe"
                  v-model:checked="rememberMe"
                  class="text-gray-700"
              >
                <label for="rememberMe" class="ml-2 text-sm text-gray-700">记住我</label>
              </Checkbox>
            </div>
            <a href="#" class="text-sm text-primary hover:text-primary/80">忘记密码？</a>
          </div>

          <Form.Item>
            <Button
                type="primary"
                htmlType="submit"
                class="w-full py-3 px-4 bg-gradient-to-r from-primary to-secondary rounded-lg text-white font-medium hover:opacity-90 transform hover:-translate-y-1 active:translate-y-0 transition-all duration-300 shadow-lg shadow-primary/20"
            >
              登录
            </Button>
          </Form.Item>
        </Form>
      </div>

      <!-- 注册表单 -->
      <div v-if="currentMode === 'register'">
        <div class="text-center mb-6">
          <h3 class="text-2xl font-bold text-gray-900 mb-2">创建账号</h3>
          <p class="text-gray-700">开始使用学业画像系统，助力你的学习</p>
        </div>

        <Form layout="vertical" class="space-y-5" @finish="handleRegister">
          <Form.Item
              label="学号/工号"
              name="username"
              :rules="registerRules.username"
              class="text-gray-700"
          >
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-user"></i>
              </span>
              <Input
                  placeholder="请输入学号或工号"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
              />
            </div>
          </Form.Item>

          <Form.Item
              label="姓名"
              name="name"
              :rules="registerRules.name"
              class="text-gray-700"
          >
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-id-card-o"></i>
              </span>
              <Input
                  placeholder="请输入您的姓名"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
              />
            </div>
          </Form.Item>

          <Form.Item
              label="邮箱"
              name="email"
              :rules="registerRules.email"
              class="text-gray-700"
          >
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-envelope-o"></i>
              </span>
              <Input
                  placeholder="请输入您的邮箱"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
              />
            </div>
          </Form.Item>

          <Form.Item
              label="密码"
              name="password"
              :rules="registerRules.password"
              class="text-gray-700"
          >
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-lock"></i>
              </span>
              <InputPassword
                  placeholder="请设置密码"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  :visibilityToggle="false"
                  ref="registerPassword"
              />
              <button
                  type="button"
                  class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-900 transition-colors"
                  @click="togglePassword('registerPassword')"
              >
                <i :class="showRegisterPassword ? 'fa fa-eye' : 'fa fa-eye-slash'"></i>
              </button>
            </div>
          </Form.Item>

          <Form.Item
              label="确认密码"
              name="confirmPassword"
              :rules="registerRules.confirmPassword"
              class="text-gray-700"
          >
            <div class="relative">
              <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-gray-500">
                <i class="fa fa-lock"></i>
              </span>
              <InputPassword
                  placeholder="请再次输入密码"
                  class="w-full pl-10 pr-4 py-3 bg-white/80 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50 focus:border-primary/50 text-gray-900 placeholder-gray-500 transition-all duration-300"
                  :visibilityToggle="false"
                  ref="confirmPassword"
              />
              <button
                  type="button"
                  class="absolute inset-y-0 right-0 flex items-center pr-3 text-gray-500 hover:text-gray-900 transition-colors"
                  @click="togglePassword('confirmPassword')"
              >
                <i :class="showConfirmPassword ? 'fa fa-eye' : 'fa fa-eye-slash'"></i>
              </button>
            </div>
          </Form.Item>

          <div class="flex items-center">
            <Checkbox
                id="agreeTerms"
                v-model:checked="agreeTerms"
                required
                class="text-gray-700"
            >
              <label for="agreeTerms" class="ml-2 text-sm text-gray-700">
                我已阅读并同意<a href="#" class="text-primary hover:text-primary/80">服务条款</a>和<a href="#" class="text-primary hover:text-primary/80">隐私政策</a>
              </label>
            </Checkbox>
          </div>

          <Form.Item>
            <Button
                type="primary"
                htmlType="submit"
                class="w-full py-3 px-4 bg-gradient-to-r from-primary to-secondary rounded-lg text-white font-medium hover:opacity-90 transform hover:-translate-y-1 active:translate-y-0 transition-all duration-300 shadow-lg shadow-primary/20"
                :disabled="!agreeTerms"
            >
              注册
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Form, Input, InputPassword, Checkbox, Button, message } from 'ant-design-vue'

export default {
  components: {
    Form,
    Input,
    InputPassword,
    Checkbox,
    Button
  },
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

    // 密码显示状态
    const showLoginPassword = ref(false)
    const showRegisterPassword = ref(false)
    const showConfirmPassword = ref(false)

    // 密码框引用
    const loginPassword = ref(null)
    const registerPassword = ref(null)
    const confirmPassword = ref(null)

    // 设置登录/注册模式
    const setMode = (mode) => {
      currentMode.value = mode
      emit('mode-change', mode)
      loginError.value = '' // 切换模式时清除错误信息
    }

    // 切换密码显示/隐藏
    const togglePassword = (refName) => {
      if (refName === 'loginPassword') {
        showLoginPassword.value = !showLoginPassword.value
        loginPassword.value.setVisible(showLoginPassword.value)
      } else if (refName === 'registerPassword') {
        showRegisterPassword.value = !showRegisterPassword.value
        registerPassword.value.setVisible(showRegisterPassword.value)
      } else if (refName === 'confirmPassword') {
        showConfirmPassword.value = !showConfirmPassword.value
        confirmPassword.value.setVisible(showConfirmPassword.value)
      }
    }

    // 关闭弹窗
    const handleClose = () => {
      emit('close')
    }

    // 登录表单验证规则
    const loginRules = {
      username: [
        { required: true, message: '请输入学号或工号', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }

    // 注册表单验证规则
    const registerRules = {
      username: [
        { required: true, message: '请输入学号或工号', trigger: 'blur' }
      ],
      name: [
        { required: true, message: '请输入您的姓名', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入您的邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请设置密码', trigger: 'blur' },
        { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        ({ getFieldValue }) => ({
          validator(_, value) {
            if (!value || getFieldValue('password') === value) {
              return Promise.resolve();
            }
            return Promise.reject(new Error('两次输入的密码不一致'));
          },
        })
      ]
    }

    // 处理登录
    const handleLogin = (values) => {
      console.log('登录信息:', values)
      // 模拟登录验证
      if (values.username && values.password) {
        localStorage.setItem('isAuthenticated', 'true')
        message.success('登录成功')
        handleClose()
        router.push('/profile')
      } else {
        loginError.value = '账号或密码错误，请重试'
      }
    }

    // 处理注册
    const handleRegister = (values) => {
      console.log('注册信息:', values)
      // 模拟注册
      message.success('注册成功，请登录')
      setMode('login')
    }

    return {
      // 状态
      currentMode,
      rememberMe,
      agreeTerms,
      loginError,
      showLoginPassword,
      showRegisterPassword,
      showConfirmPassword,
      loginPassword,
      registerPassword,
      confirmPassword,

      // 方法
      setMode,
      togglePassword,
      handleClose,
      handleLogin,
      handleRegister,

      // 验证规则
      loginRules,
      registerRules
    }
  }
}
</script>