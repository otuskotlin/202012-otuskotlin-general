package ru.otus.otuskotlin.general

inline class UserId(val value: String) {
    companion object {
        val GUEST = UserId("guest")
    }
}

data class UserModel(
    val id: UserId,
    val name: String
)

data class SecurityContext(
    val userId: UserId
)

class UserNotFoundException(
    val userId: UserId
) : Exception("No user with id = $userId found")

class OperationNotSupportedException(
    message: String
) : Exception(message)

interface CurrentUserService {
    suspend fun getCurrentUser(securityContext: SecurityContext): UserModel
    suspend fun rename(newName: String, securityContext: SecurityContext): UserModel
}