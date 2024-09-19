import axios from 'axios';
import store from '@/services/storeService';

export const universalGet = async (url) => {
    store.commit('load');
    try {
        return await axios.get(url);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
}

export const universalPost = async (url, data) => {
    store.commit('load');
    try {
        return await axios.post(url, data);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
}