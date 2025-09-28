-- data.sql - 放在 src/main/resources 目录下
-- Spring Boot 会自动执行这个脚本

-- 插入时间段数据
INSERT IGNORE INTO T_TIME_SLOTS (id, slot_name, start_time, end_time, day_of_week, is_active) VALUES
-- 周一
(1, '早班', '09:00:00', '11:00:00', 1, true),
(2, '中班', '11:00:00', '13:00:00', 1, true),
(3, '晚班', '14:00:00', '16:00:00', 1, true),
(4, '夜班', '16:00:00', '18:00:00', 1, true),
-- 周二
(5, '早班', '09:00:00', '11:00:00', 2, true),
(6, '中班', '11:00:00', '13:00:00', 2, true),
(7, '晚班', '14:00:00', '16:00:00', 2, true),
(8, '夜班', '16:00:00', '18:00:00', 2, true),
-- 周三
(9, '早班', '09:00:00', '11:00:00', 3, true),
(10, '中班', '11:00:00', '13:00:00', 3, true),
(11, '晚班', '14:00:00', '16:00:00', 3, true),
(12, '夜班', '16:00:00', '18:00:00', 3, true),
-- 周四
(13, '早班', '09:00:00', '11:00:00', 4, true),
(14, '中班', '11:00:00', '13:00:00', 4, true),
(15, '晚班', '14:00:00', '16:00:00', 4, true),
(16, '夜班', '16:00:00', '18:00:00', 4, true),
-- 周五
(17, '早班', '09:00:00', '11:00:00', 5, true),
(18, '中班', '11:00:00', '13:00:00', 5, true),
(19, '晚班', '14:00:00', '16:00:00', 5, true),
(20, '夜班', '16:00:00', '18:00:00', 5, true),
-- 周六
(21, '早班', '09:00:00', '11:00:00', 6, true),
(22, '中班', '11:00:00', '13:00:00', 6, true),
(23, '晚班', '14:00:00', '16:00:00', 6, true),
(24, '夜班', '16:00:00', '18:00:00', 6, true),
-- 周日
(25, '早班', '09:00:00', '11:00:00', 7, true),
(26, '中班', '11:00:00', '13:00:00', 7, true),
(27, '晚班', '14:00:00', '16:00:00', 7, true),
(28, '夜班', '16:00:00', '18:00:00', 7, true);

-- 插入示例教室数据
INSERT IGNORE INTO J_CLASSROOMS (id, classroom_code, classroom_name, max_capacity, location, facilities, is_active) VALUES
(1, 'CLS001', '阳光教室', 20, '一楼东侧', '投影仪,音响,空调', true),
(2, 'CLS002', '月亮教室', 15, '一楼西侧', '投影仪,空调', true),
(3, 'CLS003', '星星教室', 25, '二楼东侧', '投影仪,音响,空调,钢琴', true),
(4, 'CLS004', '彩虹教室', 18, '二楼西侧', '投影仪,空调', true),
(5, 'CLS005', '花朵教室', 22, '三楼东侧', '投影仪,音响,空调', true);

-- 插入示例课程数据
INSERT IGNORE INTO K_COURSES (id, course_code, course_name, age_group, max_students, description, teacher_names, is_active) VALUES
(1, 'COU001', '趣味数学', 2, 15, '通过游戏培养数学兴趣', '张三,李四', true),
(2, 'COU002', '英语启蒙', 2, 12, '基础英语学习', '王老师', true),
(3, 'COU003', '美术创作', 3, 18, '培养艺术创造力', '陈老师,刘老师', true),
(4, 'COU004', '音乐欣赏', 3, 20, '音乐基础知识学习', '赵老师', true),
(5, 'COU005', '体能训练', 4, 25, '体能素质提升', '孙老师,周老师', true),
(6, 'COU006', '阅读理解', 4, 16, '提升阅读能力', '吴老师', true),
(7, 'COU007', '科学探索', 1, 10, '科学启蒙教育', '郑老师', true),
(8, 'COU008', '手工制作', 2, 14, '动手能力培养', '马老师', true);

-- 插入示例课程安排（本周的课程）
INSERT IGNORE INTO T_COURSE_SCHEDULE (id, course_id, time_slot_id, classroom_id, schedule_date, status) VALUES
-- 周一课程
(1, 1, 1, 1, CURDATE(), 1),  -- 趣味数学 周一早班 阳光教室
(2, 2, 2, 2, CURDATE(), 1),  -- 英语启蒙 周一中班 月亮教室
(3, 3, 3, 3, CURDATE(), 1),  -- 美术创作 周一晚班 星星教室
(4, 7, 4, 4, CURDATE(), 1),  -- 科学探索 周一夜班 彩虹教室

-- 周二课程
(5, 4, 5, 3, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1),  -- 音乐欣赏 周二早班 星星教室
(6, 8, 6, 1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1),  -- 手工制作 周二中班 阳光教室
(7, 1, 7, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1),  -- 趣味数学 周二晚班 月亮教室
(8, 2, 8, 5, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 1),  -- 英语启蒙 周二夜班 花朵教室

-- 周三课程
(9, 5, 9, 5, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1),  -- 体能训练 周三早班 花朵教室
(10, 6, 10, 1, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1), -- 阅读理解 周三中班 阳光教室
(11, 3, 11, 3, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1), -- 美术创作 周三晚班 星星教室
(12, 4, 12, 2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 1), -- 音乐欣赏 周三夜班 月亮教室

-- 周四课程
(13, 7, 13, 4, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 1), -- 科学探索 周四早班 彩虹教室
(14, 8, 14, 3, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 1), -- 手工制作 周四中班 星星教室
(15, 5, 15, 5, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 1), -- 体能训练 周四晚班 花朵教室
(16, 6, 16, 1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 1), -- 阅读理解 周四夜班 阳光教室

-- 周五课程
(17, 1, 17, 2, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 1), -- 趣味数学 周五早班 月亮教室
(18, 2, 18, 4, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 1), -- 英语启蒙 周五中班 彩虹教室
(19, 3, 19, 3, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 1), -- 美术创作 周五晚班 星星教室
(20, 4, 20, 1, DATE_ADD(CURDATE(), INTERVAL 4 DAY), 1), -- 音乐欣赏 周五夜班 阳光教室

-- 周六课程
(21, 7, 21, 4, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1), -- 科学探索 周六早班 彩虹教室
(22, 8, 22, 2, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1), -- 手工制作 周六中班 月亮教室
(23, 5, 23, 5, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1), -- 体能训练 周六晚班 花朵教室
(24, 6, 24, 3, DATE_ADD(CURDATE(), INTERVAL 5 DAY), 1), -- 阅读理解 周六夜班 星星教室

-- 周日课程
(25, 1, 25, 1, DATE_ADD(CURDATE(), INTERVAL 6 DAY), 1), -- 趣味数学 周日早班 阳光教室
(26, 2, 26, 2, DATE_ADD(CURDATE(), INTERVAL 6 DAY), 1), -- 英语启蒙 周日中班 月亮教室
(27, 3, 27, 3, DATE_ADD(CURDATE(), INTERVAL 6 DAY), 1), -- 美术创作 周日晚班 星星教室
(28, 4, 28, 4, DATE_ADD(CURDATE(), INTERVAL 6 DAY), 1); -- 音乐欣赏 周日夜班 彩虹教室

-- 插入示例学生数据
INSERT IGNORE INTO S_STUDENTS (id, student_code, student_name, age, age_group, parent_name, parent_phone, parent_email, registration_status, is_active) VALUES
(1, 'STU001', '张小明', 4, 2, '张爸爸', '13800138001', 'zhangbaba@example.com', 2, true),
(2, 'STU002', '李小红', 7, 3, '李妈妈', '13800138002', 'limama@example.com', 2, true),
(3, 'STU003', '王小强', 10, 4, '王爸爸', '13800138003', 'wangbaba@example.com', 2, true),
(4, 'STU004', '刘小美', 3, 2, '刘妈妈', '13800138004', 'liumama@example.com', 2, true),
(5, 'STU005', '陈小华', 8, 3, '陈爸爸', '13800138005', 'chenbaba@example.com', 2, true);

-- 插入一些选课记录示例
INSERT IGNORE INTO S_STUDENT_ENROLLMENTS (id, student_id, course_schedule_id, enrollment_status, enrollment_date) VALUES
(1, 1, 1, 1, NOW()), -- 张小明选择趣味数学
(2, 1, 2, 1, NOW()), -- 张小明选择英语启蒙
(3, 2, 3, 1, NOW()), -- 李小红选择美术创作
(4, 3, 5, 1, NOW()), -- 王小强选择体能训练
(5, 4, 6, 1, NOW()); -- 刘小美选择手工制作