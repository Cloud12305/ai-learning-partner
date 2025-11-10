<template>
  <nav class="fixed top-0 left-0 right-0 bg-gradient-to-r from-blue-50 to-cyan-50 shadow-lg z-50 border-b border-blue-100">
    <div class="container mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <!-- 左侧Logo和导航菜单 -->
        <div class="flex items-center space-x-8">
          <!-- Logo -->
          <router-link to="/" class="flex items-center space-x-3">
            <div class="w-10 h-10 bg-gradient-to-br from-blue-500 to-cyan-500 rounded-xl flex items-center justify-center shadow-md">
              <i class="fas fa-graduation-cap text-white text-lg"></i>
            </div>
            <span class="text-xl font-bold bg-gradient-to-r from-blue-600 to-cyan-600 bg-clip-text text-transparent">
              AI 智能学习搭子
            </span>
          </router-link>

          <!-- 导航菜单 -->
          <div class="hidden md:flex items-center space-x-6">
            <router-link
                v-for="item in navItems"
                :key="item.id"
                :to="item.path"
                class="text-blue-700 hover:text-cyan-600 transition-colors duration-200 font-medium px-3 py-2 rounded-lg hover:bg-white/50 backdrop-blur-sm"
                :class="{ 'bg-white/80 shadow-sm': $route.path === item.path }"
            >
              <i :class="['mr-2', item.icon]"></i>
              {{ item.name }}
            </router-link>
          </div>
        </div>

        <!-- 右侧用户信息 -->
        <div class="flex items-center space-x-4">
          <div v-if="isAuthenticated" class="flex items-center space-x-3">
            <div class="w-8 h-8 rounded-full overflow-hidden border-2 border-white shadow-md">
              <img :src="currentUser.avatar_url || 'https://picsum.photos/100/100?random=1'"
                   :alt="currentUser.name"
                   class="w-full h-full object-cover">
            </div>
            <span class="text-sm font-medium text-blue-800">{{ currentUser.name || '用户' }}</span>
            <Button
                @click="handleLogout"
                size="small"
                class="ml-2 bg-red-500 hover:bg-red-600 border-red-500"
            >
              退出
            </Button>
          </div>
          <div v-else class="flex items-center space-x-2">
            <Button
                @click="$emit('open-auth-modal')"
                type="primary"
                size="small"
                class="bg-gradient-to-r from-blue-500 to-cyan-500 hover:from-blue-600 hover:to-cyan-600 border-0 shadow-md"
            >
              <i class="fas fa-sign-in-alt mr-2"></i>
              登录/注册
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

    // 导航菜单配置
    const navItems = ref([
      { id: 1, name: '学业画像', path: '/academic-profile', icon: 'fas fa-chart-line' },
      { id: 2, name: '课程学习', path: '/course-learning', icon: 'fas fa-book' },
      { id: 3, name: '学习进度', path: '/learning-progress', icon: 'fas fa-tasks' },
      { id: 4, name: '实践训练', path: '/practical-training', icon: 'fas fa-flask' },
      { id: 5, name: '学习计划', path: '/study-plan', icon: 'fas fa-calendar-alt' },
      { id: 6, name: '知识答疑', path: '/knowledge-answering', icon: 'fas fa-question-circle' },
      { id: 7, name: '就业匹配', path: '/job-matching', icon: 'fas fa-briefcase' },
      { id: 8, name: '心理陪伴', path: '/psychology-companion', icon: 'fas fa-heart' }
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
      navItems,
      isAuthenticated,
      currentUser,
      handleLogout
    }
  }
}
</script>

<style scoped>
/* 自定义导航栏样式 */
nav {
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

/* 激活状态的高亮效果 */
.router-link-active {
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(6, 182, 212, 0.1));
  color: #0891b2;
  font-weight: 600;
}
</style>