package qa.autotest.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Create Product Response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateProductResponseDto {
    
    @JsonProperty("success")
    private Boolean success;
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("data")
    private ProductDto data;
}
