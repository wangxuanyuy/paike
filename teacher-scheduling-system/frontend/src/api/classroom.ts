import request from './request'
import type { Classroom } from '@/types'

export const classroomApi = {
  getAllClassrooms: () =>
    request.get<Classroom[]>('/classrooms'),

  getClassroom: (id: number) =>
    request.get<Classroom>(`/classrooms/${id}`),

  createClassroom: (data: any) =>
    request.post<Classroom>('/classrooms', data),

  updateClassroom: (id: number, data: any) =>
    request.put<Classroom>(`/classrooms/${id}`, data),

  deleteClassroom: (id: number) =>
    request.delete(`/classrooms/${id}`)
}
