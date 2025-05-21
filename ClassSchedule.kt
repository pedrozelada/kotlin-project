import java.time.DayOfWeek
import java.time.LocalTime

data class HorarioClase(
    val courseCode: String,        
    val day: DayOfWeek,        
    val start: LocalTime,  
    val end: LocalTime      
)