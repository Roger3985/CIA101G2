package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

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

    public ProductCategory(Integer productCatNo, String productCatName, Set<Product> products) {
        this.productCatNo = productCatNo;
        this.productCatName = productCatName;
        this.products = products;
    }

    public Integer getproductCatNo() {
        return productCatNo;
    }

    public void setproductCatNo(Integer productCatNo) {
        this.productCatNo = productCatNo;
    }

    public String getproductCatName() {
        return productCatName;
    }

    public void setproductCatName(String productCatName) {
        this.productCatName = productCatName;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
