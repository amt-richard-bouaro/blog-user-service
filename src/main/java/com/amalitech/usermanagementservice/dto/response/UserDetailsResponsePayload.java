package com.amalitech.usermanagementservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailsResponsePayload {

    private Long id;
    private String username;
    private String email;
    private boolean active;
    private String name;
    private String phone;
    private String picture;

    // Required tokens
    private String token;
}
