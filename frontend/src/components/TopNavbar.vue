<template>
  <header
      id="navbar"
      class="fixed w-full z-40 transition-all duration-300 bg-dark/80 backdrop-blur-md text-white shadow-lg"
      :class="{ 'shadow-xl': isScrolled }"
  >
    <div class="container mx-auto px-4 py-4">
      <div class="flex justify-between items-center">
        <!-- 品牌标识 -->
        <div class="flex items-center">
          <router-link to="/" class="navbar-brand flex items-center gap-2" @click="closeAuthModal">
            <div class="w-8 h-8 rounded-lg gradient-bg flex items-center justify-center">
              <i class="fa fa-graduation-cap text-white"></i>
            </div>
            <span class="text-xl font-bold text-white">AI 智能·学习搭子</span>
          </router-link>
        </div>

        <!-- 桌面端导航链接 -->
        <nav class="hidden md:flex space-x-8">
          <router-link
              to="/"
              class="navbar-link"
              :class="{ 'active': $route.name === 'Home' }"
              @click="closeAuthModal"
          >
            首页
          </router-link>
          <router-link
              to="/#school-learning"
              class="navbar-link"
              @click="closeAuthModal"
          >
            校园学习
          </router-link>
          <router-link
              to="/#employment-preparation"
              class="navbar-link"
              @click="closeAuthModal"
          >
            就业准备
          </router-link>
          <router-link
              to="/#knowledge-answering"
              class="navbar-link"
              @click="closeAuthModal"
          >
            知识答疑
          </router-link>
          <router-link
              to="/#psychology-companionship"
              class="navbar-link"
              @click="closeAuthModal"
          >
            心理陪伴
          </router-link>
        </nav>

        <!-- 登录/注册按钮 - 未登录状态（点击打开悬浮窗） -->
        <div class="hidden md:flex items-center space-x-4" v-if="!isAuthenticated">
          <Button
              @click="openAuthModal('login')"
              class="navbar-btn navbar-btn-text"
              type="text"
          >
            登录
          </Button>
          <Button
              @click="openAuthModal('register')"
              class="navbar-btn navbar-btn-primary"
          >
            注册
          </Button>
        </div>

        <!-- 个人中心入口 - 已登录状态 -->
        <div class="hidden md:flex items-center space-x-4" v-else>
          <router-link to="/profile" class="profile-link flex items-center gap-2" @click="closeAuthModal">
            <div class="avatar-container w-8 h-8 rounded-full overflow-hidden border border-gray-600">
              <img src="https://picsum.photos/100/100?random=10" alt="用户头像" class="w-full h-full object-cover">
            </div>
            <span class="font-medium">张明</span>
          </router-link>
        </div>

        <!-- 移动端菜单按钮 -->
        <div class="md:hidden">
          <Button
              type="text"
              @click="mobileMenuVisible = !mobileMenuVisible; closeAuthModal()"
              class="mobile-menu-btn p-2 text-white hover:text-primary transition-colors"
          >
            <i class="fa fa-bars text-xl"></i>
          </Button>
        </div>
      </div>
    </div>

    <!-- 移动端导航菜单 -->
    <div
        id="mobileMenu"
        class="md:hidden bg-dark/95 backdrop-blur-md border-t border-gray-700 transition-all duration-300"
        :class="{ 'hidden': !mobileMenuVisible, 'block': mobileMenuVisible }"
    >
      <div class="container mx-auto px-4 py-3 mobile-menu-container flex flex-col gap-2">
        <router-link
            to="/"
            class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors"
            @click="mobileMenuVisible = false; closeAuthModal()"
        >
          首页
        </router-link>
        <router-link
            to="/#school-learning"
            class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors"
            @click="mobileMenuVisible = false; closeAuthModal()"
        >
          校园学习
        </router-link>
        <router-link
            to="/#employment-preparation"
            class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors"
            @click="mobileMenuVisible = false; closeAuthModal()"
        >
          就业准备
        </router-link>
        <router-link
            to="/#knowledge-answering"
            class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors"
            @click="mobileMenuVisible = false; closeAuthModal()"
        >
          知识答疑
        </router-link>
        <router-link
            to="/#psychology-companionship"
            class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors"
            @click="mobileMenuVisible = false; closeAuthModal()"
        >
          心理陪伴
        </router-link>

        <div v-if="!isAuthenticated" class="mobile-auth-buttons flex gap-3 pt-2 border-t border-gray-700">
          <Button
              @click="mobileMenuVisible = false; openAuthModal('login')"
              class="navbar-btn navbar-btn-text flex-1"
              type="text"
          >
            登录
          </Button>
          <Button
              @click="mobileMenuVisible = false; openAuthModal('register')"
              class="navbar-btn navbar-btn-primary flex-1"
          >
            注册
          </Button>
        </div>

        <div v-else class="pt-2 border-t border-gray-700">
          <router-link
              to="/profile"
              class="profile-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors flex items-center gap-2"
              @click="mobileMenuVisible = false; closeAuthModal()"
          >
            <div class="avatar-container w-8 h-8 rounded-full overflow-hidden border border-gray-600 mr-2">
              <img src="https://picsum.photos/100/100?random=10" alt="用户头像" class="w-full h-full object-cover">
            </div>
            <span>个人中心</span>
          </router-link>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import {ref, onMounted, onUnmounted, computed} from 'vue'
import {Button} from 'ant-design-vue'
import '@/assets/styles/navbar.css'

export default {
  components: {
    Button
  },
  emits: ['open-auth-modal', 'close-auth-modal'],
  setup(_, {emit}) {
    // 导航栏状态
    const mobileMenuVisible = ref(false)
    const isScrolled = ref(false)

    // 检查登录状态
    const isAuthenticated = computed(() => {
      return localStorage.getItem('isAuthenticated') === 'true'
    })

    // 导航栏滚动监听
    const handleScroll = () => {
      isScrolled.value = window.scrollY > 10
    }

    // 打开登录/注册悬浮窗
    const openAuthModal = (mode) => {
      emit('open-auth-modal', mode)
    }

    // 关闭登录/注册悬浮窗
    const closeAuthModal = () => {
      emit('close-auth-modal')
    }

    // 生命周期钩子
    onMounted(() => {
      window.addEventListener('scroll', handleScroll)
    })

    onUnmounted(() => {
      window.removeEventListener('scroll', handleScroll)
    })

    return {
      mobileMenuVisible,
      isScrolled,
      isAuthenticated,
      openAuthModal,
      closeAuthModal
    }
  }
}
</script>
