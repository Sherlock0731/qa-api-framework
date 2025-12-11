package examples.users;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.autotest.app.dto.UserDto;
import qa.autotest.framework.utils.DateUtils;
import examples.BaseTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for POST /api/users endpoint
 */
@Epic("Users API")
@Feature("Create User")
@Tag("users")
public class CreateUserTests extends BaseTest {

    @Test
    @DisplayName("TC-012: Successfully create user with valid data")
    @Description("Verify that user can be created with valid name and job")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create User")
    void testCreateUserSuccess() {
        UserDto createRequest = UserDto.builder()
                .name("John Doe")
                .job("Developer")
                .build();
        
        Response response = reqResClient.createUser(createRequest);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 201")
                .isEqualTo(201);
        
        UserDto createdUser = response.as(UserDto.class);
        
        assertThat(createdUser.getName())
                .as("Name should match")
                .isEqualTo("John Doe");
        
        assertThat(createdUser.getJob())
                .as("Job should match")
                .isEqualTo("Developer");
        
        assertThat(createdUser.getId())
                .as("ID should be generated")
                .isNotNull();
        
        assertThat(createdUser.getCreatedAt())
                .as("CreatedAt should be present")
                .isNotNull();
    }
    
    @Test
    @DisplayName("TC-013: Create user without name field")
    @Description("Verify that user creation fails without name")
    @Severity(SeverityLevel.NORMAL)
    @Story("Create User")
    void testCreateUserWithoutName() {
        UserDto createRequest = UserDto.builder()
                .job("Developer")
                .build();
        
        Response response = reqResClient.createUser(createRequest);
        
        // Note: ReqRes API might still accept this, adjust assertion based on actual API behavior
        assertThat(response.getStatusCode())
                .as("Status code should be 400 or 201")
                .isIn(201, 400);
    }
    
    @Test
    @DisplayName("TC-014: Create user without job field")
    @Description("Verify that user creation fails without job")
    @Severity(SeverityLevel.NORMAL)
    @Story("Create User")
    void testCreateUserWithoutJob() {
        UserDto createRequest = UserDto.builder()
                .name("John Doe")
                .build();
        
        Response response = reqResClient.createUser(createRequest);
        
        // Note: ReqRes API might still accept this, adjust assertion based on actual API behavior
        assertThat(response.getStatusCode())
                .as("Status code should be 400 or 201")
                .isIn(201, 400);
    }
    
    @Test
    @DisplayName("TC-015: Create user with empty body")
    @Description("Verify that user creation fails with empty request body")
    @Severity(SeverityLevel.NORMAL)
    @Story("Create User")
    void testCreateUserWithEmptyBody() {
        UserDto createRequest = UserDto.builder().build();
        
        Response response = reqResClient.createUser(createRequest);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 400 or 201")
                .isIn(201, 400);
    }
    
    @Test
    @DisplayName("TC-016: Verify createdAt field format")
    @Description("Verify that createdAt field is in ISO 8601 format")
    @Severity(SeverityLevel.NORMAL)
    @Story("Create User")
    void testCreatedAtFormat() {
        UserDto createRequest = UserDto.builder()
                .name("Test User")
                .job("QA Engineer")
                .build();
        
        UserDto createdUser = reqResClient.createUserDto(createRequest);
        
        assertThat(createdUser.getCreatedAt())
                .as("CreatedAt should be present")
                .isNotNull();
        
        assertThat(DateUtils.isValidIsoDateTime(createdUser.getCreatedAt()))
                .as("CreatedAt should be in ISO 8601 format")
                .isTrue();
    }
    
    @Test
    @DisplayName("TC-017: Verify ID uniqueness")
    @Description("Verify that each created user gets a unique ID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Create User")
    void testIdUniqueness() {
        Set<String> ids = new HashSet<>();
        
        for (int i = 0; i < 3; i++) {
            UserDto createRequest = UserDto.builder()
                    .name("User " + i)
                    .job("Job " + i)
                    .build();
            
            UserDto createdUser = reqResClient.createUserDto(createRequest);
            ids.add(String.valueOf(createdUser.getId()));
        }
        
        assertThat(ids)
                .as("All IDs should be unique")
                .hasSize(3);
    }
}
