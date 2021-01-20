import kotlinx.coroutines.delay

suspend fun main(args: Array<String>) {
    // entrance point
    println("Hello")
    delay(1000) // suspension point
    println("World")
    // return point
}
