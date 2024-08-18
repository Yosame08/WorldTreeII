// src/services/userService.js
import axios from 'axios';
import store from '@/services/loadService';

export const fetchCaptcha = async () => {
    store.commit('load');
    try {
        return await axios.get('/api/captcha');
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const signup = async (data) => {
    store.commit('load');
    try {
        return await axios.post('/api/signup', data);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const login = async (data) => {
    store.commit('load');
    try {
        return await axios.post('/api/login', data);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const logout = async (token) => {
    store.commit('load');
    try {
        return await axios.post('/api/logout', token);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};