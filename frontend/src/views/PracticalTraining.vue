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
                  :src="project.cover_image || getDefaultImage(project.type)"
                  :alt="project.title"
                  class="w-full h-48 object-cover"
                  @error="handleImageError"
              >
              <div v-if="project.current_learners > 100" class="absolute top-3 left-3 bg-red-500 text-white text-sm px-2 py-1 rounded-full">
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
                  预计{{ project.estimated_days || 7 }}天
                </div>
                <div class="flex items-center text-sm text-gray-500">
                  <span class="w-2 h-2 bg-blue-500 rounded-full mr-2"></span>
                  {{ project.current_learners || 0 }}人学习
                </div>
              </div>
              <div class="flex gap-2">
                <Button
                    type="primary"
                    class="flex-1 h-10 font-medium"
                    @click="viewProjectDetail(project)"
                >
                  开始练习
                </Button>
                <Button
                    v-if="getUserProgress(project.id)"
                    class="h-10 font-medium"
                    @click="continueProject(project)"
                >
                  继续
                </Button>
              </div>
              <!-- 进度显示 -->
              <div v-if="getUserProgress(project.id)" class="mt-3">
                <div class="flex justify-between text-xs text-gray-500 mb-1">
                  <span>进度</span>
                  <span>{{ getUserProgress(project.id).progress_percentage }}%</span>
                </div>
                <a-progress
                    :percent="getUserProgress(project.id).progress_percentage"
                    size="small"
                    :show-info="false"
                />
              </div>
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
                    :src="selectedProject.cover_image || getDefaultImage(selectedProject.type)"
                    :alt="selectedProject.title"
                    class="w-full h-64 object-cover rounded-lg"
                >
              </div>
              <div class="md:w-2/3">
                <div class="flex items-center gap-2 mb-4">
                  <a-badge class="bg-blue-100 text-blue-800 border-0">{{ getTypeText(selectedProject.type) }}</a-badge>
                  <a-badge :class="`${getDifficultyColor(selectedProject.difficulty)} border-0`">
                    {{ getDifficultyText(selectedProject.difficulty) }}
                  </a-badge>
                </div>
                <p class="text-gray-600 mb-4">{{ selectedProject.description }}</p>
                <div class="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <span class="text-gray-500">预计时间：</span>
                    <span>{{ selectedProject.estimated_days }}天</span>
                  </div>
                  <div>
                    <span class="text-gray-500">学习人数：</span>
                    <span>{{ selectedProject.current_learners }}人</span>
                  </div>
                  <div v-if="getUserProgress(selectedProject.id)">
                    <span class="text-gray-500">你的进度：</span>
                    <span>{{ getUserProgress(selectedProject.id).progress_percentage }}%</span>
                  </div>
                  <div v-if="getUserProgress(selectedProject.id)">
                    <span class="text-gray-500">已完成：</span>
                    <span>{{ getUserProgress(selectedProject.id).completed_modules }}/{{ getUserProgress(selectedProject.id).total_modules }}模块</span>
                  </div>
                </div>
                <div class="mt-4 flex gap-2">
                  <Button type="primary" @click="startProject(selectedProject)">
                    {{ getUserProgress(selectedProject.id) ? '继续学习' : '开始学习' }}
                  </Button>
                  <Button v-if="getUserProgress(selectedProject.id)" @click="resetProgress(selectedProject.id)">
                    重置进度
                  </Button>
                </div>
              </div>
            </div>
          </div>

          <!-- 标签页内容 -->
          <a-tabs v-model:activeKey="activeTab">
            <!-- 项目详细介绍 -->
            <a-tab-pane key="detail" tab="项目介绍">
              <div class="prose max-w-none" v-html="selectedProject.detailed_content || '暂无详细介绍'"></div>
            </a-tab-pane>

            <!-- 详细教程 -->
            <a-tab-pane key="tutorial" tab="详细教程">
              <div class="prose max-w-none" v-html="selectedProject.tutorial || '暂无教程内容'"></div>
            </a-tab-pane>

            <!-- 教学视频 -->
            <a-tab-pane key="videos" tab="教学视频">
              <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div
                    v-for="(video, index) in parseJsonField(selectedProject.teaching_videos)"
                    :key="index"
                    class="border rounded-lg overflow-hidden hover:shadow-md transition-shadow"
                >
                  <div class="aspect-w-16 aspect-h-9 bg-gray-100 flex items-center justify-center p-4">
                    <div class="text-center">
                      <div class="text-4xl mb-2 text-gray-400">🎬</div>
                      <p class="text-gray-600 text-sm">{{ video.name || `教学视频 ${index + 1}` }}</p>
                    </div>
                  </div>
                  <div class="p-4">
                    <h4 class="font-semibold mb-2 text-gray-800">{{ video.name || `教学视频 ${index + 1}` }}</h4>
                    <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ video.description || '暂无描述' }}</p>
                    <Button type="primary" size="small" block @click="playVideo(video)">观看视频</Button>
                  </div>
                </div>
                <div v-if="!parseJsonField(selectedProject.teaching_videos) || parseJsonField(selectedProject.teaching_videos).length === 0" class="col-span-3 text-center py-8">
                  <div class="text-gray-400 text-4xl mb-2">📹</div>
                  <p class="text-gray-500">暂无教学视频</p>
                </div>
              </div>
            </a-tab-pane>

            <!-- 优秀项目展示 -->
            <a-tab-pane key="projects" tab="优秀项目">
              <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div
                    v-for="(project, index) in parseJsonField(selectedProject.excellent_projects)"
                    :key="index"
                    class="border rounded-lg p-4 hover:shadow-md transition-shadow bg-white"
                >
                  <h4 class="font-semibold mb-2 text-gray-800">{{ project.name }}</h4>
                  <p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ project.description || '暂无描述' }}</p>
                  <div class="flex gap-2 mt-4">
                    <Button
                        type="primary"
                        size="small"
                        @click="viewProject(project)"
                        :disabled="!project.url || project.url === '#'"
                    >
                      查看项目
                    </Button>
                    <Button
                        size="small"
                        @click="openProjectLink(project)"
                        :disabled="!project.url || project.url === '#'"
                    >
                      源码链接
                    </Button>
                  </div>
                  <div v-if="!project.url || project.url === '#'" class="text-xs text-gray-400 mt-2">
                    链接暂不可用
                  </div>
                </div>
                <div v-if="!parseJsonField(selectedProject.excellent_projects) || parseJsonField(selectedProject.excellent_projects).length === 0" class="col-span-3 text-center py-8">
                  <div class="text-gray-400 text-4xl mb-2">⭐</div>
                  <p class="text-gray-500">暂无优秀项目展示</p>
                </div>
              </div>
            </a-tab-pane>

            <!-- 学习资源 -->
            <a-tab-pane key="resources" tab="学习资源">
              <div class="space-y-4">
                <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
                  <h4 class="font-semibold text-blue-800 mb-2">📚 学习资料</h4>
                  <ul class="text-sm text-blue-700 space-y-1">
                    <li>• 官方文档：<a href="#" class="underline">查看相关技术文档</a></li>
                    <li>• 社区论坛：<a href="#" class="underline">参与技术讨论</a></li>
                    <li>• 在线教程：<a href="#" class="underline">学习基础知识</a></li>
                  </ul>
                </div>

                <div class="bg-green-50 border border-green-200 rounded-lg p-4">
                  <h4 class="font-semibold text-green-800 mb-2">🛠️ 开发工具</h4>
                  <ul class="text-sm text-green-700 space-y-1">
                    <li>• 代码编辑器：VS Code / WebStorm</li>
                    <li>• 版本控制：Git</li>
                    <li>• 包管理器：npm / yarn</li>
                  </ul>
                </div>

                <div class="bg-purple-50 border border-purple-200 rounded-lg p-4">
                  <h4 class="font-semibold text-purple-800 mb-2">💡 学习建议</h4>
                  <ul class="text-sm text-purple-700 space-y-1">
                    <li>• 每天坚持学习2-3小时</li>
                    <li>• 边学边练，动手写代码</li>
                    <li>• 遇到问题先思考再求助</li>
                    <li>• 定期复习和总结</li>
                  </ul>
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
          <div class="aspect-w-16 aspect-h-9 bg-black rounded-lg mb-4 flex items-center justify-center min-h-[400px]">
            <div class="text-white text-lg">
              🎬 视频播放器 - {{ currentVideo.name }}
              <p class="text-sm text-gray-300 mt-2">视频链接: {{ currentVideo.url || '#' }}</p>
            </div>
          </div>
          <p class="text-gray-600">{{ currentVideo.description }}</p>
          <div class="mt-4">
            <Button type="primary" @click="openVideoLink(currentVideo)" :disabled="!currentVideo.url || currentVideo.url === '#'">
              前往观看
            </Button>
          </div>
        </div>
      </a-modal>
    </main>
  </div>
</template>

<script>
import { Select, Button, Badge, Progress, Spin, Modal, Tabs, message } from 'ant-design-vue'

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
      userProjectProgress: [], // 用户项目进度数据
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
    this.loadUserProgress()
  },
  methods: {
    async loadProjects() {
      this.loading = true
      try {
        // 调用后端API获取项目数据
        const response = await this.$api.get('/api/practical-projects', {
          params: {
            type: this.filterType || undefined,
            difficulty: this.filterDifficulty || undefined,
            estimatedDays: this.filterDays || undefined
          }
        })
        this.projects = response.data
      } catch (error) {
        console.error('加载项目失败:', error)
        message.error('加载项目失败，使用默认数据')
        this.loadDefaultProjects()
      } finally {
        this.loading = false
      }
    },

    async loadUserProgress() {
      try {
        // 获取当前用户的项目进度
        const response = await this.$api.get('/api/user-project-progress/current')
        this.userProjectProgress = response.data
      } catch (error) {
        console.error('加载用户进度失败:', error)
        // 使用模拟数据
        this.userProjectProgress = [
          {
            project_id: 1,
            completed_modules: 2,
            total_modules: 8,
            progress_percentage: 25,
            status: 'IN_PROGRESS'
          },
          {
            project_id: 4,
            completed_modules: 6,
            total_modules: 10,
            progress_percentage: 60,
            status: 'IN_PROGRESS'
          }
        ]
      }
    },

    loadDefaultProjects() {
      // 默认项目数据 - 从数据库示例数据中获取
      this.projects = [
        {
          id: 1,
          title: '电商网站前端实现',
          description: '实现一个完整的电商网站前端，包括首页、商品列表、详情页、购物车和结算功能',
          type: 'FRONTEND',
          difficulty: 'INTERMEDIATE',
          estimated_days: 7,
          current_learners: 128,
          cover_image: 'https://picsum.photos/800/400?random=1',
          detailed_content: '<h3>项目目标</h3><p>通过本项目，你将学习到现代前端开发的核心技术，包括：</p><ul><li>Vue.js/React框架使用</li><li>组件化开发思想</li><li>状态管理（Vuex/Redux）</li><li>路由管理（Vue Router/React Router）</li><li>API接口调用</li><li>响应式布局</li></ul><h3>技术栈</h3><p>Vue 3 + Vue Router + Vuex + Element Plus + Axios</p><h3>功能模块</h3><ol><li>用户登录注册</li><li>商品展示与搜索</li><li>购物车管理</li><li>订单处理</li><li>用户中心</li></ol>',
          tutorial: '<h3>第一步：环境搭建</h3><p>安装Node.js和Vue CLI，创建新项目</p><pre><code>npm install -g @vue/cli\nvue create ecommerce-project</code></pre><h3>第二步：项目结构设计</h3><p>规划组件结构，设计路由配置</p><h3>第三步：核心功能开发</h3><p>逐个实现各个功能模块</p>',
          teaching_videos: '[{"name": "项目环境搭建", "description": "学习如何搭建Vue开发环境", "url": "#"}, {"name": "组件化开发", "description": "掌握Vue组件化开发思想", "url": "#"}]',
          excellent_projects: '[{"name": "小米商城前端实现", "url": "https://gitee.com/mi-store/mi-store-frontend", "description": "完整的小米商城前端实现，包含所有核心功能"}, {"name": "京东风格电商网站", "url": "https://gitee.com/jd-clone/jd-ecommerce", "description": "模仿京东UI的电商网站，代码结构清晰"}]'
        },
        {
          id: 2,
          title: '个人博客系统',
          description: '开发一个完整的个人博客系统，包括前端展示和后端管理功能，支持文章发布、评论等功能',
          type: 'FULLSTACK',
          difficulty: 'INTERMEDIATE',
          estimated_days: 14,
          current_learners: 95,
          cover_image: 'https://picsum.photos/800/400?random=2',
          detailed_content: '<h3>项目目标</h3><p>通过本项目，你将掌握全栈开发技能：</p><ul><li>前端框架(Vue/React)</li><li>后端框架(Spring Boot/Express)</li><li>数据库设计(MySQL/MongoDB)</li><li>用户认证与授权</li><li>RESTful API设计</li></ul><h3>技术栈</h3><p>Vue 3 + Spring Boot + MySQL + JWT</p><h3>功能模块</h3><ol><li>文章管理(CRUD)</li><li>用户评论系统</li><li>标签分类</li><li>搜索功能</li><li>后台管理系统</li></ol>',
          tutorial: '<h3>第一步：数据库设计</h3><p>设计用户、文章、评论等数据表</p><h3>第二步：后端API开发</h3><p>使用Spring Boot开发RESTful API</p><h3>第三步：前端界面开发</h3><p>使用Vue 3开发用户界面</p>',
          teaching_videos: '[{"name": "后端API设计", "description": "学习RESTful API设计原则", "url": "#"}]',
          excellent_projects: '[{"name": "Hexo博客系统", "url": "https://gitee.com/mirrors/hexo", "description": "流行的静态博客生成器"}, {"name": "Vue博客系统", "url": "https://gitee.com/vue-blog/vue-blog-system", "description": "基于Vue+Spring Boot的博客系统"}]'
        },
        {
          id: 3,
          title: '任务管理应用',
          description: '开发一个简单的任务管理应用，支持任务的增删改查、状态管理和分类功能',
          type: 'FRONTEND',
          difficulty: 'BEGINNER',
          estimated_days: 3,
          current_learners: 210,
          cover_image: 'https://picsum.photos/800/400?random=3',
          detailed_content: '<h3>项目目标</h3><p>适合前端入门学习的项目：</p><ul><li>基础HTML/CSS/JavaScript</li><li>DOM操作</li><li>事件处理</li><li>本地存储</li><li>响应式设计</li></ul><h3>技术栈</h3><p>HTML5 + CSS3 + JavaScript + LocalStorage</p><h3>功能模块</h3><ol><li>任务添加与删除</li><li>任务状态切换</li><li>任务分类</li><li>数据持久化</li><li>响应式布局</li></ol>',
          tutorial: '<h3>第一步：HTML结构搭建</h3><p>设计任务列表的HTML结构，包括任务输入框、任务列表容器、筛选按钮等。</p><pre><code>&lt;div class="todo-app"&gt;  &lt;h1&gt;任务管理&lt;/h1&gt;  &lt;input type="text" id="taskInput" placeholder="添加新任务..."&gt;  &lt;ul id="taskList"&gt;&lt;/ul&gt;  &lt;div class="filters"&gt;    &lt;button class="active" data-filter="all"&gt;全部&lt;/button&gt;    &lt;button data-filter="active"&gt;进行中&lt;/button&gt;    &lt;button data-filter="completed"&gt;已完成&lt;/button&gt;  &lt;/div&gt;&lt;/div&gt;</code></pre><h3>第二步：CSS样式设计</h3><p>美化界面，实现响应式布局，添加交互动画效果。</p><h3>第三步：JavaScript功能实现</h3><p>实现任务的增删改查功能，添加本地存储支持。</p>',
          teaching_videos: '[{"name": "HTML结构设计", "description": "学习如何设计合理的HTML结构", "url": "https://example.com/video1"}, {"name": "CSS样式美化", "description": "掌握CSS布局和样式技巧", "url": "https://example.com/video2"}, {"name": "JavaScript交互实现", "description": "实现任务管理的核心交互功能", "url": "https://example.com/video3"}]',
          excellent_projects: '[{"name": "TodoList应用", "url": "https://gitee.com/todolist/todolist-vanilla", "description": "纯JavaScript实现的TodoList应用，代码简洁易懂"}, {"name": "Vue任务管理", "url": "https://gitee.com/vue-todo/vue-todo-app", "description": "基于Vue 3的任务管理应用，包含完整的功能"}, {"name": "React待办事项", "url": "https://gitee.com/react-todo/react-todo-app", "description": "使用React Hooks实现的现代化待办事项应用"}]'
        },
        {
          id: 4,
          title: '社交网络应用',
          description: '开发一个具有用户注册登录、动态发布、好友关注、消息通知等功能的社交网络应用',
          type: 'FULLSTACK',
          difficulty: 'ADVANCED',
          estimated_days: 30,
          current_learners: 75,
          cover_image: 'https://picsum.photos/800/400?random=4',
          detailed_content: '<h3>项目目标</h3><p>高级全栈项目，涵盖复杂业务逻辑：</p><ul><li>微服务架构设计与实现</li><li>WebSocket实时通信</li><li>文件上传与云存储</li><li>消息队列与异步处理</li><li>性能优化与缓存策略</li><li>安全认证与授权</li></ul><h3>技术栈</h3><p>前端：React + Redux + WebSocket + Ant Design</p><p>后端：Node.js + Express + MongoDB + Redis + Socket.io</p><p>部署：Docker + Nginx + PM2</p><h3>功能模块</h3><ol><li>用户系统（注册、登录、资料管理）</li><li>动态发布（文字、图片、视频）</li><li>好友关系（关注、粉丝、好友推荐）</li><li>实时聊天（一对一、群聊、消息推送）</li><li>通知系统（点赞、评论、关注通知）</li><li>内容推荐（个性化推荐算法）</li><li>搜索功能（全文检索、用户搜索）</li></ol>',
          tutorial: '<h3>第一步：架构设计</h3><p>设计微服务架构，规划数据库模型，定义API接口规范。</p><h3>第二步：用户服务开发</h3><p>实现用户注册登录、JWT认证、资料管理功能。</p><h3>第三步：动态服务开发</h3><p>实现动态发布、点赞、评论、分享功能。</p><h3>第四步：实时聊天开发</h3><p>使用Socket.io实现实时通信，支持一对一和群聊。</p>',
          teaching_videos: '[{"name": "微服务架构设计", "description": "学习如何设计可扩展的微服务架构", "url": "https://example.com/video4"}, {"name": "WebSocket实时通信", "description": "掌握Socket.io实现实时聊天功能", "url": "https://example.com/video5"}, {"name": "文件上传与云存储", "description": "实现图片视频上传和云存储集成", "url": "https://example.com/video6"}, {"name": "消息队列应用", "description": "使用Redis实现消息队列和异步处理", "url": "https://example.com/video7"}]',
          excellent_projects: '[{"name": "校园社交平台", "url": "https://gitee.com/campus-social/campus-network", "description": "基于Spring Cloud的校园社交平台，功能完整"}, {"name": "微博系统", "url": "https://gitee.com/weibo-clone/weibo-system", "description": "仿微博的社交网络系统，包含所有核心功能"}, {"name": "朋友圈应用", "url": "https://gitee.com/moments-app/social-network", "description": "类似微信朋友圈的社交应用实现"}]'
        },
        {
          id: 5,
          title: '在线学习平台后端',
          description: '构建一个支持课程管理、用户学习进度跟踪的在线学习平台后端系统',
          type: 'BACKEND',
          difficulty: 'INTERMEDIATE',
          estimated_days: 21,
          current_learners: 63,
          cover_image: 'https://picsum.photos/800/400?random=5',
          detailed_content: '<h3>项目目标</h3><p>专注于后端开发技能：</p><ul><li>Spring Boot框架深入应用</li><li>RESTful API设计与实现</li><li>数据库设计与优化</li><li>JWT认证与权限控制</li><li>Redis缓存与性能优化</li><li>Docker容器化部署</li><li>API文档自动化生成</li></ul><h3>技术栈</h3><p>Spring Boot 2.7 + Spring Security + JWT + MySQL + Redis + MyBatis Plus + Swagger + Docker</p><h3>功能模块</h3><ol><li>课程管理（CRUD、分类、搜索）</li><li>用户管理（注册、登录、权限）</li><li>学习进度（记录、统计、提醒）</li><li>支付集成（支付宝、微信支付）</li><li>数据统计（学习分析、报表）</li><li>消息通知（邮件、站内信）</li><li>文件管理（课程资料、视频）</li></ol><h3>数据库设计</h3><p>核心表结构：users（用户）、courses（课程）、enrollments（报名）、progress（进度）、orders（订单）、payments（支付）</p>',
          tutorial: '<h3>第一步：项目搭建</h3><p>创建Spring Boot项目，配置数据库连接和基础依赖。</p><h3>第二步：实体类设计</h3><p>设计课程、用户、订单等实体类，建立表关系。</p><h3>第三步：业务逻辑实现</h3><p>实现各个业务模块的Service层，包含核心业务逻辑。</p><h3>第四步：API接口开发</h3><p>开发RESTful API，添加Swagger文档支持。</p>',
          teaching_videos: '[{"name": "Spring Boot项目搭建", "description": "学习如何快速搭建Spring Boot项目", "url": "https://example.com/video8"}, {"name": "JWT认证实现", "description": "掌握Spring Security + JWT认证方案", "url": "https://example.com/video9"}, {"name": "Redis缓存应用", "description": "使用Redis提升系统性能", "url": "https://example.com/video10"}, {"name": "Docker部署", "description": "将应用容器化并部署到服务器", "url": "https://example.com/video11"}]',
          excellent_projects: '[{"name": "在线教育平台", "url": "https://gitee.com/edu-platform/online-edu", "description": "完整的在线教育平台后端系统，功能丰富"}, {"name": "慕课网后端", "url": "https://gitee.com/mooc-backend/mooc-system", "description": "仿慕课网的后端系统，代码质量高"}, {"name": "学堂在线", "url": "https://gitee.com/xuetangx/backend", "description": "类似学堂在线的在线学习平台后端"}]'
        },
        {
          id: 6,
          title: '移动端健康应用',
          description: '开发一个健康管理移动应用，支持运动记录、饮食管理和健康数据分析',
          type: 'MOBILE',
          difficulty: 'ADVANCED',
          estimated_days: 25,
          current_learners: 42,
          cover_image: 'https://picsum.photos/800/400?random=6',
          detailed_content: '<h3>项目目标</h3><p>移动端开发综合项目：</p><ul><li>React Native/Flutter跨平台开发</li><li>移动端UI/UX设计原则</li><li>数据可视化与图表展示</li><li>第三方API集成（健康数据）</li><li>本地数据存储与同步</li><li>应用发布与商店上架</li><li>性能优化与内存管理</li></ul><h3>技术栈</h3><p>React Native + Redux Toolkit + React Navigation + Chart.js + Health Kit API + AsyncStorage</p><h3>功能模块</h3><ol><li>运动记录（步数、距离、卡路里）</li><li>饮食管理（食物记录、营养分析）</li><li>健康数据（心率、睡眠、体重）</li><li>数据图表（趋势分析、统计报表）</li><li>目标设定（运动目标、饮食计划）</li><li>个人设置（提醒、单位、同步）</li><li>社交分享（成就分享、进度对比）</li></ol><h3>特色功能</h3><p>健康数据同步、智能提醒、成就系统、数据备份、多设备同步</p>',
          tutorial: '<h3>第一步：环境配置</h3><p>配置React Native开发环境，安装必要工具和依赖。</p><h3>第二步：UI组件开发</h3><p>开发各个功能页面的UI组件，实现响应式设计。</p><h3>第三步：业务逻辑集成</h3><p>集成健康数据API，实现本地数据存储和状态管理。</p><h3>第四步：数据可视化</h3><p>使用图表库展示健康数据趋势和分析。</p>',
          teaching_videos: '[{"name": "React Native环境搭建", "description": "配置React Native开发环境", "url": "https://example.com/video12"}, {"name": "移动端UI设计", "description": "学习移动端界面设计原则", "url": "https://example.com/video13"}, {"name": "健康API集成", "description": "集成健康数据API获取用户数据", "url": "https://example.com/video14"}, {"name": "数据可视化", "description": "使用图表展示健康数据趋势", "url": "https://example.com/video15"}, {"name": "应用发布", "description": "将应用发布到App Store和Google Play", "url": "https://example.com/video16"}]',
          excellent_projects: '[{"name": "健康管理App", "url": "https://gitee.com/health-app/health-manager", "description": "基于React Native的健康管理应用，功能完整"}, {"name": "运动记录应用", "url": "https://gitee.com/fitness-app/fitness-tracker", "description": "专业的运动记录和数据分析应用"}, {"name": "饮食管理助手", "url": "https://gitee.com/diet-app/nutrition-tracker", "description": "帮助用户记录和分析饮食营养的应用"}]'
        }
      ]
    },

    async loadRecommendedProjects() {
      this.loading = true
      try {
        // 调用推荐项目API
        const response = await this.$api.get('/api/practical-projects/recommended')
        this.projects = response.data
        this.filterType = ''
        this.filterDifficulty = ''
        this.filterDays = ''
        message.success('已加载推荐项目')
      } catch (error) {
        console.error('加载推荐项目失败:', error)
        // 降级处理：按学习人数排序
        this.projects = [...this.projects].sort((a, b) => b.current_learners - a.current_learners)
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

    // 开始/继续项目
    async startProject(project) {
      try {
        if (!this.getUserProgress(project.id)) {
          // 创建新的进度记录
          await this.$api.post('/api/user-project-progress', {
            projectId: project.id,
            totalModules: 10 // 默认模块数，实际应该从项目配置中获取
          })
          message.success('已开始学习项目')
        } else {
          message.info('继续学习项目')
        }

        // 跳转到项目学习页面或开始学习
        this.$router.push(`/project-learning/${project.id}`)
      } catch (error) {
        console.error('开始项目失败:', error)
        message.error('开始项目失败')
      }
    },

    // 继续项目
    continueProject(project) {
      this.$router.push(`/project-learning/${project.id}`)
    },

    // 重置进度
    async resetProgress(projectId) {
      try {
        await this.$api.delete(`/api/user-project-progress/${projectId}`)
        await this.loadUserProgress()
        message.success('进度已重置')
      } catch (error) {
        console.error('重置进度失败:', error)
        message.error('重置进度失败')
      }
    },

    // 获取用户项目进度
    getUserProgress(projectId) {
      return this.userProjectProgress.find(progress => progress.project_id === projectId)
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

    // 打开视频链接
    openVideoLink(video) {
      if (video.url && video.url !== '#') {
        window.open(video.url, '_blank')
      } else {
        message.warning('视频链接暂不可用')
      }
    },

    // 查看优秀项目
    viewProject(project) {
      message.info(`查看项目: ${project.name}`)
    },

    openProjectLink(project) {
      if (project.url && project.url !== '#') {
        window.open(project.url, '_blank')
      } else {
        message.warning('项目链接暂不可用')
      }
    },

    // 解析JSON字段
    parseJsonField(field) {
      if (!field) return []
      try {
        return typeof field === 'string' ? JSON.parse(field) : field
      } catch (error) {
        console.error('解析JSON字段失败:', error, field)
        return []
      }
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

/* 视频卡片样式 */
.aspect-w-16 {
  position: relative;
}

.aspect-w-16::before {
  content: '';
  display: block;
  padding-top: 56.25%; /* 16:9 Aspect Ratio */
}

.aspect-w-16 > * {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
</style>