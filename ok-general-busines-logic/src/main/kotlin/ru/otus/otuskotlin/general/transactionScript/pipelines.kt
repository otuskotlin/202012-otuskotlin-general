package ru.otus.otuskotlin.general.transactionScript

import kotlinx.coroutines.flow.firstOrNull
import ru.otus.otuskotlin.general.Connection
import ru.otus.otuskotlin.general.OperationNotSupportedException
import ru.otus.otuskotlin.general.Row
import ru.otus.otuskotlin.general.SecurityContext
import ru.otus.otuskotlin.general.UserId
import ru.otus.otuskotlin.general.UserModel
import ru.otus.otuskotlin.general.UserNotFoundException
import ru.otus.otuskotlin.general.transactionScript.pipelines.operation
import ru.otus.otuskotlin.general.transactionScript.pipelines.pipeline

data class CurrentUserContext(
    val connection: Connection,
    val securityContext: SecurityContext,
    var status: CurrentUserContextStatus = CurrentUserContextStatus.PROCESSING,
    var error: Throwable? = null,
    val requestNewName: String? = null,
    var responseUserModel: UserModel? = null
) {
    fun respondUserModel(): UserModel =
        when (status) {
            CurrentUserContextStatus.SUCCESS -> responseUserModel!!
            CurrentUserContextStatus.FAIL -> throw error!!
            else -> error("Request not processed correctly")
        }
}

enum class CurrentUserContextStatus {
    PROCESSING,
    SUCCESS,
    FAIL
}

fun Row.asUserModel(): UserModel =
    UserModel(
        id = UserId(this["id"] as String),
        name = this["name"] as String
    )

val GET_PIPELINE = pipeline<CurrentUserContext> {
    // If request for guest
    operation {
        startIf { securityContext.userId == UserId.GUEST }
        run {
            responseUserModel = UserModel(id = UserId.GUEST, name = "Guest user")
            status = CurrentUserContextStatus.SUCCESS
        }
    }

    // For all other users
    operation {
        startIf { status == CurrentUserContextStatus.PROCESSING }
        run {
            connection
                .query("SELECT * FROM users WHERE id = ?", securityContext.userId.value)
                .firstOrNull()?.let {
                    responseUserModel = it.asUserModel()
                    status = CurrentUserContextStatus.SUCCESS
                }
                ?: run {
                    error = UserNotFoundException(securityContext.userId)
                    status = CurrentUserContextStatus.FAIL
                }
        }
    }

    // On error set status to FAIL
    onError {
        error = it
        status = CurrentUserContextStatus.FAIL
    }
}

val RENAME_PIPELINE = pipeline<CurrentUserContext> {
    run {
        startIf { requestNewName.isNullOrBlank() }
        run {
            error = IllegalArgumentException("Name should be non-blank")
            status = CurrentUserContextStatus.FAIL
        }
    }

    // If request for guest
    operation {
        startIf { securityContext.userId == UserId.GUEST }
        run {
            error = OperationNotSupportedException("Guest users can't rename themselves")
            status = CurrentUserContextStatus.FAIL
        }
    }

    // For all other users
    operation {
        startIf { status == CurrentUserContextStatus.PROCESSING }
        run {
            connection
                .query(
                    "UPDATE users SET name = ? WHERE id = ? RETURNING *",
                    requestNewName!!,
                    securityContext.userId.value
                )
                .firstOrNull()?.let {
                    responseUserModel = it.asUserModel()
                    status = CurrentUserContextStatus.SUCCESS
                }
                ?: run {
                    error = UserNotFoundException(securityContext.userId)
                    status = CurrentUserContextStatus.FAIL
                }
        }
    }

    // On error set status to FAIL
    onError {
        error = it
        status = CurrentUserContextStatus.FAIL
    }
}