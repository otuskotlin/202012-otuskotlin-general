package ru.otus.otuskotlin.general.models

import java.time.Instant

data class UserModel(
    var id: UserId = UserId.NONE,
    var fname: String = "",
    var mname: String = "",
    var lname: String = "",
    var dob: Instant = Instant.MIN,
    var email: Email = Email.NONE,
    var phone: Phone = Phone.NONE,
    var permissions: MutableSet<UserPermissions> = mutableSetOf()
)

