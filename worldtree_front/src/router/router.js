import { createRouter, createWebHistory } from 'vue-router'

import Index from '../components/Index.vue'
import Rank from '../components/Rank.vue'
import BBS from '../components/BBS.vue'

const routes = [
    { path: '/', component: Index },
    { path: '/rank', component: Rank },
    { path: '/bbs', component: BBS },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router