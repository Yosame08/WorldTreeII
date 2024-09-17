import { createRouter, createWebHistory } from 'vue-router'

import Index from '@/components/Index.vue'
import Map from '@/components/Map.vue'
import Rank from '@/components/Rank.vue'
import BBS from '@/components/BBS.vue'
import Login from '@/components/account/Login.vue'
import Signup from "@/components/account/Signup.vue";
import UserSetting from "@/components/account/UserSetting.vue";
import Beginning from "@/components/Beginning.vue";

import Nim from "@/components/tasks/Nim.vue";
import Skittles from "@/components/tasks/Skittles.vue";
import BigPot from "@/components/tasks/BigPot.vue";
import ZhengYan from "@/components/tasks/ZhengYan.vue";
import Cake from "@/components/tasks/Cake.vue";
import Visiting from "@/components/tasks/Visiting.vue";

import store from '@/services/storeService';

const routes = [
    { path: '/', component: Index },
    { path: '/map', component: Map },
    { path: '/rank', component: Rank },
    { path: '/bbs', component: BBS },
    { path: '/login', component: Login },
    { path: '/signup', component: Signup },
    { path: '/usersetting', component: UserSetting },
    { path: '/nim_game', component: Nim},
    { path: '/beginning', component: Beginning },

    { path: '/tasks/nim', component: Nim },
    { path: '/tasks/skittles', component: Skittles },
    { path: '/tasks/bigpot', component: BigPot },
    { path: '/tasks/zhengyan', component: ZhengYan },
    { path: '/tasks/cake', component: Cake },
    { path: '/tasks/visiting', component: Visiting },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    if (to.path === '/' && !store.state.isLoggedIn){
        next('/beginning');
    }
    else if (to.path !== '/beginning' && to.path !== '/signup' && to.path !== '/login' && !store.state.isLoggedIn) {
        next('/login');
    } else {
        next();
    }
});

export default router
