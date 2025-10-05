<template>
  <div class="conflict-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>冲突警告</span>
          <el-button type="primary">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      <el-alert title="冲突警告功能待实现" type="info" :closable="false" />
    </el-card>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.conflict-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
