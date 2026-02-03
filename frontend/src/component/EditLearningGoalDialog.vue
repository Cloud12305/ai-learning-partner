<template>
  <div v-if="visible" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm" @click.self="$emit('close')">
    <div class="bg-white rounded-3xl p-8 max-w-2xl w-full mx-4 shadow-2xl">
      <div class="flex items-center justify-between mb-6">
        <h3 class="text-xl font-bold text-slate-800">编辑学习目标</h3>
        <button @click="$emit('close')" class="text-slate-400 hover:text-slate-600 text-2xl">&times;</button>
      </div>

      <div class="mb-6">
        <div class="mb-4">
          <label class="block text-sm font-medium text-slate-700 mb-2">
            学习目标
            <span class="text-slate-400 text-sm ml-2">(最多500字)</span>
          </label>
          <textarea
              v-model="localLearningGoal"
              rows="8"
              maxlength="500"
              class="w-full px-4 py-3 border border-slate-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-300"
              placeholder="请输入你的学习目标..."
          ></textarea>
          <div class="text-right text-sm text-slate-500 mt-1">
            {{ localLearningGoal.length }}/500
          </div>
        </div>
      </div>

      <div class="flex justify-end space-x-3">
        <button
            @click="$emit('close')"
            class="px-6 py-3 text-slate-600 font-semibold hover:bg-slate-100 rounded-xl transition-all duration-300"
        >
          取消
        </button>
        <button
            @click="handleSave"
            :disabled="!localLearningGoal.trim()"
            class="px-6 py-3 bg-gradient-to-r from-indigo-500 to-purple-500 text-white font-semibold rounded-xl hover:shadow-lg transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          保存
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  learningGoal: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['close', 'save', 'update:learningGoal'])

// 使用本地变量来避免直接修改props
const localLearningGoal = ref('')

// 监听props变化，更新本地变量
watch(() => props.visible, (newVal) => {
  if (newVal) {
    localLearningGoal.value = props.learningGoal || ''
  }
})

// 监听learningGoal prop变化
watch(() => props.learningGoal, (newVal) => {
  localLearningGoal.value = newVal || ''
})

const handleSave = () => {
  if (localLearningGoal.value.trim()) {
    // 先触发更新事件，让父组件知道最新的值
    emit('update:learningGoal', localLearningGoal.value)
    // 再触发保存事件
    emit('save', localLearningGoal.value)
  }
}
</script>