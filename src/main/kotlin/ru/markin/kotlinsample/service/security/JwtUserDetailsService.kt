package ru.markin.kotlinsample.service.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markin.kotlinsample.security.JwtUserFactory
import ru.markin.kotlinsample.repository.security.UserRepository

@Service
@Transactional
class JwtUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = this.userRepository.findByUsername(username)

        return if (user != null) JwtUserFactory.create(user)
        else throw UsernameNotFoundException("No user found with username '$username'.")
    }
}