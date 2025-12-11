package qa.autotest.framework.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import qa.autotest.app.dto.CreateProductResponseDto;
import qa.autotest.app.dto.ProductDto;
import qa.autotest.app.dto.ProductsListDto;

/**
 * REST client for Beeceptor API
 * Provides methods for product-related operations
 */
@Slf4j
public class BeeceptorClient extends BaseRestClient {
    
    private static final String API_PRODUCTS = "/products";
    private static final String API_PRODUCT = "/product";
    
    @Override
    protected String getBaseUrl() {
        return CONFIG.beeceptorBaseUrl();
    }
    
    /**
     * Get list of products
     */
    @Step("Get products list")
    public Response getProductsList() {
        log.info("Getting products list");
        return get(API_PRODUCTS);
    }
    
    /**
     * Get products list and parse to DTO
     */
    @Step("Get products list as DTO")
    public ProductsListDto getProductsListDto() {
        Response response = getProductsList();
        return response.as(ProductsListDto.class);
    }
    
    /**
     * Get product by ID
     */
    @Step("Get product by ID: {productId}")
    public Response getProductById(String productId) {
        log.info("Getting product by ID: {}", productId);
        return get(API_PRODUCT + "/" + productId);
    }
    
    /**
     * Get product by ID and parse to DTO
     */
    @Step("Get product by ID as DTO: {productId}")
    public ProductDto getProductByIdDto(String productId) {
        Response response = getProductById(productId);
        return response.as(ProductDto.class);
    }
    
    /**
     * Create new product with empty body
     */
    @Step("Create product with empty body")
    public Response createProductEmpty() {
        log.info("Creating product with empty body");
        return post(API_PRODUCTS);
    }
    
    /**
     * Create product with empty body and parse to DTO
     */
    @Step("Create product with empty body as DTO")
    public CreateProductResponseDto createProductEmptyDto() {
        Response response = createProductEmpty();
        return response.as(CreateProductResponseDto.class);
    }
    
    /**
     * Create new product with data
     */
    @Step("Create product with data")
    public Response createProduct(ProductDto productDto) {
        log.info("Creating product: {}", productDto.getName());
        return post(API_PRODUCTS, productDto);
    }
    
    /**
     * Create product and parse to DTO
     */
    @Step("Create product as DTO")
    public CreateProductResponseDto createProductDto(ProductDto productDto) {
        Response response = createProduct(productDto);
        return response.as(CreateProductResponseDto.class);
    }
}
