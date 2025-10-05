import request from './request'
import type { TimeSlot } from '@/types'

export const timeSlotApi = {
  getAllTimeSlots: () =>
    request.get<TimeSlot[]>('/time-slots'),

  getTimeSlotsByDay: (dayOfWeek: number) =>
    request.get<TimeSlot[]>(`/time-slots/day/${dayOfWeek}`)
}
