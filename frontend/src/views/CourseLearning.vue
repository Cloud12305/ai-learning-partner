<template>
  <div class="course-learning-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">课程学习</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto">丰富的课程资源，助你系统掌握各学科知识，提升专业技能</p>
      </div>
    </div>

    <!-- 主要内容 -->
    <main class="container mx-auto px-4 py-12">
      <!-- 课程搜索和筛选 -->
      <div class="bg-white rounded-xl shadow-md p-6 mb-8">
        <div class="flex flex-col md:flex-row gap-4">
          <Input
              placeholder="搜索课程名称或关键词"
              class="flex-1"
          >
            <template #prefix>
              <i class="fa fa-search" ></i>
            </template>
          </Input>
          <Select
              placeholder="选择课程分类"
              style="width: 200px"
          >
            <Select.Option value="math">数学</Select.Option>
            <Select.Option value="computer">计算机</Select.Option>
            <Select.Option value="language">语言</Select.Option>
            <Select.Option value="professional">专业课程</Select.Option>
          </Select>
          <Select
              placeholder="难度级别"
              style="width: 150px"
          >
            <Select.Option value="beginner">入门</Select.Option>
            <Select.Option value="intermediate">中级</Select.Option>
            <Select.Option value="advanced">高级</Select.Option>
          </Select>
        </div>
      </div>

      <!-- 正在学习的课程 -->
      <div class="mb-12">
        <h2 class="text-2xl font-semibold mb-6">正在学习</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow">
            <div class="relative">
              <img src="https://picsum.photos/800/400?random=1" alt="JavaScript高级编程" class="w-full h-48 object-cover">
              <div class="absolute top-3 right-3 bg-primary text-white text-sm px-2 py-1 rounded-full">
                进行中 65%
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold">JavaScript高级编程</h3>
                <span class="text-sm text-gray-500">计算机</span>
              </div>
              <p class="text-gray-600 text-sm mb-4">深入学习JavaScript的高级特性、设计模式和性能优化技巧</p>
              <div class="mb-4">
                <div class="flex justify-between text-sm mb-1">
                  <span>学习进度</span>
                  <span>65%</span>
                </div>
                <Progress percent="65" size="small" />
              </div>
              <Button type="primary" block>继续学习</Button>
            </div>
          </div>
        </div>
      </div>

      <!-- 推荐课程 -->
      <div>
        <h2 class="text-2xl font-semibold mb-6">推荐课程</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <!-- 课程卡片1 -->
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow">
            <div class="relative">
              <img src="https://picsum.photos/800/400?random=2" alt="数据结构与算法" class="w-full h-48 object-cover">
              <div class="absolute top-3 right-3 bg-gray-800/80 text-white text-sm px-2 py-1 rounded-full">
                中级
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold">数据结构与算法</h3>
                <span class="text-sm text-gray-500">计算机</span>
              </div>
              <p class="text-gray-600 text-sm mb-4">掌握常用数据结构与算法，提升编程思维和问题解决能力</p>
              <Button block>加入学习</Button>
            </div>
          </div>

          <!-- 课程卡片2 -->
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow">
            <div class="relative">
              <img src="https://picsum.photos/800/400?random=3" alt="线性代数" class="w-full h-48 object-cover">
              <div class="absolute top-3 right-3 bg-gray-800/80 text-white text-sm px-2 py-1 rounded-full">
                中级
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold">线性代数</h3>
                <span class="text-sm text-gray-500">数学</span>
              </div>
              <p class="text-gray-600 text-sm mb-4">从基础到应用，全面掌握线性代数的核心概念与解题方法</p>
              <Button block>加入学习</Button>
            </div>
          </div>

          <!-- 课程卡片3 -->
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow">
            <div class="relative">
              <img src="https://picsum.photos/800/400?random=4" alt="专业英语" class="w-full h-48 object-cover">
              <div class="absolute top-3 right-3 bg-gray-800/80 text-white text-sm px-2 py-1 rounded-full">
                入门
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold">专业英语</h3>
                <span class="text-sm text-gray-500">语言</span>
              </div>
              <p class="text-gray-600 text-sm mb-4">掌握专业领域常用英语词汇和表达方式，提升阅读专业文献能力</p>
              <Button block>加入学习</Button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script >
import {Input, Select, Button, Progress} from 'ant-design-vue'

export default {
  components: {
    Input,
    Select,
    Button,
    Progress
  }
}
</script>

<style scoped>
.course-learning-page {
  padding-top: 80px;
}

.page-header {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4eaf1 100%);
}
</style>