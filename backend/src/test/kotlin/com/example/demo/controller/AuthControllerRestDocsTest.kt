package com.example.demo.controller

import com.example.demo.service.AuthService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class, MockitoExtension::class)
class AuthControllerRestDocsTest {

    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()
    private val authService: AuthService = mock<AuthService>()

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.standaloneSetup(AuthController(authService))
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    fun login_documentsAuthenticationEndpoint() {
        val email = "admin@example.com"
        val password = "admin123"
        given(authService.authenticate(email, password)).willReturn("jwt-token")

        val request = LoginRequest(email = email, password = password)

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.token").value("jwt-token"))
            .andDo(
                document(
                    "auth-login",
                    requestFields(
                        fieldWithPath("email").description("사용자 이메일"),
                        fieldWithPath("password").description("사용자 비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("token").description("발급된 JWT 액세스 토큰")
                    )
                )
            )
    }
}
