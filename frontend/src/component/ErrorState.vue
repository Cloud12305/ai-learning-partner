<template>
  <div class="bg-gradient-to-r from-red-50 to-orange-50 rounded-2xl p-8 text-center shadow-lg">
    <div class="mb-6">
      <i class="fas fa-exclamation-triangle text-6xl text-red-400 mb-4"></i>
      <h3 class="text-2xl font-bold text-red-700 mb-2">加载失败</h3>
      <p class="text-red-600 mb-6">{{ error }}</p>
    </div>
    <button
        @click="$emit('reload')"
        class="px-8 py-3 bg-gradient-to-r from-red-500 to-orange-500 text-white font-semibold rounded-xl hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1"
    >
      <i class="fas fa-redo mr-2"></i>
      重新加载
    </button>
  </div>
</template>

<script setup>
defineProps({
  error: {
    type: String,
    required: true
  }
})

defineEmits(['reload'])
</script>