import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class ScheduleManager {
    private val courses = mutableListOf<Course>()
    private val schedules = mutableListOf<ClassSchedule>()
    private val tests = mutableListOf<Test>()
    private var nextTestId = 1

    // --- Course Methods ---
    fun addCourse(course: Course) {
        courses.add(course)
    }

    fun getCourseByCode(code: String): Course? {
        return courses.find { it.code == code }
    }

    // --- Schedule Methods ---
    fun addSchedule(schedule: ClassSchedule) {
        schedules.add(schedule)
    }

    fun getSchedulesByDay(day: DayOfWeek): List<ClassSchedule> {
        return schedules.filter { it.day == day }.sortedBy { it.start }
    }

    // --- Test Methods ---
    fun addTest(test: Test) {
        tests.add(test.copy(id = nextTestId++))
    }

    fun getTestsByCourse(courseCode: String): List<Test> {
        return tests.filter { it.courseCode == courseCode }
    }

    fun getUpcomingTests(days: Int = 7): List<Test> {
        val today = LocalDate.now()
        return tests.filter { it.date.isBefore(today.plusDays(days + 1L)) }
            .sortedBy { it.date }
    }

    fun getAllCourses(): List<Course> {
        return courses.toList()
    }

    fun getSchedulesByCourse(courseCode: String): List<ClassSchedule> {
        return schedules.filter { it.courseCode == courseCode }
    }
}
