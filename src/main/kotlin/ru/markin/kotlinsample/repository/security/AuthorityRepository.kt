package ru.markin.kotlinsample.repository.security

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.markin.kotlinsample.model.security.Authority
import ru.markin.kotlinsample.model.security.AuthorityName

@Transactional(propagation = Propagation.MANDATORY)
interface AuthorityRepository : JpaRepository<Authority, Int> {

    fun findByName(name: AuthorityName) : Authority?
}