package com.blog.app.project.blogappapi.controller;

import com.blog.app.project.blogappapi.Security.JwtHelper;
import com.blog.app.project.blogappapi.dto.JwtRequest;
import com.blog.app.project.blogappapi.dto.JwtResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication related requests.
 *
 * @author Jayesh
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    /**
     * Service for retrieving user details.
     */
    @Autowired
    private UserDetailsService userDetailsService;
    /**
     * Manager for handling authentication.
     */
    @Autowired
    private AuthenticationManager manager;
    /**
     * Helper for generating JWT tokens.
     */
    @Autowired
    private JwtHelper helper;
    /**
     * Logger for logging information.
     */
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * Handles login requests.
     *
     * @param request The request containing email and password.
     * @return A ResponseEntity containing the JWT token and user name.
     */

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {


        logger.info("Request: {}" +request);

        this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userName(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Authenticates the user.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @throws BadCredentialsException If the credentials are invalid.
     */

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    /**
     * Handles BadCredentialsException.
     *
     * @return A string message indicating that the credentials are invalid.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid!!";
}

}
