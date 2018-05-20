package ru.markin.kotlinsample.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import ru.markin.kotlinsample.security.util.JwtUser
import ru.markin.kotlinsample.model.security.Authority
import ru.markin.kotlinsample.model.security.User

object JwtUserFactory {

    fun create(user: User): JwtUser {
        return JwtUser.create {
            id { user.id }
            username { user.username }
            firstName { user.firstName }
            lastName { user.lastName }
            email { user.email }
            password { user.password }
            authorities { mapToGrantedAuthorities(user.authorities) }
            enabled { user.enabled }
            lastPasswordResetDate { user.lastPasswordResetDate }
        }
    }

    private fun mapToGrantedAuthorities(authorities: Collection<Authority>): List<GrantedAuthority> {
        return authorities
                .map { authority -> SimpleGrantedAuthority(authority.name.name) }
                .toCollection(arrayListOf())
    }
}