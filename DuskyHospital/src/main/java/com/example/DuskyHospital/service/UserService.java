package com.example.DuskyHospital.service;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.DuskyHospital.entity.Patient;
import com.example.DuskyHospital.entity.User;
import com.example.DuskyHospital.repository.PatientRepo;
import com.example.DuskyHospital.repository.UserRepo;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PatientRepo patientRepo;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Try finding in UserRepo
        User user = userRepo.findByUsername(usernameOrEmail)
                .orElse(null);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles() != null
                            ? user.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet())
                            : Collections.emptySet()
            );
        }

        // Try finding in PatientRepo
        Patient patient = patientRepo.findByEmail(usernameOrEmail)
                .orElse(null);

        if (patient != null) {
            return new org.springframework.security.core.userdetails.User(
                    patient.getEmail(),
                    patient.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority(patient.getRole()))  // e.g., ROLE_PATIENT
            );
        }

        // If neither found
        throw new UsernameNotFoundException("No user found with username or email: " + usernameOrEmail);
    }

    // Registration method
    public User registerUser(String username, String rawPassword) {
        // Encode the password using BCrypt
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Create a new User object
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        // Save user to the database
        return userRepo.save(user);
    }


    // User login method
    public boolean loginUser(String username, String rawPassword) {
        // Find the user by username
        System.out.println("Attempting login for username: " + username);

        Optional<User> user = userRepo.findByUsername(username);

        if (user.isEmpty()) {
            // User not found, invalid credentials
            return false;
        }


        // Log the hashed password stored in the database and the raw password
        System.out.println("Stored hashed password: " + user.get().getPassword());

        // Check if the raw password matches the encoded password in the database
        boolean isPasswordValid = passwordEncoder.matches(rawPassword, user.get().getPassword());

        if (isPasswordValid) {
            // Password is correct, allow login
            return true;
        } else {
            // Password is incorrect
            return false;
        }
    }
}
