const API_BASE_URL = 'http://localhost:8080/api';

class ApiService {
    async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers,
            },
            ...options,
        };

        if (config.body && typeof config.body === 'object') {
            config.body = JSON.stringify(config.body);
        }

        try {
            const response = await fetch(url, config);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || '请求失败');
            }

            return data;
        } catch (error) {
            console.error('API请求错误:', error);
            throw error;
        }
    }

    // 用户登录
    async login(credentials) {
        try {
            const data = await this.request('/users/login', {
                method: 'POST',
                body: credentials
            });
            return {
                success: true,
                data: data.data,
                message: data.message
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '登录失败，请检查网络连接',
                error: error
            };
        }
    }

    // 用户注册
    async register(userData) {
        try {
            const data = await this.request('/users', {
                method: 'POST',
                body: userData
            });
            return {
                success: true,
                data: data.data,
                message: data.message
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '注册失败，请检查网络连接',
                error: error
            };
        }
    }

    // 获取用户信息
    async getUserById(id) {
        try {
            const data = await this.request(`/users/${id}`);
            return {
                success: true,
                data: data.data
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '获取用户信息失败'
            };
        }
    }

    // 根据用户名获取用户
    async getUserByUsername(username) {
        try {
            const data = await this.request(`/users/username/${username}`);
            return {
                success: true,
                data: data.data
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '获取用户信息失败'
            };
        }
    }

    // 根据学号获取用户
    async getUserByStudentId(studentId) {
        try {
            const data = await this.request(`/users/student/${studentId}`);
            return {
                success: true,
                data: data.data
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '获取用户信息失败'
            };
        }
    }

    // 更新用户信息
    async updateUser(id, userData) {
        try {
            const data = await this.request(`/users/${id}`, {
                method: 'PUT',
                body: userData
            });
            return {
                success: true,
                data: data.data,
                message: data.message
            };
        } catch (error) {
            return {
                success: false,
                message: error.message || '更新用户信息失败'
            };
        }
    }

    // 检查服务是否可用
    async healthCheck() {
        try {
            console.log('🔧 正在检查后端服务连接...');

            // 使用 fetch 直接测试连接，避免复杂的 CORS 处理
            const response = await fetch('http://localhost:8080/api/users', {
                method: 'GET',
                mode: 'cors', // 明确指定 CORS 模式
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            console.log('🔧 后端服务响应状态:', response.status);

            if (response.ok) {
                console.log('✅ 后端服务连接正常');
                return true;
            } else {
                console.log('❌ 后端服务响应异常:', response.status, response.statusText);
                return false;
            }
        } catch (error) {
            console.error('❌ 后端服务连接失败:', error.message);
            console.error('❌ 错误详情:', error);
            return false;
        }
    }

    // 获取所有用户（用于健康检查）
    async getAllUsers() {
        try {
            const data = await this.request('/users');
            return {
                success: true,
                data: data.data
            };
        } catch (error) {
            return {
                success: false,
                message: error.message
            };
        }
    }
}

export default new ApiService();