package ru.danila.eventsapi.web.api.modules.admin.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.danila.eventsapi.persistense.RoleEntity;
import ru.danila.eventsapi.persistense.UserEntity;
import ru.danila.eventsapi.persistense.dao.RoleRepository;
import ru.danila.eventsapi.persistense.dao.UserRepository;
import ru.danila.eventsapi.web.api.modules.admin.dto.AdminChangeRoleRequest;
import ru.danila.eventsapi.web.api.modules.admin.dto.AdminResponse;
import ru.danila.eventsapi.web.api.modules.user.service.UserService;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("Админ сервис")
@FieldDefaults(level = AccessLevel.PRIVATE)
class AdminServiceTest {

    @Mock
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Изменить роль пользователя")
    void changeRole() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.ofNullable(getUserEntity1()));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.ofNullable(getRoleUser()));
        when(userRepository.save(any(UserEntity.class))).thenReturn(getUserEntity1());

        AdminResponse response = adminService.changeRole(getAdminChangeRoleRequest());

        String expectedMessage = "Пользователю test дана роль ROLE_USER";
        String actualMessage = response.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);

        verify(userService, times(1)).findByUsername(anyString());
        verify(roleRepository, times(1)).findByName(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    @DisplayName("Изменить роль пользователя, если пользователь не найден")
    void changeRole_If_User_Is_Not_Found() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> adminService.changeRole(getAdminChangeRoleRequest()));

        String expectedExceptionMessage = "Пользователь не найден";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(userService, times(1)).findByUsername(anyString());
        verifyNoMoreInteractions(userService);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(roleRepository);
    }

    @Test
    @DisplayName("Изменить роль пользователя, если роль не найдена")
    void changeRole_If_Role_Is_Not_Found() {
        when(userService.findByUsername(anyString())).thenReturn(Optional.ofNullable(getUserEntity1()));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> adminService.changeRole(getAdminChangeRoleRequest()));

        String expectedExceptionMessage = "Не найдена роль с таким именем";
        String actualExceptionMessage = exception.getMessage();

        Assertions.assertEquals(expectedExceptionMessage, actualExceptionMessage);

        verify(userService, times(1)).findByUsername(anyString());
        verify(roleRepository, times(1)).findByName(anyString());
        verifyNoMoreInteractions(userService);
        verifyNoMoreInteractions(roleRepository);
        verifyNoMoreInteractions(userRepository);
    }

    private AdminChangeRoleRequest getAdminChangeRoleRequest() {
        return AdminChangeRoleRequest.builder()
                .roleName(getRoleUser().getName())
                .username("test")
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

}