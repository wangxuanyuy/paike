# 快速启动指南

## 5分钟快速上手

### 第一步：检查环境

确保已安装：
- ✅ Node.js 18+
- ✅ npm 9+

检查版本：
```bash
node --version
npm --version
```

### 第二步：启动项目

#### Windows 用户
双击 `setup.bat` 即可自动完成所有步骤

#### Linux/Mac 用户
```bash
chmod +x setup.sh
./setup.sh
```

#### 手动启动
```bash
npm install
npm run dev
```

### 第三步：访问系统

打开浏览器访问：**http://localhost:45103**

---

## 主要功能

### 📚 教室管理
- 查看教室列表
- 添加/编辑教室
- 查看使用情况
- 启用/停用教室

### ⏰ 时间段管理
- 按星期查看时间段
- 添加/编辑时间段
- 批量创建时间段
- 启用/停用时间段

---

## 快速操作

### 添加教室
1. 点击「教室管理」
2. 点击「添加教室」按钮
3. 填写表单
4. 点击「确定」

### 添加时间段
1. 点击「时间段管理」
2. 选择星期标签
3. 点击「添加时间段」
4. 填写表单
5. 点击「确定」

### 批量创建时间段
1. 点击「时间段管理」
2. 点击「批量创建」按钮
3. 设置时间段信息
4. 选择多个星期
5. 点击「批量创建」

---

## 端口配置

| 服务 | 端口 | 说明 |
|-----|------|------|
| 前端 | 45103 | 本系统 |
| 网关 | 45180 | KrakenD |
| 后端 | 45081 | 学生课程服务 |
| MySQL | 45306 | 数据库 |
| Redis | 45379 | 缓存 |

---

## 常用命令

```bash
# 安装依赖
npm install

# 开发模式
npm run dev

# 生产构建
npm run build

# 预览构建
npm run preview

# 代码检查
npm run lint
```

---

## 目录说明

```
src/
├── api/          # API 接口
├── views/        # 页面组件
├── router/       # 路由配置
├── types/        # 类型定义
└── utils/        # 工具函数
```

---

## 后端要求

启动本系统前，确保以下服务已运行：

1. **MySQL** (端口 45306)
   - 数据库名：school_system
   - 已导入表结构

2. **KrakenD 网关** (端口 45180)
   - 已配置路由规则

3. **学生课程服务** (端口 45081)
   - 提供教室和时间段接口

---

## 故障排查

### 启动失败？
```bash
# 清理重装
rm -rf node_modules package-lock.json
npm install
```

### 端口冲突？
修改 `vite.config.ts` 和 `package.json` 中的端口配置

### API 无响应？
检查后端服务是否启动：
```bash
curl http://localhost:45180/api/classrooms
```

---

## 下一步

- 📖 查看 [README.md](./README.md) 了解详细功能
- 📋 查看 [API.md](./API.md) 了解接口文档
- 🛠️ 查看 [INSTALL.md](./INSTALL.md) 了解部署方案
- 📚 查看 [PROJECT.md](./PROJECT.md) 了解项目架构

---

## 技术支持

遇到问题？
1. 查看 [常见问题](#故障排查)
2. 检查浏览器控制台错误
3. 查看 Network 请求状态
4. 联系技术支持

---

**祝使用愉快！** 🎉
