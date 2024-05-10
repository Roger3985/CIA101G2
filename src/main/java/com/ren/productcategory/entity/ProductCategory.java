package com.ren.productcategory.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ren.product.entity.Product;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "productcategory")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productcatno")
    private Integer productCatNo;
    @Column(name = "productcatname")
    private String productCatName;
    @JsonBackReference
    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL)
    private Set<Product> products;

    public ProductCategory() {
    }

    public ProductCategory(Integer productCatNo) {
        this.productCatNo = productCatNo;
    }

    public ProductCategory(String productCatName) {
        this.productCatName = productCatName;
    }

    public ProductCategory(Integer productCatNo, String productCatName) {
        this.productCatNo = productCatNo;
        this.productCatName = productCatName;
    }

    public Integer getProductCatNo() {
        return productCatNo;
    }

    public void setProductCatNo(Integer productCatNo) {
        this.productCatNo = productCatNo;
    }

    public String getProductCatName() {
        return productCatName;
    }

    public void setProductCatName(String productCatName) {
        this.productCatName = productCatName;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
