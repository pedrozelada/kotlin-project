import java.time.LocalDate

data class Assingments(
    val id: Int,
    val courseCode: String,       
    val description: String,  
    val dueDate: LocalDate,
    val completed: Boolean = false
)