import { createRouter, createWebHistory } from 'vue-router'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/course-selection'
    },
    {
      path: '/course-selection',
      name: 'CourseSelection',
      component: () => import('@/views/CourseSelection.vue'),
      meta: { title: '学生选课' }
    },
    {
      path: '/student-management',
      name: 'StudentManagement',
      component: () => import('@/views/StudentManagement.vue'),
      meta: { title: '学生管理' }
    }
  ]
})
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - 学生选课系统`
  }
  next()
})
export default router
