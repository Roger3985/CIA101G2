package com.ren.productpicture.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.product.entity.Product;

import javax.persistence.*;


@Entity
@Table(name = "productpicture")
public class ProductPicture implements java.io.Serializable {
    @Id
    @Column(name = "productpicno", updatable = false)
    private Integer productPicNo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "productno", referencedColumnName = "productno")
    private Product product;
    @Column(name = "productpic", columnDefinition = "longblob")
    private byte[] productPic;

    public ProductPicture() {
    }

    public ProductPicture(Integer productPicNo) {
        this.productPicNo = productPicNo;
    }

    public ProductPicture(Product product, byte[] productPic) {
        this.product = product;
        this.productPic = productPic;
    }

    public ProductPicture(Integer productPicNo, Product product, byte[] productPic) {
        this.productPicNo = productPicNo;
        this.product = product;
        this.productPic = productPic;
    }

    public Integer getProductPicNo() {
        return productPicNo;
    }

    public void setProductPicNo(Integer productPicNo) {
        this.productPicNo = productPicNo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public byte[] getProductPic() {
        return productPic;
    }

    public void setProductPic(byte[] productPic) {
        this.productPic = productPic;
    }
}
