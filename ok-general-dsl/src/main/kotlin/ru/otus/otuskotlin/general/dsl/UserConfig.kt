package ru.otus.otuskotlin.general.dsl

import ru.otus.otuskotlin.general.models.Email
import ru.otus.otuskotlin.general.models.Phone
import ru.otus.otuskotlin.general.models.UserId
import ru.otus.otuskotlin.general.models.UserPermissions
import java.time.LocalDate

class UserConfig {
    private var id: UserId = UserId.NONE
    private var name_f: String = ""
    private var name_m: String = ""
    private var name_l: String = ""
    private var dob: LocalDate = LocalDate.MIN
    private var email: Email = Email.NONE
    private var phone: Phone = Phone.NONE
    private var permissions: MutableSet<UserPermissionsModel>
}
