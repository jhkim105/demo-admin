package com.example.demo.service

import com.example.demo.model.Role
import com.example.demo.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Service
class UserService(
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    private val users = ConcurrentHashMap<Long, User>()
    private val idSequence = AtomicLong(0)

    init {
        val adminRoles = roleService.ensureRoles(listOf("ROLE_ADMIN"))
        createUser(
            email = "admin@example.com",
            name = "Admin",
            rawPassword = "admin123",
            roles = adminRoles
        )
    }

    fun list(page: Int, size: Int): Pair<List<User>, Long> {
        val all = users.values.sortedBy { it.id }
        val total = all.size.toLong()
        val from = (page * size).coerceAtMost(all.size)
        val to = (from + size).coerceAtMost(all.size)
        return all.subList(from, to) to total
    }

    fun findById(id: Long): User? = users[id]

    fun findByEmail(email: String): User? = users.values.firstOrNull { it.email.equals(email, true) }

    fun createUser(email: String, name: String, rawPassword: String, roles: MutableSet<Role>): User {
        if (findByEmail(email) != null) throw IllegalArgumentException("Email already exists")
        val user = User(
            id = idSequence.incrementAndGet(),
            email = email,
            name = name,
            password = passwordEncoder.encode(rawPassword),
            roles = roles
        )
        users[user.id] = user
        return user
    }

    fun updateUser(id: Long, email: String, name: String, rawPassword: String?, roles: MutableSet<Role>): User {
        val existing = users[id] ?: throw NoSuchElementException("User not found")
        if (!existing.email.equals(email, true) && findByEmail(email) != null) {
            throw IllegalArgumentException("Email already exists")
        }
        existing.email = email
        existing.name = name
        if (!rawPassword.isNullOrBlank()) {
            existing.password = passwordEncoder.encode(rawPassword)
        }
        existing.roles = roles
        return existing
    }

    fun deleteUser(id: Long) {
        if (users.remove(id) == null) {
            throw NoSuchElementException("User not found")
        }
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = findByEmail(username) ?: throw UsernameNotFoundException("User not found")
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            mapAuthorities(user.roles)
        )
    }

    private fun mapAuthorities(roles: Set<Role>): Collection<GrantedAuthority> =
        roles.map { SimpleGrantedAuthority(it.name) }
}
