import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios'
import Home from '../views/HomeView.vue'
import AcademicProfile from '../views/AcademicProfile.vue'
import LoginRegister from '../views/LoginRegister.vue'
import StudyPlan from '../views/StudyPlan.vue'
import CourseLearning from '../views/CourseLearning.vue'
import LearningProgress from '../views/LearningProgress.vue'
import JobMatching from '../views/JobMatching.vue'
import PracticalTraining from '../views/PracticalTraining.vue'
import KnowledgeAnswering from '../views/KnowledgeAnswering.vue' // 知识答疑页面
import PsychologyCompanion from '../views/PsychologyCompanion.vue' // 心理陪伴页面


const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        meta: {
            title: '首页 - AI 智能·学习搭子系统'
        }
    },
    {
        path: '/academic-profile',
        name: 'AcademicProfile',
        component: AcademicProfile,
        meta: {
            title: '学业画像 - 学生学业画像',
            requiresAuth: true
        }
    },
    {
        path: '/login',
        name: 'LoginRegister',
        component: LoginRegister,
        props: (route) => ({ mode: route.query.mode || 'login' }),
        meta: {
            title: '登录/注册 - AI 智能·学习搭子系统'
        }
    },
    {
        path: '/study-plan',
        name: 'StudyPlan',
        component: StudyPlan,
        meta: { requiresAuth: true }
    },
    {
        path: '/course-learning',
        name: 'CourseLearning',
        component: CourseLearning,
        meta: { requiresAuth: true }
    },
    {
        path: '/learning-progress',
        name: 'LearningProgress',
        component: LearningProgress,
        meta: { requiresAuth: true }
    },
    {
        path: '/job-matching',
        name: 'JobMatching',
        component: JobMatching,
        meta: { requiresAuth: true }
    },
    {
        path: '/practical-training',
        name: 'PracticalTraining',
        component: PracticalTraining,
        meta: { requiresAuth: true }
    },
    {
        path: '/knowledge-answering',
        name: 'KnowledgeAnswering',
        component: KnowledgeAnswering,
        meta: {
            title: '知识答疑 - AI智能·学习搭子',
            requiresAuth: true
        }
    },
    {
        path: '/psychology-companion',
        name: 'PsychologyCompanion',
        component: PsychologyCompanion,
        meta: {
            title: '心理陪伴 - AI智能·学习搭子',
            requiresAuth: true
        }
    },
    {
        path: '/:pathMatch(.*)*',
        redirect: '/'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        return savedPosition || { top: 0 }
    }
})

// 配置 axios 基础 URL 和拦截器
axios.defaults.baseURL = 'http://localhost:8080'

// 请求拦截器 - 自动添加 token
axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        console.log('当前 token:', token) // 调试信息

        if (token) {
            // 使用真实的 JWT token
            config.headers.Authorization = `Bearer ${token}`
            console.log('设置 Authorization header:', config.headers.Authorization)
        } else {
            // 开发环境：临时使用 mock token（确保后端拦截器已支持）
            if (process.env.NODE_ENV === 'development') {
                config.headers.Authorization = 'Bearer mock-jwt-token'
                console.log('使用 mock token 进行开发测试')
            }
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器 - 处理认证错误
axios.interceptors.response.use(
    response => {
        return response
    },
    error => {
        if (error.response && error.response.status === 401) {
            // token 无效或过期
            localStorage.removeItem('token')
            router.push('/login')
        }
        return Promise.reject(error)
    }
)

// 路由守卫 - 检查认证
router.beforeEach((to, from, next) => {
    // 设置页面标题
    if (to.meta.title) {
        document.title = to.meta.title
    }

    // 检查是否需要认证
    if (to.matched.some(record => record.meta.requiresAuth)) {
        const token = localStorage.getItem('token')
        if (!token) {
            console.log('需要认证，跳转到登录页')
            next('/login')
            return
        }
    }

    next()
})

export default router