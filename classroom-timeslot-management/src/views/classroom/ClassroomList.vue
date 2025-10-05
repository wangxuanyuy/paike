<template>
  <div class="classroom-list">
    <div class="header">
      <h2>教室管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加教室
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入教室名称或编号"
        clearable
        style="width: 300px"
        @clear="handleSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch" />
        </template>
      </el-input>
    </div>

    <!-- 教室列表表格 -->
    <el-table
      :data="tableData"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="classroomCode" label="教室编号" width="120" />
      <el-table-column prop="classroomName" label="教室名称" width="150" />
      <el-table-column prop="maxCapacity" label="最大容量" width="100" align="center" />
      <el-table-column prop="location" label="位置" min-width="150" />
      <el-table-column prop="facilities" label="设施" min-width="200" show-overflow-tooltip />
      <el-table-column prop="isActive" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.isActive ? 'success' : 'danger'">
            {{ row.isActive ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="success" @click="handleViewUsage(row)">使用情况</el-button>
          <el-button
            link
            :type="row.isActive ? 'warning' : 'success'"
            @click="handleToggleActive(row)"
          >
            {{ row.isActive ? '停用' : '启用' }}
          </el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
      />
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="教室编号" prop="classroomCode">
          <el-input v-model="formData.classroomCode" placeholder="请输入教室编号" />
        </el-form-item>
        <el-form-item label="教室名称" prop="classroomName">
          <el-input v-model="formData.classroomName" placeholder="请输入教室名称" />
        </el-form-item>
        <el-form-item label="最大容量" prop="maxCapacity">
          <el-input-number v-model="formData.maxCapacity" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="formData.location" placeholder="请输入教室位置" />
        </el-form-item>
        <el-form-item label="设施" prop="facilities">
          <el-input
            v-model="formData.facilities"
            type="textarea"
            :rows="3"
            placeholder="请输入教室设施，多个设施用逗号分隔"
          />
        </el-form-item>
        <el-form-item label="状态" prop="isActive">
          <el-switch v-model="formData.isActive" active-text="启用" inactive-text="停用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>

    <!-- 使用情况对话框 -->
    <el-dialog
      v-model="usageDialogVisible"
      title="教室使用情况"
      width="800px"
    >
      <el-date-picker
        v-model="usageDateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="loadUsageData"
        style="margin-bottom: 20px"
      />
      <el-table :data="usageData" border stripe v-loading="usageLoading">
        <el-table-column prop="usageDate" label="日期" width="120" />
        <el-table-column prop="timeSlotName" label="时间段" width="150" />
        <el-table-column label="使用情况" align="center">
          <template #default="{ row }">
            {{ row.actualStudentCount }} / {{ row.maxCapacity }}
          </template>
        </el-table-column>
        <el-table-column label="利用率" align="center">
          <template #default="{ row }">
            <el-progress
              :percentage="Math.round((row.actualStudentCount / row.maxCapacity) * 100)"
              :color="getProgressColor(row.actualStudentCount / row.maxCapacity)"
            />
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { classroomApi } from '@/api/classroom'
import type { Classroom, ClassroomUsage } from '@/types'

// 表格数据
const tableData = ref<Classroom[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchKeyword = ref('')

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const submitting = ref(false)
const formData = reactive<Classroom>({
  classroomCode: '',
  classroomName: '',
  maxCapacity: 20,
  location: '',
  facilities: '',
  isActive: true
})

// 表单验证规则
const formRules: FormRules = {
  classroomCode: [
    { required: true, message: '请输入教室编号', trigger: 'blur' }
  ],
  classroomName: [
    { required: true, message: '请输入教室名称', trigger: 'blur' }
  ],
  maxCapacity: [
    { required: true, message: '请输入最大容量', trigger: 'blur' }
  ]
}

// 使用情况对话框
const usageDialogVisible = ref(false)
const usageData = ref<ClassroomUsage[]>([])
const usageLoading = ref(false)
const usageDateRange = ref<[Date, Date]>()
const currentClassroomId = ref<number>()

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await classroomApi.getList({
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchKeyword.value
    })
    tableData.value = res.content
    total.value = res.totalElements
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadData()
}

// 添加
const handleAdd = () => {
  dialogTitle.value = '添加教室'
  Object.assign(formData, {
    classroomCode: '',
    classroomName: '',
    maxCapacity: 20,
    location: '',
    facilities: '',
    isActive: true
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: Classroom) => {
  dialogTitle.value = '编辑教室'
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        if (formData.id) {
          await classroomApi.update(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await classroomApi.create(formData)
          ElMessage.success('添加成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error('操作失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

// 删除
const handleDelete = (row: Classroom) => {
  ElMessageBox.confirm('确定要删除该教室吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await classroomApi.delete(row.id!)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 启用/停用
const handleToggleActive = async (row: Classroom) => {
  try {
    await classroomApi.toggleActive(row.id!, !row.isActive)
    ElMessage.success('操作成功')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 查看使用情况
const handleViewUsage = (row: Classroom) => {
  currentClassroomId.value = row.id
  usageDialogVisible.value = true
  loadUsageData()
}

// 加载使用情况
const loadUsageData = async () => {
  if (!currentClassroomId.value) return

  usageLoading.value = true
  try {
    const params: any = { classroomId: currentClassroomId.value }

    if (usageDateRange.value) {
      params.startDate = usageDateRange.value[0].toISOString().split('T')[0]
      params.endDate = usageDateRange.value[1].toISOString().split('T')[0]
    }

    usageData.value = await classroomApi.getUsage(params)
  } catch (error) {
    ElMessage.error('加载使用情况失败')
  } finally {
    usageLoading.value = false
  }
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 进度条颜色
const getProgressColor = (rate: number) => {
  if (rate >= 0.9) return '#f56c6c'
  if (rate >= 0.7) return '#e6a23c'
  return '#67c23a'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.classroom-list {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
