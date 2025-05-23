import java.time.LocalDate

data class Test(
    val id: Int,
    val courseCode: String,  
    val topic: String,          
    val date: LocalDate,
    val place: String = ""     
)