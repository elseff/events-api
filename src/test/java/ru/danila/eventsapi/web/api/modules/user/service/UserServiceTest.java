package ru.danila.eventsapi.web.api.modules.user.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@DisplayName("Сервис по управлению пользователями")
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Поиск пользователя по username")
    void findByUsername() {
        when(userService.findByUsername(anyString()))
                .thenReturn(Optional.of(getUser()));

        Optional<UserEntity> userOptional = userService.findByUsername("test");

        Assertions.assertTrue(userOptional.isPresent());

        String expectedUsername = getUser().getUsername();
        String actualUsername = userOptional.get().getUsername();

        Assertions.assertEquals(expectedUsername, actualUsername);
        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Поиск пользователя по username, если такого не существует")
    void findByUsername_If_User_Is_Not_Found() {
        when(userService.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        Optional<UserEntity> userOptional = userService.findByUsername("test");

        Assertions.assertTrue(userOptional.isEmpty());
        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    private UserEntity getUser() {
        return UserEntity.builder()
                .firstName("test")
                .lastName("test")
                .username("test")
                .password("test")
                .build();
    }
}