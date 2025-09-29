<template>
  <div class="pt-16 min-h-screen bg-neutral-50">
    <!-- 顶部信息栏 -->
    <div class="bg-white shadow-sm sticky top-16 z-40">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-col md:flex-row justify-between items-center py-4">
          <div class="flex items-center mb-4 md:mb-0">
            <div class="w-12 h-12 rounded-full overflow-hidden mr-4">
              <img src="https://picsum.photos/100/100?random=10" alt="用户头像" class="w-full h-full object-cover">
            </div>
            <div>
              <h2 class="text-xl font-bold">张明</h2>
              <p class="text-neutral-500 text-sm">计算机科学与技术 | 大yi | 学号: 2021001234</p>
            </div>
          </div>
          <div class="flex items-center space-x-4">
            <Button type="text" class="text-neutral-600 hover:text-primary transition-colors">
              <i class="fa fa-bell-o text-xl"></i>
            </Button>
            <Button type="text" class="text-neutral-600 hover:text-primary transition-colors">
              <i class="fa fa-cog text-xl"></i>
            </Button>
            <Button
                @click="handleLogout"
                class="px-4 py-2 text-neutral-600 border border-neutral-300 rounded-lg hover:bg-neutral-50 transition-colors"
            >
              退出登录
            </Button>
          </div>
        </div>
      </div>
    </div>

    <!-- 主要内容 -->
    <div class="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- 总体学业评分 -->
      <Card class="mb-8 shadow-card">
        <h3 class="text-xl font-semibold mb-6">总体学业评分</h3>
        <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
          <div class="text-center">
            <div class="inline-flex items-center justify-center w-24 h-24 rounded-full bg-primary/10 mb-3">
              <span class="text-3xl font-bold text-primary">88</span>
            </div>
            <p class="text-neutral-600">综合评分</p>
          </div>
          <div class="text-center">
            <div class="inline-flex items-center justify-center w-24 h-24 rounded-full bg-secondary/10 mb-3">
              <span class="text-3xl font-bold text-secondary">3.7</span>
            </div>
            <p class="text-neutral-600">平均GPA</p>
          </div>
          <div class="text-center">
            <div class="inline-flex items-center justify-center w-24 h-24 rounded-full bg-accent/10 mb-3">
              <span class="text-3xl font-bold text-accent">12</span>
            </div>
            <p class="text-neutral-600">实验项目</p>
          </div>
          <div class="text-center">
            <div class="inline-flex items-center justify-center w-24 h-24 rounded-full bg-success/10 mb-3">
              <span class="text-3xl font-bold text-success">36</span>
            </div>
            <p class="text-neutral-600">借阅书籍</p>
          </div>
        </div>
      </Card>

      <!-- 教务系统成绩单 -->
      <Card class="mb-8 shadow-card">
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
          <h3 class="text-xl font-semibold">教务系统成绩单</h3>
          <div class="flex flex-wrap gap-2">
            <Button type="primary" class="px-3 py-1 text-sm">全部</Button>
            <Button type="text" class="px-3 py-1 text-sm bg-neutral-100 text-neutral-600 hover:bg-neutral-200">大一</Button>
            <Button type="text" class="px-3 py-1 text-sm bg-neutral-100 text-neutral-600 hover:bg-neutral-200">大二</Button>
            <Button type="text" class="px-3 py-1 text-sm bg-neutral-100 text-neutral-600 hover:bg-neutral-200">大三</Button>
          </div>
        </div>

        <!-- GPA趋势图 -->
        <div class="mb-8 h-64">
          <canvas id="gpaChart"></canvas>
        </div>

        <!-- 课程成绩表格 -->
        <CourseTable :courses="courses" />

        <div class="mt-4 text-center">
          <Button type="text" class="text-primary hover:text-primary/80 text-sm">
            查看全部课程 <i class="fa fa-angle-right ml-1"></i>
          </Button>
        </div>
      </Card>

      <!-- 实验室情况 -->
      <Card class="mb-8 shadow-card">
        <h3 class="text-xl font-semibold mb-6">实验室情况</h3>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <!-- 实验室参与统计 -->
          <div>
            <h4 class="text-lg font-medium mb-4">实验室参与统计</h4>
            <div class="h-64">
              <canvas id="labParticipationChart"></canvas>
            </div>
          </div>

          <!-- 技能掌握情况 -->
          <div>
            <h4 class="text-lg font-medium mb-4">技能掌握情况</h4>
            <div class="space-y-4">
              <div v-for="skill in skills" :key="skill.name">
                <div class="flex justify-between mb-1">
                  <span class="text-sm font-medium">{{ skill.name }}</span>
                  <span class="text-sm text-neutral-500">{{ skill.level }}%</span>
                </div>
                <div class="w-full bg-neutral-200 rounded-full h-2">
                  <div class="bg-primary h-2 rounded-full" :style="{ width: skill.level + '%' }"></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 参与的实验项目 -->
        <h4 class="text-lg font-medium mb-4">参与的实验项目</h4>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <LabProjectCard
              v-for="project in labProjects"
              :key="project.id"
              :title="project.title"
              :description="project.description"
              :status="project.status"
              :date="project.date"
              :members="project.members"
          />
        </div>
      </Card>

      <!-- 图书馆情况 -->
      <Card class="shadow-card">
        <h3 class="text-xl font-semibold mb-6">图书馆情况</h3>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <!-- 借阅类别分布 -->
          <div>
            <h4 class="text-lg font-medium mb-4">借阅类别分布</h4>
            <div class="h-64">
              <canvas id="bookCategoryChart"></canvas>
            </div>
          </div>

          <!-- 借阅趋势 -->
          <div>
            <h4 class="text-lg font-medium mb-4">借阅趋势</h4>
            <div class="h-64">
              <canvas id="borrowingTrendChart"></canvas>
            </div>
          </div>
        </div>

        <!-- 最近借阅书籍 -->
        <h4 class="text-lg font-medium mb-4">最近借阅书籍</h4>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <BookCard
              v-for="book in recentBooks"
              :key="book.id"
              :title="book.title"
              :author="book.author"
              :category="book.category"
              :date="book.date"
              :image="book.image"
          />
        </div>
      </Card>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Card, Button, message } from 'ant-design-vue'
import { Chart, registerables } from 'chart.js'
import CourseTable from '../components/CourseTable.vue'
import LabProjectCard from '../components/LabProjectCard.vue'
import BookCard from '../components/BookCard.vue'

// 注册Chart.js组件
Chart.register(...registerables)

export default {
  components: {
    Card,
    Button,
    CourseTable,
    LabProjectCard,
    BookCard
  },
  setup() {
    const router = useRouter()

    // 课程数据
    const courses = ref([
      { id: 1, name: '数据结构与算法', credit: 4, score: 92, grade: 'A', semester: '大二上' },
      { id: 2, name: '计算机组成原理', credit: 4, score: 85, grade: 'A-', semester: '大二上' },
      { id: 3, name: '操作系统', credit: 4, score: 88, grade: 'A-', semester: '大二下' },
      { id: 4, name: '计算机网络', credit: 3, score: 80, grade: 'B+', semester: '大二下' },
      { id: 5, name: '人工智能导论', credit: 3, score: 95, grade: 'A', semester: '大三上' }
    ])

    // 技能数据
    const skills = ref([
      { name: 'Python编程', level: 90 },
      { name: '机器学习', level: 75 },
      { name: '数据可视化', level: 80 },
      { name: '深度学习框架', level: 65 },
      { name: '数据库设计', level: 85 }
    ])

    // 实验项目数据
    const labProjects = ref([
      {
        id: 1,
        title: '智能推荐系统',
        description: '基于协同过滤算法的个性化推荐系统设计与实现',
        status: 'completed',
        date: '2023.09-2023.12',
        members: 3
      },
      {
        id: 2,
        title: '图像识别应用',
        description: '基于CNN的图像分类与识别系统开发',
        status: 'completed',
        date: '2024.03-2024.06',
        members: 4
      },
      {
        id: 3,
        title: '大数据分析平台',
        description: '基于Spark的分布式数据处理与分析平台',
        status: 'inProgress',
        date: '2024.09-至今',
        members: 5
      }
    ])

    // 最近借阅书籍
    const recentBooks = ref([
      {
        id: 1,
        title: '深度学习',
        author: 'Ian Goodfellow 等',
        category: '计算机科学',
        date: '2024.09.15',
        image: 'https://picsum.photos/200/300?random=20'
      },
      {
        id: 2,
        title: 'Python编程：从入门到实践',
        author: 'Eric Matthes',
        category: '编程语言',
        date: '2024.08.22',
        image: 'https://picsum.photos/200/300?random=21'
      },
      {
        id: 3,
        title: '数据结构与算法分析',
        author: 'Mark Allen Weiss',
        category: '计算机科学',
        date: '2024.07.30',
        image: 'https://picsum.photos/200/300?random=22'
      },
      {
        id: 4,
        title: '人工智能：一种现代方法',
        author: 'Stuart Russell',
        category: '人工智能',
        date: '2024.07.15',
        image: 'https://picsum.photos/200/300?random=23'
      }
    ])

    // 初始化图表
    const initCharts = () => {
      // GPA趋势图
      const gpaCtx = document.getElementById('gpaChart').getContext('2d')
      new Chart(gpaCtx, {
        type: 'line',
        data: {
          labels: ['大一上', '大一下', '大二上', '大二下', '大三上'],
          datasets: [{
            label: 'GPA',
            data: [3.2, 3.5, 3.6, 3.7, 3.8],
            borderColor: '#165DFF',
            backgroundColor: 'rgba(22, 93, 255, 0.1)',
            tension: 0.3,
            fill: true
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { display: false }
          },
          scales: {
            y: { min: 0, max: 4, ticks: { stepSize: 1 } }
          }
        }
      })

      // 实验室参与统计
      const labCtx = document.getElementById('labParticipationChart').getContext('2d')
      new Chart(labCtx, {
        type: 'doughnut',
        data: {
          labels: ['算法实验室', 'AI实验室', '大数据实验室', '网络实验室'],
          datasets: [{
            data: [4, 3, 3, 2],
            backgroundColor: ['#165DFF', '#36CFC9', '#722ED1', '#FAAD14']
          }]
        },
        options: { responsive: true, maintainAspectRatio: false }
      })

      // 借阅类别分布
      const categoryCtx = document.getElementById('bookCategoryChart').getContext('2d')
      new Chart(categoryCtx, {
        type: 'pie',
        data: {
          labels: ['计算机科学', '人工智能', '编程语言', '数学', '其他'],
          datasets: [{
            data: [12, 8, 6, 5, 5],
            backgroundColor: ['#165DFF', '#36CFC9', '#722ED1', '#FAAD14', '#A3A3A3']
          }]
        },
        options: { responsive: true, maintainAspectRatio: false }
      })

      // 借阅趋势
      const trendCtx = document.getElementById('borrowingTrendChart').getContext('2d')
      new Chart(trendCtx, {
        type: 'bar',
        data: {
          labels: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月'],
          datasets: [{
            label: '借阅数量',
            data: [2, 1, 4, 3, 5, 3, 6, 4, 2],
            backgroundColor: '#165DFF'
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
        }
      })
    }

    // 退出登录
    const handleLogout = () => {
      localStorage.setItem('isAuthenticated', 'false')
      message.success('退出登录成功')
      router.push('/')
    }

    onMounted(() => {
      initCharts()
    })

    return {
      courses,
      skills,
      labProjects,
      recentBooks,
      handleLogout
    }
  }
}
</script>
