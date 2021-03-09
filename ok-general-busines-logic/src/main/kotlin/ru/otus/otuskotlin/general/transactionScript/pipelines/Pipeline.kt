package ru.otus.otuskotlin.general.transactionScript.pipelines

class Pipeline<T>
private constructor(
    private val operations: Collection<IOperation<T>>,
    private val checkPrecondition: Predicate<T>,
    private val handleError: ErrorHandler<T>
) : IOperation<T> {
    override suspend fun run(context: T) {
        try {
            if (checkPrecondition(context)) operations.forEach { it.run(context) }
        } catch (t: Throwable) {
            handleError(context, t)
        }
    }

    @PipelineDsl
    class Builder<T> {
        private val operations: MutableList<IOperation<T>> = mutableListOf()
        private var checkPrecondition: Predicate<T> = { true }
        private var handleError: ErrorHandler<T> = { throw it }

        fun run(operation: IOperation<T>) {
            operations.add(operation)
        }

        fun run(block: Runnable<T>) {
            operations.add(Operation.Builder<T>().apply { run(block) }.build())
        }

        fun startIf(blocK: Predicate<T>) {
            checkPrecondition = blocK
        }

        fun onError(block: ErrorHandler<T>) {
            handleError = block
        }

        fun build(): Pipeline<T> =
            Pipeline(operations, checkPrecondition, handleError)
    }
}