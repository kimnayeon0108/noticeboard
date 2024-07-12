# Spring Security 인증 게시판
👉 **사용 기술** 
> Spring Boot(3.1.0) </br>
> Spring Security(6.1.0) </br>
> Hibernate  
> QueryDSL

</br>

## ERD
![게시판erd drawio](https://github.com/user-attachments/assets/1ce9fc56-38d7-4f22-92b6-ded9d164aa49)
</br>
## 인증 처리 방식 
- 인증(Authentication)과 인가(Authorization)를 필터 단에서 진행한다.
- 원래 필터는 Spring Context에 속하지 않고 서블릿 컨테이너에 속한다.
  하지만 Spring Security를 사용하면 필터 단에서 스프링의 빈들도 사용할 수 있다. 
  인증과 인가를 필터 단에서 수행하고 인증된 유저의 요청만 Spring MVC로 넘길 수 있다.
</br>

## 게시판 Spring Security 구조 
 - 커스텀 필터 2개
    - CustomAuthenticationFilter : 로그인 수행하는 필터
    - JwtAuthenticationFilter : JWT를 파싱해서 유저 정보를 Security context에 저장하는 필터

### CustomAuthenticationFilter 동작 흐름 (로그인)

1. **CustomAuthenticationFilter** 
    
    로그인 request에서 email과 password를 UsernamePasswordAuthenticaionToken에 담아서 AuthenticationManager로 보낸다. 
    
2. **AuthenticationManager** 
    
    인증을 수행할 CustomAuthenticationProvider에게 인증을 위임한다.
    
3. **CustomAuthenticationProvider**
    
    CustomUserDetailsService에게 DB에서 유저를 조회하고 UserDetailsDto 객체를 받는다.
    
    비밀번호 검증 수행한다.
    
    UsernamePasswordAuthenticationToken에 UserDetailsDto 객체와 유저 정보들을 담아서 반환한다. 
    
4. (성공) **CustomAuthenticationSuccessHandler**
    
    UsernamePasswordAuthenticationToken에 있는 유저의 정보로 JWT를 발행하여 response로 반환한다.
    
5. (실패)  **CustomAuthenticationFailureHandler**
    
    인증 처리 중 발생한 AuthenticationException 타입의 예외를 처리한다.
    

### JwtAuthenticationFilter 동작 흐름 (JWT 복호화 인증)

1. **JwtAuthenticationFilter**
    
    Authorization 헤더에서 토큰을 추출하여 JwtAuthenticationToken에 담아서 AuthenticationManager에게 보내고
    
    manager는 AuthenticationProvider에게 위임한다.
    
2. **JwtAuthenticationProvider**
    
    JWT를 유효한지 검증하고 복호화하고 유저 정보를 추출하여 UserDetailsDto를 만든다.
    
    UsernamePasswordAuthenticationToken에 UserDetailsDto 객체와 유저 정보들을 담아서 반환한다. 
    
    토큰 복호화 성공 시 SecurityContext에 UsernamePasswordAuthenticationToken을 저장한다.
    
    → 컨트롤러에서 @AuthenticationPrincipal 어노테이션으로 인증된 유저의 UserDetailsDto를 얻어올 수 있다. 
    

1. (실패)  **CustomAuthenticationFailureHandler**
    
    JWT 처리 중 발생한 AuthenticationException 타입의 예외를 처리한다.
