<template>
  <div v-if="profile?.userInfo" class="bg-white/90 backdrop-blur-sm rounded-3xl p-8 shadow-2xl border border-indigo-100">
    <div class="flex items-center justify-between mb-8">
      <h2 class="text-2xl font-bold text-indigo-800">
        <i class="fas fa-id-card mr-3 text-indigo-500"></i>
        个人信息
      </h2>
      <span class="bg-gradient-to-r from-emerald-500 to-teal-500 text-white px-4 py-1 rounded-full text-sm font-semibold shadow-md">
        <i class="fas fa-user-graduate mr-1"></i>
        学生
      </span>
    </div>

    <div class="flex flex-col lg:flex-row items-start gap-8">
      <!-- 头像区域 -->
      <div class="flex-shrink-0">
        <div class="relative">
          <img
              :src="profile.userInfo.avatarUrl || 'https://picsum.photos/150/150?random=1'"
              alt="头像"
              class="w-40 h-40 rounded-2xl object-cover shadow-lg border-4 border-white"
          >
          <div class="absolute -bottom-3 -right-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white px-4 py-2 rounded-xl shadow-lg">
            <i class="fas fa-star mr-1"></i>
            {{ profile.userInfo.grade || '未知' }}
          </div>
        </div>
      </div>

      <!-- 信息区域 -->
      <div class="flex-1">
        <div class="mb-6">
          <h3 class="text-3xl font-bold text-slate-800 mb-2">{{ profile.userInfo.name || '未设置姓名' }}</h3>
          <div class="flex items-center space-x-4">
            <span class="bg-indigo-100 text-indigo-700 px-3 py-1 rounded-lg">
              <i class="fas fa-id-badge mr-1"></i>
              {{ profile.userInfo.studentId || '未设置学号' }}
            </span>
            <span class="bg-purple-100 text-purple-700 px-3 py-1 rounded-lg">
              <i class="fas fa-graduation-cap mr-1"></i>
              {{ profile.userInfo.major || '未设置专业' }}
            </span>
          </div>
        </div>

        <!-- 信息网格 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div class="bg-gradient-to-br from-blue-50 to-indigo-50 rounded-xl p-4">
            <div class="flex items-center mb-2">
              <div class="w-10 h-10 bg-gradient-to-r from-blue-500 to-indigo-500 rounded-lg flex items-center justify-center mr-3">
                <i class="fas fa-university text-white"></i>
              </div>
              <div class="flex-1">
                <p class="text-sm text-slate-500">学院</p>
                <p class="font-semibold text-slate-800">{{ profile.userInfo.college || '未设置' }}</p>
              </div>
            </div>
          </div>

          <div class="bg-gradient-to-br from-purple-50 to-pink-50 rounded-xl p-4">
            <div class="flex items-center mb-2">
              <div class="w-10 h-10 bg-gradient-to-r from-purple-500 to-pink-500 rounded-lg flex items-center justify-center mr-3">
                <i class="fas fa-users text-white"></i>
              </div>
              <div class="flex-1">
                <p class="text-sm text-slate-500">班级</p>
                <p class="font-semibold text-slate-800">{{ profile.userInfo.className || '未设置' }}</p>
              </div>
            </div>
          </div>

          <!-- 邮箱-->
          <div class="bg-gradient-to-br from-cyan-50 to-teal-50 rounded-xl p-4">
            <div class="flex items-center justify-between mb-2">
              <div class="flex items-center flex-1">
                <div class="w-10 h-10 bg-gradient-to-r from-cyan-500 to-teal-500 rounded-lg flex items-center justify-center mr-3">
                  <i class="fas fa-envelope text-white"></i>
                </div>
                <div class="flex-1">
                  <p class="text-sm text-slate-500">邮箱</p>
                  <template v-if="editingField !== 'email'">
                    <p class="font-semibold text-slate-800">{{ profile.userInfo.email || '未设置' }}</p>
                  </template>
                  <template v-else>
                    <input
                        v-model="editForm.email"
                        type="email"
                        placeholder="请输入邮箱"
                        class="w-full px-2 py-1 border border-slate-300 rounded text-sm focus:ring-2 focus:ring-cyan-500 focus:border-cyan-500"
                        @keyup.enter="saveField('email')"
                        @keyup.esc="cancelEdit"
                    />
                  </template>
                </div>
              </div>
              <button
                  v-if="editingField !== 'email'"
                  @click="startEdit('email', profile.userInfo.email || '')"
                  class="ml-2 px-2 py-1 bg-cyan-100 text-cyan-600 text-xs rounded hover:bg-cyan-200 transition-colors"
                  title="编辑邮箱"
              >
                <i class="fas fa-edit"></i>
              </button>
              <div v-else class="ml-2 flex space-x-1">
                <button
                    @click="saveField('email')"
                    class="px-2 py-1 bg-cyan-500 text-white text-xs rounded hover:bg-cyan-600 transition-colors"
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

          <!-- 电话-->
          <div class="bg-gradient-to-br from-amber-50 to-orange-50 rounded-xl p-4">
            <div class="flex items-center justify-between mb-2">
              <div class="flex items-center flex-1">
                <div class="w-10 h-10 bg-gradient-to-r from-amber-500 to-orange-500 rounded-lg flex items-center justify-center mr-3">
                  <i class="fas fa-phone text-white"></i>
                </div>
                <div class="flex-1">
                  <p class="text-sm text-slate-500">联系电话</p>
                  <template v-if="editingField !== 'phone'">
                    <p class="font-semibold text-slate-800">{{ profile.userInfo.phone || '未设置' }}</p>
                  </template>
                  <template v-else>
                    <input
                        v-model="editForm.phone"
                        type="tel"
                        placeholder="请输入电话"
                        class="w-full px-2 py-1 border border-slate-300 rounded text-sm focus:ring-2 focus:ring-amber-500 focus:border-amber-500"
                        @keyup.enter="saveField('phone')"
                        @keyup.esc="cancelEdit"
                    />
                  </template>
                </div>
              </div>
              <button
                  v-if="editingField !== 'phone'"
                  @click="startEdit('phone', profile.userInfo.phone || '')"
                  class="ml-2 px-2 py-1 bg-amber-100 text-amber-600 text-xs rounded hover:bg-amber-200 transition-colors"
                  title="编辑电话"
              >
                <i class="fas fa-edit"></i>
              </button>
              <div v-else class="ml-2 flex space-x-1">
                <button
                    @click="saveField('phone')"
                    class="px-2 py-1 bg-amber-500 text-white text-xs rounded hover:bg-amber-600 transition-colors"
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

          <!-- 学习目标区域 -->
          <div class="bg-gradient-to-br from-sky-50 to-blue-50 rounded-xl p-4">
            <div class="flex items-center mb-2">
              <div class="w-10 h-10 bg-gradient-to-r from-sky-500 to-blue-500 rounded-lg flex items-center justify-center mr-3">
                <i class="fas fa-bullseye text-white"></i>
              </div>
              <div class="flex-1">
                <div class="flex items-center justify-between">
                  <div>
                    <p class="text-sm text-slate-500">学习目标</p>
                    <!-- 显示学习目标 -->
                    <template v-if="learningGoal">
                      <p class="font-semibold text-slate-800 whitespace-pre-line mt-1">{{ learningGoal }}</p>
                    </template>
                    <template v-else>
                      <p class="font-semibold text-slate-400 italic mt-1">暂无学习目标</p>
                    </template>
                  </div>
                  <button
                      @click="$emit('edit-learning-goal')"
                      class="ml-2 px-2 py-1 bg-sky-100 text-sky-600 text-xs rounded hover:bg-sky-200 transition-colors"
                      title="编辑学习目标"
                  >
                    <i class="fas fa-edit"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div class="rounded-xl p-4 opacity-0">
            <!-- 空模块，用于保持网格布局 -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'

const props = defineProps({
  profile: {
    type: Object,
    default: () => ({
      userInfo: {
        name: '',
        studentId: '',
        major: '',
        college: '',
        className: '',
        email: '',
        phone: '',
        avatarUrl: '',
        grade: ''
      }
    })
  },
  learningGoal: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['edit-learning-goal', 'update-info'])

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

  // 使用 props.profile 而不是直接使用 profile
  const currentValue = props.profile?.userInfo?.[field] || ''

  // 如果值没有变化，直接退出
  if (newValue === currentValue) {
    console.log('值未变化，不需要更新')
    editingField.value = ''
    editForm.value[field] = ''
    return
  }

  console.log('准备更新字段:', field, '新值:', newValue, '旧值:', currentValue)

  // 触发更新事件
  emit('update-info', {
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
</script>