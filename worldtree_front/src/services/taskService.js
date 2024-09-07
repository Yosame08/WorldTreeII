import axios from "axios";
import store from "@/services/storeService";

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

export const requestHint = async (taskID) => {
    store.commit('load');
    try {
        return await axios.get(`/api/tasks/${taskID}/hint`);
    } catch (error) {
        throw error;
    } finally {
        store.commit('finish');
    }
}
