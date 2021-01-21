package ru.otus.otuskotlin.general.dsl

import ru.otus.otuskotlin.general.models.*
import java.time.LocalDate

class UserConfig {
    private var id: UserId = UserId.NONE
    private var name_f: String = ""
    private var name_m: String = ""
    private var name_l: String = ""
    private var dob: LocalDate = LocalDate.MIN
    private var email: Email = Email.NONE
    private var phone: Phone = Phone.NONE
    private var permissions: MutableSet<UserPermissionsModel> = mutableSetOf()

    fun build() = UserModel(
        id = id,
        fname = name_f,
        mname = name_m,
        lname = name_l,
        dob = dob,
        email = email,
        phone = phone,
        permissions = permissions
    )

    fun id(id: UserId) {
        this.id = id
    }
    fun id(id: String) = id(UserId(id))


    fun name(block: UserNameConf.() -> Unit) {
        val nameConf = UserNameConf().apply(block)
        name_f = nameConf.first
        name_m = nameConf.second
        name_l = nameConf.last
    }

    fun birth(block: UserBirthConf.() -> Unit) {
        val birthConf = UserBirthConf().apply(block)
        dob = birthConf.date
    }

    fun contacts(block: UserContactsConf.() -> Unit) {
        val contactsConf = UserContactsConf().apply(block)
        email = Email(contactsConf.email)
        phone = Phone(contactsConf.phone)
    }

    fun permissions(block: UserPermissionsConf.() -> Unit) {
        val permissionsConf = UserPermissionsConf().apply(block)
        permissions = permissionsConf.permissions.toMutableSet()
    }
}

fun user(block: UserConfig.() -> Unit) = UserConfig().apply(block).build()
