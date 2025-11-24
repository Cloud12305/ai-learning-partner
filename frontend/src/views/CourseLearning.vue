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
          <a-input
              v-model:value="searchParams.keyword"
              placeholder="搜索课程名称或关键词"
              class="flex-1"
              @pressEnter="handleSearch"
          >
            <template #prefix>
              <search-outlined />
            </template>
          </a-input>

          <a-select
              v-model:value="searchParams.category"
              placeholder="选择课程分类"
              style="width: 200px"
              @change="handleSearch"
          >
            <a-select-option value="">全部分类</a-select-option>
            <a-select-option value="计算机">计算机</a-select-option>
            <a-select-option value="数学">数学</a-select-option>
            <a-select-option value="语言">语言</a-select-option>
          </a-select>

          <a-select
              v-model:value="searchParams.difficulty"
              placeholder="难度级别"
              style="width: 150px"
              @change="handleSearch"
          >
            <a-select-option value="">全部难度</a-select-option>
            <a-select-option value="beginner">入门</a-select-option>
            <a-select-option value="intermediate">中级</a-select-option>
            <a-select-option value="advanced">高级</a-select-option>
          </a-select>

          <a-button type="primary" @click="handleSearch" :loading="loading">
            搜索
          </a-button>
        </div>
      </div>

      <!-- 正在学习的课程 -->
      <div class="mb-12" v-if="currentCourses && currentCourses.length > 0">
        <h2 class="text-2xl font-semibold mb-6">正在学习</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div
              v-for="course in currentCourses"
              :key="course.id"
              class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow"
          >
            <div class="relative">
              <img
                  :src="course.imageUrl || '/images/course-placeholder.jpg'"
                  :alt="course.title"
                  class="w-full h-48 object-cover"
                  @error="handleImageError"
              >
              <div class="absolute top-3 right-3 bg-primary text-white text-sm px-2 py-1 rounded-full">
                进行中 65%
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold">{{ course.title }}</h3>
                <span class="text-sm text-gray-500">{{ course.category }}</span>
              </div>
              <p class="text-gray-600 text-sm mb-2">{{ course.university }}</p>
              <p class="text-gray-500 text-sm mb-4">讲师: {{ course.instructor }}</p>
              <div class="mb-4">
                <div class="flex justify-between text-sm mb-1">
                  <span>学习进度</span>
                  <span>65%</span>
                </div>
                <a-progress :percent="65" size="small" />
              </div>
              <a-button type="primary" block @click="startLearning(course)">
                继续学习
              </a-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 推荐课程 -->
      <div>
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-2xl font-semibold">推荐课程</h2>
          <a-button @click="refreshCourses" :loading="loading">
            刷新课程
          </a-button>
        </div>

        <div v-if="loading" class="text-center py-8">
          <a-spin size="large" />
          <p class="mt-4 text-gray-600">正在加载课程...</p>
        </div>

        <div v-else-if="!recommendedCourses || recommendedCourses.length === 0" class="text-center py-8">
          <p class="text-gray-600">暂无推荐课程</p>
          <a-button type="primary" class="mt-4" @click="refreshCourses">
            获取课程
          </a-button>
        </div>

        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <!-- 课程卡片 -->
          <div
              v-for="course in recommendedCourses"
              :key="course.courseLink + course.title"
              class="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-lg transition-shadow"
          >
            <div class="relative">
              <img
                  :src="course.imageUrl || '/images/course-placeholder.jpg'"
                  :alt="course.title"
                  class="w-full h-48 object-cover"
                  @error="handleImageError"
              >
              <div class="absolute top-3 right-3 bg-gray-800/80 text-white text-sm px-2 py-1 rounded-full">
                {{ getDifficultyText(course.difficulty) }}
              </div>
            </div>
            <div class="p-6">
              <div class="flex justify-between items-start mb-2">
                <h3 class="text-xl font-semibold line-clamp-2">{{ course.title }}</h3>
                <span class="text-sm text-gray-500 flex-shrink-0 ml-2">{{ course.platform }}</span>
              </div>
              <p class="text-gray-600 text-sm mb-2">{{ course.university }}</p>
              <p class="text-gray-500 text-sm mb-4">讲师: {{ course.instructor }}</p>
              <a-button block @click="startLearning(course)">
                加入学习
              </a-button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { Input, Select, Button, Progress, Spin, message } from 'ant-design-vue';
import { SearchOutlined } from '@ant-design/icons-vue';
// ✅ 正确的导入：使用 courseService
import courseService from '@/services/courseService';

export default {
  name: 'CourseLearning',
  components: {
    AInput: Input,
    ASelect: Select,
    ASelectOption: Select.Option,
    AButton: Button,
    AProgress: Progress,
    ASpin: Spin,
    SearchOutlined
  },
  data() {
    return {
      loading: false,
      recommendedCourses: [],
      currentCourses: [],
      searchParams: {
        keyword: '',
        category: '',
        difficulty: ''
      }
    };
  },
  mounted() {
    console.log('课程学习页面挂载');
    this.loadRecommendedCourses();
    this.loadCurrentCourses();
  },
  methods: {
    async loadRecommendedCourses() {
      console.log('开始加载推荐课程');
      this.loading = true;
      try {
        // ✅ 使用 courseService
        const response = await courseService.getRecommendedCourses();
        console.log('推荐课程完整响应:', response);

        if (response && response.success) {
          this.recommendedCourses = Array.isArray(response.data) ? response.data : [];
          console.log('设置推荐课程:', this.recommendedCourses);

          if (this.recommendedCourses.length > 0) {
            message.success(`加载了 ${this.recommendedCourses.length} 门推荐课程`);
          } else {
            message.warning('暂无推荐课程');
          }
        } else {
          console.warn('API返回失败:', response?.message);
          message.error(response?.message || '加载推荐课程失败');
          this.recommendedCourses = [];
        }
      } catch (error) {
        console.error('加载推荐课程异常:', error);
        message.error('网络请求异常');
        this.recommendedCourses = [];
      } finally {
        this.loading = false;
      }
    },

    async loadCurrentCourses() {
      // 暂时为空
      this.currentCourses = [];
    },

    async handleSearch() {
      this.loading = true;
      try {
        // ✅ 使用 courseService
        const response = await courseService.searchCourses(this.searchParams);

        if (response && response.success) {
          this.recommendedCourses = Array.isArray(response.data) ? response.data : [];
          message.success(response.message || `找到 ${this.recommendedCourses.length} 门课程`);
        } else {
          message.error(response?.message || '搜索课程失败');
        }
      } catch (error) {
        console.error('搜索课程异常:', error);
        message.error('搜索课程失败');
      } finally {
        this.loading = false;
      }
    },

    async refreshCourses() {
      console.log('手动刷新课程');
      this.loading = true;
      try {
        // ✅ 使用 courseService
        const response = await courseService.scrapeCourses();
        console.log('刷新课程响应:', response);

        if (response && response.success) {
          this.recommendedCourses = Array.isArray(response.data) ? response.data : [];
          message.success(response.message || `成功获取 ${this.recommendedCourses.length} 门课程`);
        } else {
          message.error(response?.message || '刷新课程失败');
        }
      } catch (error) {
        console.error('刷新课程异常:', error);
        message.error('刷新课程失败');
      } finally {
        this.loading = false;
      }
    },

    startLearning(course) {
      if (course.courseLink && course.courseLink.startsWith('http')) {
        window.open(course.courseLink, '_blank');
      } else {
        message.info('开始学习: ' + course.title);
      }
    },

    getDifficultyText(difficulty) {
      const map = {
        'beginner': '入门',
        'intermediate': '中级',
        'advanced': '高级'
      };
      return map[difficulty] || '中级';
    },

    handleImageError(event) {
      event.target.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZjNmM2YzIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCwgc2Fucy1zZXJpZiIgZm9udC1zaXplPSIxNiIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPua1i+ivlOeUqOaItzwvdGV4dD48L3N2Zz4=';
    }
  }
};
</script>

<style scoped>
.course-learning-page {
  padding-top: 80px;
}

.page-header {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>