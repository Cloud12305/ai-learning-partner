<template>
  <div class="bg-white rounded-xl shadow-md p-6 transition-all duration-300 hover:shadow-lg hover:-translate-y-1">
    <div :class="bgClass" class="w-14 h-14 rounded-lg flex items-center justify-center mb-5">
      <i :class="['fa fa-' + icon, textClass, 'text-2xl']"></i>
    </div>
    <h3 class="text-xl font-semibold mb-3">{{ title }}</h3>
    <p class="text-neutral-600">{{ description }}</p>
  </div>
</template>

<script>
export default {
  props: {
    icon: {
      type: String,
      required: true,
      description: "Font Awesome图标类名（不含fa-前缀）"
    },
    title: {
      type: String,
      required: true,
      description: "功能标题"
    },
    description: {
      type: String,
      required: true,
      description: "功能描述"
    },
    bgClass: {
      type: String,
      required: true,
      description: "背景颜色类名"
    },
    textClass: {
      type: String,
      required: true,
      description: "文本颜色类名"
    }
  }
}
</script>

<style scoped>
/* 组件内部样式 */
.rounded-xl {
  border-radius: 0.75rem;
}

.shadow-md {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.shadow-lg {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.transition-all {
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 150ms;
}

.hover\:-translate-y-1:hover {
  transform: translateY(-0.25rem);
}
</style>
