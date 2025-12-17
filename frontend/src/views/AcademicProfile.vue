<template>
  <div class="relative min-h-screen font-inter antialiased bg-gradient-to-br from-blue-50 to-indigo-50">
    <TopNavbar />

    <main class="pt-20">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-12">
          <!-- 页面头部 -->
          <PageHeader />

          <!-- 加载状态 -->
          <LoadingState v-if="loading" />

          <!-- 错误状态 -->
          <ErrorState v-else-if="error" :error="error" @reload="loadData" />

          <!-- 内容区域 -->
          <template v-else>
            <!-- 学生视图 -->
            <StudentView
                v-if="!isAdmin"
                :profile="profile"
                :learning-goal="learningGoal"
                @show-all-courses="showAllCourses"
                @show-all-labs="showAllLabs"
                @show-all-books="showAllBooks"
                @edit-learning-goal="showEditLearningGoal"
                @update-info="updateUserInfo"
            />

            <!-- 管理员视图 -->
            <AdminView
                v-else
                :admin-active-tab="adminActiveTab"
                :admin-courses="adminCourses"
                :admin-labs="adminLabs"
                :admin-books="adminBooks"
                :admin-students="adminStudents"
                :admin-info="adminInfo"
                :admin-stats="adminStats"
                @switch-tab="switchAdminTab"
                @add-course="showAddCourseDialog"
                @edit-course="editCourse"
                @delete-course="deleteCourse"
                @add-lab="showAddLabDialog"
                @edit-lab="editLab"
                @delete-lab="deleteLab"
                @add-book="showAddBookDialog"
                @edit-book="editBook"
                @delete-book="deleteBook"
                @add-student="showAddStudentDialog"
                @edit-student="editStudent"
                @toggle-status="toggleStudentStatus"
                @delete-student="deleteStudent"
                @update-admin-info="updateAdminInfo"
            />
          </template>
        </div>
      </div>
    </main>

    <PageFooter />

    <!-- 对话框组件 -->
    <AddCourseDialog
        v-if="addCourseDialogVisible"
        :visible="addCourseDialogVisible"
        @close="closeAddCourseDialog"
        @save="handleSaveCourse"
    />

    <EditCourseDialog
        v-if="editCourseDialogVisible"
        :visible="editCourseDialogVisible"
        :course="currentEditCourse"
        @close="closeEditCourseDialog"
        @save="handleUpdateCourse"
    />

    <AddLabDialog
        v-if="addLabDialogVisible"
        :visible="addLabDialogVisible"
        @close="closeAddLabDialog"
        @save="handleAddLab"
    />

    <EditLabDialog
        v-if="editLabDialogVisible"
        :visible="editLabDialogVisible"
        :lab="currentLab"
        @close="closeEditLabDialog"
        @save="handleEditLab"
    />

    <AddBookDialog
        v-if="addBookDialogVisible"
        :visible="addBookDialogVisible"
        @close="closeAddBookDialog"
        @save="handleAddBook"
    />

    <EditBookDialog
        v-if="editBookDialogVisible"
        :visible="editBookDialogVisible"
        :book-data="currentBook"
        @close="closeEditBookDialog"
        @save="handleEditBook"
    />

    <AddStudentsDialog
        v-if="addStudentDialogVisible"
        :visible="addStudentDialogVisible"
        @close="closeAddStudentDialog"
        @save="handleAddStudent"
    />

    <EditStudentsDialog
        v-if="editStudentDialogVisible"
        :visible="editStudentDialogVisible"
        :student-data="currentStudent"
        @close="closeEditStudentDialog"
        @save="handleEditStudent"
    />

    <EditLearningGoalDialog
        v-if="editGoalDialogVisible"
        :visible="editGoalDialogVisible"
        :learning-goal="learningGoal"
        @update:learning-goal="learningGoal = $event"
        @close="editGoalDialogVisible = false"
        @save="saveLearningGoal"
    />

    <ConfirmDialog
        v-if="confirmDialogVisible"
        :visible="confirmDialogVisible"
        :title="confirmDialogTitle || ''"
        :message="confirmDialogMessage || ''"
        @close="handleCloseConfirmDialog"
        @confirm="handleConfirmAction"
    />

    <!-- 查看全部对话框 -->
    <ViewAllDialog
        v-if="viewAllDialogVisible"
        :visible="viewAllDialogVisible"
        :title="viewAllDialogTitle"
        :type="viewAllDialogType"
        :data="viewAllDialogData"
        @close="closeViewAllDialog"
    />
  </div>
</template>

<script>
import {ref, onMounted} from 'vue'
import TopNavbar from '../component/TopNavbar.vue'
import PageFooter from '../component/PageFooter.vue'
import PageHeader from '../component/PageHeader.vue'
import LoadingState from '../component/LoadingState.vue'
import ErrorState from '../component/ErrorState.vue'
import StudentView from '../component/StudentView.vue'
import AdminView from '../component/AdminView.vue'
import AddCourseDialog from '../component/AddCourseDialog.vue'
import EditCourseDialog from '../component/EditCourseDialog.vue'
import AddLabDialog from '../component/AddLabDialog.vue'
import EditLabDialog from '../component/EditLabDialog.vue'
import AddBookDialog from '../component/AddBookDialog.vue'
import EditBookDialog from '../component/EditBookDialog.vue'
import AddStudentsDialog from '../component/AddStudentsDialog.vue'
import EditStudentsDialog from '../component/EditStudentsDialog.vue'
import EditLearningGoalDialog from '../component/EditLearningGoalDialog.vue'
import ConfirmDialog from '../component/ConfirmDialog.vue'
import ViewAllDialog from '../component/ViewAllDialog.vue'


import {academicProfileApi, userApi} from '@/services/api'


export default {
  name: 'AcademicProfileView',

  components: {
    TopNavbar,
    PageFooter,
    PageHeader,
    LoadingState,
    ErrorState,
    StudentView,
    AdminView,
    AddCourseDialog,
    EditCourseDialog,
    EditLearningGoalDialog,
    ConfirmDialog,
    ViewAllDialog,
    AddLabDialog,
    EditLabDialog,
    AddBookDialog,
    EditBookDialog,
    AddStudentsDialog,
    EditStudentsDialog
  },

  setup() {
    // 状态管理
    const loading = ref(true)
    const error = ref(null)
    const profile = ref({
      userInfo: {
        learningGoal: ''
      },
      courses: [],
      labs: [],
      books: [],
      stats: {}
    })
    const isAdmin = ref(false)

    // 学习相关数据
    const learningGoal = ref('')

    // 管理员状态
    const adminActiveTab = ref('courses')
    const adminCourses = ref([])
    const adminLabs = ref([])
    const adminBooks = ref([])
    const adminStudents = ref([])
    const adminInfo = ref({})
    const adminStats = ref({})

    // 对话框状态
    const viewAllDialogVisible = ref(false)
    const viewAllDialogTitle = ref('')
    const viewAllDialogType = ref('')
    const viewAllDialogData = ref([])

    const editGoalDialogVisible = ref(false)
    const confirmDialogVisible = ref(false)
    const confirmDialogTitle = ref('')
    const confirmDialogMessage = ref('')

    // 课程相关对话框
    const addCourseDialogVisible = ref(false)
    const editCourseDialogVisible = ref(false)
    const currentEditCourse = ref(null)
    const dialogCallback = ref(null)

    // 实验室管理状态
    const addLabDialogVisible = ref(false)
    const editLabDialogVisible = ref(false)
    const currentLab = ref(null)

    // 图书管理状态
    const addBookDialogVisible = ref(false)
    const editBookDialogVisible = ref(false)
    const currentBook = ref(null)

    // 学生管理状态
    const addStudentDialogVisible = ref(false)
    const editStudentDialogVisible = ref(false)
    const currentStudent = ref(null)

    // 通用方法
    const showConfirmDialog = (title, message, callback = null) => {
      confirmDialogTitle.value = title || ''
      confirmDialogMessage.value = message || ''
      dialogCallback.value = callback
      confirmDialogVisible.value = true
    }

    const handleCloseConfirmDialog = () => {
      confirmDialogVisible.value = false
      confirmDialogTitle.value = ''
      confirmDialogMessage.value = ''
      dialogCallback.value = null
    }

    const handleConfirmAction = () => {
      if (dialogCallback.value && typeof dialogCallback.value === 'function') {
        try {
          dialogCallback.value()
        } catch (err) {
          console.error('回调函数执行失败:', err)
        }
      }
      handleCloseConfirmDialog()
    }

    // 检查用户角色
    const checkUserRole = () => {
      try {
        const user = JSON.parse(localStorage.getItem('user') || '{}')
        isAdmin.value = user.role === 'ADMIN'
        return isAdmin.value
      } catch {
        isAdmin.value = false
        return false
      }
    }

    // 加载数据
    const loadData = async () => {
      try {
        loading.value = true
        error.value = null

        const userIsAdmin = checkUserRole()

        if (userIsAdmin) {
          await loadAdminData()
        } else {
          await loadAcademicProfile()
        }
      } catch (err) {
        error.value = err.message || '加载数据失败，请稍后重试'
        console.error('加载数据失败:', err)
      } finally {
        loading.value = false
      }
    }

    // 加载学生学业画像
    const loadAcademicProfile = async () => {
      try {
        const response = await academicProfileApi.getCurrentProfile()
        if (response.code === 200 || response.success) {
          const data = response.data || response
          profile.value = {
            userInfo: {
              learningGoal: '',
              ...data.userInfo
            },
            courses: data.courses?.map(course => ({
              ...course,
              gradePoint: course.grade_point || course.gradePoint,
              creditEarned: course.credit_earned || course.creditEarned
            })) || [],
            labs: data.labs || [],
            books: data.books || [],
            stats: data.stats || {},
            ...data
          }
          loadLearningGoal()
        }
      } catch (err) {
        console.error('加载学业画像失败:', err)
      }
    }

    // 加载管理员数据
    const loadAdminData = async () => {
      try {
        await loadAdminInfo()

        switch (adminActiveTab.value) {
          case 'courses': {
            const coursesRes = await academicProfileApi.getAdminCourses()
            if (coursesRes.code === 200 || coursesRes.success) {
              adminCourses.value = coursesRes.data || coursesRes
            }
            break
          }
          case 'labs': {
            const labsRes = await academicProfileApi.getAdminLabs()
            if (labsRes.code === 200 || labsRes.success) {
              adminLabs.value = labsRes.data || labsRes
            }
            break
          }
          case 'books': {
            const booksRes = await academicProfileApi.getAdminBooks()
            if (booksRes.code === 200 || booksRes.success) {
              adminBooks.value = booksRes.data || booksRes
            }
            break
          }
          case 'students': {
            const studentsRes = await userApi.getStudentUsers()
            if (studentsRes.code === 200 || studentsRes.success) {
              adminStudents.value = studentsRes.data || studentsRes
            }
            break
          }
        }
      } catch (err) {
        console.error('加载管理员数据失败:', err)
      }
    }

    // 加载管理员信息
    const loadAdminInfo = async () => {
      try {
        const currentUser = JSON.parse(localStorage.getItem('user') || '{}')
        if (!currentUser.id) {
          console.error('管理员ID不存在')
          return
        }

        // 获取管理员基本信息
        try {
          const userResponse = await userApi.getUserById(currentUser.id)
          if (userResponse.code === 200 || userResponse.success) {
            const userData = userResponse.data || userResponse
            adminInfo.value = {
              id: userData.id,
              employeeId: userData.student_id || userData.studentId,
              name: userData.name,
              username: userData.username,
              email: userData.email || '',
              phone: userData.phone || '',
              avatarUrl: userData.avatar_url || userData.avatarUrl,
              lastLoginTime: userData.last_login_time || userData.lastLoginTime,
              loginCount: userData.login_count || userData.loginCount || 0,
              role: userData.role || 'ADMIN',
              major: userData.major || '',
              college: userData.college || ''
            }
          }
        } catch (userErr) {
          console.error('获取管理员基本信息失败:', userErr)
        }

        // 获取统计数据
        await loadAdminStats()
      } catch (err) {
        console.error('加载管理员信息失败:', err)
      }
    }

    // 加载管理员统计数据
    const loadAdminStats = async () => {
      try {
        // 尝试从专用接口获取统计数据
        const statsResponse = await userApi.getAdminStats()
        if (statsResponse.code === 200 || statsResponse.success) {
          const statsData = statsResponse.data || statsResponse
          adminStats.value = {
            studentCount: statsData.totalStudents || statsData.studentCount || 0,
            maleCount: statsData.maleCount || 0,
            femaleCount: statsData.femaleCount || 0,
            activeCount: statsData.activeCount || 0
          }
          return
        }
      } catch (statsErr) {
        console.warn('统计接口调用失败，尝试从学生列表计算:', statsErr)
      }

      // 如果接口不可用，从学生列表计算
      try {
        const studentsRes = await userApi.getStudentUsers()
        if (studentsRes.code === 200 || studentsRes.success) {
          const students = studentsRes.data || studentsRes || []
          adminStats.value = {
            studentCount: students.length || 0,
            maleCount: students.filter(s => s.gender === 'M' || s.gender === '男').length || 0,
            femaleCount: students.filter(s => s.gender === 'F' || s.gender === '女').length || 0,
            activeCount: students.filter(s =>
                s.account_status === 'ACTIVE' ||
                !s.account_status ||
                s.account_status === 'active' ||
                s.status === 'ACTIVE' ||
                s.status === 'active'
            ).length || 0
          }
          return
        }
      } catch (err) {
        console.error('无法获取学生数据:', err)
      }

      // 设置默认值
      adminStats.value = {
        studentCount: 0,
        maleCount: 0,
        femaleCount: 0,
        activeCount: 0
      }
    }

    // 课程管理相关方法
    const showAddCourseDialog = () => {
      addCourseDialogVisible.value = true
    }

    const closeAddCourseDialog = () => {
      addCourseDialogVisible.value = false
    }

    const editCourse = (course) => {
      currentEditCourse.value = {...course}
      editCourseDialogVisible.value = true
    }

    const closeEditCourseDialog = () => {
      editCourseDialogVisible.value = false
      currentEditCourse.value = null
    }

    const deleteCourse = (courseId) => {
      showConfirmDialog('删除课程成绩', '确定要删除这条课程成绩记录吗？删除后不可恢复。', async () => {
        try {
          loading.value = true
          const response = await academicProfileApi.deleteCourse(courseId)
          if (response.code === 200 || response.success) {
            await loadAdminData()
            showConfirmDialog('删除成功', '课程成绩已删除')
          } else {
            showConfirmDialog('删除失败', response.message || '删除失败')
          }
        } catch (err) {
          console.error('删除课程失败:', err)
          showConfirmDialog('删除失败', '删除课程成绩时出错')
        } finally {
          loading.value = false
        }
      })
    }

    const handleSaveCourse = async (courseData) => {
      try {
        loading.value = true
        const response = await academicProfileApi.createCourse(courseData)
        if (response.code === 200 || response.success) {
          await loadAdminData()
          closeAddCourseDialog()
          showConfirmDialog('添加成功', '课程成绩已添加')
        } else {
          showConfirmDialog('添加失败', response.message || '添加失败')
        }
      } catch (err) {
        console.error('添加课程失败:', err)
        showConfirmDialog('添加失败', '添加课程成绩时出错')
      } finally {
        loading.value = false
      }
    }

    const handleUpdateCourse = async (courseData) => {
      try {
        loading.value = true
        const response = await academicProfileApi.updateCourse(courseData.recordId, courseData)
        if (response.code === 200 || response.success) {
          await loadAdminData()
          closeEditCourseDialog()
          showConfirmDialog('更新成功', '课程成绩已更新')
        } else {
          showConfirmDialog('更新失败', response.message || '更新失败')
        }
      } catch (err) {
        console.error('更新课程失败:', err)
        showConfirmDialog('更新失败', '更新课程成绩时出错')
      } finally {
        loading.value = false
      }
    }

    // 实验室管理方法
    const showAddLabDialog = () => {
      addLabDialogVisible.value = true
    }

    const closeAddLabDialog = () => {
      addLabDialogVisible.value = false
    }

    const editLab = (lab) => {
      currentLab.value = lab
      editLabDialogVisible.value = true
    }

    const closeEditLabDialog = () => {
      editLabDialogVisible.value = false
      currentLab.value = null
    }

    const deleteLab = (labId) => {
      showConfirmDialog('删除实验室记录', '确定要删除这条实验室记录吗？删除后不可恢复。', async () => {
        try {
          loading.value = true
          const response = await academicProfileApi.deleteLab(labId)
          if (response.code === 200 || response.success) {
            await loadAdminData()
            showConfirmDialog('删除成功', '实验室记录已删除')
          } else {
            showConfirmDialog('删除失败', response.message || '删除失败')
          }
        } catch (err) {
          console.error('删除实验室记录失败:', err)
          showConfirmDialog('删除失败', '删除实验室记录时出错')
        } finally {
          loading.value = false
        }
      })
    }

    const handleAddLab = async (labData) => {
      try {
        loading.value = true
        const response = await academicProfileApi.createLab(labData)
        if (response.code === 200 || response.success) {
          await loadAdminData()
          closeAddLabDialog()
          showConfirmDialog('添加成功', '实验室记录已添加')
        } else {
          showConfirmDialog('添加失败', response.message || '添加失败')
        }
      } catch (err) {
        console.error('添加实验室记录失败:', err)
        showConfirmDialog('添加失败', '添加实验室记录时出错')
      } finally {
        loading.value = false
      }
    }

    const handleEditLab = async (labData) => {
      try {
        loading.value = true
        const response = await academicProfileApi.updateLab(labData.attendanceId, labData)
        if (response.code === 200 || response.success) {
          await loadAdminData()
          closeEditLabDialog()
          showConfirmDialog('更新成功', '实验室记录已更新')
        } else {
          showConfirmDialog('更新失败', response.message || '更新失败')
        }
      } catch (err) {
        console.error('更新实验室记录失败:', err)
        showConfirmDialog('更新失败', '更新实验室记录时出错')
      } finally {
        loading.value = false
      }
    }

    // 图书管理方法
    const showAddBookDialog = () => {
      addBookDialogVisible.value = true
    }

    const closeAddBookDialog = () => {
      addBookDialogVisible.value = false
    }

    const editBook = (book) => {
      currentBook.value = {...book}
      editBookDialogVisible.value = true
    }

    const closeEditBookDialog = () => {
      editBookDialogVisible.value = false
      currentBook.value = null
    }

    const deleteBook = (bookId) => {
      showConfirmDialog('删除借阅记录', '确定要删除这条图书借阅记录吗？删除后不可恢复。', async () => {
        try {
          loading.value = true
          const response = await academicProfileApi.deleteBook(bookId)
          if (response.code === 200 || response.success) {
            await loadAdminData()
            showConfirmDialog('删除成功', '图书借阅记录已删除')
          } else {
            showConfirmDialog('删除失败', response.message || '删除失败')
          }
        } catch (err) {
          console.error('删除图书借阅记录失败:', err)
          showConfirmDialog('删除失败', '删除图书借阅记录时出错')
        } finally {
          loading.value = false
        }
      })
    }

    const handleAddBook = async (bookData) => {
      try {
        loading.value = true
        const response = await academicProfileApi.createBook(bookData)
        if (response.code === 200 || response.success) {
          await loadAdminData()
          closeAddBookDialog()
          showConfirmDialog('添加成功', '图书借阅记录已添加')
        } else {
          showConfirmDialog('添加失败', response.message || '添加失败')
        }
      } catch (err) {
        console.error('添加图书借阅记录失败:', err)
        showConfirmDialog('添加失败', '添加图书借阅记录时出错')
      } finally {
        loading.value = false
      }
    }

    const handleEditBook = async (bookData) => {
      try {
        loading.value = true
        if (!bookData.record_id) {
          showConfirmDialog('更新失败', '记录ID不存在')
          return
        }
        const response = await academicProfileApi.updateBook(bookData.record_id, bookData)
        if (response.code === 200 || response.success) {
          await loadAdminData()
          closeEditBookDialog()
          showConfirmDialog('更新成功', '图书借阅记录已更新')
        } else {
          showConfirmDialog('更新失败', response.message || '更新失败')
        }
      } catch (err) {
        console.error('更新图书借阅记录失败:', err)
        showConfirmDialog('更新失败', '更新图书借阅记录时出错')
      } finally {
        loading.value = false
      }
    }

    // 添加学生管理相关方法
    const showAddStudentDialog = () => {
      addStudentDialogVisible.value = true
    }

    const closeAddStudentDialog = () => {
      addStudentDialogVisible.value = false
    }

    const editStudent = (student) => {
      currentStudent.value = student
      editStudentDialogVisible.value = true
    }

    const closeEditStudentDialog = () => {
      editStudentDialogVisible.value = false
      currentStudent.value = null
    }

    const handleAddStudent = async (studentData) => {
      try {
        loading.value = true

        // 格式化数据以匹配后端 User 实体
        const formattedData = {
          username: studentData.username,
          password: studentData.password,
          name: studentData.name,
          studentId: studentData.studentId,
          email: studentData.email || '',
          phone: studentData.phone || '',
          college: studentData.college || '',
          major: studentData.major || '',
          className: studentData.className || '',
          grade: studentData.grade || '大一',
          gender: studentData.gender || '',
          enrollmentDate: studentData.enrollmentDate ?
              `${studentData.enrollmentDate}T00:00:00` : null,
          accountStatus: studentData.accountStatus || 'ACTIVE',
          role: 'STUDENT'
        }

        console.log('发送的用户数据:', JSON.stringify(formattedData, null, 2))

        const response = await userApi.createUser(formattedData)

        if (response.code === 200 || response.success) {
          await loadAdminData()
          closeAddStudentDialog()
          showConfirmDialog('添加成功', '学生信息已添加')
        } else {
          showConfirmDialog('添加失败', response.message || '添加失败')
        }
      } catch (err) {
        console.error('添加学生失败:', err)
        console.error('错误详情:', err.response?.data)
        showConfirmDialog('添加失败', err.response?.data?.message || '添加学生信息时出错')
      } finally {
        loading.value = false
      }
    }

    const handleEditStudent = async (studentData) => {
      try {
        loading.value = true
        // 确保数据格式正确
        const formattedData = {
          name: studentData.name,
          email: studentData.email || '',
          phone: studentData.phone || '',
          college: studentData.college || '',
          major: studentData.major || '',
          className: studentData.className || '',
          grade: studentData.grade || '大一',
          gender: studentData.gender || '',
          enrollmentDate: studentData.enrollmentDate ?
              `${studentData.enrollmentDate}T00:00:00` : null,
          accountStatus: studentData.accountStatus || 'ACTIVE'
        }

        // 清理空字符串
        Object.keys(formattedData).forEach(key => {
          if (formattedData[key] === '') {
            formattedData[key] = null
          }
        })
        const response = await userApi.updateUser(studentData.id, formattedData)

        if (response.code === 200 || response.success) {
          await loadAdminData()
          closeEditStudentDialog()
          showConfirmDialog('更新成功', '学生信息已更新')
        } else {
          showConfirmDialog('更新失败', response.message || '更新失败')
        }
      } catch (err) {
        console.error('更新学生失败:', err)
        console.error('错误详情:', err.response?.data)
        showConfirmDialog('更新失败', err.response?.data?.message || '更新学生信息时出错')
      } finally {
        loading.value = false
      }
    }

    const toggleStudentStatus = async ({studentId, newStatus, studentName}) => {
      const action = newStatus === 'ACTIVE' ? '激活' : '禁用'

      showConfirmDialog(
          `${action}学生`,
          `确定要${action}学生 ${studentName} 吗？`,
          async () => {
            try {
              loading.value = true
              // 发送更新请求
              const response = await userApi.updateUser(studentId, {
                accountStatus: newStatus
              })
              if (response.success || response.code === 200) {
                // 重新加载数据
                await loadAdminData()

                showConfirmDialog(
                    `${action}成功`,
                    `学生 ${studentName} 已${action}`,
                    null,
                    false,
                    'success'
                )
              } else {
                showConfirmDialog(
                    `${action}失败`,
                    response.message || '操作失败',
                    null,
                    false,
                    'error'
                )
              }
            } catch (err) {
              console.error(`${action}学生失败:`, err)
              showConfirmDialog(
                  `${action}失败`,
                  err.response?.data?.message || '操作失败',
                  null,
                  false,
                  'error'
              )
            } finally {
              loading.value = false
            }
          }
      )
    }

    // 添加状态格式化函数
    const formatStatus = (status) => {
      const statusMap = {
        'ACTIVE': '正常',
        'INACTIVE': '禁用',
        'PENDING': '待审核',
        'active': '正常',
        'inactive': '禁用',
        'pending': '待审核'
      }
      return statusMap[status] || '未知'
    }

    const deleteStudent = async (studentId) => {
      showConfirmDialog('删除学生', '确定要删除这个学生吗？删除后所有相关数据也将被清除，此操作不可恢复。', async () => {
        try {
          loading.value = true

          // 调用API删除学生
          const response = await userApi.deleteUser(studentId)
          if (response.code === 200 || response.success) {
            await loadAdminData()
            showConfirmDialog('删除成功', '学生信息已删除')
          } else {
            showConfirmDialog('删除失败', response.message || '删除失败')
          }
        } catch (err) {
          console.error('删除学生失败:', err)
          showConfirmDialog('删除失败', '删除学生信息时出错')
        } finally {
          loading.value = false
        }
      })
    }

    const loadLearningGoal = () => {
      if (profile.value?.userInfo?.learningGoal) {
        learningGoal.value = profile.value.userInfo.learningGoal
      } else {
        learningGoal.value = ''
      }
    }

    const showEditLearningGoal = () => {
      editGoalDialogVisible.value = true
    }

    const saveLearningGoal = async () => {
      try {
        if (!learningGoal.value.trim()) {
          showConfirmDialog('保存失败', '学习目标不能为空')
          return
        }
        const response = await userApi.updateLearningGoal(learningGoal.value)
        editGoalDialogVisible.value = false

        if (response.code === 200 || response.success) {
          if (!profile.value.userInfo) {
            profile.value.userInfo = {}
          }
          profile.value.userInfo.learningGoal = learningGoal.value
          showConfirmDialog('保存成功', '学习目标已更新')
        } else {
          console.error('API 返回错误:', response.message)
          showConfirmDialog('保存失败', response.message || '保存学习目标时出错')
        }
      } catch (err) {
        console.error('保存学习目标失败:', err)
        editGoalDialogVisible.value = false
        showConfirmDialog('保存失败', '更新学习目标时出错')
      }
    }

    const showAllCourses = async () => {
      try {
        loading.value = true
        const response = await academicProfileApi.getAllCourses()
        if (response.code === 200 || response.success) {
          viewAllDialogTitle.value = '所有课程成绩'
          viewAllDialogType.value = 'courses'
          viewAllDialogData.value = response.data || response || []
          viewAllDialogVisible.value = true
        } else {
          showConfirmDialog('获取失败', response.message || '获取课程数据失败')
        }
      } catch (err) {
        console.error('获取课程数据失败:', err)
        showConfirmDialog('获取失败', '获取课程数据时出错')
      } finally {
        loading.value = false
      }
    }

    const showAllLabs = async () => {
      try {
        loading.value = true
        const response = await academicProfileApi.getAllLabs()
        if (response.code === 200 || response.success) {
          viewAllDialogTitle.value = '所有实验室记录'
          viewAllDialogType.value = 'labs'
          viewAllDialogData.value = response.data || response || []
          viewAllDialogVisible.value = true
        } else {
          showConfirmDialog('获取失败', response.message || '获取实验室数据失败')
        }
      } catch (err) {
        console.error('获取实验室数据失败:', err)
        showConfirmDialog('获取失败', '获取实验室数据时出错')
      } finally {
        loading.value = false
      }
    }

    const showAllBooks = async () => {
      try {
        loading.value = true
        const response = await academicProfileApi.getAllBooks()
        if (response.code === 200 || response.success) {
          viewAllDialogTitle.value = '所有图书借阅记录'
          viewAllDialogType.value = 'books'
          viewAllDialogData.value = response.data || response || []
          viewAllDialogVisible.value = true
        } else {
          showConfirmDialog('获取失败', response.message || '获取图书数据失败')
        }
      } catch (err) {
        console.error('获取图书数据失败:', err)
        showConfirmDialog('获取失败', '获取图书数据时出错')
      } finally {
        loading.value = false
      }
    }

    const closeViewAllDialog = () => {
      viewAllDialogVisible.value = false
      viewAllDialogTitle.value = ''
      viewAllDialogType.value = ''
      viewAllDialogData.value = []
    }

    const switchAdminTab = (tab) => {
      adminActiveTab.value = tab
      loadAdminData()
    }

    const updateUserInfo = async (updateData) => {
      try {
        const userStr = localStorage.getItem('user')
        if (!userStr) {
          console.error('用户未登录')
          return
        }
        const user = JSON.parse(userStr)
        if (!user.id) {
          console.error('用户ID不存在')
          return
        }
        const updateInfo = {[updateData.field]: updateData.value}
        const response = await userApi.updateUser(user.id, updateInfo)
        if (response.success || response.code === 200) {
          profile.value = {
            ...profile.value,
            userInfo: {
              ...profile.value.userInfo,
              [updateData.field]: updateData.value
            }
          }
          const updatedUser = {...user, [updateData.field]: updateData.value}
          localStorage.setItem('user', JSON.stringify(updatedUser))
          if (typeof ElMessage !== 'undefined') {
            ElMessage.success(`${getFieldLabel(updateData.field)}更新成功`)
          } else {
            showConfirmDialog('更新成功', `${getFieldLabel(updateData.field)}已更新`)
          }
        } else {
          console.error('API返回错误:', response)
          if (typeof ElMessage !== 'undefined') {
            ElMessage.error('更新失败: ' + (response.message || '未知错误'))
          }
        }
      } catch (err) {
        console.error('更新异常:', err)
        if (typeof ElMessage !== 'undefined') {
          ElMessage.error('更新失败')
        }
      }
    }

    const updateAdminInfo = async (updateData) => {
      try {
        const userStr = localStorage.getItem('user')
        if (!userStr) {
          console.error('管理员未登录')
          showConfirmDialog('更新失败', '请先登录')
          return
        }

        const user = JSON.parse(userStr)
        if (!user.id) {
          console.error('管理员ID不存在')
          showConfirmDialog('更新失败', '用户信息不完整')
          return
        }

        const updateInfo = {[updateData.field]: updateData.value}
        let response
        try {
          response = await userApi.updateCurrentUserProfile(updateInfo)
        } catch (apiErr) {
          console.warn('更新当前用户接口失败，尝试使用通用更新接口:', apiErr)
          response = await userApi.updateUser(user.id, updateInfo)
        }

        if (response.code === 200 || response.success) {
          adminInfo.value = {
            ...adminInfo.value,
            [updateData.field]: updateData.value
          }

          const updatedUser = {...user, [updateData.field]: updateData.value}
          localStorage.setItem('user', JSON.stringify(updatedUser))

          showConfirmDialog('更新成功', `${getFieldLabel(updateData.field)}已更新`)
          await loadAdminInfo()

        } else {
          console.error('API返回错误:', response)
          showConfirmDialog('更新失败', response.message || '未知错误')
        }
      } catch (err) {
        console.error('更新管理员信息异常:', err)
        showConfirmDialog('更新失败', '网络错误或服务器异常')
      }
    }

    const getFieldLabel = (field) => {
      const labels = {
        email: '邮箱',
        phone: '电话',
        name: '姓名',
        major: '专业',
        college: '学院',
        className: '班级',
        studentId: '学号',
        employeeId: '工号'
      }
      return labels[field] || field
    }

    // 工具函数
    const getScoreClass = (score) => {
      if (score >= 90) return 'bg-emerald-100 text-emerald-700'
      if (score >= 80) return 'bg-blue-100 text-blue-700'
      if (score >= 70) return 'bg-purple-100 text-purple-700'
      if (score >= 60) return 'bg-amber-100 text-amber-700'
      return 'bg-red-100 text-red-700'
    }

    const formatDate = (dateString) => {
      if (!dateString) return '--'
      try {
        return new Date(dateString).toLocaleDateString('zh-CN', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit'
        })
      } catch {
        return dateString
      }
    }

    // 初始化
    onMounted(() => {
      loadData()
    })

    return {
      // 状态
      loading,
      error,
      profile,
      isAdmin,
      learningGoal,
      adminActiveTab,
      adminCourses,
      adminLabs,
      adminBooks,
      adminStudents,
      adminInfo,
      adminStats,
      viewAllDialogVisible,
      viewAllDialogTitle,
      viewAllDialogType,
      viewAllDialogData,
      editGoalDialogVisible,
      confirmDialogVisible,
      confirmDialogTitle,
      confirmDialogMessage,
      addCourseDialogVisible,
      editCourseDialogVisible,
      currentEditCourse,

      addLabDialogVisible,
      editLabDialogVisible,
      currentLab,
      closeAddLabDialog,
      closeEditLabDialog,
      handleAddLab,
      handleEditLab,

      addBookDialogVisible,
      editBookDialogVisible,
      currentBook,
      closeAddBookDialog,
      closeEditBookDialog,
      handleAddBook,
      handleEditBook,

      addStudentDialogVisible,
      editStudentDialogVisible,
      currentStudent,
      toggleStudentStatus,
      handleAddStudent,
      handleEditStudent,
      closeEditStudentDialog,
      closeAddStudentDialog,
      deleteStudent,


      // 方法
      loadData,
      showAllCourses,
      showAllLabs,
      showAllBooks,
      showEditLearningGoal,
      switchAdminTab,
      showAddCourseDialog,
      closeAddCourseDialog,
      editCourse,
      closeEditCourseDialog,
      deleteCourse,
      showAddLabDialog,
      editLab,
      deleteLab,
      showAddBookDialog,
      editBook,
      deleteBook,
      showAddStudentDialog,
      editStudent,
      closeViewAllDialog,
      showConfirmDialog,
      handleCloseConfirmDialog,
      handleConfirmAction,
      saveLearningGoal,
      updateUserInfo,
      getFieldLabel,
      updateAdminInfo,
      handleSaveCourse,
      handleUpdateCourse,
      dialogCallback,


      // 工具函数
      getScoreClass,
      formatDate
    }
  }
}
</script>

<style scoped>
/* 样式保持不变 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.hover-lift {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.hover-lift:hover {
  transform: translateY(-4px);
}

.text-gradient {
  background: linear-gradient(135deg, #3B82F6 0%, #8B5CF6 50%, #EC4899 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.border-animate {
  position: relative;
}

.border-animate::before {
  content: '';
  position: absolute;
  inset: -2px;
  background: linear-gradient(135deg, #3B82F6, #8B5CF6, #EC4899);
  border-radius: inherit;
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s;
}

.border-animate:hover::before {
  opacity: 1;
}
</style>