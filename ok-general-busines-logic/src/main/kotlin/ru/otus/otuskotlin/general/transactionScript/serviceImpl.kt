package ru.otus.otuskotlin.general.transactionScript

import ru.otus.otuskotlin.general.Connection
import ru.otus.otuskotlin.general.CurrentUserService
import ru.otus.otuskotlin.general.SecurityContext
import ru.otus.otuskotlin.general.UserModel

class CurrentUserServiceImpl(
    private val connection: Connection
) : CurrentUserService {
    override suspend fun getCurrentUser(securityContext: SecurityContext): UserModel =
        CurrentUserContext(connection = connection, securityContext = securityContext)
            .apply { GET_PIPELINE.run(this) }
            .respondUserModel()

    override suspend fun rename(newName: String, securityContext: SecurityContext): UserModel =
        CurrentUserContext(connection = connection, securityContext = securityContext, requestNewName = newName)
            .apply { RENAME_PIPELINE.run(this) }
            .respondUserModel()
}
