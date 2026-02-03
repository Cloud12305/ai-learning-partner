import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'  // 3.x 版本使用 antd.css
import './assets/styles/global.css'

const app = createApp(App)

// 注册 Ant Design Vue 图标（根据你的 package.json 中的 @ant-design/icons）
import * as Icons from '@ant-design/icons-vue'
Object.keys(Icons).forEach(key => {
    app.component(key, Icons[key])
})

app.use(router)
app.use(Antd)

app.mount('#app')