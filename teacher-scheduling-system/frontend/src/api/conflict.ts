import request from './request'
import type { ConflictWarning, PageResult } from '@/types'

export const conflictApi = {
  getConflictWarnings: (params: any) =>
    request.get<PageResult<ConflictWarning>>('/conflicts/warnings', { params }),

  resolveConflictWarning: (id: number, resolution: number, remark?: string) =>
    request.put(`/conflicts/warnings/${id}/resolve`, null, { 
      params: { resolution, remark } 
    }),

  performConflictCheck: (startDate: string, endDate: string) =>
    request.post('/conflicts/check', null, { 
      params: { startDate, endDate } 
    })
}
