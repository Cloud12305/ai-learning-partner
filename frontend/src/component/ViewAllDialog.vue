<template>
  <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm" @click.self="$emit('close')">
    <div class="bg-white rounded-3xl p-8 max-w-4xl w-full mx-4 shadow-2xl max-h-[80vh] overflow-y-auto">
      <div class="flex items-center justify-between mb-6">
        <h3 class="text-xl font-bold text-slate-800">{{ title }}</h3>
        <button @click="$emit('close')" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
      </div>

      <div class="space-y-4">
        <!-- 课程数据 -->
        <template v-if="type === 'courses'">
          <div v-for="item in data" :key="item.id || item.recordId" class="bg-gradient-to-r from-slate-50 to-slate-100 rounded-xl p-4">
            <div class="flex items-center justify-between">
              <span class="font-semibold text-slate-800">{{ item.courseId }}</span>
              <span :class="['px-3 py-1 rounded-full text-sm font-medium', getScoreClass(item.score)]">
                {{ item.score }}
              </span>
            </div>
            <div class="mt-2 text-sm text-slate-500">
              {{ item.academicYear }} 第{{ item.semester }}学期 | 学分：{{ item.creditEarned }}
            </div>
          </div>
        </template>

        <!-- 实验室数据 - 已修复 -->
        <template v-else-if="type === 'labs'">
          <div v-for="item in data" :key="item.attendance_id || item.id" class="bg-gradient-to-r from-slate-50 to-slate-100 rounded-xl p-4">
            <div class="flex items-center justify-between mb-2">
              <h4 class="font-semibold text-slate-800">{{ item.lab_name || item.labName }}</h4>
              <span :class="[
                'px-3 py-1 rounded-full text-sm font-medium',
                getLabStatusClass(item)
              ]">
                {{ getLabStatusText(item) }}
              </span>
            </div>
            <p class="text-sm text-slate-600 mb-2">{{ item.experiment_name || item.experimentName }}</p>
            <div class="text-sm text-slate-500 space-y-1">
              <div>
                <span class="font-medium">签到：</span>
                <span class="font-mono">{{ formatLabTime(item.check_in_time || item.checkInTime) }}</span>
              </div>
              <div>
                <span class="font-medium">签退：</span>
                <span class="font-mono">{{ formatLabTime(item.check_out_time || item.checkOutTime) }}</span>
              </div>
              <div>
                <span class="font-medium">时长：</span>
                <span>{{ getDurationMinutes(item) }}分钟</span>
                <span v-if="calculateOvertimeMinutes(item) > 0" class="ml-3 text-red-500">
                  (超时{{ calculateOvertimeMinutes(item) }}分钟)
                </span>
              </div>
              <div v-if="item.student_id" class="text-slate-400">
                <span class="font-medium">学号：</span>
                <span>{{ item.student_id }}</span>
              </div>
            </div>
          </div>
        </template>

        <!-- 图书数据 -->
        <template v-else-if="type === 'books'">
          <div v-for="item in data" :key="item.id || item.recordId" class="bg-gradient-to-r from-slate-50 to-slate-100 rounded-xl p-4">
            <div class="flex items-center justify-between mb-2">
              <h4 class="font-semibold text-slate-800">{{ item.bookTitle || item.title }}</h4>
              <span :class="['px-3 py-1 rounded-full text-sm font-medium', getBookStatusClass(item)]">
                {{ getBookStatusText(item) }}
              </span>
            </div>
            <p class="text-sm text-slate-600 mb-2">{{ item.bookAuthor || item.author }}</p>
            <div class="text-sm text-slate-500">
              学号：{{ item.studentId }} |
              借阅：{{ formatDate(item.borrowDate || item.borrowTime) }} |
              应还：{{ formatDate(item.dueDate || item.dueTime) }}
              <span v-if="item.returnDate || item.returnTime" class="ml-2">
                | 归还：{{ formatDate(item.returnDate || item.returnTime) }}
              </span>
            </div>
            <div v-if="isBookOverdue(item)" class="mt-2 text-sm text-red-500">
              逾期天数：{{ calculateOverdueDays(item) }}天
            </div>
          </div>
        </template>

        <!-- 默认显示 -->
        <template v-else>
          <div v-for="item in data" :key="item.id || item.recordId" class="bg-gradient-to-r from-slate-50 to-slate-100 rounded-xl p-4">
            <pre>{{ JSON.stringify(item, null, 2) }}</pre>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// ==== 新增：实验室相关函数 ====
// 专门解析MySQL DATETIME字符串（无时区转换）
const parseMySQLDateTime = (datetimeStr) => {
  if (!datetimeStr || typeof datetimeStr !== 'string') return null

  const cleanStr = datetimeStr.trim()

  // 处理NULL值
  if (cleanStr === 'NULL' || cleanStr === '(NULL)') {
    return null
  }

  // MySQL DATETIME格式：2024-01-15 10:00:00
  const match = cleanStr.match(/^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})$/)
  if (match) {
    const [, year, month, day, hour, minute, second] = match.map(Number)
    const date = new Date(year, month - 1, day, hour, minute, second)
    return date.getTime()
  }

  return null
}

// 解析数据库时间
const parseDatabaseTime = (timeValue) => {
  if (!timeValue) return null

  // 如果是Date对象
  if (timeValue instanceof Date) {
    return timeValue.getTime()
  }

  // 如果是数字（时间戳）
  if (typeof timeValue === 'number') {
    if (timeValue > 1000000000000) {
      return timeValue
    }
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

  return null
}

// 格式化实验室时间
const formatLabTime = (timeValue) => {
  if (!timeValue) return '--'

  // 处理NULL值
  if (timeValue === 'NULL' || timeValue === '(NULL)' || timeValue === null) {
    return '--'
  }

  const timestamp = parseDatabaseTime(timeValue)
  if (!timestamp) {
    return String(timeValue)
  }

  const date = new Date(timestamp)

  // 手动拼接，避免时区转换
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  const second = String(date.getSeconds()).padStart(2, '0')

  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

// 获取时长
const getDurationMinutes = (lab) => {
  // 优先使用数据库的duration_minutes字段
  if (lab.duration_minutes !== undefined && lab.duration_minutes !== null) {
    return lab.duration_minutes
  }

  // 其次使用驼峰命名的durationMinutes
  if (lab.durationMinutes !== undefined && lab.durationMinutes !== null) {
    return lab.durationMinutes
  }

  // 最后从时间计算
  return calculateActualDuration(lab)
}

// 计算实际时长
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
      // 如果有预期时长，检查是否超时
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

// 实验室状态颜色分类
const getLabStatusClass = (lab) => {
  const status = calculateLabStatus(lab)

  if (status === '已完成') {
    return 'bg-emerald-100 text-emerald-700'
  } else if (status === '已超时') {
    return 'bg-red-100 text-red-700'
  } else if (status === '进行中') {
    return 'bg-blue-100 text-blue-700'
  } else {
    return 'bg-slate-100 text-slate-700'
  }
}

// 实验室状态文本
const getLabStatusText = (lab) => {
  return calculateLabStatus(lab)
}

// ==== 原有的通用函数 ====
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

    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
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

// 计算图书状态
const calculateBookStatus = (book) => {
  const returnDate = book.returnDate || book.returnTime
  const dueDate = book.dueDate || book.dueTime

  if (!dueDate) {
    return '未知状态'
  }

  const dueTimestamp = parseDateToTimestamp(dueDate)
  if (!dueTimestamp) {
    return '未知状态'
  }

  if (returnDate) {
    const returnTimestamp = parseDateToTimestamp(returnDate)
    if (returnTimestamp) {
      if (returnTimestamp <= dueTimestamp) {
        return '已归还'
      } else {
        return '已逾期'
      }
    }
  }

  const currentTimestamp = new Date().getTime()

  if (currentTimestamp > dueTimestamp) {
    return '已逾期'
  } else {
    return '借阅中'
  }
}

// 判断是否逾期
const isBookOverdue = (book) => {
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
    const returnTimestamp = parseDateToTimestamp(returnDate)
    if (returnTimestamp) {
      endTimestamp = returnTimestamp
    } else {
      endTimestamp = new Date().getTime()
    }
  } else {
    endTimestamp = new Date().getTime()
  }

  const diffTime = endTimestamp - dueTimestamp
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

  return Math.max(1, diffDays)
}

// 成绩颜色分类
const getScoreClass = (score) => {
  if (score >= 90) return 'bg-emerald-100 text-emerald-700'
  if (score >= 80) return 'bg-blue-100 text-blue-700'
  if (score >= 70) return 'bg-purple-100 text-purple-700'
  if (score >= 60) return 'bg-amber-100 text-amber-700'
  return 'bg-red-100 text-red-700'
}

// 图书状态颜色分类
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

// 图书状态文本
const getBookStatusText = (book) => {
  return calculateBookStatus(book)
}

defineProps({
  visible: Boolean,
  title: String,
  type: String,
  data: {
    type: Array,
    default: () => []
  }
})

defineEmits(['close'])
</script>