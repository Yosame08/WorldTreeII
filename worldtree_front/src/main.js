import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import router from './router/router'
import './mock.js'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// axios.defaults.baseURL = 'http://localhost:8088'

const app = createApp(App)
// app.config.globalProperties.$axios = axios

app.use(router).use(ElementPlus).mount('#app')