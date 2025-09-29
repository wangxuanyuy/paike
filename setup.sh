#!/bin/bash

echo "========================================"
echo "学生选课系统 - 项目设置脚本"
echo "========================================"

echo ""
echo "正在检查环境..."

# 检查 Java
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到 Java，请安装 Java 17 或更高版本"
    exit 1
fi
echo "✅ Java 环境检查通过"

# 检查 Node.js
if ! command -v node &> /dev/null; then
    echo "❌ 错误: 未找到 Node.js，请安装 Node.js 16 或更高版本"
    exit 1
fi
echo "✅ Node.js 环境检查通过"

# 检查 MySQL
if ! command -v mysql &> /dev/null; then
    echo "⚠️  警告: 未找到 MySQL 命令行工具，请确保 MySQL 服务正在运行"
else
    echo "✅ MySQL 环境检查通过"
fi

echo ""
echo "========================================"
echo "开始设置项目..."
echo "========================================"

# 设置后端
echo ""
echo "📦 设置后端项目..."
cd student-course-backend

# 检查 Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ 错误: 未找到 Maven，请安装 Maven"
    exit 1
fi

echo "正在下载依赖..."
mvn clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ 后端依赖安装失败"
    exit 1
fi
echo "✅ 后端依赖安装完成"

cd ..

# 设置前端
echo ""
echo "📦 设置前端项目..."
cd student-course-frontend

echo "正在安装依赖..."
npm install
if [ $? -ne 0 ]; then
    echo "❌ 前端依赖安装失败"
    exit 1
fi
echo "✅ 前端依赖安装完成"

cd ..

echo ""
echo "========================================"
echo "项目设置完成！"
echo "========================================"
echo ""
echo "📋 接下来的步骤:"
echo ""
echo "1. 数据库设置:"
echo "   - 启动 MySQL 服务"
echo "   - 创建数据库: CREATE DATABASE school_system;"
echo "   - 运行建表脚本: database-schema.sql"
echo "   - 运行数据脚本: student-course-backend/src/main/resources/data.sql"
echo ""
echo "2. 启动后端服务:"
echo "   cd student-course-backend"
echo "   mvn spring-boot:run"
echo ""
echo "3. 启动前端服务 (新终端):"
echo "   cd student-course-frontend"
echo "   npm run dev"
echo ""
echo "4. 访问应用:"
echo "   前端: http://localhost:3000"
echo "   后端: http://localhost:45081"
echo ""
echo "========================================"
