import { createRouter, createWebHistory } from 'vue-router'

import Index from '@/components/Index.vue'
import Login from '@/components/account/Login.vue'
import Signup from "@/components/account/Signup.vue";
import UserSetting from "@/components/account/UserSetting.vue";
import Beginning from "@/components/Beginning.vue";

import Nim from "@/components/tasks/Nim.vue";
import Skittles from "@/components/tasks/Skittles.vue";
import BigPot from "@/components/tasks/BigPot.vue";
import ZhengYan from "@/components/tasks/ZhengYan.vue";
import Waffle from "@/components/tasks/Waffle.vue";
import Cake from "@/components/tasks/Cake.vue";
import Visiting from "@/components/tasks/Visiting.vue";

import store from '@/services/storeService';
import Hidden_relic from "@/components/tasks/hidden_relic.vue";
import TartFinish from "@/components/tasks/TartFinish.vue";
import Telegram from "@/components/tasks/Telegram.vue";
import TelegramRank from "@/components/tasks/TelegramRank.vue";
import OpenBox from "@/components/tasks/OpenBox.vue";

const routes = [
    { path: '/', component: Index, meta: { title: "反转！蒸旦宇宙" } },
    { path: '/beginning', component: Beginning, meta: { title: "反转！蒸旦宇宙" } },
    { path: '/login', component: Login, meta: { title: "登录 - 反转！蒸旦宇宙" } },
    { path: '/signup', component: Signup, meta: { title: "注册 - 反转！蒸旦宇宙" } },
    { path: '/usersetting', component: UserSetting, meta: { title: "个人信息 - 反转！蒸旦宇宙" }  },
    { path: '/telegram', component: Telegram, meta: { title: "编码 - 反转！蒸旦宇宙" } },
    { path: '/telegramRank', component: TelegramRank, meta: { title: "电报排行榜 - 反转！蒸旦宇宙" } },

    { path: '/tasks/bigpot', component: BigPot, meta: { title: "大锅饭 - 反转！蒸旦宇宙" } },
    { path: '/tasks/visiting', component: Visiting, meta: { title: "探店 - 反转！蒸旦宇宙" } },
    { path: '/tasks/cake', component: Cake, meta: { title: "生日蛋糕 - 反转！蒸旦宇宙" } },
    { path: '/tasks/skittles', component: Skittles, meta: { title: "掰与狼与孤独摇滚 - 反转！蒸旦宇宙" } },
    { path: '/tasks/waffle', component: Waffle, meta: { title: "华夫饼 - 反转！蒸旦宇宙" } },
    { path: '/inter-universe/paky-and-tarts', component: TartFinish, meta: {title: "蛋挞 - 反转！蒸旦宇宙" } },

    { path: '/tasks/openbox', component: OpenBox, meta: { title: "开盒 - 反转！蒸旦宇宙" } },

    { path: '/hidden_relic/000', component: Hidden_relic, meta: { title: "失落的遗物" } },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, from, next) => {
    if (to.meta.title){
        document.title = to.meta.title;
    }
    if (to.path === '/' && !store.state.isLoggedIn) {
        next('/beginning');
    }
    else if (to.path !== '/beginning' && to.path !== '/signup' && to.path !== '/login' && !store.state.isLoggedIn) {
        next('/login');
    } else {
        next();
    }
});

export default router
