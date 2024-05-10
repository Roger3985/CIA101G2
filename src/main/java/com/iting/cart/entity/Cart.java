package com.iting.cart.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.product.entity.Product;
import com.roger.member.entity.Member;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @EmbeddedId
    private CompositeDetail compositeKey;

    @Column(name = "productbuyqty")
    @NotNull(message="請勿空白")
    private Integer productBuyQty;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "productno", referencedColumnName = "productno", insertable = false, updatable = false)

    private Product product;

    public static  class CompositeDetail implements Serializable {
        private static final long serialVersionUID = 1L;

        @Column(name = "productno")
        @NotNull(message="請勿空白")
        private Integer productNo;

        @Column(name = "memno")
        @NotNull(message="請勿空白")
        private Integer memNo;
        public CompositeDetail() {
        }

        public CompositeDetail(Integer productNo, Integer memNo) {
            this.productNo = productNo;
            this.memNo = memNo;
        }

        public Integer getProductNo() {
            return productNo;
        }

        public void setProductNo(Integer productNo) {
            this.productNo = productNo;
        }

        public Integer getMemNo() {
            return memNo;
        }

        public void setMemNo(Integer memNo) {
            this.memNo = memNo;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((productNo == null) ? 0 : productNo.hashCode());
            result = prime * result + ((memNo == null) ? 0 : memNo.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj != null && getClass() == obj.getClass()) {
                CompositeDetail compositeKey = (CompositeDetail) obj;
                if (productNo.equals(compositeKey.productNo) && memNo.equals(compositeKey.memNo)) {
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

    public Integer getProductBuyQty() {
        return productBuyQty;
    }

    public void setProductBuyQty(Integer productBuyQty) {
        this.productBuyQty = productBuyQty;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

