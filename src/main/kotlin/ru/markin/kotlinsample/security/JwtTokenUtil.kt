package ru.markin.kotlinsample.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

import io.jsonwebtoken.impl.DefaultClock
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import ru.markin.kotlinsample.security.util.JwtUser
import java.io.Serializable
import java.util.*

@Component
class JwtTokenUtil : Serializable {

    private val clock = DefaultClock.INSTANCE

    @Value("\${jwt.secret}")
    private val secret: String = ""

    @Value("\${jwt.expiration}")
    private val expiration: Long = 0L

    fun getUsernameFromToken(token: String): String? {
        return getClaimFromToken(token, { it.subject })
    }

    fun getIssuedAtDateFromToken(token: String): Date? {
        return getClaimFromToken(token, { it.issuedAt })
    }

    fun getExpirationDateFromToken(token: String): Date? {
        return getClaimFromToken(token, { it.expiration })
    }

    private fun <R> getClaimFromToken(token: String, claimsResolver: (Claims) -> R): R {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration?.before(clock.now()) ?: true
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date?, lastPasswordReset: Date?): Boolean {
        return lastPasswordReset != null && created != null
                && created.before(lastPasswordReset)
    }

    private fun ignoreTokenExpiration(token: String): Boolean {
        // here you specify tokens, for that the expiration is ignored
        return false
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims = HashMap<String, Any>()
        return doGenerateToken(claims, userDetails.username)
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date): Boolean? {
        val created = getIssuedAtDateFromToken(token)
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token)
                || ignoreTokenExpiration(token))
    }

    fun refreshToken(token: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        val claims = getAllClaimsFromToken(token)
        claims.issuedAt = createdDate
        claims.expiration = expirationDate

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean? {
        val user = userDetails as JwtUser
        val username = getUsernameFromToken(token)
        val created = getIssuedAtDateFromToken(token)
        //final Date expiration = getExpirationDateFromToken(token);
        return (username == user.username
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate))
    }

    private fun calculateExpirationDate(createdDate: Date): Date {
        return Date(createdDate.time + expiration * 1000)
    }
}