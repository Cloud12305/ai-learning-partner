<template>
  <div class="border border-neutral-200 rounded-lg overflow-hidden hover:shadow-md transition-shadow">
    <div class="h-48 bg-neutral-100 flex items-center justify-center">
      <img :src="image" :alt="title" class="h-full w-auto object-cover">
    </div>
    <div class="p-4">
      <h5 class="font-medium mb-1">{{ title }}</h5>
      <p class="text-sm text-neutral-500 mb-2">{{ author }}</p>
      <div class="flex justify-between text-xs text-neutral-500">
        <span>{{ category }}</span>
        <span>{{ date }}</span>
      </div>
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
      description: '书籍标题'
    },
    author: {
      type: String,
      required: true,
      description: '书籍作者'
    },
    category: {
      type: String,
      required: true,
      description: '书籍类别'
    },
    date: {
      type: String,
      required: true,
      description: '借阅日期'
    },
    image: {
      type: String,
      required: true,
      description: '书籍封面图片URL'
    }
  })
}
</script>
