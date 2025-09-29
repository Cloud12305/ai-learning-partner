<template>
  <div class="practical-training-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">实战演练</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto">通过真实项目练习，提升实战能力，为就业做好充分准备</p>
      </div>
    </div>

    <!-- 主要内容 -->
    <main class="container mx-auto px-4 py-12">
      <!-- 筛选条件 -->
      <div class="bg-white rounded-xl shadow-md p-6 mb-8">
        <div class="flex flex-wrap gap-4">
          <Select
              placeholder="选择项目类型"
              style="width: 180px"
          >
            <Select.Option value="frontend">前端开发</Select.Option>
            <Select.Option value="backend">后端开发</Select.Option>
            <Select.Option value="fullstack">全栈开发</Select.Option>
            <Select.Option value="mobile">移动开发</Select.Option>
          </Select>
          <Select
              placeholder="难度级别"
              style="width: 150px"
          >
            <Select.Option value="beginner">入门</Select.Option>
            <Select.Option value="intermediate">中级</Select.Option>
            <Select.Option value="advanced">高级</Select.Option>
          </Select>
          <Select
              placeholder="完成时间"
              style="width: 180px"
          >
            <Select.Option value="1week">1周内</Select.Option>
            <Select.Option value="2week">2周内</Select.Option>
            <Select.Option value="1month">1个月内</Select.Option>
            <Select.Option value="long">1个月以上</Select.Option>
          </Select>
        </div>
      </div>

      <!-- 推荐项目 -->
      <div class="mb-12">
        <h2 class="text-2xl font-semibold mb-6">为你推荐的实战项目</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <!-- 项目1 -->
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow">
            <div class="relative">
              <img src="https://picsum.photos/800/400?random=10" alt="电商网站前端实现" class="w-full h-48 object-cover">
              <div class="absolute top-3 left-3 bg-primary text-white text-sm px-2 py-1 rounded-full">
                推荐
              </div>
              <div class="absolute top-3 right-3 bg-gray-800/80 text-white text-sm px-2 py-1 rounded-full">
                中级
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold">电商网站前端实现</h3>
                <span class="text-sm text-gray-500">前端开发</span>
              </div>
              <p class="text-gray-600 text-sm mb-4">
                实现一个完整的电商网站前端，包括首页、商品列表、详情页、购物车和结算功能
              </p>
              <div class="flex justify-between items-center mb-4">
                <div class="flex items-center text-sm text-gray-500">
                  <i class="fa fa-clock-o mr-1"></i> 预计7天
                </div>
                <div class="flex items-center text-sm text-gray-500">
                  <i class="fa fa-users mr-1"></i> 128人正在学习
                </div>
              </div>
              <Button type="primary" block>开始练习</Button>
            </div>
          </div>

          <!-- 项目2 -->
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow">
            <div class="relative">
              <img src="https://picsum.photos/800/400?random=11" alt="个人博客系统" class="w-full h-48 object-cover">
              <div class="absolute top-3 right-3 bg-gray-800/80 text-white text-sm px-2 py-1 rounded-full">
                全栈开发
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold">个人博客系统</h3>
                <span class="text-sm text-gray-500">中级</span>
              </div>
              <p class="text-gray-600 text-sm mb-4">
                开发一个完整的个人博客系统，包括前端展示和后端管理功能，支持文章发布、评论等功能
              </p>
              <div class="flex justify-between items-center mb-4">
                <div class="flex items-center text-sm text-gray-500">
                  <i class="fa fa-clock-o mr-1"></i> 预计14天
                </div>
                <div class="flex items-center text-sm text-gray-500">
                  <i class="fa fa-users mr-1"></i> 95人正在学习
                </div>
              </div>
              <Button type="primary" block>开始练习</Button>
            </div>
          </div>

          <!-- 项目3 -->
          <div class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow">
            <div class="relative">
              <img src="https://picsum.photos/800/400?random=12" alt="任务管理应用" class="w-full h-48 object-cover">
              <div class="absolute top-3 right-3 bg-gray-800/80 text-white text-sm px-2 py-1 rounded-full">
                入门
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold">任务管理应用</h3>
                <span class="text-sm text-gray-500">前端开发</span>
              </div>
              <p class="text-gray-600 text-sm mb-4">
                开发一个简单的任务管理应用，支持任务的增删改查、状态管理和分类功能
              </p>
              <div class="flex justify-between items-center mb-4">
                <div class="flex items-center text-sm text-gray-500">
                  <i class="fa fa-clock-o mr-1"></i> 预计3天
                </div>
                <div class="flex items-center text-sm text-gray-500">
                  <i class="fa fa-users mr-1"></i> 210人正在学习
                </div>
              </div>
              <Button type="primary" block>开始练习</Button>
            </div>
          </div>
        </div>
      </div>

      <!-- 正在进行的项目 -->
      <div>
        <h2 class="text-2xl font-semibold mb-6">正在进行的项目</h2>
        <div class="bg-white rounded-xl shadow-md p-6">
          <div class="flex flex-col md:flex-row gap-6">
            <div class="md:w-1/3">
              <img src="https://picsum.photos/800/600?random=13" alt="社交网络应用" class="w-full h-64 object-cover rounded-lg">
            </div>
            <div class="md:w-2/3">
              <div class="flex flex-wrap items-start justify-between mb-4">
                <div>
                  <h3 class="text-xl font-semibold">社交网络应用</h3>
                  <div class="flex items-center mt-1">
                    <Badge class="mr-2">全栈开发</Badge>
                    <Badge color="orange">高级</Badge>
                  </div>
                </div>
                <Button type="primary">继续开发</Button>
              </div>

              <div class="mb-4">
                <h4 class="font-medium mb-2">项目进度</h4>
                <div class="flex justify-between text-sm mb-1">
                  <span>已完成 6/10 个模块</span>
                  <span>60%</span>
                </div>
                <Progress percent="60" />
              </div>

              <div class="mb-4">
                <h4 class="font-medium mb-2">项目介绍</h4>
                <p class="text-gray-600 text-sm">
                  开发一个具有用户注册登录、动态发布、好友关注、消息通知等功能的社交网络应用，
                  前端使用React框架，后端使用Node.js和MongoDB数据库。
                </p>
              </div>

              <div class="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
                <div class="bg-gray-50 p-3 rounded-lg">
                  <div class="text-gray-500">开始日期</div>
                  <div class="font-medium">2023-05-10</div>
                </div>
                <div class="bg-gray-50 p-3 rounded-lg">
                  <div class="text-gray-500">预计完成</div>
                  <div class="font-medium">2023-06-20</div>
                </div>
                <div class="bg-gray-50 p-3 rounded-lg">
                  <div class="text-gray-500">已用时间</div>
                  <div class="font-medium">15天</div>
                </div>
                <div class="bg-gray-50 p-3 rounded-lg">
                  <div class="text-gray-500">提交次数</div>
                  <div class="font-medium">28次</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import {Select, Button, Badge, Progress} from 'ant-design-vue'

export default {
  components: {
    Select,
    Button,
    Badge,
    Progress
  }
}
</script>

<style scoped>
.practical-training-page {
  padding-top: 80px;
}

.page-header {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4eaf1 100%);
}
</style>