<template>
  <div class="relative min-h-screen font-inter antialiased opacity-100 transition-opacity duration-300">
    <!-- 1. 顶部导航栏 -->
    <TopNavbar @open-auth-modal="openAuthModal" />

    <!-- 2. 首页主内容 -->
    <main class="pt-16">
      <!-- 英雄区域 -->
      <div class="gradient-bg text-white py-20 md:py-32 relative overflow-hidden">
        <!-- 背景装饰（增强视觉层次） -->
        <div class="absolute inset-0 bg-dark/20 z-0"></div>
        <div class="container mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
          <div class="max-w-4xl mx-auto text-center">
            <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold mb-6 leading-tight text-shadow">
              全方位掌握你的<span class="text-yellow-300">学习轨迹</span>
            </h1>
            <p class="text-[clamp(1rem,2vw,1.25rem)] text-white/90 mb-10 max-w-2xl mx-auto">
              整合教务系统、实验室和图书馆数据，生成个性化学业画像，助你全面了解学习状况，规划未来发展
            </p>
            <div class="flex flex-col sm:flex-row justify-center gap-4">
              <!-- 立即开始按钮（点击打开登录悬浮窗） -->
              <button
                  @click="openAuthModal('login')"
                  class="px-8 py-3 bg-white text-blue-600 font-medium rounded-lg hover:bg-neutral-100 transition-all duration-300 transform hover:-translate-y-1 shadow-lg hover:shadow-xl"
              >
                <i class="fas fa-rocket mr-2"></i>
                立即开始
              </button>
              <router-link
                  to="/academic-profile"
                  class="px-8 py-3 bg-transparent border-2 border-white text-white font-medium rounded-lg hover:bg-white/10 transition-all duration-300 transform hover:-translate-y-1"
              >
                <i class="fas fa-chart-line mr-2"></i>
                探索功能
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 特点介绍 -->
      <section id="features" class="py-20 bg-white">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-4 text-blue-800">强大功能，助力学业发展</h2>
            <p class="text-neutral-600 max-w-2xl mx-auto">我们整合多维度数据，为你提供全方位的学业分析与建议</p>
          </div>

          <div class="grid md:grid-cols-3 gap-8">
            <FeatureCard
                icon="graduation-cap"
                title="成绩分析"
                description="整合教务系统成绩单，生成课程成绩分析、GPA趋势图，直观展示学习成果。"
                bgClass="bg-blue-100"
                textClass="text-blue-600"
            />

            <FeatureCard
                icon="flask"
                title="实验参与"
                description="记录实验室参与情况、项目成果与技能提升，全面展示实践能力发展轨迹。"
                bgClass="bg-cyan-100"
                textClass="text-cyan-600"
            />

            <FeatureCard
                icon="book"
                title="阅读习惯"
                description="分析图书馆借阅记录，展示阅读偏好与知识结构，发现学习兴趣点。"
                bgClass="bg-indigo-100"
                textClass="text-indigo-600"
            />
          </div>
        </div>
      </section>

      <!-- 关于我们/校园学习 -->
      <section id="school-learning" class="py-20 bg-blue-50">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex flex-col lg:flex-row items-center gap-12">
            <div class="lg:w-1/2">
              <img src="https://picsum.photos/600/400?random=1" alt="学生使用AI 智能·学习搭子系统" class="rounded-xl shadow-lg w-full h-auto">
            </div>
            <div class="lg:w-1/2">
              <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-6 text-blue-800">关于AI 智能·学习搭子系统</h2>
              <p class="text-neutral-600 mb-4">AI 智能·学习搭子是一款专为学生设计的综合性学习分析工具，通过整合教务系统、实验室和图书馆等多维度数据，为每位学生生成个性化的学业发展报告。</p>
              <p class="text-neutral-600 mb-6">我们的系统不仅能帮助你全面了解自己的学习状况，还能根据你的特点提供个性化的学习建议和发展规划，让你的学习更有效率，未来发展更明确。</p>
              <div class="grid grid-cols-2 gap-4">
                <div class="flex items-center text-blue-700">
                  <i class="fa fa-check-circle text-green-500 mr-2"></i>
                  <span>数据安全加密</span>
                </div>
                <div class="flex items-center text-blue-700">
                  <i class="fa fa-check-circle text-green-500 mr-2"></i>
                  <span>实时数据更新</span>
                </div>
                <div class="flex items-center text-blue-700">
                  <i class="fa fa-check-circle text-green-500 mr-2"></i>
                  <span>个性化分析</span>
                </div>
                <div class="flex items-center text-blue-700">
                  <i class="fa fa-check-circle text-green-500 mr-2"></i>
                  <span>专业发展建议</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 就业准备 -->
      <section id="employment-preparation" class="py-20 bg-white">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-4 text-blue-800">就业准备模块</h2>
            <p class="text-neutral-600 max-w-2xl mx-auto">结合你的学习数据，提供个性化就业指导与技能提升建议</p>
          </div>
          <div class="max-w-4xl mx-auto bg-blue-50 rounded-2xl p-8 shadow-md border border-blue-100">
            <p class="text-neutral-700 mb-6">系统将根据你的专业方向、成绩表现和实践经历，匹配适合的职业方向，并推荐需要提升的技能课程，帮助你提前做好就业准备。</p>
            <router-link
                to="/job-matching"
                class="px-6 py-2 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors inline-flex items-center"
            >
              <i class="fas fa-briefcase mr-2"></i>
              查看我的就业建议
            </router-link>
          </div>
        </div>
      </section>

      <!-- 知识答疑 -->
      <section id="knowledge-answering" class="py-20 bg-blue-50">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-4 text-blue-800">智能知识答疑</h2>
            <p class="text-neutral-600 max-w-2xl mx-auto">24小时在线答疑，解决你的学习困惑</p>
          </div>
          <div class="max-w-4xl mx-auto bg-white rounded-2xl p-8 shadow-md border border-blue-100">
            <p class="text-neutral-700 mb-6">无论是课程知识点疑问，还是作业难题，AI智能答疑系统都能为你提供详细解答，帮助你扫清学习障碍。</p>
            <router-link
                to="/knowledge-answering"
                class="px-6 py-2 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors inline-flex items-center"
            >
              <i class="fas fa-question-circle mr-2"></i>
              立即提问
            </router-link>
          </div>
        </div>
      </section>

      <!-- 心理陪伴 -->
      <section id="psychology-companionship" class="py-20 bg-white">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-4 text-blue-800">心理陪伴服务</h2>
            <p class="text-neutral-600 max-w-2xl mx-auto">关注你的心理健康，提供专业心理疏导与情绪支持</p>
          </div>
          <div class="max-w-4xl mx-auto bg-blue-50 rounded-2xl p-8 shadow-md border border-blue-100">
            <p class="text-neutral-700 mb-6">学习压力大？情绪低落？心理陪伴模块为你提供专业的心理疏导服务，帮助你保持良好的学习心态。</p>
            <router-link
                to="/psychology-companion"
                class="px-6 py-2 bg-blue-600 text-white font-medium rounded-lg hover:bg-blue-700 transition-colors inline-flex items-center"
            >
              <i class="fas fa-heart mr-2"></i>
              寻求心理支持
            </router-link>
          </div>
        </div>
      </section>
    </main>

    <!-- 4. 页脚 -->
    <PageFooter />

    <!-- 3. 登录注册悬浮窗 -->
    <LoginRegister
        :visible="authModalVisible"
        :mode="authMode"
        @close="closeAuthModal"
        @mode-change="setAuthMode"
    />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import TopNavbar from '../components/TopNavbar.vue'
import LoginRegister from './LoginRegister.vue'
import FeatureCard from '../components/FeatureCard.vue'
import PageFooter from '../components/PageFooter.vue'  // 导入页脚组件

export default {
  name: 'HomeView',
  components: {
    TopNavbar,
    LoginRegister,
    FeatureCard,
    PageFooter,  // 注册页脚组件
  },
  setup() {
    // 登录/注册悬浮窗状态
    const authModalVisible = ref(false)
    const authMode = ref('login') // 'login' 或 'register'

    // 打开登录/注册悬浮窗
    const openAuthModal = (mode) => {
      authMode.value = mode
      authModalVisible.value = true
      document.body.style.overflow = 'hidden' // 防止背景滚动
    }

    // 关闭登录/注册悬浮窗
    const closeAuthModal = () => {
      authModalVisible.value = false
      document.body.style.overflow = '' // 恢复背景滚动
    }

    // 设置登录/注册模式
    const setAuthMode = (mode) => {
      authMode.value = mode
    }

    // 生命周期钩子
    onMounted(() => {
      // 页面加载动画
      document.body.classList.add('opacity-100')
      document.body.classList.remove('opacity-0')
    })

    return {
      // 登录/注册悬浮窗相关
      authModalVisible,
      authMode,
      openAuthModal,
      closeAuthModal,
      setAuthMode,
    }
  }
}
</script>

<style scoped>
/* 全局样式补充 */
.gradient-bg {
  background: linear-gradient(135deg, #3B82F6 0%, #06B6D4 100%);
}

/* 自定义工具类 */
.text-shadow {
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

/* 颜色定义 */
.text-primary {
  color: #3B82F6;
}
.bg-primary {
  background-color: rgba(6, 182, 212, 0.1);
}
.bg-primary\/10 {
  background-color: rgba(59, 130, 246, 0.1);
}

.text-secondary {
  color: #06B6D4;
}
.bg-secondary\/10 {
  background-color: rgba(6, 182, 212, 0.1);
}

.text-accent {
  color: #8B5CF6;
}
.bg-accent\/10 {
  background-color: rgba(139, 92, 246, 0.1);
}
</style>