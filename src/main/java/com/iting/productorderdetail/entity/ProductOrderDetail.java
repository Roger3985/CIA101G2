package com.iting.productorderdetail.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.iting.productorder.entity.ProductOrder;
import com.ren.product.entity.Product;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @Pattern(regexp = "^(|[(|\\u4e00-\\u9fa5)(a-zA-Z0-9_\\s,\\.\\。\\，)]{4,40})$", message = "只能是中、英文字母、數字、_、空格、逗點和句點")
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
    private ProductOrder productOrder;

    // 需要宣告一個有包含複合主鍵屬性的類別，並一定實作 java.io.Serializable 介面

    public static class CompositeDetail implements Serializable {
        private static final long serialVersionUID = 1L;
        @Column(name = "productordno", updatable = false)
        private Integer productOrdNo;
        @Column(name = "productno", updatable = false)
        private Integer productNo;

        public CompositeDetail() {
        }

        public CompositeDetail(Integer productOrdNo, Integer productNo) {
            this.productOrdNo = productOrdNo;
            this.productNo = productNo;
        }

        public Integer getProductOrdNo() {
            return productOrdNo;
        }

        public void setProductOrdNo(Integer productOrdNo) {
            this.productOrdNo = productOrdNo;
        }

        public Integer getProductNo() {
            return productNo;
        }

        public void setProductNo(Integer productNo) {
            this.productNo = productNo;
        }

        // 一定要 override 此類別的 hashCode() 與 equals() 方法！
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

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductOrdQty() {
        return productOrdQty;
    }

    public void setProductOrdQty(Integer productOrdQty) {
        this.productOrdQty = productOrdQty;
    }

    public BigDecimal getProductRealPrice() {
return  productRealPrice;
//        BigDecimal productPrice = getProductPrice();
//        BigDecimal productOrdQty = new BigDecimal(getProductOrdQty());
// return productPrice.multiply(productOrdQty); // 不需要再次创建 BigDecimal 对象
    }


    public void setProductRealPrice(BigDecimal productRealPrice) {
        this.productRealPrice = productRealPrice;
    }

    public String getProductComContent() {

        return productComContent;
    }

    public void setProductComContent(String productComContent) {
        this.productComContent = productComContent;
    }

    public Integer getProductScore() {
        return productScore;
    }

    public void setProductScore(Integer productScore) {
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
