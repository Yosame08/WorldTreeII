import { createRouter, createWebHistory } from 'vue-router'

import Index from '@/components/Index.vue'
import Map from '@/components/Map.vue'
import Rank from '@/components/Rank.vue'
import BBS from '@/components/BBS.vue'
import Login from '@/components/account/Login.vue'
import Signup from "@/components/account/Signup.vue";
import UserSetting from "@/components/account/UserSetting.vue";

import store from '@/services/storeService';

const routes = [
    { path: '/', component: Index },
    { path: '/map', component: Map },
    { path: '/rank', component: Rank },
    { path: '/bbs', component: BBS },
    { path: '/login', component: Login },
    { path: '/signup', component: Signup },
    { path: '/usersetting', component: UserSetting },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    console.log('to', to.path, 'from', from.path, 'isLoggedIn', store.state.isLoggedIn);
    if (to.path !== '/' && to.path !== '/signup' && to.path !== '/login' && !store.state.isLoggedIn) {
        next('/login');
    } else {
        next();
    }
});

export default router