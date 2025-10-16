# 安装与部署指南

## 系统要求

- Node.js >= 18.0.0
- npm >= 9.0.0
- 现代浏览器（Chrome、Firefox、Edge、Safari 最新版本）

## 快速开始

### Windows 系统

双击运行 `setup.bat` 文件，脚本会自动：
1. 检查 Node.js 环境
2. 安装项目依赖
3. 启动开发服务器

### Linux/Mac 系统

```bash
chmod +x setup.sh
./setup.sh
```

## 手动安装

### 1. 安装依赖

```bash
npm install
```

### 2. 开发环境运行

```bash
npm run dev
```

访问地址：http://localhost:45103

### 3. 生产环境构建

```bash
npm run build
```

构建产物在 `dist` 目录下。

### 4. 预览生产构建

```bash
npm run preview
```

## 环境配置

### 开发环境

项目默认配置：
- 前端端口：45103
- API 代理地址：http://localhost:45180（KrakenD 网关）

### 修改端口

编辑 `vite.config.ts` 文件：

```typescript
export default defineConfig({
  server: {
    port: 45103,  // 修改此处端口
    proxy: {
      '/api': {
        target: 'http://localhost:45180',  // 修改后端地址
        changeOrigin: true
      }
    }
  }
})
```

同时需要修改 `package.json` 中的启动命令：

```json
{
  "scripts": {
    "dev": "vite --port 45103"  // 修改此处端口
  }
}
```

## 后端对接

### API 网关配置

确保 KrakenD 网关（端口 45180）已启动并配置了以下路由：

- `/api/classrooms/*` -> 学生课程服务（45081）
- `/api/time-slots/*` -> 学生课程服务（45081）

### CORS 配置

后端需要允许以下来源的跨域请求：
- `http://localhost:45103`（开发环境）
- 生产环境域名

## 部署

### Nginx 部署

1. 构建生产版本：

```bash
npm run build
```

2. 配置 Nginx：

```nginx
server {
    listen 80;
    server_name your-domain.com;

    root /path/to/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:45180;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

3. 重启 Nginx：

```bash
sudo nginx -s reload
```

### Docker 部署

1. 创建 `Dockerfile`：

```dockerfile
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

2. 构建镜像：

```bash
docker build -t classroom-timeslot-management .
```

3. 运行容器：

```bash
docker run -d -p 80:80 classroom-timeslot-management
```

## 常见问题

### 1. npm install 失败

**解决方法：**
- 清理缓存：`npm cache clean --force`
- 使用国内镜像：`npm config set registry https://registry.npmmirror.com`
- 删除 `node_modules` 和 `package-lock.json` 后重新安装

### 2. 端口被占用

**解决方法：**
- Windows: `netstat -ano | findstr :45103`，然后 `taskkill /PID <PID> /F`
- Linux/Mac: `lsof -ti:45103 | xargs kill`
- 或修改 `vite.config.ts` 中的端口

### 3. API 请求失败

**检查项：**
- KrakenD 网关是否启动（端口 45180）
- 学生课程服务是否启动（端口 45081）
- 网络代理配置是否正确
- 浏览器控制台查看具体错误信息

### 4. 页面空白

**解决方法：**
- 检查浏览器控制台错误
- 确认 `index.html` 正确加载
- 清除浏览器缓存
- 检查路由配置

### 5. TypeScript 报错

**解决方法：**
- 运行 `npm run build` 检查具体错误
- 确保 `tsconfig.json` 配置正确
- 检查导入路径是否正确

## 开发调试

### 开启 Vue DevTools

1. 安装 Vue DevTools 浏览器扩展
2. 开发模式下自动启用

### 查看网络请求

1. 打开浏览器开发者工具（F12）
2. 切换到 Network 标签页
3. 观察 API 请求和响应

### 调试技巧

- 使用 `console.log()` 打印变量
- 在浏览器中设置断点
- 使用 Vue DevTools 查看组件状态
- 检查 Network 面板的 API 调用

## 技术支持

如遇到问题，请检查：
1. Node.js 版本是否符合要求
2. 所有依赖是否正确安装
3. 后端服务是否正常运行
4. 网络连接是否正常
5. 浏览器控制台的错误信息

## 更新日志

### v1.0.0 (2024-01-01)
- ✅ 教室管理功能（增删改查）
- ✅ 时间段管理功能（增删改查）
- ✅ 批量创建时间段
- ✅ 教室使用情况统计
- ✅ 响应式布局设计
