package ru.markin.kotlinsample.security.controller

class AuthenticationException(message: String?, cause: Throwable?) : RuntimeException(message, cause)

class PreconditionException(message: String?) : RuntimeException(message)