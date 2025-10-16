# 学生选课系统 (Student Course Management System)

一个基于 Spring Boot + Vue 3 的全栈学生选课管理系统，支持学生注册、课程选择、时间表管理等功能。

## 🚀 项目特性

- **后端**: Spring Boot 3.2.0 + MySQL + Redis
- **前端**: Vue 3 + TypeScript + Element Plus
- **功能**: 学生注册、课程选择、时间表管理、选课统计
- **架构**: RESTful API + 响应式前端

## 📋 系统要求

- Java 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+

## 🛠️ 快速开始

### 1. 数据库设置

```sql
-- 创建数据库
CREATE DATABASE school_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建表结构
-- 请运行 student-course-backend/src/main/resources/data.sql 中的建表语句
```

### 2. 后端启动

```bash
cd student-course-backend

# 修改数据库配置
# 编辑 src/main/resources/application-dev.yml
# 更新数据库连接信息

# 启动后端服务
mvn spring-boot:run
```

后端服务将在 `http://localhost:45081` 启动

### 3. 前端启动

```bash
cd student-course-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端应用将在 `http://localhost:3000` 启动

## 📁 项目结构

```
paike/
├── student-course-backend/          # Spring Boot 后端
│   ├── src/main/java/com/school/course/
│   │   ├── config/                  # 配置类
│   │   ├── controller/              # 控制器
│   │   ├── entity/                  # 实体类
│   │   ├── repository/              # 数据访问层
│   │   ├── service/                 # 业务逻辑层
│   │   ├── dto/                     # 数据传输对象
│   │   └── exception/               # 异常处理
│   └── src/main/resources/
│       ├── application.yml          # 主配置文件
│       ├── application-dev.yml      # 开发环境配置
│       └── data.sql                 # 初始化数据
├── student-course-frontend/         # Vue 3 前端
│   ├── src/
│   │   ├── api/                     # API 接口
│   │   ├── components/              # 组件
│   │   ├── views/                   # 页面
│   │   ├── stores/                  # 状态管理
│   │   ├── types/                   # 类型定义
│   │   └── router/                  # 路由配置
│   └── package.json
└── README.md
```

## 🔧 配置说明

### 后端配置

主要配置文件：`student-course-backend/src/main/resources/application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/school_system
    username: your_username
    password: your_password
```

### 前端配置

API 基础路径配置在：`student-course-frontend/src/api/request.ts`

```typescript
const request = new Request({
  baseURL: '/api',  // 代理到后端
  timeout: 30000
})
```

## 📚 API 接口

### 学生管理
- `GET /api/students` - 获取所有学生
- `POST /api/students/register` - 学生注册
- `PUT /api/students/{id}` - 更新学生信息

### 课程管理
- `GET /api/courses/schedule` - 获取课程表
- `POST /api/enrollments` - 学生选课
- `DELETE /api/enrollments` - 取消选课

## 🎯 主要功能

1. **学生注册**: 支持学生信息录入和审核
2. **课程选择**: 可视化课程表，支持选课和退课
3. **时间管理**: 按周查看课程安排
4. **数据统计**: 选课情况统计和分析

## 🚀 部署说明

### 后端部署
```bash
cd student-course-backend
mvn clean package
java -jar target/student-course-backend-1.0.0.jar
```

### 前端部署
```bash
cd student-course-frontend
npm run build
# 将 dist 目录部署到 Web 服务器
```

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 📄 许可证

MIT License

## 📞 联系方式

如有问题，请提交 Issue 或联系开发团队。
