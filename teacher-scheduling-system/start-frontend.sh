#!/bin/bash

echo "启动教师排课系统前端..."
cd frontend

# 检查Node.js环境
if ! command -v node &> /dev/null; then
    echo "错误: 未找到Node.js，请安装Node.js 18+"
    exit 1
fi

# 检查npm环境
if ! command -v npm &> /dev/null; then
    echo "错误: 未找到npm"
    exit 1
fi

# 安装依赖
if [ ! -d "node_modules" ]; then
    echo "安装依赖..."
    npm install
fi

echo "启动开发服务器..."
npm run dev
