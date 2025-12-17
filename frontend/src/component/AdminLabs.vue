<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <h3 class="text-xl font-bold text-slate-800">实验室记录管理</h3>
      <button
          @click="openAddDialog"
          class="px-5 py-2 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300"
      >
        <i class="fas fa-plus mr-2"></i>
        添加记录
      </button>
    </div>

    <!-- 筛选区域 -->
    <div class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-2xl border border-slate-200 p-6 shadow-sm">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
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

        <!-- 实验室名称筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-flask mr-1"></i>
            实验室
          </label>
          <input
              v-model="filters.labName"
              type="text"
              placeholder="输入实验室名称"
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
            <option value="进行中">进行中</option>
            <option value="已完成">已完成</option>
            <option value="已超时">已超时</option>
          </select>
        </div>

        <!-- 日期范围筛选 -->
        <div>
          <label class="block text-sm font-medium text-slate-700 mb-2">
            <i class="fas fa-calendar-alt mr-1"></i>
            日期范围
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
          共找到 {{ filteredLabs.length }} 条记录
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

    <!-- 实验室表格 -->
    <div class="bg-white rounded-2xl border border-slate-200 overflow-hidden">
      <div class="overflow-x-auto">
        <table class="w-full min-w-[1000px]">
          <thead>
          <tr class="bg-gradient-to-r from-slate-50 to-slate-100">
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">学号</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">实验室</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">实验项目</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">签到时间</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">签退时间</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">时长</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">状态</th>
            <th class="px-6 py-4 text-center text-slate-600 font-semibold">操作</th>
          </tr>
          </thead>
          <tbody>

          <tr
              v-for="(lab, index) in paginatedLabs"
              :key="getLabKey(lab)"
              :class="[
            'border-b border-slate-100 transition-colors duration-200',
             index % 2 === 0 ? 'bg-white' : 'bg-slate-50',
             'hover:bg-gradient-to-r hover:from-blue-50 hover:to-indigo-50'
             ]"
          >
            <td class="px-6 py-4 font-medium text-center">
              {{ getSafeValue(lab.student_id, lab.studentId, '--') }}
            </td>
            <td class="px-6 py-4 text-center">
              <div class="flex flex-col ">
                <span class="text-slate-800 font-medium">
                  {{ getSafeValue(lab.lab_name, lab.labName, '未知实验室') }}
                </span>
              </div>
            </td>
            <td class="px-6 py-4 text-center">
              <span class="text-slate-700">
                {{ getSafeValue(lab.experiment_name, lab.experimentName, '常规实验') }}
              </span>
            </td>
            <td class="px-10 py-4 text-center">
              <span class="font-mono text-base ">
                {{ formatDateTime(lab.check_in_time || lab.checkInTime) }}
              </span>
            </td>
            <td class="px-10 py-4 text-center">
              <span class="font-mono text-base ">
                {{ formatDateTime(lab.check_out_time || lab.checkOutTime) }}
              </span>
            </td>
            <td class="px-6 py-4 text-center">
              <span :class="['px-3 py-1 rounded-full text-sm', getDurationClass(lab)]">
                {{ getDurationMinutes(lab) }}分钟
              </span>
            </td>
            <td class="px-6 py-4 text-center">
              <span :class="['px-3 py-1 rounded-full text-sm font-medium', getLabStatusClass(lab)]">
                {{ getLabStatusText(lab) }}
                <span v-if="calculateOvertimeMinutes(lab) > 0" class="ml-1">
                  (+{{ calculateOvertimeMinutes(lab) }}分)
                </span>
              </span>
            </td>
            <td class="px-6 py-4 ">
              <div class="flex space-x-2 justify-center">
                <button
                    @click="openEditDialog(lab)"
                    class="px-4 py-2 bg-gradient-to-r from-blue-500 to-indigo-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300 text-sm"
                >
                  <i class="fas fa-edit mr-1"></i>
                  编辑
                </button>
                <button
                    @click="confirmDeleteLab(lab)"
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
      <div v-if="filteredLabs.length === 0" class="text-center py-12">
        <i class="fas fa-flask text-4xl text-slate-300 mb-4"></i>
        <p class="text-slate-500">暂无实验室记录</p>
        <div class="mt-6 space-y-4">
          <button
              @click="openAddDialog"
              class="px-6 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-lg hover:shadow-md transition-all duration-300"
          >
            <i class="fas fa-plus mr-2"></i>
            添加第一条实验室记录
          </button>
          <p v-if="isFiltered && labs.length > 0" class="text-slate-400">
            或 <button @click="resetFilters" class="text-indigo-500 hover:text-indigo-600 underline">重置筛选条件</button> 查看所有记录
          </p>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="filteredLabs.length > 0" class="px-6 py-4 border-t border-slate-100">
        <div class="flex items-center justify-between">
          <div class="text-sm text-slate-600">
            显示第 {{ (currentPage - 1) * pageSize + 1 }} - {{ Math.min(currentPage * pageSize, filteredLabs.length) }} 条，共 {{ filteredLabs.length }} 条
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
                :disabled="currentPage * pageSize >= filteredLabs.length"
                :class="[
                'px-4 py-2 rounded-lg transition-colors',
                currentPage * pageSize >= filteredLabs.length
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
  labs: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits([
  'add-lab',
  'edit-lab',
  'delete-lab'
])

// 筛选条件
const filters = reactive({
  studentId: '',
  labName: '',
  status: '',
  dateRange: ''
})

// 分页
const currentPage = ref(1)
const pageSize = 5

// 工具函数
const showSuccess = (message) => {
  alert(` ${message}`)
}

const showError = (message) => {
  alert(` ${message}`)
}

const showConfirm = async (title, message) => {
  return window.confirm(`${title}\n\n${message}`)
}

// 安全获取值函数
const getSafeValue = (value1, value2, defaultValue = '') => {
  if (value1 !== null && value1 !== undefined && value1 !== '') {
    return value1
  }
  if (value2 !== null && value2 !== undefined && value2 !== '') {
    return value2
  }
  return defaultValue
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '--'
  try {
    const date = new Date(dateTime)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hour = String(date.getHours()).padStart(2, '0')
    const minute = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day} ${hour}:${minute}`
  } catch {
    return dateTime
  }
}

// 获取时长
const getDurationMinutes = (lab) => {
  if (lab.duration_minutes !== undefined && lab.duration_minutes !== null) {
    return lab.duration_minutes
  }

  if (lab.durationMinutes !== undefined && lab.durationMinutes !== null) {
    return lab.durationMinutes
  }

  const checkInTime = lab.check_in_time || lab.checkInTime
  const checkOutTime = lab.check_out_time || lab.checkOutTime

  if (!checkInTime || !checkOutTime) return 0

  try {
    const checkIn = new Date(checkInTime)
    const checkOut = new Date(checkOutTime)
    const durationMs = checkOut - checkIn
    return Math.round(durationMs / (1000 * 60))
  } catch {
    return 0
  }
}

// 时长样式
const getDurationClass = (lab) => {
  const duration = getDurationMinutes(lab)
  if (duration < 30) return 'bg-gradient-to-r from-blue-100 to-blue-200 text-blue-800'
  if (duration < 60) return 'bg-gradient-to-r from-green-100 to-emerald-200 text-green-800'
  if (duration < 120) return 'bg-gradient-to-r from-amber-100 to-yellow-200 text-amber-800'
  return 'bg-gradient-to-r from-red-100 to-pink-200 text-red-800'
}

// 计算实验室状态
const calculateLabStatus = (lab) => {
  const checkInTime = lab.check_in_time || lab.checkInTime
  const checkOutTime = lab.check_out_time || lab.checkOutTime
  const durationMinutes = getDurationMinutes(lab)

  if (checkOutTime && checkOutTime !== 'NULL' && checkOutTime !== '(NULL)') {
    if (durationMinutes > 0) {
      try {
        const checkIn = new Date(checkInTime)
        const checkOut = new Date(checkOutTime)
        const expectedEnd = new Date(checkIn.getTime() + (durationMinutes * 60 * 1000))
        if (checkOut > expectedEnd) {
          return '已超时'
        }
      } catch {
        // 解析失败，返回已完成
      }
    }
    return '已完成'
  }

  if (checkInTime) {
    try {
      const checkIn = new Date(checkInTime)

      if (durationMinutes > 0) {
        const expectedEnd = new Date(checkIn.getTime() + (durationMinutes * 60 * 1000))
        const now = new Date()

        if (now > expectedEnd) {
          return '已超时'
        } else {
          return '进行中'
        }
      }
      return '进行中'
    } catch {
      return '未知状态'
    }
  }

  return '未知状态'
}

// 计算超时分钟数
const calculateOvertimeMinutes = (lab) => {
  const checkInTime = lab.check_in_time || lab.checkInTime
  const checkOutTime = lab.check_out_time || lab.checkOutTime
  const durationMinutes = getDurationMinutes(lab)

  if (!checkInTime || durationMinutes <= 0) return 0

  try {
    const checkIn = new Date(checkInTime)
    const expectedEnd = new Date(checkIn.getTime() + (durationMinutes * 60 * 1000))

    if (checkOutTime && checkOutTime !== 'NULL' && checkOutTime !== '(NULL)') {
      const checkOut = new Date(checkOutTime)
      const overtime = Math.floor((checkOut - expectedEnd) / (1000 * 60))
      return Math.max(0, overtime)
    } else {
      const now = new Date()
      if (now > expectedEnd) {
        const overtime = Math.floor((now - expectedEnd) / (1000 * 60))
        return Math.max(0, overtime)
      }
    }
  } catch {
    return 0
  }

  return 0
}

// 获取实验室状态类
const getLabStatusClass = (lab) => {
  const status = calculateLabStatus(lab)
  const classMap = {
    '已完成': 'bg-gradient-to-r from-emerald-400 to-teal-500 text-white',
    '已超时': 'bg-gradient-to-r from-red-400 to-pink-500 text-white',
    '进行中': 'bg-gradient-to-r from-blue-400 to-indigo-500 text-white',
    '未知状态': 'bg-gradient-to-r from-slate-400 to-gray-500 text-white'
  }
  return classMap[status] || 'bg-gradient-to-r from-slate-400 to-gray-500 text-white'
}

// 获取实验室状态文本
const getLabStatusText = (lab) => {
  return calculateLabStatus(lab)
}

// 获取唯一键
const getLabKey = (lab) => {
  const attendanceId = getSafeValue(lab.attendance_id, lab.attendanceId)
  if (attendanceId) return String(attendanceId)

  const studentId = getSafeValue(lab.student_id, lab.studentId)
  const labName = getSafeValue(lab.lab_name, lab.labName)
  const checkInTime = lab.check_in_time || lab.checkInTime

  return `${studentId}-${labName}-${checkInTime}` || Math.random().toString()
}

// 判断是否正在筛选
const isFiltered = computed(() => {
  return Object.values(filters).some(value => value !== '')
})

// 筛选后的实验室
const filteredLabs = computed(() => {
  let result = [...props.labs]

  if (filters.studentId) {
    result = result.filter(lab => {
      const studentId = getSafeValue(lab.student_id, lab.studentId, '')
      return studentId.toString().toLowerCase().includes(filters.studentId.toLowerCase())
    })
  }

  if (filters.labName) {
    result = result.filter(lab => {
      const labName = getSafeValue(lab.lab_name, lab.labName, '')
      return labName.toLowerCase().includes(filters.labName.toLowerCase())
    })
  }

  if (filters.status) {
    result = result.filter(lab => {
      const status = calculateLabStatus(lab)
      return status === filters.status
    })
  }

  if (filters.dateRange) {
    const now = new Date()
    const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate())
    const todayEnd = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1)

    const weekStart = new Date(now.getFullYear(), now.getMonth(), now.getDate() - now.getDay())
    const weekEnd = new Date(weekStart.getFullYear(), weekStart.getMonth(), weekStart.getDate() + 7)

    const monthStart = new Date(now.getFullYear(), now.getMonth(), 1)
    const monthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 1)

    result = result.filter(lab => {
      const checkInTime = lab.check_in_time || lab.checkInTime
      if (!checkInTime) return false

      try {
        const checkInDate = new Date(checkInTime)

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

  result.sort((a, b) => {
    const timeA = a.check_in_time || a.checkInTime
    const timeB = b.check_in_time || b.checkInTime
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
const paginatedLabs = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return filteredLabs.value.slice(start, end)
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
  if (currentPage.value * pageSize < filteredLabs.value.length) {
    currentPage.value++
  }
}

// 对话框操作
const openAddDialog = () => {
  emit('add-lab')
}

const openEditDialog = (lab) => {
  emit('edit-lab', lab)
}

// 确认删除实验室记录
const confirmDeleteLab = async (lab) => {
  const attendanceId = getSafeValue(lab.attendance_id, lab.attendanceId)
  if (!attendanceId) return

  const studentId = getSafeValue(lab.student_id, lab.studentId)
  const labName = getSafeValue(lab.lab_name, lab.labName)

  const confirmed = await showConfirm(
      '确认删除',
      `确定要删除学生 ${studentId} 的 "${labName}" 实验室记录吗？`
  )
  if (confirmed) {
    emit('delete-lab', attendanceId)
  }
}

// 监听原始数据变化重置分页
watch(() => props.labs, () => {
  currentPage.value = 1
}, { immediate: true })
</script>