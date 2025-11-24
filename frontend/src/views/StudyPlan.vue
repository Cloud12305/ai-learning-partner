<template>
  <div class="study-plan-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">学业规划</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto text-lg">制定科学合理的学习计划，助你高效完成学业目标，为未来发展奠定坚实基础</p>
      </div>
    </div>

    <!-- 主要内容 -->
    <main class="container mx-auto px-4 py-12">
      <div class="grid lg:grid-cols-4 gap-8">
        <!-- 左侧：规划工具和统计 -->
        <div class="lg:col-span-1 space-y-6">
          <!-- 快速操作 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <h2 class="text-xl font-semibold mb-6">快速操作</h2>
            <div class="space-y-3">
              <a-button type="primary" block size="large" class="h-12 flex items-center justify-center" @click="showCreatePlanModal">
                <template #icon>
                  <PlusOutlined />
                </template>
                创建新计划
              </a-button>
              <a-button block size="large" class="h-12 flex items-center justify-center" @click="importPlan">
                <template #icon>
                  <ImportOutlined />
                </template>
                导入计划
              </a-button>
              <a-button block size="large" class="h-12 flex items-center justify-center" @click="generatePlan">
                <template #icon>
                  <RocketOutlined />
                </template>
                智能生成
              </a-button>
            </div>
          </div>

          <!-- 计划统计 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <h2 class="text-xl font-semibold mb-6">计划统计</h2>
            <div class="space-y-4">
              <div class="stat-item">
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">进行中</span>
                  <span class="text-lg font-bold text-blue-600">3</span>
                </div>
                <a-progress :percent="60" size="small" />
              </div>
              <div class="stat-item">
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">已完成</span>
                  <span class="text-lg font-bold text-green-600">8</span>
                </div>
                <a-progress :percent="80" size="small" strokeColor="#52c41a" />
              </div>
              <div class="stat-item">
                <div class="flex justify-between items-center">
                  <span class="text-sm text-gray-600">总计划数</span>
                  <span class="text-lg font-bold text-purple-600">12</span>
                </div>
                <a-progress :percent="100" size="small" strokeColor="#722ed1" />
              </div>
            </div>
          </div>

          <!-- 目标设置 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <h2 class="text-xl font-semibold mb-4">学习目标</h2>
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">主要目标</label>
                <a-input placeholder="例如：掌握前端开发技能" v-model:value="goalForm.mainGoal" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">目标描述</label>
                <a-textarea
                    rows="3"
                    placeholder="详细描述你的学习目标和期望成果..."
                    v-model:value="goalForm.description"
                />
              </div>
              <div class="grid grid-cols-2 gap-3">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">开始日期</label>
                  <a-date-picker style="width: 100%" v-model:value="goalForm.startDate" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">结束日期</label>
                  <a-date-picker style="width: 100%" v-model:value="goalForm.endDate" />
                </div>
              </div>
              <a-button type="primary" block @click="saveGoal">保存目标</a-button>
            </div>
          </div>
        </div>

        <!-- 右侧：主要内容 -->
        <div class="lg:col-span-3 space-y-6">
          <!-- 目标概览 -->
          <div class="grid md:grid-cols-3 gap-6 mb-6">
            <div class="bg-gradient-to-r from-blue-500 to-blue-600 rounded-2xl p-6 text-white">
              <div class="flex items-center justify-between">
                <div>
                  <div class="text-2xl font-bold">85%</div>
                  <div class="text-blue-100">整体完成度</div>
                </div>
                <CheckCircleOutlined class="text-3xl opacity-80" />
              </div>
            </div>
            <div class="bg-gradient-to-r from-green-500 to-green-600 rounded-2xl p-6 text-white">
              <div class="flex items-center justify-between">
                <div>
                  <div class="text-2xl font-bold">32</div>
                  <div class="text-green-100">已学课程</div>
                </div>
                <BookOutlined class="text-3xl opacity-80" />
              </div>
            </div>
            <div class="bg-gradient-to-r from-purple-500 to-purple-600 rounded-2xl p-6 text-white">
              <div class="flex items-center justify-between">
                <div>
                  <div class="text-2xl font-bold">186h</div>
                  <div class="text-purple-100">总学习时长</div>
                </div>
                <ClockCircleOutlined class="text-3xl opacity-80" />
              </div>
            </div>
          </div>

          <!-- 时间线规划 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-xl font-semibold">学习时间线</h2>
              <div class="flex space-x-2">
                <a-select defaultValue="all" style="width: 120px">
                  <a-select-option value="all">全部</a-select-option>
                  <a-select-option value="active">进行中</a-select-option>
                  <a-select-option value="completed">已完成</a-select-option>
                </a-select>
                <a-button type="primary" @click="showCreatePlanModal">
                  <template #icon>
                    <PlusOutlined />
                  </template>
                  新建计划
                </a-button>
              </div>
            </div>

            <a-timeline mode="alternate">
              <a-timeline-item color="green">
                <div class="timeline-card">
                  <div class="flex justify-between items-start mb-2">
                    <span class="font-semibold text-lg">短期目标 (1-3个月)</span>
                    <a-tag color="green">进行中</a-tag>
                  </div>
                  <p class="text-gray-600 mb-3">完成当前学期核心课程，平均成绩保持在85分以上</p>
                  <div class="flex justify-between text-sm text-gray-500">
                    <span>📚 5门课程</span>
                    <span>⏱️ 预计120小时</span>
                    <span>🎯 进度 75%</span>
                  </div>
                </div>
              </a-timeline-item>
              <a-timeline-item color="blue">
                <div class="timeline-card">
                  <div class="flex justify-between items-start mb-2">
                    <span class="font-semibold text-lg">中期目标 (6-12个月)</span>
                    <a-tag color="blue">规划中</a-tag>
                  </div>
                  <p class="text-gray-600 mb-3">掌握专业核心技能，完成2个相关实践项目，参与开源贡献</p>
                  <div class="flex justify-between text-sm text-gray-500">
                    <span>📚 8门课程</span>
                    <span>⏱️ 预计300小时</span>
                    <span>🎯 进度 25%</span>
                  </div>
                </div>
              </a-timeline-item>
              <a-timeline-item color="purple">
                <div class="timeline-card">
                  <div class="flex justify-between items-start mb-2">
                    <span class="font-semibold text-lg">长期目标 (1-3年)</span>
                    <a-tag color="purple">未来规划</a-tag>
                  </div>
                  <p class="text-gray-600 mb-3">获得专业领域认证，发表1-2篇学术论文，参加行业竞赛并获得名次</p>
                  <div class="flex justify-between text-sm text-gray-500">
                    <span>📚 12+课程</span>
                    <span>⏱️ 预计600小时</span>
                    <span>🎯 进度 10%</span>
                  </div>
                </div>
              </a-timeline-item>
            </a-timeline>
          </div>

          <!-- 课程规划表 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <div class="flex items-center justify-between mb-6">
              <h2 class="text-xl font-semibold">详细课程规划</h2>
              <div class="flex space-x-2">
                <a-input-search placeholder="搜索课程..." style="width: 200px" />
                <a-button type="primary" @click="showCreatePlanModal">
                  <template #icon>
                    <PlusOutlined />
                  </template>
                  添加课程
                </a-button>
              </div>
            </div>

            <a-table
                :columns="columns"
                :data-source="coursePlanData"
                :pagination="false"
                class="plan-table"
            >
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'semester'">
                  <div class="flex items-center">
                    <div class="w-3 h-3 rounded-full mr-3" :class="getSemesterColor(record.semester)"></div>
                    <span class="font-medium">{{ record.semester }}</span>
                  </div>
                </template>
                <template v-else-if="column.key === 'target'">
                  <div class="text-center">
                    <div class="text-lg font-bold" :class="getTargetColor(record.target)">{{ record.target }}</div>
                    <div class="text-xs text-gray-500">目标成绩</div>
                  </div>
                </template>
                <template v-else-if="column.key === 'priority'">
                  <a-tag :color="getPriorityColor(record.priority)">
                    {{ record.priority }}
                  </a-tag>
                </template>
                <template v-else-if="column.key === 'progress'">
                  <div>
                    <a-progress :percent="record.progress" size="small" :strokeColor="getProgressColor(record.progress)" />
                    <div class="flex justify-between text-xs text-gray-500 mt-1">
                      <span>已学 {{ record.completedHours }}h</span>
                      <span>剩余 {{ record.remainingHours }}h</span>
                    </div>
                  </div>
                </template>
                <template v-else-if="column.key === 'action'">
                  <div class="flex space-x-2">
                    <a-button type="link" size="small" @click="editPlan(record)">编辑</a-button>
                    <a-button type="link" size="small" danger @click="deletePlan(record)">删除</a-button>
                  </div>
                </template>
              </template>
            </a-table>
          </div>

          <!-- 学习建议 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-100">
            <h2 class="text-xl font-semibold mb-4">个性化学习建议</h2>
            <div class="grid md:grid-cols-2 gap-4">
              <div class="suggestion-card">
                <div class="flex items-start">
                  <BulbOutlined class="text-yellow-500 text-xl mr-3 mt-1" />
                  <div>
                    <h4 class="font-medium mb-1">专注核心课程</h4>
                    <p class="text-sm text-gray-600">建议优先完成数据结构与算法课程，这是面试和工作的核心技能</p>
                  </div>
                </div>
              </div>
              <div class="suggestion-card">
                <div class="flex items-start">
                  <TeamOutlined class="text-blue-500 text-xl mr-3 mt-1" />
                  <div>
                    <h4 class="font-medium mb-1">参与实践项目</h4>
                    <p class="text-sm text-gray-600">建议在完成理论学习后，参与1-2个实际项目来巩固知识</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 创建计划模态框 -->
    <a-modal
        v-model:visible="createPlanModalVisible"
        title="创建学习计划"
        width="600px"
        @ok="handleCreatePlan"
    >
      <!-- 模态框内容 -->
      <a-form layout="vertical">
        <a-form-item label="计划名称">
          <a-input v-model:value="planForm.name" placeholder="输入计划名称" />
        </a-form-item>
        <a-form-item label="计划描述">
          <a-textarea v-model:value="planForm.description" rows="3" placeholder="详细描述学习计划" />
        </a-form-item>
        <div class="grid grid-cols-2 gap-4">
          <a-form-item label="开始日期">
            <a-date-picker v-model:value="planForm.startDate" style="width: 100%" />
          </a-form-item>
          <a-form-item label="结束日期">
            <a-date-picker v-model:value="planForm.endDate" style="width: 100%" />
          </a-form-item>
        </div>
        <a-form-item label="优先级">
          <a-select v-model:value="planForm.priority" placeholder="选择优先级">
            <a-select-option value="high">高</a-select-option>
            <a-select-option value="medium">中</a-select-option>
            <a-select-option value="low">低</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import {
  Table,
  Button,
  Input,
  Select,
  Tag,
  Progress,
  Timeline,
  Modal,
  Form,
  DatePicker,
  message
} from 'ant-design-vue'
import {
  PlusOutlined,
  ImportOutlined,
  RocketOutlined,
  CheckCircleOutlined,
  BookOutlined,
  ClockCircleOutlined,
  BulbOutlined,
  TeamOutlined
} from '@ant-design/icons-vue'

export default {
  components: {
    ATable: Table,
    AButton: Button,
    AInput: Input,
    AInputSearch: Input.Search,
    ATextarea: Input.TextArea,
    ASelect: Select,
    ASelectOption: Select.Option,
    ATag: Tag,
    AProgress: Progress,
    ATimeline: Timeline,
    ATimelineItem: Timeline.Item,
    AModal: Modal,
    AForm: Form,
    AFormItem: Form.Item,
    ADatePicker: DatePicker,
    PlusOutlined,
    ImportOutlined,
    RocketOutlined,
    CheckCircleOutlined,
    BookOutlined,
    ClockCircleOutlined,
    BulbOutlined,
    TeamOutlined
  },
  data() {
    return {
      createPlanModalVisible: false,
      goalForm: {
        mainGoal: '',
        description: '',
        startDate: null,
        endDate: null
      },
      planForm: {
        name: '',
        description: '',
        startDate: null,
        endDate: null,
        priority: 'medium'
      },
      columns: [
        {
          title: '学期',
          dataIndex: 'semester',
          key: 'semester',
          width: '15%'
        },
        {
          title: '课程名称',
          dataIndex: 'course',
          key: 'course',
          width: '25%'
        },
        {
          title: '目标成绩',
          dataIndex: 'target',
          key: 'target',
          width: '15%'
        },
        {
          title: '优先级',
          dataIndex: 'priority',
          key: 'priority',
          width: '15%'
        },
        {
          title: '学习进度',
          dataIndex: 'progress',
          key: 'progress',
          width: '20%'
        },
        {
          title: '操作',
          key: 'action',
          width: '10%'
        }
      ],
      coursePlanData: [
        {
          key: '1',
          semester: '大一上',
          course: '高等数学',
          target: '90+',
          priority: '高',
          progress: 85,
          completedHours: 34,
          remainingHours: 6
        },
        {
          key: '2',
          semester: '大一上',
          course: '计算机基础',
          target: '85+',
          priority: '高',
          progress: 90,
          completedHours: 45,
          remainingHours: 5
        },
        {
          key: '3',
          semester: '大一下',
          course: '线性代数',
          target: '85+',
          priority: '中',
          progress: 60,
          completedHours: 24,
          remainingHours: 16
        },
        {
          key: '4',
          semester: '大一下',
          course: '程序设计入门',
          target: '90+',
          priority: '高',
          progress: 75,
          completedHours: 30,
          remainingHours: 10
        },
        {
          key: '5',
          semester: '大二上',
          course: '数据结构与算法',
          target: '88+',
          priority: '高',
          progress: 45,
          completedHours: 27,
          remainingHours: 33
        }
      ]
    }
  },
  methods: {
    showCreatePlanModal() {
      this.createPlanModalVisible = true
    },
    handleCreatePlan() {
      message.success('学习计划创建成功！')
      this.createPlanModalVisible = false
      // 重置表单
      this.planForm = {
        name: '',
        description: '',
        startDate: null,
        endDate: null,
        priority: 'medium'
      }
    },
    importPlan() {
      message.info('导入计划功能开发中')
    },
    generatePlan() {
      message.success('正在智能生成学习计划...')
    },
    saveGoal() {
      if (this.goalForm.mainGoal) {
        message.success('学习目标保存成功！')
      } else {
        message.warning('请填写主要目标')
      }
    },
    editPlan(record) {
      message.info(`编辑计划: ${record.course}`)
    },
    deletePlan(record) {
      Modal.confirm({
        title: '确认删除',
        content: `确定要删除 "${record.course}" 的学习计划吗？`,
        onOk: () => {
          message.success('计划删除成功')
        }
      })
    },
    getSemesterColor(semester) {
      const colors = {
        '大一上': 'bg-blue-500',
        '大一下': 'bg-green-500',
        '大二上': 'bg-purple-500',
        '大二下': 'bg-orange-500'
      }
      return colors[semester] || 'bg-gray-500'
    },
    getTargetColor(target) {
      if (target.includes('90')) return 'text-green-600'
      if (target.includes('85')) return 'text-blue-600'
      return 'text-orange-600'
    },
    getPriorityColor(priority) {
      const colors = {
        '高': 'red',
        '中': 'orange',
        '低': 'green'
      }
      return colors[priority] || 'default'
    },
    getProgressColor(progress) {
      if (progress >= 80) return '#52c41a'
      if (progress >= 60) return '#1890ff'
      if (progress >= 40) return '#faad14'
      return '#ff4d4f'
    }
  }
}
</script>

<style scoped>
.study-plan-page {
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

.stat-item {
  @apply p-3 rounded-lg border border-gray-100 hover:border-blue-200 transition-colors duration-200;
}

.timeline-card {
  @apply p-4 rounded-lg bg-gray-50 hover:bg-blue-50 transition-colors duration-200 border border-gray-200;
}

.suggestion-card {
  @apply p-4 rounded-lg border border-gray-200 hover:border-blue-300 transition-colors duration-200;
}

.plan-table :deep(.ant-table-thead > tr > th) {
  background: #fafafa;
  border-bottom: 2px solid #f0f0f0;
  font-weight: 600;
}

.plan-table :deep(.ant-table-tbody > tr:hover > td) {
  background: #f0f9ff;
}

:deep(.ant-timeline-item-head) {
  width: 16px;
  height: 16px;
}

:deep(.ant-timeline-item-tail) {
  left: 7px;
}
</style>