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

    <!-- 核心内容区：改为左右分栏布局 -->
    <main class="container mx-auto px-4 py-8">
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 max-w-6xl mx-auto">
        <!-- 左侧：输入区域 -->
        <div class="space-y-6">
          <!-- 情绪输入卡片 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 md:p-8 border border-gray-100">
            <div class="flex items-center mb-6">
              <div class="w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center mr-3">
                <span class="text-white text-sm font-bold">💬</span>
              </div>
              <h2 class="text-2xl font-bold text-gray-800">分享你的感受</h2>
            </div>

            <a-textarea
                v-model:value="userText"
                placeholder="比如：今天学框架学不会，好焦虑...&#10;或者：连续刷题没思路，有点挫败..."
                rows="6"
                @keydown.enter.prevent="handleSubmitEmotion"
                :disabled="isLoading"
                class="custom-textarea mb-6"
            ></a-textarea>

            <a-button
                type="primary"
                block
                size="large"
                @click="handleSubmitEmotion"
                :disabled="!userText.trim() || isLoading"
                :loading="isLoading"
                class="send-button h-12 text-lg font-semibold"
            >
              <template #loading>
                <span class="flex items-center">
                  <a-spin size="small" class="mr-2" />
                  分析中...
                </span>
              </template>
              <span v-if="!isLoading">
                <i class="fas fa-paper-plane mr-2"></i>
                发送并获取建议
              </span>
            </a-button>

            <p class="text-xs text-gray-500 mt-4 text-center flex items-center justify-center">
              <i class="fas fa-shield-alt mr-1"></i>
              AI会严格保护你的隐私，所有内容仅用于情绪分析
            </p>
          </div>

          <!-- 使用指南 -->
          <div class="bg-gradient-to-r from-purple-50 to-pink-50 rounded-2xl shadow-lg p-6 border border-purple-200">
            <h3 class="text-lg font-semibold text-purple-800 mb-3 flex items-center">
              <i class="fas fa-lightbulb mr-2"></i>
              使用小贴士
            </h3>
            <ul class="text-sm text-gray-600 space-y-2">
              <li class="flex items-start">
                <i class="fas fa-check text-green-500 mr-2 mt-1"></i>
                详细描述你的感受和具体情境
              </li>
              <li class="flex items-start">
                <i class="fas fa-check text-green-500 mr-2 mt-1"></i>
                AI会分析情绪并提供专业建议
              </li>
              <li class="flex items-start">
                <i class="fas fa-check text-green-500 mr-2 mt-1"></i>
                所有对话内容都会严格保密
              </li>
            </ul>
          </div>
        </div>

        <!-- 右侧：对话历史区域 -->
        <div class="space-y-6">
          <!-- 空状态 -->
          <div
              v-if="!lastUserQuestion && !emotionResult"
              class="bg-gradient-to-br from-gray-50 to-blue-50 rounded-2xl p-8 text-center border border-gray-200 h-full flex flex-col justify-center items-center min-h-[400px]"
          >
            <div class="text-6xl mb-4">🤗</div>
            <h3 class="text-xl font-semibold text-gray-700 mb-2">欢迎来到心理陪伴空间</h3>
            <p class="text-gray-500 max-w-sm">在这里，你可以畅所欲言，AI会用心倾听并给你温暖的建议</p>
          </div>

          <!-- 用户问题卡片 -->
          <transition name="slide-up">
            <div
                class="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-2xl shadow-lg p-6 border border-blue-200"
                v-if="lastUserQuestion"
            >
              <div class="flex items-start mb-4">
                <div class="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center mr-3 flex-shrink-0">
                  <i class="fas fa-user text-white text-xs"></i>
                </div>
                <div class="flex-1">
                  <h3 class="text-lg font-semibold text-blue-800 mb-1">你的感受</h3>
                  <div class="user-question-content text-gray-700 text-base leading-relaxed p-4 bg-white rounded-xl border border-blue-100 shadow-sm">
                    {{ lastUserQuestion }}
                  </div>
                  <div class="flex justify-end mt-2">
                    <span class="text-xs text-blue-600 bg-blue-100 px-2 py-1 rounded-full">
                      {{ new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' }) }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </transition>

          <!-- AI回应卡片 -->
          <transition name="slide-up">
            <div
                class="bg-gradient-to-r from-green-50 to-emerald-50 rounded-2xl shadow-lg p-6 border border-green-200"
                v-if="emotionResult"
            >
              <div class="flex items-start mb-4">
                <div class="w-8 h-8 bg-green-500 rounded-full flex items-center justify-center mr-3 flex-shrink-0">
                  <i class="fas fa-robot text-white text-xs"></i>
                </div>
                <div class="flex-1">
                  <div class="flex items-center justify-between mb-3">
                    <h3 class="text-lg font-semibold text-green-800">AI心理陪伴</h3>
                    <a-button
                        type="link"
                        size="small"
                        @click="copyToClipboard(emotionResult.encouragement)"
                        class="copy-button text-green-600 hover:text-green-700"
                    >
                      <i class="fas fa-copy mr-1"></i>
                      复制
                    </a-button>
                  </div>

                  <!-- 情绪标签 -->
                  <div class="flex items-center mb-4 flex-wrap">
                    <span class="text-gray-600 text-sm mr-2">情绪识别：</span>
                    <a-tag
                        :color="getEmotionColor(emotionResult.emotion)"
                        class="emotion-tag text-sm px-3 py-1 rounded-full border-0 font-medium"
                    >
                      <i class="fas fa-heart mr-1"></i>
                      {{ emotionResult.emotion || '未知情绪' }}
                    </a-tag>
                  </div>

                  <!-- 激励文案 -->
                  <div class="encouragement-content text-gray-800 text-base leading-relaxed p-4 bg-white rounded-xl border border-green-100 shadow-sm">
                    <div v-html="formatEncouragement(emotionResult.encouragement)"></div>
                  </div>

                  <div class="flex justify-between items-center mt-4">
                    <span class="text-xs text-gray-500 flex items-center">
                      <i class="far fa-clock mr-1"></i>
                      {{ emotionResult.timestamp ? new Date(emotionResult.timestamp).toLocaleString('zh-CN') : '刚刚' }}
                    </span>
                    <div class="flex space-x-2">
                      <a-tooltip title="这条建议很有帮助">
                        <a-button
                            type="link"
                            size="small"
                            class="text-gray-400 hover:text-green-500"
                            @click="handleFeedback('helpful')"
                        >
                          <i class="far fa-thumbs-up"></i>
                        </a-button>
                      </a-tooltip>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import request from '@/utils/request';

// 状态管理
const userText = ref('');
const emotionResult = ref(null);
const isLoading = ref(false);
const userId = ref('');
const lastUserQuestion = ref('');

// 情绪-颜色映射
const emotionColorMap = {
  中性: 'blue',
  积极: 'green',
  焦虑: 'orange',
  挫败: 'red',
  压力: 'volcano',
  困惑: 'purple',
  悲伤: 'cyan',
  疲惫: 'geekblue'
};

// 处理多个情绪的颜色显示
const getEmotionColor = (emotionText) => {
  if (!emotionText) return 'blue';

  // 如果包含多个情绪，取第一个情绪的颜色
  const firstEmotion = emotionText.split('、')[0];
  return emotionColorMap[firstEmotion] || 'blue';
};


// 格式化鼓励文本的函数
const formatEncouragement = (text) => {
  if (!text) return '<p>暂无回应</p>';

  // 移除数字编号格式（如 1. 2. 3.）
  let formatted = text.replace(/(\d+)\.\s*/g, '<br><strong>$1.</strong> ');

  // 处理加粗文本
  formatted = formatted.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

  // 处理换行
  formatted = formatted.replace(/\n/g, '<br>');

  // 添加段落间距
  formatted = formatted.replace(/<br><br>/g, '</p><p>');

  // 确保以p标签包裹
  if (!formatted.startsWith('<p>')) {
    formatted = '<p>' + formatted + '</p>';
  }

  return formatted;
};

// 复制到剪贴板功能
const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text);
    message.success('已复制到剪贴板');
  } catch (err) {
    // 降级方案
    const textArea = document.createElement('textarea');
    textArea.value = text;
    document.body.appendChild(textArea);
    textArea.select();
    document.execCommand('copy');
    document.body.removeChild(textArea);
    message.success('已复制到剪贴板');
  }
};

// 反馈功能
const handleFeedback = (type) => {
  if (type === 'helpful') {
    message.success('感谢你的反馈！我们会继续努力～');
  }
};

// 初始化
const init = async () => {
  await nextTick();
  const userInfo = localStorage.getItem('user');
  if (userInfo) {
    const user = JSON.parse(userInfo);
    userId.value = user.id;
  }
};

// 提交情绪分析
const handleSubmitEmotion = async () => {
  const text = userText.value.trim();
  if (!text || isLoading.value) return;

  try {
    isLoading.value = true;
    lastUserQuestion.value = text;

    console.log('[psychology] 发送情绪分析请求:', text);

    const res = await request.post('/api/psychology', {
      text,
      userId: userId.value || ''
    });

    console.log('[psychology] 后端响应:', res);

    if (res.success) {
      setTimeout(async () => {
        emotionResult.value = res.data || {};
        userText.value = '';
        await nextTick();
      }, 100);
    } else {
      message.error(res.msg || '情绪分析服务暂时不可用，请稍后重试');
    }

  } catch (err) {
    console.error('[psychology] 请求错误:', err);
    message.error(err.response?.data?.msg || '情绪分析请求失败，请重试');
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  init();
});
</script>

<style scoped>
.psychology-companion-page {
  padding-top: 80px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  min-height: calc(100vh - 80px);
}

.page-header {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
}

.page-header h1 {
  background: #000000;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 自定义文本区域样式 */
.custom-textarea {
  border-radius: 12px;
  border: 2px solid #e2e8f0;
  transition: all 0.3s ease;
  font-size: 16px;
  line-height: 1.6;
  resize: none;
}

.custom-textarea:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.custom-textarea:hover {
  border-color: #cbd5e1;
}

/* 发送按钮样式 */
.send-button {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.send-button:hover:not(:disabled) {
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  transform: translateY(-2px);
  box-shadow: 0 10px 20px -5px rgba(59, 130, 246, 0.4);
}

.send-button:disabled {
  background: #e6f7ff ;
  border-color:#7C3AED;
  transform: none;
  box-shadow: none;
}

/* 复制按钮样式 */
.copy-button {
  border: none;
  height: auto;
  padding: 4px 8px;
}

/* 情绪标签样式 */
.emotion-tag {
  border: none !important;
}

/* 动画效果 */
.slide-up-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.slide-up-enter-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-up-enter-to {
  opacity: 1;
  transform: translateY(0);
}

/* 卡片悬停效果 */
.bg-white {
  transition: all 0.3s ease;
}

.bg-white:hover {
  transform: translateY(-2px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

/* 用户问题和AI回应卡片样式 */
.user-question-content,
.encouragement-content {
  line-height: 1.7;
  word-break: break-word;
}

.encouragement-content :deep(p) {
  margin-bottom: 1em;
}

.encouragement-content :deep(strong) {
  color: #059669;
  font-weight: 600;
}

.encouragement-content :deep(br) {
  content: "";
  display: block;
  margin-top: 0.5em;
}

/* 响应式调整 */
@media (max-width: 1024px) {
  .grid-cols-1.lg\:grid-cols-2 {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
}

@media (max-width: 768px) {
  .psychology-companion-page {
    padding-top: 60px;
    min-height: calc(100vh - 60px);
  }

  .page-header {
    padding: 3rem 1rem;
  }

  .page-header h1 {
    font-size: 2rem;
  }

  main {
    padding: 1rem;
  }

  .bg-white,
  .bg-gradient-to-r {
    padding: 1.5rem;
  }

  .custom-textarea {
    font-size: 14px;
  }
}

/* 加载状态样式 */
:deep(.ant-spin) {
  color: #fff;
}

:deep(.ant-spin-dot-item) {
  background-color: #fff;
}

/* 确保图标正确显示 */
:deep(.fas),
:deep(.far),
:deep(.fab) {
  font-family: 'Font Awesome 5 Free';
  font-weight: 900;
}

:deep(.far) {
  font-weight: 400;
}
</style>

<!-- 在 public/index.html 或 main.js 中添加 Font Awesome -->
<!--
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
-->