package ru.markin.kotlinsample.repository.transaction

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.markin.kotlinsample.model.transaction.Transaction

@Transactional(propagation = Propagation.MANDATORY)
interface TransactionHistoryRepository : PagingAndSortingRepository<Transaction, Int> {

    fun findAllByUserId(userId: Int, pageable: Pageable): List<Transaction>
}