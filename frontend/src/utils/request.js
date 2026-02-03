// src/utils/request.js
import axios from 'axios';
import { message,  } from 'ant-design-vue';

// Create axios instance
const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 90000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
});

// Request interceptor
request.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
    },
    (error) => {
        message.error('请求参数错误');
        return Promise.reject(error);
    }
);

// Response interceptor
request.interceptors.response.use(
    (response) => {
        // backend returns: { success, data, msg? }
        return response.data;
    },
    (error) => {
        const errMsg = error.response?.data?.msg || '网络异常，请重试';
        message.error(errMsg);
        if (error.response?.status === 401) window.location.href = '/login';
        return Promise.reject(error);
    }
);


export default request;