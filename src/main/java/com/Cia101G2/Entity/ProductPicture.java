package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    public Integer getproductPicNo() {
        return productPicNo;
    }

    public void setproductPicNo(Integer productPicNo) {
        this.productPicNo = productPicNo;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public byte[] getproductPic() {
        return productPic;
    }

    public void setproductPic(byte[] productPic) {
        this.productPic = productPic;
    }
}
