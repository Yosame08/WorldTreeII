// src/services/storeService.js
import { createStore } from "vuex";
import { get_info } from "@/services/userService";

const store = createStore({
    state () {
        return {
            load: 0,
            isLoggedIn: !!sessionStorage.getItem('token'),
            showNavBar: true,
            userInfo: {},
        }
    },
    mutations: {
        load (state) {
            state.load++;
            // console.log('load', state.load);
        },
        finish (state) {
            if (state.load) state.load--;
            // console.log('finish', state.load);
        },
        setUserInfo(state, userInfo) {
            state.userInfo = userInfo;
        },
        setShowNavBar(state, value) {
            state.showNavBar = value;
        },
    },
    actions: {
        async fetchUserInfo({ commit }) {
            try {
                const response = await get_info();
                commit('setUserInfo', response.data.data);
            } catch (error) {
                console.error('Failed to fetch user info', error);
            }
        },
    }
});

export default store;
