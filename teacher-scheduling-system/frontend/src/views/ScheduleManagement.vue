<template>
  <div class="schedule-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>排课管理</span>
          <el-button type="primary" @click="showCreateDialog = true">
            <el-icon><Plus /></el-icon>
            新建课程安排
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="开始日期">
          <el-date-picker
            v-model="queryForm.startDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker
            v-model="queryForm.endDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable>
            <el-option
              v-for="item in SCHEDULE_STATUS"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadSchedules">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="scheduleList" style="width: 100%" v-loading="loading">
        <el-table-column prop="courseName" label="课程名称" width="150" />
        <el-table-column prop="scheduleDate" label="上课日期" width="120" />
        <el-table-column prop="timeSlotName" label="时间段" width="150">
          <template #default="{ row }">
            {{ row.timeSlotName }} ({{ row.startTime }}-{{ row.endTime }})
          </template>
        </el-table-column>
        <el-table-column prop="classroomName" label="教室" width="120" />
        <el-table-column prop="teacherNames" label="教师">
          <template #default="{ row }">
            <el-tag
              v-for="(name, index) in row.teacherNames"
              :key="index"
              size="small"
              style="margin-right: 5px"
            >
              {{ name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleAssignTeachers(row)"
              v-if="row.status === 1"
            >
              分配教师
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleCancel(row)"
              v-if="row.status === 1"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadSchedules"
          @current-change="loadSchedules"
        />
      </div>
    </el-card>

    <!-- TODO: 添加创建课程安排对话框 -->
    <!-- TODO: 添加分配教师对话框 -->
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { scheduleApi } from '@/api/schedule'
import { SCHEDULE_STATUS } from '@/types'
import type { CourseSchedule } from '@/types'

const loading = ref(false)
const showCreateDialog = ref(false)
const scheduleList = ref<CourseSchedule[]>([])

const queryForm = reactive({
  startDate: '',
  endDate: '',
  status: null as number | null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const loadSchedules = async () => {
  loading.value = true
  try {
    const data = await scheduleApi.getScheduleList({
      ...queryForm,
      page: pagination.page - 1,
      size: pagination.size
    })
    scheduleList.value = data.content
    pagination.total = data.totalElements
  } catch (error) {
    console.error('加载课程安排失败', error)
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryForm.startDate = ''
  queryForm.endDate = ''
  queryForm.status = null
  pagination.page = 1
  loadSchedules()
}

const handleAssignTeachers = (row: CourseSchedule) => {
  ElMessage.info('分配教师功能待实现')
}

const handleCancel = async (row: CourseSchedule) => {
  try {
    await ElMessageBox.prompt('请输入取消原因', '取消课程', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /.+/,
      inputErrorMessage: '请输入取消原因'
    })
    
    await scheduleApi.cancelSchedule(row.id, '管理员取消')
    ElMessage.success('取消成功')
    loadSchedules()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败', error)
    }
  }
}

const getStatusLabel = (status: number) => {
  return SCHEDULE_STATUS.find(s => s.value === status)?.label || '未知'
}

const getStatusType = (status: number) => {
  const map: any = { 1: 'success', 2: 'danger', 3: 'warning' }
  return map[status] || 'info'
}

onMounted(() => {
  loadSchedules()
})
</script>

<style scoped lang="scss">
.schedule-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.query-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
