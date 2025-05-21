import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class ScheduleManager {
    // Listas para almacenar datos
    private val courses = mutableListOf<Course>()
    private val schedules = mutableListOf<HorarioClase>()
    private val tests = mutableListOf<Test>()
    private var nextTestId = 1  // Auto-incremento para IDs de exámenes

    // --- Métodos para Courses ---
    fun addCourse(course: Course) {
        courses.add(course)
    }

    fun getCourseByCode(code: String): Course? {
        return courses.find { it.code == code }
    }

    // --- Métodos para HorarioClase ---
    fun addSchedule(schedule: HorarioClase) {
        schedules.add(schedule)
    }

    fun getSchedulesByDay(day: DayOfWeek): List<HorarioClase> {
        return schedules.filter { it.day == day }.sortedBy { it.start }
    }

    // --- Métodos para Tests ---
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
}