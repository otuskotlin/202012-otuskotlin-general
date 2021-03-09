package ru.otus.otuskotlin.general.domainModel

import ru.otus.otuskotlin.general.CurrentUserService
import ru.otus.otuskotlin.general.SecurityContext
import ru.otus.otuskotlin.general.UserModel

class CurrentUserServiceImpl(
    private val users: Users
) : CurrentUserService {
    override suspend fun getCurrentUser(securityContext: SecurityContext): UserModel =
        users.withGuestUser()
            .get(securityContext.userId)
            .toModel()

    override suspend fun rename(newName: String, securityContext: SecurityContext): UserModel =
        users.withGuestUser()
            .get(securityContext.userId)
            .apply { rename(newName) }
            .toModel()
}