import { createApp } from 'vue'
import App from './App.vue'
import router from './router'  // 从路由文件导入
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import './assets/styles/global.css' // 全局样式

// 创建并挂载应用
createApp(App)
    .use(Antd)
    .use(router)
    .mount('#app')
