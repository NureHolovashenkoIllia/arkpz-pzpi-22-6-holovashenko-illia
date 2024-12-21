package ua.nure.arkpz.task2.flameguard.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.entity.UserAccount;
import ua.nure.arkpz.task2.flameguard.service.UserAccountService;

import java.util.List;

/**
 * REST controller for managing users.
 * Provides endpoints for CRUD operations on users.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserAccountController {

    @Autowired
    public UserAccountService userAccountService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public List<UserAccount> getAllUserAccounts() {
        return userAccountService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getUserAccountById(@PathVariable Integer id) {
        return ResponseEntity.ok(userAccountService.getUserAccountById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> updateUserAccount(@PathVariable Integer id,
                                                         @RequestBody UserAccount updatedUserAccount) {
        return ResponseEntity.ok(userAccountService.updateUserAccount(id, updatedUserAccount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable Integer id) {
        userAccountService.deleteUserAccount(id);
        return ResponseEntity.noContent().build();
    }
}
