<template>
  <div class="classroom-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>教室管理</span>
          <el-button type="primary">
            <el-icon><Plus /></el-icon>
            添加教室
          </el-button>
        </div>
      </template>
      <el-alert title="教室管理功能待实现" type="info" :closable="false" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.classroom-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
