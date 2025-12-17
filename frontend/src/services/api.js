// src/services/api.js
import axios from 'axios'

// 创建axios实例
const api = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器 - 添加token
api.interceptors.request.use(
    config => {
        console.log('🚀 发起请求:', {
            method: config.method,
            url: config.url,
            fullUrl: config.baseURL + config.url
        })

        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    error => {
        console.error('请求拦截器错误:', error)
        return Promise.reject(error)
    }
)

// 响应拦截器
api.interceptors.response.use(
    response => {
        console.log('✅ 响应成功:', {
            url: response.config.url,
            status: response.status,
            data: response.data
        })
        // 直接返回后端的数据结构
        return response.data
    },
    error => {
        console.error('❌ 响应错误:', {
            message: error.message,
            code: error.code,
            status: error.response?.status,
            statusText: error.response?.statusText,
            url: error.config?.url,
            fullUrl: error.config?.baseURL + error.config?.url,
            method: error.config?.method,
            headers: error.config?.headers,
            data: error.config?.data
        })

        if (error.response) {
            console.error('API错误响应:', error.response.status, error.response.data)

            switch (error.response.status) {
                case 401:
                    // token过期或无效
                    localStorage.removeItem('token')
                    localStorage.removeItem('isAuthenticated')
                    localStorage.removeItem('user')
                    console.log('Token已过期，请重新登录')
                    break
                case 403:
                    console.error('权限不足')
                    break
                case 404:
                    console.error('资源不存在')
                    break
                case 500:
                    console.error('服务器内部错误')
                    break
                default:
                    console.error('请求失败', error.response.data)
            }
        } else if (error.request) {
            console.error('网络错误，无法连接到服务器')
        } else {
            console.error('请求配置错误', error.message)
        }

        return Promise.reject(error)
    }
)

// 学业画像相关API
const academicProfileApi = {
    // 获取当前用户的学业画像
    getCurrentProfile() {
        return api.get('/api/academic-profile/current')
    },

    // 获取当前用户的课程成绩（带分页）
    getCourses(limit = 5) {
        return api.get('/api/academic-profile/current/courses', {
            params: { limit }
        })
    },

    // 获取当前用户的所有课程成绩
    getAllCourses() {
        return api.get('/api/academic-profile/current/courses/all')
    },

    // 获取当前用户的实验室记录（带分页）
    getLabs(limit = 2) {
        return api.get('/api/academic-profile/current/labs', {
            params: { limit }
        })
    },

    // 获取当前用户的所有实验室记录
    getAllLabs() {
        return api.get('/api/academic-profile/current/labs/all')
    },

    // 获取当前用户的借阅记录（带分页）
    getBooks(limit = 4) {
        return api.get('/api/academic-profile/current/books', {
            params: { limit }
        })
    },

    // 获取当前用户的所有借阅记录
    getAllBooks() {
        return api.get('/api/academic-profile/current/books/all')
    },

    // 管理员：获取所有课程数据
    getAdminCourses() {
        return api.get('/api/academic-profile/admin/courses')
    },

    // 管理员：创建课程记录
    createCourse(courseData) {
        return api.post('/api/academic-profile/admin/courses', courseData)
    },

    // 管理员：更新课程记录
    updateCourse(id, courseData) {
        return api.put(`/api/academic-profile/admin/courses/${id}`, courseData)
    },

    // 管理员：删除课程记录
    deleteCourse(id) {
        return api.delete(`/api/academic-profile/admin/courses/${id}`)
    },

    // 管理员：获取所有实验室数据
    getAdminLabs() {
        return api.get('/api/academic-profile/admin/labs')
    },

    // 管理员：创建实验室记录
    createLab(labData) {
        return api.post('/api/academic-profile/admin/labs', labData)
    },

    // 管理员：更新实验室记录
    updateLab(id, labData) {
        return api.put(`/api/academic-profile/admin/labs/${id}`, labData)
    },

    // 管理员：删除实验室记录
    deleteLab(id) {
        return api.delete(`/api/academic-profile/admin/labs/${id}`)
    },

    // 管理员：获取所有图书数据
    getAdminBooks() {
        return api.get('/api/academic-profile/admin/books')
    },

    // 管理员：创建借阅记录
    createBook(bookData) {
        return api.post('/api/academic-profile/admin/books', bookData)
    },

    // 管理员：更新借阅记录
    updateBook(id, bookData) {
        return api.put(`/api/academic-profile/admin/books/${id}`, bookData)
    },

    // 管理员：删除借阅记录
    deleteBook(id) {
        return api.delete(`/api/academic-profile/admin/books/${id}`)
    },

    getAcademicOverview() {
        return api.get('/api/academic-profile/current/overview')
    }
}

// 用户相关API
const userApi = {
    // 用户登录
    login(credentials) {
        console.log('🔐 调用登录接口: /api/users/login')
        return api.post('/api/users/login', credentials)
    },

    // 用户注册
    register(userData) {
        console.log('📝 调用注册接口: /api/users/register')
        return api.post('/api/users/register', userData)
    },

    // 获取当前用户信息
    getCurrentUser() {
        console.log('👤 调用获取当前用户: /api/users/me')
        return api.get('/api/users/me')
    },

    // 获取所有用户（仅管理员）
    getAllUsers() {
        return api.get('/api/users')
    },

    // 获取所有学生用户（仅管理员）
    getStudentUsers(params = {}) {
        return api.get('/api/users/students', { params })
    },

    // 根据ID获取用户
    getUserById(id) {
        return api.get(`/api/users/${id}`)
    },

    // 根据学号获取用户
    getUserByStudentId(studentId) {
        return api.get(`/api/users/student/${studentId}`)
    },

    // 更新用户信息
    updateUser(id, userData) {
        return api.put(`/api/users/${id}`, userData)
    },

    // 更新用户角色
    updateUserRole(id, role) {
        return api.put(`/api/users/${id}/role`, { role })
    },

    // 删除用户
    deleteUser(id) {
        return api.delete(`/api/users/${id}`)
    },

    // 创建用户（仅管理员）
    createUser(userData) {
        return api.post('/api/users/create', userData)
    },

    // 健康检查
    healthCheck() {
        return api.get('/api/users/health')
    },

    // 权限检查
    checkPermission() {
        return api.get('/api/users/check-permission')
    },

    // 更新学习目标
    updateLearningGoal(goal) {
        return api.put('/api/users/set-learning-goal', { learningGoal: goal })
    },

    // 更新当前用户信息
    updateCurrentUser(userData) {
        return api.put('/api/users/me/update', userData)
    },

    // 获取管理员统计数据
    getAdminStats() {
        return api.get('/api/users/admin/stats')
    },

    // 清除认证数据
    clearAuthData() {
        localStorage.removeItem('token')
        localStorage.removeItem('isAuthenticated')
        localStorage.removeItem('user')
    }
}

// 简化的ApiService类，只保留必要的功能
class ApiService {
    // 清除认证数据
    clearAuthData() {
        localStorage.removeItem('token')
        localStorage.removeItem('isAuthenticated')
        localStorage.removeItem('user')
    }

    // 健康检查
    async healthCheck() {
        try {
            const response = await axios.get('http://localhost:8080/api/users/health')
            return response.status === 200
        } catch {
            return false
        }
    }

    // 获取学业概览
    async getAcademicOverview() {
        try {
            const data = await academicProfileApi.getAcademicOverview()
            return {
                success: true,
                data: data
            }
        } catch (error) {
            return {
                success: false,
                message: error.message || '加载学业概览失败'
            }
        }
    }
}

// 创建并导出单个实例
const apiService = new ApiService()

// 导出所有需要的API
export default apiService
export { academicProfileApi, userApi }