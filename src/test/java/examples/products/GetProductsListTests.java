package examples.products;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qa.autotest.app.dto.ProductDto;
import qa.autotest.app.dto.ProductsListDto;
import qa.autotest.framework.utils.UuidUtils;
import examples.BaseTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for GET /products endpoint
 */
@Epic("Products API")
@Feature("Get Products List")
@Tag("products")
@Tag("smoke")
public class GetProductsListTests extends BaseTest {
    
    @Test
    @DisplayName("TC-018: Successfully get products list")
    @Description("Verify that API returns list of products")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Products List")
    void testGetProductsListSuccess() {
        Response response = beeceptorClient.getProductsList();
        
        assertThat(response.getStatusCode())
                .as("Status code should be 200")
                .isEqualTo(200);
        
        ProductsListDto productsListDto = response.as(ProductsListDto.class);
        
        assertThat(productsListDto.getProducts())
                .as("Products array should not be empty")
                .isNotEmpty();
    }
    
    @Test
    @DisplayName("TC-019: Verify product object data types")
    @Description("Verify that product object contains correct data types")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Products List")
    void testProductObjectDataTypes() {
        ProductsListDto productsListDto = beeceptorClient.getProductsListDto();
        
        assertThat(productsListDto.getProducts())
                .as("Products array should not be empty")
                .isNotEmpty();
        
        ProductDto firstProduct = productsListDto.getProducts().get(0);
        
        assertThat(firstProduct.getId())
                .as("ID should be a string")
                .isInstanceOf(String.class);
        
        assertThat(firstProduct.getName())
                .as("Name should be a string")
                .isInstanceOf(String.class)
                .isNotEmpty();
        
        assertThat(firstProduct.getPrice())
                .as("Price should be a number")
                .isInstanceOf(Double.class)
                .isNotNull();
        
        assertThat(firstProduct.getInStock())
                .as("InStock should be a boolean")
                .isInstanceOf(Boolean.class)
                .isNotNull();
    }
    
    @Test
    @DisplayName("TC-020: Verify UUID format in product ID")
    @Description("Verify that product ID follows UUID v4 format")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get Products List")
    void testProductIdUuidFormat() {
        ProductsListDto productsListDto = beeceptorClient.getProductsListDto();
        
        for (ProductDto product : productsListDto.getProducts()) {
            assertThat(UuidUtils.isValidUuid(product.getId()))
                    .as("Product ID should be valid UUID: " + product.getId())
                    .isTrue();
        }
    }
    
    @Test
    @DisplayName("TC-021: Verify price is positive with max 2 decimal places")
    @Description("Verify that all prices are positive and have at most 2 decimal places")
    @Severity(SeverityLevel.NORMAL)
    @Story("Get Products List")
    void testProductPriceValidation() {
        ProductsListDto productsListDto = beeceptorClient.getProductsListDto();
        
        for (ProductDto product : productsListDto.getProducts()) {
            assertThat(product.getPrice())
                    .as("Price should be positive: " + product.getName())
                    .isGreaterThan(0.0);
            
            // Check decimal places
            BigDecimal price = BigDecimal.valueOf(product.getPrice());
            BigDecimal rounded = price.setScale(2, RoundingMode.HALF_UP);
            
            assertThat(price)
                    .as("Price should have at most 2 decimal places: " + product.getName())
                    .isEqualByComparingTo(rounded);
        }
    }
}
