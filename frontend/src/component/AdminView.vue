<template>
  <div class="space-y-8">

    <!-- 管理员信息板块 -->
    <AdminInfo
        :admin-info="adminInfo"
        :admin-stats="adminStats"
        @update-admin-info="$emit('update-admin-info', $event)"
    />

    <!-- 管理员标签页 -->
    <div class="bg-white/90 backdrop-blur-sm rounded-3xl shadow-2xl border border-indigo-100 overflow-hidden">
      <!-- 标签页头部 -->
      <div class="border-b border-slate-200">
        <div class="flex space-x-1 px-6 pt-6">
          <button
              v-for="tab in tabs"
              :key="tab.id"
              @click="$emit('switch-tab', tab.id)"
              :class="[
              'px-6 py-3 font-semibold rounded-t-xl transition-all duration-300',
              adminActiveTab === tab.id
                ? 'bg-gradient-to-r from-indigo-500 to-purple-500 text-white shadow-md'
                : 'text-slate-600 hover:text-indigo-600 hover:bg-slate-50'
            ]"
          >
            <i :class="tab.icon + ' mr-2'"></i>
            {{ tab.name }}
          </button>
        </div>
      </div>

      <!-- 标签页内容 -->
      <div class="p-6">
        <!-- 成绩管理 -->
        <AdminCourses
            v-if="adminActiveTab === 'courses'"
            :courses="adminCourses"
            @add-course="$emit('add-course')"
            @import-courses="$emit('import-courses')"
            @edit-course="$emit('edit-course', $event)"
            @delete-course="$emit('delete-course', $event)"
        />

        <!-- 实验室管理 -->
        <AdminLabs
            v-else-if="adminActiveTab === 'labs'"
            :labs="adminLabs"
            @add-lab="$emit('add-lab')"
            @edit-lab="$emit('edit-lab', $event)"
            @delete-lab="$emit('delete-lab', $event)"
        />

        <!-- 图书管理 -->
        <AdminBooks
            v-else-if="adminActiveTab === 'books'"
            :books="adminBooks"
            @add-book="$emit('add-book')"
            @edit-book="$emit('edit-book', $event)"
            @delete-book="$emit('delete-book', $event)"
        />

        <!-- 学生管理 -->
        <AdminStudents
            v-else
            :students="adminStudents"
            @add-student="$emit('add-student')"
            @edit-student="$emit('edit-student', $event)"
            @toggle-status="$emit('toggle-status', $event)"
            @delete-student="$emit('delete-student', $event)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import AdminInfo from './AdminInfo.vue'
import AdminCourses from './AdminCourses.vue'
import AdminLabs from './AdminLabs.vue'
import AdminBooks from './AdminBooks.vue'
import AdminStudents from './AdminStudents.vue'

const tabs = [
  { id: 'courses', name: '成绩管理', icon: 'fas fa-graduation-cap' },
  { id: 'labs', name: '实验室管理', icon: 'fas fa-flask' },
  { id: 'books', name: '图书管理', icon: 'fas fa-book' },
  { id: 'students', name: '学生管理', icon: 'fas fa-users' }
]

defineProps({
  adminActiveTab: String,
  adminCourses: Array,
  adminLabs: Array,
  adminBooks: Array,
  adminStudents: Array,
  adminInfo: Object,
  adminStats: Object
})

defineEmits([
  'refresh-data',
  'export-data',
  'switch-tab',
  'add-course',
  'import-courses',
  'edit-course',
  'delete-course',
  'add-lab',
  'edit-lab',
  'delete-lab',
  'add-book',
  'edit-book',
  'delete-book',
  'add-student',
  'view-student',
  'edit-student',
  'toggle-status',
  'delete-student'
])
</script>