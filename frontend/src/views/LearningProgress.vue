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
      <div class="grid lg:grid-cols-3 gap-8">
        <!-- 左侧：进度概览 -->
        <div class="lg:col-span-1 space-y-6">
          <!-- 总体进度 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-xl font-semibold">总体进度</h2>
              <a-tag color="blue">本学期</a-tag>
            </div>
            <div class="flex flex-col items-center">
              <div class="relative mb-6">
                <div class="w-40 h-40 rounded-full border-8 border-gray-200 flex items-center justify-center"
                     :style="{ background: `conic-gradient(#1890ff ${68 * 3.6}deg, #e6f7ff 0deg)` }">
                  <div class="w-32 h-32 bg-white rounded-full flex items-center justify-center">
                    <div class="text-center">
                      <span class="text-3xl font-bold text-gray-800">{{ 68 }}%</span>
                      <div class="text-sm text-gray-500 mt-1">已完成</div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="w-full space-y-3">
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">目标完成度</span>
                  <span class="text-sm font-medium">86%</span>
                </div>
                <a-progress :percent="86" size="small" />

                <div class="flex justify-between items-center mt-4">
                  <span class="text-sm text-gray-600">学习效率</span>
                  <span class="text-sm font-medium">92%</span>
                </div>
                <a-progress :percent="92" size="small" strokeColor="#52c41a" />
              </div>
            </div>
          </div>

          <!-- 学习时间统计 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <h2 class="text-xl font-semibold mb-6">学习时间分析</h2>
            <div class="space-y-5">
              <div class="time-stat-item">
                <div class="flex justify-between items-center mb-2">
                  <div class="flex items-center">
                    <div class="w-3 h-3 bg-blue-500 rounded-full mr-3"></div>
                    <span class="text-sm font-medium">本周学习</span>
                  </div>
                  <span class="text-sm font-semibold">12.5小时</span>
                </div>
                <a-progress :percent="75" size="small" />
                <div class="flex justify-between text-xs text-gray-500 mt-1">
                  <span>日均 1.8小时</span>
                  <span>环比 +12%</span>
                </div>
              </div>

              <div class="time-stat-item">
                <div class="flex justify-between items-center mb-2">
                  <div class="flex items-center">
                    <div class="w-3 h-3 bg-green-500 rounded-full mr-3"></div>
                    <span class="text-sm font-medium">本月学习</span>
                  </div>
                  <span class="text-sm font-semibold">48.2小时</span>
                </div>
                <a-progress :percent="62" size="small" strokeColor="#52c41a" />
                <div class="flex justify-between text-xs text-gray-500 mt-1">
                  <span>日均 1.6小时</span>
                  <span>环比 +8%</span>
                </div>
              </div>

              <div class="time-stat-item">
                <div class="flex justify-between items-center mb-2">
                  <div class="flex items-center">
                    <div class="w-3 h-3 bg-purple-500 rounded-full mr-3"></div>
                    <span class="text-sm font-medium">本学期学习</span>
                  </div>
                  <span class="text-sm font-semibold">186.7小时</span>
                </div>
                <a-progress :percent="68" size="small" strokeColor="#722ed1" />
                <div class="flex justify-between text-xs text-gray-500 mt-1">
                  <span>日均 1.5小时</span>
                  <span>目标 275小时</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 学习习惯 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <h2 class="text-xl font-semibold mb-6">学习习惯</h2>
            <div class="space-y-4">
              <div class="habit-item">
                <div class="flex justify-between mb-1">
                  <span class="text-sm">早晨学习 (6-12点)</span>
                  <span class="text-sm font-medium">35%</span>
                </div>
                <a-progress :percent="35" size="small" strokeColor="#faad14" />
              </div>
              <div class="habit-item">
                <div class="flex justify-between mb-1">
                  <span class="text-sm">下午学习 (12-18点)</span>
                  <span class="text-sm font-medium">45%</span>
                </div>
                <a-progress :percent="45" size="small" strokeColor="#1890ff" />
              </div>
              <div class="habit-item">
                <div class="flex justify-between mb-1">
                  <span class="text-sm">晚上学习 (18-24点)</span>
                  <span class="text-sm font-medium">20%</span>
                </div>
                <a-progress :percent="20" size="small" strokeColor="#722ed1" />
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：详细进度 -->
        <div class="lg:col-span-2 space-y-6">
          <!-- 课程进度 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-xl font-semibold">课程进度</h2>
              <div class="flex space-x-2">
                <a-input-search placeholder="搜索课程..." style="width: 200px" />
                <a-button type="primary">
                  <template #icon>
                    <PlusOutlined />
                  </template>
                  添加课程
                </a-button>
              </div>
            </div>
            <a-table
                :columns="columns"
                :data-source="courseProgressData"
                :pagination="false"
                class="progress-table"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'progress'">
                  <div class="progress-cell">
                    <div class="flex justify-between text-sm mb-2">
                      <span class="font-medium">{{ record.course }}</span>
                      <span class="text-gray-600">{{ record.progress }}%</span>
                    </div>
                    <a-progress
                        :percent="record.progress"
                        size="small"
                        :strokeColor="getProgressColor(record.progress)"
                    />
                    <div class="flex justify-between text-xs text-gray-500 mt-1">
                      <span>已学习 {{ record.studyTime }}</span>
                      <span>剩余 {{ record.remainingTime }}</span>
                    </div>
                  </div>
                </template>
                <template v-else-if="column.key === 'lastStudy'">
                  <div class="last-study">
                    <div class="text-sm font-medium">{{ record.lastStudy }}</div>
                    <div class="text-xs text-gray-500">{{ record.lastStudyTime }}</div>
                  </div>
                </template>
                <template v-else-if="column.key === 'action'">
                  <div class="flex space-x-2">
                    <a-button
                        type="primary"
                        size="small"
                        class="action-btn"
                        @click="continueStudy(record)"
                    >
                      继续学习
                    </a-button>
                    <a-button
                        size="small"
                        class="action-btn"
                        @click="showDetail(record)"
                    >
                      详情
                    </a-button>
                  </div>
                </template>
              </template>
            </a-table>
          </div>

          <!-- 图表区域 -->
          <div class="grid md:grid-cols-2 gap-6">
            <!-- 每周学习趋势 -->
            <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
              <div class="flex items-center justify-between mb-6">
                <h2 class="text-xl font-semibold">每周学习趋势</h2>
                <a-select defaultValue="weekly" style="width: 120px" @change="handleTimeRangeChange">
                  <a-select-option value="weekly">本周</a-select-option>
                  <a-select-option value="monthly">本月</a-select-option>
                  <a-select-option value="yearly">本学期</a-select-option>
                </a-select>
              </div>
              <div class="h-64">
                <!-- 柱状图 -->
                <div class="h-full flex flex-col">
                  <div class="flex-1 flex items-end justify-between space-x-2 pb-8">
                    <div v-for="(item, index) in chartData" :key="index"
                         class="flex flex-col items-center flex-1 group relative">
                      <!-- 柱状条 -->
                      <div
                          class="w-3/4 bg-gradient-to-t from-blue-500 to-blue-400 rounded-t transition-all duration-500 hover:from-blue-600 hover:to-blue-500 cursor-pointer"
                          :style="{ height: `${(item.hours / maxHours) * 80}%`, minHeight: '20px' }"
                          @mouseenter="activeIndex = index"
                          @mouseleave="activeIndex = -1"
                      >
                        <!-- 悬停提示 -->
                        <div v-if="activeIndex === index"
                             class="absolute -top-8 left-1/2 transform -translate-x-1/2 bg-gray-800 text-white text-xs px-2 py-1 rounded whitespace-nowrap z-10">
                          {{ item.week }}: {{ item.hours }}小时
                        </div>
                      </div>
                      <!-- 周标签 -->
                      <div class="text-xs text-gray-500 mt-2 text-center leading-tight">{{ item.week }}</div>
                      <!-- 数值标签 -->
                      <div class="text-xs font-medium mt-1">{{ item.hours }}h</div>
                    </div>
                  </div>
                  <!-- Y轴标签 -->
                  <div class="flex justify-between text-xs text-gray-500 px-2 border-t pt-2">
                    <span>0h</span>
                    <span>{{ Math.round(maxHours / 2) }}h</span>
                    <span>{{ maxHours }}h</span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 学习分布 -->
            <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
              <h2 class="text-xl font-semibold mb-6">学习分布</h2>
              <div class="h-64 flex">
                <!-- 左侧饼图 -->
                <div class="flex-1 flex items-center justify-center">
                  <div class="relative w-32 h-32">
                    <!-- 饼图使用多个扇形div拼接 -->
                    <div class="absolute inset-0">
                      <div class="absolute inset-0 rounded-full border-8 border-transparent"
                           :style="{
                             background: `conic-gradient(
                               #1890ff 0% ${pieData[0].value}%,
                               #52c41a 0% ${pieData[0].value + pieData[1].value}%,
                               #faad14 0% ${pieData[0].value + pieData[1].value + pieData[2].value}%,
                               #722ed1 0% 100%
                             )`
                           }">
                      </div>
                      <div class="absolute inset-4 bg-white rounded-full"></div>
                      <div class="absolute inset-0 flex items-center justify-center">
                        <div class="text-center">
                          <div class="text-sm font-bold">总计</div>
                          <div class="text-xs text-gray-500">100%</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 右侧图例 -->
                <div class="flex-1 flex flex-col justify-center space-y-3">
                  <div v-for="(item, index) in pieData" :key="index" class="flex items-center">
                    <div class="w-4 h-4 rounded mr-3" :style="{ backgroundColor: getPieColor(index) }"></div>
                    <div class="flex-1">
                      <div class="text-sm font-medium">{{ item.type }}</div>
                      <div class="text-xs text-gray-500">{{ item.value }}%</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 最近活动 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <h2 class="text-xl font-semibold mb-6">最近学习活动</h2>
            <a-timeline>
              <a-timeline-item color="green">
                <div class="timeline-item">
                  <div class="timeline-content">
                    <div class="flex justify-between">
                      <span class="font-medium">完成了 JavaScript 高级编程第5章</span>
                      <span class="text-sm text-gray-500">今天 14:30</span>
                    </div>
                    <p class="text-sm text-gray-600 mt-1">学习了闭包和高阶函数的概念，完成了3个练习</p>
                  </div>
                </div>
              </a-timeline-item>
              <a-timeline-item color="blue">
                <div class="timeline-item">
                  <div class="timeline-content">
                    <div class="flex justify-between">
                      <span class="font-medium">数据结构与算法 - 二叉树</span>
                      <span class="text-sm text-gray-500">昨天 09:15</span>
                    </div>
                    <p class="text-sm text-gray-600 mt-1">掌握了二叉树的前序、中序、后序遍历算法</p>
                  </div>
                </div>
              </a-timeline-item>
              <a-timeline-item color="purple">
                <div class="timeline-item">
                  <div class="timeline-content">
                    <div class="flex justify-between">
                      <span class="font-medium">线性代数测验</span>
                      <span class="text-sm text-gray-500">3天前</span>
                    </div>
                    <p class="text-sm text-gray-600 mt-1">矩阵运算测验得分 92/100</p>
                  </div>
                </div>
              </a-timeline-item>
            </a-timeline>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import {
  Table,
  Progress,
  Tag,
  Button,
  Input,
  Select,
  Timeline,
  message
} from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'

export default {
  components: {
    ATable: Table,
    AProgress: Progress,
    ATag: Tag,
    AButton: Button,
    AInputSearch: Input.Search,
    ASelect: Select,
    ASelectOption: Select.Option,
    ATimeline: Timeline,
    ATimelineItem: Timeline.Item,
    PlusOutlined
  },
  data() {
    return {
      activeIndex: -1,
      columns: [
        {
          title: '课程进度',
          key: 'progress',
          width: '40%'
        },
        {
          title: '最近学习',
          key: 'lastStudy',
          width: '25%'
        },
        {
          title: '操作',
          key: 'action',
          width: '35%'
        },
      ],
      courseProgressData: [
        {
          key: '1',
          course: '计算思维与人工智能-问题',
          progress: 65,
          studyTime: '32小时',
          remainingTime: '18小时',
          lastStudy: '今天 14:30',
          lastStudyTime: '学习了2.5小时',
          // 中国大学MOOC视频链接
          videoUrl: 'https://www.icourse163.org/learn/ZJU-1472836187?tid=1475579485#/learn/content?type=detail&id=1264999031&cid=1304753985',
          detailUrl: 'https://www.icourse163.org/course/ZJU-1472836187'
        },
        {
          key: '2',
          course: '数据结构',
          progress: 30,
          studyTime: '18小时',
          remainingTime: '42小时',
          lastStudy: '昨天 09:15',
          lastStudyTime: '学习了1.8小时',
          videoUrl: 'https://www.icourse163.org/learn/NJUPT-1206361803?tid=1475568512#/learn/content?type=detail&id=1265003884&cid=1304897163',
          detailUrl: 'https://www.icourse163.org/learn/NJUPT-1206361803'
        },
        {
          key: '3',
          course: '线性代数',
          progress: 80,
          studyTime: '45小时',
          remainingTime: '12小时',
          lastStudy: '3天前',
          lastStudyTime: '完成了测验',
          videoUrl: 'https://www.icourse163.org/learn/SJTU-1003639009?tid=1475428456#/learn/content?type=detail&id=1264631210&cid=1303997949',
          detailUrl: 'https://www.icourse163.org/learn/SJTU-1003639009'
        },
        {
          key: '4',
          course: '高级英语',
          progress: 15,
          studyTime: '8小时',
          remainingTime: '45小时',
          lastStudy: '1周前',
          lastStudyTime: '学习了1.2小时',
          videoUrl: 'https://www.icourse163.org/learn/JIANGNAN-1207256803?tid=1475440455#/learn/content?type=detail&id=1264566936&cid=1303978693',
          detailUrl: 'https://www.icourse163.org/learn/JIANGNAN-1207256803?'
        }
      ],
      chartData: [
        { week: '第1周', hours: 8.5 },
        { week: '第2周', hours: 12.3 },
        { week: '第3周', hours: 9.8 },
        { week: '第4周', hours: 15.2 },
        { week: '第5周', hours: 7.6 },
        { week: '第6周', hours: 10.5 },
        { week: '第7周', hours: 12.5 },
      ],
      pieData: [
        { type: '编程课程', value: 45 },
        { type: '数学课程', value: 30 },
        { type: '英语课程', value: 15 },
        { type: '其他课程', value: 10 }
      ]
    }
  },
  computed: {
    maxHours() {
      return Math.max(...this.chartData.map(item => item.hours))
    }
  },
  methods: {
    // 继续学习 - 跳转到中国大学MOOC视频播放页面
    continueStudy(record) {
      message.loading('正在跳转到中国大学MOOC...', 1)
          .then(() => {
            // 在新标签页打开中国大学MOOC视频链接
            window.open(record.videoUrl, '_blank')
            message.success(`开始学习 ${record.course}`)
          })
    },

    // 查看详情 - 跳转到中国大学MOOC课程详情页面
    showDetail(record) {
      message.loading('正在跳转到课程详情...', 1)
          .then(() => {
            // 在新标签页打开中国大学MOOC课程详情页面
            window.open(record.detailUrl, '_blank')
            message.info(`查看 ${record.course} 详情`)
          })
    },

    getProgressColor(progress) {
      if (progress >= 80) return '#52c41a'
      if (progress >= 60) return '#1890ff'
      if (progress >= 40) return '#faad14'
      return '#ff4d4f'
    },
    getPieColor(index) {
      const colors = ['#1890ff', '#52c41a', '#faad14', '#722ed1']
      return colors[index] || '#666'
    },
    handleTimeRangeChange(value) {
      message.info(`切换时间范围: ${value}`)
    }
  }
}
</script>

<style scoped>
.learning-progress-page {
  padding-top: 80px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf5 100%);
  min-height: 100vh;
}

.page-header {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
}

.page-header h1,
.page-header p {
  color: #000000;
}

.progress-table :deep(.ant-table-thead > tr > th) {
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.progress-cell {
  @apply p-2;
}

.last-study {
  @apply p-2;
}

.action-btn {
  @apply transition-all duration-200;
}

.action-btn:hover {
  transform: translateY(-1px);
}

.time-stat-item,
.habit-item {
  @apply p-3 rounded-lg border border-gray-100 hover:border-blue-200 transition-colors duration-200;
}

.timeline-item {
  @apply p-2;
}

.timeline-content {
  @apply p-3 rounded-lg bg-gray-50 hover:bg-blue-50 transition-colors duration-200;
}

:deep(.ant-progress-text) {
  font-size: 12px;
}

:deep(.ant-timeline-item-head) {
  width: 16px;
  height: 16px;
}

:deep(.ant-timeline-item-tail) {
  left: 7px;
}

/* 柱状图悬停效果 */
.chart-bar {
  transition: all 0.3s ease;
}
.chart-bar:hover {
  transform: scale(1.05);
}
</style>