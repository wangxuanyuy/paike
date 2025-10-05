import { request } from '@/utils/request'
import type { Classroom, ClassroomUsage, PageResponse, PageRequest } from '@/types'

/**
 * 教室管理API
 */
export const classroomApi = {
  /**
   * 获取教室列表
   */
  getList(params?: PageRequest) {
    return request.get<PageResponse<Classroom>>('/classrooms', { params })
  },

  /**
   * 获取所有教室（不分页）
   */
  getAll() {
    return request.get<Classroom[]>('/classrooms/all')
  },

  /**
   * 获取教室详情
   */
  getDetail(id: number) {
    return request.get<Classroom>(`/classrooms/${id}`)
  },

  /**
   * 创建教室
   */
  create(data: Classroom) {
    return request.post<Classroom>('/classrooms', data)
  },

  /**
   * 更新教室
   */
  update(id: number, data: Classroom) {
    return request.put<Classroom>(`/classrooms/${id}`, data)
  },

  /**
   * 删除教室
   */
  delete(id: number) {
    return request.delete(`/classrooms/${id}`)
  },

  /**
   * 批量删除教室
   */
  batchDelete(ids: number[]) {
    return request.post('/classrooms/batch-delete', { ids })
  },

  /**
   * 获取教室使用情况
   */
  getUsage(params: {
    classroomId?: number
    startDate?: string
    endDate?: string
  }) {
    return request.get<ClassroomUsage[]>('/classrooms/usage', { params })
  },

  /**
   * 启用/停用教室
   */
  toggleActive(id: number, isActive: boolean) {
    return request.put(`/classrooms/${id}/toggle`, { isActive })
  }
}
