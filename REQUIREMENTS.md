# Reuqirements
- 인증/로그인
  - 이메일(or 아이디) + 비밀번호 로그인
  - JWT 기반 토큰 

- 사용자 관리
  - 사용자 목록 조회, 페이징
  - 사용자 생성/수정/삭제
- 권한(역할) 관리
  - ROLE_ADMIN, ROLE_USER
  - 나중에 권한(Privilege)까지 세분화 예정 (예: USER_READ, USER_WRITE)

# Structures
- FE 와 BE 분리 개발
  - /backend : Spring Boot + Kotlin
  - /frontend: React (Vite + TypeScript 추천)

# BackEnd

## Domain Model
- User
- Role
- UserRole

## API
- POST /api/auth/login
- GET /api/users
- GET /api/users/{id}
- POST /api/users : 사용자 생성
- PUT /api/users/{id} : 사용자 수정
- DELETE /api/users/{id} : 사용자 삭제
- GET /api/roles : 역할 목록

## API Documentation
- RestDoc & openapi3

# FE
- 로그인 페이지
- 사용자 목록 페이지
- 사용자 생성/수정 페이지