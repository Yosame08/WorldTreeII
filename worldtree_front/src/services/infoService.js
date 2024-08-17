// src/services/infoService.js
import axios from 'axios';

export const fetchRankData = () => {
    return axios.get('/api/rank');
};