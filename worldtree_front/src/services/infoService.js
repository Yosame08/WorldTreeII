// src/services/infoService.js
import axios from 'axios';
import store from '@/services/storeService';

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

export const fetchBbsTitles = async () => {
    store.commit('load');
    try {
        return await axios.get('/api/bbs');
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
}