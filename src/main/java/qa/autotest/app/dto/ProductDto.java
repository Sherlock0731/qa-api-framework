package qa.autotest.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Product entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("price")
    private Double price;
    
    @JsonProperty("inStock")
    private Boolean inStock;
    
    @JsonProperty("category")
    private String category;
}
