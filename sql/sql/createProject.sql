-- 用户信息表
DROP TABLE IF EXISTS `sys_users`;
CREATE TABLE `sys_users`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                              `username` varchar(50) NOT NULL COMMENT '账号',
                              `password` varchar(255) NOT NULL COMMENT '密码',
                              `user_type` varchar(2) NOT NULL DEFAULT '00' COMMENT '用户类型(00-系统用户)',
                              `name` varchar(50) NOT NULL COMMENT '昵称',
                              `phone` varchar(18) NULL DEFAULT NULL COMMENT '手机号',
                              `sex` char(2) NOT NULL DEFAULT '0' COMMENT '0-女 1-男 2-未知',
                              `email` varchar(100) NULL DEFAULT NULL COMMENT '邮箱',
                              `avatar` varchar(255) NULL DEFAULT NULL COMMENT '头像地址',
                              `enabled` char(2) NOT NULL DEFAULT '1' COMMENT '0-禁用 1-启用',
                              `login_ip` varchar(128) NULL DEFAULT NULL COMMENT '最后登录IP',
                              `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
                              `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `create_by` varchar(50) NULL DEFAULT NULL COMMENT '创建人',
                              `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `update_by` varchar(50) NULL DEFAULT NULL COMMENT '更新人',
                              `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标志，0-未删除，1-已删除',
                              PRIMARY KEY (`id`),
                              UNIQUE INDEX `uk_username`(`username`) COMMENT '账号唯一索引',
                              INDEX `idx_users_enabled`(`enabled`) COMMENT '用户状态索引',
                              INDEX `idx_users_phone`(`phone`) COMMENT '手机号索引',
                              INDEX `idx_users_email`(`email`) COMMENT '邮箱索引',
                              INDEX `idx_users_del_flag`(`del_flag`) COMMENT '逻辑删除索引',
                              INDEX `idx_users_login_time`(`login_time` DESC) COMMENT '最近登录时间索引'
) ENGINE = InnoDB COMMENT = '用户信息';

-- 插入用户信息 账号 admin 密码 1111111
INSERT INTO `sys_users` VALUES (1, 'admin', '$2a$10$aJDVk5Kr9JFW/JG5mlZD5erCc8NGQ//WsaUKEyb7VeLdhoHsE/7Ki', '00', '超级管理员', '', '1', 'admin@qq.com', NULL, '1', '127.0.0.1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'system', 0);

-- 角色信息表
DROP TABLE IF EXISTS `sys_roles`;
CREATE TABLE `sys_roles`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                              `name` varchar(50) NOT NULL COMMENT '角色名称',
                              `description` varchar(255) NULL DEFAULT NULL COMMENT '角色描述',
                              `sort` int NOT NULL COMMENT '显示顺序',
                              `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `create_by` varchar(50) NULL DEFAULT NULL COMMENT '创建人',
                              `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `update_by` varchar(50) NULL DEFAULT NULL COMMENT '更新人',
                              PRIMARY KEY (`id`),
                              UNIQUE INDEX `uk_role_name`(`name`) COMMENT '角色名称唯一索引',
                              INDEX `idx_roles_sort`(`sort`) COMMENT '显示顺序索引'
) ENGINE = InnoDB COMMENT = '角色信息';

-- 插入角色信息
INSERT INTO `sys_roles` VALUES (1, 'admin', '超级管理员', 1, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');
INSERT INTO `sys_roles` VALUES (2, 'user', '普通用户', 2, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_roles`;
CREATE TABLE `sys_user_roles`  (
                                   `user_id` bigint NOT NULL COMMENT '用户ID',
                                   `role_id` bigint NOT NULL COMMENT '角色ID',
                                   PRIMARY KEY (`user_id`, `role_id`),
                                   UNIQUE INDEX `uk_user_role`(`user_id`, `role_id`) COMMENT '用户角色唯一索引',
                                   INDEX `idx_user_roles_user_id`(`user_id`) COMMENT '用户ID索引',
                                   INDEX `idx_user_roles_role_id`(`role_id`) COMMENT '角色ID索引'
) ENGINE = InnoDB COMMENT = '用户角色';

-- 插入用户角色信息
INSERT INTO `sys_user_roles` VALUES (1, 1);
INSERT INTO `sys_user_roles` VALUES (1, 2);

-- 菜单信息
DROP TABLE IF EXISTS `sys_menus`;
CREATE TABLE `sys_menus`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
                              `title` varchar(50) NOT NULL DEFAULT '' COMMENT '菜单名称',
                              `name` varchar(50) NULL DEFAULT NULL COMMENT '路由名称',
                              `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
                              `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
                              `path` varchar(200) NULL DEFAULT '' COMMENT '路由地址',
                              `component` varchar(255) NULL DEFAULT NULL COMMENT '组件路径',
                              `query` varchar(255) NULL DEFAULT NULL COMMENT '路由参数',
                              `is_cache` int NULL DEFAULT 1 COMMENT '是否缓存（0-不缓存 1-缓存）',
                              `menu_type` char(2) NULL DEFAULT '' COMMENT '菜单类型（M-目录 C-菜单 F-按钮 N-内联 W-外链）',
                              `visible` int NULL DEFAULT 1 COMMENT '菜单状态（0-隐藏 1-显示）',
                              `roles` varchar(100) NULL DEFAULT NULL COMMENT '权限标识',
                              `icon` varchar(100) NULL DEFAULT '#' COMMENT '菜单图标',
                              `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `create_by` varchar(64) NULL DEFAULT '' COMMENT '创建人',
                              `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `update_by` varchar(64) NULL DEFAULT '' COMMENT '更新人',
                              `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
                              PRIMARY KEY (`id`),
                              UNIQUE INDEX `uk_menu_name_title`(`name`, `title`, `menu_type`, `path`, `roles`) COMMENT '菜单唯一索引',
                              INDEX `idx_menus_parent_id`(`parent_id`) COMMENT '父菜单ID索引',
                              INDEX `idx_menus_menu_type`(`menu_type`) COMMENT '菜单类型索引',
                              INDEX `idx_menus_visible`(`visible`) COMMENT '菜单可见性索引',
                              INDEX `idx_menus_sort`(`sort`) COMMENT '显示顺序索引',
                              INDEX `idx_menus_parent_sort`(`parent_id`, `sort`) COMMENT '父ID+顺序复合索引'
) ENGINE = InnoDB COMMENT = '菜单信息';


-- 插入菜单信息
INSERT INTO `sys_menus` VALUES (1, '系统管理', 'system', 0, 1, '/system', '', '', 1, 'M', 1, '', 'system', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (2, '用户管理', 'users', 1, 1, '/system/users', '/system/users/index', '', 0, 'C', 1, '', 'user', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (3, '菜单管理', 'menus', 1, 2, '/system/menus', '/system/menus/index', '', 0, 'C', 1, '', 'tree-table', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (4, '角色管理', 'roles', 1, 3, '/system/roles', '/system/roles/index', '', 0, 'C', 1, '', 'peoples', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (5, '日志管理', 'logs', 1, 4, '/system/logs', '', '', 0, 'M', 1, '', 'log', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (6, '操作日志', 'operlog', 5, 1, '/system/logs/operlog', '/system/logs/operlog/index', '', 0, 'C', 1, '', 'form', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (7, '登录日志', 'loginlog', 5, 2, '/system/logs/loginlog', '/system/logs/loginlog/index', '', 0, 'C', 1, '', 'logininfor',CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (8, '用户查询', '', 2, 1, '', '', '', 0, 'F', 1, 'system::users::query', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (9, '用户新增', '', 2, 2, '', '', '', 0, 'F', 1, 'system::users::insert', '', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (10, '用户修改', '', 2, 3, '', '', '', 0, 'F', 1, 'system::users::update', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (11, '用户删除', '', 2, 4, '', '', '', 0, 'F', 1, 'system::users::delete', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (12, '分配角色', '', 2, 5, '', '', '', 0, 'F', 1, 'system::users::auth', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (13, '菜单查询', '', 3, 1, '', '', '', 0, 'F', 1, 'system::menus::query', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (14, '菜单新增', '', 3, 2, '', '', '', 0, 'F', 1, 'system::menus::insert', '', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (15, '菜单修改', '', 3, 3, '', '', '', 0, 'F', 1, 'system::menus::update', '', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (16, '菜单删除', '', 3, 4, '', '', '', 0, 'F', 1, 'system::menus::delete', '',CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (17, '角色查询', '', 4, 1, '', '', '', 0, 'F', 1, 'system::roles::query', '', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (18, '角色新增', '', 4, 2, '', '', '', 0, 'F', 1, 'system::roles::insert', '',CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (19, '角色修改', '', 4, 3, '', '', '', 0, 'F', 1, 'system::roles::update', '',CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (20, '角色删除', '', 4, 4, '', '', '', 0, 'F', 1, 'system::roles::delete', '',CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (21, '分配用户', '', 4, 5, '', '', '', 0, 'F', 1, 'system::roles::auth', '',CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (22, '操作查询', '', 6, 1, '', '', '', 0, 'F', 1, 'system::logs::operlog::query', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (23, '查询所有', '', 6, 2, '', '', '', 0, 'F', 1, 'system::logs::operlog::all', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (24, '登录查询', '', 7, 1, '', '', '', 0, 'F', 1, 'system::logs::loginlog::query', '',CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (25, '查询所有', '', 7, 2, '', '', '', 0, 'F', 1, 'system::logs::loginlog::all', '',CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (29, '系统监控', 'monitor', 0, 3, '/monitor', '', '', 0, 'M', 1, '', 'monitor', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (30, '在线用户', 'online', 29, 1, '/monitor/online', '/monitor/online/index', '', 0, 'C', 1, '', 'online', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (31, '服务监控', 'server', 29, 2, '/monitor/server', '/monitor/server/index', '', 0, 'C', 1, '', 'server', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (32, '缓存列表', 'cache', 29, 3, '/monitor/cacheList', '/monitor/cacheList/index', '', 0, 'C', 1, '', 'redis-list', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (33, '查询在线用户', '', 30, 1, '', '', '', 0, 'F', 1, 'monitor::online::query', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (34, '强制下线用户', '', 30, 2, '', '', '', 0, 'F', 1, 'monitor::online::kickout', '',CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (35, '服务查询', '', 31, 1, '', '', '', 0, 'F', 1, 'monitor::server::query', '', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (36, '缓存查询', '', 32, 1, '', '', '', 0, 'F', 1, 'monitor::cacheList::query', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (37, '缓存删除', '', 32, 2, '', '', '', 0, 'F', 1, 'monitor::cacheList::delete', '',CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (38, '系统工具', 'tools', 0, 2, '/tools', '', '', 0, 'M', 1, '', 'tool',CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (39, '代码生成', 'gen', 38, 1, '/tools/gen', '/tools/gen/index', '', 0, 'C', 1, '', 'code', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (40, '系统接口', 'swagger', 38, 2, '/tools/swagger', '/tools/swagger/index', '', 0, 'C', 1, '', 'swagger', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (41, '查询预览下载', '', 39, 1, '', '', '', 0, 'F', 1, 'tools::gen::query', '', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (42, '查询接口', '', 40, 1, '', '', '', 0, 'F', 1, 'tools::swagger::query', '', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (43, '文件管理', 'files', 1, 4, '/system/files', '/system/files', '', 0, 'C', 1, '', 'upload', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (44, '文件查询', '', 43, 1, '', '', '', 0, 'F', 1, 'system::files::query', '', CURRENT_TIMESTAMP, 'system',CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus`VALUES (45, '文件新增', '', 43, 2, '', '', '', 0, 'F', 1, 'system::files::insert', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (46, '文件删除', '', 43, 3, '', '', '', 0, 'F', 1, 'system::files::delete', '', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
-- 插入APP菜单
INSERT INTO `sys_menus` VALUES (47, 'APP管理', 'app', 0, 5, '/app', '', '', 0, 'M', 1, '', 'phone', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (48, 'APP菜单', 'appmenus', 47, 1, '/app/menus', '/app/menus/index', '', 0, 'C', 1, '', 'tree-table', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (49, 'APP角色', 'approles', 47, 2, '/app/roles', '/app/roles/index', '', 0, 'C', 1, '', 'peoples', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');


-- 角色菜单
DROP TABLE IF EXISTS `sys_roles_menus`;
CREATE TABLE `sys_roles_menus`  (
                                    `role_id` bigint NOT NULL COMMENT '角色ID',
                                    `menu_id` bigint NOT NULL COMMENT '菜单ID',
                                    PRIMARY KEY (`role_id`, `menu_id`),
                                    UNIQUE INDEX `uk_role_menu`(`role_id`, `menu_id`) COMMENT '角色菜单唯一索引',
                                    INDEX `idx_roles_menus_role_id`(`role_id`) COMMENT '角色ID索引',
                                    INDEX `idx_roles_menus_menu_id`(`menu_id`) COMMENT '菜单ID索引'
) ENGINE = InnoDB COMMENT = '角色菜单';

-- 插入角色菜单信息
INSERT INTO `sys_roles_menus` VALUES (1, 1);
INSERT INTO `sys_roles_menus` VALUES (1, 2);
INSERT INTO `sys_roles_menus` VALUES (1, 3);
INSERT INTO `sys_roles_menus` VALUES (1, 4);
INSERT INTO `sys_roles_menus` VALUES (1, 5);
INSERT INTO `sys_roles_menus` VALUES (1, 6);
INSERT INTO `sys_roles_menus` VALUES (1, 7);
INSERT INTO `sys_roles_menus` VALUES (1, 8);
INSERT INTO `sys_roles_menus` VALUES (1, 9);
INSERT INTO `sys_roles_menus` VALUES (1, 10);
INSERT INTO `sys_roles_menus` VALUES (1, 11);
INSERT INTO `sys_roles_menus` VALUES (1, 12);
INSERT INTO `sys_roles_menus` VALUES (1, 13);
INSERT INTO `sys_roles_menus` VALUES (1, 14);
INSERT INTO `sys_roles_menus` VALUES (1, 15);
INSERT INTO `sys_roles_menus` VALUES (1, 16);
INSERT INTO `sys_roles_menus` VALUES (1, 17);
INSERT INTO `sys_roles_menus` VALUES (1, 18);
INSERT INTO `sys_roles_menus` VALUES (1, 19);
INSERT INTO `sys_roles_menus` VALUES (1, 20);
INSERT INTO `sys_roles_menus` VALUES (1, 21);
INSERT INTO `sys_roles_menus` VALUES (1, 22);
INSERT INTO `sys_roles_menus` VALUES (1, 23);
INSERT INTO `sys_roles_menus` VALUES (1, 24);
INSERT INTO `sys_roles_menus` VALUES (1, 25);
INSERT INTO `sys_roles_menus` VALUES (1, 29);
INSERT INTO `sys_roles_menus` VALUES (1, 30);
INSERT INTO `sys_roles_menus` VALUES (1, 31);
INSERT INTO `sys_roles_menus` VALUES (1, 32);
INSERT INTO `sys_roles_menus` VALUES (1, 33);
INSERT INTO `sys_roles_menus` VALUES (1, 34);
INSERT INTO `sys_roles_menus` VALUES (1, 35);
INSERT INTO `sys_roles_menus` VALUES (1, 36);
INSERT INTO `sys_roles_menus` VALUES (1, 37);
INSERT INTO `sys_roles_menus` VALUES (1, 38);
INSERT INTO `sys_roles_menus` VALUES (1, 39);
INSERT INTO `sys_roles_menus` VALUES (1, 40);
INSERT INTO `sys_roles_menus` VALUES (1, 41);
INSERT INTO `sys_roles_menus` VALUES (1, 42);
INSERT INTO `sys_roles_menus` VALUES (1, 43);
INSERT INTO `sys_roles_menus` VALUES (1, 44);
INSERT INTO `sys_roles_menus` VALUES (1, 45);
INSERT INTO `sys_roles_menus` VALUES (1, 46);
INSERT INTO `sys_roles_menus` VALUES (2, 1);
INSERT INTO `sys_roles_menus` VALUES (2, 2);
INSERT INTO `sys_roles_menus` VALUES (2, 3);
INSERT INTO `sys_roles_menus` VALUES (2, 4);
INSERT INTO `sys_roles_menus` VALUES (2, 5);
INSERT INTO `sys_roles_menus` VALUES (2, 6);
INSERT INTO `sys_roles_menus` VALUES (2, 7);
INSERT INTO `sys_roles_menus` VALUES (2, 8);
INSERT INTO `sys_roles_menus` VALUES (2, 13);
INSERT INTO `sys_roles_menus` VALUES (2, 17);
INSERT INTO `sys_roles_menus` VALUES (2, 22);
INSERT INTO `sys_roles_menus` VALUES (2, 24);
INSERT INTO `sys_roles_menus` VALUES (2, 29);
INSERT INTO `sys_roles_menus` VALUES (2, 30);
INSERT INTO `sys_roles_menus` VALUES (2, 31);
INSERT INTO `sys_roles_menus` VALUES (2, 32);
INSERT INTO `sys_roles_menus` VALUES (2, 33);
INSERT INTO `sys_roles_menus` VALUES (2, 35);
INSERT INTO `sys_roles_menus` VALUES (2, 36);
-- 插入APP菜单角色
INSERT INTO `sys_roles_menus` VALUES (1, 47);
INSERT INTO `sys_roles_menus` VALUES (1, 48);
INSERT INTO `sys_roles_menus` VALUES (1, 49);

-- 登录日志
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '登录账号',
                                  `ip` varchar(50) NOT NULL DEFAULT '' COMMENT '登录IP地址',
                                  `ip_addr` varchar(255) NOT NULL DEFAULT '' COMMENT '登录地点',
                                  `browser` varchar(50) NOT NULL DEFAULT '' COMMENT '浏览器类型',
                                  `os` varchar(50) NOT NULL DEFAULT '' COMMENT '操作系统',
                                  `status` char(2) NOT NULL DEFAULT '0' COMMENT '登录状态（0-失败 1-成功）',
                                  `msg` varchar(255) NOT NULL DEFAULT '' COMMENT '提示消息',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
                                  PRIMARY KEY (`id`),
                                  INDEX `idx_login_log_status`(`status`) COMMENT '登录状态索引',
                                  INDEX `idx_login_log_username`(`username`) COMMENT '账号索引',
                                  INDEX `idx_login_log_create_time`(`create_time` DESC) COMMENT '访问时间降序索引',
                                  INDEX `idx_login_log_user_time`(`username`, `create_time` DESC) COMMENT '账号+时间复合索引'
) ENGINE = InnoDB COMMENT = '登录日志';

-- 操作日志
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
                                 `title` varchar(50) NULL DEFAULT '' COMMENT '模块标题',
                                 `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0-其它 1-新增 2-修改 3-删除）',
                                 `method` varchar(200) NULL DEFAULT '' COMMENT '方法名称',
                                 `request_method` varchar(10) NULL DEFAULT '' COMMENT '请求方式',
                                 `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0-其它 1-后台用户 2-手机端用户）',
                                 `username` varchar(50) NOT NULL COMMENT '用户名称',
                                 `userid` bigint NOT NULL COMMENT '用户ID',
                                 `oper_url` varchar(255) NULL DEFAULT '' COMMENT '请求URL',
                                 `oper_ip` varchar(128) NULL DEFAULT '' COMMENT '主机地址',
                                 `oper_location` varchar(255) NULL DEFAULT '' COMMENT '操作地点',
                                 `oper_param` varchar(2000) NULL DEFAULT '' COMMENT '请求参数',
                                 `json_result` varchar(2000) NULL DEFAULT '' COMMENT '返回参数',
                                 `status` int NULL DEFAULT 0 COMMENT '操作状态（0-异常 1-正常）',
                                 `error_msg` varchar(2000) NULL DEFAULT '' COMMENT '错误消息',
                                 `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
                                 `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
                                 PRIMARY KEY (`id`),
                                 INDEX `idx_oper_log_business_type`(`business_type`) COMMENT '业务类型索引',
                                 INDEX `idx_oper_log_status`(`status`) COMMENT '操作状态索引',
                                 INDEX `idx_oper_log_oper_time`(`oper_time` DESC) COMMENT '操作时间降序索引',
                                 INDEX `idx_oper_log_username_time`(`username`, `oper_time` DESC) COMMENT '用户+时间复合索引'
) ENGINE = InnoDB COMMENT = '操作日志';

-- 文件信息
DROP TABLE IF EXISTS `sys_files`;
CREATE TABLE `sys_files`  (
                              `file_id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
                              `original_name` varchar(255) NOT NULL COMMENT '原始文件名',
                              `file_name` varchar(255) NOT NULL COMMENT '当前文件名',
                              `storage_path` varchar(512) NOT NULL COMMENT '存储路径',
                              `file_hash` char(64) NULL COMMENT '哈希值',
                              `file_type` varchar(100) NOT NULL COMMENT '文件类型',
                              `file_business_type` varchar(50) NULL DEFAULT NULL COMMENT '业务分类',
                              `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
                              `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标志，0-未删除，1-已删除',
                              `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `create_by` varchar(64) NULL DEFAULT '' COMMENT '创建人',
                              `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `update_by` varchar(64) NULL DEFAULT '' COMMENT '更新人',
                              `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
                              PRIMARY KEY (`file_id`),
                              UNIQUE INDEX `uk_file_storage_path`(`storage_path`) COMMENT '存储路径唯一索引',
                              UNIQUE INDEX `uk_file_hash`(`file_hash`) COMMENT '哈希值唯一索引',
                              INDEX `idx_file_business_type`(`file_business_type`) COMMENT '业务分类索引',
                              INDEX `idx_file_original_name`(`original_name`) COMMENT '原始文件名索引',
                              INDEX `idx_file_file_type`(`file_type`) COMMENT '文件类型索引',
                              INDEX `idx_file_del_flag`(`del_flag`) COMMENT '逻辑删除索引',
                              INDEX `idx_file_create_time`(`create_time` DESC) COMMENT '创建时间降序索引'
) ENGINE = InnoDB COMMENT = '文件信息';

-- 业务文件
DROP TABLE IF EXISTS `sys_business_files`;
CREATE TABLE `sys_business_files`  (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                       `business_type` varchar(50) NOT NULL COMMENT '业务分类',
                                       `business_id` bigint NOT NULL COMMENT '业务表ID',
                                       `file_id` bigint NOT NULL COMMENT '文件表ID',
                                       `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       PRIMARY KEY (`id`),
                                       UNIQUE INDEX `uk_business_file`(`business_type`, `business_id`, `file_id`) COMMENT '业务文件唯一索引',
                                       INDEX `idx_business_type_id`(`business_type`, `business_id`) COMMENT '业务分类+ID复合索引',
                                       INDEX `idx_business_file_id`(`file_id`) COMMENT '文件ID索引'
) ENGINE = InnoDB COMMENT = '业务文件';

-- 跨平台用户
DROP TABLE IF EXISTS `sys_open_users`;
CREATE TABLE `sys_open_users`  (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '绑定ID',
                                   `sys_user_id` bigint NOT NULL COMMENT '系统账户ID',
                                   `user_open_id` varchar(100) NOT NULL COMMENT '跨平台用户标识',
                                   `sys_user_type` int NOT NULL COMMENT '平台标识（0-系统账户 1-微信小程序 2-抖音小程序）',
                                   `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `create_by` varchar(50) NULL DEFAULT NULL COMMENT '创建人',
                                   `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `update_by` varchar(50) NULL DEFAULT NULL COMMENT '更新人',
                                   `del_flag` int NULL DEFAULT 0 COMMENT '逻辑删除标志，0-未删除，1-已删除',
                                   PRIMARY KEY (`id`),
                                   UNIQUE INDEX `uk_open_user`(`sys_user_id`, `sys_user_type`) COMMENT '系统用户+平台唯一索引',
                                   UNIQUE INDEX `uk_open_user_open_id`(`user_open_id`, `sys_user_type`) COMMENT '跨平台标识+平台唯一索引',
                                   INDEX `idx_open_user_type`(`sys_user_type`) COMMENT '平台类型索引'
) ENGINE = InnoDB COMMENT = '跨平台用户';
