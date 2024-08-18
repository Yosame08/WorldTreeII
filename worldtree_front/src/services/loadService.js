// src/services/loadService.js
import { createStore } from "vuex";

const store = createStore({
    state () {
        return {
            load: 0
        }
    },
    mutations: {
        load (state) {
            state.load++;
            console.log('load', state.load);
        },
        finish (state) {
            state.load--;
            console.log('finish', state.load);
        }
    }
})

export default store;