// src/store/modules/academicProfile.js
import { academicProfileApi, userApi } from '@/services/api'

export default {
    state: {
        profile: null,
        adminCourses: [],
        adminLabs: [],
        adminBooks: [],
        adminStudents: [],
        allCourses: [],
        allLabs: [],
        allBooks: [],
        loading: false,
        error: null
    },

    mutations: {
        SET_PROFILE(state, profile) {
            state.profile = profile
        },
        SET_ADMIN_COURSES(state, courses) {
            state.adminCourses = courses
        },
        SET_ADMIN_LABS(state, labs) {
            state.adminLabs = labs
        },
        SET_ADMIN_BOOKS(state, books) {
            state.adminBooks = books
        },
        SET_ADMIN_STUDENTS(state, students) {
            state.adminStudents = students
        },
        SET_ALL_COURSES(state, courses) {
            state.allCourses = courses
        },
        SET_ALL_LABS(state, labs) {
            state.allLabs = labs
        },
        SET_ALL_BOOKS(state, books) {
            state.allBooks = books
        },
        SET_LOADING(state, loading) {
            state.loading = loading
        },
        SET_ERROR(state, error) {
            state.error = error
        }
    },

    actions: {
        // 获取学业画像
        async fetchAcademicProfile({ commit }) {
            try {
                commit('SET_LOADING', true)
                commit('SET_ERROR', null)

                const response = await academicProfileApi.getCurrentProfile()

                // 根据后端响应结构处理
                if (response.code === 200 || response.success) {
                    const data = response.data || response
                    commit('SET_PROFILE', data)
                } else {
                    throw new Error(response.message || '获取数据失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message || '获取学业画像失败')
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 学生：获取所有课程
        async fetchAllCoursesForStudent({ commit }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.getAllCourses()

                if (response.code === 200 || response.success) {
                    const data = response.data || response
                    commit('SET_ALL_COURSES', data)
                    return data
                } else {
                    throw new Error(response.message || '获取数据失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 学生：获取所有实验室记录
        async fetchAllLabsForStudent({ commit }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.getAllLabs()

                if (response.code === 200 || response.success) {
                    const data = response.data || response
                    commit('SET_ALL_LABS', data)
                    return data
                } else {
                    throw new Error(response.message || '获取数据失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 学生：获取所有图书记录
        async fetchAllBooksForStudent({ commit }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.getAllBooks()

                if (response.code === 200 || response.success) {
                    const data = response.data || response
                    commit('SET_ALL_BOOKS', data)
                    return data
                } else {
                    throw new Error(response.message || '获取数据失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：获取所有课程
        async fetchAllCourses({ commit }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.getAdminCourses()

                if (response.code === 200 || response.success) {
                    const data = response.data || response
                    commit('SET_ADMIN_COURSES', data)
                    return data
                } else {
                    throw new Error(response.message || '获取数据失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：创建课程
        async createCourse({ commit }, courseData) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.createCourse(courseData)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '创建失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：更新课程
        async updateCourse({ commit }, { id, data }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.updateCourse(id, data)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '更新失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：删除课程
        async deleteCourse({ commit }, id) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.deleteCourse(id)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '删除失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：获取所有实验室记录
        async fetchAllLabs({ commit }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.getAdminLabs()

                if (response.code === 200 || response.success) {
                    const data = response.data || response
                    commit('SET_ADMIN_LABS', data)
                    return data
                } else {
                    throw new Error(response.message || '获取数据失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：创建实验室记录
        async createLab({ commit }, labData) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.createLab(labData)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '创建失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：更新实验室记录
        async updateLab({ commit }, { id, data }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.updateLab(id, data)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '更新失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：删除实验室记录
        async deleteLab({ commit }, id) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.deleteLab(id)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '删除失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：获取所有图书记录
        async fetchAllBooks({ commit }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.getAdminBooks()

                if (response.code === 200 || response.success) {
                    const data = response.data || response
                    commit('SET_ADMIN_BOOKS', data)
                    return data
                } else {
                    throw new Error(response.message || '获取数据失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：创建图书记录
        async createBook({ commit }, bookData) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.createBook(bookData)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '创建失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：更新图书记录
        async updateBook({ commit }, { id, data }) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.updateBook(id, data)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '更新失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：删除图书记录
        async deleteBook({ commit }, id) {
            try {
                commit('SET_LOADING', true)
                const response = await academicProfileApi.deleteBook(id)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '删除失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：获取所有学生
        async fetchAllStudents({ commit }, params = {}) {
            try {
                commit('SET_LOADING', true)
                const response = await userApi.getStudentUsers(params)

                if (response.code === 200 || response.success) {
                    const data = response.data || response
                    commit('SET_ADMIN_STUDENTS', data)
                    return data
                } else {
                    throw new Error(response.message || '获取数据失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        },

        // 管理员：更新学生状态
        async updateStudentStatus({ commit }, { id, status }) {
            try {
                commit('SET_LOADING', true)
                const response = await userApi.updateUserRole(id, status)

                if (response.code === 200 || response.success) {
                    return response.data || response
                } else {
                    throw new Error(response.message || '更新失败')
                }
            } catch (error) {
                commit('SET_ERROR', error.message)
                throw error
            } finally {
                commit('SET_LOADING', false)
            }
        }
    },

    getters: {
        profile: state => state.profile,
        adminCourses: state => state.adminCourses,
        adminLabs: state => state.adminLabs,
        adminBooks: state => state.adminBooks,
        adminStudents: state => state.adminStudents,
        allCourses: state => state.allCourses,
        allLabs: state => state.allLabs,
        allBooks: state => state.allBooks,
        loading: state => state.loading,
        error: state => state.error
    }
}