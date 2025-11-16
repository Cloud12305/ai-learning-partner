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

    <!-- 核心内容区：移除图表后，调整为单栏布局（居中显示） -->
    <main class="container mx-auto px-4 py-12">
      <div class="max-w-2xl mx-auto">
        <!-- 情绪输入与激励结果：合并为单栏，居中展示 -->
        <div class="space-y-8">
          <!-- 情绪输入卡片 -->
          <div class="bg-white rounded-xl shadow-lg p-6 md:p-8">
            <h2 class="text-xl font-semibold mb-4">分享你的感受</h2>
            <a-textarea
                v-model:value="userText"
                placeholder="比如“今天学框架学不会，好焦虑”“连续刷题没思路，有点挫败”"
                rows="5"

                @keydown.enter.prevent="handleSubmitEmotion"

                :disabled="isLoading"
                class="mb-6"
            ></a-textarea>
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
            <p class="text-xs text-gray-500 mt-4 text-center">
              AI会严格保护你的隐私，所有内容仅用于情绪分析
            </p>
          </div>

          <!-- 激励结果卡片：延迟渲染避免实例冲突 -->
          <transition name="fade-in">
            <div
                class="bg-white rounded-xl shadow-lg p-6 md:p-8"
                v-if="emotionResult"
            >
              <h2 class="text-xl font-semibold mb-4">AI的回应</h2>
              <!-- 情绪标签：添加默认值容错 -->
              <div class="flex items-center mb-6 flex-wrap">
                <span class="text-gray-600">AI识别到你的情绪：</span>
                <a-tag
                    :color="emotionColorMap[emotionResult.emotion || '中性']"
                    class="ml-3 text-base mt-1 md:mt-0"
                >
                  {{ emotionResult.emotion || '未知情绪' }}
                </a-tag>
              </div>
              <!-- 激励文案：增加行高和内边距，提升阅读体验 -->
              <div class="encouragement-content text-gray-800 line-height-2.5 text-base mb-6 p-4 bg-gray-50 rounded-lg">
                {{ emotionResult.encouragement || '请稍后再试～' }}
              </div>
              <!-- 附加资源 -->
              <div v-if="emotionResult.encouragement?.includes('链接')" class="mt-6 pt-6 border-t border-gray-100">
                <a
                    href="javascript:;"
                    class="text-primary flex items-center hover:underline text-base"
                    @click="message.info('已为你推荐《框架入门实战指南》，可在“我的学习-资源库”查看')"
                >
                  <i class="fa fa-link mr-2"></i> 查看相关学习资源/调整建议
                </a>
              </div>
            </div>
          </transition>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
// 1. 基础导入：仅保留核心依赖（移除所有ECharts相关导入）
import { ref, onMounted, nextTick } from 'vue';
import { message } from 'ant-design-vue'; // Ant Design 组件（需全局注册）
import request from '@/utils/request'; // HTTP请求工具（确认路径与项目一致）

// 2. 状态管理：移除ECharts相关状态（emotionChart、echartsInitialized、chartOption等）
const userText = ref(''); // 用户输入的情绪文本
const emotionResult = ref(null); // 情感分析结果
const isLoading = ref(false); // 加载状态
const userId = ref(''); // 用户ID（从登录态获取）

// 3. 情绪-颜色映射（保留，用于AI回应的情绪标签）
const emotionColorMap = {
  中性: 'gray',
  积极: 'green',
  焦虑: 'orange',
  挫败: 'red'
};

// 4. 页面初始化：仅保留登录态判断（移除ECharts初始化逻辑）
const init = async () => {
  await nextTick(); // 等待DOM挂载完成
  // 读取登录态（用于后续接口请求，未登录也可提交情绪）
  const userInfo = localStorage.getItem('user');
  if (userInfo) {
    const user = JSON.parse(userInfo);
    userId.value = user.id;
  } else {
    message.warning("未登录，情绪记录无法同步，登录后可查看历史记录");
  }
};

// 5. 提交情绪文本：保留核心逻辑（移除图表刷新相关代码）
// const handleSubmitEmotion = async () => {
//   const text = userText.value.trim();
//   if (!text || isLoading.value) return;
//
//   try {
//     isLoading.value = true;
//     // 调用后端情感分析API（传递用户ID，未登录则为空）
//     const res = await request.post('psychology', {
//       text,
//       userId: userId.value || ''
//     });
//
//     // 延迟100ms赋值：避免DOM渲染冲突，确保组件实例正常创建
//     setTimeout(async () => {
//       emotionResult.value = res.data?.data || {}; // 容错：避免接口返回空数据
//       userText.value = ''; // 清空输入框
//       await nextTick(); // 等待回应卡片渲染完成
//     }, 100);
//   } catch (err) {
//     // 错误提示：兼容接口返回异常或网络错误
//     message.error(err.response?.data?.msg || "情绪分析请求失败，请重试");
//   } finally {
//     isLoading.value = false; // 无论成功失败，都关闭加载状态
//   }
// };
const handleSubmitEmotion = async () => {
  const text = userText.value.trim();
  if (!text || isLoading.value) return;

  try {
    isLoading.value = true;

    console.log('[psychology] sending request...');

    // Call backend: POST http://localhost:8080/api/psychology
    const res = await request.post('/api/psychology', {
      text,
      userId: userId.value || ''
    });

    console.log('[psychology] backend response:', res);

    // res = { success: true/false, data: { emotion, encouragement }, msg? }

    if (res.success) {
      // small delay is optional
      setTimeout(async () => {
        emotionResult.value = res.data || {};   // ⬅️ use res.data, NOT res.data.data
        userText.value = '';
        await nextTick();
      }, 100);
    } else {
      message.error(res.msg || '情绪分析服务暂时不可用，请稍后重试');
    }

  } catch (err) {
    console.error('[psychology] request error:', err);
    message.error(err.response?.data?.msg || '情绪分析请求失败，请重试');
  } finally {
    isLoading.value = false;
  }
};

// 6. 页面挂载时初始化（仅执行登录态判断）
onMounted(() => {
  init();
});
</script>

<style scoped>
/* 页面基础样式：保留，确保页面布局正常 */
.psychology-companion-page {
  padding-top: 80px; /* 与导航栏高度匹配，可根据实际导航栏调整 */
  background-color: #fafafa;
  min-height: calc(100vh - 80px); /* 确保页面占满屏幕高度 */
}

/* 头部样式：保留渐变背景，提升视觉效果 */
.page-header {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
}

/* 过渡动画：保留，确保AI回应卡片显示时有平滑动画 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 进入动画：与transition组件的name="fade-in"对应 */
.fade-in-enter-from {
  opacity: 0;
  transform: translateY(10px);
}
.fade-in-enter-active {
  animation: fadeIn 0.3s ease-in-out;
}

/* 离开动画（可选保留，确保卡片消失时平滑） */
.fade-in-leave-from {
  opacity: 1;
}
.fade-in-leave-active {
  opacity: 0;
  transform: translateY(10px);
  transition: opacity 0.3s, transform 0.3s;
}

/* 响应式调整：确保移动端体验正常 */
@media (max-width: 768px) {
  .psychology-companion-page {
    padding-top: 60px; /* 移动端导航栏通常更矮，调整顶部间距 */
    min-height: calc(100vh - 60px);
  }
}
</style>

<!-- 重要注释：
1. 确保 Ant Design Vue 已全局注册（main.js 中需添加）：
   import { createApp } from 'vue';
   import Antd from 'ant-design-vue';
   import 'ant-design-vue/dist/antd.css'; // 若使用Vite，需改为 'ant-design-vue/dist/reset.css'
   import App from './App.vue';
   const app = createApp(App);
   app.use(Antd);
   app.mount('#app');

2. 确认 request 工具路径正确：
   若项目中 HTTP 请求工具路径不是 '@/utils/request'，需修改为实际路径（如 '@/api/request'）。

3. 功能说明：
   - 保留核心功能：情绪输入、AI情感分析、AI回应展示、附加资源提示；
   - 移除图表相关功能：7天情绪趋势图、情绪历史加载；
   - 优化布局：从双栏改为单栏居中，提升移动端和桌面端的视觉体验。
-->