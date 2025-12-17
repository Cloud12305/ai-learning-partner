// services/courseService.js - 只处理课程相关接口
const API_BASE_URL = 'http://localhost:8080/api';

class CourseService {
    async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;

        // 获取令牌
        const token = localStorage.getItem('token');

        const config = {
            headers: {
                'Content-Type': 'application/json',
                // 添加 Authorization 头
                ...(token && { 'Authorization': `Bearer ${token}` }),
                ...options.headers,
            },
            ...options,
        };

        if (config.body && typeof config.body === 'object') {
            config.body = JSON.stringify(config.body);
        }

        try {
            const response = await fetch(url, config);
            console.log('📡 课程请求详情:', {
                url,
                method: config.method,
                headers: config.headers,
                status: response.status,
                statusText: response.statusText
            });

            // 检查响应状态
            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));

                // 如果是401未授权，清除令牌
                if (response.status === 401) {
                    console.warn('⚠️ 令牌无效，清除本地存储');
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                }

                throw new Error(errorData.message || `请求失败: ${response.status}`);
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error('课程API请求错误:', error);
            throw error;
        }
    }

    // ==================== 课程相关接口 ====================

    /**
     * 获取推荐课程
     * GET /api/courses/recommended
     */
    async getRecommendedCourses() {
        try {
            console.log('📚 调用获取推荐课程接口');

            const data = await this.request('/courses/recommended', {
                method: 'GET'
            });

            console.log('📚 推荐课程接口返回:', data);

            // 后端直接返回数组
            const courses = Array.isArray(data) ? data : (data.data || []);

            return {
                success: true,
                data: courses,
                message: '获取成功'
            };
        } catch (error) {
            console.error('❌ 获取推荐课程失败:', error);
            return {
                success: false,
                message: error.message || '获取推荐课程失败',
                data: []
            };
        }
    }

    /**
     * 搜索课程 - 修复为 GET 请求
     * GET /api/courses/search?keyword=xxx&category=xxx&difficulty=xxx
     */
    async searchCourses(searchParams) {
        try {
            console.log('🔍 调用搜索课程接口，参数:', searchParams);

            // 构建查询字符串
            const queryParams = new URLSearchParams();

            if (searchParams.keyword && searchParams.keyword.trim()) {
                queryParams.append('keyword', searchParams.keyword.trim());
            }
            if (searchParams.category && searchParams.category.trim()) {
                queryParams.append('category', searchParams.category.trim());
            }
            if (searchParams.difficulty && searchParams.difficulty.trim()) {
                queryParams.append('difficulty', searchParams.difficulty.trim());
            }
            if (searchParams.university && searchParams.university.trim()) {
                queryParams.append('university', searchParams.university.trim());
            }

            const queryString = queryParams.toString();
            const endpoint = queryString ? `/courses/search?${queryString}` : '/courses/search';

            console.log('🔍 请求URL:', endpoint);

            // 使用 GET 请求
            const data = await this.request(endpoint, {
                method: 'GET'
            });

            console.log('🔍 搜索课程接口返回:', data);

            // 后端直接返回数组
            const courses = Array.isArray(data) ? data : (data.data || []);

            return {
                success: true,
                data: courses,
                message: `找到 ${courses.length} 门课程`
            };
        } catch (error) {
            console.error('❌ 搜索课程失败:', error);
            return {
                success: false,
                message: error.message || '搜索课程失败',
                data: []
            };
        }
    }

    /**
     * 手动抓取课程
     * POST /api/courses/scrape
     */
    async scrapeCourses() {
        try {
            console.log('🔄 调用抓取课程接口');

            const data = await this.request('/courses/scrape', {
                method: 'POST'
            });

            console.log('🔄 抓取课程接口返回:', data);

            // 后端直接返回数组
            const courses = Array.isArray(data) ? data : (data.data || []);

            return {
                success: true,
                data: courses,
                message: `成功抓取 ${courses.length} 门课程`
            };
        } catch (error) {
            console.error('❌ 抓取课程失败:', error);
            return {
                success: false,
                message: error.message || '抓取课程失败',
                data: []
            };
        }
    }
}

// 导出单例
export default new CourseService();