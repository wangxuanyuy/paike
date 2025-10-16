export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  page: number
  size: number
}

export interface Teacher {
  id: number
  teacherCode: string
  teacherName: string
  phone: string
  email?: string
  specialties?: string
  ageGroups: number[]
  isActive: boolean
  createdAt: string
  updatedAt: string
}

export interface Course {
  id: number
  courseCode: string
  courseName: string
  ageGroup: number
  ageGroupName: string
  maxStudents: number
  description?: string
  isActive: boolean
  createdAt: string
}

export interface Classroom {
  id: number
  classroomCode: string
  classroomName: string
  maxCapacity: number
  location?: string
  facilities?: string
  isActive: boolean
  createdAt: string
}

export interface TimeSlot {
  id: number
  slotName: string
  startTime: string
  endTime: string
  dayOfWeek: number
  dayOfWeekName: string
  isActive: boolean
}

export interface CourseSchedule {
  id: number
  courseId: number
  courseName: string
  ageGroup: number
  timeSlotId: number
  timeSlotName: string
  startTime: string
  endTime: string
  classroomId: number
  classroomName: string
  scheduleDate: string
  status: number
  teacherNames: string[]
  currentStudents: number
  maxStudents: number
}

export interface ConflictWarning {
  id: number
  conflictType: number
  conflictTypeName: string
  conflictDescription: string
  relatedIds: number[]
  conflictDate: string
  timeSlotId: number
  timeSlotName: string
  status: number
  statusName: string
  createdAt: string
}

export const AGE_GROUPS = [
  { value: 1, label: '学前班(1-3岁)' },
  { value: 2, label: '小班(3-6岁)' },
  { value: 3, label: '中班(6-9岁)' },
  { value: 4, label: '大班(9-12岁)' }
]

export const WEEK_DAYS = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']

export const SCHEDULE_STATUS = [
  { value: 1, label: '正常' },
  { value: 2, label: '取消' },
  { value: 3, label: '调课' }
]

export const CONFLICT_TYPES = [
  { value: 1, label: '教师冲突' },
  { value: 2, label: '学生冲突' },
  { value: 3, label: '教室冲突' }
]

export const WARNING_STATUS = [
  { value: 1, label: '未处理' },
  { value: 2, label: '已处理' },
  { value: 3, label: '已忽略' }
]
