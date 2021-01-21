package ru.otus.otuskotlin.general.dsl

import java.time.LocalDate

@UserDSL
class UserBirthConf {
    var date: LocalDate = LocalDate.MIN
}
