import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import router from './router/router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import ECharts from 'vue-echarts'
import * as echarts from 'echarts'

import './mock.js'
import store from './services/storeService'
import { quillEditor } from "vue3-quill";

// 添加请求拦截器
axios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    // 判断是否存在token,如果存在将每个页面header添加token
    if (sessionStorage.getItem("token")) {
        config.headers.common['Authorization'] = sessionStorage.getItem("token");
    }
    return config
}, function (error) {
    router.push('/login')
    return Promise.reject(error)
})

const app = createApp(App)
app.component("v-chart", ECharts)
app.config.globalProperties.$echarts = echarts
// axios.defaults.baseURL = 'http://localhost:8088'
app.use(quillEditor).use(router).use(ElementPlus).use(store).mount('#app')


import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { FontAwesomeIcon, FontAwesomeLayers, FontAwesomeLayersText } from '@fortawesome/vue-fontawesome'
library.add(fas, far, fab)
app.component('font-awesome-icon', FontAwesomeIcon)
app.component('font-awesome-layers', FontAwesomeLayers)
app.component('font-awesome-layers-text', FontAwesomeLayersText)