package ru.otus.otuskotlin.general.models

inline class Email(val email: String) {
    companion object {
        val NONE = Email("")
    }
}
