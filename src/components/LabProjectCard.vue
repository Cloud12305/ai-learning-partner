
<template>
  <div class="border border-neutral-200 rounded-lg p-4 hover:shadow-md transition-shadow">
    <div class="flex justify-between items-start mb-3">
      <h5 class="font-medium">{{ title }}</h5>
      <span
          class="px-2 py-1 text-xs rounded-full"
          :class="status === 'completed' ? 'bg-success/10 text-success' : 'bg-warning/10 text-warning'"
      >
        {{ status === 'completed' ? '已完成' : '进行中' }}
      </span>
    </div>
    <p class="text-sm text-neutral-600 mb-3">{{ description }}</p>
    <div class="flex justify-between text-sm text-neutral-500">
      <span><i class="fa fa-calendar-o mr-1"></i> {{ date }}</span>
      <span><i class="fa fa-users mr-1"></i> {{ members }}人</span>
    </div>
  </div>
</template>

<script>
import { defineProps } from 'vue'

export default {
  props: defineProps({
    title: {
      type: String,
      required: true,
      description: '项目标题'
    },
    description: {
      type: String,
      required: true,
      description: '项目描述'
    },
    status: {
      type: String,
      required: true,
      validator: (value) => {
        return ['completed', 'inProgress'].includes(value)
      },
      description: '项目状态: completed-已完成, inProgress-进行中'
    },
    date: {
      type: String,
      required: true,
      description: '项目时间'
    },
    members: {
      type: Number,
      required: true,
      description: '项目成员数量'
    }
  })
}
</script>
