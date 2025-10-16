# 教室与时间段管理系统 - 项目文档

## 项目概述

本项目是排课系统的一个独立模块，专门用于管理教室和时间段，提供了完整的增删改查功能，方便管理员从后台进行设置。

### 项目信息

- **项目名称**: 教室与时间段管理系统
- **版本**: v1.0.0
- **开发时间**: 2024年
- **运行端口**: 45103
- **技术栈**: Vue 3 + TypeScript + Element Plus

## 目录结构

```
classroom-timeslot-management/
│
├── public/                      # 静态资源目录
│
├── src/                         # 源代码目录
│   ├── api/                     # API 接口层
│   │   ├── classroom.ts         # 教室 API
│   │   └── timeslot.ts          # 时间段 API
│   │
│   ├── router/                  # 路由配置
│   │   └── index.ts             # 路由定义
│   │
│   ├── types/                   # TypeScript 类型定义
│   │   └── index.ts             # 通用类型
│   │
│   ├── utils/                   # 工具函数
│   │   └── request.ts           # Axios 封装
│   │
│   ├── views/                   # 页面组件
│   │   ├── classroom/           # 教室管理
│   │   │   └── ClassroomList.vue
│   │   └── timeslot/            # 时间段管理
│   │       └── TimeSlotList.vue
│   │
│   ├── App.vue                  # 根组件
│   └── main.ts                  # 应用入口
│
├── .eslintrc.cjs                # ESLint 配置
├── .gitignore                   # Git 忽略文件
├── API.md                       # API 接口文档
├── env.d.ts                     # 环境变量类型定义
├── index.html                   # HTML 入口
├── INSTALL.md                   # 安装部署文档
├── package.json                 # 项目依赖配置
├── PROJECT.md                   # 项目文档（本文件）
├── README.md                    # 项目说明
├── setup.bat                    # Windows 启动脚本
├── setup.sh                     # Linux/Mac 启动脚本
├── tsconfig.json                # TypeScript 配置
├── tsconfig.node.json           # Node TypeScript 配置
└── vite.config.ts               # Vite 配置
```

## 核心功能

### 1. 教室管理

#### 功能列表
- ✅ 教室列表展示（支持分页和搜索）
- ✅ 添加教室
- ✅ 编辑教室信息
- ✅ 删除教室
- ✅ 启用/停用教室
- ✅ 查看教室使用情况
- ✅ 教室使用率统计

#### 数据字段
| 字段 | 类型 | 说明 |
|-----|------|------|
| 教室编号 | string | 唯一标识 |
| 教室名称 | string | 显示名称 |
| 最大容量 | number | 可容纳学生数 |
| 位置 | string | 教室位置 |
| 设施 | string | 教学设施 |
| 状态 | boolean | 启用/停用 |

#### 业务规则
1. 教室编号必须唯一
2. 最大容量范围：1-100人
3. 删除前需检查是否有课程安排
4. 停用的教室不能用于新排课

### 2. 时间段管理

#### 功能列表
- ✅ 按星期分组展示时间段
- ✅ 添加时间段
- ✅ 编辑时间段
- ✅ 删除时间段
- ✅ 批量创建时间段（可选多个星期）
- ✅ 启用/停用时间段

#### 数据字段
| 字段 | 类型 | 说明 |
|-----|------|------|
| 时间段名称 | string | 如：早班、中班 |
| 开始时间 | string | HH:mm:ss 格式 |
| 结束时间 | string | HH:mm:ss 格式 |
| 星期 | number | 1-7（周一到周日）|
| 状态 | boolean | 启用/停用 |

#### 业务规则
1. 结束时间必须大于开始时间
2. 同一天的时间段不应重叠
3. 批量创建可为多个星期创建相同时间段
4. 删除前需检查是否有课程安排

## 技术架构

### 前端技术栈

| 技术 | 版本 | 用途 |
|-----|------|------|
| Vue | 3.4+ | 前端框架 |
| TypeScript | 5.3+ | 类型支持 |
| Element Plus | 2.5+ | UI 组件库 |
| Vue Router | 4.2+ | 路由管理 |
| Axios | 1.6+ | HTTP 客户端 |
| Vite | 5.0+ | 构建工具 |

### 后端对接

```
Frontend (45103)
    ↓
KrakenD Gateway (45180)
    ↓
Student Course Service (45081)
    ↓
MySQL Database (45306)
```

### 代码规范

#### 命名规范
- **组件**: PascalCase（如：`ClassroomList.vue`）
- **文件**: kebab-case（如：`classroom.ts`）
- **变量**: camelCase（如：`classroomList`）
- **常量**: UPPER_SNAKE_CASE（如：`DAY_LABELS`）
- **类型**: PascalCase（如：`Classroom`）

#### 目录规范
- `api/`: API 接口封装
- `views/`: 页面级组件
- `components/`: 可复用组件（如需要）
- `types/`: TypeScript 类型定义
- `utils/`: 工具函数
- `router/`: 路由配置

#### Git 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 测试相关
chore: 构建/工具链相关
```

## 数据流程

### 教室管理流程

```
1. 用户操作
   ↓
2. Vue 组件触发
   ↓
3. 调用 API 层
   ↓
4. Axios 发送请求
   ↓
5. 经过 KrakenD 网关
   ↓
6. 到达后端服务
   ↓
7. 数据库操作
   ↓
8. 返回响应
   ↓
9. 更新 UI
```

### 状态管理

目前使用组件内部状态管理（ref/reactive）。如需要全局状态，可以：
1. 引入 Pinia
2. 使用 provide/inject
3. 使用 EventBus

## API 设计

### RESTful 规范

| 方法 | 路径 | 说明 |
|-----|------|------|
| GET | /api/classrooms | 获取列表 |
| GET | /api/classrooms/:id | 获取详情 |
| POST | /api/classrooms | 创建 |
| PUT | /api/classrooms/:id | 更新 |
| DELETE | /api/classrooms/:id | 删除 |

### 响应格式

```typescript
interface ApiResponse<T> {
  code: number      // 状态码
  message: string   // 消息
  data: T          // 数据
}
```

## 开发指南

### 本地开发

```bash
# 1. 克隆代码
cd D:\1vueproj\paike\classroom-timeslot-management

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev

# 4. 访问
http://localhost:45103
```

### 添加新功能

1. **添加新页面**
   ```bash
   # 在 src/views 创建组件
   src/views/新功能/NewFeature.vue
   ```

2. **添加路由**
   ```typescript
   // src/router/index.ts
   {
     path: '/new-feature',
     name: 'NewFeature',
     component: () => import('@/views/新功能/NewFeature.vue')
   }
   ```

3. **添加 API**
   ```typescript
   // src/api/newfeature.ts
   export const newfeatureApi = {
     getList() {
       return request.get('/api/newfeature')
     }
   }
   ```

### 调试技巧

1. **Vue DevTools**: 安装浏览器扩展
2. **Network**: 查看 API 请求
3. **Console**: 使用 console.log
4. **Breakpoints**: 在浏览器中设置断点

## 部署流程

### 1. 构建生产版本

```bash
npm run build
```

### 2. 部署到服务器

```bash
# 方式一：Nginx
cp -r dist/* /var/www/html/

# 方式二：Docker
docker build -t classroom-timeslot-management .
docker run -d -p 80:80 classroom-timeslot-management
```

### 3. 环境配置

生产环境需要配置：
- API 网关地址
- 跨域设置
- HTTPS 证书
- 负载均衡

## 测试

### 单元测试（规划中）

```bash
npm run test:unit
```

### E2E 测试（规划中）

```bash
npm run test:e2e
```

### 手动测试清单

- [ ] 教室列表加载
- [ ] 教室添加/编辑/删除
- [ ] 教室搜索功能
- [ ] 教室使用情况查询
- [ ] 时间段按星期显示
- [ ] 时间段添加/编辑/删除
- [ ] 批量创建时间段
- [ ] 启用/停用功能

## 性能优化

### 已实现
- ✅ 组件按需加载（路由懒加载）
- ✅ 图标按需引入
- ✅ API 请求防抖
- ✅ 列表分页加载

### 待优化
- [ ] 虚拟滚动（长列表）
- [ ] 请求缓存
- [ ] 图片懒加载
- [ ] CDN 加速

## 安全考虑

### 前端安全
1. XSS 防护：Element Plus 已处理
2. CSRF 防护：Token 验证
3. 输入验证：表单验证
4. 敏感数据：不在前端存储

### 接口安全
1. HTTPS 传输
2. Token 认证
3. 权限校验
4. 请求签名（规划中）

## 常见问题

### Q1: 端口被占用？
```bash
# Windows
netstat -ano | findstr :45103
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:45103 | xargs kill
```

### Q2: 依赖安装失败？
```bash
npm cache clean --force
npm install
```

### Q3: API 请求失败？
检查：
1. 后端服务是否启动
2. 网关配置是否正确
3. 网络连接是否正常

### Q4: 页面空白？
1. 检查浏览器控制台错误
2. 确认路由配置正确
3. 清除浏览器缓存

## 后续规划

### v1.1.0
- [ ] 教室冲突检测
- [ ] 时间段冲突检测
- [ ] 数据导出功能

### v1.2.0
- [ ] 批量导入教室
- [ ] 使用统计报表
- [ ] 图表可视化

### v2.0.0
- [ ] 权限管理
- [ ] 多租户支持
- [ ] 国际化支持

## 团队协作

### 开发流程
1. 从 main 分支创建功能分支
2. 完成开发并自测
3. 提交 Pull Request
4. Code Review
5. 合并到 main

### 代码审查要点
- 代码规范
- 类型定义
- 错误处理
- 注释文档
- 性能考虑

## 联系方式

如有问题，请联系：
- 项目负责人：[填写]
- 技术支持：[填写]
- 文档维护：[填写]

## 许可证

MIT License

---

**最后更新时间**: 2024年1月
**维护者**: 开发团队
