package ru.otus.otuskotlin.general.transactionScript.pipelines

interface IOperation<T> {
    suspend fun run(context: T)
}