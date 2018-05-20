package ru.markin.kotlinsample.repository.security

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.markin.kotlinsample.model.security.User

@Transactional(propagation = Propagation.MANDATORY)
interface UserRepository : PagingAndSortingRepository<User, Int> {

    fun findByUsername(username: String): User?
}