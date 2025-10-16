<template>
  <div class="teacher-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>教师管理</span>
          <el-button type="primary" @click="showDialog(null)">
            <el-icon><Plus /></el-icon>
            添加教师
          </el-button>
        </div>
      </template>

      <el-form :inline="true" :model="queryForm" class="query-form">
        <el-form-item label="关键词">
          <el-input
            v-model="queryForm.keyword"
            placeholder="姓名/手机号"
            clearable
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.isActive" placeholder="全部" clearable>
            <el-option label="在职" :value="true" />
            <el-option label="离职" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadTeachers">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="teacherList" style="width: 100%" v-loading="loading">
        <el-table-column prop="teacherCode" label="教师编号" width="150" />
        <el-table-column prop="teacherName" label="姓名" width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="ageGroups" label="可教年龄组">
          <template #default="{ row }">
            <el-tag
              v-for="group in row.ageGroups"
              :key="group"
              size="small"
              style="margin-right: 5px"
            >
              {{ getAgeGroupLabel(group) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isActive ? 'success' : 'danger'">
              {{ row.isActive ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="showDialog(row)">
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(row)"
              v-if="row.isActive"
            >
              删除
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
          @size-change="loadTeachers"
          @current-change="loadTeachers"
        />
      </div>
    </el-card>

    <!-- TODO: 添加教师编辑对话框 -->
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { teacherApi } from '@/api/teacher'
import { AGE_GROUPS } from '@/types'
import type { Teacher } from '@/types'

const loading = ref(false)
const teacherList = ref<Teacher[]>([])

const queryForm = reactive({
  keyword: '',
  isActive: null as boolean | null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const loadTeachers = async () => {
  loading.value = true
  try {
    const data = await teacherApi.getTeacherList({
      ...queryForm,
      page: pagination.page - 1,
      size: pagination.size
    })
    teacherList.value = data.content
    pagination.total = data.totalElements
  } catch (error) {
    console.error('加载教师列表失败', error)
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryForm.keyword = ''
  queryForm.isActive = null
  pagination.page = 1
  loadTeachers()
}

const showDialog = (teacher: Teacher | null) => {
  ElMessage.info('编辑功能待实现')
}

const handleDelete = async (teacher: Teacher) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除教师 ${teacher.teacherName} 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await teacherApi.deleteTeacher(teacher.id)
    ElMessage.success('删除成功')
    loadTeachers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

const getAgeGroupLabel = (value: number) => {
  return AGE_GROUPS.find(g => g.value === value)?.label || ''
}

onMounted(() => {
  loadTeachers()
})
</script>

<style scoped lang="scss">
.teacher-management {
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
