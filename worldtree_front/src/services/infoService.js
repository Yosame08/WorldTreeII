// src/services/infoService.js
import axios from 'axios';
import store from '@/services/loadService';

export const fetchRankData = async (token) => {
    store.commit('load');
    try {
        return await axios.post('/api/rank', token);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const fetchBbsTitles = async (token) => {
    store.commit('load');
    try {
        return await axios.post('/api/bbs', token);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
}