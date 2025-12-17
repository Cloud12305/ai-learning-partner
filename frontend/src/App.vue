<template>
  <div id="app">
    <TopNavbar @show-login="showLoginModal = true" />

    <!-- 主要内容 -->
    <main class="pt-16">
      <router-view />
    </main>

    <!-- 登录注册弹窗 -->
    <LoginRegister
        :visible="showLoginModal"
        @close="showLoginModal = false"
        @login-success="handleLoginSuccess"
    />
  </div>
</template>

<script>
import { ref, provide } from 'vue'
import { message } from 'ant-design-vue'
import TopNavbar from '@/component/TopNavbar.vue'
import LoginRegister from './views/LoginRegister.vue'

export default {
  name: 'App',
  components: {
    TopNavbar,
    LoginRegister
  },
  setup() {
    const showLoginModal = ref(false)

    // 提供全局更新用户信息的函数
    const updateGlobalUserInfo = () => {
      // 触发自定义事件，通知所有组件更新用户信息
      window.dispatchEvent(new CustomEvent('user-info-updated'))
    }

    // 提供全局状态
    provide('globalUser', {
      updateUserInfo: updateGlobalUserInfo
    })

    // 登录成功处理
    const handleLoginSuccess = () => {
      showLoginModal.value = false
      message.success('登录成功！')

      // 触发全局用户信息更新
      updateGlobalUserInfo()
    }

    return {
      showLoginModal,
      handleLoginSuccess
    }
  }
}
</script>

<style>
/* 全局样式 */
#app {
  font-family: 'Inter', system-ui, sans-serif;
}

/* 平滑滚动 */
html {
  scroll-behavior: smooth;
}

/* 动画效果 */
.fade-in {
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 加载动画 */
.loader {
  border-top-color: #165DFF;
  animation: spinner 0.6s linear infinite;
}

@keyframes spinner {
  to { transform: rotate(360deg); }
}
</style>