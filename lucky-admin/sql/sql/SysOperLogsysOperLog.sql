INSERT INTO `menus`
    (`id`, `name`, `title`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `roles`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`)
VALUES
    (NULL, NULL, '操作日志查询', 0, 1, '', NULL, NULL, 0, 1, 'F', 1, 'system::sysOperLog::list', '#', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');


INSERT INTO `menus`
(`id`, `name`, `title`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `roles`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`)
VALUES
    (NULL, NULL, '操作日志新增', 0, 1, '', NULL, NULL, 0, 1, 'F', 1, 'system::sysOperLog::insert', '#', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');

INSERT INTO `menus`
(`id`, `name`, `title`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `roles`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`)
VALUES
    (NULL, NULL, '操作日志修改', 0, 1, '', NULL, NULL, 0, 1, 'F', 1, 'system::sysOperLog::update', '#', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');


INSERT INTO `menus`
(`id`, `name`, `title`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `roles`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`)
VALUES
    (NULL, NULL, '操作日志删除', 0, 1, '', NULL, NULL, 0, 1, 'F', 1, 'system::sysOperLog::delete', '#', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');


INSERT INTO `menus`
(`id`, `name`, `title`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `roles`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`)
VALUES
    (NULL, NULL, '操作日志导入', 0, 1, '', NULL, NULL, 0, 1, 'F', 1, 'system::sysOperLog::import', '#', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');

INSERT INTO `menus`
(`id`, `name`, `title`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `roles`, `icon`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`)
VALUES
    (NULL, NULL, '操作日志导出', 0, 1, '', NULL, NULL, 0, 1, 'F', 1, 'system::sysOperLog::export', '#', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system', '');

