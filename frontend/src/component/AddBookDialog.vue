<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl w-full max-w-2xl flex flex-col shadow-2xl max-h-[90vh]">
      <!-- 对话框头部 -->
      <div class="px-8 py-6 bg-gradient-to-r from-indigo-500 to-purple-500 text-white flex-shrink-0">
        <div class="flex items-center justify-between">
          <h2 class="text-2xl font-bold">
            <i class="fas fa-plus-circle mr-3"></i>
            添加借阅记录
          </h2>
          <button
              @click="close"
              class="text-white hover:text-gray-200 transition-colors"
          >
            <i class="fas fa-times text-2xl"></i>
          </button>
        </div>
      </div>

      <!-- 表单内容 -->
      <div class="flex-1 overflow-y-auto p-8">
        <form @submit.prevent="save" class="space-y-6">
          <!-- 学号和图书ID -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 学号 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-id-card mr-2"></i>
                学号 *
              </label>
              <input
                  v-model="formData.student_id"
                  type="text"
                  placeholder="请输入学号"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.student_id ? 'border-red-500' : ''"
                  @input="validateField('student_id')"
              />
              <p v-if="errors.student_id" class="mt-2 text-sm text-red-600">{{ errors.student_id }}</p>
            </div>

            <!-- 图书ID -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-hashtag mr-2"></i>
                图书ID *
              </label>
              <div class="relative">
                <input
                    v-model="formData.book_id"
                    type="text"
                    placeholder="请输入图书ID"
                    class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                    :class="errors.book_id ? 'border-red-500' : ''"
                    @input="onBookIdChange"
                    @focus="handleBookIdFocus"
                    @blur="handleBookIdBlur"
                />
                <!-- 图书ID提示列表 -->
                <div v-if="showBookList && formData.book_id" class="absolute z-10 w-full mt-1 bg-white border border-slate-200 rounded-lg shadow-lg max-h-60 overflow-y-auto">
                  <div class="p-3 text-sm text-slate-500 bg-slate-50 border-b">
                    可用图书ID:
                    <span v-for="bookId in Object.keys(bookMap)" :key="bookId" class="inline-block px-2 py-1 mx-1 bg-white border rounded text-xs">
                      {{ bookId }}
                    </span>
                  </div>
                  <div v-for="[bookId, bookInfo] in filteredBookList" :key="bookId"
                       class="px-4 py-2 hover:bg-slate-50 cursor-pointer"
                       @mousedown="selectBook(bookId)">
                    <div class="font-medium">{{ bookId }} - {{ bookInfo.title }}</div>
                    <div class="text-xs text-slate-500">{{ bookInfo.author }}</div>
                  </div>
                </div>
              </div>
              <p v-if="errors.book_id" class="mt-2 text-sm text-red-600">{{ errors.book_id }}</p>
              <p v-else-if="formData.book_id && !bookMap[formData.book_id]" class="mt-2 text-sm text-orange-600">
                <i class="fas fa-exclamation-triangle mr-1"></i>
                未找到该图书ID，请输入有效的图书ID
              </p>
            </div>
          </div>

          <!-- 图书信息 -->
          <div class="bg-gradient-to-r from-blue-50 to-indigo-50 border-2 border-blue-100 rounded-xl p-6">
            <h3 class="font-semibold text-slate-700 mb-4 flex items-center">
              <i class="fas fa-book mr-2"></i>
              图书信息
              <span v-if="formData.book_title" class="ml-2 text-sm text-emerald-600">
                <i class="fas fa-check-circle mr-1"></i>
                已自动填充
              </span>
            </h3>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <!-- 书名 -->
              <div>
                <label class="block text-sm font-medium text-slate-700 mb-2">
                  <i class="fas fa-book mr-2"></i>
                  书名 *
                </label>
                <input
                    v-model="formData.book_title"
                    type="text"
                    placeholder="输入图书ID后自动填充"
                    class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                    readonly
                    disabled
                />
                <p v-if="!formData.book_title" class="mt-2 text-sm text-slate-500">
                  请先输入正确的图书ID
                </p>
              </div>

              <!-- 作者 -->
              <div>
                <label class="block text-sm font-medium text-slate-700 mb-2">
                  <i class="fas fa-user-pen mr-2"></i>
                  作者
                </label>
                <input
                    v-model="formData.book_author"
                    type="text"
                    placeholder="自动填充"
                    class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                    readonly
                    disabled
                />
              </div>
            </div>

            <!-- ISBN和分类 -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mt-4">
              <!-- ISBN -->
              <div>
                <label class="block text-sm font-medium text-slate-700 mb-2">
                  <i class="fas fa-barcode mr-2"></i>
                  ISBN
                </label>
                <input
                    v-model="formData.book_isbn"
                    type="text"
                    placeholder="自动填充"
                    class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                    readonly
                    disabled
                />
              </div>

              <!-- 分类 -->
              <div>
                <label class="block text-sm font-medium text-slate-700 mb-2">
                  <i class="fas fa-tags mr-2"></i>
                  分类
                </label>
                <input
                    v-model="formData.book_category"
                    type="text"
                    placeholder="自动填充"
                    class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                    readonly
                    disabled
                />
              </div>
            </div>
          </div>

          <!-- 借阅信息 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- 借阅日期 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-sign-in-alt mr-2"></i>
                借阅日期 *
              </label>
              <input
                  v-model="formData.borrow_date"
                  type="date"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.borrow_date ? 'border-red-500' : ''"
                  @change="validateField('borrow_date')"
              />
              <p v-if="errors.borrow_date" class="mt-2 text-sm text-red-600">{{ errors.borrow_date }}</p>
            </div>

            <!-- 应还日期 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-clock mr-2"></i>
                应还日期 *
              </label>
              <input
                  v-model="formData.due_date"
                  type="date"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                  :class="errors.due_date ? 'border-red-500' : ''"
                  @change="validateDueDate"
              />
              <p v-if="errors.due_date" class="mt-2 text-sm text-red-600">{{ errors.due_date }}</p>
            </div>
          </div>

          <!-- 归还信息 -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              <i class="fas fa-sign-out-alt mr-2"></i>
              归还日期（可选）
            </label>
            <input
                v-model="formData.return_date"
                type="date"
                class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all duration-300"
                :class="errors.return_date ? 'border-red-500' : ''"
                @change="validateReturnDate"
            />
            <p v-if="errors.return_date" class="mt-2 text-sm text-red-600">{{ errors.return_date }}</p>
            <p class="mt-2 text-sm text-slate-500">
              * 如果填写归还日期，系统会自动标记为"已归还"状态
            </p>
          </div>

          <!-- 提示信息 -->
          <div class="bg-blue-50 border-2 border-blue-100 rounded-xl p-6">
            <h4 class="font-semibold text-blue-800 mb-3">
              <i class="fas fa-lightbulb mr-2"></i>
              操作提示
            </h4>
            <ul class="space-y-2 text-sm text-blue-600">
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                带有 * 号的字段为必填项
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                应还日期必须晚于借阅日期
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                归还日期如果填写，必须晚于借阅日期
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                输入图书ID后，图书信息将自动填充
              </li>
            </ul>
          </div>
        </form>
      </div>

      <!-- 对话框底部 -->
      <div class="px-8 py-6 border-t border-slate-200 bg-white flex-shrink-0">
        <div class="flex justify-end space-x-4">
          <button
              type="button"
              @click="close"
              class="px-8 py-3 border-2 border-slate-300 text-slate-700 font-semibold rounded-xl hover:bg-slate-100 transition-all duration-300"
          >
            <i class="fas fa-times mr-2"></i>
            取消
          </button>
          <button
              @click="save"
              :disabled="isSaving || !isFormValid"
              :class="[
                'px-8 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl transition-all duration-300',
                isSaving || !isFormValid ? 'opacity-50 cursor-not-allowed' : 'hover:shadow-lg hover:scale-105'
              ]"
          >
            <i v-if="isSaving" class="fas fa-spinner fa-spin mr-2"></i>
            <i v-else class="fas fa-save mr-2"></i>
            {{ isSaving ? '添加中...' : '添加记录' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch } from 'vue'

const showError = (message) => {
  console.error('Error:', message)
  alert(`错误: ${message}`)
}

const props = defineProps({
  visible: Boolean
})

const emit = defineEmits(['close', 'save'])

// 状态
const isSaving = ref(false)
const showBookList = ref(false)
const blurTimeout = ref(null)

// 图书信息映射表
const bookMap = {
  'B001': {
    title: '深入理解计算机系统',
    author: 'Randal E. Bryant',
    isbn: '9787115546081',
    category: '计算机系统 / 组成'
  },
  'B002': {
    title: '算法导论',
    author: 'Thomas H. Cormen',
    isbn: '9787115427472',
    category: '算法与数据结构'
  },
  'B003': {
    title: '计算机网络',
    author: 'Andrew S. Tanenbaum',
    isbn: '9787121372983',
    category: '计算机软件基础'
  },
  'B004': {
    title: '操作系统概念',
    author: 'Abraham Silberschatz',
    isbn: '9787111636664',
    category: '计算机软件基础'
  },
  'B005': {
    title: '数据库系统概念',
    author: 'Abraham Silberschatz',
    isbn: '9787111612729',
    category: '数据库'
  },
  'B006': {
    title: '编译原理',
    author: 'Alfred V. Aho',
    isbn: '9787111558058',
    category: '计算机软件基础'
  },
  'B007': {
    title: '计算机组成与设计',
    author: 'David A. Patterson',
    isbn: '9787111547434',
    category: '计算机系统 / 组成'
  }
}

// 表单数据
const formData = reactive({
  student_id: '',
  book_id: '',
  book_title: '',
  book_author: '',
  book_isbn: '',
  book_category: '',
  borrow_date: '',
  due_date: '',
  return_date: ''
})

// 表单验证
const errors = reactive({})

// 过滤后的图书列表（用于搜索提示）
const filteredBookList = computed(() => {
  const searchTerm = formData.book_id.toLowerCase()
  return Object.entries(bookMap).filter(([bookId, bookInfo]) => {
    return bookId.toLowerCase().includes(searchTerm) ||
        bookInfo.title.toLowerCase().includes(searchTerm) ||
        bookInfo.author.toLowerCase().includes(searchTerm)
  })
})

// 处理图书ID输入框获取焦点
const handleBookIdFocus = () => {
  showBookList.value = true
  // 清除之前的定时器
  if (blurTimeout.value) {
    clearTimeout(blurTimeout.value)
    blurTimeout.value = null
  }
}

// 处理图书ID输入框失去焦点
const handleBookIdBlur = () => {
  // 设置定时器，延迟隐藏列表，以便点击列表项
  blurTimeout.value = setTimeout(() => {
    showBookList.value = false
  }, 200)
}

// 选择图书
const selectBook = (bookId) => {
  formData.book_id = bookId
  onBookIdChange()
  showBookList.value = false
  // 清除定时器
  if (blurTimeout.value) {
    clearTimeout(blurTimeout.value)
    blurTimeout.value = null
  }
}

// 图书ID变化时自动填充图书信息
const onBookIdChange = () => {
  const bookId = formData.book_id
  const book = bookMap[bookId]

  if (book) {
    // 自动填充图书信息
    formData.book_title = book.title || ''
    formData.book_author = book.author || ''
    formData.book_isbn = book.isbn || ''
    formData.book_category = book.category || ''

    // 验证图书ID
    delete errors.book_id
  } else if (bookId) {
    errors.book_id = '未找到该图书ID对应的图书信息'
    // 清空图书信息
    formData.book_title = ''
    formData.book_author = ''
    formData.book_isbn = ''
    formData.book_category = ''
  } else {
    delete errors.book_id
  }
}

// 表单验证规则
const validationRules = {
  student_id: (value) => {
    if (!value) return '学号不能为空'
    if (!/^\d{8,12}$/.test(value)) return '请输入有效的学号'
    return null
  },
  book_id: (value) => {
    if (!value) return '图书ID不能为空'
    if (!bookMap[value]) return '未找到该图书ID对应的图书信息'
    return null
  },
  book_title: (value) => {
    if (!value) return '书名不能为空'
    return null
  },
  borrow_date: (value) => {
    if (!value) return '借阅日期不能为空'

    const borrowDate = new Date(value)
    const now = new Date()

    if (borrowDate > now) {
      return '借阅日期不能晚于当前日期'
    }

    return null
  },
  due_date: (value) => {
    if (!value) return '应还日期不能为空'

    if (!formData.borrow_date) {
      return '请先填写借阅日期'
    }

    const borrowDate = new Date(formData.borrow_date)
    const dueDate = new Date(value)

    if (dueDate <= borrowDate) {
      return '应还日期必须晚于借阅日期'
    }

    return null
  },
  return_date: (value) => {
    if (!value) return null

    if (!formData.borrow_date) {
      return '请先填写借阅日期'
    }

    const borrowDate = new Date(formData.borrow_date)
    const returnDate = new Date(value)

    if (returnDate < borrowDate) {
      return '归还日期不能早于借阅日期'
    }

    const now = new Date()
    if (returnDate > now) {
      return '归还日期不能晚于当前日期'
    }

    return null
  }
}

// 验证应还日期
const validateDueDate = () => {
  const rule = validationRules.due_date
  const error = rule(formData.due_date)

  if (error) {
    errors.due_date = error
  } else {
    delete errors.due_date
  }
}

// 验证归还日期
const validateReturnDate = () => {
  const rule = validationRules.return_date
  const error = rule(formData.return_date)

  if (error) {
    errors.return_date = error
  } else {
    delete errors.return_date
  }
}

// 验证字段
const validateField = (field) => {
  const rule = validationRules[field]
  if (rule) {
    const error = rule(formData[field])
    if (error) {
      errors[field] = error
    } else {
      delete errors[field]
    }
  }
}

// 验证整个表单
const validateForm = () => {
  Object.keys(validationRules).forEach(field => {
    if (field === 'return_date') {
      validateReturnDate()
    } else if (field === 'due_date') {
      validateDueDate()
    } else {
      validateField(field)
    }
  })
  return Object.keys(errors).length === 0
}

// 表单是否有效
const isFormValid = computed(() => {
  return Object.keys(validationRules).every(field => {
    if (field === 'return_date') return true // 归还日期不是必填

    const value = formData[field]
    if (!value && value !== 0) return false

    return !validationRules[field](value)
  })
})

// 关闭对话框
const close = () => {
  emit('close')
}

// 保存数据
const save = async () => {
  if (!validateForm()) {
    showError('请填写完整且正确的信息')
    return
  }

  isSaving.value = true
  try {
    const bookData = {
      student_id: formData.student_id,
      book_id: formData.book_id,
      book_title: formData.book_title,
      book_author: formData.book_author || '',
      book_isbn: formData.book_isbn || '',
      book_category: formData.book_category || '',
      borrow_date: formData.borrow_date,
      due_date: formData.due_date,
      return_date: formData.return_date || null
    }

    console.log('添加图书借阅数据:', bookData)
    emit('save', bookData)
  } catch (err) {
    console.error('添加失败:', err)
    isSaving.value = false
  }
}

// 监听对话框关闭时重置表单
watch(() => props.visible, (visible) => {
  if (!visible) {
    // 重置表单数据
    Object.keys(formData).forEach(key => {
      formData[key] = ''
    })

    // 清除错误信息
    Object.keys(errors).forEach(key => {
      delete errors[key]
    })

    // 隐藏图书列表
    showBookList.value = false

    // 清除定时器
    if (blurTimeout.value) {
      clearTimeout(blurTimeout.value)
      blurTimeout.value = null
    }
  } else {
    // 打开对话框时设置默认借阅日期为当天
    const today = new Date()
    const year = today.getFullYear()
    const month = String(today.getMonth() + 1).padStart(2, '0')
    const day = String(today.getDate()).padStart(2, '0')
    formData.borrow_date = `${year}-${month}-${day}`

    // 设置默认应还日期为30天后
    const dueDate = new Date(today)
    dueDate.setDate(today.getDate() + 30)
    const dueYear = dueDate.getFullYear()
    const dueMonth = String(dueDate.getMonth() + 1).padStart(2, '0')
    const dueDay = String(dueDate.getDate()).padStart(2, '0')
    formData.due_date = `${dueYear}-${dueMonth}-${dueDay}`
  }
})

// 组件卸载时清除定时器
watch(() => props.visible, (visible) => {
  if (!visible) {
    if (blurTimeout.value) {
      clearTimeout(blurTimeout.value)
      blurTimeout.value = null
    }
  }
})
</script>