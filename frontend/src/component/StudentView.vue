<template>
  <div class="space-y-8">
    <!-- 个人信息 -->
    <StudentProfile
        :profile="profile"
        :learning-goal="learningGoal"
        @edit-learning-goal="$emit('edit-learning-goal')"
        @update-info="$emit('update-info', $event)"
    />

    <!-- 学业统计 -->
    <StudentStats :profile="profile" />

    <!-- 数据展示区域 -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <!-- 左侧：课程成绩和图书借阅 -->
      <div class="space-y-8">
        <!-- 课程成绩 -->
        <StudentCourses
            :courses="profile.recentCourses || []"
            @show-all="$emit('show-all-courses')"
        />

        <!-- 图书借阅 -->
        <StudentBooks
            :books="profile.recentBooks || []"
            @show-all="$emit('show-all-books')"
        />
      </div>

      <!-- 右侧：实验室记录和图表 -->
      <div class="space-y-8">
        <!-- 实验室记录 -->
        <StudentLabs
            :labs="profile.recentLabs || []"
            @show-all="$emit('show-all-labs')"
        />

        <!-- 图表区域 -->
        <StudentCharts :profile="profile" />
      </div>
    </div>
  </div>
</template>

<script setup>
import StudentProfile from './StudentProfile.vue'
import StudentStats from './StudentStats.vue'
import StudentCourses from './StudentCourses.vue'
import StudentBooks from './StudentBooks.vue'
import StudentLabs from './StudentLabs.vue'
import StudentCharts from './StudentCharts.vue'

const props = defineProps({
  profile: {
    type: Object,
    default: () => ({
      userInfo: {
        learningGoal: ''
      },
      courses: [],
      labs: [],
      books: [],
      stats: {}
    })
  },
  learningGoal: {
    type: String,
    default: ''
  }
})

// 确保定义了 update-info 事件
defineEmits(['edit-learning-goal', 'show-all-courses', 'show-all-labs', 'show-all-books', 'update-info'])
</script>