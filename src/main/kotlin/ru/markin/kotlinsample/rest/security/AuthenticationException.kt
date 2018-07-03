package ru.markin.kotlinsample.rest.security

class AuthenticationException(message: String?, cause: Throwable?) : RuntimeException(message, cause)

class PreconditionException(message: String?) : RuntimeException(message)