package com.jobtrack.repository;

import com.jobtrack.entity.User;
import com.jobtrack.entity.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and retrieve User by username and email")
    void testSaveAndFindByUsernameAndEmail() {
        User user = User.builder()
                .username("david_smith")
                .email("david@example.com")
                .password("securehash123")
                .fullName("David Smith")
                .role(Role.ROLE_USER)
                .build();

        User saved = userRepository.save(user);
        assertNotNull(saved.getId());

        Optional<User> byUsername = userRepository.findByUsername("david_smith");
        assertTrue(byUsername.isPresent());
        assertEquals("David Smith", byUsername.get().getFullName());

        Optional<User> byEmail = userRepository.findByEmail("david@example.com");
        assertTrue(byEmail.isPresent());

        Optional<User> byUsernameOrEmail = userRepository.findByUsernameOrEmail("david@example.com", "david@example.com");
        assertTrue(byUsernameOrEmail.isPresent());

        assertTrue(userRepository.existsByUsername("david_smith"));
        assertTrue(userRepository.existsByEmail("david@example.com"));
        assertFalse(userRepository.existsByUsername("non_existent"));
    }
}
