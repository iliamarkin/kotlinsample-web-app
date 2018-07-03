package ru.markin.kotlinsample.repository.transaction

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.markin.kotlinsample.model.transaction.TransactionCategory

@Transactional(propagation = Propagation.MANDATORY)
interface TransactionCategoryRepository : JpaRepository<TransactionCategory, Int>