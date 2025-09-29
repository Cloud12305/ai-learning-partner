// src/utils/request.js
import axios from 'axios';
import { message, Spin } from 'ant-design-vue';

// 创建axios实例
const request = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL || '/',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    }
});

// 请求拦截器
let loadingInstance = null;
request.interceptors.request.use(
    (config) => {
        loadingInstance = Spin.service({
            text: '加载中...',
            fullscreen: true,
            background: 'rgba(0, 0, 0, 0.1)'
        });
        const token = localStorage.getItem('token');
        if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
    },
    (error) => {
        if (loadingInstance) loadingInstance.close();
        message.error('请求参数错误');
        return Promise.reject(error);
    }
);

// 响应拦截器
request.interceptors.response.use(
    (response) => {
        if (loadingInstance) loadingInstance.close();
        return response.data;
    },
    (error) => {
        if (loadingInstance) loadingInstance.close();
        const errMsg = error.response?.data?.msg || '网络异常，请重试';
        message.error(errMsg);
        if (error.response?.status === 401) window.location.href = '/login';
        return Promise.reject(error);
    }
);

export default request;