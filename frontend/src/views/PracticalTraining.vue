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
              v-model="filterType"
              placeholder="选择项目类型"
              style="width: 180px"
              @change="loadProjects"
          >
            <Select.Option value="">全部类型</Select.Option>
            <Select.Option value="FRONTEND">前端开发</Select.Option>
            <Select.Option value="BACKEND">后端开发</Select.Option>
            <Select.Option value="FULLSTACK">全栈开发</Select.Option>
            <Select.Option value="MOBILE">移动开发</Select.Option>
            <Select.Option value="DATABASE">数据库</Select.Option>
            <Select.Option value="DEVOPS">运维开发</Select.Option>
          </Select>

          <Select
              v-model="filterDifficulty"
              placeholder="难度级别"
              style="width: 150px"
              @change="loadProjects"
          >
            <Select.Option value="">全部难度</Select.Option>
            <Select.Option value="BEGINNER">入门</Select.Option>
            <Select.Option value="INTERMEDIATE">中级</Select.Option>
            <Select.Option value="ADVANCED">高级</Select.Option>
          </Select>

          <Select
              v-model="filterDays"
              placeholder="完成时间"
              style="width: 180px"
              @change="loadProjects"
          >
            <Select.Option value="">全部时间</Select.Option>
            <Select.Option value="3">3天内</Select.Option>
            <Select.Option value="7">1周内</Select.Option>
            <Select.Option value="14">2周内</Select.Option>
            <Select.Option value="30">1个月内</Select.Option>
          </Select>

          <Button type="primary" @click="loadRecommendedProjects">推荐项目</Button>
          <Button @click="resetFilters">重置筛选</Button>
        </div>
      </div>

      <!-- 项目列表 -->
      <div class="mb-12">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-2xl font-semibold">实战项目</h2>
          <span class="text-gray-500">共 {{ projects.length }} 个项目</span>
        </div>

        <div v-if="loading" class="text-center py-8">
          <a-spin size="large" />
          <p class="mt-4 text-gray-500">加载中...</p>
        </div>

        <div v-else-if="projects.length === 0" class="text-center py-12">
          <div class="text-gray-400 text-6xl mb-4">📁</div>
          <p class="text-gray-500 text-lg">暂无项目数据</p>
          <Button type="primary" class="mt-4" @click="loadDefaultProjects">加载示例项目</Button>
        </div>

        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
              v-for="project in projects"
              :key="project.id"
              class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300 border border-gray-100"
          >
            <div class="relative">
              <img
                  :src="project.coverImage || getDefaultImage(project.type)"
                  :alt="project.title"
                  class="w-full h-48 object-cover"
                  @error="handleImageError"
              >
              <div v-if="project.currentLearners > 100" class="absolute top-3 left-3 bg-red-500 text-white text-sm px-2 py-1 rounded-full">
                热门
              </div>
              <div class="absolute top-3 right-3 bg-gray-800/80 text-white text-sm px-2 py-1 rounded-full">
                {{ getDifficultyText(project.difficulty) }}
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold text-gray-800">{{ project.title }}</h3>
                <span class="text-sm text-blue-600 bg-blue-50 px-2 py-1 rounded">
                  {{ getTypeText(project.type) }}
                </span>
              </div>
              <p class="text-gray-600 text-sm mb-4 line-clamp-2 leading-relaxed">
                {{ project.description || '暂无项目描述' }}
              </p>
              <div class="flex justify-between items-center mb-4">
                <div class="flex items-center text-sm text-gray-500">
                  <span class="w-2 h-2 bg-green-500 rounded-full mr-2"></span>
                  预计{{ project.estimatedDays || 7 }}天
                </div>
                <div class="flex items-center text-sm text-gray-500">
                  <span class="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
                  {{ project.currentLearners || 0 }}人学习
                </div>
              </div>
              <Button
                  type="primary"
                  block
                  class="h-10 font-medium"
                  @click="viewProjectDetail(project)"
              >
                开始练习
              </Button>
            </div>
          </div>
        </div>
      </div>

      <!-- 项目详情模态框 -->
      <a-modal
          v-model:visible="projectDetailVisible"
          :title="selectedProject?.title"
          width="90%"
          style="max-width: 1200px"
          :footer="null"
          @cancel="closeProjectDetail"
      >
        <div v-if="selectedProject" class="project-detail">
          <!-- 项目基本信息 -->
          <div class="bg-gray-50 p-6 rounded-lg mb-6">
            <div class="flex flex-col md:flex-row gap-6">
              <div class="md:w-1/3">
                <img
                    :src="selectedProject.coverImage || getDefaultImage(selectedProject.type)"
                    :alt="selectedProject.title"
                    class="w-full h-64 object-cover rounded-lg"
                >
              </div>
              <div class="md:w-2/3">
                <div class="flex items-center gap-2 mb-4">
                  <Badge class="bg-blue-100 text-blue-800 border-0">{{ getTypeText(selectedProject.type) }}</Badge>
                  <Badge :class="`${getDifficultyColor(selectedProject.difficulty)} border-0`">
                    {{ getDifficultyText(selectedProject.difficulty) }}
                  </Badge>
                </div>
                <p class="text-gray-600 mb-4">{{ selectedProject.description }}</p>
                <div class="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <span class="text-gray-500">预计时间：</span>
                    <span>{{ selectedProject.estimatedDays }}天</span>
                  </div>
                  <div>
                    <span class="text-gray-500">学习人数：</span>
                    <span>{{ selectedProject.currentLearners }}人</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 标签页内容 -->
          <a-tabs v-model:activeKey="activeTab">
            <!-- 项目详细介绍 -->
            <a-tab-pane key="detail" tab="项目介绍">
              <div class="prose max-w-none">
                <h3>项目概述</h3>
                <div v-html="selectedProject.detailedContent || '暂无详细介绍'"></div>
              </div>
            </a-tab-pane>

            <!-- 详细教程 -->
            <a-tab-pane key="tutorial" tab="详细教程">
              <div class="prose max-w-none">
                <h3>学习教程</h3>
                <div v-html="selectedProject.tutorial || '暂无教程内容'"></div>
              </div>
            </a-tab-pane>

            <!-- 教学视频 -->
            <a-tab-pane key="videos" tab="教学视频">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div
                    v-for="(video, index) in selectedProject.teachingVideos || []"
                    :key="index"
                    class="border rounded-lg overflow-hidden"
                >
                  <div class="aspect-w-16 aspect-h-9 bg-gray-200 flex items-center justify-center">
                    <div class="text-center">
                      <div class="text-4xl mb-2">🎬</div>
                      <p class="text-gray-600">{{ video.name || `教学视频 ${index + 1}` }}</p>
                    </div>
                  </div>
                  <div class="p-4">
                    <h4 class="font-semibold mb-2">{{ video.name || `教学视频 ${index + 1}` }}</h4>
                    <p class="text-gray-600 text-sm mb-3">{{ video.description || '暂无描述' }}</p>
                    <Button type="primary" @click="playVideo(video)">观看视频</Button>
                  </div>
                </div>
                <div v-if="!selectedProject.teachingVideos || selectedProject.teachingVideos.length === 0" class="col-span-2 text-center py-8">
                  <div class="text-gray-400 text-4xl mb-2">📹</div>
                  <p class="text-gray-500">暂无教学视频</p>
                </div>
              </div>
            </a-tab-pane>

            <!-- 优秀项目展示 -->
            <a-tab-pane key="projects" tab="优秀项目">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div
                    v-for="(project, index) in selectedProject.excellentProjects || []"
                    :key="index"
                    class="border rounded-lg p-4 hover:shadow-md transition-shadow"
                >
                  <h4 class="font-semibold mb-2">{{ project.name }}</h4>
                  <p class="text-gray-600 text-sm mb-3">{{ project.description || '暂无描述' }}</p>
                  <div class="flex gap-2">
                    <Button type="primary" size="small" @click="viewProject(project)">查看项目</Button>
                    <Button size="small" @click="openProjectLink(project)">源码链接</Button>
                  </div>
                </div>
                <div v-if="!selectedProject.excellentProjects || selectedProject.excellentProjects.length === 0" class="col-span-2 text-center py-8">
                  <div class="text-gray-400 text-4xl mb-2">⭐</div>
                  <p class="text-gray-500">暂无优秀项目展示</p>
                </div>
              </div>
            </a-tab-pane>
          </a-tabs>
        </div>
      </a-modal>

      <!-- 视频播放模态框 -->
      <a-modal
          v-model:visible="videoModalVisible"
          :title="currentVideo?.name"
          width="70%"
          :footer="null"
          @cancel="closeVideo"
      >
        <div v-if="currentVideo" class="text-center">
          <div class="aspect-w-16 aspect-h-9 bg-black rounded-lg mb-4 flex items-center justify-center">
            <div class="text-white text-lg">
              视频播放器 - {{ currentVideo.name }}
            </div>
          </div>
          <p class="text-gray-600">{{ currentVideo.description }}</p>
        </div>
      </a-modal>
    </main>
  </div>
</template>

<script>
import { Select, Button, Badge, Progress, Spin, message, Modal, Tabs } from 'ant-design-vue'

export default {
  components: {
    Select,
    Button,
    Badge,
    Progress,
    ASpin: Spin,
    AModal: Modal,
    ATabs: Tabs,
    ATabPane: Tabs.TabPane
  },
  data() {
    return {
      loading: false,
      projects: [],
      currentProject: null,
      projectProgress: {
        completedModules: 6,
        totalModules: 10,
        progressPercentage: 60,
        startDate: '2023-05-10',
        expectedCompletionDate: '2023-06-20',
        submissionCount: 28
      },
      filterType: '',
      filterDifficulty: '',
      filterDays: '',
      // 项目详情相关
      projectDetailVisible: false,
      selectedProject: null,
      activeTab: 'detail',
      // 视频相关
      videoModalVisible: false,
      currentVideo: null
    }
  },
  mounted() {
    this.loadProjects()
    this.loadCurrentProject()
  },
  methods: {
    async loadProjects() {
      this.loading = true
      try {
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 500))
        this.loadDefaultProjects()
      } catch (error) {
        console.error('API调用失败，使用默认数据:', error)
        this.loadDefaultProjects()
      } finally {
        this.loading = false
      }
    },

    loadDefaultProjects() {
      // 默认项目数据 - 添加更多详细数据
      this.projects = [
        {
          id: 1,
          title: '电商网站前端实现',
          description: '实现一个完整的电商网站前端，包括首页、商品列表、详情页、购物车和结算功能',
          type: 'FRONTEND',
          difficulty: 'INTERMEDIATE',
          estimatedDays: 7,
          currentLearners: 128,
          coverImage: 'https://picsum.photos/800/400?random=1',
          detailedContent: `
            <h3>项目目标</h3>
            <p>通过本项目，你将学习到现代前端开发的核心技术，包括：</p>
            <ul>
              <li>Vue.js/React框架使用</li>
              <li>组件化开发思想</li>
              <li>状态管理（Vuex/Redux）</li>
              <li>路由管理（Vue Router/React Router）</li>
              <li>API接口调用</li>
              <li>响应式布局</li>
            </ul>

            <h3>技术栈</h3>
            <p>Vue 3 + Vue Router + Vuex + Element Plus + Axios</p>

            <h3>功能模块</h3>
            <ol>
              <li>用户登录注册</li>
              <li>商品展示与搜索</li>
              <li>购物车管理</li>
              <li>订单处理</li>
              <li>用户中心</li>
            </ol>
          `,
          tutorial: `
            <h3>第一步：环境搭建</h3>
            <p>安装Node.js和Vue CLI，创建新项目</p>
            <pre><code>npm install -g @vue/cli
vue create ecommerce-project</code></pre>

            <h3>第二步：项目结构设计</h3>
            <p>规划组件结构，设计路由配置</p>

            <h3>第三步：核心功能开发</h3>
            <p>逐个实现各个功能模块</p>
          `,
          teachingVideos: [
            {
              name: '项目环境搭建',
              description: '学习如何搭建Vue开发环境',
              url: '#'
            },
            {
              name: '组件化开发',
              description: '掌握Vue组件化开发思想',
              url: '#'
            }
          ],
          excellentProjects: [
            {
              name: '小米商城前端实现',
              url: 'https://github.com/example/mi-store',
              description: '完整的小米商城前端实现，包含所有核心功能'
            },
            {
              name: '京东风格电商网站',
              url: 'https://github.com/example/jd-clone',
              description: '模仿京东UI的电商网站，代码结构清晰'
            }
          ]
        },
        {
          id: 2,
          title: '个人博客系统',
          description: '开发一个完整的个人博客系统，包括前端展示和后端管理功能，支持文章发布、评论等功能',
          type: 'FULLSTACK',
          difficulty: 'INTERMEDIATE',
          estimatedDays: 14,
          currentLearners: 95,
          coverImage: 'https://picsum.photos/800/400?random=2',
          detailedContent: '全栈博客系统详细内容...',
          tutorial: '博客系统开发教程...',
          teachingVideos: [
            {
              name: '后端API设计',
              description: '学习RESTful API设计原则',
              url: '#'
            }
          ],
          excellentProjects: [
            {
              name: 'Hexo博客系统',
              url: 'https://github.com/hexojs/hexo',
              description: '流行的静态博客生成器'
            }
          ]
        },
        {
          id: 3,
          title: '任务管理应用',
          description: '开发一个简单的任务管理应用，支持任务的增删改查、状态管理和分类功能',
          type: 'FRONTEND',
          difficulty: 'BEGINNER',
          estimatedDays: 3,
          currentLearners: 210,
          coverImage: 'https://picsum.photos/800/400?random=3'
        },
        {
          id: 4,
          title: '社交网络应用',
          description: '开发一个具有用户注册登录、动态发布、好友关注、消息通知等功能的社交网络应用',
          type: 'FULLSTACK',
          difficulty: 'ADVANCED',
          estimatedDays: 30,
          currentLearners: 75,
          coverImage: 'https://picsum.photos/800/400?random=4'
        },
        {
          id: 5,
          title: '在线学习平台后端',
          description: '构建一个支持课程管理、用户学习进度跟踪的在线学习平台后端系统',
          type: 'BACKEND',
          difficulty: 'INTERMEDIATE',
          estimatedDays: 21,
          currentLearners: 63,
          coverImage: 'https://picsum.photos/800/400?random=5'
        },
        {
          id: 6,
          title: '移动端健康应用',
          description: '开发一个健康管理移动应用，支持运动记录、饮食管理和健康数据分析',
          type: 'MOBILE',
          difficulty: 'ADVANCED',
          estimatedDays: 25,
          currentLearners: 42,
          coverImage: 'https://picsum.photos/800/400?random=6'
        }
      ]
    },

    async loadRecommendedProjects() {
      this.loading = true
      try {
        // 模拟推荐项目（按学习人数排序）
        this.projects = [...this.projects].sort((a, b) => b.currentLearners - a.currentLearners)
        this.filterType = ''
        this.filterDifficulty = ''
        this.filterDays = ''
        message.success('已加载推荐项目')
      } catch (error) {
        console.error('加载推荐项目失败:', error)
        this.projects = [...this.projects].sort((a, b) => b.currentLearners - a.currentLearners)
        message.info('已显示热门项目')
      } finally {
        this.loading = false
      }
    },

    resetFilters() {
      this.filterType = ''
      this.filterDifficulty = ''
      this.filterDays = ''
      this.loadProjects()
      message.info('已重置筛选条件')
    },

    loadCurrentProject() {
      // 当前进行中的项目数据
      this.currentProject = {
        id: 4,
        title: '社交网络应用',
        description: '开发一个具有用户注册登录、动态发布、好友关注、消息通知等功能的社交网络应用，前端使用React框架，后端使用Node.js和MongoDB数据库。',
        type: 'FULLSTACK',
        difficulty: 'ADVANCED',
        coverImage: 'https://picsum.photos/800/600?random=13'
      }
    },

    // 查看项目详情
    viewProjectDetail(project) {
      this.selectedProject = project
      this.projectDetailVisible = true
      this.activeTab = 'detail'
    },

    closeProjectDetail() {
      this.projectDetailVisible = false
      this.selectedProject = null
    },

    // 播放视频
    playVideo(video) {
      this.currentVideo = video
      this.videoModalVisible = true
      message.info(`开始播放: ${video.name}`)
    },

    closeVideo() {
      this.videoModalVisible = false
      this.currentVideo = null
    },

    // 查看优秀项目
    viewProject(project) {
      message.info(`查看项目: ${project.name}`)
    },

    openProjectLink(project) {
      if (project.url && project.url !== '#') {
        window.open(project.url, '_blank')
      } else {
        message.info('项目链接暂不可用')
      }
    },

    continueProject(projectId) {
      message.info(`继续开发项目: ${projectId}`)
    },

    getTypeText(type) {
      const typeMap = {
        'FRONTEND': '前端开发',
        'BACKEND': '后端开发',
        'FULLSTACK': '全栈开发',
        'MOBILE': '移动开发',
        'DATABASE': '数据库',
        'DEVOPS': '运维开发'
      }
      return typeMap[type] || type
    },

    getDifficultyText(difficulty) {
      const difficultyMap = {
        'BEGINNER': '入门',
        'INTERMEDIATE': '中级',
        'ADVANCED': '高级'
      }
      return difficultyMap[difficulty] || difficulty
    },

    getDifficultyColor(difficulty) {
      const colorMap = {
        'BEGINNER': 'bg-green-100 text-green-800',
        'INTERMEDIATE': 'bg-blue-100 text-blue-800',
        'ADVANCED': 'bg-orange-100 text-orange-800'
      }
      return colorMap[difficulty] || 'bg-gray-100 text-gray-800'
    },

    getDefaultImage(type) {
      const imageMap = {
        'FRONTEND': 'https://picsum.photos/800/400?random=10',
        'BACKEND': 'https://picsum.photos/800/400?random=20',
        'FULLSTACK': 'https://picsum.photos/800/400?random=30',
        'MOBILE': 'https://picsum.photos/800/400?random=40',
        'DATABASE': 'https://picsum.photos/800/400?random=50',
        'DEVOPS': 'https://picsum.photos/800/400?random=60'
      }
      return imageMap[type] || 'https://picsum.photos/800/400?random=100'
    },

    handleImageError(event) {
      event.target.src = 'https://picsum.photos/800/400?random=999'
    },

    formatDate(dateString) {
      if (!dateString) return '-'
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    },

    getDaysUsed(startDate) {
      if (!startDate) return 0
      const start = new Date(startDate)
      const now = new Date()
      const diffTime = Math.abs(now - start)
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    }
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

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 自定义进度条颜色 */
:deep(.ant-progress-bg) {
  background: linear-gradient(90deg, #1890ff, #52c41a);
}

/* 项目详情样式 */
.project-detail {
  max-height: 70vh;
  overflow-y: auto;
}

:deep(.ant-tabs-content) {
  padding: 0 16px;
}
</style>