package ru.otus.otuskotlin.general.transactionScript.pipelines

typealias Runnable<T> = suspend T.() -> Unit

typealias Predicate<T> = suspend T.() -> Boolean

typealias ErrorHandler<T> = suspend T.(Throwable) -> Unit