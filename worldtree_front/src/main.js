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
import moment from "moment";

// 添加请求拦截器
axios.interceptors.request.use(function (config) {
    // 判断是否存在token,如果存在将每个页面header添加token
    if (sessionStorage.getItem("token")) {
        config.headers.Authorization = sessionStorage.getItem("token");
    }
    return config
}, function (error) {
    router.push('/login')
    return Promise.reject(error)
})

axios.interceptors.response.use(
    response => {
        if (response.data.code !== 0) {
            store.commit("setErrorMsg", response.data.message);
            return Promise.reject(response.data.message);
        }
        return response;
    },
    error => {
        store.commit("setErrorMsg", error.message);
        return Promise.reject(error);
    }
);

const app = createApp(App)
app.component("v-chart", ECharts)
app.config.globalProperties.$echarts = echarts
axios.defaults.baseURL = 'http://10.20.26.32:8080/'

moment.locale("zh-CN");
app.use(moment).use(quillEditor).use(router).use(ElementPlus).use(store).mount('#app')


import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'
import { FontAwesomeIcon, FontAwesomeLayers, FontAwesomeLayersText } from '@fortawesome/vue-fontawesome'
library.add(fas, far, fab)
app.component('font-awesome-icon', FontAwesomeIcon)
app.component('font-awesome-layers', FontAwesomeLayers)
app.component('font-awesome-layers-text', FontAwesomeLayersText)