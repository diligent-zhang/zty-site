-- 星空地图留言表
CREATE TABLE IF NOT EXISTS star_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(100) NOT NULL COMMENT '昵称',
    message TEXT NOT NULL COMMENT '留言内容',
    longitude DECIMAL(10, 7) NOT NULL COMMENT '经度',
    latitude DECIMAL(10, 7) NOT NULL COMMENT '纬度',
    color_hue INT DEFAULT 200 COMMENT '色相值 0-360',
    is_anonymous TINYINT DEFAULT 1 COMMENT '0=实名, 1=匿名',
    is_approved TINYINT DEFAULT 1 COMMENT '0=未审核, 1=已审核',
    ip_address VARCHAR(50) COMMENT '留言者IP',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='星空地图留言';

-- 索引：按经纬度范围查询
CREATE INDEX idx_star_bounds ON star_messages(is_approved, longitude, latitude);
CREATE INDEX idx_star_created ON star_messages(created_at DESC);
