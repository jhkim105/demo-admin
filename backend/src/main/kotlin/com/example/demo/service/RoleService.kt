package com.example.demo.service

import com.example.demo.model.Role
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@Service
class RoleService {
    private val roles = ConcurrentHashMap<Long, Role>()
    private val idSequence = AtomicLong(0)

    init {
        createRoleIfMissing("ROLE_ADMIN")
        createRoleIfMissing("ROLE_USER")
    }

    fun findAll(): List<Role> = roles.values.sortedBy { it.id }

    fun findByName(name: String): Role? = roles.values.firstOrNull { it.name == name }

    fun findById(id: Long): Role? = roles[id]

    fun ensureRoles(names: Collection<String>): MutableSet<Role> =
        names.map { findByName(it) ?: createRoleIfMissing(it) }.toMutableSet()

    private fun createRoleIfMissing(name: String): Role {
        val existing = findByName(name)
        if (existing != null) return existing
        val role = Role(idSequence.incrementAndGet(), name)
        roles[role.id] = role
        return role
    }
}
