-- 插入APP菜单
INSERT INTO `sys_menus` VALUES (47, 'APP管理', 'app', 0, 5, '/app', '', '', 0, 'M', 1, '', 'phone', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (48, 'APP菜单', 'appmenus', 47, 1, '/app/menus', '/app/menus/index', '', 0, 'C', 1, '', 'tree-table', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
INSERT INTO `sys_menus` VALUES (49, 'APP角色', 'approles', 47, 2, '/app/roles', '/app/roles/index', '', 0, 'C', 1, '', 'peoples', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');
-- 插入APP菜单角色
INSERT INTO `sys_roles_menus` VALUES (1, 47);
INSERT INTO `sys_roles_menus` VALUES (1, 48);
INSERT INTO `sys_roles_menus` VALUES (1, 49);
