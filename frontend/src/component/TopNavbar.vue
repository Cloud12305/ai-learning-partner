<template>
  <nav class="fixed top-0 left-0 right-0 bg-gradient-to-r from-blue-500 to-purple-600 shadow-2xl z-50 border-b border-white/20">
    <div class="container mx-auto px-6">
      <div class="flex justify-between items-center h-20">
        <!-- 左侧Logo和标题 -->
        <div class="flex items-center">
          <router-link to="/" class="flex items-center space-x-4 group">
            <!-- Logo -->
            <div class="w-12 h-12 bg-white rounded-2xl flex items-center justify-center shadow-2xl transform group-hover:scale-110 transition-all duration-300 overflow-hidden">
              <img src="https://api.iconify.design/noto:owl.svg" alt="Logo" class="w-8 h-8">
            </div>
            <div class="flex flex-col">
              <div class="text-2xl font-bold text-white tracking-wide drop-shadow-lg">
                Ai智能学习搭子
              </div>
              <div class="text-white/90 text-sm font-light tracking-wider">AI Learning Companion</div>
            </div>
          </router-link>
        </div>

        <!-- 中间导航菜单 -->
        <div class="hidden lg:flex items-center space-x-2">
          <!-- 个人中心 -->
          <router-link
              to="/academic-profile"
              class="nav-menu-item text-white hover:bg-white/20"
              :class="{ 'nav-menu-item-active': $route.path === '/academic-profile' }"
          >
            <i class="fas fa-user-graduate mr-3 text-lg"></i>
            <span class="text-lg font-semibold">个人中心</span>
          </router-link>

          <!-- 校园学习 - 下拉菜单 -->
          <div class="relative group">
            <button class="nav-menu-item text-white hover:bg-white/20">
              <i class="fas fa-university mr-3 text-lg"></i>
              <span class="text-lg font-semibold">校园学习</span>
              <i class="fas fa-chevron-down ml-2 text-sm transition-transform group-hover:rotate-180"></i>
            </button>
            <div class="absolute top-full left-0 mt-1 w-48 bg-white/95 backdrop-blur-xl rounded-2xl shadow-2xl border border-white/20 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 transform translate-y-2 group-hover:translate-y-0">
              <div class="py-2">
                <router-link
                    v-for="item in campusLearningItems"
                    :key="item.path"
                    :to="item.path"
                    class="dropdown-item text-gray-700 hover:text-blue-600"
                    :class="{ 'dropdown-item-active': $route.path === item.path }"
                >
                  <i :class="['mr-3', item.icon]"></i>
                  {{ item.name }}
                </router-link>
              </div>
            </div>
          </div>

          <!-- 就业准备 - 下拉菜单 -->
          <div class="relative group">
            <button class="nav-menu-item text-white hover:bg-white/20">
              <i class="fas fa-briefcase mr-3 text-lg"></i>
              <span class="text-lg font-semibold">就业准备</span>
              <i class="fas fa-chevron-down ml-2 text-sm transition-transform group-hover:rotate-180"></i>
            </button>
            <div class="absolute top-full left-0 mt-1 w-48 bg-white/95 backdrop-blur-xl rounded-2xl shadow-2xl border border-white/20 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 transform translate-y-2 group-hover:translate-y-0">
              <div class="py-2">
                <router-link
                    v-for="item in careerPreparationItems"
                    :key="item.path"
                    :to="item.path"
                    class="dropdown-item text-gray-700 hover:text-blue-600"
                    :class="{ 'dropdown-item-active': $route.path === item.path }"
                >
                  <i :class="['mr-3', item.icon]"></i>
                  {{ item.name }}
                </router-link>
              </div>
            </div>
          </div>

          <!-- 知识答疑 -->
          <router-link
              to="/knowledge-answering"
              class="nav-menu-item text-white hover:bg-white/20"
              :class="{ 'nav-menu-item-active': $route.path === '/knowledge-answering' }"
          >
            <i class="fas fa-comments mr-3 text-lg"></i>
            <span class="text-lg font-semibold">知识答疑</span>
          </router-link>

          <!-- 心理陪伴 -->
          <router-link
              to="/psychology-companion"
              class="nav-menu-item text-white hover:bg-white/20"
              :class="{ 'nav-menu-item-active': $route.path === '/psychology-companion' }"
          >
            <i class="fas fa-heartbeat mr-3 text-lg"></i>
            <span class="text-lg font-semibold">心理陪伴</span>
          </router-link>
        </div>

        <!-- 右侧用户信息 -->
        <div class="flex items-center space-x-6">
          <div v-if="isAuthenticated" class="flex items-center space-x-4">
            <!-- 用户头像和欢迎信息 -->
            <div class="flex items-center space-x-3 bg-white/20 backdrop-blur-sm rounded-2xl px-4 py-2 border border-white/30">
              <div class="w-10 h-10 rounded-full overflow-hidden border-2 border-white shadow-lg">
                <img :src="currentUser.avatar_url || 'https://picsum.photos/100/100?random=1'"
                     :alt="currentUser.name"
                     class="w-full h-full object-cover">
              </div>
              <div class="text-white">
                <div class="font-semibold text-lg">{{ currentUser.name || '用户' }}</div>
                <div class="text-white/80 text-sm">欢迎回来！</div>
              </div>
            </div>

            <!-- 退出按钮 -->
            <Button
                @click="handleLogout"
                size="large"
                class="logout-btn bg-white/20 hover:bg-white/30 border-white/30 text-white font-semibold h-12 px-6 rounded-2xl backdrop-blur-sm"
            >
              <i class="fas fa-sign-out-alt mr-2"></i>
              退出登录
            </Button>
          </div>

          <div v-else class="flex items-center space-x-3">
            <!-- 登录按钮 -->
            <Button
                @click="$emit('open-auth-modal')"
                size="large"
                class="login-btn bg-white hover:bg-gray-100 border-0 text-blue-600 font-bold h-12 px-8 rounded-2xl shadow-2xl transform hover:scale-105 transition-all duration-300"
            >
              <i class="fas fa-rocket mr-3"></i>
              立即登录
            </Button>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Button, message } from 'ant-design-vue'

export default {
  name: 'TopNavbar',
  components: {
    Button
  },
  emits: ['open-auth-modal'],
  setup() {
    const router = useRouter()

    // 校园学习子菜单
    const campusLearningItems = ref([
      { name: '课程学习', path: '/course-learning', icon: 'fas fa-book-open' },
      { name: '学习进度', path: '/learning-progress', icon: 'fas fa-chart-bar' },
      { name: '学习计划', path: '/study-plan', icon: 'fas fa-calendar-check' }
    ])

    // 就业准备子菜单
    const careerPreparationItems = ref([
      { name: '就业匹配', path: '/job-matching', icon: 'fas fa-handshake' },
      { name: '实践训练', path: '/practical-training', icon: 'fas fa-laptop-code' }
    ])

    // 独立菜单项
    const independentItems = ref([
      { name: '知识答疑', path: '/knowledge-answering', icon: 'fas fa-comments' },
      { name: '心理陪伴', path: '/psychology-companion', icon: 'fas fa-heartbeat' }
    ])

    // 使用 ref 来跟踪用户信息变化
    const currentUser = ref({
      name: '',
      avatar_url: '',
      student_id: '',
      major: '',
      grade: ''
    })

    // 监听 localStorage 变化的函数
    const handleStorageChange = () => {
      updateUserInfo()
    }

    // 更新用户信息的函数
    const updateUserInfo = () => {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        try {
          const userData = JSON.parse(userStr)
          console.log('导航栏更新用户信息:', userData)
          currentUser.value = {
            name: userData.name || userData.username || '用户',
            avatar_url: userData.avatar_url || userData.avatarUrl || 'https://picsum.photos/100/100?random=1',
            student_id: userData.student_id || userData.studentId || '',
            major: userData.major || '',
            grade: userData.grade || ''
          }
        } catch (error) {
          console.error('解析用户信息失败:', error)
          currentUser.value = {
            name: '用户',
            avatar_url: 'https://picsum.photos/100/100?random=1'
          }
        }
      } else {
        currentUser.value = {
          name: '用户',
          avatar_url: 'https://picsum.photos/100/100?random=1'
        }
      }
    }

    // 计算属性：检查用户是否已认证
    const isAuthenticated = computed(() => {
      return localStorage.getItem('isAuthenticated') === 'true'
    })

    // 退出登录
    const handleLogout = () => {
      localStorage.removeItem('isAuthenticated')
      localStorage.removeItem('user')
      localStorage.removeItem('token')
      message.success('退出登录成功')

      // 更新用户信息
      updateUserInfo()

      // 跳转到首页
      router.push('/')
    }

    onMounted(() => {
      // 初始化用户信息
      updateUserInfo()

      // 监听 localStorage 变化
      window.addEventListener('storage', handleStorageChange)

      // 自定义事件监听（用于组件间通信）
      window.addEventListener('user-info-updated', handleStorageChange)

      console.log('导航栏初始化完成，当前用户:', currentUser.value)
    })

    onUnmounted(() => {
      // 清理事件监听
      window.removeEventListener('storage', handleStorageChange)
      window.removeEventListener('user-info-updated', handleStorageChange)
    })

    return {
      campusLearningItems,
      careerPreparationItems,
      independentItems,
      isAuthenticated,
      currentUser,
      handleLogout
    }
  }
}
</script>

<style scoped>
/* 导航菜单项基础样式 */
.nav-menu-item {
  @apply flex items-center px-6 py-3 rounded-2xl transition-all duration-300 font-medium backdrop-blur-sm;
}

/* 激活状态的导航菜单项 */
.nav-menu-item-active {
  @apply bg-white/30 backdrop-blur-xl shadow-lg;
}

/* 下拉菜单项样式 */
.dropdown-item {
  @apply flex items-center px-6 py-3 transition-all duration-200 font-medium border-l-4 border-transparent;
}

.dropdown-item-active {
  @apply text-blue-600 bg-blue-50 border-l-4 border-blue-500 font-semibold;
}

/* 下拉菜单悬停效果 */
.dropdown-item:hover {
  @apply bg-gray-50 border-l-4 border-blue-400 transform translate-x-1;
}

/* 登录按钮特殊效果 */
.login-btn {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  box-shadow: 0 10px 30px rgba(37, 99, 235, 0.3);
}

.login-btn:hover {
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  box-shadow: 0 15px 40px rgba(37, 99, 235, 0.4);
}

/* 退出按钮样式 */
.logout-btn {
  transition: all 0.3s ease;
}

.logout-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(255, 255, 255, 0.2);
}

/* 导航栏毛玻璃效果 */
nav {
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .container {
    @apply px-4;
  }

  .nav-menu-item {
    @apply px-4 py-2 text-base;
  }
}
</style>