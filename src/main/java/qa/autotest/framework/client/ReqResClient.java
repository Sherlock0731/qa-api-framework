package qa.autotest.framework.client;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import qa.autotest.app.dto.TokenDto;
import qa.autotest.app.dto.UserDto;
import qa.autotest.app.dto.UsersListDto;

import java.util.HashMap;
import java.util.Map;

/**
 * REST client for ReqRes API
 * Provides methods for user-related operations
 */
@Slf4j
public class ReqResClient extends BaseRestClient {
    
    private static final String API_KEY_HEADER = "x-api-key";
    private static final String API_USERS = "/api/users";
    private static final String API_LOGIN = "/api/login";
    
    @Override
    protected String getBaseUrl() {
        return CONFIG.reqresBaseUrl();
    }
    
    /**
     * Override createRequestSpec to add ReqRes API key header
     */
    @Override
    protected RequestSpecification createRequestSpec() {
        RequestSpecification baseSpec = super.createRequestSpec();
        
        // Add API key header if configured
        String apiKey = CONFIG.reqresApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            log.debug("Adding API key header to request");
            return new RequestSpecBuilder()
                    .addRequestSpecification(baseSpec)
                    .addHeader(API_KEY_HEADER, apiKey)
                    .build();
        }
        
        return baseSpec;
    }
    
    /**
     * Get list of users
     */
    @Step("Get users list")
    public Response getUsersList() {
        log.info("Getting users list");
        return get(API_USERS);
    }
    
    /**
     * Get users list and parse to DTO
     */
    @Step("Get users list as DTO")
    public UsersListDto getUsersListDto() {
        Response response = getUsersList();
        return response.as(UsersListDto.class);
    }
    
    /**
     * Get users list with pagination
     */
    @Step("Get users list with page: {page}")
    public Response getUsersList(int page) {
        log.info("Getting users list for page: {}", page);
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        return get(API_USERS, params);
    }
    
    /**
     * Login user
     */
    @Step("Login user with email: {userDto.email}")
    public Response login(UserDto userDto) {
        log.info("Logging in user: {}", userDto.getEmail());
        return post(API_LOGIN, userDto);
    }
    
    /**
     * Login user and get token
     */
    @Step("Login and get token for user: {userDto.email}")
    public TokenDto loginAndGetToken(UserDto userDto) {
        Response response = login(userDto);
        return response.as(TokenDto.class);
    }
    
    /**
     * Create new user
     */
    @Step("Create user with name: {userDto.name}")
    public Response createUser(UserDto userDto) {
        log.info("Creating user: {}", userDto.getName());
        return post(API_USERS, userDto);
    }
    
    /**
     * Create user and parse to DTO
     */
    @Step("Create user and get response as DTO")
    public UserDto createUserDto(UserDto userDto) {
        Response response = createUser(userDto);
        return response.as(UserDto.class);
    }
    
    /**
     * Get user by ID
     */
    @Step("Get user by ID: {userId}")
    public Response getUserById(int userId) {
        log.info("Getting user by ID: {}", userId);
        return get(API_USERS + "/" + userId);
    }
}
