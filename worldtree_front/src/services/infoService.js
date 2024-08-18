// src/services/infoService.js
import axios from 'axios';
import store from '@/services/loadService';

export const fetchRankData = async () => {
    store.commit('load');
    try {
        return await axios.get('/api/rank');
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};