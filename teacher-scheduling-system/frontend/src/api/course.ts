import request from './request'
import type { Course, PageResult } from '@/types'

export const courseApi = {
  getCourseList: (params: any) =>
    request.get<PageResult<Course>>('/courses', { params }),

  getCourse: (id: number) =>
    request.get<Course>(`/courses/${id}`),

  createCourse: (data: any) =>
    request.post<Course>('/courses', data),

  updateCourse: (id: number, data: any) =>
    request.put<Course>(`/courses/${id}`, data),

  deleteCourse: (id: number) =>
    request.delete(`/courses/${id}`),

  getActiveCourses: () =>
    request.get<Course[]>('/courses/active')
}
