import api from './api';

export const login = async (email, password) => {
    try {
        const response = await api.post('/user/login', null, {
            params: { email, password }
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const register = async (userData) => {
    try {
        const response = await api.post('/user/register', userData);
        return response.data;
    } catch (error) {
        throw error;
    }
};