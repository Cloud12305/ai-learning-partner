import http from '@/utils/http'

// 基础 URL
const BASE_URL = '/api/academic-profile'

// 学生用户API
export const getAcademicOverview = () => http.get(`${BASE_URL}/current/overview`)

export const getCourses = (limit = 5) => http.get(`${BASE_URL}/current/courses`, { params: { limit } })
export const getAllCourses = () => http.get(`${BASE_URL}/current/courses/all`)

export const getLabProjects = (limit = 2) => http.get(`${BASE_URL}/current/labs`, { params: { limit } })
export const getAllLabs = () => http.get(`${BASE_URL}/current/labs/all`)

export const getBooks = (limit = 4) => http.get(`${BASE_URL}/current/books`, { params: { limit } })
export const getAllBooks = () => http.get(`${BASE_URL}/current/books/all`)

// 管理员API
export const adminCourseApi = {
    getAll: () => http.get(`${BASE_URL}/admin/courses`),
    create: (data) => http.post(`${BASE_URL}/admin/courses`, data),
    update: (id, data) => http.put(`${BASE_URL}/admin/courses/${id}`, data),
    delete: (id) => http.delete(`${BASE_URL}/admin/courses/${id}`)
}

export const adminLabApi = {
    getAll: () => http.get(`${BASE_URL}/admin/labs`),
    create: (data) => http.post(`${BASE_URL}/admin/labs`, data),
    update: (id, data) => http.put(`${BASE_URL}/admin/labs/${id}`, data),
    delete: (id) => http.delete(`${BASE_URL}/admin/labs/${id}`)
}

export const adminBookApi = {
    getAll: () => http.get(`${BASE_URL}/admin/books`),
    create: (data) => http.post(`${BASE_URL}/admin/books`, data),
    update: (id, data) => http.put(`${BASE_URL}/admin/books/${id}`, data),
    delete: (id) => http.delete(`${BASE_URL}/admin/books/${id}`)
}

// 默认导出
export default {
    getAcademicOverview,
    getCourses,
    getAllCourses,
    getLabProjects,
    getAllLabs,
    getBooks,
    getAllBooks,
    adminCourseApi,
    adminLabApi,
    adminBookApi
}