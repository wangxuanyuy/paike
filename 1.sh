mkdir -p student-course-backend/src/main/java/com/school/course/{config,controller,entity,repository,service,dto,exception} &&
mkdir -p student-course-backend/src/main/resources &&
touch student-course-backend/pom.xml &&
touch student-course-backend/src/main/java/com/school/course/StudentCourseApplication.java &&
touch student-course-backend/src/main/java/com/school/course/config/{CorsConfig.java,JpaConfig.java,RedisConfig.java} &&
touch student-course-backend/src/main/java/com/school/course/controller/{StudentController.java,CourseController.java,EnrollmentController.java,TimeSlotController.java} &&
touch student-course-backend/src/main/java/com/school/course/entity/{Student.java,Course.java,CourseSchedule.java,TimeSlot.java,Classroom.java,StudentEnrollment.java} &&
touch student-course-backend/src/main/java/com/school/course/repository/{StudentRepository.java,CourseRepository.java,CourseScheduleRepository.java,TimeSlotRepository.java,ClassroomRepository.java,StudentEnrollmentRepository.java} &&
touch student-course-backend/src/main/java/com/school/course/service/{StudentService.java,CourseService.java,EnrollmentService.java} &&
touch student-course-backend/src/main/java/com/school/course/dto/{StudentDTO.java,CourseScheduleViewDTO.java,EnrollmentDTO.java,ApiResponse.java} &&
touch student-course-backend/src/main/java/com/school/course/exception/{GlobalExceptionHandler.java,BusinessException.java,EntityNotFoundException.java} &&
touch student-course-backend/src/main/resources/{application.yml,application-dev.yml,data.sql}
