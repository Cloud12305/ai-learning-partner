// 统一的登录状态管理工具
export const auth = {
    // 检查是否已登录
    isAuthenticated() {
        const token = localStorage.getItem('token')
        const isAuth = localStorage.getItem('isAuthenticated')
        return !!(token && isAuth === 'true')
    },

    // 获取用户信息
    getUserInfo() {
        const userStr = localStorage.getItem('user')
        if (userStr) {
            try {
                return JSON.parse(userStr)
            } catch (error) {
                console.error('解析用户信息失败:', error)
                return null
            }
        }
        return null
    },

    // 登录
    login(userData, token) {
        localStorage.setItem('isAuthenticated', 'true')
        localStorage.setItem('user', JSON.stringify(userData))
        localStorage.setItem('token', token)

        // 触发自定义事件通知其他组件
        window.dispatchEvent(new Event('user-info-updated'))
    },

    // 退出登录
    logout() {
        localStorage.removeItem('isAuthenticated')
        localStorage.removeItem('user')
        localStorage.removeItem('token')

        // 触发自定义事件
        window.dispatchEvent(new Event('user-logout'))
    }
}

export default auth