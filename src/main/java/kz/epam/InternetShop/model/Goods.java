package kz.epam.InternetShop.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GOODS")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_SEQ")
    @SequenceGenerator(name = "AUTO_SEQ", allocationSize = 1, sequenceName = "AUTO_SEQ")
    @Column(name = "ID")
    private Long id;
    @NotBlank
    @Column(name = "NAME")
    private String name;
    @NotBlank
    @Column(name = "DESCRIPTION")
    private String description;
    @NotNull
    @Column(name = "COST")
    private Double cost;
    @NotNull
    @Column(name = "COUNT")
    private Integer count;

    @ElementCollection
    @CollectionTable(name = "PHOTO_GOODS", joinColumns = @JoinColumn(name = "GOODS_ID"))
    @Column(name = "PHOTO")
    private List<String> photos = new ArrayList<>();

    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GoodsCategory goodsCategory;
}
