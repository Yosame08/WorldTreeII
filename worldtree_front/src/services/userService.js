// src/services/userService.js
import axios from 'axios';

export const signup = (data) => {
    return axios.post('/api/signup', data);
};

export const fetchCaptcha = () => {
    return axios.get('/api/captcha');
};

export const login = (data) => {
    return axios.post('/api/login', data);
};

export const logout = () => {
    return axios.post('/api/logout');
};