const API_BASE_URL = 'http://localhost:8080/api';

class ApiService {
    async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;

        // 获取 token
        const token = localStorage.getItem('token');

        // 检查 token 格式，如果是有问题的测试 token 则清除
        if (token && token.includes('test-signature')) {
            console.warn('⚠️ 检测到有问题的测试 token，正在清除...');
            this.clearAuthData();
            throw new Error('Token 格式错误，请重新登录');
        }

        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
            ...options,
        };

        // 如果有 token，添加到请求头
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
            console.log('🔑 添加认证令牌到请求头');
        }

        if (config.body && typeof config.body === 'object') {
            config.body = JSON.stringify(config.body);
        }

        try {
            console.log(`🔄 API请求: ${config.method || 'GET'} ${url}`);

            const response = await fetch(url, config);

            // 检查响应状态
            if (!response.ok) {
                const errorMessage = `请求失败: ${response.status} ${response.statusText}`;
                console.error(`❌ API响应错误: ${response.status}`, errorMessage);

                // 如果是 401 未授权错误，清除本地存储的 token
                if (response.status === 401) {
                    this.clearAuthData();
                    console.log('🔐 Token 已过期，已清除本地存储');
                }

                throw new Error(errorMessage);
            }

            const data = await response.json();
            console.log('✅ API响应成功');
            return data;
        } catch (error) {
            console.error('❌ API请求错误:', error);
            throw error;
        }
    }

    // 清除认证数据
    clearAuthData() {
        localStorage.removeItem('token');
        localStorage.removeItem('isAuthenticated');
        localStorage.removeItem('user');
    }

    // 用户登录
    async login(credentials) {
        try {
            console.log('🔐 尝试登录:', credentials.username);

            // 登录请求不需要 token，所以单独处理
            const url = `${API_BASE_URL}/users/login`;
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentials)
            });

            if (!response.ok) {
                const errorMessage = `登录失败: ${response.status} ${response.statusText}`;
                console.error('❌ 登录响应错误:', errorMessage);
                throw new Error(errorMessage);
            }

            const data = await response.json();
            console.log('📨 登录响应数据:', data);

            // 保存 token 到本地存储
            if (data.data && data.data.token) {
                localStorage.setItem('token', data.data.token);
                console.log('💾 Token 已保存到本地存储');
            } else if (data.token) {
                localStorage.setItem('token', data.token);
                console.log('💾 Token 已保存到本地存储');
            }

            return {
                success: true,
                data: data.data || data,
                message: data.message || '登录成功'
            };
        } catch (error) {
            console.error('登录失败:', error);
            return {
                success: false,
                message: error.message || '登录失败，请检查网络连接'
            };
        }
    }

    // 检查服务是否可用
    async healthCheck() {
        try {
            console.log('🔧 正在检查后端服务连接...');

            const response = await fetch(`${API_BASE_URL}/users/health`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            console.log('🔧 健康检查响应状态:', response.status);

            if (response.ok) {
                await response.json(); // 处理响应但不使用数据
                console.log('✅ 后端服务连接正常');
                return true;
            } else {
                console.log('❌ 后端服务响应异常:', response.status, response.statusText);
                return false;
            }
        } catch (error) {
            console.error('❌ 后端服务连接失败:', error.message);
            return false;
        }
    }

    // 获取成绩记录
    async getAcademicRecords() {
        try {
            const data = await this.request('/academic/records');
            return {
                success: true,
                data: data.data || data
            };
        } catch (error) {
            console.error('获取成绩记录失败:', error);
            return {
                success: false,
                message: error.message || '加载成绩数据失败'
            };
        }
    }

    // 其他方法保持不变...
    async register(userData) {
        try {
            console.log('📝 尝试注册:', userData.username);

            const data = await this.request('/users/register', {
                method: 'POST',
                body: userData
            });

            return {
                success: true,
                data: data.data || data,
                message: data.message || '注册成功'
            };
        } catch (error) {
            console.error('注册失败:', error);
            return {
                success: false,
                message: error.message || '注册失败，请检查网络连接'
            };
        }
    }

    async getCurrentUser() {
        try {
            const data = await this.request('/users/me');
            return {
                success: true,
                data: data.data || data
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '获取用户信息失败'
            };
        }
    }

    async getUserById(id) {
        try {
            const data = await this.request(`/users/${id}`);
            return {
                success: true,
                data: data.data || data
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '获取用户信息失败'
            };
        }
    }

    async updateUser(id, userData) {
        try {
            const data = await this.request(`/users/${id}`, {
                method: 'PUT',
                body: userData
            });
            return {
                success: true,
                data: data.data || data,
                message: data.message || '更新成功'
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '更新用户信息失败'
            };
        }
    }
}

export default new ApiService();