<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <h3 class="text-xl font-bold text-slate-800">图书借阅管理</h3>
      <button
          @click="openAddDialog"
          class="px-5 py-2 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300"
      >
        <i class="fas fa-plus mr-2"></i>
        添加借阅记录
      </button>
    </div>

    <!-- 筛选区域 -->
    <div class="bg-gradient-to-r from-emerald-50 to-teal-50 rounded-2xl border border-slate-200 p-6 shadow-sm">
      <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
        <!-- 学号筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-id-card mr-1"></i>
            学号
          </label>
          <input
              v-model="filters.studentId"
              type="text"
              placeholder="输入学号"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @input="handleFilter"
          />
        </div>

        <!-- 书名筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-book mr-1"></i>
            书名
          </label>
          <input
              v-model="filters.bookTitle"
              type="text"
              placeholder="输入书名"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @input="handleFilter"
          />
        </div>

        <!-- ISBN筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-barcode mr-1"></i>
            ISBN
          </label>
          <input
              v-model="filters.bookIsbn"
              type="text"
              placeholder="输入ISBN"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @input="handleFilter"
          />
        </div>

        <!-- 状态筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-tasks mr-1"></i>
            状态
          </label>
          <select
              v-model="filters.status"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @change="handleFilter"
          >
            <option value="">全部状态</option>
            <option value="借阅中">借阅中</option>
            <option value="已归还">已归还</option>
            <option value="已逾期">已逾期</option>
          </select>
        </div>

        <!-- 日期范围筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-calendar-alt mr-1"></i>
            借阅时间
          </label>
          <select
              v-model="filters.dateRange"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @change="handleFilter"
          >
            <option value="">全部时间</option>
            <option value="today">今天</option>
            <option value="week">本周</option>
            <option value="month">本月</option>
          </select>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="flex justify-between items-center mt-4">
        <div class="text-sm text-slate-600">
          共找到 {{ filteredBooks.length }} 条记录
        </div>
        <div class="flex space-x-2">
          <button
              @click="resetFilters"
              class="px-4 py-2 border border-slate-300 text-slate-700 rounded-lg hover:bg-slate-50 transition-colors"
          >
            <i class="fas fa-redo mr-1"></i>
            重置筛选
          </button>
        </div>
      </div>
    </div>

    <!-- 图书借阅表格 -->
    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full min-w-[1200px]">
          <thead>
          <tr class="bg-gradient-to-r from-slate-50 to-slate-100">
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">学号</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">图书信息</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">借阅信息</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">归还信息</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">状态</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">操作</th>
          </tr>
          </thead>
          <tbody>
          <tr
              v-for="(book, index) in paginatedBooks"
              :key="book.record_id || book.recordId"
              :class="[
                'border-b border-slate-100 transition-colors duration-200',
                index % 2 === 0 ? 'bg-white' : 'bg-slate-50',
                'hover:bg-gradient-to-r hover:from-emerald-50 hover:to-teal-50'
              ]"
          >
            <!-- 学号 -->
            <td class="px-6 py-4 font-medium text-center">
              {{ getSafeValue(book.student_id, book.studentId, '--') }}
            </td>

            <!-- 图书信息 -->
            <td class="px-6 py-4 text-center">
              <div class="flex flex-col space-y-1">
                <span class="text-slate-800 font-medium">
                  {{ getSafeValue(book.book_title, book.bookTitle, '未命名') }}
                </span>
                <div class="text-sm text-slate-500">
                  <div>作者：{{ getSafeValue(book.book_author, book.bookAuthor, '未知') }}</div>
                  <div>ISBN：{{ getSafeValue(book.book_isbn, book.bookIsbn, '--') }}</div>
                  <div>分类：{{ getSafeValue(book.book_category, book.bookCategory, '未分类') }}</div>
                </div>
              </div>
            </td>

            <!-- 借阅信息 -->
            <td class="px-6 py-4 text-center">
              <div class="flex flex-col space-y-2">
                <div>
                  <span class="text-sm text-slate-600">借阅日期：</span>
                  <span class="font-medium">{{ formatDate(getBorrowDate(book)) }}</span>
                </div>
                <div>
                  <span class="text-sm text-slate-600">应还日期：</span>
                  <span :class="[
                    'font-medium',
                    isOverdue(book) ? 'text-red-600' : 'text-slate-800'
                  ]">
                    {{ formatDate(getDueDate(book)) }}
                  </span>
                </div>
              </div>
            </td>

            <!-- 归还信息 -->
            <td class="px-6 py-4 text-center">
              <div class="flex flex-col space-y-2">
                <div v-if="getReturnDate(book)">
                  <span class="text-sm text-slate-600">归还日期：</span>
                  <span class="font-medium text-emerald-600">
                    {{ formatDate(getReturnDate(book)) }}
                  </span>
                </div>
                <div v-else>
                  <span class="text-sm text-slate-400">尚未归还</span>
                </div>
              </div>
            </td>

            <!-- 状态 -->
            <td class="px-6 py-4 text-center">
              <span :class="[
                'px-3 py-1 rounded-full text-sm font-medium',
                getBookStatusClass(book)
              ]">
                {{ getBookStatusText(book) }}
              </span>
            </td>

            <!-- 操作 -->
            <td class="px-6 py-4 ">
              <div class="flex space-x-2 justify-center">
                <button
                    @click="openEditDialog(book)"
                    class="px-4 py-2 bg-gradient-to-r from-blue-500 to-indigo-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300 text-sm"
                >
                  <i class="fas fa-edit mr-1"></i>
                  编辑
                </button>
                <button
                    @click="confirmDeleteBook(book)"
                    class="px-4 py-2 bg-gradient-to-r from-red-500 to-pink-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300 text-sm "
                >
                  <i class="fas fa-trash-alt mr-1"></i>
                  删除
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredBooks.length === 0" class="text-center py-12">
        <div class="inline-flex items-center justify-center w-24 h-24 bg-gradient-to-br from-emerald-100 to-teal-200 rounded-full mb-6">
          <i class="fas fa-book text-4xl text-emerald-600"></i>
        </div>
        <h3 class="text-xl font-semibold text-slate-700 mb-2">暂无借阅记录</h3>
        <p class="text-slate-500 mb-6">请添加借阅记录或调整筛选条件</p>
        <div class="space-y-4">
          <button
              @click="openAddDialog"
              class="px-6 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300"
          >
            <i class="fas fa-plus mr-2"></i>
            添加第一条借阅记录
          </button>
          <p v-if="isFiltered && books.length > 0" class="text-slate-400">
            或 <button @click="resetFilters" class="text-indigo-500 hover:text-indigo-600 underline">重置筛选条件</button> 查看所有记录
          </p>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="filteredBooks.length > 0" class="px-6 py-4 border-t border-slate-100">
        <div class="flex items-center justify-between">
          <div class="text-sm text-slate-600">
            显示第 {{ (currentPage - 1) * pageSize + 1 }} - {{ Math.min(currentPage * pageSize, filteredBooks.length) }} 条，共 {{ filteredBooks.length }} 条
          </div>
          <div class="flex space-x-2">
            <button
                @click="prevPage"
                :disabled="currentPage === 1"
                :class="[
                  'px-4 py-2 rounded-lg transition-colors',
                  currentPage === 1
                    ? 'bg-slate-100 text-slate-400 cursor-not-allowed'
                    : 'bg-slate-50 text-slate-700 hover:bg-slate-100'
                ]"
            >
              上一页
            </button>
            <span class="px-4 py-2 bg-indigo-50 text-indigo-600 rounded-lg">
              第 {{ currentPage }} 页
            </span>
            <button
                @click="nextPage"
                :disabled="currentPage * pageSize >= filteredBooks.length"
                :class="[
                  'px-4 py-2 rounded-lg transition-colors',
                  currentPage * pageSize >= filteredBooks.length
                    ? 'bg-slate-100 text-slate-400 cursor-not-allowed'
                    : 'bg-slate-50 text-slate-700 hover:bg-slate-100'
                ]"
            >
              下一页
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, defineProps, defineEmits } from 'vue'

const props = defineProps({
  books: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits([
  'add-book',
  'edit-book',
  'delete-book'
])

// 筛选条件
const filters = reactive({
  studentId: '',
  bookTitle: '',
  bookIsbn: '',
  status: '',
  dateRange: ''
})

// 分页
const currentPage = ref(1)
const pageSize = 5

// 安全获取值函数
const getSafeValue = (value1, value2, defaultValue = '') => {
  // 首先检查下划线格式
  if (value1 !== null && value1 !== undefined && value1 !== '') {
    return value1
  }
  // 然后检查驼峰格式
  if (value2 !== null && value2 !== undefined && value2 !== '') {
    return value2
  }
  return defaultValue
}

// 专用的日期获取函数
const getBorrowDate = (book) => {
  // 优先使用下划线格式
  return book.borrow_date || book.borrowDate
}

const getDueDate = (book) => {
  // 优先使用下划线格式
  return book.due_date || book.dueDate
}

const getReturnDate = (book) => {
  // 优先使用下划线格式
  const date = book.return_date || book.returnDate
  // 检查是否为null或空字符串
  return (date === null || date === undefined || date === '' || date === 'NULL' || date === 'null') ? null : date
}

// 日期格式化函数
const formatDate = (dateValue) => {
  // 如果是null或undefined，返回--
  if (!dateValue || dateValue === 'NULL' || dateValue === 'null') {
    return '--'
  }

  // 如果已经是YYYY-MM-DD格式，直接返回
  if (/^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
    return dateValue
  }

  try {
    // 尝试解析日期
    const date = new Date(dateValue)
    if (isNaN(date.getTime())) {
      // 解析失败，返回原始值
      return dateValue
    }

    // 格式化为YYYY-MM-DD
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  } catch (error) {
    console.error('日期格式化错误:', error, '原始值:', dateValue)
    return String(dateValue)
  }
}

// 计算借阅状态
const calculateBookStatus = (book) => {
  const returnDate = getReturnDate(book)
  const dueDate = getDueDate(book)

  // 如果有归还日期
  if (returnDate) {
    try {
      const returnTimestamp = new Date(returnDate).getTime()
      const dueTimestamp = new Date(dueDate).getTime()
      if (returnTimestamp > dueTimestamp) {
        return '已逾期'
      }
      return '已归还'
    } catch (error) {
      console.error('日期比较错误:', error)
      return '已归还'
    }
  }

  // 没有归还日期，判断是否逾期
  if (!dueDate || dueDate === 'NULL' || dueDate === 'null') {
    return '未知状态'
  }

  try {
    const dueTimestamp = new Date(dueDate).getTime()
    const currentTimestamp = new Date().getTime()

    if (currentTimestamp > dueTimestamp) {
      return '已逾期'
    } else {
      return '借阅中'
    }
  } catch (error) {
    console.error('日期比较错误:', error)
    return '未知状态'
  }
}

// 判断是否逾期
const isOverdue = (book) => {
  return calculateBookStatus(book) === '已逾期'
}

// 获取图书状态类
const getBookStatusClass = (book) => {
  const status = calculateBookStatus(book)
  const classMap = {
    '已归还': 'bg-gradient-to-r from-emerald-400 to-teal-500 text-white',
    '已逾期': 'bg-gradient-to-r from-red-400 to-pink-500 text-white',
    '借阅中': 'bg-gradient-to-r from-blue-400 to-indigo-500 text-white',
    '未知状态': 'bg-gradient-to-r from-slate-400 to-gray-500 text-white'
  }
  return classMap[status] || 'bg-gradient-to-r from-slate-400 to-gray-500 text-white'
}

// 获取图书状态文本
const getBookStatusText = (book) => {
  return calculateBookStatus(book)
}

// 判断是否正在筛选
const isFiltered = computed(() => {
  return Object.values(filters).some(value => value !== '')
})

// 筛选后的图书
const filteredBooks = computed(() => {
  let result = [...props.books]

  // 学号筛选
  if (filters.studentId) {
    result = result.filter(book => {
      const studentId = getSafeValue(book.student_id, book.studentId, '')
      return studentId.toString().toLowerCase().includes(filters.studentId.toLowerCase())
    })
  }

  // 书名筛选
  if (filters.bookTitle) {
    result = result.filter(book => {
      const title = getSafeValue(book.book_title, book.bookTitle, '')
      return title.toLowerCase().includes(filters.bookTitle.toLowerCase())
    })
  }

  // ISBN筛选
  if (filters.bookIsbn) {
    result = result.filter(book => {
      const isbn = getSafeValue(book.book_isbn, book.bookIsbn, '')
      return isbn.toLowerCase().includes(filters.bookIsbn.toLowerCase())
    })
  }

  // 状态筛选
  if (filters.status) {
    result = result.filter(book => {
      const status = calculateBookStatus(book)
      return status === filters.status
    })
  }

  // 日期范围筛选
  if (filters.dateRange) {
    const now = new Date()
    const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate())
    const todayEnd = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1)

    const weekStart = new Date(now.getFullYear(), now.getMonth(), now.getDate() - now.getDay())
    const weekEnd = new Date(weekStart.getFullYear(), weekStart.getMonth(), weekStart.getDate() + 7)

    const monthStart = new Date(now.getFullYear(), now.getMonth(), 1)
    const monthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 1)

    result = result.filter(book => {
      const borrowDate = getBorrowDate(book)
      if (!borrowDate) return false

      try {
        const checkInDate = new Date(borrowDate)

        switch (filters.dateRange) {
          case 'today':
            return checkInDate >= todayStart && checkInDate < todayEnd
          case 'week':
            return checkInDate >= weekStart && checkInDate < weekEnd
          case 'month':
            return checkInDate >= monthStart && checkInDate < monthEnd
          default:
            return true
        }
      } catch {
        return false
      }
    })
  }

  // 按借阅日期倒序排序
  result.sort((a, b) => {
    const timeA = getBorrowDate(a)
    const timeB = getBorrowDate(b)
    try {
      const dateA = timeA ? new Date(timeA) : new Date(0)
      const dateB = timeB ? new Date(timeB) : new Date(0)
      return dateB - dateA
    } catch {
      return 0
    }
  })

  return result
})

// 分页后的数据
const paginatedBooks = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return filteredBooks.value.slice(start, end)
})

// 处理筛选
const handleFilter = () => {
  currentPage.value = 1
}

// 重置筛选
const resetFilters = () => {
  Object.keys(filters).forEach(key => {
    filters[key] = ''
  })
  currentPage.value = 1
}

// 分页控制
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

const nextPage = () => {
  if (currentPage.value * pageSize < filteredBooks.value.length) {
    currentPage.value++
  }
}

// 对话框操作
const openAddDialog = () => {
  emit('add-book')
}

const openEditDialog = (book) => {
  emit('edit-book', book)
}

const confirmDeleteBook = (book) => {
  const recordId = book.record_id || book.recordId
  if (recordId) {
    emit('delete-book', recordId)
  }
}

// 监听原始数据变化时调试
watch(() => props.books, () => {
  currentPage.value = 1
}, { immediate: true })
</script>