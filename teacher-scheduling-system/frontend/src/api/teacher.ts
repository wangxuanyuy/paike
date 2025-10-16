import request from './request'
import type { Teacher, PageResult } from '@/types'

export const teacherApi = {
  getTeacherList: (params: any) =>
    request.get<PageResult<Teacher>>('/teachers', { params }),

  getTeacher: (id: number) =>
    request.get<Teacher>(`/teachers/${id}`),

  createTeacher: (data: any) =>
    request.post<Teacher>('/teachers', data),

  updateTeacher: (id: number, data: any) =>
    request.put<Teacher>(`/teachers/${id}`, data),

  deleteTeacher: (id: number) =>
    request.delete(`/teachers/${id}`),

  getAvailableTeachers: (params: any) =>
    request.get<Teacher[]>('/teachers/available', { params })
}
