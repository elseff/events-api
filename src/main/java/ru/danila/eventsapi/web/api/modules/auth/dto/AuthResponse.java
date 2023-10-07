package ru.danila.eventsapi.web.api.modules.auth.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {

    Timestamp registerAt;

    String username;

    String token;

    String role;
}
