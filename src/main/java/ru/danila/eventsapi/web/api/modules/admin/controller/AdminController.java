package ru.danila.eventsapi.web.api.modules.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.danila.eventsapi.web.api.modules.admin.dto.AdminChangeRoleRequest;
import ru.danila.eventsapi.web.api.modules.admin.dto.AdminResponse;
import ru.danila.eventsapi.web.api.modules.admin.service.AdminService;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@SecurityRequirement(name = "Bearer Authentication")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Admin controller", description = "Панель администратора")
public class AdminController {

    AdminService adminService;

    @Operation(
            method = "POST",
            summary = "Изменить роль пользователя",
            description = "Изменить роль пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Роль пользователя успешно изменена",
                            content = @Content(
                                    schema = @Schema(implementation = AdminResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/change_role")
    @ResponseStatus(HttpStatus.OK)
    public AdminResponse changeRole(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Необходимые параметры для смены роли пользоввателя",
            required = true,
            content = @Content(schema = @Schema(implementation = AdminChangeRoleRequest.class)))
                                    @RequestBody @Valid AdminChangeRoleRequest request) {
        return adminService.changeRole(request);
    }
}
