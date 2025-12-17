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
            <a href="https://weibo.com" target="_blank" class="text-neutral-400 hover:text-white transition-colors">
              <i class="fa fa-weibo"></i>
            </a>
            <a href="https://weixin.qq.com" target="_blank" class="text-neutral-400 hover:text-white transition-colors">
              <i class="fa fa-wechat"></i>
            </a>
            <a href="https://github.com" target="_blank" class="text-neutral-400 hover:text-white transition-colors">
              <i class="fa fa-github"></i>
            </a>
          </div>
        </div>
        <div>
          <h4 class="text-lg font-bold mb-6 text-white">快速链接</h4>
          <ul class="space-y-2">
            <li>
              <router-link to="/" class="text-neutral-400 hover:text-white transition-colors">首页</router-link>
            </li>
            <li>
              <router-link to="/academic-profile" class="text-neutral-400 hover:text-white transition-colors">个人中心</router-link>
            </li>
            <li>
              <router-link to="/course-learning" class="text-neutral-400 hover:text-white transition-colors">课程学习</router-link>
            </li>
            <li>
              <router-link to="/job-matching" class="text-neutral-400 hover:text-white transition-colors">就业匹配</router-link>
            </li>
            <li>
              <router-link to="/knowledge-answering" class="text-neutral-400 hover:text-white transition-colors">知识答疑</router-link>
            </li>
          </ul>
        </div>

        <div>
          <h4 class="text-lg font-bold mb-6 text-white">帮助中心</h4>
          <ul class="space-y-2">
            <li>
              <a href="https://edu.cn" target="_blank" class="text-neutral-400 hover:text-white transition-colors">使用指南</a>
            </li>
            <li>
              <a href="https://stackoverflow.com" target="_blank" class="text-neutral-400 hover:text-white transition-colors">技术问答</a>
            </li>
            <li>
              <a href="https://www.miit.gov.cn" target="_blank" class="text-neutral-400 hover:text-white transition-colors">隐私政策</a>
            </li>
            <li>
              <a href="https://www.gov.cn" target="_blank" class="text-neutral-400 hover:text-white transition-colors">服务条款</a>
            </li>
          </ul>
        </div>
        <div>
          <h4 class="text-lg font-bold mb-6 text-white">联系我们</h4>
          <ul class="space-y-2">
            <li class="flex items-start">
              <i class="fa fa-map-marker mt-1 mr-3 text-neutral-400"></i>
              <span class="text-neutral-400">地址：浙江省金华市婺城区迎宾大道688号</span>
            </li>
            <li class="flex items-center">
              <i class="fa fa-phone mr-3 text-neutral-400"></i>
              <a href="tel:010-12345678" class="text-neutral-400 hover:text-white transition-colors">电话：010-12345678</a>
            </li>
            <li class="flex items-center">
              <i class="fa fa-envelope-o mr-3 text-neutral-400"></i>
              <a href="mailto:contact@xuezhi.com" class="text-neutral-400 hover:text-white transition-colors">邮箱：contact@xuezhi.com</a>
            </li>
            <li class="flex items-center">
              <i class="fa fa-globe mr-3 text-neutral-400"></i>
              <a href="https://www.zjxu.edu.cn" target="_blank" class="text-neutral-400 hover:text-white transition-colors">官网：www.xuezhi.com</a>
            </li>
          </ul>
        </div>
      </div>
      <div class="border-t border-neutral-700 mt-12 pt-8 text-center text-neutral-500 text-sm">
        <p>&copy; 2025 AI 智能·学习搭子系统 版权所有</p>
        <p class="mt-2">
          <a href="https://beian.miit.gov.cn" target="_blank" class="text-neutral-400 hover:text-white transition-colors">浙ICP备12345678号</a>
        </p>
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

.page-footer {
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  padding: 3rem 0 1.5rem;
}

/* 如果需要更精确的控制，可以添加以下样式 */
::v-deep .page-footer h4 {
  color: inherit;
}
</style>