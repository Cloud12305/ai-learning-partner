<!-- ConfirmDialog.vue -->
<template>
  <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm" @click.self="$emit('close')">
    <div class="bg-white rounded-3xl p-8 max-w-md w-full mx-4 shadow-2xl">
      <div class="mb-6">
        <h3 v-if="title" class="text-xl font-bold text-slate-800 mb-2">{{ title }}</h3>
        <p class="text-slate-600">{{ message }}</p>
      </div>

      <div class="flex justify-end space-x-3">
        <button
            @click="$emit('close')"
            class="px-6 py-3 text-slate-600 font-semibold hover:bg-slate-100 rounded-xl transition-all duration-300"
        >
          取消
        </button>
        <button
            @click="handleConfirm"
        class="px-6 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300"
        >
        确定
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  },
  message: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['close', 'confirm'])

const handleConfirm = () => {
  console.log(' ConfirmDialog: 确定按钮被点击')
  emit('confirm')  // 触发 confirm 事件
}
</script>