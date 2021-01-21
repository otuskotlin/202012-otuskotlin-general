import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface CallbackApi {
    fun operation(arg: String, callback: Callback<Int>): Cancellable
}

interface Callback<T> {
    fun onSuccess(value: T)
    fun onFailure(throwable: Throwable)
}

interface Cancellable {
    fun cancel()
}

// Wraps callback API with coroutine
suspend fun CallbackApi.operation(arg: String): Int =
    suspendCancellableCoroutine { continuation ->
        val callback = object : Callback<Int> {
            override fun onSuccess(value: Int) {
                continuation.resume(value)
            }

            override fun onFailure(throwable: Throwable) {
                continuation.resumeWithException(throwable)
            }
        }

        val cancellable = operation(arg, callback)
        continuation.invokeOnCancellation { cancellable.cancel() }
    }

// Implement callback API using coroutines
inline fun <T> Callback<T>.completeWithCoroutineResult(
    coroutineScope: CoroutineScope,
    callbackExecutor: Executor,
    crossinline block: suspend () -> T
): Cancellable {
    val job = coroutineScope.launch {
        val value = block()
        callbackExecutor.execute { onSuccess(value) }
    }
    job.invokeOnCompletion { throwable ->
        if (throwable != null) callbackExecutor.execute { onFailure(throwable) }
    }
    return object : Cancellable {
        override fun cancel() {
            job.cancel()
        }
    }
}

class CoroutineBackedCallbackApi(
    private val coroutineScope: CoroutineScope,
    private val callbackExecutor: Executor
) : CallbackApi {
    override fun operation(arg: String, callback: Callback<Int>): Cancellable =
        callback.completeWithCoroutineResult(coroutineScope, callbackExecutor) {
            TODO("Not implemented yet")
        }
}
