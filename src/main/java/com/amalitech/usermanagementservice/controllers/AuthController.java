package com.amalitech.usermanagementservice.controllers;


import com.amalitech.usermanagementservice.dto.request.LoginRequestPayload;
import com.amalitech.usermanagementservice.dto.request.PasswordResetRequestPayload;
import com.amalitech.usermanagementservice.dto.response.PasswordResetResponsePayload;
import com.amalitech.usermanagementservice.dto.response.UserDetailsResponsePayload;
import com.amalitech.usermanagementservice.features.AuthFeatures;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    private final AuthFeatures authFeatures;

    /**
     * Authenticate user in the system.
     *
     * @param payload LoginRequestPayload containing login credentials.
     * @return UserDetailsResponsePayload containing the created user details.
     */
    @Operation(summary = "Login", description = "Allows a user to login by providing username, password.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Login successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailsResponsePayload.class))), @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content(mediaType = "application/json")),})
    @PostMapping("/login")
    public ResponseEntity<UserDetailsResponsePayload> authenticateUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Payload containing login credentials", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequestPayload.class), examples = @ExampleObject(value = """
            {
                "username": "John",
                "password": "Password@123"
            }
            """)))

                                                                       @Valid @RequestBody LoginRequestPayload payload) {
        UserDetailsResponsePayload response = authFeatures.login(payload);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Logout", description = "Logout authenticated user.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Authenticated user logged out"), @ApiResponse(responseCode = "403", description = "Access denied. The user is not authorized to perform this action.", content = @Content(mediaType = "application/json"))})
    @GetMapping("/logout")
    public ResponseEntity<Void> getAuthenticatedUser() {
        authFeatures.logout();
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Reset User Password",
            description = "Resets the user's password and sends a new password to their registered email address."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset successfully. The new password has been emailed to the user."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error."
            )
    })
    @PatchMapping("/password-reset")
    public ResponseEntity<PasswordResetResponsePayload> resetPassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload containing username of user to reset their password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PasswordResetRequestPayload.class),
                            examples = @ExampleObject(value = """
                {
                    "username": "John"
                }
                """
                            )
                    )
            )
            @Valid @RequestBody PasswordResetRequestPayload payload
    ) {
        PasswordResetResponsePayload response = authFeatures.resetUserPassword(payload);
        return ResponseEntity.ok(response);
    }



}
