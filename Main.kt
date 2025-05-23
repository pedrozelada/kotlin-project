import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException
import java.util.*

fun main() {
    val manager = ScheduleManager()
    val scanner = Scanner(System.`in`)

    while (true) {
        println("\n=== MAIN MENU ===")
        println("1. Add course")
        println("2. View all courses")
        println("3. View daily schedule")
        println("4. Add test")
        println("5. View upcoming tests")
        println("6. Exit")
        print("Select an option: ")

        when (scanner.nextLine()) {
            "1" -> addCourseFromInput(manager, scanner)
            "2" -> showAllCourses(manager)
            "3" -> showDailySchedule(manager, scanner)
            "4" -> addTestFromInput(manager, scanner)
            "5" -> showUpcomingTests(manager)
            "6" -> {
                println("Goodbye!")
                return
            }
            else -> println("Invalid option.")
        }
    }
}

// --- Helper function to add a course and schedule ---
fun addCourseFromInput(manager: ScheduleManager, scanner: Scanner) {
    println("\n--- NEW COURSE ---")
    print("Course code (e.g., MATH-101): ")
    val code = scanner.nextLine()
    print("Name: ")
    val name = scanner.nextLine()
    print("Instructor: ")
    val teacher = scanner.nextLine()

    manager.addCourse(Course(code, name, teacher))

    // Add schedule(s)
    while (true) {
        println("\nAdd a schedule for this course? (y/n): ")
        if (scanner.nextLine().lowercase() != "y") break

        print("Day (MONDAY, TUESDAY, ...): ")
        val dayInput = scanner.nextLine()
        val day = parseDayOfWeek(dayInput)
        if (day == null) {
            println("Invalid day entered.")
            continue
        }

        try {
            print("Start time (HH:mm): ")
            val start = LocalTime.parse(scanner.nextLine())
            print("End time (HH:mm): ")
            val end = LocalTime.parse(scanner.nextLine())

            if (start >= end) {
                println("Start time must be before end time.")
                continue
            }

            val conflict = manager.hasConflict(code, day, start, end)
            if (conflict) {
                println("Time conflict with another course.")
                continue
            }

            manager.addSchedule(ClassSchedule(code, day, start, end))
            println("Schedule added!")
        } catch (e: DateTimeParseException) {
            println("Invalid time format. Please use HH:mm.")
        }
    }
}

// --- Show schedule by day ---
fun showDailySchedule(manager: ScheduleManager, scanner: Scanner) {
    println("\n--- DAILY SCHEDULE ---")
    println("Day (MONDAY, TUESDAY, ...): ")
    try {
        val day = DayOfWeek.valueOf(scanner.nextLine().uppercase())
        val schedules = manager.getSchedulesByDay(day)
        if (schedules.isEmpty()) {
            println("No classes on this day.")
        } else {
            schedules.forEach { s ->
                val course = manager.getCourseByCode(s.courseCode)
                println(" ${s.start}-${s.end}: ${course?.name ?: "Unknown course"} (${s.courseCode})")
            }
        }
    } catch (e: IllegalArgumentException) {
        println("Invalid day.")
    }
}

// --- Add a new test ---
fun addTestFromInput(manager: ScheduleManager, scanner: Scanner) {
    println("\n--- NEW TEST ---")
    showAllCourses(manager)
    print("\nEnter course code: ")
    val courseCode = scanner.nextLine()

    if (manager.getCourseByCode(courseCode) == null) {
        println("Invalid course code")
        return
    }

    print("Topic: ")
    val topic = scanner.nextLine()

    try {
        print("Date (yyyy-MM-dd): ")
        val date = LocalDate.parse(scanner.nextLine())
        print("Location (optional): ")
        val place = scanner.nextLine()

        manager.addTest(Test(0, courseCode, topic, date, place))
        println("Test added!")
    } catch (e: DateTimeParseException) {
        println("Invalid date format. Please use yyyy-MM-dd.")
    }
}

// --- Show upcoming tests ---
fun showUpcomingTests(manager: ScheduleManager) {
    println("\n--- UPCOMING TESTS (next 7 days) ---")
    val tests = manager.getUpcomingTests()
    if (tests.isEmpty()) {
        println("No upcoming tests.")
    } else {
        tests.forEach { t ->
            val course = manager.getCourseByCode(t.courseCode)
            println("${t.date}: ${course?.name ?: "Unknown course"} - ${t.topic} (${t.place})")
        }
    }
}

// --- Show all registered courses ---
fun showAllCourses(manager: ScheduleManager) {
    println("\n--- ALL COURSES ---")
    manager.getAllCourses().forEach { course ->
        println("${course.code} - ${course.name} (Instructor: ${course.teacher})")
    }
}

// --- Show all schedules for a specific course ---
fun showCourseSchedules(manager: ScheduleManager, scanner: Scanner) {
    showAllCourses(manager)
    print("\nEnter course code to view schedules: ")
    val code = scanner.nextLine()

    val schedules = manager.getSchedulesByCourse(code)
    if (schedules.isEmpty()) {
        println("This course has no registered schedules.")
    } else {
        println("\n--- COURSE SCHEDULES ---")
        schedules.forEach { s ->
            println("${s.day}: ${s.start}-${s.end}")
        }
    }
}

// --- Convert day input to DayOfWeek ---
fun parseDayOfWeek(input: String): DayOfWeek? {
    return when (input.trim().uppercase()) {
        "MONDAY", "LUNES" -> DayOfWeek.MONDAY
        "TUESDAY", "MARTES" -> DayOfWeek.TUESDAY
        "WEDNESDAY", "MIERCOLES", "MIÉRCOLES" -> DayOfWeek.WEDNESDAY
        "THURSDAY", "JUEVES" -> DayOfWeek.THURSDAY
        "FRIDAY", "VIERNES" -> DayOfWeek.FRIDAY
        "SATURDAY", "SABADO", "SÁBADO" -> DayOfWeek.SATURDAY
        "SUNDAY", "DOMINGO" -> DayOfWeek.SUNDAY
        else -> null
    }
}

// --- Check for schedule conflicts ---
fun ScheduleManager.hasConflict(courseCode: String, day: DayOfWeek, start: LocalTime, end: LocalTime): Boolean {
    return getSchedulesByDay(day).any {
        it.courseCode != courseCode &&
        ((start < it.end) && (end > it.start))
    }
}
