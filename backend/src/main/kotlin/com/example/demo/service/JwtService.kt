package com.example.demo.service

import com.example.demo.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${app.jwt.secret}") secret: String,
    @Value("\${app.jwt.expiration-minutes}") private val expirationMinutes: Long
) {
    private val signingKey: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun generateToken(user: User): String {
        val now = Instant.now()
        val expiry = now.plusSeconds(expirationMinutes * 60)
        return Jwts.builder()
            .subject(user.email)
            .claim("roles", user.roles.map { it.name })
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(signingKey, Jwts.SIG.HS256)
            .compact()
    }

    fun extractUsername(token: String): String? = extractAllClaims(token).subject

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean =
        extractAllClaims(token).expiration.before(Date.from(Instant.now()))

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).payload
}
