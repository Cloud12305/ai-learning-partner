<template>
  <div class="overflow-x-auto">
    <table class="min-w-full divide-y divide-neutral-200">
      <thead>
      <tr>
        <th class="px-4 py-3 bg-neutral-50 text-left text-xs font-medium text-neutral-500 uppercase tracking-wider">课程名称</th>
        <th class="px-4 py-3 bg-neutral-50 text-left text-xs font-medium text-neutral-500 uppercase tracking-wider">学分</th>
        <th class="px-4 py-3 bg-neutral-50 text-left text-xs font-medium text-neutral-500 uppercase tracking-wider">成绩</th>
        <th class="px-4 py-3 bg-neutral-50 text-left text-xs font-medium text-neutral-500 uppercase tracking-wider">等级</th>
        <th class="px-4 py-3 bg-neutral-50 text-left text-xs font-medium text-neutral-500 uppercase tracking-wider">学期</th>
      </tr>
      </thead>
      <tbody class="bg-white divide-y divide-neutral-200">
      <tr v-for="course in courses" :key="course.id" class="hover:bg-neutral-50 transition-colors">
        <td class="px-4 py-3 whitespace-nowrap">{{ course.name }}</td>
        <td class="px-4 py-3 whitespace-nowrap">{{ course.credit }}</td>
        <td class="px-4 py-3 whitespace-nowrap">{{ course.score }}</td>
        <td class="px-4 py-3 whitespace-nowrap">
          <span class="px-2 py-1 text-xs rounded-full bg-success/10 text-success">{{ course.grade }}</span>
        </td>
        <td class="px-4 py-3 whitespace-nowrap">{{ course.semester }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import { defineProps } from 'vue'

export default {
  props: defineProps({
    courses: {
      type: Array,
      required: true,
      description: '课程列表数据'
    }
  })
}
</script>
