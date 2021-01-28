package ru.otus.otuskotlin.general.dsl

import ru.otus.otuskotlin.general.models.UserPermissionsModel

@UserDSL
class UserPermissionsConf {
    private val _permissions: MutableSet<UserPermissionsModel> = mutableSetOf()
    val permissions: Set<UserPermissionsModel>
        get() = _permissions.toSet()

    fun add(permission: UserPermissionsModel) = _permissions.add(permission)
    fun add(permissionStr: String) = add(UserPermissionsModel.valueOf(permissionStr))

    operator fun String.unaryPlus() = add(this)
    operator fun UserPermissionsModel.unaryPlus() = add(this)
}
