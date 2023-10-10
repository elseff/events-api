package ru.danila.eventsapi.web.api.modules.admin.controller;

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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {

    AdminService adminService;

    @PostMapping("/change_role")
    @ResponseStatus(HttpStatus.OK)
    public AdminResponse changeRole(@RequestBody @Valid AdminChangeRoleRequest request){
        return adminService.changeRole(request);
    }
}
