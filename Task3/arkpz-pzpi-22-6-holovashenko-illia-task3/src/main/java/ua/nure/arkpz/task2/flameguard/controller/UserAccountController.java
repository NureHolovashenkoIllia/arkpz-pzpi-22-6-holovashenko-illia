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
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * Retrieve all user accounts.
     *
     * @return List of all user accounts.
     */
    @Operation(summary = "Get all users", description = "Retrieve a list of all user accounts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user accounts.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAccount.class)))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @GetMapping
    public List<UserAccount> getAllUserAccounts() {
        return userAccountService.getAllUsers();
    }

    /**
     * Retrieve a user account by ID.
     *
     * @param id ID of the user account to retrieve.
     * @return UserAccount object if found, or error message if not found.
     */
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user account by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAccount.class))),
            @ApiResponse(responseCode = "404", description = "User account not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No user account found with specified id\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserAccountById(@PathVariable Integer id) {
        try {
            UserAccount userAccount = userAccountService.getUserAccountById(id);
            return ResponseEntity.ok(userAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Update user account details.
     *
     * @param id                ID of the user account to update.
     * @param updatedUserAccount Updated UserAccount object.
     * @return Updated UserAccount object or error message if update fails.
     */
    @Operation(summary = "Update user account", description = "Update the details of an existing user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAccount.class))),
            @ApiResponse(responseCode = "404", description = "User account not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No user account found with specified id\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserAccount(@PathVariable Integer id,
                                               @RequestBody UserAccount updatedUserAccount) {
        try {
            UserAccount updatedUser = userAccountService.updateUserAccount(id, updatedUserAccount);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Delete a user account by ID.
     *
     * @param id ID of the user account to delete.
     * @return HTTP status indicating the result of the operation.
     */
    @Operation(summary = "Delete user account", description = "Delete a user account by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User account deleted successfully."),
            @ApiResponse(responseCode = "404", description = "User account not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No user account found with specified id\"}")))
    })
    @PreAuthorize("hasAnyAuthority('Global_Administrator')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserAccount(@PathVariable Integer id) {
        try {
            userAccountService.deleteUserAccount(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Update the role of a user account.
     *
     * @param id   ID of the user account to update.
     * @param role New role for the user account.
     * @return Updated UserAccount object or error message if update fails.
     */
    @Operation(summary = "Update user account role", description = "Change the role of an existing user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAccount.class))),
            @ApiResponse(responseCode = "404", description = "User account not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No user account found with specified id\"}")))
    })
    @PreAuthorize("hasAnyAuthority('Global_Administrator')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateUserAccountRole(@PathVariable Integer id, @RequestParam String role) {
        try {
            UserAccount updatedUser = userAccountService.updateUserRole(id, role);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Reset the role of a user account to the default.
     *
     * @param id ID of the user account.
     * @return Updated UserAccount object with the default role, or error message if operation fails.
     */
    @Operation(summary = "Reset user role to default", description = "Set the role of a user account to its default value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role reset successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAccount.class))),
            @ApiResponse(responseCode = "404", description = "User account not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No user account found with specified id\"}")))
    })
    @PreAuthorize("hasAuthority('Global_Administrator')")
    @DeleteMapping("/{id}/role")
    public ResponseEntity<?> setDefaultUserAccountRole(@PathVariable Integer id) {
        try {
            UserAccount updatedUser = userAccountService.setDefaultUserRole(id);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
