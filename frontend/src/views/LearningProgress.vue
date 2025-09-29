<template>
  <div class="learning-progress-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">学习进度</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto">实时追踪你的学习进度，掌握学习节奏，高效完成学习目标</p>
      </div>
    </div>

    <!-- 主要内容 -->
    <main class="container mx-auto px-4 py-12">
      <div class="grid md:grid-cols-3 gap-8">
        <!-- 左侧：进度概览 -->
        <div class="md:col-span-1 space-y-6">
          <!-- 总体进度 -->
          <div class="bg-white rounded-xl shadow-md p-6">
            <h2 class="text-xl font-semibold mb-6">总体进度</h2>
            <div class="flex flex-col items-center">
              <div class="relative mb-4">
                <CircleProgress
                    :percent="68"
                    strokeWidth="8"
                    :strokeColor="['#1890ff', '#e6f7ff']"
                    size="160"
                >
                  <div class="text-center">
                    <span class="text-3xl font-bold">{{ 68 }}%</span>
                    <div class="text-sm text-gray-500">已完成</div>
                  </div>
                </CircleProgress>
              </div>
              <p class="text-sm text-gray-600 text-center">
                本学期已完成68%的学习计划，继续加油！
              </p>
            </div>
          </div>

          <!-- 学习时间统计 -->
          <div class="bg-white rounded-xl shadow-md p-6">
            <h2 class="text-xl font-semibold mb-4">学习时间</h2>
            <div class="space-y-4">
              <div>
                <div class="flex justify-between text-sm mb-1">
                  <span>本周学习</span>
                  <span>12.5小时</span>
                </div>
                <Progress percent="75" size="small" />
              </div>
              <div>
                <div class="flex justify-between text-sm mb-1">
                  <span>本月学习</span>
                  <span>48.2小时</span>
                </div>
                <Progress percent="62" size="small" />
              </div>
              <div>
                <div class="flex justify-between text-sm mb-1">
                  <span>本学期学习</span>
                  <span>186.7小时</span>
                </div>
                <Progress percent="68" size="small" />
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：详细进度 -->
        <div class="md:col-span-2 space-y-6">
          <!-- 课程进度 -->
          <div class="bg-white rounded-xl shadow-md p-6">
            <h2 class="text-xl font-semibold mb-6">课程进度</h2>
            <Table
                :columns="columns"
                :data-source="courseProgressData"
                pagination="false"
            />
          </div>

          <!-- 每周学习趋势 -->
          <div class="bg-white rounded-xl shadow-md p-6">
            <h2 class="text-xl font-semibold mb-6">每周学习趋势</h2>
            <div class="h-80">
              <LineChart
                  :data="chartData"
                  :x-axis="xAxis"
                  :y-axis="yAxis"
                  :series="series"
                  :legend="false"
                  :tooltip="true"
              />
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script lang="jsx">
import {Table, Progress, CircleProgress, LineChart} from 'ant-design-vue'

export default {
  components: {
    Table,
    Progress,
    CircleProgress,
    LineChart
  },
  setup() {
    // 课程进度表格列定义
    const columns = [
      {
        title: '课程名称',
        dataIndex: 'course',
        key: 'course',
      },
      {
        title: '进度',
        dataIndex: 'progress',
        key: 'progress',
        render: (progress) => (
            <div>
              <div class="flex justify-between text-sm mb-1">
                <span></span>
                <span>{progress}%</span>
              </div>
              <Progress percent={progress} size="small" />
            </div>
        )
      },
      {
        title: '最近学习',
        dataIndex: 'lastStudy',
        key: 'lastStudy',
      },
      {
        title: '操作',
        key: 'action',
        render: () => (
            <Button type="link" size="small">继续学习</Button>
        )
      },
    ]

    // 课程进度数据
    const courseProgressData = [
      {
        key: '1',
        course: 'JavaScript高级编程',
        progress: 65,
        lastStudy: '今天 14:30'
      },
      {
        key: '2',
        course: '数据结构与算法',
        progress: 30,
        lastStudy: '昨天 09:15'
      },
      {
        key: '3',
        course: '线性代数',
        progress: 80,
        lastStudy: '3天前'
      },
      {
        key: '4',
        course: '专业英语',
        progress: 15,
        lastStudy: '1周前'
      }
    ]

    // 图表数据
    const chartData = [
      { week: '第1周', hours: 8.5 },
      { week: '第2周', hours: 12.3 },
      { week: '第3周', hours: 9.8 },
      { week: '第4周', hours: 15.2 },
      { week: '第5周', hours: 7.6 },
      { week: '第6周', hours: 10.5 },
      { week: '第7周', hours: 12.5 },
    ]

    const xAxis = {
      dataKey: 'week'
    }

    const yAxis = {
      title: {
        text: '学习时长（小时）'
      }
    }

    const series = [
      {
        dataKey: 'hours',
        name: '学习时长',
        stroke: '#1890ff',
        dot: { r: 4 },
        activeDot: { r: 6 }
      }
    ]

    return {
      columns,
      courseProgressData,
      chartData,
      xAxis,
      yAxis,
      series
    }
  }
}
</script>

<style scoped>
.learning-progress-page {
  padding-top: 80px;
}

.page-header {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4eaf1 100%);
}
</style>