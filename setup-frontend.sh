#!/bin/bash

# 创建项目根目录（如果尚未在项目目录中）
mkdir -p student-course-frontend
cd student-course-frontend

# 第1部分：根目录配置文件
cat << 'EOF' > package.json
{
  "name": "student-course-frontend",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite --host 0.0.0.0 --port 3000",
    "build": "vue-tsc && vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "vue": "^3.3.8",
    "vue-router": "^4.2.5",
    "pinia": "^2.1.7",
    "axios": "^1.6.2",
    "element-plus": "^2.4.4",
    "@element-plus/icons-vue": "^2.1.0",
    "dayjs": "^1.11.10"
  },
  "devDependencies": {
    "@types/node": "^20.10.0",
    "@vitejs/plugin-vue": "^4.5.0",
    "typescript": "^5.3.2",
    "vite": "^5.0.0",
    "vue-tsc": "^1.8.25",
    "unplugin-auto-import": "^0.17.2",
    "unplugin-vue-components": "^0.25.2"
  }
}
EOF

cat << 'EOF' > vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      imports: ['vue', 'vue-router', 'pinia'],
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [ElementPlusResolver()]
    })
  ],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    host: '0.0.0.0',
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api')
      }
    }
  }
})
EOF

cat << 'EOF' > tsconfig.json
{
  "compilerOptions": {
    "target": "ES2020",
    "useDefineForClassFields": true,
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "skipLibCheck": true,
    "moduleResolution": "bundler",
    "allowImportingTsExtensions": true,
    "resolveJsonModule": true,
    "isolatedModules": true,
    "noEmit": true,
    "jsx": "preserve",
    "strict": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noFallthroughCasesInSwitch": true,
    "baseUrl": ".",
    "paths": {
      "@/*": ["src/*"]
    }
  },
  "include": ["src/**/*.ts", "src/**/*.d.ts", "src/**/*.tsx", "src/**/*.vue"],
  "references": [{ "path": "./tsconfig.node.json" }]
}
EOF

cat << 'EOF' > tsconfig.node.json
{
  "compilerOptions": {
    "composite": true,
    "skipLibCheck": true,
    "module": "ESNext",
    "moduleResolution": "bundler",
    "allowSyntheticDefaultImports": true
  },
  "include": ["vite.config.ts"]
}
EOF

# 第2部分：入口与类型定义
cat << 'EOF' > src/main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'

const app = createApp(App)

// 注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.mount('#app')
EOF

cat << 'EOF' > index.html
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8" />
    <link rel="icon" type="image/svg+xml" href="/vite.svg" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>学生选课系统</title>
  </head>
  <body>
    <div id="app"></div>
    <script type="module" src="/src/main.ts"></script>
  </body>
</html>
EOF

mkdir -p src/types

cat << 'EOF' > src/types/index.ts
export interface Student {
  id: number
  studentCode: string
  studentName: string
  age: number
  ageGroup: number
  parentName: string
  parentPhone: string
  parentEmail?: string
  registrationStatus: number
  isActive: boolean
  createdAt: string
}

export interface StudentRegistration {
  studentName: string
  age: number
  parentName: string
  parentPhone: string
  parentEmail?: string
}

export interface CourseScheduleView {
  dayOfWeek: number
  dayName: string
  timeSlots: TimeSlotView[]
}

export interface TimeSlotView {
  timeSlotId: number
  slotName: string
  startTime: string
  endTime: string
  courses: CourseInfo[]
}

export interface CourseInfo {
  courseId: number
  scheduleId: number
  courseName: string
  classroomName: string
  teacherNames: string[]
  maxStudents: number
  currentStudents: number
  status: 'FULL' | 'AVAILABLE' | 'EMPTY' | 'DISABLED'
  ageGroup: number
  canEnroll: boolean
}

export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export const AGE_GROUPS = [
  { value: 1, label: '学前班(1-3岁)' },
  { value: 2, label: '小班(3-6岁)' },
  { value: 3, label: '中班(6-9岁)' },
  { value: 4, label: '大班(9-12岁)' }
]

export const WEEK_DAYS = [
  '周一', '周二', '周三', '周四', '周五', '周六', '周日'
]
EOF

# 第3部分：API 封装
mkdir -p src/api

cat << 'EOF' > src/api/request.ts
import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage, ElLoading } from 'element-plus'
import type { ApiResponse } from '@/types'

class Request {
  private instance: AxiosInstance
  private loading: any

  constructor(config: AxiosRequestConfig) {
    this.instance = axios.create(config)
    this.setupInterceptors()
  }

  private setupInterceptors() {
    // 请求拦截器
    this.instance.interceptors.request.use(
      (config) => {
        // 显示loading
        this.loading = ElLoading.service({
          lock: true,
          text: '请求中...',
          background: 'rgba(0, 0, 0, 0.1)'
        })
        return config
      },
      (error) => {
        this.hideLoading()
        return Promise.reject(error)
      }
    )

    // 响应拦截器
    this.instance.interceptors.response.use(
      (response: AxiosResponse<ApiResponse>) => {
        this.hideLoading()
        const { code, message, data } = response.data
        if (code === 200) {
          return data
        } else {
          ElMessage.error(message || '请求失败')
          return Promise.reject(new Error(message))
        }
      },
      (error) => {
        this.hideLoading()
        if (error.response) {
          const { status, data } = error.response
          if (status >= 500) {
            ElMessage.error('服务器错误，请稍后重试')
          } else {
            ElMessage.error(data?.message || '请求失败')
          }
        } else {
          ElMessage.error('网络错误，请检查网络连接')
        }
        return Promise.reject(error)
      }
    )
  }

  private hideLoading() {
    if (this.loading) {
      this.loading.close()
      this.loading = null
    }
  }

  public get<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.get(url, config)
  }

  public post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.post(url, data, config)
  }

  public put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.put(url, data, config)
  }

  public delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.delete(url, config)
  }
}

const request = new Request({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

export default request
EOF

cat << 'EOF' > src/api/index.ts
import request from './request'
import type { Student, StudentRegistration, CourseScheduleView } from '@/types'

// 学生相关API
export const studentApi = {
  // 学生注册
  register: (data: StudentRegistration) =>
    request.post<Student>('/students/register', data),
  // 获取所有学生
  getAllStudents: () =>
    request.get<Student[]>('/students'),
  // 根据年龄组获取学生
  getStudentsByAgeGroup: (ageGroup: number) =>
    request.get<Student[]>(`/students/age-group/${ageGroup}`),
  // 获取学生详情
  getStudent: (id: number) =>
    request.get<Student>(`/students/${id}`),
  // 更新学生信息
  updateStudent: (id: number, data: Partial<Student>) =>
    request.put<Student>(`/students/${id}`, data)
}

// 课程相关API
export const courseApi = {
  // 获取课程表视图
  getScheduleView: (studentAgeGroup: number, startDate: string) =>
    request.get<CourseScheduleView[]>('/courses/schedule', {
      params: { studentAgeGroup, startDate }
    })
}

// 选课相关API
export const enrollmentApi = {
  // 学生选课
  enrollCourse: (studentId: number, courseScheduleId: number) =>
    request.post('/enrollments', null, {
      params: { studentId, courseScheduleId }
    }),
  // 取消选课
  cancelEnrollment: (studentId: number, courseScheduleId: number) =>
    request.delete('/enrollments', {
      params: { studentId, courseScheduleId }
    })
}
EOF

# 第4部分：状态管理与路由
mkdir -p src/stores src/router

cat << 'EOF' > src/stores/student.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { Student } from '@/types'
import { studentApi } from '@/api'
export const useStudentStore = defineStore('student', () => {
  const students = ref<Student[]>([])
  const currentStudent = ref<Student | null>(null)
  const loading = ref(false)
  // 获取所有学生
  const fetchStudents = async () => {
    loading.value = true
    try {
      students.value = await studentApi.getAllStudents()
    } catch (error) {
      console.error('获取学生列表失败:', error)
    } finally {
      loading.value = false
    }
  }
  // 设置当前学生
  const setCurrentStudent = (student: Student | null) => {
    currentStudent.value = student
  }
  // 注册学生
  const registerStudent = async (data: any) => {
    try {
      const newStudent = await studentApi.register(data)
      students.value.push(newStudent)
      return newStudent
    } catch (error) {
      throw error
    }
  }
  return {
    students,
    currentStudent,
    loading,
    fetchStudents,
    setCurrentStudent,
    registerStudent
  }
})
EOF

cat << 'EOF' > src/stores/course.ts
import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { CourseScheduleView } from '@/types'
import { courseApi, enrollmentApi } from '@/api'
export const useCourseStore = defineStore('course', () => {
  const scheduleData = ref<CourseScheduleView[]>([])
  const loading = ref(false)
  // 获取课程表数据
  const fetchScheduleView = async (studentAgeGroup: number, startDate: string) => {
    loading.value = true
    try {
      scheduleData.value = await courseApi.getScheduleView(studentAgeGroup, startDate)
    } catch (error) {
      console.error('获取课程表失败:', error)
    } finally {
      loading.value = false
    }
  }
  // 学生选课
  const enrollCourse = async (studentId: number, courseScheduleId: number) => {
    try {
      await enrollmentApi.enrollCourse(studentId, courseScheduleId)
      return true
    } catch (error) {
      throw error
    }
  }
  // 取消选课
  const cancelEnrollment = async (studentId: number, courseScheduleId: number) => {
    try {
      await enrollmentApi.cancelEnrollment(studentId, courseScheduleId)
      return true
    } catch (error) {
      throw error
    }
  }
  return {
    scheduleData,
    loading,
    fetchScheduleView,
    enrollCourse,
    cancelEnrollment
  }
})
EOF

cat << 'EOF' > src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/course-selection'
    },
    {
      path: '/course-selection',
      name: 'CourseSelection',
      component: () => import('@/views/CourseSelection.vue'),
      meta: { title: '学生选课' }
    },
    {
      path: '/student-management',
      name: 'StudentManagement',
      component: () => import('@/views/StudentManagement.vue'),
      meta: { title: '学生管理' }
    }
  ]
})
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - 学生选课系统`
  }
  next()
})
export default router
EOF

# 第5部分：组件与页面
mkdir -p src/components src/views

cat << 'EOF' > src/App.vue
<template>
  <div id="app">
    <el-container>
      <el-header height="60px">
        <div class="header">
          <h1>学生选课系统</h1>
          <nav class="nav">
            <el-button 
              type="text" 
              @click="$router.push('/course-selection')"
              :class="{ active: $route.path === '/course-selection' }"
            >
              学生选课
            </el-button>
            <el-button 
              type="text" 
              @click="$router.push('/student-management')"
              :class="{ active: $route.path === '/student-management' }"
            >
              学生管理
            </el-button>
          </nav>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>
<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
  background: #409eff;
  color: white;
}
.header h1 {
  margin: 0;
  font-size: 20px;
}
.nav .el-button {
  color: white;
  margin-left: 20px;
}
.nav .el-button.active,
.nav .el-button:hover {
  color: #67c23a;
}
</style>
EOF

cat << 'EOF' > src/components/CourseScheduleGrid.vue
<template>
  <div class="course-schedule-grid">
    <!-- 学生信息卡片 -->
    <el-card class="student-info-card" v-if="currentStudent">
      <div class="student-header">
        <el-avatar :size="60" class="student-avatar">
          {{ currentStudent.studentName.charAt(0) }}
        </el-avatar>
        <div class="student-details">
          <h3>{{ currentStudent.studentName }}</h3>
          <p>年龄：{{ currentStudent.age }}岁 | {{ getAgeGroupLabel(currentStudent.ageGroup) }}</p>
          <p>家长：{{ currentStudent.parentName }} | {{ currentStudent.parentPhone }}</p>
        </div>
      </div>
    </el-card>
    <!-- 周选择器 -->
    <div class="week-selector">
      <el-button-group>
        <el-button @click="previousWeek" :icon="ArrowLeft">上一周</el-button>
        <el-button disabled>
          {{ formatDateRange(currentWeekStart, currentWeekEnd) }}
        </el-button>
        <el-button @click="nextWeek" :icon="ArrowRight">下一周</el-button>
      </el-button-group>
      <el-button type="primary" @click="goToCurrentWeek">当前周</el-button>
    </div>
    <!-- 课程表格 -->
    <div class="schedule-grid">
      <div class="grid-header">
        <div class="time-header">时间</div>
        <div 
          v-for="(day, index) in WEEK_DAYS" 
          :key="index"
          class="day-header"
          :class="{ 'today': isToday(index + 1) }"
        >
          {{ day }}
          <span class="date">{{ getDateForDay(index + 1) }}</span>
        </div>
      </div>
      <div 
        v-for="timeSlot in timeSlots" 
        :key="timeSlot.name"
        class="grid-row"
      >
        <div class="time-cell">
          <div class="time-name">{{ timeSlot.name }}</div>
          <div class="time-range">{{ timeSlot.startTime }}-{{ timeSlot.endTime }}</div>
        </div>
        <div 
          v-for="(day, dayIndex) in WEEK_DAYS" 
          :key="`${timeSlot.name}-${dayIndex}`"
          class="course-cell"
        >
          <CourseCard
            v-for="course in getCoursesForSlot(dayIndex + 1, timeSlot.name)"
            :key="course.scheduleId"
            :course="course"
            :current-student="currentStudent"
            @enroll-course="handleEnrollCourse"
            @cancel-enrollment="handleCancelEnrollment"
          />
          <div v-if="getCoursesForSlot(dayIndex + 1, timeSlot.name).length === 0" class="empty-slot">
            <el-icon class="empty-icon"><CirclePlus /></el-icon>
            <span class="empty-text">暂无课程</span>
          </div>
        </div>
      </div>
    </div>
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-mask">
      <el-icon class="loading-icon"><Loading /></el-icon>
      <span>加载中...</span>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowLeft, ArrowRight, Loading, CirclePlus } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import CourseCard from './CourseCard.vue'
import { useCourseStore } from '@/stores/course'
import type { Student, CourseInfo } from '@/types'
import { AGE_GROUPS, WEEK_DAYS } from '@/types'
interface Props {
  currentStudent?: Student
}
const props = defineProps<Props>()
const courseStore = useCourseStore()
const loading = computed(() => courseStore.loading)
const scheduleData = computed(() => courseStore.scheduleData)
// 响应式数据
const currentWeekStart = ref(dayjs().startOf('week'))
// 时间段定义
const timeSlots = [
  { name: '早班', startTime: '09:00', endTime: '11:00' },
  { name: '中班', startTime: '11:00', endTime: '13:00' },
  { name: '晚班', startTime: '14:00', endTime: '16:00' },
  { name: '夜班', startTime: '16:00', endTime: '18:00' }
]
// 计算属性
const currentWeekEnd = computed(() => currentWeekStart.value.add(6, 'day'))
// 方法
const loadScheduleData = async () => {
  if (!props.currentStudent) return
  await courseStore.fetchScheduleView(
    props.currentStudent.ageGroup,
    currentWeekStart.value.format('YYYY-MM-DD')
  )
}
const getCoursesForSlot = (dayOfWeek: number, timeSlotName: string): CourseInfo[] => {
  const dayData = scheduleData.value.find(d => d.dayOfWeek === dayOfWeek)
  if (!dayData) return []
  const slotData = dayData.timeSlots.find(s => s.slotName === timeSlotName)
  return slotData?.courses || []
}
const isToday = (dayOfWeek: number): boolean => {
  return dayjs().day() === dayOfWeek || (dayOfWeek === 7 && dayjs().day() === 0)
}
const getDateForDay = (dayOfWeek: number): string => {
  const adjustedDay = dayOfWeek === 7 ? 0 : dayOfWeek
  return currentWeekStart.value.day(adjustedDay).format('MM/DD')
}
const formatDateRange = (start: dayjs.Dayjs, end: dayjs.Dayjs): string => {
  return `${start.format('YYYY年MM月DD日')} - ${end.format('MM月DD日')}`
}
const getAgeGroupLabel = (ageGroup: number): string => {
  return AGE_GROUPS.find(g => g.value === ageGroup)?.label || ''
}
const previousWeek = () => {
  currentWeekStart.value = currentWeekStart.value.subtract(1, 'week')
}
const nextWeek = () => {
  currentWeekStart.value = currentWeekStart.value.add(1, 'week')
}
const goToCurrentWeek = () => {
  currentWeekStart.value = dayjs().startOf('week')
}
const handleEnrollCourse = async (courseCard: CourseInfo) => {
  if (!props.currentStudent) return
  try {
    await courseStore.enrollCourse(props.currentStudent.id, courseCard.scheduleId)
    ElMessage.success('选课成功！')
    await loadScheduleData()
  } catch (error) {
    // 错误信息由拦截器处理
  }
}
const handleCancelEnrollment = async (courseCard: CourseInfo) => {
  if (!props.currentStudent) return
  try {
    await courseStore.cancelEnrollment(props.currentStudent.id, courseCard.scheduleId)
    ElMessage.success('取消选课成功！')
    await loadScheduleData()
  } catch (error) {
    // 错误信息由拦截器处理
  }
}
// 监听器
watch(() => props.currentStudent, () => {
  if (props.currentStudent) {
    loadScheduleData()
  }
})
watch(() => currentWeekStart.value, () => {
  loadScheduleData()
})
// 生命周期
onMounted(() => {
  if (props.currentStudent) {
    loadScheduleData()
  }
})
</script>
<style lang="scss" scoped>
.course-schedule-grid {
  padding: 20px;
  min-height: 100vh;
  background: #f5f5f5;
  position: relative;
}
.student-info-card {
  margin-bottom: 20px;
  .student-header {
    display: flex;
    align-items: center;
    gap: 15px;
    .student-avatar {
      background: linear-gradient(45deg, #409eff, #67c23a);
      color: white;
      font-weight: bold;
    }
    .student-details {
      h3 {
        margin: 0 0 5px 0;
        color: #303133;
        font-size: 18px;
      }
      p {
        margin: 2px 0;
        color: #606266;
        font-size: 14px;
      }
    }
  }
}
.week-selector {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 10px;
}
.schedule-grid {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}
.grid-header {
  display: grid;
  grid-template-columns: 120px repeat(7, 1fr);
  background: #fafafa;
  border-bottom: 2px solid #e4e7ed;
  .time-header, .day-header {
    padding: 15px 10px;
    text-align: center;
    font-weight: bold;
    color: #303133;
    border-right: 1px solid #e4e7ed;
  }
  .day-header {
    display: flex;
    flex-direction: column;
    gap: 5px;
    &.today {
      background: #e6f7ff;
      color: #1890ff;
    }
    .date {
      font-size: 12px;
      font-weight: normal;
      color: #909399;
    }
  }
}
.grid-row {
  display: grid;
  grid-template-columns: 120px repeat(7, 1fr);
  border-bottom: 1px solid #e4e7ed;
  &:last-child {
    border-bottom: none;
  }
}
.time-cell {
  padding: 20px 10px;
  text-align: center;
  background: #f9f9f9;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
  justify-content: center;
  .time-name {
    font-weight: bold;
    color: #303133;
    margin-bottom: 5px;
  }
  .time-range {
    font-size: 12px;
    color: #909399;
  }
}
.course-cell {
  padding: 10px;
  border-right: 1px solid #e4e7ed;
  min-height: 120px;
  &:last-child {
    border-right: none;
  }
}
.empty-slot {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #c0c4cc;
  .empty-icon {
    font-size: 24px;
    margin-bottom: 8px;
  }
  .empty-text {
    font-size: 12px;
  }
}
.loading-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 100;
  .loading-icon {
    font-size: 32px;
    margin-bottom: 10px;
    animation: rotate 2s linear infinite;
  }
  span {
    color: #606266;
    font-size: 14px;
  }
}
@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>
EOF

cat << 'EOF' > src/components/CourseCard.vue
<template>
  <div 
    class="course-card"
    :class="[
      \`status-\${course.status.toLowerCase()}\`,
      { 'can-enroll': course.canEnroll && currentStudent }
    ]"
  >
    <div class="course-header">
      <h4 class="course-name">{{ course.courseName }}</h4>
      <div class="student-count">
        {{ course.currentStudents }}/{{ course.maxStudents }}
      </div>
    </div>
    <div class="course-details">
      <div class="detail-item">
        <el-icon><Location /></el-icon>
        <span>{{ course.classroomName }}</span>
      </div>
      <div class="detail-item">
        <el-icon><User /></el-icon>
        <span>{{ course.teacherNames.join(', ') }}</span>
      </div>
    </div>
    <div class="course-actions" v-if="currentStudent">
      <el-button 
        v-if="course.canEnroll && course.status !== 'FULL'"
        type="primary" 
        size="small"
        :disabled="course.status === 'DISABLED'"
        @click="handleEnroll"
      >
        选课
      </el-button>
      <el-button 
        v-else-if="isEnrolled"
        type="danger" 
        size="small"
        @click="handleCancelEnrollment"
      >
        取消
      </el-button>
      <span v-else-if="course.status === 'FULL'" class="status-text full">
        已满
      </span>
      <span v-else-if="course.status === 'DISABLED'" class="status-text disabled">
        不可选
      </span>
    </div>
    <!-- 状态指示器 -->
    <div class="status-indicator" :class="\`status-\${course.status.toLowerCase()}\`"></div>
  </div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import { Location, User } from '@element-plus/icons-vue'
import type { CourseInfo, Student } from '@/types'
interface Props {
  course: CourseInfo
  currentStudent?: Student
}
const props = defineProps<Props>()
const emit = defineEmits<{
  'enroll-course': [course: CourseInfo]
  'cancel-enrollment': [course: CourseInfo]
}>()
// 判断学生是否已选择此课程
const isEnrolled = computed(() => {
  return false
})
const handleEnroll = () => {
  emit('enroll-course', props.course)
}
const handleCancelEnrollment = () => {
  emit('cancel-enrollment', props.course)
}
</script>
<style lang="scss" scoped>
.course-card {
  position: relative;
  padding: 12px;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
  background: white;
  transition: all 0.3s ease;
  cursor: pointer;
  overflow: hidden;
  margin-bottom: 8px;
  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transform: translateY(-1px);
  }
  &.can-enroll:hover {
    border-color: #409eff;
  }
  &.status-full {
    background: #fef0f0;
    border-color: #f56c6c;
  }
  &.status-available {
    background: #fdf6ec;
    border-color: #e6a23c;
  }
  &.status-empty {
    background: #f0f9ff;
    border-color: #409eff;
  }
  &.status-disabled {
    background: #f5f5f5;
    border-color: #c0c4cc;
    opacity: 0.6;
    cursor: not-allowed;
    &:hover {
      transform: none;
      box-shadow: none;
    }
  }
}
.course-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
  .course-name {
    font-size: 14px;
    font-weight: 600;
    color: #303133;
    margin: 0;
    line-height: 1.4;
    flex: 1;
    margin-right: 8px;
  }
  .student-count {
    font-size: 12px;
    color: #909399;
    background: #f5f5f5;
    padding: 2px 6px;
    border-radius: 10px;
    white-space: nowrap;
  }
}
.course-details {
  margin-bottom: 10px;
  .detail-item {
    display: flex;
    align-items: center;
    gap: 4px;
    margin-bottom: 4px;
    font-size: 12px;
    color: #606266;
    .el-icon {
      font-size: 12px;
      color: #909399;
    }
    span {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
.course-actions {
  display: flex;
  justify-content: center;
  .el-button {
    width: 100%;
    font-size: 12px;
  }
  .status-text {
    font-size: 12px;
    text-align: center;
    padding: 4px 0;
    width: 100%;
    &.full {
      color: #f56c6c;
    }
    &.disabled {
      color: #c0c4cc;
    }
  }
}
.status-indicator {
  position: absolute;
  top: 0;
  right: 0;
  width: 0;
  height: 0;
  border-style: solid;
  &.status-full {
    border-width: 0 12px 12px 0;
    border-color: transparent #f56c6c transparent transparent;
  }
  &.status-available {
    border-width: 0 12px 12px 0;
    border-color: transparent #e6a23c transparent transparent;
  }
  &.status-empty {
    border-width: 0 12px 12px 0;
    border-color: transparent #67c23a transparent transparent;
  }
  &.status-disabled {
    border-width: 0 12px 12px 0;
    border-color: transparent #c0c4cc transparent transparent;
  }
}
</style>
EOF

cat << 'EOF' > src/components/StudentRegistrationDialog.vue
<template>
  <el-dialog
    v-model="dialogVisible"
    title="学生注册"
    width="500px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
      @submit.prevent="handleSubmit"
    >
      <el-form-item label="学生姓名" prop="studentName">
        <el-input
          v-model="form.studentName"
          placeholder="请输入学生姓名"
          maxlength="20"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="学生年龄" prop="age">
        <el-input-number
          v-model="form.age"
          :min="1"
          :max="12"
          style="width: 100%"
        />
        <div class="age-hint">
          <span>年龄将自动分配到对应班级：</span>
          <div class="age-groups">
            <el-tag size="small">学前班 1-3岁</el-tag>
            <el-tag size="small">小班 3-6岁</el-tag>
            <el-tag size="small">中班 6-9岁</el-tag>
            <el-tag size="small">大班 9-12岁</el-tag>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="家长姓名" prop="parentName">
        <el-input
          v-model="form.parentName"
          placeholder="请输入家长姓名"
          maxlength="20"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="家长电话" prop="parentPhone">
        <el-input
          v-model="form.parentPhone"
          placeholder="请输入家长电话"
          maxlength="11"
        />
      </el-form-item>
      <el-form-item label="家长邮箱" prop="parentEmail">
        <el-input
          v-model="form.parentEmail"
          placeholder="请输入家长邮箱（可选）"
          type="email"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button 
          type="primary" 
          @click="handleSubmit"
          :loading="submitting"
        >
          提交注册
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { useStudentStore } from '@/stores/student'
import type { StudentRegistration, Student } from '@/types'
interface Props {
  modelValue: boolean
}
const props = defineProps<Props>()
const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'register-success': [student: Student]
}>()
const studentStore = useStudentStore()
const dialogVisible = ref(false)
const formRef = ref<FormInstance>()
const submitting = ref(false)
const form = ref<StudentRegistration>({
  studentName: '',
  age: 3,
  parentName: '',
  parentPhone: '',
  parentEmail: ''
})
const rules: FormRules = {
  studentName: [
    { required: true, message: '请输入学生姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在2-20个字符', trigger: 'blur' }
  ],
  age: [
    { required: true, message: '请选择学生年龄', trigger: 'change' },
    { type: 'number', min: 1, max: 12, message: '年龄应在1-12岁之间', trigger: 'change' }
  ],
  parentName: [
    { required: true, message: '请输入家长姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在2-20个字符', trigger: 'blur' }
  ],
  parentPhone: [
    { required: true, message: '请输入家长电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  parentEmail: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}
const handleSubmit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const student = await studentStore.registerStudent(form.value)
    emit('register-success', student)
    resetForm()
  } catch (error) {
    // 错误信息由拦截器处理
  } finally {
    submitting.value = false
  }
}
const handleClose = () => {
  emit('update:modelValue', false)
  resetForm()
}
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.value = {
    studentName: '',
    age: 3,
    parentName: '',
    parentPhone: '',
    parentEmail: ''
  }
}
watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal
})
watch(() => dialogVisible.value, (newVal) => {
  emit('update:modelValue', newVal)
})
</script>
<style lang="scss" scoped>
.age-hint {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
  .age-groups {
    margin-top: 4px;
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
  }
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
EOF

cat << 'EOF' > src/views/CourseSelection.vue
<template>
  <div class="course-selection-page">
    <div class="page-header">
      <h1>学生选课</h1>
      <p class="page-description">请选择学生查看可选课程</p>
    </div>
    <!-- 学生选择器 -->
    <div class="student-selector">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>选择学生</span>
            <el-button 
              type="text" 
              size="small"
              @click="showRegistrationDialog = true"
            >
              新学生注册
            </el-button>
          </div>
        </template>
        <div class="selector-content">
          <el-select
            v-model="selectedStudentId"
            placeholder="请选择学生"
            filterable
            clearable
            style="width: 300px"
            @change="handleStudentChange"
          >
            <el-option
              v-for="student in students"
              :key="student.id"
              :label="`${student.studentName} (${student.parentName})`"
              :value="student.id"
              :disabled="student.registrationStatus !== 2"
            >
              <div class="student-option">
                <span class="student-name">{{ student.studentName }}</span>
                <span class="student-info">
                  {{ getAgeGroupLabel(student.ageGroup) }} | {{ student.parentName }}
                </span>
                <el-tag 
                  v-if="student.registrationStatus === 1" 
                  type="warning" 
                  size="small"
                >
                  待审核
                </el-tag>
              </div>
            </el-option>
          </el-select>
          <el-button 
            type="primary" 
            :icon="Refresh" 
            @click="loadStudents"
            :loading="studentStore.loading"
          >
            刷新列表
          </el-button>
        </div>
      </el-card>
    </div>
    <!-- 课程表 -->
    <div class="schedule-container" v-if="currentStudent">
      <CourseScheduleGrid 
        :current-student="currentStudent"
        @enroll-success="handleEnrollSuccess"
        @cancel-success="handleCancelSuccess"
      />
    </div>
    <!-- 空状态 -->
    <div class="empty-state" v-else>
      <el-empty description="请先选择学生">
        <el-button 
          type="primary" 
          @click="showRegistrationDialog = true"
        >
          注册新学生
        </el-button>
      </el-empty>
    </div>
    <!-- 学生注册对话框 -->
    <StudentRegistrationDialog
      v-model="showRegistrationDialog"
      @register-success="handleRegistrationSuccess"
    />
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import CourseScheduleGrid from '@/components/CourseScheduleGrid.vue'
import StudentRegistrationDialog from '@/components/StudentRegistrationDialog.vue'
import { useStudentStore } from '@/stores/student'
import type { Student } from '@/types'
import { AGE_GROUPS } from '@/types'
const studentStore = useStudentStore()
const selectedStudentId = ref<number | null>(null)
const showRegistrationDialog = ref(false)
const students = computed(() => studentStore.students)
const currentStudent = computed(() => studentStore.currentStudent)
const loadStudents = async () => {
  await studentStore.fetchStudents()
}
const handleStudentChange = (studentId: number | null) => {
  if (studentId) {
    const student = students.value.find(s => s.id === studentId) || null
    studentStore.setCurrentStudent(student)
  } else {
    studentStore.setCurrentStudent(null)
  }
}
const handleRegistrationSuccess = (student: Student) => {
  showRegistrationDialog.value = false
  ElMessage.success('学生注册成功！')
  loadStudents()
}
const handleEnrollSuccess = () => {
  ElMessage.success('选课成功！')
}
const handleCancelSuccess = () => {
  ElMessage.success('取消选课成功！')
}
const getAgeGroupLabel = (ageGroup: number): string => {
  return AGE_GROUPS.find(g => g.value === ageGroup)?.label || ''
}
onMounted(() => {
  loadStudents()
})
</script>
<style lang="scss" scoped>
.course-selection-page {
  padding: 20px;
}
.page-header {
  margin-bottom: 20px;
  h1 {
    color: #303133;
    margin: 0 0 8px 0;
  }
  .page-description {
    color: #909399;
    margin: 0;
  }
}
.student-selector {
  margin-bottom: 20px;
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .selector-content {
    display: flex;
    align-items: center;
    gap: 12px;
  }
}
.student-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  .student-name {
    font-weight: 500;
  }
  .student-info {
    font-size: 12px;
    color: #909399;
  }
}
.empty-state {
  text-align: center;
  padding: 60px 20px;
}
</style>
EOF

cat << 'EOF' > src/views/StudentManagement.vue
<template>
  <div class="student-management">
    <div class="page-header">
      <h1>学生管理</h1>
      <el-button type="primary" @click="showAddDialog = true" :icon="Plus">
        添加学生
      </el-button>
    </div>
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-card>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-input
              v-model="searchForm.keyword"
              placeholder="搜索学生姓名或家长姓名"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            />
          </el-col>
          <el-col :span="4">
            <el-select
              v-model="searchForm.ageGroup"
              placeholder="年龄组"
              clearable
              @change="handleSearch"
            >
              <el-option
                v-for="group in AGE_GROUPS"
                :key="group.value"
                :label="group.label"
                :value="group.value"
              />
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select
              v-model="searchForm.status"
              placeholder="注册状态"
              clearable
              @change="handleSearch"
            >
              <el-option label="待审核" :value="1" />
              <el-option label="已通过" :value="2" />
              <el-option label="已拒绝" :value="3" />
            </el-select>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" @click="handleSearch" :icon="Search">
              搜索
            </el-button>
            <el-button @click="handleReset" :icon="Refresh">
              重置
            </el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <!-- 学生列表 -->
    <el-card>
      <el-table
        :data="filteredStudents"
        v-loading="studentStore.loading"
        style="width: 100%"
        stripe
      >
        <el-table-column prop="studentCode" label="学生编号" width="120" />
        <el-table-column prop="studentName" label="学生姓名" width="100" />
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column label="年龄组" width="120">
          <template #default="{ row }">
            <el-tag>{{ getAgeGroupLabel(row.ageGroup) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="parentName" label="家长姓名" width="100" />
        <el-table-column prop="parentPhone" label="家长电话" width="120" />
        <el-table-column label="注册状态" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getStatusType(row.registrationStatus)"
            >
              {{ getStatusText(row.registrationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
              :icon="Edit"
            >
              编辑
            </el-button>
            <el-button
              v-if="row.registrationStatus === 1"
              type="success"
              size="small"
              @click="handleApprove(row)"
              :icon="Check"
            >
              通过
            </el-button>
            <el-button
              v-if="row.registrationStatus === 1"
              type="danger"
              size="small"
              @click="handleReject(row)"
              :icon="Close"
            >
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!-- 添加/编辑学生对话框 -->
    <StudentRegistrationDialog
      v-model="showAddDialog"
      @register-success="handleAddSuccess"
    />
    <!-- 编辑学生对话框 -->
    <el-dialog
      v-model="showEditDialog"
      title="编辑学生信息"
      width="500px"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="80px"
      >
        <el-form-item label="学生姓名" prop="studentName">
          <el-input v-model="editForm.studentName" />
        </el-form-item>
        <el-form-item label="学生年龄" prop="age">
          <el-input-number v-model="editForm.age" :min="1" :max="12" />
        </el-form-item>
        <el-form-item label="家长姓名" prop="parentName">
          <el-input v-model="editForm.parentName" />
        </el-form-item>
        <el-form-item label="家长电话" prop="parentPhone">
          <el-input v-model="editForm.parentPhone" />
        </el-form-item>
        <el-form-item label="家长邮箱" prop="parentEmail">
          <el-input v-model="editForm.parentEmail" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSaveEdit" :loading="editing">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Search, Refresh, Edit, Check, Close } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import StudentRegistrationDialog from '@/components/StudentRegistrationDialog.vue'
import { useStudentStore } from '@/stores/student'
import { studentApi } from '@/api'
import type { Student } from '@/types'
import { AGE_GROUPS } from '@/types'
const studentStore = useStudentStore()
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const editing = ref(false)
const editFormRef = ref<FormInstance>()
const searchForm = ref({
  keyword: '',
  ageGroup: null,
  status: null
})
const editForm = ref({
  id: null,
  studentName: '',
  age: 1,
  parentName: '',
  parentPhone: '',
  parentEmail: ''
})
const editRules: FormRules = {
  studentName: [
    { required: true, message: '请输入学生姓名', trigger: 'blur' }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' }
  ],
  parentName: [
    { required: true, message: '请输入家长姓名', trigger: 'blur' }
  ],
  parentPhone: [
    { required: true, message: '请输入家长电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}
const students = computed(() => studentStore.students)
const filteredStudents = computed(() => {
  let result = students.value
  if (searchForm.value.keyword) {
    result = result.filter(student => 
      student.studentName.includes(searchForm.value.keyword) ||
      student.parentName.includes(searchForm.value.keyword)
    )
  }
  if (searchForm.value.ageGroup) {
    result = result.filter(student => student.ageGroup === searchForm.value.ageGroup)
  }
  if (searchForm.value.status) {
    result = result.filter(student => student.registrationStatus === searchForm.value.status)
  }
  return result
})
const loadStudents = async () => {
  await studentStore.fetchStudents()
}
const handleSearch = () => {}
const handleReset = () => {
  searchForm.value = {
    keyword: '',
    ageGroup: null,
    status: null
  }
}
const handleAddSuccess = () => {
  showAddDialog.value = false
  ElMessage.success('学生添加成功！')
  loadStudents()
}
const handleEdit = (student: Student) => {
  editForm.value = {
    id: student.id,
    studentName: student.studentName,
    age: student.age,
    parentName: student.parentName,
    parentPhone: student.parentPhone,
    parentEmail: student.parentEmail || ''
  }
  showEditDialog.value = true
}
const handleSaveEdit = async () => {
  if (!editFormRef.value) return
  const valid = await editFormRef.value.validate().catch(() => false)
  if (!valid) return
  editing.value = true
  try {
    await studentApi.updateStudent(editForm.value.id!, editForm.value)
    ElMessage.success('学生信息更新成功！')
    showEditDialog.value = false
    loadStudents()
  } catch (error) {
    // 错误信息由拦截器处理
  } finally {
    editing.value = false
  }
}
const handleApprove = async (student: Student) => {
  try {
    await ElMessageBox.confirm('确认通过该学生的注册申请？', '确认操作', {
      type: 'warning'
    })
    await studentApi.updateStudent(student.id, { registrationStatus: 2 })
    ElMessage.success('审核通过！')
    loadStudents()
  } catch (error) {
    if (error !== 'cancel') {
      // 错误信息由拦截器处理
    }
  }
}
const handleReject = async (student: Student) => {
  try {
    await ElMessageBox.confirm('确认拒绝该学生的注册申请？', '确认操作', {
      type: 'warning'
    })
    await studentApi.updateStudent(student.id, { registrationStatus: 3 })
    ElMessage.success('已拒绝申请！')
    loadStudents()
  } catch (error) {
    if (error !== 'cancel') {
      // 错误信息由拦截器处理
    }
  }
}
const getAgeGroupLabel = (ageGroup: number): string => {
  return AGE_GROUPS.find(g => g.value === ageGroup)?.label || ''
}
const getStatusType = (status: number) => {
  const types = { 1: 'warning', 2: 'success', 3: 'danger' }
  return types[status] || 'info'
}
const getStatusText = (status: number): string => {
  const texts = { 1: '待审核', 2: '已通过', 3: '已拒绝' }
  return texts[status] || '未知'
}
const formatDate = (dateStr: string): string => {
  return dayjs(dateStr).format('YYYY-MM-DD HH:mm')
}
onMounted(() => {
  loadStudents()
})
</script>
<style lang="scss" scoped>
.student-management {
  padding: 20px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  h1 {
    color: #303133;
    margin: 0;
  }
}
.search-bar {
  margin-bottom: 20px;
}
</style>
EOF

echo "✅ Vue 3 前端工程已创建完成！"
echo "👉 请运行以下命令启动项目："
echo "   cd student-course-frontend"
echo "   npm install"
echo "   npm run dev"
