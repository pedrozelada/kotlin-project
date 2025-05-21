import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

fun main() {
    val manager = ScheduleManager()
    val scanner = Scanner(System.`in`)

    // Ejemplo de datos iniciales (opcional)
    manager.addCourse(Course("MAT-101", "Cálculo I", "Dr. Pérez", "#FF5733"))
    manager.addSchedule(HorarioClase("MAT-101", DayOfWeek.MONDAY, LocalTime.of(14, 0), LocalTime.of(16, 0)))

    while (true) {
        println("\n=== MENÚ PRINCIPAL ===")
        println("1. Agregar curso")
        println("2. Ver horario por día")
        println("3. Agregar examen")
        println("4. Ver próximos exámenes")
        println("5. Salir")
        print("Seleccione una opción: ")

        when (scanner.nextLine()) {
            "1" -> addCourseFromInput(manager, scanner)
            "2" -> showDailySchedule(manager, scanner)
            "3" -> addTestFromInput(manager, scanner)
            "4" -> showUpcomingTests(manager)
            "5" -> {
                println("¡Hasta luego! 👋")
                return
            }
            else -> println("⚠️ Opción no válida.")
        }
    }
}

// --- Funciones auxiliares para entrada ---
fun addCourseFromInput(manager: ScheduleManager, scanner: Scanner) {
    println("\n--- NUEVO CURSO ---")
    print("Código del curso (ej: MAT-101): ")
    val code = scanner.nextLine()
    print("Nombre: ")
    val name = scanner.nextLine()
    print("Profesor: ")
    val teacher = scanner.nextLine()
    manager.addCourse(Course(code, name, teacher))
    println("✅ Curso agregado!")
}

fun showDailySchedule(manager: ScheduleManager, scanner: Scanner) {
    println("\n--- HORARIO POR DÍA ---")
    print("Día (LUNES, MARTES, ...): ")
    try {
        val day = DayOfWeek.valueOf(scanner.nextLine().uppercase())
        val schedules = manager.getSchedulesByDay(day)
        if (schedules.isEmpty()) {
            println("No hay clases este día.")
        } else {
            schedules.forEach { s ->
                val course = manager.getCourseByCode(s.courseCode)
                println("⏰ ${s.start}-${s.end}: ${course?.name ?: "Curso desconocido"} (${s.courseCode})")
            }
        }
    } catch (e: IllegalArgumentException) {
        println("⚠️ Día no válido.")
    }
}

fun addTestFromInput(manager: ScheduleManager, scanner: Scanner) {
    println("\n--- NUEVO EXAMEN ---")
    print("Código del curso: ")
    val courseCode = scanner.nextLine()
    print("Tema: ")
    val topic = scanner.nextLine()
    print("Fecha (yyyy-MM-dd): ")
    val date = LocalDate.parse(scanner.nextLine())
    print("Lugar (opcional): ")
    val place = scanner.nextLine()
    manager.addTest(Test(0, courseCode, topic, date, place))
    println("✅ Examen agregado!")
}

fun showUpcomingTests(manager: ScheduleManager) {
    println("\n--- PRÓXIMOS EXÁMENES (7 días) ---")
    val tests = manager.getUpcomingTests()
    if (tests.isEmpty()) {
        println("No hay exámenes próximos.")
    } else {
        tests.forEach { t ->
            val course = manager.getCourseByCode(t.courseCode)
            println("📝 ${t.date}: ${course?.name ?: "Curso desconocido"} - ${t.topic} (${t.place})")
        }
    }
}