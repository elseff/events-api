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
import org.springframework.security.core.userdetails.UserDetails;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.UserRepository;
import ru.danila.eventsapi.security.UserDetailsImpl;
import ru.danila.eventsapi.web.api.modules.auth.service.AuthService;

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

    @Mock
    AuthService authService;

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
        verifyNoInteractions(authService);
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
        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("Текущий пользователь")
    void getMe() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(getUser()));
        when(authService.getCurrentAuthUser()).thenReturn(getUserDetails());
        UserEntity me = userService.getMe();

        Assertions.assertNotNull(me);

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(authService, times(1)).getCurrentAuthUser();
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(authService);
    }

    private UserEntity getUser() {
        return UserEntity.builder()
                .firstName("test")
                .lastName("test")
                .username(getUserDetails().getUsername())
                .password(getUserDetails().getPassword())
                .build();
    }

    private UserDetails getUserDetails() {
        return UserDetailsImpl.builder()
                .username("test")
                .password("test")
                .build();
    }
}