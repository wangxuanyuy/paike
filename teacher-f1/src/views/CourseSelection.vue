<template>
  <div class="course-selection-page">
    <div class="page-header">
      <h1>学生选课</h1>
      <p class="page-description">请选择学生查看可选课程</p>
    </div>
    <!-- 学生选择器 -->
    <div class="student-selector">
      <el-card>
        <template #header>
          <div class="card-header">
            <span>选择学生</span>
            <el-button 
              type="text" 
              size="small"
              @click="showRegistrationDialog = true"
            >
              新学生注册
            </el-button>
          </div>
        </template>
        <div class="selector-content">
          <el-select
            v-model="selectedStudentId"
            placeholder="请选择学生"
            filterable
            clearable
            style="width: 300px"
            @change="handleStudentChange"
          >
            <el-option
              v-for="student in students"
              :key="student.id"
              :label="`${student.studentName} (${student.parentName})`"
              :value="student.id"
              :disabled="student.registrationStatus !== 2"
            >
              <div class="student-option">
                <span class="student-name">{{ student.studentName }}</span>
                <span class="student-info">
                  {{ getAgeGroupLabel(student.ageGroup) }} | {{ student.parentName }}
                </span>
                <el-tag 
                  v-if="student.registrationStatus === 1" 
                  type="warning" 
                  size="small"
                >
                  待审核
                </el-tag>
              </div>
            </el-option>
          </el-select>
          <el-button 
            type="primary" 
            :icon="Refresh" 
            @click="loadStudents"
            :loading="studentStore.loading"
          >
            刷新列表
          </el-button>
        </div>
      </el-card>
    </div>
    <!-- 课程表 -->
    <div class="schedule-container" v-if="currentStudent">
      <CourseScheduleGrid 
        :current-student="currentStudent"
        @enroll-success="handleEnrollSuccess"
        @cancel-success="handleCancelSuccess"
      />
    </div>
    <!-- 空状态 -->
    <div class="empty-state" v-else>
      <el-empty description="请先选择学生">
        <el-button 
          type="primary" 
          @click="showRegistrationDialog = true"
        >
          注册新学生
        </el-button>
      </el-empty>
    </div>
    <!-- 学生注册对话框 -->
    <StudentRegistrationDialog
      v-model="showRegistrationDialog"
      @register-success="handleRegistrationSuccess"
    />
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import CourseScheduleGrid from '@/components/CourseScheduleGrid.vue'
import StudentRegistrationDialog from '@/components/StudentRegistrationDialog.vue'
import { useStudentStore } from '@/stores/student'
import type { Student } from '@/types'
import { AGE_GROUPS } from '@/types'
const studentStore = useStudentStore()
const selectedStudentId = ref<number | null>(null)
const showRegistrationDialog = ref(false)
const students = computed(() => studentStore.students)
const currentStudent = computed(() => studentStore.currentStudent)
const loadStudents = async () => {
  await studentStore.fetchStudents()
}
const handleStudentChange = (studentId: number | null) => {
  if (studentId) {
    const student = students.value.find(s => s.id === studentId) || null
    studentStore.setCurrentStudent(student)
  } else {
    studentStore.setCurrentStudent(null)
  }
}
const handleRegistrationSuccess = (student: Student) => {
  showRegistrationDialog.value = false
  ElMessage.success('学生注册成功！')
  loadStudents()
}
const handleEnrollSuccess = () => {
  ElMessage.success('选课成功！')
}
const handleCancelSuccess = () => {
  ElMessage.success('取消选课成功！')
}
const getAgeGroupLabel = (ageGroup: number): string => {
  return AGE_GROUPS.find(g => g.value === ageGroup)?.label || ''
}
onMounted(() => {
  loadStudents()
})
</script>
<style lang="scss" scoped>
.course-selection-page {
  padding: 20px;
}
.page-header {
  margin-bottom: 20px;
  h1 {
    color: #303133;
    margin: 0 0 8px 0;
  }
  .page-description {
    color: #909399;
    margin: 0;
  }
}
.student-selector {
  margin-bottom: 20px;
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .selector-content {
    display: flex;
    align-items: center;
    gap: 12px;
  }
}
.student-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  .student-name {
    font-weight: 500;
  }
  .student-info {
    font-size: 12px;
    color: #909399;
  }
}
.empty-state {
  text-align: center;
  padding: 60px 20px;
}
</style>
