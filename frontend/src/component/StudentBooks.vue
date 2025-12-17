<template>
  <div class="bg-white/90 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-emerald-100">
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-bold text-emerald-800">
        <i class="fas fa-book mr-3 text-emerald-500"></i>
        借阅记录
      </h2>
      <div class="flex items-center space-x-3">
        <!-- 状态筛选 -->
        <select
            v-model="statusFilter"
            class="px-3 py-2 bg-white border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500"
        >
          <option value="">全部状态</option>
          <option value="borrowed">借阅中</option>
          <option value="returned">已归还</option>
          <option value="overdue">已逾期</option>
        </select>

        <button
            @click="$emit('show-all')"
            class="px-4 py-2 bg-gradient-to-r from-emerald-500 to-teal-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300 text-sm"
        >
          查看全部
        </button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="text-center py-8">
      <i class="fas fa-spinner fa-spin text-3xl text-emerald-500 mb-3"></i>
      <p class="text-slate-500">正在加载借阅数据...</p>
    </div>

    <!-- 数据内容 -->
    <div v-else class="space-y-4">
      <div
          v-for="book in filteredBooks"
          :key="book.recordId || book.bookId"
          class="bg-gradient-to-r from-emerald-50 to-teal-50 rounded-xl p-4 hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1"
      >
        <div class="flex items-start justify-between mb-2">
          <div class="flex-1">
            <h4 class="font-semibold text-slate-800 mb-1">{{ book.bookTitle || book.title }}</h4>
            <p class="text-sm text-slate-500 mb-1">{{ book.bookAuthor || book.author }}</p>
            <p class="text-xs text-slate-400">
              ISBN: {{ book.isbn || book.bookIsbn || '--' }} |
              分类：{{ book.bookCategory || book.category || '未分类' }}
            </p>
          </div>
          <span :class="[
            'px-3 py-1 rounded-full text-sm font-medium whitespace-nowrap',
            getBookStatusClass(book)
          ]">
            {{ getBookStatusText(book) }}
          </span>
        </div>

        <div class="grid grid-cols-2 gap-2 text-sm text-slate-500 mt-3 pt-3 border-t border-slate-100">
          <div>
            <span class="font-medium">借阅日期：</span>
            <span>{{ formatDate(book.borrowDate || book.borrowTime) }}</span>
          </div>
          <div>
            <span class="font-medium">应还日期：</span>
            <span>{{ formatDate(book.dueDate || book.dueTime) }}</span>
          </div>
          <div v-if="book.returnDate || book.returnTime">
            <span class="font-medium">归还日期：</span>
            <span>{{ formatDate(book.returnDate || book.returnTime) }}</span>
          </div>
          <div v-if="getBookStatusText(book) === '已逾期'" class="text-red-500">
            <span class="font-medium">逾期天数：</span>
            <span>{{ calculateOverdueDays(book) }}天</span>
          </div>
        </div>
      </div>

      <!-- 无数据提示 -->
      <div v-if="filteredBooks.length === 0" class="text-center py-8 text-slate-400">
        <i class="fas fa-book text-3xl mb-3"></i>
        <p v-if="statusFilter">该状态下暂无借阅记录</p>
        <p v-else>暂无借阅记录</p>
      </div>
    </div>

    <!-- 数据统计 -->
    <div v-if="!loading && books.length > 0" class="mt-6 pt-6 border-t border-slate-100">
      <div class="flex justify-between text-sm text-slate-500">
        <span>总计：{{ books.length }} 本</span>
        <span>借阅中：{{ borrowedCount }} 本</span>
        <span>已归还：{{ returnedCount }} 本</span>
        <span>已逾期：{{ overdueCount }} 本</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getAllBooks } from '@/services/api/academic'

defineEmits(['show-all'])

// 数据状态
const books = ref([])
const loading = ref(false)
const statusFilter = ref('')

// 获取所有借阅记录
const fetchBooks = async () => {
  try {
    loading.value = true
    console.log('开始获取所有借阅记录...')

    const response = await getAllBooks()
    console.log(' 借阅记录响应:', response)

    if (response && response.data) {
      books.value = Array.isArray(response.data) ? response.data : []
      console.log(`获取到 ${books.value.length} 条借阅记录`)

      // 分析数据并打印每本书的状态判断
      books.value.forEach((book, index) => {
        const status = calculateBookStatus(book)
      })
    } else {
      console.warn('借阅记录接口返回异常:', response)
      books.value = []
    }
  } catch (error) {
    console.error('获取借阅记录失败:', error)
    books.value = []
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateValue) => {
  if (!dateValue) return '--'

  try {
    let date
    if (typeof dateValue === 'number') {
      date = new Date(dateValue)
    } else if (typeof dateValue === 'string') {
      if (/^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
        date = new Date(dateValue + 'T00:00:00')
      } else if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(dateValue)) {
        date = new Date(dateValue.replace(' ', 'T'))
      } else if (/^\d+$/.test(dateValue)) {
        date = new Date(parseInt(dateValue))
      } else {
        date = new Date(dateValue)
      }
    } else {
      return String(dateValue)
    }

    if (isNaN(date.getTime())) {
      return dateValue
    }

    return date.toLocaleDateString('zh-CN')
  } catch (error) {
    return String(dateValue)
  }
}

// 解析日期为时间戳
const parseDateToTimestamp = (dateValue) => {
  if (!dateValue) return null

  try {
    let date
    if (typeof dateValue === 'number') {
      date = new Date(dateValue)
    } else if (typeof dateValue === 'string') {
      if (/^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
        date = new Date(dateValue + 'T00:00:00')
      } else if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(dateValue)) {
        date = new Date(dateValue.replace(' ', 'T'))
      } else if (/^\d+$/.test(dateValue)) {
        date = new Date(parseInt(dateValue))
      } else {
        date = new Date(dateValue)
      }
    } else {
      return null
    }

    if (isNaN(date.getTime())) {
      return null
    }

    return date.getTime()
  } catch (error) {
    return null
  }
}

// 根据日期计算图书状态
const calculateBookStatus = (book) => {
  const returnDate = book.returnDate || book.returnTime
  const dueDate = book.dueDate || book.dueTime

  // 如果没有应还日期，无法判断状态
  if (!dueDate) {
    return '未知状态'
  }

  const dueTimestamp = parseDateToTimestamp(dueDate)
  if (!dueTimestamp) {
    return '未知状态'
  }

  // 如果有归还日期
  if (returnDate) {
    const returnTimestamp = parseDateToTimestamp(returnDate)
    if (returnTimestamp) {
      // 归还日期早于等于应还日期：正常归还
      // 归还日期晚于应还日期：逾期归还
      if (returnTimestamp <= dueTimestamp) {
        return '已归还'
      } else {
        return '已逾期'
      }
    }
  }

  // 没有归还日期，判断是否逾期
  const currentTimestamp = new Date().getTime()

  if (currentTimestamp > dueTimestamp) {
    return '已逾期'
  } else {
    return '借阅中'
  }
}

// 判断是否逾期
const isOverdue = (book) => {
  return calculateBookStatus(book) === '已逾期'
}

// 计算逾期天数
const calculateOverdueDays = (book) => {
  const status = calculateBookStatus(book)
  if (status !== '已逾期') {
    return 0
  }

  const dueDate = book.dueDate || book.dueTime
  const dueTimestamp = parseDateToTimestamp(dueDate)

  if (!dueTimestamp) {
    return 0
  }

  const returnDate = book.returnDate || book.returnTime
  let endTimestamp

  if (returnDate) {
    // 已归还：计算应还日期到归还日期的天数
    const returnTimestamp = parseDateToTimestamp(returnDate)
    if (returnTimestamp) {
      endTimestamp = returnTimestamp
    } else {
      // 归还日期解析失败，使用当前日期
      endTimestamp = new Date().getTime()
    }
  } else {
    // 未归还：计算应还日期到当前日期的天数
    endTimestamp = new Date().getTime()
  }

  // 计算天数差
  const diffTime = endTimestamp - dueTimestamp
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

  // 确保逾期天数为正数
  return Math.max(1, diffDays) // 最少1天
}

// 获取图书状态类
const getBookStatusClass = (book) => {
  const status = calculateBookStatus(book)

  if (status === '已归还') {
    return 'bg-emerald-100 text-emerald-700'
  } else if (status === '已逾期') {
    return 'bg-red-100 text-red-700'
  } else if (status === '借阅中') {
    return 'bg-blue-100 text-blue-700'
  } else {
    return 'bg-slate-100 text-slate-700'
  }
}

// 获取图书状态文本
const getBookStatusText = (book) => {
  return calculateBookStatus(book)
}

// 统计数量
const borrowedCount = computed(() => {
  return books.value.filter(book => {
    return calculateBookStatus(book) === '借阅中'
  }).length
})

const returnedCount = computed(() => {
  return books.value.filter(book => {
    const status = calculateBookStatus(book)
    return status === '已归还'
  }).length
})

const overdueCount = computed(() => {
  return books.value.filter(book => {
    const status = calculateBookStatus(book)
    return status === '已逾期'
  }).length
})

// 根据筛选条件过滤图书
const filteredBooks = computed(() => {
  if (!books.value || books.value.length === 0) return []

  if (!statusFilter.value) {
    return books.value.sort((a, b) => {
      const dateA = parseDateToTimestamp(a.borrowDate || a.borrowTime) || 0
      const dateB = parseDateToTimestamp(b.borrowDate || b.borrowTime) || 0
      return dateB - dateA
    })
  }

  return books.value.filter(book => {
    const status = calculateBookStatus(book)

    if (statusFilter.value === 'borrowed') {
      return status === '借阅中'
    } else if (statusFilter.value === 'returned') {
      return status === '已归还'
    } else if (statusFilter.value === 'overdue') {
      return status === '已逾期'
    }
    return true
  }).sort((a, b) => {
    const dateA = parseDateToTimestamp(a.borrowDate || a.borrowTime) || 0
    const dateB = parseDateToTimestamp(b.borrowDate || b.borrowTime) || 0
    return dateB - dateA
  })
})

onMounted(() => {
  console.log('借阅记录组件挂载')
  fetchBooks()
})
</script>