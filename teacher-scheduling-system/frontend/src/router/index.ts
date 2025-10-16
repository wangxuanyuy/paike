import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/schedule'
    },
    {
      path: '/schedule',
      name: 'Schedule',
      component: () => import('@/views/ScheduleManagement.vue'),
      meta: { title: '排课管理' }
    },
    {
      path: '/teachers',
      name: 'Teachers',
      component: () => import('@/views/TeacherManagement.vue'),
      meta: { title: '教师管理' }
    },
    {
      path: '/courses',
      name: 'Courses',
      component: () => import('@/views/CourseManagement.vue'),
      meta: { title: '课程管理' }
    },
    {
      path: '/classrooms',
      name: 'Classrooms',
      component: () => import('@/views/ClassroomManagement.vue'),
      meta: { title: '教室管理' }
    },
    {
      path: '/conflicts',
      name: 'Conflicts',
      component: () => import('@/views/ConflictWarnings.vue'),
      meta: { title: '冲突警告' }
    }
  ]
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || '教师排课系统'} - 教师排课管理系统`
  next()
})

export default router
