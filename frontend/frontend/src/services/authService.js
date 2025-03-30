import api from './api';

export const login = async (email, password) => {
    const response = await api.post('/user/login', null, { params: { email, password } });
    return response.data;
};

export const register = async (userData) => {
    const response = await api.post('/user/register', userData);
    return response.data;
};
