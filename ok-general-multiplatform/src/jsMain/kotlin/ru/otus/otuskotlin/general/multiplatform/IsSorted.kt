package ru.otus.otuskotlin.general.multiplatform

@JsModule("is-sorted")
@JsNonModule
external fun <T> sorted(a: Array<T>): Boolean
