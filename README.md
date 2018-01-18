# springboot-oidc-openam

Example project illustrating:

- how to implement a simple REST services with Springboot
- how to protect REST services using Spring Security (included in Spring Boot) with OpenID Connect and OpenAM as Single-Sign On provider

# Using the project:

1. Import in Eclipse as maven project
2. Fire-up and configure OpenAM with OAuth2/OpenID Connect Agent
3. Run As Java Application the class com.lotzy.sample.SocialApplication.java
4. URL for the rest endpoints:
  [http://localhost:8080/springboot-oidc-openam/greet](http://localhost:8080/springboot-oidc-openam/greet) or
  [http://localhost:8080/springboot-oidc-openam/user](http://localhost:8080/springboot-oidc-openam/user)
