package kz.epam.InternetShop.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_DETAILS")
@NoArgsConstructor
@Data
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_SEQ")
    @SequenceGenerator(name = "AUTO_SEQ", allocationSize = 1, sequenceName = "AUTO_SEQ")
    private Long id;
    @Column(name = "ORDER_ID")
    private Long order_id;
    @Column(name = "GOODS_ID")
    private Long goods_id;
    @Column(name = "COUNT")
    private Integer count;
    @Column(name = "COST", nullable = false)
    private Double cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GOODS_ID")
    private Goods goods;
}
