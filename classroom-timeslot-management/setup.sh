#!/bin/bash

echo "========================================"
echo "   教室时间段管理系统 - Linux/Mac安装脚本"
echo "========================================"
echo ""

echo "[1/3] 检查 Node.js 环境..."
if ! command -v node &> /dev/null; then
    echo "❌ 未检测到 Node.js，请先安装 Node.js"
    echo "下载地址: https://nodejs.org/"
    exit 1
fi
echo "✅ Node.js 环境正常"
echo ""

echo "[2/3] 安装项目依赖..."
npm install
if [ $? -ne 0 ]; then
    echo "❌ 依赖安装失败"
    exit 1
fi
echo "✅ 依赖安装完成"
echo ""

echo "[3/3] 启动开发服务器..."
echo ""
echo "========================================"
echo "项目将在 http://localhost:45103 启动"
echo "按 Ctrl+C 可以停止服务器"
echo "========================================"
echo ""

npm run dev
