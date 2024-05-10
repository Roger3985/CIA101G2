package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "productorderdetail")
public class ProductOrderDetail {
    @EmbeddedId
    private CompositeDetail compositeKey;
    @Column(name = "productprice")
    private BigDecimal productPrice;
    @Column(name = "productordqty")
    private Integer productOrdQty;
    @Column(name = "productrealprice")
    private BigDecimal productRealPrice;
    @Column(name = "productcomcontent")
    private String productComContent;
    @Column(name = "productscore")
    private Integer productScore;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "productno", referencedColumnName = "productno", insertable = false, updatable = false)
    private Product product;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "productordno", referencedColumnName = "productordno", insertable = false, updatable = false)
    private ProductOrder productOrder ;
    // 需要宣告一個有包含複合主鍵屬性的類別，並一定實作 java.io.Serializable 介面
    @Embeddable
    public static class CompositeDetail implements Serializable {
        private static final long serialVersionUID = 1L;
        @Column(name = "productordno", updatable = false)
        private Integer productOrdNo;
        @Column(name = "productno", updatable = false)
        private Integer productNo;

        public CompositeDetail() {
            super();
        }

        public CompositeDetail(Integer productOrdNo, Integer productNo) {
            super();
            this.productOrdNo = productOrdNo;
            this.productNo = productNo;
        }

        public Integer getproductOrdNo() {
            return productOrdNo;
        }

        public void setproductOrdNo(Integer productOrdNo) {
            this.productOrdNo = productOrdNo;
        }

        public Integer getproductNo() {
            return productNo;
        }

        public void setproductNo(Integer productNo) {
            this.productNo = productNo;
        }

        //一定要 override 此類別的 hashCode() 與 equals() 方法！
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((productOrdNo == null) ? 0 : productOrdNo.hashCode());
            result = prime * result + ((productNo == null) ? 0 : productNo.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj != null && getClass() == obj.getClass()) {
                CompositeDetail compositeKey = (CompositeDetail) obj;
                if (productOrdNo.equals(compositeKey.productOrdNo) && productNo.equals(compositeKey.productNo)) {
                    return true;
                }
            }

            return false;
        }
    }

    public CompositeDetail getCompositeKey() {
        return compositeKey;
    }

    public void setCompositeKey(CompositeDetail compositeKey) {
        this.compositeKey = compositeKey;
    }

    public BigDecimal getproductPrice() {
        return productPrice;
    }

    public void setproductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getproductOrdQty() {
        return productOrdQty;
    }

    public void setproductOrdQty(Integer productOrdQty) {
        this.productOrdQty = productOrdQty;
    }

    public BigDecimal getproductRealPrice() {
        return productRealPrice;
    }

    public void setproductRealPrice(BigDecimal productRealPrice) {
        this.productRealPrice = productRealPrice;
    }

    public String getproductComContent() {
        return productComContent;
    }

    public void setproductComContent(String productComContent) {
        this.productComContent = productComContent;
    }

    public Integer getproductScore() {
        return productScore;
    }

    public void setproductScore(Integer productScore) {
        this.productScore = productScore;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }
}
