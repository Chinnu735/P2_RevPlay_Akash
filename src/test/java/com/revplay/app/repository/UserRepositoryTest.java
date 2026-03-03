package com.revplay.app.repository;

import com.revplay.app.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IUserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole("USER");
        user.setIsActive(1);
        user = em.persistAndFlush(user);
    }

    @Test
    void findByEmail_ShouldReturnUser() {
        Optional<User> found = userRepository.findByEmail("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenNotFound() {
        assertThat(userRepository.findByEmail("notfound@example.com")).isEmpty();
    }

    @Test
    void findByUsername_ShouldReturnUser() {
        Optional<User> found = userRepository.findByUsername("testuser");
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void existsByEmail_ShouldReturnTrue() {
        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse() {
        assertThat(userRepository.existsByEmail("other@example.com")).isFalse();
    }

    @Test
    void existsByUsername_ShouldReturnTrue() {
        assertThat(userRepository.existsByUsername("testuser")).isTrue();
    }

    @Test
    void existsByUsername_ShouldReturnFalse() {
        assertThat(userRepository.existsByUsername("other")).isFalse();
    }
}
