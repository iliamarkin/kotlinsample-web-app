package ru.markin.kotlinsample.service.transaction

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.markin.kotlinsample.domain.transaction.Balance
import ru.markin.kotlinsample.domain.transaction.Transaction
import ru.markin.kotlinsample.domain.transaction.TransactionCategory
import ru.markin.kotlinsample.repository.transaction.TransactionCategoryRepository
import ru.markin.kotlinsample.repository.transaction.TransactionHistoryRepository
import ru.markin.kotlinsample.service.util.NotFoundObjectException
import java.util.*

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
class TransactionService {

    @Autowired
    private lateinit var transactionHistoryRepository: TransactionHistoryRepository

    @Autowired
    private lateinit var categoryRepository: TransactionCategoryRepository

    @Autowired
    private lateinit var balanceService: BalanceService

    @Transactional
    fun getTransactionHistory(userId: Int, page: Int): List<Transaction> {

        val pageRequest = PageRequest.of(page, 1000, Sort.by(Sort.Direction.DESC, "transactionDate"))

        return this.transactionHistoryRepository.findAllByUserId(userId, pageRequest)
                .map { map(it) }
                .toList()
    }

    @Transactional
    fun addTransaction(transaction: Transaction): Balance {

        val category = this.categoryRepository.findById(transaction.category.id)
                .orElseThrow { NotFoundObjectException("'categoryId' = ${transaction.category.id}") }

        val newTransaction = ru.markin.kotlinsample.model.transaction.Transaction().apply {
            this.userId = transaction.userId
            this.sum = transaction.sum
            this.transactionDate = Date(transaction.transactionDate)
            this.category = category
        }

        val savedTransaction = this.transactionHistoryRepository.save(newTransaction)

        return this.balanceService.addToBalance(savedTransaction.userId, savedTransaction.sum)
    }

    @Transactional
    fun getTransactionCategories(): List<TransactionCategory> {

        return this.categoryRepository.findAll()
                .map { TransactionCategory(it.id, it.income, it.name) }
                .toList()
    }

    @Transactional
    fun addTransactionCategory(category: TransactionCategory): Int {

        val transactionCategory = ru.markin.kotlinsample.model.transaction.TransactionCategory()
                .apply {
                    this.income = category.income
                    this.name = category.name
                }

        return this.categoryRepository.save(transactionCategory).id
    }

    private fun map(t: ru.markin.kotlinsample.model.transaction.Transaction): Transaction {
        return Transaction().apply {
            this.userId = t.userId
            this.sum = t.sum
            this.transactionDate = t.transactionDate.time
            this.category = TransactionCategory(
                    t.category.id,
                    t.category.income,
                    t.category.name)
        }
    }
}