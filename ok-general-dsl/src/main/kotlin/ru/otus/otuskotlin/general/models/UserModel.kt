package ru.otus.otuskotlin.general.models

import java.time.LocalDate

data class UserModel(
    var id: UserId = UserId.NONE,
    var fname: String = "",
    var mname: String = "",
    var lname: String = "",
    var dob: LocalDate = LocalDate.MIN,
    var email: Email = Email.NONE,
    var phone: Phone = Phone.NONE,
    var permissions: MutableSet<UserPermissions> = mutableSetOf()
)

