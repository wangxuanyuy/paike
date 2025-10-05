// 教室类型定义
export interface Classroom {
  id?: number
  classroomCode: string
  classroomName: string
  maxCapacity: number
  location?: string
  facilities?: string
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
}

// 时间段类型定义
export interface TimeSlot {
  id?: number
  slotName: string
  startTime: string
  endTime: string
  dayOfWeek: number  // 1-7 代表周一到周日
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
}

// 教室使用情况
export interface ClassroomUsage {
  id: number
  classroomId: number
  classroomName: string
  usageDate: string
  timeSlotId: number
  timeSlotName: string
  actualStudentCount: number
  maxCapacity: number
}

// 分页请求
export interface PageRequest {
  page?: number
  size?: number
  keyword?: string
}

// 分页响应
export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}

// API响应
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 星期枚举
export enum DayOfWeek {
  MONDAY = 1,
  TUESDAY = 2,
  WEDNESDAY = 3,
  THURSDAY = 4,
  FRIDAY = 5,
  SATURDAY = 6,
  SUNDAY = 7
}

export const DAY_LABELS: Record<number, string> = {
  1: '周一',
  2: '周二',
  3: '周三',
  4: '周四',
  5: '周五',
  6: '周六',
  7: '周日'
}
