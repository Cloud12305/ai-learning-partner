<template>
  <div class="relative min-h-screen font-inter antialiased opacity-100 transition-opacity duration-300">
    <!-- 1. 顶部导航栏 -->
    <TopNavbar @open-auth-modal="openAuthModal" />

    <!-- 2. 首页主内容 -->
    <main class="pt-16">
      <!-- 英雄区域 -->
      <div
          class="text-white relative overflow-hidden"
          :style="heroStyle"
      >
        <!-- 背景装饰（恢复原来的遮罩） -->
        <div class="absolute inset-0 bg-dark/20 z-0"></div>
        <div class="container mx-auto px-4 sm:px-6 lg:px-8 relative z-10 w-full">
          <div class="max-w-5xl mx-auto text-center py-16 md:py-24">
            <h1 class="text-[clamp(2.8rem,6vw,4.2rem)] font-bold mb-8 leading-[1.1] tracking-tight">
  <span class="bg-gradient-to-r from-white via-cyan-50 to-blue-50 bg-clip-text text-transparent">
    全方位掌握你的
  </span>
              <span class="bg-gradient-to-r from-purple-400 via-violet-500 to-fuchsia-500 bg-clip-text text-transparent drop-shadow-2xl">
  学习轨迹
</span>
            </h1>

            <!-- 优化副标题 -->
            <p class="text-[clamp(1.2rem,2.5vw,1.5rem)] text-white/90 mb-12 max-w-3xl mx-auto font-normal leading-relaxed tracking-wide">
              整合教学系统、实验室和图书馆数据，生成个性化学业画像，助你全面了解学习状况，规划未来发展
            </p>

            <!-- 优化按钮布局 -->
            <div class="flex flex-col sm:flex-row justify-center gap-6">
              <!-- 立即开始按钮（登录后隐藏） -->
              <button
                  v-if="!isAuthenticated"
                  @click="openAuthModal('login')"
                  class="px-12 py-5 bg-gradient-to-r from-purple-500 to-indigo-600 text-white font-bold rounded-2xl hover:from-purple-600 hover:to-indigo-700 transition-all duration-300 transform hover:-translate-y-1 shadow-2xl hover:shadow-3xl text-xl backdrop-blur-sm border border-purple-400/30 hover:glow"
              >
                <i class="fas fa-rocket mr-3"></i>
                立即开始
              </button>
              <router-link
                  to="/academic-profile"
                  class="px-12 py-5 bg-white/10 backdrop-blur-lg border-2 border-white/30 text-white font-bold rounded-2xl hover:bg-white/20 hover:border-white/50 transition-all duration-300 transform hover:-translate-y-1 text-xl shadow-xl hover:shadow-2xl"
              >
                <i class="fas fa-chart-line mr-3"></i>
                {{ isAuthenticated ? '进入个人中心' : '探索功能' }}
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 特点介绍 -->
      <section id="features" class="py-20 bg-gradient-to-br from-blue-50 to-indigo-50">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-4 text-indigo-800">强大功能，助力学业发展</h2>
            <p class="text-slate-600 max-w-2xl mx-auto text-lg">我们整合多维度数据，为你提供全方位的学业分析与建议</p>
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
                bgClass="bg-indigo-100"
                textClass="text-indigo-600"
            />

            <FeatureCard
                icon="book"
                title="阅读习惯"
                description="分析图书馆借阅记录，展示阅读偏好与知识结构，发现学习兴趣点。"
                bgClass="bg-purple-100"
                textClass="text-purple-600"
            />
          </div>
        </div>
      </section>

      <!-- 关于我们/校园学习 -->
      <section id="school-learning" class="py-20 bg-gradient-to-br from-indigo-50 to-purple-50">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex flex-col lg:flex-row items-center gap-12">
            <div class="lg:w-1/2">
              <div class="rounded-2xl overflow-hidden shadow-2xl transform hover:scale-105 transition-all duration-500">
                <img src="https://picsum.photos/600/400?random=1" alt="学生使用AI 智能·学习搭子系统" class="w-full h-auto">
              </div>
            </div>
            <div class="lg:w-1/2">
              <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-6 text-indigo-800">关于AI 智能·学习搭子系统</h2>
              <p class="text-slate-600 mb-4 text-lg leading-relaxed">AI 智能·学习搭子是一款专为学生设计的综合性学习分析工具，通过整合教务系统、实验室和图书馆等多维度数据，为每位学生生成个性化的学业发展报告。</p>
              <p class="text-slate-600 mb-6 text-lg leading-relaxed">我们的系统不仅能帮助你全面了解自己的学习状况，还能根据你的特点提供个性化的学习建议和发展规划，让你的学习更有效率，未来发展更明确。</p>
              <div class="grid grid-cols-2 gap-4">
                <div class="flex items-center text-indigo-700 font-medium">
                  <i class="fas fa-shield-alt text-indigo-500 mr-3 text-lg"></i>
                  <span>数据安全加密</span>
                </div>
                <div class="flex items-center text-indigo-700 font-medium">
                  <i class="fas fa-sync-alt text-indigo-500 mr-3 text-lg"></i>
                  <span>实时数据更新</span>
                </div>
                <div class="flex items-center text-indigo-700 font-medium">
                  <i class="fas fa-user-cog text-indigo-500 mr-3 text-lg"></i>
                  <span>个性化分析</span>
                </div>
                <div class="flex items-center text-indigo-700 font-medium">
                  <i class="fas fa-lightbulb text-indigo-500 mr-3 text-lg"></i>
                  <span>专业发展建议</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 就业准备 -->
      <section id="employment-preparation" class="py-20 bg-gradient-to-br from-blue-50 to-cyan-50">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-4 text-blue-800">就业准备模块</h2>
            <p class="text-slate-600 max-w-2xl mx-auto text-lg">结合你的学习数据，提供个性化就业指导与技能提升建议</p>
          </div>
          <div class="max-w-4xl mx-auto bg-white/80 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-blue-200">
            <p class="text-slate-700 mb-6 text-lg leading-relaxed">系统将根据你的专业方向、成绩表现和实践经历，匹配适合的职业方向，并推荐需要提升的技能课程，帮助你提前做好就业准备。</p>
            <router-link
                to="/job-matching"
                class="px-8 py-3 bg-gradient-to-r from-blue-500 to-indigo-500 text-white font-bold rounded-2xl hover:from-blue-600 hover:to-indigo-600 transition-all duration-300 transform hover:-translate-y-1 shadow-lg hover:shadow-xl inline-flex items-center text-lg"
            >
              <i class="fas fa-briefcase mr-3"></i>
              查看我的就业建议
            </router-link>
          </div>
        </div>
      </section>

      <!-- 知识答疑 -->
      <section id="knowledge-answering" class="py-20 bg-gradient-to-br from-indigo-50 to-purple-50">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-4 text-purple-800">智能知识答疑</h2>
            <p class="text-slate-600 max-w-2xl mx-auto text-lg">24小时在线答疑，解决你的学习困惑</p>
          </div>
          <div class="max-w-4xl mx-auto bg-white/80 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-purple-200">
            <p class="text-slate-700 mb-6 text-lg leading-relaxed">无论是课程知识点疑问，还是作业难题，AI智能答疑系统都能为你提供详细解答，帮助你扫清学习障碍。</p>
            <router-link
                to="/knowledge-answering"
                class="px-8 py-3 bg-gradient-to-r from-purple-500 to-indigo-500 text-white font-bold rounded-2xl hover:from-purple-600 hover:to-indigo-600 transition-all duration-300 transform hover:-translate-y-1 shadow-lg hover:shadow-xl inline-flex items-center text-lg"
            >
              <i class="fas fa-question-circle mr-3"></i>
              立即提问
            </router-link>
          </div>
        </div>
      </section>

      <!-- 心理陪伴 -->
      <section id="psychology-companionship" class="py-20 bg-gradient-to-br from-purple-50 to-pink-50">
        <div class="container mx-auto px-4 sm:px-6 lg:px-8">
          <div class="text-center mb-16">
            <h2 class="text-[clamp(1.5rem,3vw,2.5rem)] font-bold mb-4 text-purple-800">心理陪伴服务</h2>
            <p class="text-slate-600 max-w-2xl mx-auto text-lg">关注你的心理健康，提供专业心理疏导与情绪支持</p>
          </div>
          <div class="max-w-4xl mx-auto bg-white/80 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-purple-200">
            <p class="text-slate-700 mb-6 text-lg leading-relaxed">学习压力大？情绪低落？心理陪伴模块为你提供专业的心理疏导服务，帮助你保持良好的学习心态。</p>
            <router-link
                to="/psychology-companion"
                class="px-8 py-3 bg-gradient-to-r from-purple-500 to-pink-500 text-white font-bold rounded-2xl hover:from-purple-600 hover:to-pink-600 transition-all duration-300 transform hover:-translate-y-1 shadow-lg hover:shadow-xl inline-flex items-center text-lg"
            >
              <i class="fas fa-heart mr-3"></i>
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
import { ref, onMounted, computed, onUnmounted, reactive } from 'vue'
import TopNavbar from '@/component/TopNavbar.vue'
import LoginRegister from './LoginRegister.vue'
import FeatureCard from '@/component/FeatureCard.vue'
import PageFooter from '@/component/PageFooter.vue'

// 导入背景图片
import bgImage from '@/assets/bg1.jpg'

export default {
  name: 'HomeView',
  components: {
    TopNavbar,
    LoginRegister,
    FeatureCard,
    PageFooter,
  },
  setup() {
    // 登录/注册悬浮窗状态
    const authModalVisible = ref(false)
    const authMode = ref('login')

    // 英雄区域样式
    const heroStyle = reactive({
      backgroundImage: `url(${bgImage})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center 20%', // 调整图片显示位置，让重要内容显示在顶部
      backgroundRepeat: 'no-repeat',
      position: 'relative',
      marginTop: '-64px',
      paddingTop: '64px',
      minHeight: '70vh', // 增加高度但不覆盖整个视口
      display: 'flex',
      alignItems: 'center'
    })

    // 检查用户是否已登录
    const isAuthenticated = computed(() => {
      return localStorage.getItem('isAuthenticated') === 'true'
    })

    // 监听登录状态变化
    const handleStorageChange = () => {
      // 强制重新计算 computed
    }

    // 打开登录/注册悬浮窗
    const openAuthModal = (mode) => {
      authMode.value = mode
      authModalVisible.value = true
      document.body.style.overflow = 'hidden'
    }

    // 关闭登录/注册悬浮窗
    const closeAuthModal = () => {
      authModalVisible.value = false
      document.body.style.overflow = ''
    }

    // 设置登录/注册模式
    const setAuthMode = (mode) => {
      authMode.value = mode
    }

    // 生命周期钩子
    onMounted(() => {
      document.body.classList.add('opacity-100')
      document.body.classList.remove('opacity-0')

      // 监听 localStorage 变化
      window.addEventListener('storage', handleStorageChange)
      window.addEventListener('user-info-updated', handleStorageChange)
    })

    onUnmounted(() => {
      window.removeEventListener('storage', handleStorageChange)
      window.removeEventListener('user-info-updated', handleStorageChange)
    })

    return {
      authModalVisible,
      authMode,
      isAuthenticated,
      heroStyle, // 确保返回 heroStyle
      openAuthModal,
      closeAuthModal,
      setAuthMode,
    }
  }
}
</script>

<style scoped>
/* 按钮发光效果 - 改为紫色系 */
.hover\:glow:hover {
  box-shadow: 0 0 25px rgba(168, 85, 247, 0.5);
}

/* 自定义工具类 */
.text-shadow {
  text-shadow: 0 4px 8px rgba(0,0,0,0.3);
}

/* 毛玻璃效果增强 */
.backdrop-blur-sm {
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

/* 悬停动画增强 */
.hover-lift {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.hover-lift:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.15);
}

/* 按钮渐变效果 */
.btn-gradient {
  background: linear-gradient(135deg, #3B82F6 0%, #8B5CF6 100%);
  transition: all 0.3s ease;
}

.btn-gradient:hover {
  background: linear-gradient(135deg, #2563EB 0%, #7C3AED 100%);
  transform: translateY(-2px);
  box-shadow: 0 10px 25px rgba(59, 130, 246, 0.3);
}

/* 卡片悬停效果 */
.card-hover {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.card-hover:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0,0,0,0.1);
}

/* 响应式字体大小 */
@media (max-width: 768px) {
  .text-responsive {
    font-size: clamp(1rem, 4vw, 1.5rem);
  }
}

/* 边框渐变效果 */
.gradient-border {
  position: relative;
  background: white;
  border-radius: 1rem;
}

.gradient-border::before {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: linear-gradient(135deg, #3B82F6, #8B5CF6, #EC4899);
  border-radius: 1rem;
  z-index: -1;
  animation: gradientRotate 3s linear infinite;
}

@keyframes gradientRotate {
  0% { filter: hue-rotate(0deg); }
  100% { filter: hue-rotate(360deg); }
}

/* 文字渐变效果 */
.text-gradient {
  background: linear-gradient(135deg, #3B82F6 0%, #8B5CF6 50%, #EC4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}
</style>