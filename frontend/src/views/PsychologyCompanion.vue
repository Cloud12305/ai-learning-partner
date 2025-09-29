<template>
  <div class="psychology-companion-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">AI心理陪伴</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto">
          学习压力大？情绪低落？和AI聊聊你的感受，获取专属激励和调整建议～
        </p>
      </div>
    </div>

    <!-- 核心内容区 -->
    <main class="container mx-auto px-4 py-12">
      <div class="grid md:grid-cols-3 gap-8 max-w-6xl mx-auto">
        <!-- 左侧：情绪输入与激励结果 -->
        <div class="md:col-span-1 space-y-6">
          <!-- 情绪输入卡片 -->
          <div class="bg-white rounded-xl shadow-lg p-6">
            <h2 class="text-xl font-semibold mb-4">分享你的感受</h2>
            <a-textarea
                v-model:value="userText"
                placeholder="比如“今天学框架学不会，好焦虑”“连续刷题没思路，有点挫败”"
                rows="5"
                @keyup.enter="handleSubmitEmotion"
                :disabled="isLoading"
                class="mb-4"
            />
            <a-button
                type="primary"
                block
                size="large"
                @click="handleSubmitEmotion"
                :disabled="!userText.trim() || isLoading"
                :loading="isLoading"
            >
              发送并获取建议
            </a-button>
            <p class="text-xs text-gray-500 mt-3 text-center">
              AI会严格保护你的隐私，所有内容仅用于情绪分析
            </p>
          </div>

          <!-- 激励结果卡片 -->
          <div
              class="bg-white rounded-xl shadow-lg p-6"
              v-if="emotionResult"
              animation="fadeIn 0.3s ease-in-out"
          >
            <h2 class="text-xl font-semibold mb-4">AI的回应</h2>
            <!-- 情绪标签 -->
            <div class="flex items-center mb-4">
              <span class="text-gray-600">AI识别到你的情绪：</span>
              <a-tag
                  :color="emotionColorMap[emotionResult.emotion]"
                  class="ml-3 text-base"
              >
                {{ emotionResult.emotion }}
              </a-tag>
            </div>
            <!-- 激励文案 -->
            <div class="encouragement-content text-gray-800 line-height-2 mb-4">
              {{ emotionResult.encouragement }}
            </div>
            <!-- 附加资源（如学习案例、调整方法） -->
            <div v-if="emotionResult.encouragement.includes('链接')" class="mt-4 pt-4 border-t border-gray-100">
              <a
                  href="javascript:;"
                  class="text-primary flex items-center hover:underline"
                  @click="message.info('已为你推荐《框架入门实战指南》，可在“我的学习-资源库”查看')"
              >
                <i class="fa fa-link mr-2"></i> 查看相关学习资源/调整建议
              </a>
            </div>
          </div>
        </div>

        <!-- 右侧：情绪趋势图 -->
        <div class="md:col-span-2">
          <div class="bg-white rounded-xl shadow-lg p-6 h-full">
            <h2 class="text-xl font-semibold mb-6">7天情绪趋势</h2>
            <div class="chart-container" style="height: 420px; width: 100%;">
              <ECharts :option="chartOption" ref="emotionChart" />
            </div>
            <p class="text-xs text-gray-500 mt-4 text-center">
              记录你的情绪变化，AI会根据趋势调整激励策略
            </p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { message, TextArea, Button, Tag} from 'ant-design-vue';
import { ECharts } from 'vue-echarts';
import { use } from 'echarts/core';
// 引入echarts组件
import { LineChart, Line } from 'echarts/charts';
import { XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'echarts/components';
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components';


// 注册echarts组件
use([
  LineChart, Line,
  XAxis, YAxis, CartesianGrid, Tooltip, Legend,
  GridComponent, TooltipComponent, LegendComponent
]);

// 状态管理
const userText = ref(''); // 用户输入的情绪文本
const emotionResult = ref(null); // 情感分析结果
const isLoading = ref(false); // 加载状态
const emotionChart = ref(null); // echarts实例
const userId = ref(''); // 用户ID（从登录态获取）

// 情绪-颜色映射（与Ant Design主题匹配）
const emotionColorMap = {
  中性: 'gray',
  积极: 'green',
  焦虑: 'orange',
  挫败: 'red'
};

// 情绪趋势图配置
const chartOption = ref({
  tooltip: {
    trigger: 'axis',
    formatter: (params) => {
      const emotionMap = { 0: '中性', 1: '积极', 2: '焦虑', 3: '挫败' };
      return `${params[0].name}<br/>情绪状态：${emotionMap[params[0].value]}`;
    },
    backgroundColor: 'rgba(255,255,255,0.9)',
    borderColor: '#eee',
    borderWidth: 1,
    textStyle: { color: '#333' }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '15%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: [], // 7天日期（如["10-01", "10-02"]）
    axisLabel: {
      interval: 0,
      rotate: 30 // 日期横向显示时旋转，避免重叠
    }
  },
  yAxis: {
    type: 'value',
    min: 0,
    max: 3,
    axisLabel: {
      formatter: (value) => {
        const emotionMap = { 0: '中性', 1: '积极', 2: '焦虑', 3: '挫败' };
        return emotionMap[value] || '';
      }
    },
    splitLine: {
      lineStyle: { color: '#f0f0f0' }
    }
  },
  series: [
    {
      name: '情绪趋势',
      type: 'line',
      data: [], // 每天的情绪值（0-3）
      symbol: 'circle',
      symbolSize: 10,
      itemStyle: {
        color: (params) => {
          const colorMap = { 0: '#888888', 1: '#52c41a', 2: '#fa8c16', 3: '#f5222d' };
          return colorMap[params.data] || '#888888';
        }
      },
      lineStyle: {
        color: '#1890ff',
        width: 2
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 8,
          shadowColor: 'rgba(24, 144, 255, 0.3)'
        }
      }
    }
  ]
});

// 初始化：获取用户ID + 加载情绪历史
const init = () => {
  // 1. 获取用户ID（登录态判断）
  const userInfo = localStorage.getItem('userInfo');
  if (userInfo) {
    userId.value = JSON.parse(userInfo).id;
  } else {
    message.warning("未登录，无法查看情绪历史，登录后可同步记录");
    return;
  }

  // 2. 加载7天情绪历史（后端返回）
  fetchEmotionHistory();
};

// 提交情绪文本，调用后端情感分析API
const handleSubmitEmotion = async () => {
  const text = userText.value.trim();
  if (!text) return;

  try {
    isLoading.value = true;
    const res = await request.post('/api/psychology', {
      text,
      user_id: userId.value
    });

    // 保存分析结果
    emotionResult.value = res.data;
    // 清空输入框
    userText.value = '';
    // 刷新情绪趋势图
    fetchEmotionHistory();
  } catch (err) {
    message.error(err.response?.data?.msg || "情绪分析请求失败，请重试");
  } finally {
    isLoading.value = false;
  }
};

// 获取7天情绪历史，更新趋势图
const fetchEmotionHistory = async () => {
  try {
    const res = await request.get('/api/psychology/history', {
      params: { user_id: userId.value }
    });
    const history = res.data.history || []; // 格式：[{date: "10-01", value: 2}, ...]

    // 更新图表数据
    chartOption.value.xAxis.data = history.map(item => item.date);
    chartOption.value.series[0].data = history.map(item => item.value);
    // 刷新图表
    emotionChart.value?.setOption(chartOption.value);
  } catch (err) {
    console.error("加载情绪历史失败：", err);
    message.warning("无法加载情绪历史，仅展示当前分析结果");
  }
};

// 页面挂载时初始化
onMounted(() => {
  init();
});
</script>

<style scoped>
.psychology-companion-page {
  padding-top: 80px; /* 与导航栏高度匹配 */
  background-color: #fafafa;
  min-height: calc(100vh - 80px);
}

.page-header {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
}

.chart-container {
  overflow: hidden;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>