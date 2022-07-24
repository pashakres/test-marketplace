package com.challenge.marketplace.models;

import javax.persistence.*;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_generator")
    @SequenceGenerator(name="purchase_generator", sequenceName = "purchase_seq", allocationSize=50)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private Long userId;
    private Long productId;

    public Purchase() {
    }

    public Purchase(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
