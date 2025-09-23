<template>
  <div id="app" class="font-inter bg-neutral-100 text-neutral-800 min-h-screen flex flex-col">
    <!-- 导航栏 -->
    <Navbar />

    <!-- 主内容区域 -->
    <main class="flex-grow transition-all duration-300">
      <router-view/>
    </main>

    <!-- 页脚 -->
    <Footer />
  </div>
</template>

<script>
import Navbar from './components/TopNavbar.vue'
import Footer from './components/PageFooter.vue'

export default {
  name: 'App',
  components: {
    Navbar,
    Footer,
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
