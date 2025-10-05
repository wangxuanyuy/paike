import { request } from '@/utils/request'
import type { TimeSlot, PageResponse, PageRequest } from '@/types'

/**
 * 时间段管理API
 */
export const timeslotApi = {
  /**
   * 获取时间段列表
   */
  getList(params?: PageRequest) {
    return request.get<PageResponse<TimeSlot>>('/time-slots', { params })
  },

  /**
   * 获取所有时间段（不分页）
   */
  getAll() {
    return request.get<TimeSlot[]>('/time-slots/all')
  },

  /**
   * 根据星期获取时间段
   */
  getByDayOfWeek(dayOfWeek: number) {
    return request.get<TimeSlot[]>(`/time-slots/day/${dayOfWeek}`)
  },

  /**
   * 获取时间段详情
   */
  getDetail(id: number) {
    return request.get<TimeSlot>(`/time-slots/${id}`)
  },

  /**
   * 创建时间段
   */
  create(data: TimeSlot) {
    return request.post<TimeSlot>('/time-slots', data)
  },

  /**
   * 更新时间段
   */
  update(id: number, data: TimeSlot) {
    return request.put<TimeSlot>(`/time-slots/${id}`, data)
  },

  /**
   * 删除时间段
   */
  delete(id: number) {
    return request.delete(`/time-slots/${id}`)
  },

  /**
   * 批量删除时间段
   */
  batchDelete(ids: number[]) {
    return request.post('/time-slots/batch-delete', { ids })
  },

  /**
   * 启用/停用时间段
   */
  toggleActive(id: number, isActive: boolean) {
    return request.put(`/time-slots/${id}/toggle`, { isActive })
  },

  /**
   * 批量创建时间段（按周）
   */
  batchCreateByWeek(data: {
    slotName: string
    startTime: string
    endTime: string
    dayOfWeeks: number[]  // 选择的星期数组
  }) {
    return request.post('/time-slots/batch-create', data)
  }
}
