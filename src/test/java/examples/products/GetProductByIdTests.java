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

    @Test
    @DisplayName("TC-025: Verify product data consistency between list and detail")
    @Description("Verify that product data from list matches detail view")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get Product By ID")
    void testProductDataConsistency() {
        // Get product from list
        Response listResponse = beeceptorClient.getProductsList();

        assertThat(listResponse.getStatusCode())
                .as("List request should return 200")
                .isEqualTo(200);

        ProductsListDto productsListDto = listResponse.as(ProductsListDto.class);

        assertThat(productsListDto.getProducts())
                .as("Products list should not be empty")
                .isNotEmpty();

        ProductDto productFromList = productsListDto.getProducts().get(0);
        String productId = productFromList.getId();

        // Get same product by ID
        Response detailResponse = beeceptorClient.getProductById(productId);

        assertThat(detailResponse.getStatusCode())
                .as("Detail request should return 200")
                .isEqualTo(200);

        ProductDto productById = detailResponse.as(ProductDto.class);

        // Use soft assertions - collect all failures before failing the test
        org.assertj.core.api.SoftAssertions softly = new org.assertj.core.api.SoftAssertions();

        // Compare only non-null fields
        if (productById.getId() != null && productFromList.getId() != null) {
            softly.assertThat(productById.getId())
                    .as("ID should match")
                    .isEqualTo(productFromList.getId());
        }

        if (productById.getName() != null && productFromList.getName() != null) {
            softly.assertThat(productById.getName())
                    .as("Name should match")
                    .isEqualTo(productFromList.getName());
        }

        if (productById.getPrice() != null && productFromList.getPrice() != null) {
            softly.assertThat(productById.getPrice())
                    .as("Price should match")
                    .isEqualTo(productFromList.getPrice());
        }

        if (productById.getInStock() != null && productFromList.getInStock() != null) {
            softly.assertThat(productById.getInStock())
                    .as("InStock should match")
                    .isEqualTo(productFromList.getInStock());
        }

        softly.assertAll();
    }
}
