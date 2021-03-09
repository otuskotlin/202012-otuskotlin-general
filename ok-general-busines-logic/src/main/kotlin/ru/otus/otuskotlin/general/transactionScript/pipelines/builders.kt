package ru.otus.otuskotlin.general.transactionScript.pipelines

inline fun <T> pipeline(block: Pipeline.Builder<T>.() -> Unit): Pipeline<T> =
    Pipeline.Builder<T>().apply(block).build()

inline fun <T> operation(block: Operation.Builder<T>.() -> Unit): Operation<T> =
    Operation.Builder<T>().apply(block).build()

inline fun <T> Pipeline.Builder<T>.pipeline(block: Pipeline.Builder<T>.() -> Unit) =
    run(Pipeline.Builder<T>().apply(block).build())

inline fun <T> Pipeline.Builder<T>.operation(block: Operation.Builder<T>.() -> Unit) =
    run(Operation.Builder<T>().apply(block).build())