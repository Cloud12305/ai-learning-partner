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
  name: 'FeatureCard',
  props: {
    icon: {
      type: String,
      required: true
    },
    title: {
      type: String,
      required: true
    },
    description: {
      type: String,
      required: true
    },
    bgClass: {
      type: String,
      required: true
    },
    textClass: {
      type: String,
      required: true
    }
  }
}
</script>