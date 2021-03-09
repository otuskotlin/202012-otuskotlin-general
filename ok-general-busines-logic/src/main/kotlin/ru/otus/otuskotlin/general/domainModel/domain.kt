package ru.otus.otuskotlin.general.domainModel

import kotlinx.coroutines.flow.firstOrNull
import ru.otus.otuskotlin.general.Connection
import ru.otus.otuskotlin.general.OperationNotSupportedException
import ru.otus.otuskotlin.general.UserId
import ru.otus.otuskotlin.general.UserModel
import ru.otus.otuskotlin.general.UserNotFoundException

interface User {
    suspend fun id(): UserId
    suspend fun name(): String
    suspend fun rename(newName: String)
    suspend fun toModel(): UserModel = UserModel(
        id = id(),
        name = name()
    )
}

interface Users {
    suspend fun get(id: UserId): User
}

class DBUser(
    private val id: UserId,
    private val connection: Connection
) : User {
    override suspend fun id(): UserId =
        connection.query("SELECT id FROM users WHERE id = ?", id.value)
            .firstOrNull()
            ?.let { UserId(it["id"] as String) }
            ?: throw UserNotFoundException(id)

    override suspend fun name(): String =
        connection.query("SELECT name FROM users WHERE id = ?", id.value)
            .firstOrNull()
            ?.let { it["name"] as String }
            ?: throw UserNotFoundException(id)

    override suspend fun rename(newName: String) {
        connection.query("UPDATE users SET name = ? WHERE id = ?", newName, id.value)
            .also { if (it.modifiedCount() == 0) throw UserNotFoundException(id) }
    }
}

class DBUsers(
    private val connection: Connection
) : Users {
    override suspend fun get(id: UserId): User =
        DBUser(id, connection)
}

object GuestUser : User {
    override suspend fun id(): UserId = UserId.GUEST

    override suspend fun name(): String = "Guest user"

    override suspend fun rename(newName: String) =
        throw OperationNotSupportedException("Guest users can't rename themselves")
}

class WithGuestUsers(
    private val delegate: Users
) : Users {
    override suspend fun get(id: UserId): User =
        if (id == UserId.GUEST) GuestUser
        else delegate.get(id)
}

fun Users.withGuestUser(): Users =
    if (this is WithGuestUsers) this
    else WithGuestUsers(this)