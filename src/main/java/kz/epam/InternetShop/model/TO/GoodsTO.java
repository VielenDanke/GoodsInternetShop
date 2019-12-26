package kz.epam.InternetShop.model.TO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor

public class GoodsTO {
    private Long id;
    private String name;
    private Double cost;
    private Integer count;
    private String photos;
    private Long orderId;
}
