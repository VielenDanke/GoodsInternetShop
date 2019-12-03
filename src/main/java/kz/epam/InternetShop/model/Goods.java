package kz.epam.InternetShop.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GOODS")
@NoArgsConstructor
@Data
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTO_SEQ")
    @SequenceGenerator(name = "AUTO_SEQ", allocationSize = 1, sequenceName = "AUTO_SEQ")
    @Column(name = "ID")
    private Long id;
    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;
    @NotBlank
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @NotBlank
    @Column(name = "COST", nullable = false)
    private Double cost;
    @NotBlank
    @Column(name = "COUNT", nullable = false)
    private Integer count;

    @ElementCollection
    @CollectionTable(name = "PHOTO_GOODS", joinColumns = @JoinColumn(name = "GOODS_ID"))
    @Column(name = "PHOTO")
    private List<String> photos = new ArrayList<>();

    @OneToMany(
            mappedBy = "goods",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderDetails> orderDetails;
}
