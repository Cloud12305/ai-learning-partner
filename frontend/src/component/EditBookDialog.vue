<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-white rounded-2xl w-full max-w-2xl flex flex-col shadow-2xl max-h-[90vh]">
      <!-- 对话框头部 -->
      <div class="px-8 py-6 bg-gradient-to-r from-blue-500 to-indigo-500 text-white flex-shrink-0">
        <div class="flex items-center justify-between">
          <h2 class="text-2xl font-bold">
            <i class="fas fa-edit mr-3"></i>
            编辑借阅记录
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
          <!-- 学号 -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              <i class="fas fa-id-card mr-2"></i>
              学号
            </label>
            <input
                :value="studentId || '--'"
                type="text"
                class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                readonly
                disabled
            />
          </div>

          <!-- 图书信息 -->
          <div class="bg-slate-50 border-2 border-slate-100 rounded-xl p-4">
            <h3 class="font-semibold text-slate-700 mb-3">
              <i class="fas fa-book mr-2"></i>
              图书信息
            </h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm text-slate-600 mb-1">书名</label>
                <input
                    :value="bookTitle || '未命名'"
                    type="text"
                    class="w-full px-3 py-2 border border-slate-200 rounded-lg bg-white"
                    readonly
                    disabled
                />
              </div>
              <div>
                <label class="block text-sm text-slate-600 mb-1">作者</label>
                <input
                    :value="bookAuthor || '未知'"
                    type="text"
                    class="w-full px-3 py-2 border border-slate-200 rounded-lg bg-white"
                    readonly
                    disabled
                />
              </div>
              <div>
                <label class="block text-sm text-slate-600 mb-1">ISBN</label>
                <input
                    :value="bookIsbn || '--'"
                    type="text"
                    class="w-full px-3 py-2 border border-slate-200 rounded-lg bg-white"
                    readonly
                    disabled
                />
              </div>
              <div>
                <label class="block text-sm text-slate-600 mb-1">分类</label>
                <input
                    :value="bookCategory || '未分类'"
                    type="text"
                    class="w-full px-3 py-2 border border-slate-200 rounded-lg bg-white"
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
                借阅日期
              </label>
              <input
                  :value="formatDate(borrowDate) || '--'"
                  type="text"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl bg-gray-50"
                  readonly
                  disabled
              />
            </div>

            <!-- 应还日期 -->
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-2">
                <i class="fas fa-clock mr-2"></i>
                应还日期 *
              </label>
              <input
                  v-model="dueDateInput"
                  type="date"
                  class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                  :class="errors.dueDate ? 'border-red-500' : ''"
                  @change="validateDueDate"
              />
              <p v-if="errors.dueDate" class="mt-2 text-sm text-red-600">{{ errors.dueDate }}</p>
            </div>
          </div>

          <!-- 归还信息 -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              <i class="fas fa-sign-out-alt mr-2"></i>
              归还日期（可选）
            </label>
            <input
                v-model="returnDateInput"
                type="date"
                class="w-full px-4 py-3 border-2 border-slate-200 rounded-xl focus:border-blue-500 focus:ring-2 focus:ring-blue-200 transition-all duration-300"
                :class="errors.returnDate ? 'border-red-500' : ''"
                @change="validateReturnDate"
            />
            <p v-if="errors.returnDate" class="mt-2 text-sm text-red-600">{{ errors.returnDate }}</p>
            <div class="mt-3">
              <button
                  type="button"
                  @click="clearReturnDate"
                  class="px-3 py-1.5 text-sm bg-red-50 text-red-600 border border-red-200 rounded-lg hover:bg-red-100 transition-colors"
              >
                <i class="fas fa-trash-alt mr-1"></i>
                清除归还日期
              </button>
            </div>
          </div>

          <!-- 提示信息 -->
          <div class="bg-blue-50 border-2 border-blue-100 rounded-xl p-6">
            <h4 class="font-semibold text-blue-800 mb-3">
              <i class="fas fa-lightbulb mr-2"></i>
              编辑提示
            </h4>
            <ul class="space-y-2 text-sm text-blue-600">
              <li class="flex items-start">
                <i class="fas fa-info-circle mt-1 mr-2 text-blue-500"></i>
                学号、图书信息和借阅日期不可更改
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                只能修改应还日期和归还日期
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                应还日期必须晚于借阅日期
              </li>
              <li class="flex items-start">
                <i class="fas fa-check-circle mt-1 mr-2 text-emerald-500"></i>
                归还日期如果填写，必须晚于借阅日期
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
                'px-8 py-3 bg-gradient-to-r from-blue-500 to-indigo-500 text-white font-semibold rounded-xl transition-all duration-300',
                isSaving || !isFormValid ? 'opacity-50 cursor-not-allowed' : 'hover:shadow-lg hover:scale-105'
              ]"
          >
            <i v-if="isSaving" class="fas fa-spinner fa-spin mr-2"></i>
            <i v-else class="fas fa-save mr-2"></i>
            {{ isSaving ? '保存中...' : '保存修改' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const showError = (message) => {
  console.error(' Error:', message)
  alert(`错误: ${message}`)
}

const props = defineProps({
  visible: Boolean,
  bookData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['close', 'save'])

// 状态
const isSaving = ref(false)

// 直接从 props 提取数据（使用驼峰格式）
const recordId = computed(() => props.bookData?.recordId || props.bookData?.record_id || '')
const studentId = computed(() => props.bookData?.studentId || props.bookData?.student_id || '')
const bookId = computed(() => props.bookData?.bookId || props.bookData?.book_id || '')
const bookTitle = computed(() => props.bookData?.bookTitle || props.bookData?.book_title || '')
const bookAuthor = computed(() => props.bookData?.bookAuthor || props.bookData?.book_author || '')
const bookIsbn = computed(() => props.bookData?.bookIsbn || props.bookData?.book_isbn || '')
const bookCategory = computed(() => props.bookData?.bookCategory || props.bookData?.book_category || '')
const borrowDate = computed(() => props.bookData?.borrowDate || props.bookData?.borrow_date || '')

// 可编辑的日期字段
const dueDateInput = ref('')
const returnDateInput = ref('')

// 表单验证
const errors = ref({})

// 清除归还日期
const clearReturnDate = () => {
  returnDateInput.value = ''
  delete errors.value.returnDate
}

// 格式化日期显示
const formatDate = (dateValue) => {
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
     return dateValue
    }

    // 格式化为YYYY-MM-DD
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const result = `${year}-${month}-${day}`
    return result
  } catch (error) {
    console.error('日期格式化错误:', error, '原始值:', dateValue)
    return String(dateValue)
  }
}

// 格式化日期为输入框格式
const formatDateForInput = (dateValue) => {

  if (!dateValue || dateValue === 'null' || dateValue === 'NULL') {
    return ''
  }

  try {
    // 如果已经是YYYY-MM-DD格式，直接返回
    if (/^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
      console.log('输入日期已经是YYYY-MM-DD格式:', dateValue)
      return dateValue
    }

    const date = new Date(dateValue)
    if (isNaN(date.getTime())) {
      console.log('输入日期解析失败')
      return ''
    }

    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const result = `${year}-${month}-${day}`
    console.log('输入日期格式化结果:', result)
    return result
  } catch (error) {
    console.error('输入日期格式化错误:', error, '原始值:', dateValue)
    return ''
  }
}

// 表单验证规则
const validationRules = {
  dueDate: (value) => {
    if (!value) return '应还日期不能为空'

    const borrowDateValue = borrowDate.value
    if (!borrowDateValue) {
      console.error('无法获取借阅日期:', props.bookData)
      return '无法获取借阅日期'
    }

    const borrowDateObj = new Date(borrowDateValue)
    const dueDateObj = new Date(value)

    if (dueDateObj <= borrowDateObj) {
      return '应还日期必须晚于借阅日期'
    }

    return null
  },
  returnDate: (value) => {
    if (!value) return null

    const borrowDateValue = borrowDate.value
    if (!borrowDateValue) {
      return '无法获取借阅日期'
    }

    const borrowDateObj = new Date(borrowDateValue)
    const returnDateObj = new Date(value)

    if (returnDateObj < borrowDateObj) {
      return '归还日期不能早于借阅日期'
    }

    const now = new Date()
    if (returnDateObj > now) {
      return '归还日期不能晚于当前日期'
    }

    return null
  }
}

// 验证应还日期
const validateDueDate = () => {
  const rule = validationRules.dueDate
  const error = rule(dueDateInput.value)

  if (error) {
    errors.value.dueDate = error
  } else {
    delete errors.value.dueDate
  }
}

// 验证归还日期
const validateReturnDate = () => {
  const rule = validationRules.returnDate
  const error = rule(returnDateInput.value)

  if (error) {
    errors.value.returnDate = error
  } else {
    delete errors.value.returnDate
  }
}

// 表单是否有效
const isFormValid = computed(() => {
  return Object.keys(validationRules).every(field => {
    if (field === 'returnDate') return true // 归还日期不是必填

    const value = field === 'dueDate' ? dueDateInput.value : ''
    if (!value) return false

    return !validationRules[field](value)
  })
})

// 关闭对话框
const close = () => {
  emit('close')
}

// 保存数据
const save = async () => {
  // 验证
  validateDueDate()
  validateReturnDate()

  if (Object.keys(errors.value).length > 0) {
    showError('请填写完整且正确的信息')
    return
  }

  isSaving.value = true
  try {
    // 获取所有必要字段
    const bookData = {
      record_id: recordId.value,
      student_id: studentId.value,  // 添加学号
      book_id: bookId.value,        // 添加图书ID
      book_title: bookTitle.value,  // 添加书名
      book_author: bookAuthor.value, // 添加作者
      book_isbn: bookIsbn.value,    // 添加ISBN
      book_category: bookCategory.value, // 添加分类
      borrow_date: borrowDate.value, // 添加借阅日期
      due_date: dueDateInput.value,  // 应还日期
      return_date: returnDateInput.value || null  // 归还日期
    }
    emit('save', bookData)
  } catch (err) {
    console.error('更新失败:', err)
    isSaving.value = false
  }
}

// 初始化表单数据
const initFormData = () => {
  if (props.bookData) {
    // 获取应还日期
    const dueDateValue = props.bookData?.dueDate || props.bookData?.due_date
    dueDateInput.value = formatDateForInput(dueDateValue)

    // 获取归还日期
    const returnDateValue = props.bookData?.returnDate || props.bookData?.return_date
    returnDateInput.value = formatDateForInput(returnDateValue)
  }
}

// 监听对话框打开时初始化数据
watch(() => props.visible, (visible) => {
  if (visible) {
    initFormData()
  } else {
    // 重置数据
    dueDateInput.value = ''
    returnDateInput.value = ''
    errors.value = {}
  }
})

// 监听bookData变化
watch(() => props.bookData, (newBookData) => {
  if (props.visible && newBookData) {
    initFormData()
  }
}, { deep: true })

// 立即执行一次初始化
if (props.visible && props.bookData) {
  initFormData()
}
</script>