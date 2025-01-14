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
            showMsg: '',
            isError: true,
            msgTimer: null,
        }
    },
    mutations: {
        load (state) {
            state.load++;
        },
        finish (state) {
            if (state.load) state.load--;
        },
        setUserInfo(state, userInfo) {
            state.userInfo = userInfo;
        },
        setShowNavBar(state, value) {
            state.showNavBar = value;
        },
        setLoggedIn(state, value) {
            state.isLoggedIn = value;
        },
        setErrorMsg(state, value) {
            this.commit('clearTimer');
            state.showMsg = value;
            state.isError = true;
            this.commit('setTimer', setTimeout(() => {
                this.commit('clearErrorMsg');
            }, 8000));
        },
        setSuccessMsg(state, value) {
            this.commit('clearTimer');
            state.showMsg = value;
            state.isError = false;
            this.commit('setTimer', setTimeout(() => {
                this.commit('clearErrorMsg');
            }, 8000));
        },
        clearErrorMsg(state) {
            state.showMsg = '';
        },
        clearTimer(state) {
            if(state.msgTimer) clearTimeout(state.msgTimer);
        },
        setTimer(state, timer) {
            state.msgTimer = timer;
        }
    },
    actions: {
        async fetchUserInfo({ commit }) {
            try {
                const response = await get_info();
                commit('setUserInfo', response.data.data);
            } catch (error) {
            }
        },
    }
});

export default store;
