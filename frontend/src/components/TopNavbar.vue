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

        <!-- 桌面端导航链接：修改知识答疑/心理陪伴为独立页面路由 -->
        <nav class="hidden md:flex items-center space-x-8">
          <router-link
              to="/"
              class="navbar-link"
              :class="{ 'active': $route.name === 'Home' }"
              @click="closeAuthModal"
          >
            首页
          </router-link>

          <!-- 校园学习下拉菜单（不变） -->
          <Dropdown
              placement="bottom"
              @click="closeAuthModal"
          >
            <a class="navbar-link flex items-center cursor-pointer">
              校园学习
              <i class="fa fa-chevron-down ml-1 text-xs transition-transform" :class="{ 'rotate-180': campusVisible }"></i>
            </a>
            <template #overlay>
              <Menu @click="handleMenuClick">
                <Menu.Item key="study-plan">
                  <router-link to="/study-plan">学业规划</router-link>
                </Menu.Item>
                <Menu.Item key="course-learning">
                  <router-link to="/course-learning">课程学习</router-link>
                </Menu.Item>
                <Menu.Item key="learning-progress">
                  <router-link to="/learning-progress">学习进度</router-link>
                </Menu.Item>
              </Menu>
            </template>
          </Dropdown>

          <!-- 就业准备下拉菜单（不变） -->
          <Dropdown
              placement="bottom"
              @click="closeAuthModal"
          >
            <a class="navbar-link flex items-center cursor-pointer">
              就业准备
              <i class="fa fa-chevron-down ml-1 text-xs transition-transform" :class="{ 'rotate-180': jobVisible }"></i>
            </a>
            <template #overlay>
              <Menu @click="handleMenuClick">
                <Menu.Item key="job-matching">
                  <router-link to="/job-matching">岗位匹配</router-link>
                </Menu.Item>
                <Menu.Item key="practical-training">
                  <router-link to="/practical-training">实战演练</router-link>
                </Menu.Item>
              </Menu>
            </template>
          </Dropdown>


          <router-link
              to="/knowledge-answering"
          class="navbar-link"
          :class="{ 'active': $route.name === 'KnowledgeAnswering' }"
          @click="closeAuthModal"
          >
          知识答疑
          </router-link>

          <router-link
              to="/psychology-companion"
          class="navbar-link"
          :class="{ 'active': $route.name === 'PsychologyCompanion' }"
              click="closeAuthModal"
          >
          心理陪伴
          </router-link>
        </nav>

        <!-- 登录/注册按钮（不变） -->
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

        <!-- 个人中心入口（不变） -->
        <div class="hidden md:flex items-center space-x-4" v-else>
          <router-link to="/profile" class="profile-link flex items-center gap-2" @click="closeAuthModal">
            <div class="avatar-container w-8 h-8 rounded-full overflow-hidden border border-gray-600">
              <img src="https://picsum.photos/100/100?random=10" alt="用户头像" class="w-full h-full object-cover">
            </div>
            <span class="font-medium">张明</span>
          </router-link>
        </div>

        <!-- 移动端菜单按钮（不变） -->
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

    <!-- 移动端导航菜单：同步修改知识答疑/心理陪伴为独立路由 -->
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

        <!-- 移动端校园学习下拉（不变） -->
        <div class="mobile-dropdown">
          <div
              class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors flex justify-between items-center cursor-pointer"
              @click="campusMobileVisible = !campusMobileVisible"
          >
            <span>校园学习</span>
            <i class="fa fa-chevron-down text-xs transition-transform" :class="{ 'rotate-180': campusMobileVisible }"></i>
          </div>
          <div class="pl-4 mt-1 space-y-1" v-if="campusMobileVisible">
            <router-link
                to="/study-plan"
                class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors block"
                @click="mobileMenuVisible = false; closeAuthModal()"
            >
              学业规划
            </router-link>
            <router-link
                to="/course-learning"
                class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors block"
                @click="mobileMenuVisible = false; closeAuthModal()"
            >
              课程学习
            </router-link>
            <router-link
                to="/learning-progress"
                class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors block"
                @click="mobileMenuVisible = false; closeAuthModal()"
            >
              学习进度
            </router-link>
          </div>
        </div>

        <!-- 移动端就业准备下拉（不变） -->
        <div class="mobile-dropdown">
          <div
              class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors flex justify-between items-center cursor-pointer"
              @click="jobMobileVisible = !jobMobileVisible"
          >
            <span>就业准备</span>
            <i class="fa fa-chevron-down text-xs transition-transform" :class="{ 'rotate-180': jobMobileVisible }"></i>
          </div>
          <div class="pl-4 mt-1 space-y-1" v-if="jobMobileVisible">
            <router-link
                to="/job-matching"
                class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors block"
                @click="mobileMenuVisible = false; closeAuthModal()"
            >
              岗位匹配
            </router-link>
            <router-link
                to="/practical-training"
                class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors block"
                @click="mobileMenuVisible = false; closeAuthModal()"
            >
              实战演练
            </router-link>
          </div>
        </div>


        <router-link
            to="/knowledge-answering"
        class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors"
        @click="mobileMenuVisible = false; closeAuthModal()"
        >
        知识答疑
        </router-link>
        <router-link
            to="/psychology-companion"
        class="navbar-link py-2 px-3 rounded-lg hover:bg-gray-800/50 transition-colors"
        @click="mobileMenuVisible = false; closeAuthModal()"
        >
        心理陪伴
        </router-link>

        <!-- 移动端登录/注册/个人中心（不变） -->
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
import {Button, Dropdown, Menu} from 'ant-design-vue'
import '@/assets/styles/navbar.css'

export default {
  components: {
    Button,
    Dropdown,
    Menu
  },
  emits: ['open-auth-modal', 'close-auth-modal'],
  setup(_, {emit}) {
    // 导航栏状态（不变）
    const mobileMenuVisible = ref(false)
    const isScrolled = ref(false)
    const campusVisible = ref(false)
    const jobVisible = ref(false)
    const campusMobileVisible = ref(false)
    const jobMobileVisible = ref(false)

    // 登录状态判断（不变）
    const isAuthenticated = computed(() => {
      return localStorage.getItem('isAuthenticated') === 'true'
    })

    // 滚动监听（不变）
    const handleScroll = () => {
      isScrolled.value = window.scrollY > 10
    }

    // 下拉菜单关闭（不变）
    const handleMenuClick = () => {
      campusVisible.value = false
      jobVisible.value = false
    }

    // 登录/注册弹窗（不变）
    const openAuthModal = (mode) => {
      emit('open-auth-modal', mode)
    }
    const closeAuthModal = () => {
      emit('close-auth-modal')
    }

    // 生命周期（不变）
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
      campusVisible,
      jobVisible,
      campusMobileVisible,
      jobMobileVisible,
      openAuthModal,
      closeAuthModal,
      handleMenuClick
    }
  }
}
</script>