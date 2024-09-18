import axios from "axios";
import store from "@/services/storeService";
import {universalGet} from "@/services/universalService";

export const getTask = async (taskID) => {
    return await universalGet(`/api/task/${taskID}`);
};

export const requestHint = async (taskID) => {
    return await universalGet(`/api/tasks/${taskID}/hint`);
}

export const submitTask = async (taskID, answer) => {
    store.commit('load');
    try {
        return await axios.post(`/api/tasks/${taskID}/submit`, { answer })
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
};

export const updateVisiting = async (options) => {
    store.commit('load');
    try {
        return await axios.post(`/api/subtask/visiting/update`, options);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
}