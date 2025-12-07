package com.example.demo.controller

import com.example.demo.model.Role
import com.example.demo.service.RoleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/roles")
class RoleController(
    private val roleService: RoleService
) {
    @GetMapping
    fun list(): ResponseEntity<List<Role>> = ResponseEntity.ok(roleService.findAll())
}
