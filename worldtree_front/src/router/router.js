import { createRouter, createWebHistory } from 'vue-router'

import Index from '@/components/Index.vue'
import Map from '@/components/Map.vue'
import Rank from '@/components/Rank.vue'
import BBS from '@/components/BBS.vue'
import Login from '@/components/account/Login.vue'
import Signup from "@/components/account/Signup.vue";

const routes = [
    { path: '/', component: Index },
    { path: '/map', component: Map },
    { path: '/rank', component: Rank },
    { path: '/bbs', component: BBS },
    { path: '/login', component: Login },
    { path: '/signup', component: Signup },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router