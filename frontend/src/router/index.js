import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/HomeView.vue'
import AcademicProfile from '../views/AcademicProfile.vue'
import LoginRegister from '../views/LoginRegister.vue'

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
        path: '/profile',
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


export default router
