<template>
  <div class="timeslot-list">
    <div class="header">
      <h2>时间段管理</h2>
      <div>
        <el-button type="success" @click="handleBatchCreate">
          <el-icon><DocumentAdd /></el-icon>
          批量创建
        </el-button>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加时间段
        </el-button>
      </div>
    </div>

    <!-- 按星期分组显示 -->
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane
        v-for="day in 7"
        :key="day"
        :label="getDayLabel(day)"
        :name="String(day)"
      >
        <el-table
          :data="getTimeSlotsByDay(day)"
          border
          stripe
          v-loading="loading"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="slotName" label="时间段名称" width="150" />
          <el-table-column label="时间范围" width="200">
            <template #default="{ row }">
              {{ row.startTime }} - {{ row.endTime }}
            </template>
          </el-table-column>
          <el-table-column prop="isActive" label="状态" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="row.isActive ? 'success' : 'danger'">
                {{ row.isActive ? '启用' : '停用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
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
      </el-tab-pane>
    </el-tabs>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="时间段名称" prop="slotName">
          <el-input v-model="formData.slotName" placeholder="例如：早班、中班" />
        </el-form-item>
        <el-form-item label="星期" prop="dayOfWeek">
          <el-select v-model="formData.dayOfWeek" placeholder="请选择星期">
            <el-option
              v-for="day in 7"
              :key="day"
              :label="getDayLabel(day)"
              :value="day"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker
            v-model="formData.startTime"
            format="HH:mm"
            value-format="HH:mm:ss"
            placeholder="选择开始时间"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="formData.endTime"
            format="HH:mm"
            value-format="HH:mm:ss"
            placeholder="选择结束时间"
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

    <!-- 批量创建对话框 -->
    <el-dialog
      v-model="batchDialogVisible"
      title="批量创建时间段"
      width="600px"
      @close="handleBatchDialogClose"
    >
      <el-form
        ref="batchFormRef"
        :model="batchFormData"
        :rules="batchFormRules"
        label-width="100px"
      >
        <el-form-item label="时间段名称" prop="slotName">
          <el-input v-model="batchFormData.slotName" placeholder="例如：早班" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker
            v-model="batchFormData.startTime"
            format="HH:mm"
            value-format="HH:mm:ss"
            placeholder="选择开始时间"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker
            v-model="batchFormData.endTime"
            format="HH:mm"
            value-format="HH:mm:ss"
            placeholder="选择结束时间"
          />
        </el-form-item>
        <el-form-item label="适用星期" prop="dayOfWeeks">
          <el-checkbox-group v-model="batchFormData.dayOfWeeks">
            <el-checkbox v-for="day in 7" :key="day" :label="day">
              {{ getDayLabel(day) }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchSubmit" :loading="batchSubmitting">
          批量创建
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, DocumentAdd } from '@element-plus/icons-vue'
import { timeslotApi } from '@/api/timeslot'
import type { TimeSlot } from '@/types'
import { DAY_LABELS } from '@/types'

// 表格数据
const allTimeSlots = ref<TimeSlot[]>([])
const loading = ref(false)
const activeTab = ref('1')

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const submitting = ref(false)
const formData = reactive<TimeSlot>({
  slotName: '',
  startTime: '',
  endTime: '',
  dayOfWeek: 1,
  isActive: true
})

// 批量创建对话框
const batchDialogVisible = ref(false)
const batchFormRef = ref<FormInstance>()
const batchSubmitting = ref(false)
const batchFormData = reactive({
  slotName: '',
  startTime: '',
  endTime: '',
  dayOfWeeks: [] as number[]
})

// 表单验证规则
const formRules: FormRules = {
  slotName: [
    { required: true, message: '请输入时间段名称', trigger: 'blur' }
  ],
  dayOfWeek: [
    { required: true, message: '请选择星期', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ]
}

const batchFormRules: FormRules = {
  slotName: [
    { required: true, message: '请输入时间段名称', trigger: 'blur' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endTime: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ],
  dayOfWeeks: [
    { required: true, type: 'array', min: 1, message: '请至少选择一个星期', trigger: 'change' }
  ]
}

// 获取星期标签
const getDayLabel = (day: number) => {
  return DAY_LABELS[day] || ''
}

// 根据星期获取时间段
const getTimeSlotsByDay = (day: number) => {
  return allTimeSlots.value.filter(slot => slot.dayOfWeek === day)
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    allTimeSlots.value = await timeslotApi.getAll()
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 添加
const handleAdd = () => {
  dialogTitle.value = '添加时间段'
  Object.assign(formData, {
    slotName: '',
    startTime: '',
    endTime: '',
    dayOfWeek: Number(activeTab.value),
    isActive: true
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: TimeSlot) => {
  dialogTitle.value = '编辑时间段'
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
          await timeslotApi.update(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await timeslotApi.create(formData)
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

// 批量创建
const handleBatchCreate = () => {
  batchFormData.slotName = ''
  batchFormData.startTime = ''
  batchFormData.endTime = ''
  batchFormData.dayOfWeeks = []
  batchDialogVisible.value = true
}

// 批量提交
const handleBatchSubmit = async () => {
  if (!batchFormRef.value) return

  await batchFormRef.value.validate(async (valid) => {
    if (valid) {
      batchSubmitting.value = true
      try {
        await timeslotApi.batchCreateByWeek(batchFormData)
        ElMessage.success('批量创建成功')
        batchDialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error('批量创建失败')
      } finally {
        batchSubmitting.value = false
      }
    }
  })
}

// 删除
const handleDelete = (row: TimeSlot) => {
  ElMessageBox.confirm('确定要删除该时间段吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await timeslotApi.delete(row.id!)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

// 启用/停用
const handleToggleActive = async (row: TimeSlot) => {
  try {
    await timeslotApi.toggleActive(row.id!, !row.isActive)
    ElMessage.success('操作成功')
    loadData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

const handleBatchDialogClose = () => {
  batchFormRef.value?.resetFields()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.timeslot-list {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header > div {
  display: flex;
  gap: 10px;
}
</style>
