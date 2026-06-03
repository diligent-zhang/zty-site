# zty-site · 个人网站

基于 **Spring Boot 4.x + Vue 3 + MyBatis-Plus + DeepSeek AI** 构建的现代化个人网站，集成了博客、摄影画廊、星图留言、作品展示、AI 数字分身等功能。

## 功能特性

- **博客系统** — Markdown 文章发布与管理，支持分类、标签、关键词搜索，阅读量统计
- **摄影画廊** — 图片/视频展示，支持排序、评论互动
- **星图留言** — 基于高德地图的匿名留言功能，用户可在地图上任意位置留下寄语
- **作品展示** — 个人作品/项目陈列，支持图片和视频媒体
- **AI 数字分身** — 接入 DeepSeek 大模型，通过 SSE 流式对话与访客交流
- **后台管理** — 基于 Session 的登录鉴权，文章/照片/留言/作品的统一管理
- **文件上传** — 支持图片和视频上传，UUID 文件名防止冲突
- **3D 形象** — 首页集成 VRM 3D 模型展示

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 4.0.6 |
| ORM | MyBatis-Plus 3.5.15 |
| 数据库 | MySQL |
| Session 管理 | Spring Session JDBC |
| 安全 | BCrypt 密码加密 |
| 前端 | Vue 3 + Vite（SPA） |
| 地图 | 高德地图 JS API |
| AI | DeepSeek Chat API (SSE 流式) |

## 快速开始

### 环境要求

- **JDK 17** 或更高版本
- **MySQL** 8.0+ 并确保服务运行中
- **Maven** 3.6+（项目已内置 Maven Wrapper，无需单独安装）

### 1. 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS ztysite
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

### 2. 初始化数据表

执行以下 SQL 创建必要的数据表：

```sql
-- 用户表
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL
);

-- 文章表
CREATE TABLE articles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  content TEXT,
  summary VARCHAR(500),
  cover_image VARCHAR(500),
  category VARCHAR(50),
  view_count INT DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 标签表
CREATE TABLE tags (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- 文章-标签关联表
CREATE TABLE article_tags (
  article_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL,
  PRIMARY KEY (article_id, tag_id)
);

-- 照片表
CREATE TABLE photos (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  url VARCHAR(500) NOT NULL,
  location VARCHAR(255),
  sort_order INT DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 照片评论表
CREATE TABLE photo_comments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  photo_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  author VARCHAR(100),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 星图留言表
CREATE TABLE star_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  content TEXT NOT NULL,
  author VARCHAR(100),
  longitude DOUBLE NOT NULL,
  latitude DOUBLE NOT NULL,
  hue INT DEFAULT 0,
  approved TINYINT DEFAULT 1,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 作品/Passions 表
CREATE TABLE passions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255),
  description TEXT,
  media_url VARCHAR(500),
  media_type VARCHAR(20),
  sort_order INT DEFAULT 0
);

-- Spring Session 表（JDBC Session 持久化所需）
CREATE TABLE SPRING_SESSION (
  PRIMARY_ID CHAR(36) NOT NULL,
  SESSION_ID CHAR(36) NOT NULL,
  CREATION_TIME BIGINT NOT NULL,
  LAST_ACCESS_TIME BIGINT NOT NULL,
  MAX_INACTIVE_INTERVAL INT NOT NULL,
  EXPIRY_TIME BIGINT NOT NULL,
  PRINCIPAL_NAME VARCHAR(100),
  PRIMARY KEY (PRIMARY_ID),
  UNIQUE KEY SPRING_SESSION_IX (SESSION_ID)
);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
  SESSION_PRIMARY_ID CHAR(36) NOT NULL,
  ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
  ATTRIBUTE_BYTES BLOB NOT NULL,
  PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
  CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID)
    REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);
```

### 3. 创建管理员账户

使用 BCrypt 加密工具生成密码哈希后插入用户表，默认密码为 `admin123`：

```sql
-- 密码：admin123（BCrypt 加密）
INSERT INTO users (username, password_hash)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EHsM8lE9lBOsl7iK');
```

> 建议使用在线 BCrypt 工具生成你自己的密码哈希，或通过项目中的 `SecurityConfig` 中注入的 `PasswordEncoder` 生成。

### 4. 修改配置

编辑 `src/main/resources/application.yml`，将数据库连接信息、DeepSeek API Key 替换为你自己的：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ztysite?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: 你的数据库用户名
    password: 你的数据库密码

deepseek:
  api-key: 你的DeepSeek-API-Key
```

### 5. 启动项目

```bash
# 开发环境直接运行
./mvnw spring-boot:run

# 或打包后运行
./mvnw clean package -DskipTests
java -jar target/zty-site-0.0.1-SNAPSHOT.jar
```

启动后访问 **http://localhost:8080** 即可看到网站首页。

## 项目结构

```
zty-site/
├── src/main/java/com/zty/ztysite/
│   ├── ZtySiteApplication.java      # 应用入口
│   ├── config/                       # 配置类
│   │   ├── CorsConfig.java          # 跨域配置
│   │   ├── MyBatisPlusConfig.java   # MyBatis-Plus 分页插件
│   │   ├── SecurityConfig.java      # BCrypt PasswordEncoder
│   │   └── WebConfig.java          # 静态资源映射
│   ├── controller/                   # 控制器层
│   │   ├── ArticleController.java   # 文章 CRUD + 分页查询
│   │   ├── AuthController.java      # 登录/登出/状态
│   │   ├── ChatController.java      # DeepSeek SSE 流式对话
│   │   ├── FileUploadController.java # 文件上传
│   │   ├── HomeController.java      # 首页个人资料
│   │   ├── MessageController.java   # 星图留言
│   │   ├── PassionController.java   # 作品展示
│   │   ├── PhotoCommentController.java # 照片评论
│   │   ├── PhotoController.java     # 照片管理
│   │   └── TagController.java       # 标签
│   ├── dto/                          # 数据传输对象
│   ├── entity/                       # 数据库实体
│   ├── mapper/                       # MyBatis-Plus Mapper
│   └── service/                      # 业务逻辑层
├── src/main/resources/
│   ├── application.yml              # 应用配置
│   ├── static/                       # 前端静态资源 (Vue 构建产物)
│   │   ├── index.html
│   │   ├── assets/                  # JS / CSS / 图片 / 音频
│   │   └── models/                  # 3D VRM 模型
│   └── knowledge.json               # AI 数字分身知识库
├── pom.xml                           # Maven 依赖
├── mvnw / mvnw.cmd                   # Maven Wrapper
└── uploads/                          # 文件上传目录
```

## 后台管理

访问 `/admin` 路由进入后台管理页面，使用管理员账户登录。

| 功能 | 说明 |
|------|------|
| 文章管理 | 新建/编辑/删除 Markdown 文章 |
| 照片管理 | 上传照片、编辑信息、删除 |
| 评论管理 | 删除不当评论 |
| 留言管理 | 审核/删除星图留言 |
| 作品管理 | 管理 Passions 展示内容 |

## API 接口

### 公开接口

| Method | Path | 说明 |
|--------|------|------|
| GET | `/api/home/profile` | 个人资料 |
| GET | `/api/home/education` | 教育经历 |
| GET | `/api/home/skills` | 技能清单 |
| GET | `/api/articles` | 文章列表（分页+筛选） |
| GET | `/api/articles/{id}` | 文章详情 |
| GET | `/api/tags` | 标签列表 |
| GET | `/api/photos` | 照片列表 |
| GET | `/api/photos/{photoId}/comments` | 照片评论 |
| POST | `/api/photos/{photoId}/comments` | 发表评论 |
| GET | `/api/messages` | 星图留言（按地图范围） |
| POST | `/api/messages` | 发布留言 |
| GET | `/api/passions` | 作品列表 |
| POST | `/api/chat` | AI 对话（SSE 流式） |
| GET | `/api/auth/status` | 登录状态 |

### 管理接口（需 Session 认证）

| Method | Path | 说明 |
|--------|------|------|
| POST | `/api/auth/login` | 登录 |
| POST | `/api/auth/logout` | 登出 |
| POST | `/api/articles` | 新建文章 |
| PUT | `/api/articles/{id}` | 更新文章 |
| DELETE | `/api/articles/{id}` | 删除文章 |
| POST | `/api/admin/photos` | 新建照片 |
| PUT | `/api/admin/photos/{id}` | 更新照片 |
| DELETE | `/api/admin/photos/{id}` | 删除照片 |
| DELETE | `/api/admin/comments/{id}` | 删除评论 |
| POST | `/api/admin/passions` | 新建作品 |
| PUT | `/api/admin/passions/{id}` | 更新作品 |
| DELETE | `/api/admin/passions/{id}` | 删除作品 |
| GET | `/api/admin/messages` | 全部留言 |
| PATCH | `/api/admin/messages/{id}/approve` | 审核留言 |
| DELETE | `/api/admin/messages/{id}` | 删除留言 |
| POST | `/api/admin/upload` | 上传文件 |

## 部署指南

### JAR 包部署

```bash
# 1. 打包
./mvnw clean package -DskipTests

# 2. 上传至服务器
scp target/zty-site-0.0.1-SNAPSHOT.jar user@your-server:/opt/zty-site/

# 3. 启动（建议配合 systemd 或 supervisor 管理进程）
java -jar /opt/zty-site/zty-site-0.0.1-SNAPSHOT.jar
```

### 使用 systemd 管理（推荐）

创建服务文件 `/etc/systemd/system/zty-site.service`：

```ini
[Unit]
Description=zty-site Personal Website
After=network.target

[Service]
Type=simple
User=www
WorkingDirectory=/opt/zty-site
ExecStart=/usr/bin/java -jar /opt/zty-site/zty-site-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable zty-site
sudo systemctl start zty-site
```

### Nginx 反向代理（可选）

```nginx
server {
    listen 80;
    server_name your-domain.com;

    client_max_body_size 200M;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_buffering off;  # SSE 流式响应需要关闭缓冲
    }

    location /uploads/ {
        alias /opt/zty-site/uploads/;
    }
}
```

> `proxy_buffering off` 对于 AI 聊天的 SSE 流式响应是必须的，否则客户端会等待缓冲区满才收到数据。

## 前端开发

前端 Vue 3 源码使用 Vite 构建，构建产物输出到 `src/main/resources/static/`。前端源码未包含在本仓库中。

如需开发前端：

```bash
# 在前端项目中
npm install
npm run dev     # 开发模式，默认代理到 localhost:8080

# 构建并部署到 Spring Boot
npm run build
cp -r dist/* ../zty-site/src/main/resources/static/
```

## 注意事项

- **API Key 安全**：`application.yml` 中包含了 DeepSeek API Key 和高德地图 JS API Key，部署到生产环境前请替换为你自己的密钥，并考虑使用环境变量或外部配置管理敏感信息
- **数据库初始化**：Spring Session JDBC 的表需要手动创建（配置为 `initialize-schema: never`）
- **文件上传路径**：上传的文件默认存储在 JAR 运行目录下的 `uploads/` 文件夹，确保该目录有写入权限
- **CORS**：默认允许所有 `localhost` 来源的跨域请求，生产环境请根据需要调整 `CorsConfig.java`

## License

MIT
