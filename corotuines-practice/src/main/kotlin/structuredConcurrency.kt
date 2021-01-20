import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.URL
import kotlin.random.Random
import kotlin.system.measureTimeMillis

suspend fun loadImageWithThumbnail(
    imageUrl: URL,
    thumbnailUrl: URL
): ImageWithThumbnail {
    TODO("Load both image and it’s thumbnail then return them")
}

// Container for both images
data class ImageWithThumbnail(
    val image: Image,
    val thumbnail: Image
)

// Loads single image and returns it
suspend fun loadImage(imageUrl: URL, shouldFail: Boolean = false): Image {
    // Imitates image loading
    if (shouldFail) {
        delay(100)
        throw RuntimeException("Imitate image loading error")
    }
    val downloadTimeMillis = Random.nextLong(500) + 1000
    delay(downloadTimeMillis)
    println("Image $imageUrl loaded for ${downloadTimeMillis}ms")
    return Image
}

// Image imitation
object Image

fun main(): Unit = runBlocking {
    val elapsedMillis = measureTimeMillis {
        try {
            loadImageWithThumbnail(
                URL("https://example.com/image.png"),
                URL("https://example.com/thumbnail.png")
            )
        } catch (e: Exception) {
            System.err.println("Image loading failed with next exception")
            e.printStackTrace()
        }
    }
    println("Complete within ${elapsedMillis}ms")

    // Wait a 3 seconds to be sure there are no images loading
    delay(3000)
}