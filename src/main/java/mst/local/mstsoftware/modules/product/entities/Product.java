package mst.local.mstsoftware.modules.product.entities;

import jakarta.persistence.*;
import lombok.*;
import mst.local.mstsoftware.converts.StringListConverter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products", indexes = {
        @Index(name = "idx_category_id", columnList = "category_id")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_category_id")
    )
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    private Integer quantity = 0;

    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> images;

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    protected void onCreated() {
        createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdated() {
        updatedAt = Instant.now();
    }
}
