package utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtils {

    fun currentDate(): String{
        val formatter = DateTimeFormatter.ofPattern("dd-MM")
        val current = LocalDateTime.now().format(formatter)

        return current
    }

}