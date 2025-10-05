<template>
  <div class="course-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>课程管理</span>
          <el-button type="primary">
            <el-icon><Plus /></el-icon>
            添加课程
          </el-button>
        </div>
      </template>
      <el-alert title="课程管理功能待实现" type="info" :closable="false" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.course-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
