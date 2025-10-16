package com.yl.paike.teacher.util;

public class Constants {
    
    public static final int AGE_GROUP_PRE_SCHOOL = 1;
    public static final int AGE_GROUP_SMALL = 2;
    public static final int AGE_GROUP_MIDDLE = 3;
    public static final int AGE_GROUP_BIG = 4;
    
    public static final int SCHEDULE_STATUS_NORMAL = 1;
    public static final int SCHEDULE_STATUS_CANCELLED = 2;
    public static final int SCHEDULE_STATUS_ADJUSTED = 3;
    
    public static final int CONFLICT_TYPE_TEACHER = 1;
    public static final int CONFLICT_TYPE_STUDENT = 2;
    public static final int CONFLICT_TYPE_CLASSROOM = 3;
    
    public static final int WARNING_STATUS_UNRESOLVED = 1;
    public static final int WARNING_STATUS_RESOLVED = 2;
    public static final int WARNING_STATUS_IGNORED = 3;
    
    public static final String[] WEEK_DAYS = {
        "周一", "周二", "周三", "周四", "周五", "周六", "周日"
    };
    
    public static final String[] AGE_GROUP_NAMES = {
        "", "学前班(1-3岁)", "小班(3-6岁)", "中班(6-9岁)", "大班(9-12岁)"
    };
    
    public static final String CACHE_TEACHER_PREFIX = "teacher:";
    public static final String CACHE_COURSE_PREFIX = "course:";
    public static final String CACHE_SCHEDULE_PREFIX = "schedule:";
}
