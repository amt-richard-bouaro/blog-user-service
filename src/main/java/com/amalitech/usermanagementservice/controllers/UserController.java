package com.amalitech.usermanagementservice.controllers;

import com.amalitech.usermanagementservice.dto.request.RegistrationRequestPayload;
import com.amalitech.usermanagementservice.dto.request.UpdatePasswordRequestPayload;
import com.amalitech.usermanagementservice.dto.request.UpdateProfileRequestPayload;
import com.amalitech.usermanagementservice.dto.response.UserDetailsResponsePayload;
import com.amalitech.usermanagementservice.features.UserFeatures;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@Tag(name = "User Management")
public class UserController {

    private final UserFeatures userFeatures;

    public UserController(UserFeatures userFeatures) {
        this.userFeatures = userFeatures;
    }

    /**
     * Register a new user in the system.
     *
     * @param payload RegistrationRequestPayload containing user details.
     * @return UserDetailsResponsePayload containing the created user details.
     */
    @Operation(
            summary = "Register a new user",
            description = "Allows a new user to register by providing username, password, email, name, and other details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailsResponsePayload.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflict - Username or email already exists",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponsePayload> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload containing registration details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RegistrationRequestPayload.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "username": "John",
                                                "name": "John Doe",
                                                "email": "john.doe@example.com",
                                                "password": "Password@123",
                                                "confirmPassword": "Password@123",
                                                "phone": "+131748832137"
                                            }
                                            """)
                    )
            )

            @Valid
            @RequestBody
            RegistrationRequestPayload payload
    ) {
        UserDetailsResponsePayload response = userFeatures.registerUser(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieve the profile details of the currently authenticated user.
     *
     * @return ResponseEntity containing the authenticated user's profile details.
     */
    @Operation(
            summary = "Get Authenticated User Profile",
            description = "Fetches the profile details of the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authenticated user's profile retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailsResponsePayload.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. The user is not authorized to perform this action.",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/profile")
    public ResponseEntity<UserDetailsResponsePayload> getAuthenticatedUser() {
        return ResponseEntity.ok(userFeatures.getAuthenticatedUserProfile());
    }


    /**
     * Update the authenticated user's profile.
     *
     * @param payload UpdateProfileRequestPayload containing updated user details.
     * @return UserDetailsResponsePayload containing the updated user details.
     */
    @Operation(
            summary = "Update User Profile",
            description = "Allows an authenticated user to update their profile details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailsResponsePayload.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Conflict: Username or Email already exists",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/profile")
    public ResponseEntity<UserDetailsResponsePayload> updateUserProfile(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload containing profile details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = RegistrationRequestPayload.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "username": "John",
                                                "name": "John Doe",
                                                "email": "john.doe@example.com",
                                                "phone": "+131748832137"
                                            }
                                            """)
                    )
            )
            @Valid @RequestBody UpdateProfileRequestPayload payload
    ) {
        UserDetailsResponsePayload updatedUser = userFeatures.updateUserProfile(payload);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }


    /**
     * Update the password for the currently authenticated user.
     *
     * @param payload The payload containing current password, new password, and confirm password.
     * @return ResponseEntity with a success message.
     */
    @Operation(
            summary = "Update Password",
            description = "Allows the currently authenticated user to update their password."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request - validation failed",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied - user is not authorized",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/change-password")
    public ResponseEntity<Void> updatePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload containing profile details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = UpdatePasswordRequestPayload.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "currentPassword": "Password@123",
                                                "newPassword": "NewPassword@123",
                                                "confirmPassword": "NewPassword@123"
                                            }
                                            """)
                    )
            )
            @Valid @RequestBody UpdatePasswordRequestPayload payload) {
        userFeatures.updateUserPassword(payload);
        return ResponseEntity.noContent().build();
    }



}
