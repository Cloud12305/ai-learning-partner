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

    // 用户相关API
    async login(credentials) {
        return this.request('/users/login', {
            method: 'POST',
            body: credentials
        });
    }

    async register(userData) {
        return this.request('/users', {
            method: 'POST',
            body: userData
        });
    }

    async getUserById(id) {
        return this.request(`/users/${id}`);
    }

    async getUserByUsername(username) {
        return this.request(`/users/username/${username}`);
    }

    async getUserByStudentId(studentId) {
        return this.request(`/users/student/${studentId}`);
    }

    async updateUser(id, userData) {
        return this.request(`/users/${id}`, {
            method: 'PUT',
            body: userData
        });
    }
}

export default new ApiService();