-- 更新菜单，添加通知记录管理
-- 注意：此SQL需要在后台管理系统的菜单管理页面执行，或者直接更新menu表的menujson字段

-- 方法1：通过后台管理界面手动添加菜单
-- 1. 登录管理员后台
-- 2. 进入"菜单管理"页面
-- 3. 在"系统管理"菜单下添加子菜单：
--    - 菜单名称：通知记录
--    - 路由路径：tongzhijilu
--    - 权限：查看、重试

-- 方法2：直接更新数据库（需要先查询现有菜单配置，然后修改）
-- 由于menujson是JSON格式，建议使用后台管理界面添加

-- 如果要在就诊通知管理后添加通知记录管理，可以使用以下方式：
-- 先备份原数据
-- CREATE TABLE menu_backup AS SELECT * FROM menu;

-- 注意：以下更新语句仅供参考，实际使用时请根据现有菜单结构进行调整
-- UPDATE menu SET menujson = REPLACE(menujson, 
-- '"menu":"就诊通知管理"', 
-- '"menu":"就诊通知管理"},{"child":[{"allButtons":["查看","重试"],"appFrontIcon":"cuIcon-notice","buttons":["查看","重试"],"classname":"tongzhijilu","menu":"通知记录","menuJump":"列表","tableName":"tongzhijilu"}],"fontClass":"icon-common39","menu":"通知记录管理","unicode":"&#xeeba;"}');
