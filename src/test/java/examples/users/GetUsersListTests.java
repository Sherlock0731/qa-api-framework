package examples.users;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.autotest.app.dto.UserDto;
import qa.autotest.app.dto.UsersListDto;
import examples.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for GET /api/users endpoint
 */
@Epic("Users API")
@Feature("Get Users List")
@Tag("users")
@Tag("smoke")
public class GetUsersListTests extends BaseTest {

    @Test
    @DisplayName("TC-001: Successfully get users list with valid API key")
    @Description("Verify that API returns list of users with valid API key")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Users List")
    void testGetUsersListSuccess() {
        Response response = reqResClient.getUsersList();
        
        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);
        
        UsersListDto usersListDto = response.as(UsersListDto.class);
        
        assertThat(usersListDto.getPage())
                .as("Page should be present")
                .isNotNull();
        
        assertThat(usersListDto.getPerPage())
                .as("Per page should be present")
                .isNotNull();
        
        assertThat(usersListDto.getTotal())
                .as("Total should be present")
                .isNotNull();
        
        assertThat(usersListDto.getTotalPages())
                .as("Total pages should be present")
                .isNotNull();
        
        assertThat(usersListDto.getData())
                .as("Data array should not be empty")
                .isNotEmpty();
    }
    
    @Test
    @DisplayName("TC-002: Verify user object structure")
    @Description("Verify that each user object contains all required fields")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Users List")
    void testUserObjectStructure() {
        UsersListDto usersListDto = reqResClient.getUsersListDto();
        
        assertThat(usersListDto.getData())
                .as("Data array should not be empty")
                .isNotEmpty();
        
        UserDto firstUser = usersListDto.getData().get(0);
        
        assertThat(firstUser.getId())
                .as("User ID should be present")
                .isNotNull();
        
        assertThat(firstUser.getEmail())
                .as("Email should be present")
                .isNotNull()
                .contains("@");
        
        assertThat(firstUser.getFirstName())
                .as("First name should be present")
                .isNotNull()
                .isNotEmpty();
        
        assertThat(firstUser.getLastName())
                .as("Last name should be present")
                .isNotNull()
                .isNotEmpty();
        
        assertThat(firstUser.getAvatar())
                .as("Avatar URL should be present")
                .isNotNull()
                .startsWith("https://");
    }
    
    @Test
    @DisplayName("TC-003: Get second page of users")
    @Description("Verify pagination works correctly for page 2")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get Users List")
    void testGetUsersListPagination() {
        Response response = reqResClient.getUsersList(2);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);
        
        UsersListDto usersListDto = response.as(UsersListDto.class);
        
        assertThat(usersListDto.getPage())
                .as("Page should be 2")
                .isEqualTo(2);
        
        assertThat(usersListDto.getData())
                .as("Data array should not be empty")
                .isNotEmpty();
    }
}
