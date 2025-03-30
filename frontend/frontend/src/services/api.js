import axios from 'axios';

// Create axios instance with base URL
const api = axios.create({
    baseURL: '/api'
});

// Add request interceptor for adding auth token
api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default api;