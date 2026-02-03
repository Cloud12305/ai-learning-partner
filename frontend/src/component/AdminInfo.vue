<template>
  <div class="bg-white/90 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-indigo-100">
    <div class="flex items-center justify-between mb-6">
      <h2 class="text-2xl font-bold text-indigo-800">
        <i class="fas fa-user-shield mr-3 text-indigo-500"></i>
        管理员信息
      </h2>
      <span class="bg-gradient-to-r from-indigo-500 to-purple-500 text-white px-4 py-1 rounded-full text-sm font-semibold shadow-md">
        <i class="fas fa-crown mr-1"></i>
        管理员
      </span>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <!-- 管理员个人信息卡片 -->
      <div class="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-2xl p-6">
        <div class="flex items-center mb-4">
          <div class="w-12 h-12 bg-gradient-to-r from-blue-500 to-indigo-500 rounded-xl flex items-center justify-center mr-4">
            <i class="fas fa-user-tie text-white text-xl"></i>
          </div>
          <div>
            <h3 class="font-bold text-slate-800">{{ adminInfo?.name || '管理员' }}</h3>
            <p class="text-sm text-slate-500">{{ adminInfo?.username || '--' }}</p>
          </div>
        </div>
        <div class="space-y-3">
          <div class="flex items-center text-sm text-slate-600">
            <i class="fas fa-id-badge mr-2 text-blue-500"></i>
            <span>工号：{{ adminInfo?.employeeId || '--' }}</span>
          </div>

          <!-- 专业 -->
          <div class="flex items-center text-sm text-slate-600">
            <i class="fas fa-graduation-cap mr-2 text-blue-500"></i>
            <span>专业：{{ adminInfo?.major || '--' }}</span>
          </div>

          <!-- 学院 -->
          <div class="flex items-center text-sm text-slate-600">
            <i class="fas fa-university mr-2 text-blue-500"></i>
            <span>学院：{{ adminInfo?.college || '--' }}</span>
          </div>

          <!-- 可编辑的邮箱 -->
          <div class="flex items-center justify-between">
            <div class="flex items-center text-sm text-slate-600 flex-1">
              <i class="fas fa-envelope mr-2 text-blue-500"></i>
              <div class="flex-1">
                <template v-if="editingField !== 'email'">
                  <span>邮箱：{{ adminInfo?.email || '--' }}</span>
                </template>
                <template v-else>
                  <input
                      v-model="editForm.email"
                      type="email"
                      placeholder="请输入邮箱"
                      class="w-full px-2 py-1 border border-slate-300 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                      @keyup.enter="saveField('email')"
                      @keyup.esc="cancelEdit"
                  />
                </template>
              </div>
            </div>
            <button
                v-if="editingField !== 'email'"
                @click="startEdit('email', adminInfo?.email || '')"
                class="ml-2 px-2 py-1 bg-blue-100 text-blue-600 text-xs rounded hover:bg-blue-200 transition-colors"
                title="编辑邮箱"
            >
              <i class="fas fa-edit"></i>
            </button>
            <div v-else class="ml-2 flex space-x-1">
              <button
                  @click="saveField('email')"
                  class="px-2 py-1 bg-blue-500 text-white text-xs rounded hover:bg-blue-600 transition-colors"
                  title="保存"
              >
                <i class="fas fa-check"></i>
              </button>
              <button
                  @click="cancelEdit"
                  class="px-2 py-1 bg-slate-300 text-slate-700 text-xs rounded hover:bg-slate-400 transition-colors"
                  title="取消"
              >
                <i class="fas fa-times"></i>
              </button>
            </div>
          </div>

          <!-- 可编辑的电话 -->
          <div class="flex items-center justify-between">
            <div class="flex items-center text-sm text-slate-600 flex-1">
              <i class="fas fa-phone mr-2 text-blue-500"></i>
              <div class="flex-1">
                <template v-if="editingField !== 'phone'">
                  <span>电话：{{ adminInfo?.phone || '--' }}</span>
                </template>
                <template v-else>
                  <input
                      v-model="editForm.phone"
                      type="tel"
                      placeholder="请输入电话"
                      class="w-full px-2 py-1 border border-slate-300 rounded text-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                      @keyup.enter="saveField('phone')"
                      @keyup.esc="cancelEdit"
                  />
                </template>
              </div>
            </div>
            <button
                v-if="editingField !== 'phone'"
                @click="startEdit('phone', adminInfo?.phone || '')"
                class="ml-2 px-2 py-1 bg-blue-100 text-blue-600 text-xs rounded hover:bg-blue-200 transition-colors"
                title="编辑电话"
            >
              <i class="fas fa-edit"></i>
            </button>
            <div v-else class="ml-2 flex space-x-1">
              <button
                  @click="saveField('phone')"
                  class="px-2 py-1 bg-blue-500 text-white text-xs rounded hover:bg-blue-600 transition-colors"
                  title="保存"
              >
                <i class="fas fa-check"></i>
              </button>
              <button
                  @click="cancelEdit"
                  class="px-2 py-1 bg-slate-300 text-slate-700 text-xs rounded hover:bg-slate-400 transition-colors"
                  title="取消"
              >
                <i class="fas fa-times"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 管理统计 -->
      <div class="bg-gradient-to-br from-emerald-50 to-teal-50 rounded-2xl p-6">
        <div class="flex items-center justify-between mb-4">
          <div class="w-12 h-12 bg-gradient-to-r from-emerald-500 to-teal-500 rounded-xl flex items-center justify-center">
            <i class="fas fa-users text-white text-xl"></i>
          </div>
          <span class="text-3xl font-bold text-slate-800">{{ adminStats?.studentCount || studentCount || 0 }}</span>
        </div>
        <h3 class="font-semibold text-slate-700 mb-2">管理学生总数</h3>
        <p class="text-sm text-slate-500">当前在籍学生</p>

        <!-- 显示更多统计信息 -->
        <div class="mt-4 space-y-2">
          <div class="flex justify-between text-sm text-slate-600">
            <span>男生人数：</span>
            <span class="font-semibold">{{ adminStats?.maleCount || 0 }}</span>
          </div>
          <div class="flex justify-between text-sm text-slate-600">
            <span>女生人数：</span>
            <span class="font-semibold">{{ adminStats?.femaleCount || 0 }}</span>
          </div>
          <div class="flex justify-between text-sm text-slate-600">
            <span>活跃学生：</span>
            <span class="font-semibold">{{ adminStats?.activeCount || 0 }}</span>
          </div>
        </div>

        <div v-if="adminInfo?.loginCount" class="mt-4 pt-3 border-t border-slate-200">
          <p class="text-sm text-slate-600">
            <i class="fas fa-sign-in-alt mr-1"></i>
            登录次数：{{ adminInfo.loginCount }}
          </p>
        </div>
      </div>

      <!-- 管理范围信息 -->
      <div class="bg-gradient-to-br from-purple-50 to-pink-50 rounded-2xl p-6">
        <div class="flex items-center mb-4">
          <div class="w-12 h-12 bg-gradient-to-r from-purple-500 to-pink-500 rounded-xl flex items-center justify-center mr-4">
            <i class="fas fa-chart-pie text-white text-xl"></i>
          </div>
          <div>
            <h3 class="font-semibold text-slate-700">管理范围</h3>
            <p class="text-sm text-slate-500">权限与管辖</p>
          </div>
        </div>

        <div class="space-y-3">
          <div class="flex items-center text-sm text-slate-600">
            <i class="fas fa-shield-alt mr-2 text-purple-500"></i>
            <span>角色：{{ adminInfo?.role || 'ADMIN' }}</span>
          </div>

          <div class="flex items-center text-sm text-slate-600">
            <i class="fas fa-clock mr-2 text-purple-500"></i>
            <span>最后登录：{{ formatDateTime(adminInfo?.lastLoginTime) || '未登录' }}</span>
          </div>

          <div v-if="adminInfo?.college" class="mt-3 pt-3 border-t border-slate-200">
            <p class="text-sm text-slate-700 font-semibold">管辖学院：</p>
            <p class="text-sm text-slate-600 mt-1">{{ adminInfo.college }}</p>
          </div>

          <div v-if="adminInfo?.major" class="mt-2">
            <p class="text-sm text-slate-700 font-semibold">管辖专业：</p>
            <p class="text-sm text-slate-600 mt-1">{{ adminInfo.major }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'

const props = defineProps({
  adminInfo: {
    type: Object,
    default: () => ({
      name: '',
      username: '',
      employeeId: '',
      email: '',
      phone: '',
      major: '',
      college: '',
      role: 'ADMIN',
      loginCount: 0,
      lastLoginTime: null
    })
  },
  adminStats: {
    type: Object,
    default: () => ({
      studentCount: 0,
      maleCount: 0,
      femaleCount: 0,
      activeCount: 0
    })
  },
  studentCount: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['update-admin-info'])

// 编辑状态管理
const editingField = ref('')
const editForm = ref({
  email: '',
  phone: ''
})

// 开始编辑
const startEdit = (field, currentValue) => {
  editingField.value = field
  editForm.value[field] = currentValue
}

// 保存字段
const saveField = async (field) => {
  const newValue = editForm.value[field].trim()
  const currentValue = props.adminInfo?.[field] || ''

  // 如果值没有变化，直接退出
  if (newValue === currentValue) {
    editingField.value = ''
    editForm.value[field] = ''
    return
  }

  // 触发更新事件
  emit('update-admin-info', {
    field: field,
    value: newValue
  })

  // 重置编辑状态
  editingField.value = ''
  editForm.value[field] = ''
}

// 取消编辑
const cancelEdit = () => {
  editingField.value = ''
  editForm.value.email = ''
  editForm.value.phone = ''
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '--'
  try {
    const date = new Date(dateString)
    return date.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
  } catch {
    return dateString
  }
}

// 格式化日期时间
const formatDateTime = (dateString) => {
  if (!dateString) return '未登录'
  try {
    const date = new Date(dateString)
    return date.toLocaleString('zh-CN', {
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch {
    return dateString
  }
}
</script>