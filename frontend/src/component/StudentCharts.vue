<template>
  <div class="bg-white/90 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-indigo-100">
    <h2 class="text-xl font-bold text-indigo-800 mb-6">
      <i class="fas fa-chart-pie mr-3 text-indigo-500"></i>
      数据可视化
    </h2>

    <!-- 加载状态 -->
    <div v-if="loading" class="text-center py-8">
      <i class="fas fa-spinner fa-spin text-3xl text-indigo-500 mb-3"></i>
      <p class="text-slate-500">正在加载图表数据...</p>
    </div>

    <!-- 数据内容 -->
    <div v-else class="space-y-6">
      <!-- 成绩分布图 -->
      <div class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-xl p-6">
        <h3 class="text-lg font-semibold text-slate-700 mb-4 flex items-center">
          <i class="fas fa-graduation-cap mr-2 text-blue-500"></i>
          成绩分布
          <span class="ml-2 text-sm font-normal text-slate-500">
            (共 {{ totalCourses }} 门课程)
          </span>
        </h3>
        <div class="h-64 flex items-center justify-center">
          <canvas ref="gradesChart" class="max-w-full"></canvas>
        </div>
        <div v-if="courses.length > 0" class="mt-4 grid grid-cols-2 md:grid-cols-5 gap-2 text-center">
          <div v-for="(count, label) in scoreDistribution" :key="label" class="bg-white/70 rounded-lg p-2">
            <div class="text-sm font-medium text-slate-700">{{ label }}</div>
            <div class="text-lg font-bold text-slate-800">{{ count }}</div>
            <div class="text-xs text-slate-500">{{ getPercentage(count) }}%</div>
          </div>
        </div>
      </div>

      <!-- 图书分类图 -->
      <div class="bg-gradient-to-r from-emerald-50 to-teal-50 rounded-xl p-6">
        <h3 class="text-lg font-semibold text-slate-700 mb-4 flex items-center">
          <i class="fas fa-book-open mr-2 text-emerald-500"></i>
          图书借阅分类
          <span class="ml-2 text-sm font-normal text-slate-500">
            (共 {{ totalBooks }} 本图书)
          </span>
        </h3>
        <div class="h-64 flex items-center justify-center">
          <canvas ref="booksChart" class="max-w-full"></canvas>
        </div>
        <div v-if="books.length > 0" class="mt-4 text-center">
          <div class="inline-flex flex-wrap gap-2 justify-center">
            <div v-for="(count, category) in bookCategories" :key="category"
                 class="bg-white/70 rounded-lg px-3 py-1 flex items-center">
              <span class="w-3 h-3 rounded-full mr-2"
                    :style="{ backgroundColor: getCategoryColor(category) }"></span>
              <span class="text-sm text-slate-700">{{ category }}: {{ count }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 无数据提示 -->
    <div v-if="!loading && (courses.length === 0 || books.length === 0)"
         class="text-center py-8 text-slate-400">
      <i class="fas fa-chart-bar text-3xl mb-3"></i>
      <p v-if="courses.length === 0 && books.length === 0">暂无可视化数据</p>
      <p v-else-if="courses.length === 0">暂无课程数据</p>
      <p v-else-if="books.length === 0">暂无图书数据</p>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, onBeforeUnmount, computed} from 'vue'
import Chart from 'chart.js/auto'
import {getAllCourses, getAllBooks} from '@/services/api/academic'

// 引用
const gradesChart = ref(null)
const booksChart = ref(null)

// 数据状态
const courses = ref([])
const books = ref([])
const loading = ref(false)
let gradesChartInstance = null
let booksChartInstance = null

// 颜色配置
const chartColors = {
  grades: {
    '优秀(90-100)': 'rgb(34, 197, 94)',
    '良好(80-89)': 'rgb(59, 130, 246)',
    '中等(70-79)': 'rgb(168, 85, 247)',
    '及格(60-69)': 'rgb(249, 115, 22)',
    '不及格(<60)': 'rgb(239, 68, 68)'
  },
  books: [
    'rgb(6, 182, 212)',
    'rgb(14, 165, 233)',
    'rgb(139, 92, 246)',
    'rgb(192, 132, 252)',
    'rgb(244, 114, 182)',
    'rgb(251, 146, 60)',
    'rgb(34, 197, 94)',
    'rgb(59, 130, 246)',
    'rgb(245, 158, 11)',
    'rgb(236, 72, 153)'
  ]
}

// 计算属性
const totalCourses = computed(() => courses.value.length)
const totalBooks = computed(() => books.value.length)

const scoreDistribution = computed(() => {
  if (courses.value.length === 0) {
    return {
      '优秀(90-100)': 0,
      '良好(80-89)': 0,
      '中等(70-79)': 0,
      '及格(60-69)': 0,
      '不及格(<60)': 0
    }
  }

  const scores = courses.value
      .map(course => course.score)
      .filter(score => score !== undefined && score !== null)
      .map(score => Number(score))

  return {
    '优秀(90-100)': scores.filter(s => s >= 90).length,
    '良好(80-89)': scores.filter(s => s >= 80 && s < 90).length,
    '中等(70-79)': scores.filter(s => s >= 70 && s < 80).length,
    '及格(60-69)': scores.filter(s => s >= 60 && s < 70).length,
    '不及格(<60)': scores.filter(s => s < 60).length
  }
})

const bookCategories = computed(() => {
  const categories = {}
  books.value.forEach(book => {
    const category = book.bookCategory || '未分类'
    categories[category] = (categories[category] || 0) + 1
  })
  return categories
})

// 获取百分比
const getPercentage = (count) => {
  const total = totalCourses.value
  return total > 0 ? ((count / total) * 100).toFixed(1) : 0
}

// 获取分类颜色
const getCategoryColor = (category) => {
  const categories = Object.keys(bookCategories.value)
  const index = categories.indexOf(category)
  return chartColors.books[index % chartColors.books.length]
}

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    console.log('开始加载图表数据...')

    // 并行加载课程和图书数据
    const [coursesResponse, booksResponse] = await Promise.all([
      getAllCourses(),
      getAllBooks()
    ])

    console.log('课程数据响应:', coursesResponse)
    console.log('图书数据响应:', booksResponse)

    // 处理课程数据 - 你的API直接返回数组
    if (coursesResponse && coursesResponse.data) {
      courses.value = Array.isArray(coursesResponse.data) ? coursesResponse.data : []
      console.log(`获取到 ${courses.value.length} 门课程数据`)
    } else {
      console.warn('课程数据接口返回异常:', coursesResponse)
      courses.value = []
    }

    // 处理图书数据 - 你的API直接返回数组
    if (booksResponse && booksResponse.data) {
      books.value = Array.isArray(booksResponse.data) ? booksResponse.data : []
      console.log(`获取到 ${books.value.length} 条借阅记录`)
    } else {
      console.warn('借阅记录接口返回异常:', booksResponse)
      books.value = []
    }

    // 初始化图表
    setTimeout(initCharts, 100)

  } catch (error) {
    console.error('加载图表数据失败:', error)
    courses.value = []
    books.value = []
  } finally {
    loading.value = false
  }
}

// 初始化图表
const initCharts = () => {
  // 销毁旧图表
  if (gradesChartInstance) gradesChartInstance.destroy()
  if (booksChartInstance) booksChartInstance.destroy()

  // 初始化成绩分布图
  if (gradesChart.value && courses.value.length > 0) {
    const labels = Object.keys(scoreDistribution.value)
    const data = Object.values(scoreDistribution.value)
    const backgroundColors = labels.map(label => chartColors.grades[label])

    gradesChartInstance = new Chart(gradesChart.value, {
      type: 'doughnut',
      data: {
        labels: labels,
        datasets: [{
          data: data,
          backgroundColor: backgroundColors,
          borderWidth: 2,
          borderColor: 'white',
          hoverBorderWidth: 3,
          hoverBorderColor: 'rgb(243, 244, 246)'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'right',
            labels: {
              padding: 15,
              usePointStyle: true,
              font: {
                size: 11
              }
            }
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                const label = context.label || ''
                const value = context.raw || 0
                const total = context.dataset.data.reduce((a, b) => a + b, 0)
                const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0
                return `${label}: ${value} 门 (${percentage}%)`
              }
            }
          }
        },
        cutout: '60%'
      }
    })
  }

  // 初始化图书分类图
  if (booksChart.value && books.value.length > 0) {
    const categories = bookCategories.value
    const labels = Object.keys(categories)
    const data = Object.values(categories)
    const backgroundColors = labels.map((_, index) =>
        chartColors.books[index % chartColors.books.length]
    )

    // 如果分类太多，合并小分类
    let finalLabels = labels
    let finalData = data
    let finalColors = backgroundColors

    if (labels.length > 8) {
      const sortedEntries = Object.entries(categories)
          .sort((a, b) => b[1] - a[1])

      // 取前7个分类
      finalLabels = sortedEntries.slice(0, 7).map(entry => entry[0])
      finalData = sortedEntries.slice(0, 7).map(entry => entry[1])
      finalColors = finalLabels.map((_, index) =>
          chartColors.books[index % chartColors.books.length]
      )

      // 合并剩余的为"其他"
      const otherCount = sortedEntries.slice(7).reduce((sum, [, count]) => sum + count, 0)
      if (otherCount > 0) {
        finalLabels.push('其他')
        finalData.push(otherCount)
        finalColors.push('rgb(156, 163, 175)')
      }
    }

    booksChartInstance = new Chart(booksChart.value, {
      type: 'pie',
      data: {
        labels: finalLabels,
        datasets: [{
          data: finalData,
          backgroundColor: finalColors,
          borderWidth: 2,
          borderColor: 'white',
          hoverBorderWidth: 3,
          hoverBorderColor: 'rgb(243, 244, 246)'
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'right',
            labels: {
              padding: 15,
              usePointStyle: true,
              font: {
                size: 11
              }
            }
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                const label = context.label || ''
                const value = context.raw || 0
                const total = context.dataset.data.reduce((a, b) => a + b, 0)
                const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0
                return `${label}: ${value} 本 (${percentage}%)`
              }
            }
          }
        }
      }
    })
  }
}

onMounted(() => {
  console.log('数据可视化组件挂载')
  loadData()
})

onBeforeUnmount(() => {
  if (gradesChartInstance) {
    gradesChartInstance.destroy()
  }
  if (booksChartInstance) {
    booksChartInstance.destroy()
  }
})
</script>