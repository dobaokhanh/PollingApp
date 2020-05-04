# Polling Application
Polling Application is an application where we could create polls for other users to vote

## Table of contents
* [Technologies](#Technologies)
* [Explainations](#Explaination)
* [Status](#Status)

## Technologies
* Java
* Spring Boot
* Spring Security
* JWT

## Explaination
  * ### Security
    * #### Overview
      * Build an API that registers new users with their name, username, email and password
      * Build an API to let users login using their username/email and password. After validating user's credential, the API should generate JWT authentication token and return the token in the response.
      * Configure security to restrict access to protected resources
      * Configure security to throw 401 unauthorized error if a client tries to access protected resources.
      * Configure Role-based Authorization to protect resources on the sever.
    * #### Implementation
      * **SecurityConfig**:
        * @**EnableWebSecurity**: enable Spring Security's web security support and provide the Spring MVC intergration
        * @**EnableGlobalMethodSecurity**: enable method level security based on annotation
          * @**securedEnabled**: determine if the annotation @Secured annotation should be enable
          * @**jsr250Enabled**: enable the @RolesAllowed annotation
          * @**prePostEnabled**: enable more complex expression based access control syntax with @PreAuthorized and @PostAuthorized annotations
        * **WebSecurityConfigureAdapter**: Provides default security configurations and allows other classes to extend it and customize the security configuration by overrding its methods.
        * **CustomerUserDetailService**: authenticates a User or perform various role-based checks. This class implements UserDetailService interface that provides the implementation for `loadUserByUserName()` method.
        * **JwtAuthenticationEntryPoint**: return a 401 unauthorized error to clients that try to access protected resource without proper authentication. 
        * **JwtAuthenticationFilter**: implements a filter that:
          * reads JWT authentication token from the `Àuthorization` header of all requests
          * validates the JWT token
          * loads the user details associated with that token
          * Set the user details in Spring Security's `SecurityContext`. Spring Security uses the user details to perform authorization checks.
        * **AuthenticationManagerBuilder and AuthenticationManager**: `AuthenticationManagerBuilder` is used to created an `AuthenticationManager` instance which is the main Spring Security interface for authenticating user. `AuthenticationManager` is used to authenticate a user in login API
        * **HttpSecurity configurations**: `HttpSecurity` configurations are used to configure security functionalities like `csrf`, `sessionManagement`, and add the rules to protect resources.
      * **WebMvcConfig**: enable CORS (cross origin requests) because we gonna build React client, so in order to access API from React client, we have to enable CORS 
    * **Auditing**:
     * @**EnableJpaAuditing**
     * @**JsonIgnoreProperties**: ignores the specified local properties in JSON serialization and deserialization
  * ### REST APIs:
    * **Model Mapper**: Maps the `Poll` entity to a `PollResponse` payload. All these information will be used in front-end for presentation
    * **Poll Controller**:
      * Create a poll
      * Get a paginated list of polls sorted by their creation time
      * Get a Poll by pollId
      * Vote in a poll
    * **User Controller**;
      * Get the current logged in user
      * Check if a username is available for registration
      * Check if an email is avaliable for registration
      * Get the public profile of a user
      * Get a paginated list of polls created by a given user
      * Get a paginated list of polls in which a given user has voted
## Status
Pending. Maybe continuing after 1 months. 
