import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    doWorld()
}

suspend fun doWorld() = coroutineScope {
    repeat(50_000) {
        launch {
            delay(5000L)
            print(".")
        }
    }
    println("done done??")
}
