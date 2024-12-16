package ua.nure.arkpz.task2.flameguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.entity.UserAccount;
import ua.nure.arkpz.task2.flameguard.service.UserAccountService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
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

    @PostMapping
    public ResponseEntity<UserAccount> createUserAccount(@RequestBody UserAccount userAccount) {
        return ResponseEntity.ok(userAccountService.createUserAccount(userAccount));
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

    @PostMapping("/login")
    public ResponseEntity<UserAccount> loginUser(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(userAccountService.authorizeUser(email, password));
    }
}
