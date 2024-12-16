package ua.nure.arkpz.task2.flameguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.arkpz.task2.flameguard.entity.UserAccount;
import ua.nure.arkpz.task2.flameguard.repository.UserAccountRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    public UserAccount getUserAccountById(Integer id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public UserAccount createUserAccount(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    public UserAccount updateUserAccount(Integer id, UserAccount userAccount) {
        UserAccount existingUserAccount = getUserAccountById(id);
        existingUserAccount.setFirstName(userAccount.getFirstName());
        existingUserAccount.setLastName(userAccount.getLastName());
        existingUserAccount.setPhoneNumber(userAccount.getPhoneNumber());
        existingUserAccount.setEmail(userAccount.getEmail());
        existingUserAccount.setUserPassword(userAccount.getUserPassword());
        existingUserAccount.setUserRole(userAccount.getUserRole());
        return userAccountRepository.save(existingUserAccount);
    }

    public void deleteUserAccount(Integer id) {
        UserAccount userAccount = getUserAccountById(id);
        userAccountRepository.delete(userAccount);
    }

    public UserAccount authorizeUser(String email, String password) {
        Optional<UserAccount> user = userAccountRepository.findByEmail(email);
        if (user.isPresent() && user.get().getUserPassword().equals(password)) {
            return user.get();
        } else {
            throw new IllegalArgumentException("Invalid email or password.");
        }
    }
}
