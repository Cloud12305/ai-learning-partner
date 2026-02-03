<!-- JobMatching.vue - 修复版本 -->
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
              <div class="flex flex-wrap gap-2 mb-3">
                <Tag
                    v-for="(skill, index) in userSkills"
                    :key="skill"
                    :closable="true"
                    color="blue"
                    @close="removeSkill(index)"
                >
                  {{ skill }}
                </Tag>
                <div v-if="userSkills.length === 0" class="text-gray-400 text-sm">
                  暂无技能，请添加技能
                </div>
              </div>

              <div class="flex gap-2">
                <Input
                    v-model:value="newSkill"
                    placeholder="输入技能名称"
                    size="small"
                    @press-enter="addSkill"
                    class="flex-1"
                />
                <Button type="dashed" size="small" @click="addSkill">
                  <i class="fa fa-plus mr-1"></i> 添加
                </Button>
              </div>
              <p class="text-xs text-gray-500 mt-1">按回车或点击添加按钮添加技能</p>
            </div>

            <div>
              <h3 class="font-medium mb-3">求职意向</h3>
              <div class="space-y-3">
                <div>
                  <label class="text-sm text-gray-600 block mb-1">期望职位</label>
                  <Select
                      placeholder="选择职位"
                      v-model:value="careerPreference.position"
                      @change="handlePreferenceChange"
                  >
                    <Select.Option value="前端开发工程师">前端开发工程师</Select.Option>
                    <Select.Option value="后端开发工程师">后端开发工程师</Select.Option>
                    <Select.Option value="全栈开发工程师">全栈开发工程师</Select.Option>
                    <Select.Option value="Java开发工程师">Java开发工程师</Select.Option>
                    <Select.Option value="Node.js开发工程师">Node.js开发工程师</Select.Option>
                    <Select.Option value="Python开发工程师">Python开发工程师</Select.Option>
                  </Select>
                </div>
                <div>
                  <label class="text-sm text-gray-600 block mb-1">期望行业</label>
                  <Select
                      placeholder="选择行业"
                      v-model:value="careerPreference.industry"
                      @change="handlePreferenceChange"
                  >
                    <Select.Option value="互联网">互联网</Select.Option>
                    <Select.Option value="游戏">游戏</Select.Option>
                    <Select.Option value="医疗健康">医疗健康</Select.Option>
                    <Select.Option value="金融">金融</Select.Option>
                    <Select.Option value="教育">教育</Select.Option>
                    <Select.Option value="电子商务">电子商务</Select.Option>
                    <Select.Option value="人工智能">人工智能</Select.Option>
                  </Select>
                </div>
                <div>
                  <label class="text-sm text-gray-600 block mb-1">期望地点</label>
                  <Select
                      placeholder="选择城市"
                      v-model:value="careerPreference.location"
                      @change="handlePreferenceChange"
                  >
                    <Select.Option value="北京">北京</Select.Option>
                    <Select.Option value="上海">上海</Select.Option>
                    <Select.Option value="广州">广州</Select.Option>
                    <Select.Option value="深圳">深圳</Select.Option>
                    <Select.Option value="杭州">杭州</Select.Option>
                    <Select.Option value="成都">成都</Select.Option>
                    <Select.Option value="武汉">武汉</Select.Option>
                    <Select.Option value="远程">远程</Select.Option>
                  </Select>
                </div>
              </div>
              <Button
                  type="primary"
                  block
                  class="mt-4"
                  :loading="updatingPreference"
                  @click="updateCareerPreference"
              >
                {{ updatingPreference ? '更新中...' : '更新求职意向' }}
              </Button>
            </div>
          </div>
        </div>

        <!-- 右侧：匹配岗位 -->
        <div class="md:col-span-2">
          <div class="bg-white rounded-xl shadow-md p-6 mb-6">
            <div class="flex justify-between items-center mb-6">
              <h2 class="text-xl font-semibold">为你匹配的岗位</h2>
              <div class="flex gap-2">
                <Button
                    type="primary"
                    :loading="loading"
                    @click="loadJobMatches"
                >
                  {{ loading ? '匹配中...' : '重新匹配' }}
                </Button>
              </div>
            </div>

            <!-- 当前求职意向 -->
            <div class="mb-6 p-4 bg-blue-50 rounded-lg">
              <div class="flex items-center justify-between">
                <div>
                  <span class="text-blue-700 font-medium">当前求职意向：</span>
                  <span class="text-blue-600">{{ careerPreference.position }} · {{ careerPreference.industry }} · {{ careerPreference.location }}</span>
                </div>
                <div class="text-blue-600 text-sm">
                  {{ autoMatch ? '自动匹配中' : '点击重新匹配更新结果' }}
                </div>
              </div>
            </div>

            <!-- 匹配结果统计 -->
            <div v-if="matchedJobs.length > 0" class="mb-6 p-4 bg-green-50 rounded-lg">
              <div class="flex items-center justify-between">
                <div>
                  <span class="text-green-700 font-medium">找到 {{ totalJobs }} 个匹配岗位</span>
                  <span class="text-green-600 text-sm ml-2">基于你的求职意向和技能</span>
                </div>
                <div class="text-green-600 text-sm">
                  最高匹配度：{{ (Math.max(...matchedJobs.map(job => job.matchScore)) * 100).toFixed(0) }}%
                </div>
              </div>
            </div>

            <div v-if="loading" class="text-center py-8">
              <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
              <p class="mt-4 text-gray-600">正在智能匹配岗位...</p>
            </div>

            <div v-else-if="matchedJobs.length === 0" class="text-center py-8">
              <div class="text-gray-400 mb-4">
                <i class="fa fa-search text-4xl"></i>
              </div>
              <p class="text-gray-600 mb-2">暂无匹配的岗位</p>
              <p class="text-gray-500 text-sm">请完善您的技能和求职意向，然后点击"重新匹配"</p>
              <Button type="primary" class="mt-4" @click="loadJobMatches">
                开始匹配
              </Button>
            </div>

            <div v-else class="space-y-6">
              <!-- 岗位列表 -->
              <div
                  v-for="job in displayedJobs"
                  :key="job.jobId"
                  class="border border-gray-200 rounded-lg p-6 hover:shadow-md transition-shadow"
              >
                <div class="flex flex-col md:flex-row md:items-center justify-between mb-4">
                  <div class="flex-1">
                    <h3 class="text-lg font-semibold text-gray-900 mb-1">{{ job.jobTitle }}</h3>
                    <div class="flex items-center gap-4 text-sm text-gray-600">
                      <span class="flex items-center">
                        <i class="fa fa-building mr-1"></i>{{ job.companyName }}
                      </span>
                      <span class="flex items-center">
                        <i class="fa fa-map-marker mr-1"></i>{{ job.location }}
                      </span>
                      <span class="flex items-center">
                        <i class="fa fa-money mr-1"></i>{{ job.salaryRange }}
                      </span>
                      <span class="flex items-center">
                        <i class="fa fa-industry mr-1"></i>{{ job.industry }}
                      </span>
                    </div>
                  </div>
                  <div class="mt-3 md:mt-0 flex items-center gap-2">
                    <Badge :color="getMatchScoreColor(job.matchScore)" class="mr-2">
                      匹配度 {{ (job.matchScore * 100).toFixed(0) }}%
                    </Badge>
                    <Button type="primary" size="small" @click="applyJob(job)">立即申请</Button>
                  </div>
                </div>

                <div class="mb-4">
                  <div class="text-sm text-gray-700 mb-2 font-medium">技能要求：</div>
                  <div class="flex flex-wrap gap-2">
                    <Tag
                        v-for="skill in job.requiredSkills"
                        :key="skill"
                        :color="userSkills.includes(skill) ? 'green' : 'red'"
                    >
                      {{ skill }}
                      <i v-if="userSkills.includes(skill)" class="fa fa-check ml-1"></i>
                      <i v-else class="fa fa-times ml-1"></i>
                    </Tag>
                  </div>
                </div>

                <div class="text-sm text-gray-600 mb-4 leading-relaxed">
                  {{ job.jobDescription }}
                </div>

                <!-- 匹配分析 -->
                <div class="grid grid-cols-2 gap-4 text-xs text-gray-500 mb-4">
                  <div>
                    <span class="font-medium">职位匹配：</span>
                    <span :class="job.positionMatch ? 'text-green-600' : 'text-red-600'">
                      {{ job.positionMatch ? '✓ 符合意向' : '✗ 不完全匹配' }}
                    </span>
                  </div>
                  <div>
                    <span class="font-medium">行业匹配：</span>
                    <span :class="job.industryMatch ? 'text-green-600' : 'text-red-600'">
                      {{ job.industryMatch ? '✓ 符合意向' : '✗ 不完全匹配' }}
                    </span>
                  </div>
                  <div>
                    <span class="font-medium">地点匹配：</span>
                    <span :class="job.locationMatch ? 'text-green-600' : 'text-red-600'">
                      {{ job.locationMatch ? '✓ 符合意向' : '✗ 不完全匹配' }}
                    </span>
                  </div>
                  <div>
                    <span class="font-medium">技能匹配：</span>
                    <span :class="job.skillMatch > 0.5 ? 'text-green-600' : 'text-yellow-600'">
                      {{ (job.skillMatch * 100).toFixed(0) }}% 匹配
                    </span>
                  </div>
                </div>

                <!-- 缺失技能提示 -->
                <div v-if="job.missingSkills && job.missingSkills.length > 0" class="bg-yellow-50 border border-yellow-200 rounded p-3">
                  <div class="flex items-center text-yellow-800 mb-2">
                    <i class="fa fa-lightbulb-o mr-2"></i>
                    <span class="font-medium">技能提升建议</span>
                  </div>
                  <div class="flex flex-wrap gap-1">
                    <Tag
                        v-for="skill in job.missingSkills"
                        :key="skill"
                        color="orange"
                    >
                      {{ skill }}
                    </Tag>
                  </div>
                </div>
              </div>
            </div>

            <!-- 加载更多 - 修复分页显示 -->
            <div v-if="matchedJobs.length > displayedJobs.length && !loading" class="mt-6 text-center">
              <Button type="dashed" @click="loadMore" :loading="loadingMore">
                {{ loadingMore ? '加载中...' : `加载更多 (${matchedJobs.length - displayedJobs.length})` }}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { Input, Select, Button, Tag, Badge, message } from 'ant-design-vue'
import { jobMatchingApi } from '@/services/jobMatching'

export default {
  name: 'JobMatching',
  components: {
    Input,
    Select,
    Button,
    Tag,
    Badge
  },
  data() {
    return {
      loading: false,
      loadingMore: false,
      updatingPreference: false,
      autoMatch: true, // 默认开启自动匹配
      newSkill: '',
      userId: 1,
      allJobs: [], // 所有岗位数据
      matchedJobs: [], // 匹配到的岗位
      displayedJobs: [], // 当前显示的岗位
      pageSize: 3,
      currentPage: 1,
      userSkills: ['JavaScript', 'HTML/CSS', 'Vue', 'Node.js', 'Java', 'MySQL'],
      careerPreference: {
        position: '后端开发工程师',
        industry: '游戏',
        location: '北京'
      }
    }
  },
  computed: {
    totalJobs() {
      return this.matchedJobs.length
    }
  },
  watch: {
    // 监听求职意向变化，自动重新匹配
    careerPreference: {
      handler(newVal) {
        if (this.autoMatch) {
          console.log('求职意向变化，自动重新匹配:', newVal)
          this.performMatching()
        }
      },
      deep: true
    }
  },
  async mounted() {
    await this.initPage()
  },
  methods: {
    // 初始化页面
    async initPage() {
      // 先加载用户偏好
      await this.loadUserPreference()
      // 然后加载岗位数据并匹配
      await this.loadAllJobs()
      await this.performMatching()
    },

    // 加载用户偏好
    async loadUserPreference() {
      try {
        const response = await jobMatchingApi.getCareerPreference(this.userId)
        if (response.data) {
          this.careerPreference = {
            position: response.data.expectedPosition || '后端开发工程师',
            industry: response.data.expectedIndustry || '游戏',
            location: response.data.expectedLocation || '北京'
          }
        }
      } catch (error) {
        console.error('加载用户偏好失败:', error)
        // 使用默认值
      }
    },

    // 加载所有岗位数据
    async loadAllJobs() {
      try {
        const response = await jobMatchingApi.getJobMatchesByUser(this.userId)
        this.allJobs = response.data.matchedJobs || []
        console.log('加载到所有岗位:', this.allJobs.length)
      } catch (error) {
        console.error('加载岗位数据失败:', error)
        // 使用完整的演示数据
        this.allJobs = this.getAllDemoJobs()
      }
    },

    // 执行匹配逻辑 - 修复版本
    async performMatching() {
      this.loading = true
      try {
        console.log('开始匹配，意向:', this.careerPreference, '技能:', this.userSkills)

        // 基于当前意向和技能进行综合匹配
        this.matchedJobs = this.allJobs.map(job => {
          // 技能匹配度计算
          const matchedSkills = job.requiredSkills.filter(skill =>
              this.userSkills.includes(skill)
          )
          const skillMatch = matchedSkills.length / job.requiredSkills.length

          // 意向匹配度计算
          const industryMatch = job.industry === this.careerPreference.industry
          const positionMatch = job.jobTitle.includes(this.careerPreference.position) ||
              this.careerPreference.position.includes(job.jobTitle)
          const locationMatch = !this.careerPreference.location ||
              job.location === this.careerPreference.location

          // 综合匹配度计算（技能权重60%，意向权重40%）
          const preferenceMatchScore = (industryMatch ? 0.4 : 0) +
              (positionMatch ? 0.3 : 0) +
              (locationMatch ? 0.3 : 0)

          const finalMatchScore = (skillMatch * 0.6) + (preferenceMatchScore * 0.4)

          return {
            ...job,
            matchScore: finalMatchScore,
            matchedSkills,
            skillMatch,
            missingSkills: job.requiredSkills.filter(skill =>
                !this.userSkills.includes(skill)
            ),
            positionMatch,
            industryMatch,
            locationMatch
          }
        })
            .filter(job => job.matchScore > 0.2) // 过滤掉匹配度过低的岗位
            .sort((a, b) => b.matchScore - a.matchScore) // 按匹配度排序

        console.log('匹配结果:', this.matchedJobs.length, '个岗位')

        this.displayedJobs = this.matchedJobs.slice(0, this.pageSize)
        this.currentPage = 1

        if (this.matchedJobs.length > 0) {
          const topMatch = this.matchedJobs[0]
          message.success(`找到 ${this.matchedJobs.length} 个匹配岗位，最高匹配度 ${(topMatch.matchScore * 100).toFixed(0)}%`)
        } else {
          message.warning('未找到匹配的岗位，请调整求职意向或添加更多技能')
        }

      } catch (error) {
        console.error('匹配失败:', error)
        message.error('匹配失败，请重试')
      } finally {
        this.loading = false
      }
    },

    // 处理求职意向变化
    handlePreferenceChange() {
      console.log('求职意向变化，重新匹配...')
      this.autoMatch = true
      // 不需要手动调用 performMatching，watch 会自动处理
    },

    // 加载岗位匹配（手动触发）
    async loadJobMatches() {
      this.autoMatch = false
      await this.performMatching()
    },

    // 加载更多
    async loadMore() {
      this.loadingMore = true
      try {
        await new Promise(resolve => setTimeout(resolve, 500))
        this.currentPage++
        const startIndex = (this.currentPage - 1) * this.pageSize
        const endIndex = startIndex + this.pageSize
        const newJobs = this.matchedJobs.slice(startIndex, endIndex)
        this.displayedJobs = [...this.displayedJobs, ...newJobs]
        console.log(`加载了 ${newJobs.length} 个岗位，当前显示 ${this.displayedJobs.length} 个`)
      } catch (error) {
        console.error('加载更多失败:', error)
        message.error('加载失败')
      } finally {
        this.loadingMore = false
      }
    },

    // 更新职业偏好
    async updateCareerPreference() {
      this.updatingPreference = true
      try {
        const preferenceDTO = {
          expectedPosition: this.careerPreference.position,
          expectedIndustry: this.careerPreference.industry,
          expectedLocation: this.careerPreference.location
        }
        await jobMatchingApi.updateCareerPreference(this.userId, preferenceDTO)
        message.success('求职意向更新成功')
        // 更新成功后重新匹配
        await this.performMatching()
      } catch (error) {
        console.error('更新职业偏好失败:', error)
        message.success('求职意向已更新（演示模式）')
        // 即使API失败也重新匹配
        await this.performMatching()
      } finally {
        this.updatingPreference = false
      }
    },

    // 添加技能
    addSkill() {
      if (this.newSkill.trim() && !this.userSkills.includes(this.newSkill.trim())) {
        this.userSkills.push(this.newSkill.trim())
        this.newSkill = ''
        message.success('技能添加成功')
        // 技能变化时重新匹配
        this.performMatching()
      } else if (this.userSkills.includes(this.newSkill.trim())) {
        message.warning('该技能已存在')
      }
    },

    // 删除技能
    removeSkill(index) {
      this.userSkills.splice(index, 1)
      message.success('技能删除成功')
      // 技能变化时重新匹配
      this.performMatching()
    },

    // 申请岗位
    applyJob(job) {
      message.success(`已申请 ${job.companyName} 的 ${job.jobTitle} 岗位`)
    },

    // 获取匹配度颜色
    getMatchScoreColor(score) {
      if (score >= 0.8) return 'success'
      if (score >= 0.6) return 'processing'
      if (score >= 0.4) return 'warning'
      return 'error'
    },

    // 完整的演示数据
    getAllDemoJobs() {
      return jobMatchingApi.getAllDemoJobs()
    }
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

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>