<template>
  <div class="bg-white/90 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-purple-100">
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-xl font-bold text-purple-800">
        <i class="fas fa-flask mr-3 text-purple-500"></i>
        实验室记录
      </h2>
      <div class="flex items-center space-x-3">
        <!-- 状态筛选 -->
        <select
            v-model="statusFilter"
            class="px-3 py-2 bg-white border border-slate-300 rounded-lg text-sm focus:ring-2 focus:ring-purple-500 focus:border-purple-500"
        >
          <option value="">全部状态</option>
          <option value="completed">已完成</option>
          <option value="in-progress">进行中</option>
          <option value="overdue">已超时</option>
        </select>

        <button
            @click="$emit('show-all')"
            class="px-4 py-2 bg-gradient-to-r from-purple-500 to-pink-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300 text-sm"
        >
          查看全部
        </button>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="text-center py-8">
      <i class="fas fa-spinner fa-spin text-3xl text-purple-500 mb-3"></i>
      <p class="text-slate-500">正在加载实验室数据...</p>
    </div>

    <!-- 数据内容 -->
    <div v-else class="space-y-4">
      <div
          v-for="lab in filteredLabs"
          :key="lab.attendance_id || lab.attendanceId"
          class="bg-gradient-to-r from-purple-50 to-pink-50 rounded-xl p-4 hover:shadow-lg transition-all duration-300 transform hover:-translate-y-1"
      >
        <div class="flex items-center justify-between mb-2">
          <div class="flex-1">
            <h4 class="font-semibold text-slate-800">{{ lab.lab_name || lab.labName }}</h4>
            <p class="text-sm text-slate-600 mb-1">{{ lab.experiment_name || lab.experimentName }}</p>
          </div>
          <span :class="[
            'px-3 py-1 rounded-full text-sm font-medium whitespace-nowrap',
            getLabStatusClass(lab)
          ]">
            {{ getLabStatusText(lab) }}
          </span>
        </div>

        <div class="text-sm text-slate-500 space-y-1 mt-3 pt-3 border-t border-slate-100">
          <div>
            <span class="font-medium">签到时间：</span>
            <span class="font-mono">{{ displayTime(lab.check_in_time || lab.checkInTime) }}</span>
          </div>
          <div>
            <span class="font-medium">签退时间：</span>
            <span class="font-mono">{{ displayTime(lab.check_out_time || lab.checkOutTime) }}</span>
          </div>
          <div>
            <span class="font-medium">实验时长：</span>
            <span>{{ getDurationMinutes(lab) }}分钟</span>
          </div>
          <div v-if="calculateOvertimeMinutes(lab) > 0" class="text-red-500">
            <span class="font-medium">超时时间：</span>
            <span>{{ calculateOvertimeMinutes(lab) }}分钟</span>
          </div>
          <div v-if="lab.student_id" class="text-sm text-slate-400">
            <span class="font-medium">学号：</span>
            <span>{{ lab.student_id }}</span>
          </div>
        </div>
      </div>

      <!-- 无数据提示 -->
      <div v-if="filteredLabs.length === 0" class="text-center py-8 text-slate-400">
        <i class="fas fa-flask text-3xl mb-3"></i>
        <p v-if="statusFilter">该状态下暂无实验室记录</p>
        <p v-else>暂无实验室记录</p>
      </div>
    </div>

    <!-- 数据统计 -->
    <div v-if="!loading && labs.length > 0" class="mt-6 pt-6 border-t border-slate-100">
      <div class="flex justify-between text-sm text-slate-500">
        <span>总计：{{ labs.length }} 条</span>
        <span>进行中：{{ inProgressCount }} 条</span>
        <span>已完成：{{ completedCount }} 条</span>
        <span>已超时：{{ overdueCount }} 条</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, computed, onMounted} from 'vue'
import {getAllLabs} from '@/services/api/academic'

defineEmits(['show-all'])

// 数据状态
const labs = ref([])
const loading = ref(false)
const statusFilter = ref('')
const showDebug = ref(true) // 开发时设为true，生产时设为false

// 调试函数：显示原始时间
const debugRawTime = (timeValue) => {
  if (!timeValue) return 'null'
  return JSON.stringify(timeValue)
}

// 专门解析MySQL DATETIME字符串（无时区转换）
const parseMySQLDateTime = (datetimeStr) => {
  if (!datetimeStr || typeof datetimeStr !== 'string') return null

  const cleanStr = datetimeStr.trim()

  // 处理NULL值
  if (cleanStr === 'NULL' || cleanStr === '(NULL)') {
    return null
  }

  // MySQL DATETIME格式：2024-01-15 10:00:00
  // 使用正则表达式精确匹配
  const match = cleanStr.match(/^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})$/)
  if (match) {
    const [, year, month, day, hour, minute, second] = match.map(Number)
    const date = new Date(year, month - 1, day, hour, minute, second)

    // 验证解析是否正确
    if (date.getFullYear() !== year ||
        date.getMonth() + 1 !== month ||
        date.getDate() !== day ||
        date.getHours() !== hour ||
        date.getMinutes() !== minute) {
      console.warn('时间解析可能有问题:', {
        输入: cleanStr,
        解析结果: `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`
      })
    }

    return date.getTime()
  }

  return null
}

// 解析数据库时间（主函数）
const parseDatabaseTime = (timeValue) => {
  if (!timeValue) return null

  // 如果是Date对象
  if (timeValue instanceof Date) {
    return timeValue.getTime()
  }

  // 如果是数字（时间戳）
  if (typeof timeValue === 'number') {
    // 检查是否是毫秒时间戳
    if (timeValue > 1000000000000) {
      return timeValue
    }
    // 可能是秒时间戳
    if (timeValue > 1000000000) {
      return timeValue * 1000
    }
  }

  // 如果是字符串
  if (typeof timeValue === 'string') {
    // 先尝试MySQL格式解析
    const mysqlTimestamp = parseMySQLDateTime(timeValue)
    if (mysqlTimestamp !== null) {
      return mysqlTimestamp
    }

    // 其他格式尝试直接解析
    const date = new Date(timeValue)
    if (!isNaN(date.getTime())) {
      return date.getTime()
    }
  }

  console.warn('无法解析时间:', timeValue, typeof timeValue)
  return null
}

// 计算实际时长（从签到签退时间计算）
const calculateActualDuration = (lab) => {
  const checkInTime = lab.check_in_time || lab.checkInTime
  const checkOutTime = lab.check_out_time || lab.checkOutTime

  if (!checkInTime || !checkOutTime) return 0

  const checkInTimestamp = parseDatabaseTime(checkInTime)
  const checkOutTimestamp = parseDatabaseTime(checkOutTime)

  if (!checkInTimestamp || !checkOutTimestamp) return 0

  const durationMs = checkOutTimestamp - checkInTimestamp
  return Math.round(durationMs / (1000 * 60))
}

// 获取时长（优先使用数据库字段，否则计算）
const getDurationMinutes = (lab) => {
  // 优先使用数据库的duration_minutes字段
  if (lab.duration_minutes !== undefined && lab.duration_minutes !== null) {
    return lab.duration_minutes
  }

  // 其次使用驼峰命名的durationMinutes
  if (lab.durationMinutes !== undefined && lab.durationMinutes !== null) {
    return lab.durationMinutes
  }

  // 最后计算实际时长
  return calculateActualDuration(lab)
}

// 显示时间（数据库是多少就显示多少）
const displayTime = (timeValue) => {
  if (!timeValue) return '--'

  // 处理NULL值
  if (timeValue === 'NULL' || timeValue === '(NULL)' || timeValue === null) {
    return '--'
  }

  const timestamp = parseDatabaseTime(timeValue)
  if (!timestamp) {
    // 如果无法解析，直接显示原始字符串
    return String(timeValue)
  }

  const date = new Date(timestamp)

  // 格式化为本地时间字符串
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  const second = String(date.getSeconds()).padStart(2, '0')

  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

// 获取所有实验室记录
const fetchLabs = async () => {
  try {
    loading.value = true
    console.log('开始获取实验室记录...')

    const response = await getAllLabs()
    console.log(' API原始响应:', response)

    if (response && response.data) {
      const rawData = Array.isArray(response.data) ? response.data : []
      console.log(`获取到 ${rawData.length} 条原始记录`)

      // 记录第一条数据的完整结构
      if (rawData.length > 0) {
        const firstRecord = rawData[0]
        console.log('第一条记录完整结构:', JSON.stringify(firstRecord, null, 2))

        // 测试时间解析
        console.log(' 时间解析测试:')
        if (firstRecord.check_in_time) {
          const parsedTime = parseDatabaseTime(firstRecord.check_in_time)
          const date = new Date(parsedTime)
          console.log('原始签到时间:', firstRecord.check_in_time)
          console.log('解析后时间:', date.toString())
          console.log('显示格式:', displayTime(firstRecord.check_in_time))
        }
      }

      labs.value = rawData
    } else {
      console.warn('API返回异常:', response)
      labs.value = []
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    labs.value = []
  } finally {
    loading.value = false
  }
}

// 计算实验室状态
const calculateLabStatus = (lab) => {
  const checkInTime = lab.check_in_time || lab.checkInTime
  const checkOutTime = lab.check_out_time || lab.checkOutTime
  const durationMinutes = getDurationMinutes(lab)

  // 如果有签退时间且不为NULL
  if (checkOutTime && checkOutTime !== 'NULL' && checkOutTime !== '(NULL)') {
    const checkInTimestamp = parseDatabaseTime(checkInTime)
    const checkOutTimestamp = parseDatabaseTime(checkOutTime)

    if (checkInTimestamp && checkOutTimestamp) {
      // 如果数据库有预期时长，检查是否超时
      if (durationMinutes > 0) {
        const expectedEndTimestamp = checkInTimestamp + (durationMinutes * 60 * 1000)
        if (checkOutTimestamp > expectedEndTimestamp) {
          return '已超时'
        }
      }
      return '已完成'
    }
  }

  // 没有签退时间
  if (checkInTime) {
    const checkInTimestamp = parseDatabaseTime(checkInTime)
    if (checkInTimestamp) {
      // 如果有预期时长，检查是否超时
      if (durationMinutes > 0) {
        const expectedEndTimestamp = checkInTimestamp + (durationMinutes * 60 * 1000)
        const currentTimestamp = Date.now()

        if (currentTimestamp > expectedEndTimestamp) {
          return '已超时'
        } else {
          return '进行中'
        }
      }
      // 没有预期时长，但有签到时间
      return '进行中'
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

  const checkInTimestamp = parseDatabaseTime(checkInTime)
  if (!checkInTimestamp) return 0

  const expectedEndTimestamp = checkInTimestamp + (durationMinutes * 60 * 1000)

  // 如果有签退时间
  if (checkOutTime && checkOutTime !== 'NULL' && checkOutTime !== '(NULL)') {
    const checkOutTimestamp = parseDatabaseTime(checkOutTime)
    if (checkOutTimestamp) {
      const overtime = Math.floor((checkOutTimestamp - expectedEndTimestamp) / (1000 * 60))
      return Math.max(0, overtime)
    }
  } else {
    // 无签退时间，检查当前是否超时
    const currentTimestamp = Date.now()
    if (currentTimestamp > expectedEndTimestamp) {
      const overtime = Math.floor((currentTimestamp - expectedEndTimestamp) / (1000 * 60))
      return Math.max(0, overtime)
    }
  }

  return 0
}

// 获取实验室状态类
const getLabStatusClass = (lab) => {
  const status = calculateLabStatus(lab)
  const classMap = {
    '已完成': 'bg-emerald-100 text-emerald-700',
    '已超时': 'bg-red-100 text-red-700',
    '进行中': 'bg-blue-100 text-blue-700',
    '未知状态': 'bg-slate-100 text-slate-700'
  }
  return classMap[status] || 'bg-slate-100 text-slate-700'
}

// 获取实验室状态文本
const getLabStatusText = (lab) => {
  return calculateLabStatus(lab)
}

// 统计数量
const inProgressCount = computed(() => {
  return labs.value.filter(lab => calculateLabStatus(lab) === '进行中').length
})

const completedCount = computed(() => {
  return labs.value.filter(lab => calculateLabStatus(lab) === '已完成').length
})

const overdueCount = computed(() => {
  return labs.value.filter(lab => calculateLabStatus(lab) === '已超时').length
})

// 根据筛选条件过滤实验室记录
const filteredLabs = computed(() => {
  if (!labs.value || labs.value.length === 0) return []

  const filtered = statusFilter.value
      ? labs.value.filter(lab => {
        const status = calculateLabStatus(lab)
        const filterMap = {
          'in-progress': status === '进行中',
          'completed': status === '已完成',
          'overdue': status === '已超时'
        }
        return filterMap[statusFilter.value] || false
      })
      : labs.value

  // 按签到时间倒序排序
  return filtered.sort((a, b) => {
    const timeA = parseDatabaseTime(a.check_in_time || a.checkInTime) || 0
    const timeB = parseDatabaseTime(b.check_in_time || b.checkInTime) || 0
    return timeB - timeA
  })
})

onMounted(() => {
  console.log('实验室记录组件挂载')
  fetchLabs()
})
</script>