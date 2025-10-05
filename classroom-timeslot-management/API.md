# API 接口文档

本文档描述了教室与时间段管理系统的前后端接口规范。

## 基本信息

- **Base URL**: `/api`
- **网关地址**: `http://localhost:45180`
- **后端服务**: `http://localhost:45081` (学生课程服务)
- **数据格式**: JSON
- **字符编码**: UTF-8

## 通用响应格式

### 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {
    // 具体数据
  }
}
```

### 错误响应

```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

### 状态码说明

| 状态码 | 说明 |
|-------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

## 教室管理接口

### 1. 获取教室列表（分页）

**请求**

```
GET /api/classrooms
```

**参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | number | 否 | 页码，从0开始，默认0 |
| size | number | 否 | 每页数量，默认10 |
| keyword | string | 否 | 搜索关键词 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "classroomCode": "CLS001",
        "classroomName": "阳光教室",
        "maxCapacity": 20,
        "location": "一楼东侧",
        "facilities": "投影仪,音响,空调",
        "isActive": true,
        "createdAt": "2024-01-01T10:00:00",
        "updatedAt": "2024-01-01T10:00:00"
      }
    ],
    "totalElements": 100,
    "totalPages": 10,
    "number": 0,
    "size": 10
  }
}
```

### 2. 获取所有教室（不分页）

**请求**

```
GET /api/classrooms/all
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "classroomCode": "CLS001",
      "classroomName": "阳光教室",
      "maxCapacity": 20,
      "location": "一楼东侧",
      "facilities": "投影仪,音响,空调",
      "isActive": true
    }
  ]
}
```

### 3. 获取教室详情

**请求**

```
GET /api/classrooms/{id}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | number | 教室ID |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "classroomCode": "CLS001",
    "classroomName": "阳光教室",
    "maxCapacity": 20,
    "location": "一楼东侧",
    "facilities": "投影仪,音响,空调",
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 4. 创建教室

**请求**

```
POST /api/classrooms
```

**请求体**

```json
{
  "classroomCode": "CLS001",
  "classroomName": "阳光教室",
  "maxCapacity": 20,
  "location": "一楼东侧",
  "facilities": "投影仪,音响,空调",
  "isActive": true
}
```

**字段说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| classroomCode | string | 是 | 教室编号，唯一 |
| classroomName | string | 是 | 教室名称 |
| maxCapacity | number | 是 | 最大容量，1-100 |
| location | string | 否 | 位置 |
| facilities | string | 否 | 设施，多个用逗号分隔 |
| isActive | boolean | 否 | 是否启用，默认true |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "classroomCode": "CLS001",
    "classroomName": "阳光教室",
    "maxCapacity": 20,
    "location": "一楼东侧",
    "facilities": "投影仪,音响,空调",
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 5. 更新教室

**请求**

```
PUT /api/classrooms/{id}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | number | 教室ID |

**请求体**

```json
{
  "classroomCode": "CLS001",
  "classroomName": "阳光教室",
  "maxCapacity": 25,
  "location": "一楼东侧",
  "facilities": "投影仪,音响,空调",
  "isActive": true
}
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "classroomCode": "CLS001",
    "classroomName": "阳光教室",
    "maxCapacity": 25,
    "location": "一楼东侧",
    "facilities": "投影仪,音响,空调",
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-02T10:00:00"
  }
}
```

### 6. 删除教室

**请求**

```
DELETE /api/classrooms/{id}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | number | 教室ID |

**响应**

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 7. 启用/停用教室

**请求**

```
PUT /api/classrooms/{id}/toggle
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | number | 教室ID |

**请求体**

```json
{
  "isActive": false
}
```

**响应**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

### 8. 获取教室使用情况

**请求**

```
GET /api/classrooms/usage
```

**参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| classroomId | number | 否 | 教室ID |
| startDate | string | 否 | 开始日期 YYYY-MM-DD |
| endDate | string | 否 | 结束日期 YYYY-MM-DD |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "classroomId": 1,
      "classroomName": "阳光教室",
      "usageDate": "2024-01-01",
      "timeSlotId": 1,
      "timeSlotName": "早班",
      "actualStudentCount": 15,
      "maxCapacity": 20
    }
  ]
}
```

### 9. 批量删除教室

**请求**

```
POST /api/classrooms/batch-delete
```

**请求体**

```json
{
  "ids": [1, 2, 3]
}
```

**响应**

```json
{
  "code": 200,
  "message": "批量删除成功",
  "data": null
}
```

## 时间段管理接口

### 1. 获取时间段列表（分页）

**请求**

```
GET /api/time-slots
```

**参数**

| 参数 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| page | number | 否 | 页码，从0开始 |
| size | number | 否 | 每页数量 |
| keyword | string | 否 | 搜索关键词 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "content": [
      {
        "id": 1,
        "slotName": "早班",
        "startTime": "09:00:00",
        "endTime": "11:00:00",
        "dayOfWeek": 1,
        "isActive": true,
        "createdAt": "2024-01-01T10:00:00",
        "updatedAt": "2024-01-01T10:00:00"
      }
    ],
    "totalElements": 28,
    "totalPages": 3,
    "number": 0,
    "size": 10
  }
}
```

### 2. 获取所有时间段（不分页）

**请求**

```
GET /api/time-slots/all
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "slotName": "早班",
      "startTime": "09:00:00",
      "endTime": "11:00:00",
      "dayOfWeek": 1,
      "isActive": true
    }
  ]
}
```

### 3. 根据星期获取时间段

**请求**

```
GET /api/time-slots/day/{dayOfWeek}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| dayOfWeek | number | 星期几，1-7 |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "slotName": "早班",
      "startTime": "09:00:00",
      "endTime": "11:00:00",
      "dayOfWeek": 1,
      "isActive": true
    }
  ]
}
```

### 4. 获取时间段详情

**请求**

```
GET /api/time-slots/{id}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | number | 时间段ID |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "slotName": "早班",
    "startTime": "09:00:00",
    "endTime": "11:00:00",
    "dayOfWeek": 1,
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 5. 创建时间段

**请求**

```
POST /api/time-slots
```

**请求体**

```json
{
  "slotName": "早班",
  "startTime": "09:00:00",
  "endTime": "11:00:00",
  "dayOfWeek": 1,
  "isActive": true
}
```

**字段说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| slotName | string | 是 | 时间段名称 |
| startTime | string | 是 | 开始时间 HH:mm:ss |
| endTime | string | 是 | 结束时间 HH:mm:ss |
| dayOfWeek | number | 是 | 星期几，1-7 |
| isActive | boolean | 否 | 是否启用，默认true |

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "slotName": "早班",
    "startTime": "09:00:00",
    "endTime": "11:00:00",
    "dayOfWeek": 1,
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  }
}
```

### 6. 更新时间段

**请求**

```
PUT /api/time-slots/{id}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | number | 时间段ID |

**请求体**

```json
{
  "slotName": "早班",
  "startTime": "09:00:00",
  "endTime": "12:00:00",
  "dayOfWeek": 1,
  "isActive": true
}
```

**响应**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "slotName": "早班",
    "startTime": "09:00:00",
    "endTime": "12:00:00",
    "dayOfWeek": 1,
    "isActive": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-02T10:00:00"
  }
}
```

### 7. 删除时间段

**请求**

```
DELETE /api/time-slots/{id}
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | number | 时间段ID |

**响应**

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 8. 启用/停用时间段

**请求**

```
PUT /api/time-slots/{id}/toggle
```

**路径参数**

| 参数 | 类型 | 说明 |
|-----|------|------|
| id | number | 时间段ID |

**请求体**

```json
{
  "isActive": false
}
```

**响应**

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

### 9. 批量创建时间段

**请求**

```
POST /api/time-slots/batch-create
```

**请求体**

```json
{
  "slotName": "早班",
  "startTime": "09:00:00",
  "endTime": "11:00:00",
  "dayOfWeeks": [1, 2, 3, 4, 5]
}
```

**字段说明**

| 字段 | 类型 | 必填 | 说明 |
|-----|------|-----|------|
| slotName | string | 是 | 时间段名称 |
| startTime | string | 是 | 开始时间 |
| endTime | string | 是 | 结束时间 |
| dayOfWeeks | number[] | 是 | 星期数组 |

**响应**

```json
{
  "code": 200,
  "message": "批量创建成功",
  "data": null
}
```

### 10. 批量删除时间段

**请求**

```
POST /api/time-slots/batch-delete
```

**请求体**

```json
{
  "ids": [1, 2, 3]
}
```

**响应**

```json
{
  "code": 200,
  "message": "批量删除成功",
  "data": null
}
```

## 数据模型

### Classroom（教室）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 主键ID |
| classroomCode | string | 教室编号 |
| classroomName | string | 教室名称 |
| maxCapacity | number | 最大容量 |
| location | string | 位置 |
| facilities | string | 设施 |
| isActive | boolean | 是否启用 |
| createdAt | string | 创建时间 |
| updatedAt | string | 更新时间 |

### TimeSlot（时间段）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 主键ID |
| slotName | string | 时间段名称 |
| startTime | string | 开始时间 |
| endTime | string | 结束时间 |
| dayOfWeek | number | 星期几 1-7 |
| isActive | boolean | 是否启用 |
| createdAt | string | 创建时间 |
| updatedAt | string | 更新时间 |

### ClassroomUsage（教室使用情况）

| 字段 | 类型 | 说明 |
|-----|------|------|
| id | number | 主键ID |
| classroomId | number | 教室ID |
| classroomName | string | 教室名称 |
| usageDate | string | 使用日期 |
| timeSlotId | number | 时间段ID |
| timeSlotName | string | 时间段名称 |
| actualStudentCount | number | 实际学生数 |
| maxCapacity | number | 最大容量 |

## 错误码说明

| 错误码 | 说明 | 处理建议 |
|-------|------|---------|
| 1001 | 教室编号已存在 | 使用不同的编号 |
| 1002 | 教室不存在 | 检查ID是否正确 |
| 1003 | 教室正在使用中 | 取消相关排课后再删除 |
| 2001 | 时间段冲突 | 调整时间范围 |
| 2002 | 时间段不存在 | 检查ID是否正确 |
| 2003 | 时间段正在使用中 | 取消相关排课后再删除 |
| 9999 | 系统错误 | 联系管理员 |

## 注意事项

1. 所有时间格式使用 `HH:mm:ss`（24小时制）
2. 日期格式使用 `YYYY-MM-DD`
3. dayOfWeek: 1=周一, 2=周二, ..., 7=周日
4. 删除操作需要检查是否有关联数据
5. 批量操作需要事务支持
6. 建议使用分页查询以提高性能
