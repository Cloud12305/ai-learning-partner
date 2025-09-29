<template>
  <div class="job-matching-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">岗位匹配</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto">基于你的专业技能和兴趣，智能匹配最适合你的就业岗位</p>
      </div>
    </div>

    <!-- 主要内容 -->
    <main class="container mx-auto px-4 py-12">
      <div class="grid md:grid-cols-3 gap-8">
        <!-- 左侧：个人技能评估 -->
        <div class="md:col-span-1 space-y-6">
          <div class="bg-white rounded-xl shadow-md p-6">
            <h2 class="text-xl font-semibold mb-4">个人技能评估</h2>

            <div class="mb-6">
              <h3 class="font-medium mb-3">技能标签</h3>
              <div class="flex flex-wrap gap-2">
                <Tag color="blue">JavaScript</Tag>
                <Tag color="green">HTML/CSS</Tag>
                <Tag color="purple">Vue</Tag>
                <Tag color="orange">React</Tag>
                <Tag color="red">Node.js</Tag>
                <Tag color="geekblue">数据结构</Tag>
              </div>
              <Button type="dashed" size="small" class="mt-3">
                <i class="fa fa-plus mr-1"></i> 添加技能
              </Button>
            </div>

            <div>
              <h3 class="font-medium mb-3">求职意向</h3>
              <div class="space-y-3">
                <div>
                  <label class="text-sm text-gray-600 block mb-1">期望职位</label>
                  <Input placeholder="例如：前端开发工程师" value="前端开发工程师" />
                </div>
                <div>
                  <label class="text-sm text-gray-600 block mb-1">期望行业</label>
                  <Select placeholder="选择行业">
                    <Select.Option value="internet">互联网</Select.Option>
                    <Select.Option value="finance">金融</Select.Option>
                    <Select.Option value="education">教育</Select.Option>
                    <Select.Option value="medical">医疗健康</Select.Option>
                  </Select>
                </div>
                <div>
                  <label class="text-sm text-gray-600 block mb-1">期望地点</label>
                  <Select placeholder="选择城市">
                    <Select.Option value="beijing">北京</Select.Option>
                    <Select.Option value="shanghai">上海</Select.Option>
                    <Select.Option value="guangzhou">广州</Select.Option>
                    <Select.Option value="shenzhen">深圳</Select.Option>
                  </Select>
                </div>
              </div>
              <Button type="primary" block class="mt-4">更新求职意向</Button>
            </div>
          </div>
        </div>

        <!-- 右侧：匹配岗位 -->
        <div class="md:col-span-2">
          <div class="bg-white rounded-xl shadow-md p-6 mb-6">
            <h2 class="text-xl font-semibold mb-6">为你匹配的岗位</h2>

            <div class="space-y-6">
              <!-- 岗位1 -->
              <div class="border-b border-gray-100 pb-6 last:border-0 last:pb-0">
                <div class="flex flex-col md:flex-row md:items-center justify-between mb-3">
                  <div>
                    <h3 class="text-lg font-semibold">前端开发工程师</h3>
                    <p class="text-gray-600 text-sm">字节跳动 · 北京</p>
                  </div>
                  <div class="mt-2 md:mt-0">
                    <Badge color="success" class="mr-2">匹配度 92%</Badge>
                    <Button type="primary" size="small">立即申请</Button>
                  </div>
                </div>

                <div class="mb-3">
                  <div class="text-sm text-gray-700 mb-2">技能要求：</div>
                  <div class="flex flex-wrap gap-2">
                    <Tag>JavaScript</Tag>
                    <Tag>HTML/CSS</Tag>
                    <Tag>Vue</Tag>
                    <Tag>React</Tag>
                    <Tag>Webpack</Tag>
                  </div>
                </div>

                <div class="text-sm text-gray-600 line-clamp-2">
                  负责公司Web前端开发工作，参与产品需求分析和技术方案设计，与后端开发人员协作，完成功能模块的开发与维护。
                </div>
              </div>

              <!-- 岗位2 -->
              <div class="border-b border-gray-100 pb-6 last:border-0 last:pb-0">
                <div class="flex flex-col md:flex-row md:items-center justify-between mb-3">
                  <div>
                    <h3 class="text-lg font-semibold">全栈开发工程师</h3>
                    <p class="text-gray-600 text-sm">阿里巴巴 · 杭州</p>
                  </div>
                  <div class="mt-2 md:mt-0">
                    <Badge color="success" class="mr-2">匹配度 85%</Badge>
                    <Button type="primary" size="small">立即申请</Button>
                  </div>
                </div>

                <div class="mb-3">
                  <div class="text-sm text-gray-700 mb-2">技能要求：</div>
                  <div class="flex flex-wrap gap-2">
                    <Tag>JavaScript</Tag>
                    <Tag>Node.js</Tag>
                    <Tag>Vue</Tag>
                    <Tag>MongoDB</Tag>
                    <Tag>Express</Tag>
                  </div>
                </div>

                <div class="text-sm text-gray-600 line-clamp-2">
                  负责公司产品的前后端开发工作，参与技术架构设计和优化，解决开发过程中的技术难题，持续提升产品性能和用户体验。
                </div>
              </div>

              <!-- 岗位3 -->
              <div>
                <div class="flex flex-col md:flex-row md:items-center justify-between mb-3">
                  <div>
                    <h3 class="text-lg font-semibold">H5开发工程师</h3>
                    <p class="text-gray-600 text-sm">腾讯 · 深圳</p>
                  </div>
                  <div class="mt-2 md:mt-0">
                    <Badge color="success" class="mr-2">匹配度 88%</Badge>
                    <Button type="primary" size="small">立即申请</Button>
                  </div>
                </div>

                <div class="mb-3">
                  <div class="text-sm text-gray-700 mb-2">技能要求：</div>
                  <div class="flex flex-wrap gap-2">
                    <Tag>JavaScript</Tag>
                    <Tag>HTML/CSS</Tag>
                    <Tag>响应式设计</Tag>
                    <Tag>微信小程序</Tag>
                  </div>
                </div>

                <div class="text-sm text-gray-600 line-clamp-2">
                  负责公司移动端H5页面和小程序的开发工作，优化移动端体验，解决各种浏览器和设备兼容性问题，提升页面加载速度。
                </div>
              </div>
            </div>

            <div class="mt-6 text-center">
              <Button type="dashed">查看更多岗位</Button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import {Input, Select, Button, Tag, Badge} from 'ant-design-vue'

export default {
  components: {
    Input,
    Select,
    Button,
    Tag,
    Badge
  }
}
</script>

<style scoped>
.job-matching-page {
  padding-top: 80px;
}

.page-header {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4eaf1 100%);
}
</style>