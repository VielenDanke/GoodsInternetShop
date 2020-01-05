package kz.epam.InternetShop.model.TO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class OrderDetailsTO {
    private Long id;
    private Long goodsId;
    private String goodsName;
    private Double cost;
    private Integer count;
    private String goodsPhoto;
    private Long orderId;
    private boolean available;
}
