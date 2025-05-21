import java.time.LocalDate

data class Tarea(
    val id: Int,
    val courseCode: Int,       
    val description: String,  
    val dueDate: LocalDate,
    val completed: Boolean = false
)