import axios from 'axios'
import { message } from 'ant-design-vue'
import router from '@/router'

// 创建 axios 实例
const http = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 请求拦截器
http.interceptors.request.use(
    (config) => {
        // 添加 token 到请求头
        const token = localStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }

        // 添加调试日志
        console.log(`发起请求: ${config.method?.toUpperCase()} ${config.url}`)
        return config
    },
    (error) => {
        console.error('请求拦截器错误:', error)
        return Promise.reject(error)
    }
)

// 响应拦截器
http.interceptors.response.use(
    (response) => {
        console.log(`请求成功: ${response.config.url}`, response.data)
        // 直接返回响应数据
        return response.data
    },
    (error) => {
        // 处理响应错误
        console.error('响应错误:', error)

        if (error.response) {
            const { status, data } = error.response
            const url = error.config?.url || '未知URL'

            console.error(`API 错误 [${status}]: ${url}`, data)

            switch (status) {
                case 401:
                    message.error('未授权，请重新登录')
                    localStorage.removeItem('token')
                    localStorage.removeItem('user')
                    router.push('/login')
                    break
                case 403:
                    message.error('拒绝访问')
                    break
                case 404:
                    message.error('请求的资源不存在')
                    break
                case 500:
                    message.error('服务器内部错误')
                    break
                default:
                    message.error(data?.message || `请求失败 (${status})`)
            }
        } else if (error.request) {
            // 请求已发出但没有收到响应
            console.error('网络错误:', error.request)
            message.error('网络连接失败，请检查网络连接')
        } else {
            // 其他错误
            console.error('请求配置错误:', error.message)
            message.error('请求配置错误: ' + error.message)
        }

        return Promise.reject(error)
    }
)

export default http