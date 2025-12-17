<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <h3 class="text-xl font-bold text-slate-800">学生信息管理</h3>
      <button
          @click="$emit('add-student')"
          class="px-5 py-2 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300"
      >
        <i class="fas fa-plus mr-2"></i>
        添加学生
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

        <!-- 姓名筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-user mr-1"></i>
            姓名
          </label>
          <input
              v-model="filters.name"
              type="text"
              placeholder="输入姓名"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @input="handleFilter"
          />
        </div>

        <!-- 学院筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-building mr-1"></i>
            学院
          </label>
          <select
              v-model="filters.college"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @change="handleFilter"
          >
            <option value="">全部学院</option>
            <option v-for="college in collegeOptions" :key="college" :value="college">{{ college }}</option>
          </select>
        </div>

        <!-- 专业筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-graduation-cap mr-1"></i>
            专业
          </label>
          <select
              v-model="filters.major"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @change="handleFilter"
          >
            <option value="">全部专业</option>
            <option v-for="major in majorOptions" :key="major" :value="major">{{ major }}</option>
          </select>
        </div>

        <!-- 状态筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-tasks mr-1"></i>
            状态
          </label>
          <select
              v-model="filters.accountStatus"
              class="w-full px-4 py-2 border border-slate-300 rounded-lg focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 transition-all"
              @change="handleFilter"
          >
            <option value="">全部状态</option>
            <option value="ACTIVE">正常</option>
            <option value="INACTIVE">禁用</option>
            <option value="PENDING">待审核</option>
          </select>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="flex justify-between items-center mt-4">
        <div class="text-sm text-slate-600">
          共找到 {{ filteredStudents.length }} 条记录
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

    <!-- 学生表格 -->
    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full min-w-[1200px]">
          <thead>
          <tr class="bg-gradient-to-r from-slate-50 to-slate-100">
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">学号</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">基本信息</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">联系方式</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">学院/专业</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">账户信息</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">操作</th>
          </tr>
          </thead>
          <tbody>
          <tr
              v-for="(student, index) in paginatedStudents"
              :key="student.id"
              :class="[
                'border-b border-slate-100 transition-colors duration-200',
                index % 2 === 0 ? 'bg-white' : 'bg-slate-50',
                'hover:bg-gradient-to-r hover:from-emerald-50 hover:to-teal-50'
              ]"
          >
            <!-- 学号 -->
            <td class="px-6 py-4 font-medium text-center">
              {{ student.studentId || '--' }}
            </td>

            <!-- 基本信息 -->
            <td class="px-6 py-4 text-center">
              <div class="flex flex-col space-y-1">
                <span class="text-slate-800 font-medium">
                  {{ student.name || '未命名' }}
                </span>
                <div class="text-sm text-slate-500">
                  <div>性别：{{ formatGender(student.gender) }}</div>
                  <div>班级：{{ student.className || '--' }}</div>
                </div>
              </div>
            </td>

            <!-- 联系方式 -->
            <td class="px-6 py-4 text-center">
              <div class="flex flex-col space-y-2">
                <div v-if="student.email">
                  <span class="text-sm text-slate-600">邮箱：</span>
                  <span class="font-medium">{{ student.email }}</span>
                </div>
                <div v-if="student.phone">
                  <span class="text-sm text-slate-600">电话：</span>
                  <span class="font-medium">{{ student.phone }}</span>
                </div>
                <div v-if="!student.email && !student.phone">
                  <span class="text-sm text-slate-400">未填写</span>
                </div>
              </div>
            </td>

            <!-- 学院/专业 -->
            <td class="px-6 py-4 text-center">
              <div class="flex flex-col space-y-2">
                <div>
                  <span class="text-sm text-slate-600">学院：</span>
                  <span class="font-medium">{{ student.college || '--' }}</span>
                </div>
                <div>
                  <span class="text-sm text-slate-600">专业：</span>
                  <span class="font-medium">{{ student.major || '--' }}</span>
                </div>
              </div>
            </td>

            <!-- 账户信息 -->
            <td class="px-6 py-4 text-center">
              <div class="flex flex-col space-y-2">
                <div>
                  <span :class="[
                    'px-3 py-1 rounded-full text-sm font-medium',
                    getStatusClass(student.accountStatus)
                  ]">
                    {{ formatStatus(student.accountStatus) }}
                  </span>
                </div>

              </div>
            </td>

            <!-- 操作 -->
            <td class="px-6 py-4">
              <div class="flex space-x-2 justify-center">
                <!-- 编辑按钮 -->
                <button
                    @click="$emit('edit-student', student)"
                    class="px-4 py-2 bg-gradient-to-r from-amber-500 to-orange-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300 text-sm"
                >
                  <i class="fas fa-edit mr-1"></i>
                  编辑
                </button>
                <button
                    @click="toggleStudentStatus(student)"
                    :class="[
                       'px-4 py-2 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300 text-sm',
                         student.accountStatus === 'ACTIVE' || student.accountStatus === 'active'
                        ? 'bg-gradient-to-r from-red-500 to-pink-500'
                         : 'bg-gradient-to-r from-emerald-500 to-teal-500'
                     ]"
                  >
                  <i :class="[
                    'fas mr-1',
                    student.accountStatus === 'ACTIVE' || student.accountStatus === 'active'
                    ? 'fa-ban'
                    : 'fa-check-circle'
                    ]"></i>
                  {{ (student.accountStatus === 'ACTIVE' || student.accountStatus === 'active') ? '禁用' : '激活' }}
                </button>
                <!-- 删除按钮 -->
                <button
                    @click="$emit('delete-student', student.id)"
                    class="px-4 py-2 bg-gradient-to-r from-red-500 to-pink-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300 text-sm"
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
      <div v-if="filteredStudents.length === 0" class="text-center py-12">
        <div class="inline-flex items-center justify-center w-24 h-24 bg-gradient-to-br from-emerald-100 to-teal-200 rounded-full mb-6">
          <i class="fas fa-users text-4xl text-emerald-600"></i>
        </div>
        <h3 class="text-xl font-semibold text-slate-700 mb-2">暂无学生记录</h3>
        <p class="text-slate-500 mb-6">请添加学生记录或调整筛选条件</p>
        <div class="space-y-4">
          <button
              @click="$emit('add-student')"
              class="px-6 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300"
          >
            <i class="fas fa-plus mr-2"></i>
            添加第一条学生记录
          </button>
          <p v-if="isFiltered && students.length > 0" class="text-slate-400">
            或 <button @click="resetFilters" class="text-indigo-500 hover:text-indigo-600 underline">重置筛选条件</button> 查看所有记录
          </p>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="filteredStudents.length > 0" class="px-6 py-4 border-t border-slate-100">
        <div class="flex items-center justify-between">
          <div class="text-sm text-slate-600">
            显示第 {{ (currentPage - 1) * pageSize + 1 }} - {{ Math.min(currentPage * pageSize, filteredStudents.length) }} 条，共 {{ filteredStudents.length }} 条
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
                :disabled="currentPage * pageSize >= filteredStudents.length"
                :class="[
                  'px-4 py-2 rounded-lg transition-colors',
                  currentPage * pageSize >= filteredStudents.length
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
import { ref, reactive, computed, watch } from 'vue'

const props = defineProps({
  students: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits([
  'add-student',
  'edit-student',
  'toggle-status',
  'delete-student'
])

// 筛选条件
const filters = reactive({
  studentId: '',
  name: '',
  college: '',
  major: '',
  accountStatus: ''
})

// 分页
const currentPage = ref(1)
const pageSize = 5

// 学院和专业选项
const collegeOptions = computed(() => {
  return [...new Set(props.students
      .filter(s => s.college)
      .map(s => s.college)
      .sort())]
})

const majorOptions = computed(() => {
  return [...new Set(props.students
      .filter(s => s.major)
      .map(s => s.major)
      .sort())]
})

// 格式化函数
const formatGender = (gender) => {
  const genderMap = {
    'M': '男',
    'F': '女',
    'MALE': '男',
    'FEMALE': '女',
    '男': '男',
    '女': '女'
  }
  return genderMap[gender] || '未知'
}

const formatStatus = (status) => {
  const statusMap = {
    'ACTIVE': '正常',
    'INACTIVE': '禁用',
    'PENDING': '待审核',
    'active': '正常',
    'inactive': '禁用',
    'pending': '待审核'
  }
  return statusMap[status] || '未知'
}

const formatDate = (dateString) => {
  if (!dateString) return '--'
  try {
    return new Date(dateString).toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
  } catch {
    return dateString
  }
}

const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return '--'
  try {
    return new Date(dateTimeString).toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch {
    return dateTimeString
  }
}

// 获取状态样式
const getStatusClass = (status) => {
  const classMap = {
    'ACTIVE': 'bg-gradient-to-r from-emerald-400 to-teal-500 text-white',
    'active': 'bg-gradient-to-r from-emerald-400 to-teal-500 text-white',
    'INACTIVE': 'bg-gradient-to-r from-red-400 to-pink-500 text-white',
    'inactive': 'bg-gradient-to-r from-red-400 to-pink-500 text-white',
    'PENDING': 'bg-gradient-to-r from-amber-400 to-orange-500 text-white',
    'pending': 'bg-gradient-to-r from-amber-400 to-orange-500 text-white'
  }
  return classMap[status] || 'bg-gradient-to-r from-slate-400 to-gray-500 text-white'
}

// 判断是否正在筛选
const isFiltered = computed(() => {
  return Object.values(filters).some(value => value !== '')
})

// 筛选后的学生
const filteredStudents = computed(() => {
  let result = [...props.students]

  // 学号筛选
  if (filters.studentId) {
    result = result.filter(student => {
      const studentId = student.studentId || ''
      return studentId.toString().toLowerCase().includes(filters.studentId.toLowerCase())
    })
  }

  // 姓名筛选
  if (filters.name) {
    result = result.filter(student => {
      const name = student.name || ''
      return name.toLowerCase().includes(filters.name.toLowerCase())
    })
  }

  // 学院筛选
  if (filters.college) {
    result = result.filter(student => {
      const college = student.college || ''
      return college === filters.college
    })
  }

  // 专业筛选
  if (filters.major) {
    result = result.filter(student => {
      const major = student.major || ''
      return major === filters.major
    })
  }

  // 状态筛选
  if (filters.accountStatus) {
    result = result.filter(student => {
      const status = student.accountStatus || ''
      return status === filters.accountStatus
    })
  }

  // 按学号排序
  result.sort((a, b) => {
    const idA = a.studentId || ''
    const idB = b.studentId || ''
    return idA.localeCompare(idB)
  })

  return result
})

// 分页后的数据
const paginatedStudents = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return filteredStudents.value.slice(start, end)
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
  if (currentPage.value * pageSize < filteredStudents.value.length) {
    currentPage.value++
  }
}

// 切换学生状态
const toggleStudentStatus = (student) => {
  const newStatus = student.accountStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
  const action = newStatus === 'ACTIVE' ? '激活' : '禁用'

  emit('toggle-status', {
    studentId: student.id,
    newStatus,
    studentName: student.name
  })
}

// 监听原始数据变化
watch(() => props.students, () => {
  currentPage.value = 1
}, { immediate: true })
</script>