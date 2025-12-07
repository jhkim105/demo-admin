package com.example.demo.controller

import com.example.demo.model.User
import com.example.demo.service.RoleService
import com.example.demo.service.UserService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val roles: Set<String>
) {
    companion object {
        fun from(user: User) = UserResponse(
            id = user.id,
            email = user.email,
            name = user.name,
            roles = user.roles.map { it.name }.toSet()
        )
    }
}

data class UserCreateRequest(
    @field:Email @field:NotBlank val email: String,
    @field:NotBlank val name: String,
    @field:Size(min = 6, message = "password must be at least 6 characters") val password: String,
    val roles: List<String> = listOf("ROLE_USER")
)

data class UserUpdateRequest(
    @field:Email @field:NotBlank val email: String,
    @field:NotBlank val name: String,
    val password: String? = null,
    val roles: List<String> = listOf("ROLE_USER")
)

data class PagedUsersResponse(
    val content: List<UserResponse>,
    val total: Long,
    val page: Int,
    val size: Int
)

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val roleService: RoleService
) {
    @GetMapping
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<PagedUsersResponse> {
        val (users, total) = userService.list(page, size)
        val response = PagedUsersResponse(
            content = users.map { UserResponse.from(it) },
            total = total,
            page = page,
            size = size
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(UserResponse.from(user))
    }

    @PostMapping
    fun create(@Valid @RequestBody request: UserCreateRequest): ResponseEntity<UserResponse> {
        val roles = roleService.ensureRoles(request.roles)
        val user = userService.createUser(
            email = request.email,
            name = request.name,
            rawPassword = request.password,
            roles = roles
        )
        return ResponseEntity.ok(UserResponse.from(user))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequest
    ): ResponseEntity<UserResponse> {
        val roles = roleService.ensureRoles(request.roles)
        return runCatching {
            val updated = userService.updateUser(
                id = id,
                email = request.email,
                name = request.name,
                rawPassword = request.password,
                roles = roles
            )
            ResponseEntity.ok(UserResponse.from(updated))
        }.getOrElse { throwable ->
            when (throwable) {
                is NoSuchElementException -> ResponseEntity.notFound().build()
                else -> throw throwable
            }
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return runCatching<ResponseEntity<Void>> {
            userService.deleteUser(id)
            ResponseEntity.noContent().build()
        }.getOrElse { throwable: Throwable ->
            when (throwable) {
                is NoSuchElementException -> ResponseEntity.notFound().build()
                else -> throw throwable
            }
        }
    }
}
