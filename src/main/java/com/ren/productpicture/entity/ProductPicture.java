package com.ren.productpicture.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.product.entity.Product;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


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
    @Column(name = "mimetype")
    private String mimeType;

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

    public ProductPicture(Product product, byte[] productPic, String mimeType) {
        this.product = product;
        this.productPic = productPic;
        this.mimeType = mimeType;
    }

    public ProductPicture(Integer productPicNo, Product product, byte[] productPic, String mimeType) {
        this.productPicNo = productPicNo;
        this.product = product;
        this.productPic = productPic;
        this.mimeType = mimeType;
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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
