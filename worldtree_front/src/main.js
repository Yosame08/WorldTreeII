import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import router from './router/router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import ECharts from 'vue-echarts'
import * as echarts from 'echarts'

import './mock.js'
import store from './services/loadService'

const app = createApp(App)
app.component("v-chart", ECharts)
app.config.globalProperties.$echarts = echarts
// axios.defaults.baseURL = 'http://localhost:8088'
app.use(router).use(ElementPlus).use(store).mount('#app')