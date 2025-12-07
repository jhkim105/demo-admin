package com.example.demo.service

import com.example.demo.model.User
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder
) {
    fun authenticate(email: String, password: String): String {
        val user: User = userService.findByEmail(email) ?: throw BadCredentialsException("Invalid credentials")
        if (!passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("Invalid credentials")
        }
        return jwtService.generateToken(user)
    }
}
