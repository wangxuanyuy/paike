#!/bin/bash

echo "启动教师排课系统后端..."
cd backend

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请安装JDK 17+"
    exit 1
fi

# 检查Maven环境
if ! command -v mvn &> /dev/null; then
    echo "错误: 未找到Maven，请安装Maven 3.6+"
    exit 1
fi

# 编译项目
echo "编译项目..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "编译成功，启动应用..."
    java -jar target/teacher-scheduling-system-1.0.0.jar
else
    echo "编译失败，请检查错误信息"
    exit 1
fi
