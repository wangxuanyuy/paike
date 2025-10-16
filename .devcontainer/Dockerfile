FROM ubuntu:22.04

# 设置非交互模式
ENV DEBIAN_FRONTEND=noninteractive

# 安装 ca-certificates 首先更新证书
RUN apt-get update 
RUN apt-get install -y --no-install-recommends ca-certificates

# 再次更新并安装其他软件
RUN apt-get update && apt-get install -y --no-install-recommends \
    git curl wget unzip zip vim \
    openjdk-17-jdk maven gradle \
    && rm -rf /var/lib/apt/lists/*

# 新建开发用户
RUN useradd -m dev && echo "dev ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

USER root
WORKDIR /workspace
