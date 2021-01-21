package ru.otus.otuskotlin.general.models

inline class UserId(val id: String) {
    companion object {
        val NONE = UserId("")
    }
}
