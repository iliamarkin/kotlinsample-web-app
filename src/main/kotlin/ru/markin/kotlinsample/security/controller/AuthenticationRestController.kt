package ru.markin.kotlinsample.security.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import ru.markin.kotlinsample.security.JwtTokenUtil
import ru.markin.kotlinsample.security.util.Account
import ru.markin.kotlinsample.security.util.AccountCredentials
import ru.markin.kotlinsample.security.util.JwtUser
import ru.markin.kotlinsample.service.security.JwtAuthenticationResponse
import ru.markin.kotlinsample.service.security.UserAuthorityService
import javax.servlet.http.HttpServletRequest

@RestController
class AuthenticationRestController {

    @Value("\${jwt.header}")
    private lateinit var tokenHeader: String

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var userAuthorityService: UserAuthorityService

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private lateinit var userDetailsService: UserDetailsService

    @RequestMapping(value = ["\${jwt.route.authentication.path}"], method = [(RequestMethod.POST)])
    fun createAuthenticationToken(@RequestBody accountCredentials: AccountCredentials): ResponseEntity<*> {

        authenticate(accountCredentials.username, accountCredentials.password)

        // Reload password post-security so we can generate the token
        val userDetails = this.userDetailsService.loadUserByUsername(accountCredentials.username)
        val token = this.jwtTokenUtil.generateToken(userDetails)

        // Return the token
        return ResponseEntity.ok(JwtAuthenticationResponse(token))
    }

    @RequestMapping(value = ["\${jwt.route.authentication.refresh}"], method = [(RequestMethod.GET)])
    fun refreshAndGetAuthenticationToken(request: HttpServletRequest): ResponseEntity<*> {
        val authToken = request.getHeader(this.tokenHeader)
        val token = authToken.substring(7)
        val username = this.jwtTokenUtil.getUsernameFromToken(token)
        val user = this.userDetailsService.loadUserByUsername(username) as JwtUser

        return if (this.jwtTokenUtil.canTokenBeRefreshed(token, user.lastPasswordResetDate)!!) {
            val refreshedToken = this.jwtTokenUtil.refreshToken(token)
            ResponseEntity.ok(JwtAuthenticationResponse(refreshedToken))
        } else {
            ResponseEntity.badRequest().body(null)
        }
    }

    @RequestMapping(value = ["\${jwt.route.registration.path}"], method = [(RequestMethod.POST)])
    fun signUpAndCreateAuthenticationToken(@RequestBody account: Account): ResponseEntity<*> {

        checkAccount(account)

        val createdUser = this.userAuthorityService.createUser(account)
        authenticate(createdUser.username, account.credentials.password)
        val token = this.jwtTokenUtil.generateToken(createdUser)

        // Return the token
        return ResponseEntity.ok(JwtAuthenticationResponse(token))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
    }

    @ExceptionHandler(PreconditionException::class)
    fun handlePreconditionException(e: PreconditionException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.message)
    }

    /**
     * Authenticates the user. If something is wrong, an [AuthenticationException] will be thrown
     */
    private fun authenticate(username: String, password: String) {
        checkUsernameAndPassword(username, password)

        try {
            this.authenticationManager
                    .authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: DisabledException) {
            throw AuthenticationException("User is disabled!", e)
        } catch (e: BadCredentialsException) {
            throw AuthenticationException("Bad credentials!", e)
        }
    }

    private fun checkUsernameAndPassword(username: String, password: String) {

        if (username.length < 4 || username.length > 50) {
            throw PreconditionException("Min size of 'username' is 4. Max size of 'username' is 50.")
        }
        if (password.length < 6 || password.length > 60) {
            throw PreconditionException("Min size of 'password' is 6. Max size of 'password' is 50.")
        }
    }

    private fun checkAccount(account: Account) {

        checkUsernameAndPassword(account.credentials.username, account.credentials.password)

        if (account.firstName.length < 2 || account.firstName.length > 50) {
            throw PreconditionException("Min size of 'first_name' is 2. Max size of 'first_name' is 50.")
        }
        if (account.lastName.length < 2 || account.lastName.length > 50) {
            throw PreconditionException("Min size of 'last_name' is 2. Max size of 'last_name' is 50.")
        }
        if (account.email.length < 4 || account.email.length > 50) {
            throw PreconditionException("Min size of 'email' is 4. Max size of 'email' is 50.")
        }
    }
}