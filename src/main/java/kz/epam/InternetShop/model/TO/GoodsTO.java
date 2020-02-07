package kz.epam.InternetShop.model.TO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class GoodsTO {
    private Long id;
    private String name;
    private Double cost;
    private Integer count;
    private String description;
    private List<String> photos;
    private Long categoryId;
}
