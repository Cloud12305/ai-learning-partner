<template>
  <div class="pt-16 min-h-screen bg-neutral-50">
    <!-- 顶部信息栏 -->
    <div class="bg-white shadow-sm sticky top-16 z-40">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-col md:flex-row justify-between items-center py-4">
          <div class="flex items-center mb-4 md:mb-0">
            <div class="w-12 h-12 rounded-full overflow-hidden mr-4">
              <img :src="userInfo.avatar_url || 'https://picsum.photos/100/100?random=10'" alt="用户头像" class="w-full h-full object-cover">
            </div>
            <div>
              <h2 class="text-xl font-bold">{{ userInfo.name || '加载中...' }}</h2>
              <p class="text-neutral-500 text-sm">{{ userInfo.major }} | {{ userInfo.grade }} | 学号: {{ userInfo.student_id }}</p>
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
      <!-- 个人信息栏 -->
      <Card class="mb-8 shadow-card">
        <h3 class="text-xl font-semibold mb-6">个人信息</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div class="space-y-2">
            <p class="text-neutral-600"><strong>姓名:</strong> {{ userInfo.name }}</p>
            <p class="text-neutral-600"><strong>学号:</strong> {{ userInfo.student_id }}</p>
            <p class="text-neutral-600"><strong>学院:</strong> {{ userInfo.college }}</p>
          </div>
          <div class="space-y-2">
            <p class="text-neutral-600"><strong>专业:</strong> {{ userInfo.major }}</p>
            <p class="text-neutral-600"><strong>年级:</strong> {{ userInfo.grade }}</p>
            <p class="text-neutral-600"><strong>班级:</strong> {{ userInfo.class_name }}</p>
          </div>
          <div class="space-y-2">
            <p class="text-neutral-600"><strong>邮箱:</strong> {{ userInfo.email || '未设置' }}</p>
            <p class="text-neutral-600"><strong>电话:</strong> {{ userInfo.phone || '未设置' }}</p>
            <p class="text-neutral-600"><strong>入学时间:</strong> {{ formatDate(userInfo.enrollment_date) }}</p>
          </div>
        </div>
      </Card>

      <!-- 学业数据概览 -->
      <Card class="mb-8 shadow-card">
        <h3 class="text-xl font-semibold mb-6">学业数据概览</h3>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div class="text-center cursor-pointer" @click="showAllCourses = true">
            <div class="inline-flex items-center justify-center w-24 h-24 rounded-full bg-secondary/10 mb-3 hover:bg-secondary/20 transition-colors">
              <span class="text-3xl font-bold text-secondary">{{ academicStats.averageGPA }}</span>
            </div>
            <p class="text-neutral-600">平均GPA</p>
            <p class="text-sm text-neutral-400 mt-1">点击查看全部成绩</p>
          </div>
          <div class="text-center cursor-pointer" @click="showAllLabs = true">
            <div class="inline-flex items-center justify-center w-24 h-24 rounded-full bg-accent/10 mb-3 hover:bg-accent/20 transition-colors">
              <span class="text-3xl font-bold text-accent">{{ academicStats.labProjects }}</span>
            </div>
            <p class="text-neutral-600">实验项目</p>
            <p class="text-sm text-neutral-400 mt-1">点击查看实验室详情</p>
          </div>
          <div class="text-center cursor-pointer" @click="showAllBooks = true">
            <div class="inline-flex items-center justify-center w-24 h-24 rounded-full bg-success/10 mb-3 hover:bg-success/20 transition-colors">
              <span class="text-3xl font-bold text-success">{{ academicStats.borrowedBooks }}</span>
            </div>
            <p class="text-neutral-600">借阅书籍</p>
            <p class="text-sm text-neutral-400 mt-1">点击查看借阅详情</p>
          </div>
        </div>
      </Card>

      <!-- 教务系统成绩单 -->
      <Card class="mb-8 shadow-card">
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
          <h3 class="text-xl font-semibold">教务系统成绩单</h3>
          <div class="flex flex-wrap gap-2">
            <Button
                type="primary"
                class="px-3 py-1 text-sm"
                @click="showAllCourses = true"
            >
              查看全部成绩
            </Button>
          </div>
        </div>

        <!-- GPA趋势图 -->
        <div class="mb-8 h-64">
          <canvas id="gpaChart"></canvas>
        </div>

        <!-- 课程成绩表格 -->
        <CourseTable :courses="courses" />

        <div class="mt-4 text-center">
          <Button
              type="text"
              class="text-primary hover:text-primary/80 text-sm"
              @click="showAllCourses = true"
          >
            查看全部课程 <i class="fa fa-angle-right ml-1"></i>
          </Button>
        </div>
      </Card>

      <!-- 实验室情况 -->
      <Card class="mb-8 shadow-card">
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
          <h3 class="text-xl font-semibold">实验室情况</h3>
          <Button
              type="primary"
              class="px-3 py-1 text-sm"
              @click="showAllLabs = true"
          >
            查看实验室详情
          </Button>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <!-- 实验室参与统计 -->
          <div>
            <h4 class="text-lg font-medium mb-4">实验室参与统计</h4>
            <div class="h-64">
              <canvas id="labParticipationChart"></canvas>
            </div>
          </div>

          <!-- 实验室时间分布 -->
          <div>
            <h4 class="text-lg font-medium mb-4">实验室时间分布</h4>
            <div class="h-64">
              <canvas id="labTimeChart"></canvas>
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
        <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
          <h3 class="text-xl font-semibold">图书馆情况</h3>
          <Button
              type="primary"
              class="px-3 py-1 text-sm"
              @click="showAllBooks = true"
          >
            查看借阅详情
          </Button>
        </div>

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

    <!-- 全部课程弹窗 -->
    <a-modal
        v-model:visible="showAllCourses"
        title="全部课程成绩"
        width="90%"
        :footer="null"
    >
      <div class="max-h-96 overflow-y-auto">
        <CourseTable :courses="allCourses" />
      </div>
    </a-modal>

    <!-- 实验室详情弹窗 -->
    <a-modal
        v-model:visible="showAllLabs"
        title="实验室详情"
        width="90%"
        :footer="null"
    >
      <div class="space-y-6">
        <div>
          <h4 class="text-lg font-semibold mb-4">所有实验项目</h4>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <LabProjectCard
                v-for="project in allLabProjects"
                :key="project.id"
                :title="project.title"
                :description="project.description"
                :status="project.status"
                :date="project.date"
                :members="project.members"
            />
          </div>
        </div>

        <div>
          <h4 class="text-lg font-semibold mb-4">实验室统计</h4>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="h-64">
              <canvas id="detailLabChart"></canvas>
            </div>
            <div class="h-64">
              <canvas id="detailTimeChart"></canvas>
            </div>
          </div>
        </div>
      </div>
    </a-modal>

    <!-- 借阅详情弹窗 -->
    <a-modal
        v-model:visible="showAllBooks"
        title="借阅详情"
        width="90%"
        :footer="null"
    >
      <div class="space-y-6">
        <div>
          <h4 class="text-lg font-semibold mb-4">所有借阅书籍</h4>
          <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            <BookCard
                v-for="book in allBooks"
                :key="book.id"
                :title="book.title"
                :author="book.author"
                :category="book.category"
                :date="book.date"
                :image="book.image"
            />
          </div>
        </div>

        <div>
          <h4 class="text-lg font-semibold mb-4">借阅分析</h4>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="h-64">
              <canvas id="detailCategoryChart"></canvas>
            </div>
            <div class="h-64">
              <canvas id="detailTrendChart"></canvas>
            </div>
          </div>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Card, Button, message, Modal } from 'ant-design-vue'
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
    BookCard,
    'a-modal': Modal
  },
  setup() {
    const router = useRouter()

    // 弹窗控制
    const showAllCourses = ref(false)
    const showAllLabs = ref(false)
    const showAllBooks = ref(false)

    // 用户信息
    const userInfo = ref({
      name: '',
      student_id: '',
      major: '',
      grade: '',
      avatar_url: '',
      college: '',
      class_name: '',
      email: '',
      phone: '',
      enrollment_date: ''
    })

    // 学业统计数据
    const academicStats = ref({
      averageGPA: 0,
      labProjects: 0,
      borrowedBooks: 0
    })

    // 课程数据
    const courses = ref([])
    const allCourses = ref([])

    // 实验项目数据
    const labProjects = ref([])
    const allLabProjects = ref([])

    // 书籍数据
    const recentBooks = ref([])
    const allBooks = ref([])

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '未设置'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    // 获取用户信息
    const fetchUserInfo = async () => {
      try {
        const storedUser = localStorage.getItem('user')
        if (storedUser) {
          const userData = JSON.parse(storedUser)
          userInfo.value = {
            name: userData.name || '',
            student_id: userData.student_id || userData.studentId || userData.username || '',
            major: userData.major || '',
            grade: userData.grade || '',
            avatar_url: userData.avatar_url || userData.avatarUrl || '',
            college: userData.college || '',
            class_name: userData.class_name || userData.className || '',
            email: userData.email || '',
            phone: userData.phone || '',
            enrollment_date: userData.enrollment_date || userData.enrollmentDate || ''
          }

          console.log('学业画像用户信息:', userInfo.value)
          calculateAcademicStats()
        } else {
          message.warning('请先登录')
          router.push('/')
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        message.error('获取用户信息失败')
      }
    }

    // 计算学业统计数据
    const calculateAcademicStats = () => {
      academicStats.value = {
        averageGPA: 3.7,
        labProjects: 12,
        borrowedBooks: 36
      }
    }

    // 获取课程成绩数据
    const fetchCourseData = async () => {
      try {
        // 模拟数据
        courses.value = [
          { id: 1, name: '数据结构与算法', credit: 4, score: 92, grade: 'A', semester: '大二上' },
          { id: 2, name: '计算机组成原理', credit: 4, score: 85, grade: 'A-', semester: '大二上' },
          { id: 3, name: '操作系统', credit: 4, score: 88, grade: 'A-', semester: '大二下' },
          { id: 4, name: '计算机网络', credit: 3, score: 80, grade: 'B+', semester: '大二下' },
          { id: 5, name: '人工智能导论', credit: 3, score: 95, grade: 'A', semester: '大三上' }
        ]

        // 全部课程数据
        allCourses.value = [
          ...courses.value,
          { id: 6, name: '高等数学', credit: 6, score: 89, grade: 'A-', semester: '大一上' },
          { id: 7, name: '线性代数', credit: 4, score: 91, grade: 'A', semester: '大一上' },
          { id: 8, name: '概率论', credit: 4, score: 87, grade: 'A-', semester: '大一下' },
          { id: 9, name: 'C语言程序设计', credit: 3, score: 94, grade: 'A', semester: '大一下' },
          { id: 10, name: 'Java程序设计', credit: 3, score: 88, grade: 'A-', semester: '大二上' }
        ]
      } catch (error) {
        console.error('获取课程数据失败:', error)
      }
    }

    // 获取实验项目数据
    const fetchLabData = async () => {
      labProjects.value = [
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
      ]

      allLabProjects.value = [
        ...labProjects.value,
        {
          id: 4,
          title: 'Web应用开发',
          description: '基于Vue和Spring Boot的全栈Web应用开发',
          status: 'completed',
          date: '2023.03-2023.06',
          members: 3
        },
        {
          id: 5,
          title: '移动应用开发',
          description: '基于React Native的跨平台移动应用开发',
          status: 'completed',
          date: '2023.09-2023.12',
          members: 2
        },
        {
          id: 6,
          title: '数据库设计',
          description: '关系型数据库设计与优化实践',
          status: 'completed',
          date: '2024.03-2024.04',
          members: 4
        }
      ]
    }

    // 获取书籍数据
    const fetchBookData = async () => {
      recentBooks.value = [
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
      ]

      allBooks.value = [
        ...recentBooks.value,
        {
          id: 5,
          title: '计算机组成与设计',
          author: 'David A. Patterson',
          category: '计算机科学',
          date: '2024.06.20',
          image: 'https://picsum.photos/200/300?random=24'
        },
        {
          id: 6,
          title: '操作系统概念',
          author: 'Abraham Silberschatz',
          category: '计算机科学',
          date: '2024.05.15',
          image: 'https://picsum.photos/200/300?random=25'
        },
        {
          id: 7,
          title: '计算机网络',
          author: 'Andrew S. Tanenbaum',
          category: '计算机科学',
          date: '2024.04.10',
          image: 'https://picsum.photos/200/300?random=26'
        },
        {
          id: 8,
          title: '算法导论',
          author: 'Thomas H. Cormen',
          category: '计算机科学',
          date: '2024.03.05',
          image: 'https://picsum.photos/200/300?random=27'
        }
      ]
    }

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
          plugins: { legend: { display: false } },
          scales: { y: { min: 0, max: 4, ticks: { stepSize: 1 } } }
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

      // 实验室时间分布
      const timeCtx = document.getElementById('labTimeChart').getContext('2d')
      new Chart(timeCtx, {
        type: 'bar',
        data: {
          labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
          datasets: [{
            label: '实验室时长(小时)',
            data: [3, 4, 2, 5, 3, 6, 2],
            backgroundColor: '#36CFC9'
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } }
        }
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

    // 初始化详情图表
    const initDetailCharts = () => {
      // 实验室详情图表
      setTimeout(() => {
        if (showAllLabs.value) {
          const detailLabCtx = document.getElementById('detailLabChart')?.getContext('2d')
          if (detailLabCtx) {
            new Chart(detailLabCtx, {
              type: 'doughnut',
              data: {
                labels: ['算法实验室', 'AI实验室', '大数据实验室', '网络实验室', '软件工程实验室'],
                datasets: [{
                  data: [6, 5, 4, 3, 2],
                  backgroundColor: ['#165DFF', '#36CFC9', '#722ED1', '#FAAD14', '#FF4D4F']
                }]
              },
              options: { responsive: true, maintainAspectRatio: false }
            })
          }

          const detailTimeCtx = document.getElementById('detailTimeChart')?.getContext('2d')
          if (detailTimeCtx) {
            new Chart(detailTimeCtx, {
              type: 'line',
              data: {
                labels: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月'],
                datasets: [{
                  label: '实验室时长(小时)',
                  data: [25, 30, 35, 40, 45, 50, 48, 52, 55],
                  borderColor: '#36CFC9',
                  backgroundColor: 'rgba(54, 207, 201, 0.1)',
                  tension: 0.3,
                  fill: true
                }]
              },
              options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } }
              }
            })
          }
        }
      }, 100)

      // 书籍详情图表
      setTimeout(() => {
        if (showAllBooks.value) {
          const detailCategoryCtx = document.getElementById('detailCategoryChart')?.getContext('2d')
          if (detailCategoryCtx) {
            new Chart(detailCategoryCtx, {
              type: 'pie',
              data: {
                labels: ['计算机科学', '人工智能', '编程语言', '数学', '英语', '其他'],
                datasets: [{
                  data: [15, 10, 8, 6, 4, 3],
                  backgroundColor: ['#165DFF', '#36CFC9', '#722ED1', '#FAAD14', '#FF4D4F', '#A3A3A3']
                }]
              },
              options: { responsive: true, maintainAspectRatio: false }
            })
          }

          const detailTrendCtx = document.getElementById('detailTrendChart')?.getContext('2d')
          if (detailTrendCtx) {
            new Chart(detailTrendCtx, {
              type: 'line',
              data: {
                labels: ['2023.01', '2023.04', '2023.07', '2023.10', '2024.01', '2024.04', '2024.07'],
                datasets: [{
                  label: '累计借阅量',
                  data: [5, 12, 18, 25, 30, 34, 36],
                  borderColor: '#165DFF',
                  backgroundColor: 'rgba(22, 93, 255, 0.1)',
                  tension: 0.3,
                  fill: true
                }]
              },
              options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } }
              }
            })
          }
        }
      }, 100)
    }

    // 退出登录
    const handleLogout = () => {
      localStorage.removeItem('isAuthenticated')
      localStorage.removeItem('user')
      localStorage.removeItem('token')
      message.success('退出登录成功')
      router.push('/')
    }

    onMounted(async () => {
      await fetchUserInfo()
      await fetchCourseData()
      await fetchLabData()
      await fetchBookData()
      initCharts()

      // 监听弹窗变化，初始化详情图表
      setTimeout(() => {
        initDetailCharts()
      }, 500)
    })

    return {
      userInfo,
      academicStats,
      courses,
      labProjects,
      recentBooks,
      allCourses,
      allLabProjects,
      allBooks,
      showAllCourses,
      showAllLabs,
      showAllBooks,
      formatDate,
      handleLogout
    }
  }
}
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}
</style>