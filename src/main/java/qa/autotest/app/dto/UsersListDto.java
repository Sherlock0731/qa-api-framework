package qa.autotest.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for Users List Response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersListDto {
    
    @JsonProperty("page")
    private Integer page;
    
    @JsonProperty("per_page")
    private Integer perPage;
    
    @JsonProperty("total")
    private Integer total;
    
    @JsonProperty("total_pages")
    private Integer totalPages;
    
    @JsonProperty("data")
    private List<UserDto> data;
    
    @JsonProperty("support")
    private SupportDto support;
}
