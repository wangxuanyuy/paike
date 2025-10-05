# 教室与时间段管理系统

这是一个基于 Vue 3 + TypeScript + Element Plus 开发的教室和时间段管理系统，用于排课系统的后台管理。

## 功能特性

### 教室管理
- ✅ 教室列表展示（分页）
- ✅ 添加/编辑/删除教室
- ✅ 教室信息包括：编号、名称、最大容量、位置、设施
- ✅ 启用/停用教室
- ✅ 查看教室使用情况
- ✅ 教室使用率统计

### 时间段管理
- ✅ 按星期分组展示时间段
- ✅ 添加/编辑/删除时间段
- ✅ 时间段信息：名称、开始时间、结束时间、星期
- ✅ 批量创建时间段（可选多个星期）
- ✅ 启用/停用时间段

## 技术栈

- **前端框架**: Vue 3 (Composition API)
- **开发语言**: TypeScript
- **UI 组件库**: Element Plus
- **构建工具**: Vite
- **路由管理**: Vue Router
- **HTTP 客户端**: Axios

## 项目结构

```
classroom-timeslot-management/
├── src/
│   ├── api/                  # API 接口
│   │   ├── classroom.ts      # 教室 API
│   │   └── timeslot.ts       # 时间段 API
│   ├── router/               # 路由配置
│   │   └── index.ts
│   ├── types/                # TypeScript 类型定义
│   │   └── index.ts
│   ├── utils/                # 工具函数
│   │   └── request.ts        # Axios 封装
│   ├── views/                # 页面组件
│   │   ├── classroom/        # 教室管理
│   │   │   └── ClassroomList.vue
│   │   └── timeslot/         # 时间段管理
│   │       └── TimeSlotList.vue
│   ├── App.vue               # 根组件
│   └── main.ts               # 入口文件
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

## 安装与运行

### 安装依赖

```bash
npm install
```

### 开发模式运行

```bash
npm run dev
```

访问地址: http://localhost:45103

### 生产构建

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 端口配置

- **前端端口**: 45103
- **API 网关**: 45180 (通过 proxy 代理)

## API 接口

### 教室管理接口

- `GET /api/classrooms` - 获取教室列表（分页）
- `GET /api/classrooms/all` - 获取所有教室
- `GET /api/classrooms/:id` - 获取教室详情
- `POST /api/classrooms` - 创建教室
- `PUT /api/classrooms/:id` - 更新教室
- `DELETE /api/classrooms/:id` - 删除教室
- `PUT /api/classrooms/:id/toggle` - 启用/停用教室
- `GET /api/classrooms/usage` - 获取教室使用情况

### 时间段管理接口

- `GET /api/time-slots` - 获取时间段列表（分页）
- `GET /api/time-slots/all` - 获取所有时间段
- `GET /api/time-slots/day/:dayOfWeek` - 根据星期获取时间段
- `GET /api/time-slots/:id` - 获取时间段详情
- `POST /api/time-slots` - 创建时间段
- `PUT /api/time-slots/:id` - 更新时间段
- `DELETE /api/time-slots/:id` - 删除时间段
- `PUT /api/time-slots/:id/toggle` - 启用/停用时间段
- `POST /api/time-slots/batch-create` - 批量创建时间段

## 数据模型

### 教室 (Classroom)

```typescript
interface Classroom {
  id?: number
  classroomCode: string       // 教室编号
  classroomName: string       // 教室名称
  maxCapacity: number         // 最大容量
  location?: string           // 位置
  facilities?: string         // 设施
  isActive?: boolean          // 是否启用
  createdAt?: string
  updatedAt?: string
}
```

### 时间段 (TimeSlot)

```typescript
interface TimeSlot {
  id?: number
  slotName: string            // 时间段名称
  startTime: string           // 开始时间 (HH:mm:ss)
  endTime: string             // 结束时间 (HH:mm:ss)
  dayOfWeek: number           // 星期 (1-7)
  isActive?: boolean          // 是否启用
  createdAt?: string
  updatedAt?: string
}
```

## 星期映射

- 1 - 周一
- 2 - 周二
- 3 - 周三
- 4 - 周四
- 5 - 周五
- 6 - 周六
- 7 - 周日

## 后端对接说明

本系统需要对接以下后端服务：

1. **学生课程服务** (端口 45081) - 提供教室和时间段相关接口
2. **KrakenD 网关** (端口 45180) - API 统一入口

确保后端服务已启动并正确配置 CORS。

## 注意事项

1. 删除教室或时间段前，需要确认是否有关联的课程安排
2. 教室容量设置要合理，建议范围 1-100 人
3. 时间段设置要避免冲突，结束时间必须大于开始时间
4. 批量创建时间段时，系统会为选中的每个星期创建相同时间范围的时间段

## 开发计划

- [ ] 添加教室冲突检测
- [ ] 添加时间段冲突检测
- [ ] 导出教室使用报表
- [ ] 批量导入教室数据
- [ ] 权限管理集成

## License

MIT
