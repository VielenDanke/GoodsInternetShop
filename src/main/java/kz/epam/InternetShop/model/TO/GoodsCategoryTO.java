package kz.epam.InternetShop.model.TO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class GoodsCategoryTO {
    private Long id;
    private String name;
}
