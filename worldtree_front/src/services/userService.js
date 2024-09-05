// src/services/userService.js
import axios from 'axios';
import store from '@/services/storeService';

export const fetchCaptcha = async () => {
    store.commit('load');
    try {
        return await axios.get('/api/user/captcha');
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const signup = async (data) => {
    store.commit('load');
    try {
        return await axios.post('/api/user/signup', data);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const login = async (data) => {
    store.commit('load');
    try {
        return await axios.post('/api/user/login', data);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const set_info = async (data) => {
    store.commit('load');
    try {
        return await axios.post('/api/user/set_info', data);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const get_info = async () => {
    store.commit('load');
    try {
        return await axios.get('/api/user/get_info');
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};