package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.entity.UserAccount;
import ua.nure.arkpz.task2.flameguard.service.UserAccountService;
import ua.nure.arkpz.task2.flameguard.util.JWTUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JWTUtil jwtUtil;

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