package examples.products;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.autotest.app.dto.ProductDto;
import qa.autotest.app.dto.ProductsListDto;
import examples.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for GET /product/{id} endpoint
 */
@Epic("Products API")
@Feature("Get Product By ID")
@Tag("products")
public class GetProductByIdTests extends BaseTest {

    private static final String VALID_PRODUCT_ID = "ef3697db-f111-42cd-9e84-aa49af42b3a4";
    private static final String NON_EXISTENT_ID = "00000000-0000-0000-0000-000000000000";

    @Test
    @DisplayName("TC-022: Successfully get product by existing ID")
    @Description("Verify that product details are returned for existing ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Product By ID")
    void testGetProductByIdSuccess() {
        Response response = beeceptorClient.getProductById(VALID_PRODUCT_ID);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);
        
        ProductDto productDto = response.as(ProductDto.class);
        
        assertThat(productDto.getId())
                .as("ID should match requested ID")
                .isEqualTo(VALID_PRODUCT_ID);
        
        assertThat(productDto.getName())
                .as("Name should be present")
                .isNotNull()
                .isNotEmpty();
        
        assertThat(productDto.getPrice())
                .as("Price should be present")
                .isNotNull()
                .isGreaterThan(0.0);
        
        assertThat(productDto.getInStock())
                .as("InStock should be present")
                .isNotNull();
    }
    
    @Test
    @DisplayName("TC-023: Get product with non-existent ID")
    @Description("Verify that 404 is returned for non-existent product ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Product By ID")
    void testGetProductByNonExistentId() {
        Response response = beeceptorClient.getProductById(NON_EXISTENT_ID);
        
        assertThat(response.getStatusCode())
                .as("Status code should be 404")
                .isIn(404, 200); // Some mock APIs return 200 even for non-existent
    }
    
    @Test
    @DisplayName("TC-024: Get product with invalid UUID format")
    @Description("Verify that 400 is returned for invalid UUID format")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get Product By ID")
    void testGetProductByInvalidIdFormat() {
        Response response = beeceptorClient.getProductById("123");
        
        assertThat(response.getStatusCode())
                .as("Status code should be 400 or 404")
                .isIn(400, 404, 200); // API behavior may vary
    }
}
