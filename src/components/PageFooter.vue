<template>
  <footer class="page-footer">
    <div class="container mx-auto px-4 sm:px-6 lg:px-8">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
        <div>
          <div class="flex items-center space-x-2 mb-4">
            <span class="text-xl font-bold">AI 智能·学习搭子系统</span>
          </div>
          <p class="text-neutral-400 mb-4">全方位掌握学习轨迹，助力学业发展</p>
          <div class="flex space-x-4">
            <a href="#" class="text-neutral-400 hover:text-white transition-colors">
              <i class="fa fa-weibo"></i>
            </a>
            <a href="#" class="text-neutral-400 hover:text-white transition-colors">
              <i class="fa fa-wechat"></i>
            </a>
            <a href="#" class="text-neutral-400 hover:text-white transition-colors">
              <i class="fa fa-github"></i>
            </a>
          </div>
        </div>
        <div>
          <!-- 增加了text-bold类并调整了颜色 -->
          <h4 class="text-lg font-bold mb-6 text-white">快速链接</h4>
          <ul class="space-y-2">
            <li>
              <router-link to="/" class="text-neutral-400 hover:text-white transition-colors">首页</router-link>
            </li>
            <li>
              <router-link to="/#school-learning" class="text-neutral-400 hover:text-white transition-colors">校园学习</router-link>
            </li>
            <li>
              <router-link to="/#employment-preparation" class="text-neutral-400 hover:text-white transition-colors">就业准备</router-link>
            </li>
            <li>
              <router-link to="/#knowledge-answering" class="text-neutral-400 hover:text-white transition-colors">知识答疑</router-link>
            </li>
            <li>
              <router-link to="/#psychology-companionship" class="text-neutral-400 hover:text-white transition-colors">心理陪伴</router-link>
            </li>
          </ul>
        </div>

        <div>
          <!-- 增加了text-bold类并调整了颜色 -->
          <h4 class="text-lg font-bold mb-6 text-white">帮助中心</h4>
          <ul class="space-y-2">
            <li><a href="#" class="text-neutral-400 hover:text-white transition-colors"> 使用指南</a></li>
            <li><a href="#" class="text-neutral-400 hover:text-white transition-colors"> 常见问题</a></li>
            <li><a href="#" class="text-neutral-400 hover:text-white transition-colors"> 隐私政策</a></li>
            <li><a href="#" class="text-neutral-400 hover:text-white transition-colors"> 服务条款</a></li>
          </ul>
        </div>
        <div>
          <!-- 增加了text-bold类并调整了颜色 -->
          <h4 class="text-lg font-bold mb-6 text-white">&nbsp;&nbsp;联系我们</h4>
          <ul class="space-y-2">
            <li class="flex items-start">
              <i class="fa fa-map-marker mt-1 mr-3 text-neutral-400"></i>
              <span class="text-neutral-400">地址：浙江省金华市婺城区迎宾大道688号</span>
            </li>
            <li class="flex items-center">
              <i class="fa fa-phone mr-3 text-neutral-400"></i>
              <span class="text-neutral-400">电话：010-12345678</span>
            </li>
            <li class="flex items-center">
              <i class="fa fa-envelope-o mr-3 text-neutral-400"></i>
              <span class="text-neutral-400">邮箱：contact@xuezhi.com</span>
            </li>
          </ul>
        </div>
      </div>
      <div class="border-t border-neutral-700 mt-12 pt-8 text-center text-neutral-500 text-sm">
        <p>&copy; 2025 AI 智能·学习搭子系统 版权所有</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'PageFooter'
}
</script>

<style scoped>
@import url('~@/assets/styles/footer-styles.css');

/* 如果需要更精确的控制，可以添加以下样式 */
::v-deep .page-footer h4 {
  /* 确保与标题颜色一致 */
  color: inherit;
}
</style>
