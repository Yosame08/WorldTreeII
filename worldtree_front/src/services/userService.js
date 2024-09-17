// src/services/userService.js
import axios from 'axios';
import store from '@/services/storeService';
import {universalGet} from "@/services/universalService";

export const fetchCaptcha = async () => {
    return await universalGet('/api/user/captcha');
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
        return await axios.post('/api/user/set_info', data, {
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const get_info = async () => {
    return await universalGet('/api/user/get_info');
};