<template>
  <div id="app">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>教室与时间段管理系统</h1>
          <el-menu
            :default-active="activeMenu"
            mode="horizontal"
            @select="handleMenuSelect"
          >
            <el-menu-item index="/classroom">教室管理</el-menu-item>
            <el-menu-item index="/timeslot">时间段管理</el-menu-item>
          </el-menu>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const activeMenu = ref(route.path)

watch(() => route.path, (newPath) => {
  activeMenu.value = newPath
})

const handleMenuSelect = (index: string) => {
  router.push(index)
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100vh;
}

.el-header {
  background-color: #409eff;
  color: #fff;
  padding: 0;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
}

.header-content h1 {
  font-size: 22px;
  font-weight: 500;
}

.el-menu--horizontal {
  border: none;
  background-color: transparent;
}

.el-menu--horizontal .el-menu-item {
  color: #fff;
  border-bottom: 3px solid transparent;
}

.el-menu--horizontal .el-menu-item:hover,
.el-menu--horizontal .el-menu-item.is-active {
  background-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  border-bottom-color: #fff;
}

.el-main {
  background-color: #f5f7fa;
  padding: 0;
  overflow-y: auto;
}
</style>
