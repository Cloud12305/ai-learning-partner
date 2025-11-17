<template>
  <div class="knowledge-qa-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="container mx-auto px-4 py-16 md:py-24">
        <h1 class="text-[clamp(2rem,5vw,3.5rem)] font-bold text-center mb-4">AI知识答疑</h1>
        <p class="text-gray-600 text-center max-w-2xl mx-auto">
          遇到学习疑问？向AI提问吧！支持知识点查询、例题解析，还能生成知识图谱～
        </p>
      </div>
    </div>

    <!-- 核心内容区：左右分栏布局 -->
    <main class="container mx-auto px-4 py-8">
      <div class="grid grid-cols-1 lg:grid-cols-2 gap-8 max-w-7xl mx-auto">
        <!-- 左侧：问答区域 -->
        <div class="space-y-6">
          <!-- 提问卡片 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 md:p-8 border border-gray-100">
            <div class="flex items-center mb-6">
              <div class="w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center mr-3">
                <span class="text-white text-sm font-bold">❓</span>
              </div>
              <h2 class="text-2xl font-bold text-gray-800">提问AI</h2>
            </div>

            <a-textarea
                v-model:value="currentQuestion"
                placeholder="比如：什么是二叉树？&#10;或者：比较一下链表和数组的区别？&#10;或者：解释一下快速排序算法？"
                :rows="4"
                @keydown.enter.prevent="handleSubmit"
                :disabled="isLoading"
                class="custom-textarea mb-6"
            ></a-textarea>

            <a-button
                type="primary"
                block
                size="large"
                @click="handleSubmit"
                :disabled="!currentQuestion.trim() || isLoading"
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
                提交提问
              </span>
            </a-button>

            <p class="text-xs text-gray-500 mt-4 text-center flex items-center justify-center">
              <i class="fas fa-lightbulb mr-1"></i>
              支持编程、算法、数据结构、计算机基础等知识点查询
            </p>
          </div>

          <!-- 问答历史 -->
          <div class="space-y-4" v-if="qaHistory.length > 0">
            <div
                class="bg-white rounded-2xl shadow-lg p-6 border border-gray-200"
                v-for="(item, index) in qaHistory"
                :key="index"
            >
              <!-- 用户问题 -->
              <div class="flex items-start mb-4">
                <div class="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center mr-3 flex-shrink-0">
                  <i class="fas fa-user text-white text-xs"></i>
                </div>
                <div class="flex-1">
                  <div class="user-question-content text-gray-700 text-base leading-relaxed p-4 bg-blue-50 rounded-xl border border-blue-100">
                    {{ item.question }}
                  </div>
                  <div class="flex justify-end mt-2">
                    <span class="text-xs text-blue-600 bg-blue-100 px-2 py-1 rounded-full">
                      {{ formatTime(item.timestamp) }}
                    </span>
                  </div>
                </div>
              </div>

              <!-- AI回答 -->
              <div class="flex items-start mt-4">
                <div class="w-8 h-8 bg-green-500 rounded-full flex items-center justify-center mr-3 flex-shrink-0">
                  <i class="fas fa-robot text-white text-xs"></i>
                </div>
                <div class="flex-1">
                  <div class="ai-answer-content text-gray-800 text-base leading-relaxed p-4 bg-green-50 rounded-xl border border-green-100">
                    <div v-html="formatAnswer(item.answer)"></div>

                    <!-- 追问按钮 -->
                    <div class="mt-4 pt-4 border-t border-green-200">
                      <a-button
                          type="link"
                          size="small"
                          @click="handleFollowUp(item)"
                          class="text-green-600 hover:text-green-700"
                      >
                        <i class="fas fa-comment-dots mr-1"></i>
                        追问相关问题
                      </a-button>

                      <a-button
                          type="link"
                          size="small"
                          @click="copyToClipboard(item.answer)"
                          class="text-green-600 hover:text-green-700 ml-4"
                      >
                        <i class="fas fa-copy mr-1"></i>
                        复制答案
                      </a-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 空状态 -->
          <div
              v-if="qaHistory.length === 0"
              class="bg-gradient-to-br from-gray-50 to-blue-50 rounded-2xl p-8 text-center border border-gray-200"
          >
            <div class="text-6xl mb-4">🤔</div>
            <h3 class="text-xl font-semibold text-gray-700 mb-2">开始你的知识探索</h3>
            <p class="text-gray-500 mb-4">在这里提问，AI会为你详细解答并生成知识图谱</p>
            <div class="text-sm text-gray-600 text-left bg-white p-4 rounded-lg">
              <p class="font-semibold mb-2">试试这些问题：</p>
              <ul class="space-y-1">
                <li>• 什么是二叉树？</li>
                <li>• 比较一下链表和数组的区别</li>
                <li>• 解释一下快速排序算法</li>
                <li>• 什么是面向对象编程？</li>
              </ul>
            </div>
          </div>
        </div>

        <!-- 右侧：知识图谱区域 -->
        <div class="space-y-6">
          <!-- 知识图谱卡片 -->
          <div class="bg-white rounded-2xl shadow-lg p-6 border border-gray-200">
            <div class="flex items-center justify-between mb-6">
              <div class="flex items-center">
                <div class="w-10 h-10 bg-gradient-to-r from-purple-500 to-pink-600 rounded-full flex items-center justify-center mr-3">
                  <span class="text-white text-sm font-bold">🌐</span>
                </div>
                <h2 class="text-2xl font-bold text-gray-800">知识图谱</h2>
              </div>
              <div class="flex space-x-2">
                <a-button
                    v-if="currentGraphData.nodes.length > 0"
                    type="link"
                    size="small"
                    @click="exportGraph"
                    class="text-purple-600"
                >
                  <i class="fas fa-download mr-1"></i>
                  导出
                </a-button>
                <a-button
                    v-if="currentGraphData.nodes.length > 0"
                    type="link"
                    size="small"
                    @click="toggleGraphLayout"
                    class="text-purple-600"
                >
                  <i class="fas fa-sync mr-1"></i>
                  切换布局
                </a-button>
              </div>
            </div>

            <!-- 知识图谱容器 -->
            <div
                ref="graphContainer"
                class="knowledge-graph-container bg-gray-50 rounded-xl border-2 border-dashed border-gray-300 min-h-[500px] relative"
            >
              <!-- 图谱可视化区域 -->
              <div
                  v-if="currentGraphData.nodes.length > 0"
                  ref="graphSvg"
                  class="w-full h-full"
              ></div>

              <!-- 空状态 -->
              <div
                  v-else
                  class="absolute inset-0 flex flex-col items-center justify-center text-gray-400"
              >
                <i class="fas fa-project-diagram text-6xl mb-4"></i>
                <p class="text-lg">提问后将会生成知识图谱</p>
                <p class="text-sm mt-2">可视化展示知识点之间的关系</p>
              </div>

              <!-- 加载状态 -->
              <div
                  v-if="isGeneratingGraph"
                  class="absolute inset-0 flex items-center justify-center bg-white bg-opacity-80 rounded-xl"
              >
                <div class="text-center">
                  <a-spin size="large" />
                  <p class="mt-2 text-gray-600">正在生成知识图谱...</p>
                </div>
              </div>
            </div>

            <!-- 图例 -->
            <div v-if="currentGraphData.nodes.length > 0" class="mt-4 p-4 bg-gray-50 rounded-lg">
              <h4 class="text-sm font-semibold text-gray-700 mb-2">图例说明：</h4>
              <div class="flex flex-wrap gap-4 text-xs">
                <div class="flex items-center">
                  <div class="w-3 h-3 bg-blue-500 rounded-full mr-2"></div>
                  <span>核心知识点</span>
                </div>
                <div class="flex items-center">
                  <div class="w-3 h-3 bg-green-500 rounded-full mr-2"></div>
                  <span>相关知识点</span>
                </div>
                <div class="flex items-center">
                  <div class="w-3 h-3 bg-purple-500 rounded-full mr-2"></div>
                  <span>基础概念</span>
                </div>
                <div class="flex items-center">
                  <div class="w-3 h-3 bg-orange-500 rounded-full mr-2"></div>
                  <span>应用实例</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 知识点详情 -->
          <div
              v-if="selectedNode"
              class="bg-white rounded-2xl shadow-lg p-6 border border-gray-200"
          >
            <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center">
              <i class="fas fa-info-circle mr-2 text-blue-500"></i>
              知识点详情
            </h3>
            <div class="space-y-3">
              <div>
                <label class="text-sm font-medium text-gray-600">知识点名称：</label>
                <p class="text-base text-gray-800 font-semibold">{{ selectedNode.name }}</p>
              </div>
              <div>
                <label class="text-sm font-medium text-gray-600">定义：</label>
                <p class="text-base text-gray-700">{{ selectedNode.definition }}</p>
              </div>
              <div v-if="selectedNode.category">
                <label class="text-sm font-medium text-gray-600">分类：</label>
                <a-tag :color="getCategoryColor(selectedNode.category)" class="ml-2">
                  {{ selectedNode.category }}
                </a-tag>
              </div>
              <div v-if="selectedNode.links && selectedNode.links.length > 0">
                <label class="text-sm font-medium text-gray-600">相关关系：</label>
                <div class="mt-2 space-y-1">
                  <div
                      v-for="(link, index) in selectedNode.links"
                      :key="index"
                      class="text-sm text-gray-600 bg-gray-50 p-2 rounded"
                  >
                    <span class="font-medium">{{ getNodeName(link.target) }}</span>
                    <a-tag color="blue" size="small" class="ml-2">
                      {{ link.relation }}
                    </a-tag>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 快捷提问 -->
          <div class="bg-gradient-to-r from-orange-50 to-yellow-50 rounded-2xl shadow-lg p-6 border border-orange-200">
            <h3 class="text-lg font-semibold text-orange-800 mb-3 flex items-center">
              <i class="fas fa-bolt mr-2"></i>
              快捷提问
            </h3>
            <div class="grid grid-cols-2 gap-2">
              <a-button
                  v-for="(quickQuestion, index) in quickQuestions"
                  :key="index"
                  size="small"
                  @click="currentQuestion = quickQuestion"
                  class="text-xs h-8"
              >
                {{ quickQuestion }}
              </a-button>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import { message } from 'ant-design-vue';
import request from '@/utils/request';
import * as d3 from 'd3';

// 状态管理
const currentQuestion = ref('');
const qaHistory = ref([]);
const isLoading = ref(false);
const isGeneratingGraph = ref(false);
const currentGraphData = ref({ nodes: [], links: [] });
const selectedNode = ref(null);
const graphContainer = ref(null);
const graphSvg = ref(null);

// 快捷问题示例
const quickQuestions = ref([
  '什么是二叉树？',
  '链表和数组的区别',
  '快速排序原理',
  '面向对象特性',
  'TCP和UDP区别',
  '数据库索引原理'
]);

// 初始化用户ID
const userId = ref('');
const initUserId = () => {
  const userInfo = localStorage.getItem('user');
  if (userInfo) {
    const user = JSON.parse(userInfo);
    userId.value = user.id;
  } else {
    userId.value = `guest_${Date.now()}`;
    message.warning("未登录，将以游客身份使用答疑功能");
  }
};

// 提交问题 (修改：使用 knowledgeGraphData，新增 console.log 调试)
const handleSubmit = async () => {
  const question = currentQuestion.value.trim();
  if (!question) return;

  try {
    isLoading.value = true;
    isGeneratingGraph.value = true;

    console.log('🚀 开始发送请求，问题:', question);

    const res = await request.post('/api/knowledge/qa', {
      question,
      userId: userId.value
    });

    console.log('✅ 收到API响应:', res);  // 调试：检查完整响应

    if (res.success) {
      console.log('📊 响应数据详情:');
      console.log('- answer:', res.data?.answer?.substring(0, 200) + '...');  // AI 数据
      console.log('- knowledgeGraphData nodes:', res.data?.knowledgeGraphData?.nodes?.length || 0);  // 新图谱

      // 检查数据是否存在
      if (!res.data?.answer) {
        console.error('❌ 回答数据为空');
        message.error('获取回答失败');
        return;
      }

      // 添加到历史记录 (新增：保存 knowledgeGraphData 到历史，便于追问)
      const newQA = {
        question,
        answer: res.data.answer,
        knowledgeGraphData: res.data.knowledgeGraphData,  // 新增：保存新图谱
        timestamp: res.data.timestamp || new Date().toISOString()
      };

      qaHistory.value.unshift(newQA);
      console.log('📝 已添加到历史记录');

      // 更新知识图谱数据：使用新格式，基于 AI answer
      if (res.data.knowledgeGraphData && res.data.knowledgeGraphData.nodes && res.data.knowledgeGraphData.nodes.length > 0) {
        currentGraphData.value = transformNewGraphData(res.data.knowledgeGraphData);  // 修改：用新转换函数
        console.log('🕸️ 转换后的图谱数据 (基于AI answer):', currentGraphData.value);
      } else {
        console.warn('⚠️ 知识图谱数据为空');
        currentGraphData.value = { nodes: [], links: [] };
      }

      // 清空输入框
      currentQuestion.value = '';

      // 渲染知识图谱
      await nextTick();
      if (currentGraphData.value.nodes.length > 0) {
        console.log('🎨 开始渲染知识图谱 (基于AI数据)');
        renderKnowledgeGraph();
      } else {
        console.warn('⚠️ 没有图谱数据可渲染');
      }

      message.success('问题分析完成！知识图谱已基于AI回答生成。');
    } else {
      console.error('❌ API返回失败:', res.msg);
      message.error(res.msg || '问答服务暂时不可用');
    }
  } catch (err) {
    console.error('💥 请求错误:', err);
    message.error(err.response?.data?.msg || '问答请求失败，请重试');
  } finally {
    isLoading.value = false;
    isGeneratingGraph.value = false;
  }
};

// 新转换函数：将 KnowledgeGraphData 转为 D3 格式 {nodes, links} (新增)
const transformNewGraphData = (graphData) => {
  console.log('🔄 转换新图谱数据，原始:', graphData);

  if (!graphData || !graphData.nodes || graphData.nodes.length === 0) {
    console.log('❌ 新图谱数据为空');
    return { nodes: [], links: [] };
  }

  const nodes = [];
  const links = [];
  const nodeMap = new Map();  // id -> node

  // 1. 构建 nodes：提取字段，推导 group/color/radius
  graphData.nodes.forEach((nodeData, index) => {
    if (nodeData && !nodeMap.has(nodeData.id)) {
      const transformedNode = {
        id: nodeData.id || `node_${index}`,
        name: nodeData.name || '未命名节点',
        definition: nodeData.definition || '暂无定义',
        category: nodeData.category || '未知分类',
        group: getNodeGroup(nodeData.category),  // 复用旧 group 逻辑
        color: (nodeData.itemStyle && nodeData.itemStyle.color) || getDefaultColor(nodeData.category),  // 从 itemStyle 取色
        radius: (nodeData.symbolSize || 20) / 3,  // symbolSize 转为 D3 r (调整比例)
        links: []  // 本地 links，不用于全局
      };
      nodes.push(transformedNode);
      nodeMap.set(nodeData.id, transformedNode);
      console.log(`✅ 添加节点: ${transformedNode.name}, color: ${transformedNode.color}, radius: ${transformedNode.radius}`);
    }
  });

  // 2. 构建 links：直接从 graphData.links
  if (graphData.links && Array.isArray(graphData.links)) {
    graphData.links.forEach((linkData) => {
      if (linkData && linkData.source && linkData.target) {
        const sourceNode = nodeMap.get(linkData.source);
        const targetNode = nodeMap.get(linkData.target);
        if (sourceNode && targetNode) {  // 确保节点存在
          links.push({
            source: linkData.source,  // id 字符串
            target: linkData.target,
            relation: linkData.relation || '相关',
            description: linkData.description || ''
          });
          console.log(`🔗 添加链接: ${linkData.source} -> ${linkData.target}`);
        }
      }
    });
  }

  console.log('📈 新图谱转换完成: 节点数=', nodes.length, ', 链接数=', links.length);
  return { nodes, links };
};

// 辅助：默认颜色 (新增，fallback)
const getDefaultColor = (category) => {
  const colors = {
    '数据结构': '#3B82F6',
    '算法': '#10B981',
    '编程语言': '#8B5CF6',
    '计算机网络': '#F59E0B',
    '数据库': '#EF4444',
    '计算机基础': '#6B7280'
  };
  return colors[category] || '#6B7280';
};

// 获取节点分组
const getNodeGroup = (category) => {
  const groups = {
    '数据结构': 1,
    '算法': 2,
    '编程语言': 3,
    '计算机网络': 4,
    '数据库': 5,
    '计算机基础': 0
  };
  return groups[category] || 0;
};

// 获取分类颜色
const getCategoryColor = (category) => {
  const colors = {
    '数据结构': 'blue',
    '算法': 'green',
    '编程语言': 'purple',
    '计算机网络': 'orange',
    '数据库': 'red'
  };
  return colors[category] || 'gray';
};

// 渲染知识图谱 (修改：使用 color/radius，适配新数据)
const renderKnowledgeGraph = () => {
  if (!graphSvg.value || currentGraphData.value.nodes.length === 0) return;

  console.log('🎨 开始渲染知识图谱 (新格式)');

  // 清除之前的图谱
  d3.select(graphSvg.value).selectAll('*').remove();

  const width = graphContainer.value.clientWidth;
  const height = graphContainer.value.clientHeight;

  const svg = d3.select(graphSvg.value)
      .attr('width', width)
      .attr('height', height);

  // 创建力导向图
  const simulation = d3.forceSimulation(currentGraphData.value.nodes)
      .force('link', d3.forceLink(currentGraphData.value.links).id(d => d.id).distance(100))
      .force('charge', d3.forceManyBody().strength(-300))
      .force('center', d3.forceCenter(width / 2, height / 2))
      .force('collision', d3.forceCollide().radius(50));

  // 创建箭头
  svg.append('defs').selectAll('marker')
      .data(['arrow'])
      .enter().append('marker')
      .attr('id', d => d)
      .attr('viewBox', '0 -5 10 10')
      .attr('refX', 15)
      .attr('refY', -0.5)
      .attr('markerWidth', 6)
      .attr('markerHeight', 6)
      .attr('orient', 'auto')
      .append('path')
      .attr('d', 'M0,-5L10,0L0,5')
      .attr('fill', '#999');

  // 创建链接
  const link = svg.append('g')
      .selectAll('line')
      .data(currentGraphData.value.links)
      .enter().append('line')
      .attr('stroke', '#999')
      .attr('stroke-opacity', 0.6)
      .attr('stroke-width', 2)
      .attr('marker-end', 'url(#arrow)');

  // 创建节点 (修改：r 从 radius，fill 从 color)
  const node = svg.append('g')
      .selectAll('circle')
      .data(currentGraphData.value.nodes)
      .enter().append('circle')
      .attr('r', d => d.radius)  // 修改：用 radius
      .attr('fill', d => d.color)  // 修改：用 color (从 AI itemStyle)
      .attr('stroke', '#fff')
      .attr('stroke-width', 2)
      .call(d3.drag()
          .on('start', dragstarted)
          .on('drag', dragged)
          .on('end', dragended))
      .on('click', (event, d) => {
        selectedNode.value = d;  // 选中节点，用于详情
        console.log('🔍 选中节点:', d);
      });

  // 添加节点标签
  const label = svg.append('g')
      .selectAll('text')
      .data(currentGraphData.value.nodes)
      .enter().append('text')
      .text(d => d.name)
      .attr('font-size', 12)
      .attr('dx', 15)
      .attr('dy', 4)
      .attr('fill', '#374151');

  // 更新位置 (修改：collision 用 radius)
  simulation.on('tick', () => {
    link
        .attr('x1', d => d.source.x)
        .attr('y1', d => d.source.y)
        .attr('x2', d => d.target.x)
        .attr('y2', d => d.target.y);

    node
        .attr('cx', d => d.x)
        .attr('cy', d => d.y);

    label
        .attr('x', d => d.x)
        .attr('y', d => d.y);
  });

  function dragstarted(event, d) {
    if (!event.active) simulation.alphaTarget(0.3).restart();
    d.fx = d.x;
    d.fy = d.y;
  }

  function dragged(event, d) {
    d.fx = event.x;
    d.fy = event.y;
  }

  function dragended(event, d) {
    if (!event.active) simulation.alphaTarget(0);
    d.fx = null;
    d.fy = null;
  }

  console.log('✅ 知识图谱渲染完成 (基于AI数据)');
};

// 追问功能 (微调：用 answer 提取主题)
const handleFollowUp = (item) => {
  const mainTopic = extractMainTopicFromAnswer(item.answer);
  currentQuestion.value = `关于「${mainTopic}」的更多细节？`;
};

// 新增：从 answer 提取主主题 (用于追问)
const extractMainTopicFromAnswer = (answer) => {
  const match = answer.match(/\*\*(.*?)\*\*/);
  return match ? match[1] : '这个知识点';
};

// 切换图谱布局
const toggleGraphLayout = () => {
  renderKnowledgeGraph();
  message.info('已切换图谱布局');
};

// 导出图谱
const exportGraph = () => {
  const svgElement = graphSvg.value;
  const serializer = new XMLSerializer();
  const source = serializer.serializeToString(svgElement);
  const blob = new Blob([source], { type: 'image/svg+xml' });
  const url = URL.createObjectURL(blob);

  const link = document.createElement('a');
  link.href = url;
  link.download = 'knowledge-graph.svg';
  link.click();

  URL.revokeObjectURL(url);
  message.success('知识图谱已导出');
};

// 格式化答案
const formatAnswer = (answer) => {
  if (!answer) return '';
  return answer.replace(/\*\*(.*?)\*\*/g, '<strong class="text-green-700">$1</strong>')
      .replace(/\n/g, '<br>');
};

// 格式化时间
const formatTime = (timestamp) => {
  if (!timestamp) return '刚刚';
  return new Date(timestamp).toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 获取节点名称
const getNodeName = (nodeId) => {
  const node = currentGraphData.value.nodes.find(n => n.id === nodeId);
  return node ? node.name : nodeId;
};

// 复制到剪贴板
const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text);
    message.success('已复制到剪贴板');
  } catch (err) {
    const textArea = document.createElement('textarea');
    textArea.value = text;
    document.body.appendChild(textArea);
    textArea.select();
    document.execCommand('copy');
    document.body.removeChild(textArea);
    message.success('已复制到剪贴板');
  }
};

// 响应式调整
const handleResize = () => {
  if (currentGraphData.value.nodes.length > 0) {
    renderKnowledgeGraph();
  }
};

onMounted(() => {
  initUserId();
  window.addEventListener('resize', handleResize);
});
</script>

<style scoped>
.knowledge-qa-page {
  padding-top: 80px;
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  min-height: calc(100vh - 80px);
}

.page-header {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
}

.page-header h1 {
  color: #000000;
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
  background: #e6f7ff;
  border-color: #7C3AED;
  transform: none;
  box-shadow: none;
}

/* 知识图谱容器样式 */
.knowledge-graph-container {
  position: relative;
}

/* 动画效果 */
.slide-up-enter-from {
  opacity: 0;
  transform: translateY(20px);
}

.slide-up-enter-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 响应式调整 */
@media (max-width: 1024px) {
  .grid-cols-1.lg\:grid-cols-2 {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
}

@media (max-width: 768px) {
  .knowledge-qa-page {
    padding-top: 60px;
    min-height: calc(100vh - 60px);
  }

  .page-header {
    padding: 3rem 1rem;
  }

  main {
    padding: 1rem;
  }
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

<!-- 在 public/index.html 中添加依赖 -->
<!--
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<script src="https://d3js.org/d3.v7.min.js"></script>
-->