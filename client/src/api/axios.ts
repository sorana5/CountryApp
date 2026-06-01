import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

api.interceptors.response.use(
    res => res,
    err => {
        const message = err.response?.data?.message || 'An unexpected error occurred';
        const errorCode = err.response?.data?.errorCode || 'UNKNOWN';
        return Promise.reject({ message, errorCode, status: err.response?.status });
    }
);

export default api;