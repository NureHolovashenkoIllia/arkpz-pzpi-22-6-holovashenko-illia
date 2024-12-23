package ua.nure.arkpz.task2.flameguard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "User_account", uniqueConstraints = {
        @UniqueConstraint(columnNames = "Phone_number"),
        @UniqueConstraint(columnNames = "Email")
})
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_account_id", nullable = false)
    private Integer userAccountId;

    @Size(max = 50)
    @Column(name = "First_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "Last_name", length = 50)
    private String lastName;

    @NotNull
    @Size(max = 15)
    @Column(name = "Phone_number", length = 15, nullable = false, unique = true)
    private String phoneNumber;

    @NotNull
    @Size(max = 100)
    @Column(name = "Email", length = 100, nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(max = 150)
    @Column(name = "User_password", length = 150, nullable = false)
    private String userPassword;

    @NotNull
    @Size(max = 20)
    @Column(name = "User_role", length = 20, nullable = false)
    private String userRole = "Customer";

    // Getters and Setters
    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @PrePersist
    public void validateUserRole() {
        if (!userRole.matches("Customer|System_Administrator|Database_Administrator|Global_Administrator")) {
            throw new IllegalArgumentException("Invalid user role: " + userRole);
        }
    }
}
