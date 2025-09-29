<template>
  <div id="app">
    <el-container>
      <el-header height="60px">
        <div class="header">
          <h1>学生选课系统</h1>
          <nav class="nav">
            <el-button 
              type="text" 
              @click="$router.push('/course-selection')"
              :class="{ active: $route.path === '/course-selection' }"
            >
              学生选课
            </el-button>
            <el-button 
              type="text" 
              @click="$router.push('/student-management')"
              :class="{ active: $route.path === '/student-management' }"
            >
              学生管理
            </el-button>
          </nav>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>
<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
  background: #409eff;
  color: white;
}
.header h1 {
  margin: 0;
  font-size: 20px;
}
.nav .el-button {
  color: white;
  margin-left: 20px;
}
.nav .el-button.active,
.nav .el-button:hover {
  color: #67c23a;
}
</style>
