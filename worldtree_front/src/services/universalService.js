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