// src/services/userService.js
import axios from 'axios';

export const login = (credentials) => {
    return axios.post('/api/login', credentials);
};

export const signup = (credentials) => {
    return axios.post('/api/signup', credentials);
};

export const logout = () => {
    return axios.post('/api/logout');
};