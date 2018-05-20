package ru.markin.kotlinsample.service.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markin.kotlinsample.model.security.Authority
import ru.markin.kotlinsample.model.security.AuthorityName
import ru.markin.kotlinsample.model.security.User
import ru.markin.kotlinsample.repository.security.AuthorityRepository
import ru.markin.kotlinsample.repository.security.UserRepository
import ru.markin.kotlinsample.security.JwtUserFactory
import ru.markin.kotlinsample.security.util.Account
import java.util.*

class UserAlreadyExistException(message: String?) : RuntimeException(message)

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
class UserAuthorityService {

    @Autowired
    private lateinit var authorityRepository: AuthorityRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passEncoder: PasswordEncoder

    @Transactional
    fun getAuthority(authorityName: AuthorityName): GrantedAuthority? {

        val authority = this.authorityRepository.findByName(authorityName)

        return if (authority != null)
            SimpleGrantedAuthority(authority.name.name)
        else
            null
    }

    @Transactional
    fun createAuthorityIfNotExist(authorityName: AuthorityName): GrantedAuthority {

        return SimpleGrantedAuthority(createAuthIfNotExist(authorityName).name.name)
    }

    @Transactional
    fun createUser(account: Account): UserDetails {

        val user = this.userRepository.findByUsername(account.credentials.username)
        if (user != null) {
            throw UserAlreadyExistException(user.username)
        }

        val authority = createAuthIfNotExist(AuthorityName.ROLE_USER)

        val savedUser = this.userRepository.save(User().apply {
            this.username = account.credentials.username
            this.password = passEncoder.encode(account.credentials.password)
            this.email = account.email
            this.firstName = account.firstName
            this.lastName = account.lastName
            this.enabled = true
            this.lastPasswordResetDate = Date()
            this.authorities += authority
        })

        return JwtUserFactory.create(savedUser)
    }

    private fun createAuthIfNotExist(authorityName: AuthorityName): Authority {

        return authorityRepository.findByName(authorityName)
                ?: return this.authorityRepository
                        .save(Authority().apply { this.name = authorityName })
    }
}