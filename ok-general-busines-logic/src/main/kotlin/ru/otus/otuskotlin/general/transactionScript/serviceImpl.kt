package ru.otus.otuskotlin.general.transactionScript

import kotlinx.coroutines.flow.firstOrNull
import ru.otus.otuskotlin.general.Connection
import ru.otus.otuskotlin.general.CurrentUserService
import ru.otus.otuskotlin.general.OperationNotSupportedException
import ru.otus.otuskotlin.general.Row
import ru.otus.otuskotlin.general.SecurityContext
import ru.otus.otuskotlin.general.UserId
import ru.otus.otuskotlin.general.UserModel
import ru.otus.otuskotlin.general.UserNotFoundException

fun Row.asUserModel(): UserModel =
    UserModel(
        id = UserId(this["id"] as String),
        name = this["name"] as String
    )

class CurrentUserServiceImpl(
    private val connection: Connection
) : CurrentUserService {
    override suspend fun getCurrentUser(securityContext: SecurityContext): UserModel =
        with(securityContext) {
            if (userId == UserId.GUEST) {
                UserModel(id = UserId.GUEST, name = "Guest user")
            } else {
                connection.query("SELECT * FROM users WHERE id = ?", userId.value)
                    .firstOrNull()
                    ?.asUserModel()
                    ?: throw UserNotFoundException(userId)
            }
        }

    override suspend fun rename(newName: String, securityContext: SecurityContext): UserModel =
        with(securityContext) {
            if (userId == UserId.GUEST) {
                throw OperationNotSupportedException("Guest users can't rename themselves")
            } else {
                connection.query("UPDATE users SET name = ? WHERE id = ? RETURNING *", newName, userId.value)
                    .firstOrNull()
                    ?.asUserModel()
                    ?: throw UserNotFoundException(userId)
            }
        }
}