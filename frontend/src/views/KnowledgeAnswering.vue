<template>
  <div class="knowledge-answering-page">
    <!-- 页面头部（与项目风格统一） -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">AI知识答疑</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto">
          遇到学习疑问？向AI提问吧！支持知识点查询、例题解析，还能追问关联内容～
        </p>
      </div>
    </div>

    <!-- 核心问答区 -->
    <main class="container mx-auto px-4 py-12">
      <div class="max-w-4xl mx-auto bg-white rounded-xl shadow-lg p-8">
        <!-- 问答历史 -->
        <div class="qa-history mb-8" v-if="qaHistory.length > 0">
          <h2 class="text-xl font-semibold mb-4">问答记录</h2>
          <div
              class="qa-item border-b border-gray-100 pb-6 mb-6 last:border-0 last:mb-0 last:pb-0"
              v-for="(item, idx) in qaHistory"
              :key="idx"
          >
            <!-- 用户问题 -->
            <div class="flex items-start mb-4">
              <div class="user-avatar w-8 h-8 rounded-full bg-primary/10 flex items-center justify-center text-primary mr-3">
                <i class="fa fa-user"></i>
              </div>
              <div class="user-question bg-gray-50 p-4 rounded-lg flex-1">
                {{ item.question }}
              </div>
            </div>
            <!-- AI回答 -->
            <div class="flex items-start ml-11">
              <div class="ai-avatar w-8 h-8 rounded-full bg-green-100 flex items-center justify-center text-green-600 mr-3">
                <i class="fa fa-robot"></i>
              </div>
              <div class="ai-answer bg-green-50 p-4 rounded-lg flex-1">
                <!-- 结构化答案（定义+例题） -->
                <div v-if="item.answer[0]?.kpName">
                  <p class="mb-2"><strong class="text-green-700">知识点：</strong>{{ item.answer[0].kpName }}</p>
                  <p class="mb-2 text-gray-700">{{ item.answer[0].definition }}</p>
                  <!-- 例题（如有） -->
                  <div v-if="item.answer[0].exampleTitle" class="mt-4 pt-4 border-t border-gray-200">
                    <p class="mb-1"><strong class="text-green-700">例题：</strong>{{ item.answer[0].exampleTitle }}</p>
                    <p class="mb-2 text-sm text-gray-600">{{ item.answer[0].exampleContent }}</p>
                    <p class="text-sm text-gray-700"><strong>解析：</strong>{{ item.answer[0].exampleSolution }}</p>
                  </div>
                  <!-- 追问按钮 -->
                  <button
                      class="mt-4 text-primary text-sm hover:underline"
                      @click="handleFollowUp(item.answer[0].kpName)"
                  >
                    追问关于「{{ item.answer[0].kpName }}」的问题
                  </button>
                </div>
                <!-- 追问结果（知识点对比） -->
                <div v-else-if="item.answer[0]?.kp1Name">
                  <p class="mb-2"><strong class="text-green-700">关系类型：</strong>{{ item.answer[0].relationType }}</p>
                  <p class="mb-2 text-gray-700"><strong>{{ item.answer[0].kp1Name }}：</strong>{{ item.answer[0].kp1Def }}</p>
                  <p class="text-gray-700"><strong>{{ item.answer[0].kp2Name }}：</strong>{{ item.answer[0].kp2Def }}</p>
                </div>
                <!-- 无结果提示 -->
                <div v-else class="text-red-500">
                  {{ item.answer.error || "未找到相关知识点，请尝试重新表述问题（如“什么是二叉树？”）" }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 问题输入区 -->
        <div class="qa-input-container">
          <h2 class="text-xl font-semibold mb-4">提问AI</h2>
          <div class="flex flex-col md:flex-row gap-4">
            <a-textarea
                v-model:value="currentQuestion"
                placeholder="请输入你的学习疑问（如“什么是链表？”“二叉树和链表的区别？”）"
                rows="3"
                class="flex-1"
                @keyup.enter="handleSubmit"
                :disabled="isLoading"
            />
            <a-button
                type="primary"
                size="large"
                @click="handleSubmit"
                :disabled="!currentQuestion.trim() || isLoading"
                :loading="isLoading"
                class="md:w-40"
            >
              提交提问
            </a-button>
          </div>
          <p class="text-xs text-gray-500 mt-2">
            提示：支持查询数据结构、编程语言、专业课程等知识点，可追问关联内容
          </p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { message } from 'ant-design-vue';
import request from '@/utils/request';
// 状态管理
const currentQuestion = ref(''); // 当前输入问题
const qaHistory = ref([]); // 问答历史
const isLoading = ref(false); // 加载状态
const followUpContext = ref(''); // 追问上下文（上一轮知识点）
const userId = ref(''); // 用户ID（从登录态获取）

// 初始化：获取用户ID（登录态/游客ID）
const initUserId = () => {
  const userInfo = localStorage.getItem('userInfo');
  if (userInfo) {
    userId.value = JSON.parse(userInfo).id; // 登录用户：从localStorage获取ID
  } else {
    // 未登录：生成临时游客ID
    userId.value = `guest_${Date.now()}`;
    message.warning("未登录，将以游客身份使用答疑功能，登录后可保存问答记录");
  }
};

// 提交问题到后端
const handleSubmit = async () => {
  const question = currentQuestion.value.trim();
  if (!question) return;

  try {
    isLoading.value = true;
    // 调用后端问答API（与之前弹窗逻辑一致）
    const res = await request.get('/api/qa', {
      params: {
        question,
        user_id: userId.value,
        context_kp: followUpContext.value || undefined // 携带追问上下文
      }
    });

    // 新增问答记录到历史
    qaHistory.value.push({
      question,
      answer: res.data.answer
    });

    // 更新追问上下文（供下一轮使用）
    followUpContext.value = res.data.last_context_kp || '';

    // 清空输入框
    currentQuestion.value = '';

    // 滚动到最新问答记录（优化体验）
    setTimeout(() => {
      const lastItem = document.querySelector('.qa-item:last-child');
      if (lastItem) {
        lastItem.scrollIntoView({ behavior: 'smooth', block: 'end' });
      }
    }, 100);
  } catch (err) {
    message.error(err.response?.data?.msg || "问答请求失败，请稍后重试");
  } finally {
    isLoading.value = false;
  }
};

// 处理追问：自动填充上一轮知识点
const handleFollowUp = (kpName) => {
  followUpContext.value = kpName;
  currentQuestion.value = `关于「${kpName}」的更多问题？`;
  // 聚焦输入框
  document.querySelector('.qa-input-container textarea').focus();
};

// 页面加载时初始化用户ID
initUserId();
</script>

<style scoped>
.knowledge-answering-page {
  padding-top: 80px; /* 与导航栏高度匹配，避免遮挡 */
  background-color: #fafafa;
  min-height: calc(100vh - 80px);
}

.page-header {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
}

.qa-history {
  max-height: 600px;
  overflow-y: auto;
  padding-right: 8px;
}

.qa-item {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.qa-input-container {
  background-color: #fff;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
</style>