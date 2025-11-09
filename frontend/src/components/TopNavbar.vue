<template>
  <nav class="fixed top-0 left-0 right-0 bg-white shadow-md z-50">
    <div class="container mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between items-center h-16">
        <!-- 左侧Logo和标题 -->
        <div class="flex items-center">
          <router-link to="/" class="text-xl font-bold text-primary">
            AI 智能学习搭子系统
          </router-link>
        </div>

        <!-- 右侧用户信息 -->
        <div class="flex items-center space-x-4">
          <div v-if="isAuthenticated" class="flex items-center space-x-3">
            <div class="w-8 h-8 rounded-full overflow-hidden">
              <img :src="currentUser.avatar_url || 'https://picsum.photos/100/100?random=1'"
                   :alt="currentUser.name"
                   class="w-full h-full object-cover">
            </div>
            <span class="text-sm font-medium">{{ currentUser.name || '用户' }}</span>
            <Button
                @click="handleLogout"
                size="small"
                class="ml-2"
            >
              退出
            </Button>
          </div>
          <div v-else class="flex items-center space-x-2">
            <Button @click="$emit('show-login')" type="primary" size="small">
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
  emits: ['show-login'],
  setup() {
    const router = useRouter()

    // 使用 ref 来跟踪用户信息变化，而不是 computed
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
      isAuthenticated,
      currentUser,
      handleLogout
    }
  }
}
</script>