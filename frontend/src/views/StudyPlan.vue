<template>
  <div class="study-plan-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">学业规划</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto">制定科学合理的学习计划，助你高效完成学业目标，为未来发展奠定坚实基础</p>
      </div>
    </div>

    <!-- 主要内容 -->
    <main class="container mx-auto px-4 py-12">
      <div class="grid md:grid-cols-3 gap-8">
        <!-- 左侧：规划工具 -->
        <div class="md:col-span-1 bg-white rounded-xl shadow-md p-6">
          <h2 class="text-xl font-semibold mb-4">规划工具</h2>
          <div class="space-y-4">
            <Button type="primary" block class="mb-2">生成学习计划</Button>
            <Button block>导入课程表</Button>

            <div class="mt-6">
              <h3 class="font-medium mb-2">学习目标</h3>
              <Input placeholder="输入你的学习目标" class="mb-2" />
              <TextArea rows="3" placeholder="详细描述你的学习目标和期望" />
            </div>
          </div>
        </div>

        <!-- 右侧：规划内容 -->
        <div class="md:col-span-2 space-y-6">
          <!-- 推荐规划 -->
          <div class="bg-white rounded-xl shadow-md p-6">
            <h2 class="text-xl font-semibold mb-4">为你推荐的学业规划</h2>

            <div class="space-y-4">
              <div class="border-l-4 border-primary pl-4 py-1">
                <h3 class="font-medium">短期目标 (1-3个月)</h3>
                <p class="text-gray-600 text-sm">完成当前学期核心课程，平均成绩保持在85分以上</p>
              </div>

              <div class="border-l-4 border-success pl-4 py-1">
                <h3 class="font-medium">中期目标 (1年)</h3>
                <p class="text-gray-600 text-sm">掌握专业核心技能，完成2个相关实践项目</p>
              </div>

              <div class="border-l-4 border-warning pl-4 py-1">
                <h3 class="font-medium">长期目标 (3年)</h3>
                <p class="text-gray-600 text-sm">获得专业领域认证，发表1-2篇学术论文或参加行业竞赛</p>
              </div>
            </div>
          </div>

          <!-- 课程规划表 -->
          <div class="bg-white rounded-xl shadow-md p-6">
            <div class="flex justify-between items-center mb-4">
              <h2 class="text-xl font-semibold">课程规划表</h2>
              <Button type="primary" size="small">编辑规划</Button>
            </div>

            <Table
                :columns="columns"
                :data-source="coursePlanData"
                pagination="false"
            />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script lang="jsx">
import {Button, Input, TextArea, Table} from 'ant-design-vue'

export default {
  components: {
    Button,
    Input,
    TextArea,
    Table
  },
  setup() {
    // 课程规划表格列定义
    const columns = [
      {
        title: '学期',
        dataIndex: 'semester',
        key: 'semester',
      },
      {
        title: '课程名称',
        dataIndex: 'course',
        key: 'course',
      },
      {
        title: '目标成绩',
        dataIndex: 'target',
        key: 'target',
      },
      {
        title: '优先级',
        dataIndex: 'priority',
        key: 'priority',
        render: (priority) => {
          const colorMap = {
            高: 'red',
            中: 'orange',
            低: 'green'
          }
          return <span style={{color: colorMap[priority]}}>{priority}</span>
        }
      },
    ]

    // 课程规划数据
    const coursePlanData = [
      {
        key: '1',
        semester: '大一上',
        course: '高等数学',
        target: '90+',
        priority: '高'
      },
      {
        key: '2',
        semester: '大一上',
        course: '计算机基础',
        target: '85+',
        priority: '高'
      },
      {
        key: '3',
        semester: '大一下',
        course: '线性代数',
        target: '85+',
        priority: '中'
      },
      {
        key: '4',
        semester: '大一下',
        course: '程序设计入门',
        target: '90+',
        priority: '高'
      }
    ]

    return {
      columns,
      coursePlanData
    }
  }
}
</script>

<style scoped>
.study-plan-page {
  padding-top: 80px;
}

.page-header {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4eaf1 100%);
}
</style>