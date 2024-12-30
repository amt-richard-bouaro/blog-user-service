package com.amalitech.usermanagementservice.services;

import com.amalitech.usermanagementservice.dto.request.RegistrationRequestPayload;
import com.amalitech.usermanagementservice.exceptions.ConflictException;
import com.amalitech.usermanagementservice.exceptions.ForbiddenException;
import com.amalitech.usermanagementservice.exceptions.InternalServerErrorException;
import com.amalitech.usermanagementservice.exceptions.NotFoundException;
import com.amalitech.usermanagementservice.model.User;
import com.amalitech.usermanagementservice.repository.UserRepository;
import com.amalitech.usermanagementservice.services.impl.UserServiceImpl;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Epic("Service implementations")
@Feature("User Service Tests")
class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static User mockUser;

    @BeforeAll
    static void beforeAll() {
       mockUser = User.builder()
               .id(101L)
               .name("Test User")
                .username("testuser")
               .email("test@example.com")
               .active(false)
               .build();
    }


    @Test
    @DisplayName("Ensure username or email is not already taken")
    @Description("Throws a ConflictException when the username or email already exists in the database.")
    void ensureUsernameOrEmailNotTaken_ShouldThrowConflictException_WhenUsernameOrEmailExists() {
        String username = "testuser";
        String email = "test@example.com";

        when(userRepository.findByUsernameOrEmail(username, email)).thenReturn(Optional.of(mockUser));

        ConflictException exception = assertThrowsExactly(ConflictException.class,
                () -> userService.ensureUsernameOrEmailNotTaken(username, email));
        assertEquals("Username already in use", exception.getMessage());
    }


    @Test
    @DisplayName("Ensure username or email is not already taken")
    @Description("Does not throw a ConflictException when the username or email does exists in the database.")
    void ensureUsernameOrEmailNotTaken_ShouldNotThrowException_WhenUsernameOrEmailDoesNotExist() {
        String username = "testuser";
        String email = "test@example.com";

        when(userRepository.findByUsernameOrEmail(username, email)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.ensureUsernameOrEmailNotTaken(username, email));
    }

    @Test
    @DisplayName("Retrieve an existing user by username")
    @Description("Verifies that getUserByUsername retrieves the user when the username exists.")
    void getUserByUsername_ShouldReturnUser_WhenUsernameExists() {

        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Throw exception for non-existing user by username")
    @Description("Verifies that getUserByUsername throws a NotFoundException when the username does not exist.")
    void getUserByUsername_ShouldThrowNotFoundException_WhenUsernameDoesNotExist() {

        String username = "nonexistentuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUserByUsername(username));

        assertEquals("User with username '" + username + "' not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);

    }

    @Test
    @DisplayName("Retrieve an existing user by ID")
    @Description("Verifies that getUserById retrieves the user when the ID exists.")
    void getUserById_ShouldReturnUser_WhenIdExists() {
        Long id = 101L;

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Throw exception for non-existing user by ID")
    @Description("Verifies that getUserById throws a NotFoundException when the ID does not exist.")
    void getUserById_ShouldThrowNotFoundException_WhenIdDoesNotExist() {

        Long id = 99L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUserById(id));

        assertEquals("User with id '" + id + "' not found", exception.getMessage());
        verify(userRepository, times(1)).findById(id);

    }


    @Test
    @DisplayName("Check for existing username and email")
    @Description("Verifies that no exception is thrown when both username and email do not exist.")
    void checkThatEmailAndUsernameDoesNotExist_ShouldNotThrowException_WhenNeitherExists() {

        String username = "newuser";
        String email = "newuser@example.com";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertDoesNotThrow(() -> userService.checkThatEmailAndUsernameDoesNotExist(username, email));
        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("Check for existing username and email")
    @Description("Verifies that ConflictException is thrown when the username already exists.")
    void checkThatEmailAndUsernameDoesNotExist_ShouldThrowConflictException_WhenUsernameExists() {

        String username = "existinguser";
        String email = "newuser@example.com";

        when(userRepository.existsByUsername(username)).thenReturn(true);


        ConflictException exception = assertThrows(ConflictException.class,
                () -> userService.checkThatEmailAndUsernameDoesNotExist(username, email));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, never()).existsByEmail(email);
    }

    @Test
    @DisplayName("Check for existing username and email")
    @Description("Verifies that ConflictException is thrown when the email already exists.")
    void checkThatEmailAndUsernameDoesNotExist_ShouldThrowConflictException_WhenEmailExists() {
        String username = "newuser";
        String email = "existingemail@example.com";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class,
                () -> userService.checkThatEmailAndUsernameDoesNotExist(username, email));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("Create user object with default avatar")
    @Description("Ensures that a new user object is created with a default avatar URL.")
    void createUserObjectWithDefaultAvatar_ShouldReturnUserWithAvatar() {
        RegistrationRequestPayload payload = new RegistrationRequestPayload(
                "testuser",
                "password",
                "password",
                "Test User",
                "test@example.com",
                "+233383938034"
                );

        when(passwordEncoder.encode(payload.password())).thenReturn("encodedPassword");

        User user = userService.createUserObjectWithDefaultAvatar(payload);

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.isActive());
        assertTrue(user.getPicture().contains("https://eu.ui-avatars.com/api/"));
    }

    @Test
    @DisplayName("Activate user account")
    @Description("Sets a user's active status to true and saves the user.")
    void activateUser_ShouldSetUserToActive() {
        mockUser.setActive(false);

        when(userRepository.save(mockUser)).thenReturn(mockUser);
        userService.activateUser(mockUser);

        assertTrue(mockUser.isActive());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    @DisplayName("Save user with database failure")
    @Description("Throws InternalServerErrorException when saving the user fails due to database errors.")
    void saveUser_ShouldThrowInternalServerErrorException_OnSaveFailure() {

        when(userRepository.save(mockUser)).thenThrow(new RuntimeException("Database error"));

        assertThrowsExactly(InternalServerErrorException.class, () -> userService.saveUser(mockUser));
    }


    @Test
    @DisplayName("Verify current password")
    @Description("Throws ForbiddenException when the provided password does not match the current password.")
    void verifyCurrentPassword_ShouldThrowForbiddenException_WhenPasswordDoesNotMatch() {
        String existingPassword = "encodedPassword";
        String inputPassword = "wrongPassword";

        when(passwordEncoder.matches(inputPassword, existingPassword)).thenReturn(false);

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> userService.verifyCurrentPassword(existingPassword, inputPassword));
        assertEquals("Password incorrect", exception.getMessage());
    }

    @Test
    @DisplayName("Update user password")
    @Description("Saves a user with the newly encoded password.")
    void updatePassword_ShouldSaveUserWithEncodedPassword() {
        String newPassword = "newPassword";

        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        userService.updatePassword(mockUser, newPassword);

        assertEquals("encodedNewPassword", mockUser.getPassword());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    @DisplayName("Check if username is taken")
    @Description("Returns true if the username exists in the database.")
    void isUsernameTaken_ShouldReturnTrue_WhenUsernameExists() {
        String username = "existingUser";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertTrue(userService.isUsernameTaken(username));
    }

    @Test
    @DisplayName("Check if email is already in use")
    @Description("Returns true if the email exists in the database.")
    void isEmailAlreadyInUse_ShouldReturnTrue_WhenEmailExists() {
        String email = "existing@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertTrue(userService.isEmailAlreadyInUse(email));
    }

    @Test
    @DisplayName("Get paginated users")
    @Description("Returns a paginated list of users from the database.")
    void getUsers_ShouldReturnPaginatedUsers() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<User> mockUsers = List.of(mockUser);
        Page<User> mockPage = new PageImpl<>(mockUsers, pageable, mockUsers.size());

        when(userRepository.findAll(pageable)).thenReturn(mockPage);

        Page<User> users = userService.getUsers(pageable, null, null);

        assertNotNull(users);
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Handles unsorted pageable")
    @Description("When pageable is unsorted, it should apply a default sort by 'createdAt' descending.")
    @Story("Fetching Users")
    void getUsers_ShouldApplyDefaultSort_WhenPageableIsUnsorted() {
        Pageable unsortedPageable = PageRequest.of(0, 10, Sort.unsorted());
        List<User> mockUsers = List.of(mockUser);
        Page<User> mockPage = new PageImpl<>(mockUsers, unsortedPageable, mockUsers.size());

        when(userRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<User> users = userService.getUsers(unsortedPageable, null, null);

        assertNotNull(users);
        assertEquals(mockUsers.size(), users.getContent().size());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Get users with search term")
    @Description("Returns a filtered list of users when a search term is provided.")
    @Story("Fetching Users")
    void getUsers_ShouldReturnFilteredUsers_WhenSearchTermIsProvided() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        String searchTerm = "testuser";
        String status = null;
        List<User> mockUsers = List.of(mockUser);
        Page<User> mockPage = new PageImpl<>(mockUsers, pageable, mockUsers.size());

        when(userRepository.findByWithSearchAndSortAndFilters(searchTerm, status, pageable)).thenReturn(mockPage);

        Page<User> users = userService.getUsers(pageable, searchTerm, status);

        assertNotNull(users);
        assertEquals(mockUsers.size(), users.getContent().size());
        verify(userRepository, times(1)).findByWithSearchAndSortAndFilters(searchTerm, status, pageable);

    }

    @Test
    @DisplayName("Get users with search term and status")
    @Description("Returns a filtered list of users when both a search term and a status are provided.")
    @Story("Fetching Users")
    void getUsers_ShouldReturnFilteredUsers_WhenSearchTermAndStatusAreProvided() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        String searchTerm = "testuser";
        String status = "active";
        List<User> mockUsers = List.of(mockUser);
        Page<User> mockPage = new PageImpl<>(mockUsers, pageable, mockUsers.size());

        when(userRepository.findByWithSearchAndSortAndFilters(searchTerm, status, pageable)).thenReturn(mockPage);

        Page<User> users = userService.getUsers(pageable, searchTerm, status);

        assertNotNull(users);
        assertEquals(mockUsers.size(), users.getContent().size());
        verify(userRepository, times(1)).findByWithSearchAndSortAndFilters(searchTerm, status, pageable);
    }

}
