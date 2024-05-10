package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cart")
public class Cart {

    @EmbeddedId
    private CompositeDetail2 compositeKey2;
    @Column(name = "productbuyqty")
    private Integer productBuyQty;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "productno", referencedColumnName = "productno", insertable = false, updatable = false)
    private Product product;

    public CompositeDetail2 getCompositeKey2() {
        return compositeKey2;
    }

    public void setCompositeKey2(CompositeDetail2 compositeKey2) {
        this.compositeKey2 = compositeKey2;
    }

    public Integer getproductBuyQty() {
        return productBuyQty;
    }

    public void setproductBuyQty(Integer productBuyQty) {
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

    @Embeddable
    public static class CompositeDetail2 implements Serializable {
        private static final long serialVersionUID = 1L;
        @Column(name = "productno")
        private Integer productNo;
        @Column(name = "memno")
        private Integer memNo;


        public CompositeDetail2(){
            super();
        }
        public CompositeDetail2(Integer productNo,Integer memNo){
            super();
            this.productNo = productNo;
            this.memNo = memNo;

        }

        public Integer getproductNo() {
            return productNo;
        }
        public void setproductNo(Integer productNo) {
            this.productNo = productNo;
        }
        public Integer getmemNo() {
            return memNo;
        }
        public void setmemNo(Integer memNo) {
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
               CompositeDetail2 compositeKey2 = (CompositeDetail2) obj;
                if (productNo.equals(compositeKey2.productNo) && memNo.equals(compositeKey2.memNo)) {
                    return true;
                }
            }

            return false;
        }
    }

}


