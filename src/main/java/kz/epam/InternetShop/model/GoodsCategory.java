package kz.epam.InternetShop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GOODS_CATEGORIES")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class GoodsCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_SEQ")
    @SequenceGenerator(name = "AUTO_SEQ", allocationSize = 1, sequenceName = "AUTO_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "goodsCategory", fetch = FetchType.LAZY)
    private List<Goods> goods = new ArrayList<>();
}
