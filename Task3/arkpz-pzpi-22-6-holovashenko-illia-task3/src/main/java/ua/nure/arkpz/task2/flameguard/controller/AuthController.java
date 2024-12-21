package ua.nure.arkpz.task2.flameguard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.entity.UserAccount;
import ua.nure.arkpz.task2.flameguard.service.UserAccountService;
import ua.nure.arkpz.task2.flameguard.util.JWTUtil;

/**
 * REST controller for authorisation management.
 * Provides endpoints for user authentication and registration.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Registers a new user account and generates a JWT token.
     *
     * @param userAccount The user account details for registration.
     * @return A response containing the generated JWT token or an error message.
     */
    @Operation(summary = "Register a new user account", description = "Registers a new user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid user account details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"error\": \"Invalid user data\"}")))
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserAccount userAccount) {
        try {
            UserAccount user = userAccountService.registerUserAccount(userAccount);
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{ \"token\":\"" + token + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{ \"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Logs in a user and generates a JWT token.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @return A response containing the generated JWT token or an error message.
     */
    @Operation(summary = "Login a user", description = "Authenticates a user and returns a JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid email or password",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{ \"error\": \"Invalid credentials\"}")))
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        try {
            UserAccount user = userAccountService.loginUser(email, password);
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok("{ \"token\":\"" + token + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}