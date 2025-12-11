package examples.users;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.autotest.app.dto.TokenDto;
import qa.autotest.app.dto.UserDto;
import examples.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for POST /api/login endpoint
 */
@Epic("Users API")
@Feature("User Authentication")
@Tag("users")
@Tag("smoke")
public class LoginTests extends BaseTest {

    @Test
    @DisplayName("TC-006: Successfully login with valid credentials")
    @Description("Verify that user can login with valid email and password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Login")
    void testLoginSuccess() {
        UserDto loginRequest = UserDto.builder()
                .id(CONFIG.testUserId())
                .email(CONFIG.testUserEmail())
                .firstName(CONFIG.testUserFirstName())
                .lastName(CONFIG.testUserLastName())
                .password(CONFIG.testUserPassword())
                .build();
        
        Response response = reqResClient.login(loginRequest);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);
        
        TokenDto tokenDto = response.as(TokenDto.class);
        
        assertThat(tokenDto.getToken())
                .as("Token should be present")
                .isNotNull()
                .isNotEmpty();
    }
    
    @Test
    @Disabled("Temporarily disabled - ReqRes API issue")
    @DisplayName("TC-007: Login with incorrect password")
    @Description("Verify that login fails with incorrect password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Login")
    void testLoginWithWrongPassword() {
        UserDto loginRequest = UserDto.builder()
                .email(CONFIG.testUserEmail())
                .password("WrongPassword123")
                .build();
        
        Response response = reqResClient.login(loginRequest);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 401 or 400")
                .isIn(400, 401);
    }
    
    @Test
    @DisplayName("TC-008: Login with non-existent email")
    @Description("Verify that login fails with non-existent email")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User Login")
    void testLoginWithNonExistentEmail() {
        UserDto loginRequest = UserDto.builder()
                .email("nonexistent@test.com")
                .password("SomePassword123")
                .build();
        
        Response response = reqResClient.login(loginRequest);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 401 or 404")
                .isIn(400, 401, 404);
    }
    
    @Test
    @DisplayName("TC-009: Login without password field")
    @Description("Verify that login fails when password is missing")
    @Severity(SeverityLevel.NORMAL)
    @Story("User Login")
    void testLoginWithoutPassword() {
        UserDto loginRequest = UserDto.builder()
                .email(CONFIG.testUserEmail())
                .build();
        
        Response response = reqResClient.login(loginRequest);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 400")
                .isEqualTo(400);
    }
    
    @Test
    @DisplayName("TC-010: Login without email field")
    @Description("Verify that login fails when email is missing")
    @Severity(SeverityLevel.NORMAL)
    @Story("User Login")
    void testLoginWithoutEmail() {
        UserDto loginRequest = UserDto.builder()
                .password(CONFIG.testUserPassword())
                .build();
        
        Response response = reqResClient.login(loginRequest);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 400")
                .isEqualTo(400);
    }
    
    @Test
    @DisplayName("TC-011: Login with invalid email format")
    @Description("Verify that login fails with invalid email format")
    @Severity(SeverityLevel.NORMAL)
    @Story("User Login")
    void testLoginWithInvalidEmailFormat() {
        UserDto loginRequest = UserDto.builder()
                .email("not-an-email")
                .password(CONFIG.testUserPassword())
                .build();
        
        Response response = reqResClient.login(loginRequest);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 400")
                .isEqualTo(400);
    }
}
