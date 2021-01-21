import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

fun main(): Unit = runBlocking {
    withTimeout(1000) {
        possiblyLongTask()
    }
}

suspend fun possiblyLongTask() {
    println("Start possibly long task")
    delay(2000)
    println("Complete possibly long task")
}