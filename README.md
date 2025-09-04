# NFS-RPC

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-1.8+-green.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/maven-3.6+-orange.svg)](https://maven.apache.org/)

NFS-RPC 是一个高性能的Java RPC框架，支持多种网络传输协议和序列化方式，提供简单易用的API接口。

## 📋 项目简介

NFS-RPC 是一个基于Java开发的高性能RPC框架，具有以下特点：

* **多协议支持**: 支持Netty、Netty4、Mina2、Grizzly等多种网络传输协议
* **多序列化方式**: 支持Java原生序列化、Hessian、Kryo、Protobuf等多种序列化方式
* **高性能**: 基于异步非阻塞I/O，支持连接池和负载均衡
* **易扩展**: 模块化设计，支持自定义协议和序列化方式
* **生产就绪**: 包含完整的性能测试套件和监控功能

## 🏗️ 项目架构

```xml
nfs-rpc/
├── nfs-rpc-core/          # 核心模块，包含RPC基础框架
├── nfs-rpc-codec/         # 序列化模块，支持多种序列化方式
├── nfs-rpc-netty/         # Netty传输协议实现
├── nfs-rpc-netty4/        # Netty4传输协议实现
├── nfs-rpc-mina/          # Mina2传输协议实现
├── nfs-rpc-grizzly/       # Grizzly传输协议实现
└── nfs-rpc-benchmark/     # 性能测试模块
```

## ✨ 主要特性

### 传输协议支持

* **Netty**: 基于Netty 3.x的高性能异步网络框架
* **Netty4**: 基于Netty 4.x的现代化网络框架
* **Mina2**: 基于Apache Mina 2.x的网络应用框架
* **Grizzly**: 基于Grizzly的网络框架

### 序列化支持

* **Java**: Java原生序列化
* **Hessian**: 高效的二进制序列化协议
* **Kryo**: 快速、高效的Java序列化库
* **Protobuf**: Google的高性能序列化协议

### 核心功能

* 同步/异步RPC调用
* 连接池管理
* 负载均衡
* 超时控制
* 异常处理
* 性能监控

## 🚀 快速开始

### 环境要求

* JDK 1.8+
* Maven 3.6+

### 依赖配置

在Maven项目中添加依赖：

```xml
<dependency>
    <groupId>com.bytesgo.nfs.rpc</groupId>
    <artifactId>nfs-rpc-core</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>

<!-- 选择传输协议 -->
<dependency>
    <groupId>com.bytesgo.nfs.rpc</groupId>
    <artifactId>nfs-rpc-netty</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>

<!-- 选择序列化方式 -->
<dependency>
    <groupId>com.bytesgo.nfs.rpc</groupId>
    <artifactId>nfs-rpc-codec</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

### 服务端示例

```java
import com.bytesgo.nfs.rpc.core.server.Server;
import com.bytesgo.nfs.rpc.netty.server.NettyServer;

public class RPCServer {
    public static void main(String[] args) throws Exception {
        // 创建服务器配置
        ServerConfig config = new ServerConfig();
        config.setHost("127.0.0.1");
        config.setPort(8888);
        config.setBusinessThreadPool(Executors.newFixedThreadPool(20));
        
        // 创建服务器
        Server server = new NettyServer(config);
        
        // 注册服务处理器
        server.registerProcessor(RPCProtocol.TYPE, "testservice", new TestServiceImpl());
        
        // 启动服务器
        server.start();
        System.out.println("RPC Server started on port 8888");
    }
}
```

### 客户端示例

```java
import com.bytesgo.nfs.rpc.core.client.ClientFactory;
import com.bytesgo.nfs.rpc.netty.client.NettyClientFactory;

public class RPCClient {
    public static void main(String[] args) throws Exception {
        // 创建客户端工厂
        ClientFactory clientFactory = new NettyClientFactory();
        
        // 获取客户端
        Client client = clientFactory.get("127.0.0.1", 8888, 1000);
        
        // 创建调用对象
        Invocation invocation = new Invocation()
            .setProcessorName("testservice")
            .setMethodName("execute")
            .setArgs(new Object[]{"Hello RPC"});
        
        // 执行RPC调用
        Object result = client.invokeSync(invocation, 5000, Codecs.JAVA_CODEC, RPCProtocol.TYPE);
        System.out.println("RPC Result: " + result);
    }
}
```

## 📊 性能测试

项目包含完整的性能测试套件，支持不同协议和序列化方式的性能对比。

### 运行性能测试

#### 启动服务器

```bash
# Netty4服务器
cd nfs-rpc-benchmark/src/nfs-script/server
sh netty4server.sh 8888 20 1024

# 参数说明: 端口 最大线程数 响应大小(字节)
```

#### 运行客户端测试

```bash
# Netty4 RPC测试
cd nfs-rpc-benchmark/src/nfs-script/client
sh netty4rpc.sh

# 其他协议测试
sh minarpc.sh      # Mina2 RPC
sh grizzlyrpc.sh   # Grizzly RPC
sh nettyrpc.sh     # Netty3 RPC
```

### 测试结果

性能测试会输出以下指标：
* 请求总数
* 响应时间统计
* 吞吐量
* 错误率

## 🔧 配置说明

### 服务器配置

```java
ServerConfig config = new ServerConfig();
config.setHost("127.0.0.1");           // 监听地址
config.setPort(8888);                  // 监听端口
config.setBusinessThreadPool(threadPool); // 业务线程池
config.setMaxThreads(100);             // 最大线程数
config.setConnectTimeout(3000);        // 连接超时时间
```

### 客户端配置

```java
// 连接池配置
ClientFactory clientFactory = new NettyClientFactory();
clientFactory.enableSendLimit();        // 启用发送限制
ClientFactory.sendLimitPercent = 50;    // 发送限制百分比

// 客户端参数
Client client = clientFactory.get(
    "127.0.0.1",    // 服务器IP
    8888,           // 服务器端口
    1000,           // 连接超时时间
    5               // 连接池大小
);
```

## 🧩 扩展开发

### 自定义协议

实现 `Protocol` 接口：

```java
public class CustomProtocol implements Protocol {
    public static final int TYPE = 100;
    
    @Override
    public int getType() {
        return TYPE;
    }
    
    // 实现其他方法...
}
```

### 自定义序列化

实现 `Codec` 接口：

```java
public class CustomCodec implements Codec {
    public static final int TYPE = 100;
    
    @Override
    public int getType() {
        return TYPE;
    }
    
    @Override
    public byte[] encode(Object obj) throws Exception {
        // 实现编码逻辑
    }
    
    @Override
    public Object decode(String className, byte[] bytes) throws Exception {
        // 实现解码逻辑
    }
}
```

## 📝 协议类型

| 协议类型 | 值 | 说明 |
|---------|-----|------|
| `RPCProtocol.TYPE` | 1 | 反射RPC协议 |
| `SimpleProcessorProtocol.TYPE` | 2 | 简单处理器协议 |

## 🔌 序列化类型

| 序列化类型 | 值 | 说明 |
|-----------|-----|------|
| `Codecs.JAVA_CODEC` | 1 | Java原生序列化 |
| `Codecs.HESSIAN_CODEC` | 2 | Hessian序列化 |
| `Codecs.KRYO_CODEC` | 3 | Kryo序列化 |
| `Codecs.PB_CODEC` | 4 | Protobuf序列化 |

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 📄 许可证

本项目基于 [Apache License 2.0](LICENSE) 开源协议。

## 👥 作者

* **bluedavy** - *初始作者* - [bluedavy@gmail.com](mailto:bluedavy@gmail.com)

## 🙏 致谢

感谢所有为NFS-RPC项目做出贡献的开发者。

## 📞 联系方式

* 项目主页: [http://code.google.com/p/nfs-rpc](http://code.google.com/p/nfs-rpc)
* 问题反馈: 请在GitHub Issues中提交问题
* 邮箱: bluedavy@gmail.com

---

**注意**: 本项目已迁移到GitHub，原Google Code项目页面仅供参考。
