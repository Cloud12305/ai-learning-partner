<template>
  <div class="bg-gradient-to-br from-indigo-500/10 to-purple-500/10 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-indigo-100/50">
    <h2 class="text-2xl font-bold text-indigo-800 mb-8">
      <i class="fas fa-chart-bar mr-3 text-indigo-500"></i>
      学业概览
    </h2>

    <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
      <!-- 学年学期绩点卡片 -->
      <div class="bg-white/90 backdrop-blur-sm rounded-2xl p-6 shadow-lg transform hover:-translate-y-1 transition-all duration-300">
        <div class="flex items-center justify-between mb-4">
          <div class="w-14 h-14 bg-gradient-to-r from-blue-500 to-indigo-500 rounded-xl flex items-center justify-center">
            <i class="fas fa-calendar-alt text-2xl text-white"></i>
          </div>
          <div class="text-right">
            <span class="text-3xl font-bold text-slate-800 block">
              {{ currentSemesterGPA }}
            </span>
            <span class="text-sm text-slate-500 block">
              <template v-if="currentAcademicYear !== '--' && currentSemester !== '--'">
                {{ currentAcademicYear }} 第{{ currentSemester }}学期
              </template>
              <template v-else>
                --
              </template>
            </span>
          </div>
        </div>
        <h3 class="text-lg font-semibold text-slate-700 mb-2">本学期绩点</h3>
        <p class="text-slate-500 text-sm">当前学期的平均绩点</p>
      </div>

      <!-- 累计绩点卡片 -->
      <div class="bg-white/90 backdrop-blur-sm rounded-2xl p-6 shadow-lg transform hover:-translate-y-1 transition-all duration-300">
        <div class="flex items-center justify-between mb-4">
          <div class="w-14 h-14 bg-gradient-to-r from-purple-500 to-pink-500 rounded-xl flex items-center justify-center">
            <i class="fas fa-chart-line text-2xl text-white"></i>
          </div>
          <div class="text-right">
            <span class="text-3xl font-bold text-slate-800 block">
              {{ overallGPA }}
            </span>
            <span class="text-sm text-slate-500 block">
              累计 {{ totalCredits }} 学分
            </span>
          </div>
        </div>
        <h3 class="text-lg font-semibold text-slate-700 mb-2">累计平均绩点</h3>
        <p class="text-slate-500 text-sm">所有学期的加权平均</p>
      </div>

      <!-- 实验项目卡片 -->
      <div class="bg-white/90 backdrop-blur-sm rounded-2xl p-6 shadow-lg transform hover:-translate-y-1 transition-all duration-300">
        <div class="flex items-center justify-between mb-4">
          <div class="w-14 h-14 bg-gradient-to-r from-emerald-500 to-teal-500 rounded-xl flex items-center justify-center">
            <i class="fas fa-flask text-2xl text-white"></i>
          </div>
          <span class="text-3xl font-bold text-slate-800">
            {{ labProjectsCount }}
          </span>
        </div>
        <h3 class="text-lg font-semibold text-slate-700 mb-2">实验项目</h3>
        <p class="text-slate-500 text-sm">展示你的实践能力</p>
      </div>

      <!-- 借阅书籍卡片 -->
      <div class="bg-white/90 backdrop-blur-sm rounded-2xl p-6 shadow-lg transform hover:-translate-y-1 transition-all duration-300">
        <div class="flex items-center justify-between mb-4">
          <div class="w-14 h-14 bg-gradient-to-r from-orange-500 to-red-500 rounded-xl flex items-center justify-center">
            <i class="fas fa-book-open text-2xl text-white"></i>
          </div>
          <span class="text-3xl font-bold text-slate-800">
            {{ totalBorrowedBooks }}
          </span>
        </div>
        <h3 class="text-lg font-semibold text-slate-700 mb-2">借阅书籍</h3>
        <p class="text-slate-500 text-sm">借阅过的所有书籍</p>
        <div v-if="totalBorrowedBooks > 0" class="mt-2 text-xs text-slate-500">
          <span class="inline-block mr-3">📚 总数</span>
        </div>
      </div>
    </div>

    <!-- 学年学期绩点详情表格 -->
    <div v-if="semesterGPAs && semesterGPAs.length > 0" class="mt-8 bg-white/90 backdrop-blur-sm rounded-2xl p-6 shadow-lg">
      <h3 class="text-lg font-semibold text-slate-700 mb-4">各学期绩点详情</h3>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-slate-200">
          <thead>
          <tr class="bg-slate-50">
            <th class="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">学年</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">学期</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">学分</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">平均绩点</th>
            <th class="px-4 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">课程数</th>
          </tr>
          </thead>
          <tbody class="divide-y divide-slate-200">
          <tr v-for="(semester, index) in semesterGPAs" :key="index" class="hover:bg-slate-50">
            <td class="px-4 py-3 text-sm text-slate-800">{{ semester.academicYear }}</td>
            <td class="px-4 py-3 text-sm text-slate-800">第{{ semester.semester }}学期</td>
            <td class="px-4 py-3 text-sm text-slate-800">{{ semester.totalCredits }}</td>
            <td class="px-4 py-3 text-sm">
                <span :class="[
                  'px-2 py-1 rounded-full text-xs font-medium',
                  getGPAClass(semester.averageGPA)
                ]">
                  {{ formatGPA(semester.averageGPA) }}
                </span>
            </td>
            <td class="px-4 py-3 text-sm text-slate-800">{{ semester.courseCount }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 无数据提示 -->
    <div v-if="!loading && semesterGPAs.length === 0" class="mt-8 bg-white/90 backdrop-blur-sm rounded-2xl p-8 shadow-lg text-center">
      <i class="fas fa-graduation-cap text-4xl text-slate-300 mb-4"></i>
      <p class="text-slate-500">暂无学业数据</p>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import { getAcademicOverview, getAllBooks } from '@/services/api/academic'

// 数据状态
const currentAcademicYear = ref('--')
const currentSemester = ref('--')
const currentSemesterGPA = ref('0.00')
const overallGPA = ref('0.00')
const totalCredits = ref('0')
const labProjectsCount = ref(0)
const totalBorrowedBooks = ref(0)
const semesterGPAs = ref([])
const loading = ref(true)

// 格式化绩点
const formatGPA = (gpa) => {
  if (gpa === undefined || gpa === null) return '0.00'
  if (typeof gpa === 'number') return gpa.toFixed(2)
  if (typeof gpa === 'string') {
    const num = parseFloat(gpa)
    return isNaN(num) ? '0.00' : num.toFixed(2)
  }
  return '0.00'
}

// 获取学业概览数据
const fetchAcademicOverview = async () => {
  try {
    loading.value = true
    console.log(' 开始获取学业概览数据...')

    // 并行获取概览数据和借阅记录
    const [overviewResponse, booksResponse] = await Promise.all([
      getAcademicOverview(),
      getAllBooks()
    ])

    console.log('学业概览API响应:', overviewResponse)
    console.log(' 借阅记录API响应:', booksResponse)

    if (overviewResponse && overviewResponse.data) {
      const data = overviewResponse.data

      // 打印详细日志
      console.log('学业数据详情:', data)

      // 赋值学业数据
      currentAcademicYear.value = data.currentAcademicYear || '--'
      currentSemester.value = data.currentSemester || '--'
      currentSemesterGPA.value = formatGPA(data.currentSemesterGPA)
      overallGPA.value = formatGPA(data.overallGPA)
      totalCredits.value = data.totalCredits || '0'
      labProjectsCount.value = data.labProjectsCount || 0
      semesterGPAs.value = data.semesterGPAs || []
    } else {
      console.warn('学业概览响应结构不正确')
      resetAcademicData()
    }

    // 处理借阅记录数据 - 你的API直接返回数组
    if (booksResponse && booksResponse.data) {
      const booksData = Array.isArray(booksResponse.data) ? booksResponse.data : []
      totalBorrowedBooks.value = booksData.length
      console.log(`获取到 ${totalBorrowedBooks.value} 条借阅记录`)
    } else {
      console.warn('借阅记录响应结构不正确，使用概览中的借阅数量')
      totalBorrowedBooks.value = 0
    }

    console.log(' 学业概览数据加载成功')

  } catch (error) {
    console.error('获取学业概览失败:', error)
    resetData()
  } finally {
    loading.value = false
  }
}

// 重置学业数据
const resetAcademicData = () => {
  currentAcademicYear.value = '--'
  currentSemester.value = '--'
  currentSemesterGPA.value = '0.00'
  overallGPA.value = '0.00'
  totalCredits.value = '0'
  labProjectsCount.value = 0
  semesterGPAs.value = []
}

// 重置所有数据
const resetData = () => {
  resetAcademicData()
  totalBorrowedBooks.value = 0
}

// 绩点颜色分类
const getGPAClass = (gpa) => {
  const gpaValue = typeof gpa === 'number' ? gpa : parseFloat(gpa) || 0

  if (gpaValue >= 3.5) return 'bg-emerald-100 text-emerald-700'
  if (gpaValue >= 3.0) return 'bg-blue-100 text-blue-700'
  if (gpaValue >= 2.5) return 'bg-purple-100 text-purple-700'
  if (gpaValue >= 2.0) return 'bg-amber-100 text-amber-700'
  return 'bg-red-100 text-red-700'
}

onMounted(() => {
  console.log('学业概览组件挂载')
  fetchAcademicOverview()
})
</script>