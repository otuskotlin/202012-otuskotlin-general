import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) { println("I'M STARTED LAZILY") }

    println("Ensure no messages printed")
    delay(2000)

    println("Start coroutine and wait some time")
    job.start()
    delay(2000)
}