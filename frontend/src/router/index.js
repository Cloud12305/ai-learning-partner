import { createRouter, createWebHistory } from 'vue-router'
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
        path: '/',
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
        component: StudyPlan
    },
    {
        path: '/course-learning',
        name: 'CourseLearning',
        component: CourseLearning
    },
    {
        path: '/learning-progress',
        name: 'LearningProgress',
        component: LearningProgress
    },
    {
        path: '/job-matching',
        name: 'JobMatching',
        component: JobMatching
    },
    {
        path: '/practical-training',
        name: 'PracticalTraining',
        component: PracticalTraining
    },
    {
        path: '/knowledge-answering',
        name: 'KnowledgeAnswering',
        component: KnowledgeAnswering,
        meta: { title: '知识答疑 - AI智能·学习搭子' }
    },
    // 2. 新增：心理陪伴独立页面路由
    {
        path: '/psychology-companion',
        name: 'PsychologyCompanion',
        component: PsychologyCompanion,
        meta: { title: '心理陪伴 - AI智能·学习搭子' }
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
// 路由拦截：统一设置页面标题
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title
    }
    next()
})


export default router
