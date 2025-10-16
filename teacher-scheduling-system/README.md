# 教师排课管理系统

## 项目结构

teacher-scheduling-system/
├── backend/ # Spring Boot后端
├── frontend/ # Vue3前端
├── start-backend.sh # 后端启动脚本
├── start-frontend.sh # 前端启动脚本
└── README.md # 说明文档

text

## 环境要求
- JDK 17+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+
- Redis (可选)

## 快速启动

### 1. 启动后端
```bash
chmod +x start-backend.sh
./start-backend.sh
后端服务将在 http://localhost:45082 启动

2. 启动前端
bash
chmod +x start-frontend.sh
./start-frontend.sh
前端服务将在 http://localhost:45100 启动

数据库配置
确保MySQL服务运行在 localhost:45306

创建数据库: CREATE DATABASE school_system;

应用启动时会自动创建表结构

手动执行 backend/src/main/resources/init.sql 插入初始数据

功能特性
✅ 教师管理（增删改查）
✅ 课程安排管理
✅ 冲突检测
✅ 教师分配
✅ 响应式前端界面

待完善功能
课程管理完整界面

教室管理完整界面

冲突警告完整界面

教师分配对话框

批量排课功能

开发说明
项目使用标准的Spring Boot + Vue3技术栈，代码结构清晰，易于扩展。
