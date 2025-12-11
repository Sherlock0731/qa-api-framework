package qa.autotest.framework.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import qa.autotest.framework.config.ConfigFactory;
import qa.autotest.framework.config.TestConfig;

import java.util.Map;

/**
 * Base REST client with thread-safe RequestSpecification
 * Provides common HTTP operations (GET, POST, PUT, DELETE)
 */
@Slf4j
public abstract class BaseRestClient {
    
    protected static final TestConfig CONFIG = ConfigFactory.getConfig();
    
    /**
     * Create new RequestSpecification with default settings
     * Called for each request to ensure thread safety
     */
    protected RequestSpecification createRequestSpec() {
        RestAssuredConfig config = RestAssured.config()
                .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JACKSON_2));
        
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setConfig(config)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured());
        
        if (CONFIG.detailedLogging()) {
            builder.log(LogDetail.ALL);
        }
        
        return builder.build();
    }
    
    /**
     * Get base URL for API requests
     */
    protected abstract String getBaseUrl();
    
    /**
     * Execute GET request
     */
    @io.qameta.allure.Step("GET {endpoint}")
    protected Response get(String endpoint) {
        log.info("Executing GET request to: {}", endpoint);
        Response response = RestAssured
                .given()
                .spec(createRequestSpec())
                .when()
                .get(endpoint);
        log.info("Response status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Execute GET request with query parameters
     */
    @io.qameta.allure.Step("GET {endpoint} with params")
    protected Response get(String endpoint, Map<String, ?> queryParams) {
        log.info("Executing GET request to: {} with params: {}", endpoint, queryParams);
        Response response = RestAssured
                .given()
                .spec(createRequestSpec())
                .queryParams(queryParams)
                .when()
                .get(endpoint);
        log.info("Response status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Execute POST request with body
     */
    @io.qameta.allure.Step("POST {endpoint}")
    protected Response post(String endpoint, Object body) {
        log.info("Executing POST request to: {}", endpoint);
        Response response = RestAssured
                .given()
                .spec(createRequestSpec())
                .body(body)
                .when()
                .post(endpoint);
        log.info("Response status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Execute POST request without body
     */
    @io.qameta.allure.Step("POST {endpoint} without body")
    protected Response post(String endpoint) {
        log.info("Executing POST request to: {} without body", endpoint);
        Response response = RestAssured
                .given()
                .spec(createRequestSpec())
                .when()
                .post(endpoint);
        log.info("Response status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Execute PUT request
     */
    @io.qameta.allure.Step("PUT {endpoint}")
    protected Response put(String endpoint, Object body) {
        log.info("Executing PUT request to: {}", endpoint);
        Response response = RestAssured
                .given()
                .spec(createRequestSpec())
                .body(body)
                .when()
                .put(endpoint);
        log.info("Response status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Execute DELETE request
     */
    @io.qameta.allure.Step("DELETE {endpoint}")
    protected Response delete(String endpoint) {
        log.info("Executing DELETE request to: {}", endpoint);
        Response response = RestAssured
                .given()
                .spec(createRequestSpec())
                .when()
                .delete(endpoint);
        log.info("Response status: {}", response.getStatusCode());
        return response;
    }
    
    /**
     * Execute PATCH request
     */
    @io.qameta.allure.Step("PATCH {endpoint}")
    protected Response patch(String endpoint, Object body) {
        log.info("Executing PATCH request to: {}", endpoint);
        Response response = RestAssured
                .given()
                .spec(createRequestSpec())
                .body(body)
                .when()
                .patch(endpoint);
        log.info("Response status: {}", response.getStatusCode());
        return response;
    }
}
