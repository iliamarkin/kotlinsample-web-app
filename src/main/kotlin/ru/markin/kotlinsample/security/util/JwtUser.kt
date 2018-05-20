package ru.markin.kotlinsample.security.util

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class HasNullFieldException : RuntimeException()

class JwtUser private constructor(

        @JsonIgnore
        val id: Int,

        val firstName: String,

        val lastName: String,

        val email: String,

        @JsonIgnore
        val lastPasswordResetDate: Date,

        private val username: String,

        private val password: String,

        private val authorities: Collection<GrantedAuthority>,

        private val enabled: Boolean) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return this.authorities
    }

    override fun isEnabled(): Boolean {
        return this.enabled
    }

    override fun getUsername(): String {
        return this.username
    }

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun getPassword(): String {
        return this.password
    }

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    companion object {
        fun create(init: Builder.() -> Unit) = Builder(init).build()
    }


    class Builder private constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }

        private var id: Int? = null
        private var username: String? = null
        private var firstName: String? = null
        private var lastName: String? = null
        private var password: String? = null
        private var email: String? = null
        private var authorities: Collection<GrantedAuthority>? = null
        private var enabled: Boolean? = null
        private var lastPasswordResetDate: Date? = null

        fun id(init: Builder.() -> Int) = apply { this.id = init() }

        fun username(init: Builder.() -> String) = apply { this.username = init() }

        fun firstName(init: Builder.() -> String) = apply { this.firstName = init() }

        fun lastName(init: Builder.() -> String) = apply { this.lastName = init() }

        fun password(init: Builder.() -> String) = apply { this.password = init() }

        fun email(init: Builder.() -> String) = apply { this.email = init() }

        fun authorities(init: Builder.() -> Collection<GrantedAuthority>) = apply { this.authorities = init() }

        fun enabled(init: Builder.() -> Boolean) = apply { this.enabled = init() }

        fun lastPasswordResetDate(init: Builder.() -> Date) = apply { this.lastPasswordResetDate = init() }

        fun build(): JwtUser {
            if (this.id == null
                    || this.username == null
                    || this.firstName == null
                    || this.lastName == null
                    || this.password == null
                    || this.email == null
                    || this.authorities == null
                    || this.enabled == null
                    || this.lastPasswordResetDate == null) {
                throw HasNullFieldException()
            }
            return JwtUser(id!!,
                    firstName!!,
                    lastName!!,
                    email!!,
                    lastPasswordResetDate!!,
                    username!!,
                    password!!,
                    authorities!!,
                    enabled!!)
        }
    }
}

