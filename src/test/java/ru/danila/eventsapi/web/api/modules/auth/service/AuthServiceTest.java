package ru.danila.eventsapi.web.api.modules.auth.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.danila.eventsapi.persistense.RoleEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.RoleRepository;
import ru.danila.eventsapi.persistense.dao.UserRepository;
import ru.danila.eventsapi.security.JwtProvider;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthLoginRequest;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthRegisterRequest;
import ru.danila.eventsapi.web.api.modules.auth.dto.AuthResponse;
import ru.danila.eventsapi.web.api.modules.auth.dto.assembler.AuthDtoAssembler;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Управление аутентификацией/авторизацией")
@FieldDefaults(level = AccessLevel.PRIVATE)
class AuthServiceTest {

    @Mock
    AuthDtoAssembler authDtoAssembler;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtProvider jwtProvider;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Регистрация")
    void register() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(roleRepository.findByName("ROLE_ORGANIZER")).thenReturn(Optional.ofNullable(getRoleOrganizer()));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.ofNullable(getRoleUser()));
        when(authDtoAssembler.mapAuthRegisterRequestToUserEntity(any(AuthRegisterRequest.class))).thenReturn(getUserEntity1());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(UserEntity.class))).thenReturn(getUserEntity1());
        when(jwtProvider.generateToken(anyString())).thenReturn("token");

        AuthResponse response = authService.register(getAuthRegisterRequest());

        String expectedToken = "token";
        String actualToken = response.getToken();
        String expectedUsername = "test";
        String actualUsername = response.getUsername();
        String expectedRoleName = "ROLE_ORGANIZER";
        String actualRoleName = response.getRole();

        Assertions.assertEquals(expectedToken, actualToken);
        Assertions.assertEquals(expectedUsername, actualUsername);
        Assertions.assertEquals(expectedRoleName, actualRoleName);

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(roleRepository, times(1)).findByName(anyString());
        verify(authDtoAssembler, times(1)).mapAuthRegisterRequestToUserEntity(any(AuthRegisterRequest.class));
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(jwtProvider, times(1)).generateToken(anyString());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(roleRepository);
        verifyNoMoreInteractions(authDtoAssembler);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(jwtProvider);
    }

    @Test
    @DisplayName("Регистрация, если ник занят")
    void register_If_Username_Already_Exists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(getUserEntity1()));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> authService.register(getAuthRegisterRequest()));

        String expectedExceptionMessage = "Пользователь с таким ником уже существует";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(authDtoAssembler);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtProvider);
    }

    @Test
    @DisplayName("Авторизация")
    void login() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(getUserEntity2()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtProvider.generateToken(anyString())).thenReturn("token");

        AuthResponse response = authService.login(getAuthLoginRequest());

        String expectedToken = "token";
        String actualToken = response.getToken();
        String expectedUsername = "test";
        String actualUsername = response.getUsername();

        Assertions.assertEquals(expectedToken, actualToken);
        Assertions.assertEquals(expectedUsername, actualUsername);

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verify(jwtProvider, times(1)).generateToken(anyString());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoMoreInteractions(jwtProvider);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(authDtoAssembler);
    }

    @Test
    @DisplayName("Авторизация, если пользователь не найден")
    void login_If_User_Not_Found() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> authService.login(getAuthLoginRequest()));

        String expectedExceptionMessage = "Пользователь с таким ником не найден";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(userRepository, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(authDtoAssembler);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtProvider);
    }

    @Test
    @DisplayName("Авторизация, если неверный пароль")
    void login_If_Password_Is_Incorrect() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(getUserEntity2()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> authService.login(getAuthLoginRequest()));

        String expectedExceptionMessage = "Неверный пароль!";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(userRepository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(authDtoAssembler);
        verifyNoInteractions(jwtProvider);
    }

    private AuthLoginRequest getAuthLoginRequest() {
        return AuthLoginRequest.builder()
                .username("test")
                .password("test")
                .build();
    }

    private RoleEntity getRoleOrganizer() {
        return RoleEntity.builder()
                .id(1)
                .name("ROLE_ORGANIZER")
                .build();
    }

    private RoleEntity getRoleUser() {
        return RoleEntity.builder()
                .id(1)
                .name("ROLE_USER")
                .build();
    }

    private UserEntity getUserEntity1() {
        return UserEntity.builder()
                .id(1L)
                .firstName("test")
                .lastName("test")
                .username("test")
                .password("test")
                .roles(new HashSet<>())
                .build();
    }

    private UserEntity getUserEntity2() {
        UserEntity user = getUserEntity1();
        user.getRoles().add(getRoleUser());
        return user;
    }

    private AuthRegisterRequest getAuthRegisterRequest() {
        return AuthRegisterRequest.builder()
                .firstName("test")
                .lastName("test")
                .username("test")
                .password("test")
                .isOrganizer(true)
                .build();
    }
}