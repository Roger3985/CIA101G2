package com.iting.cart.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.product.entity.Product;
import com.roger.member.entity.Member;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cart")
public class Cart {

    @EmbeddedId
    private CompositeDetail2 compositeKey2;
    @Column(name = "pbuyqty")
    private Integer pBuyQty;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "pno", referencedColumnName = "pno", insertable = false, updatable = false)
    private Product product;

    public CompositeDetail2 getCompositeKey2() {
        return compositeKey2;
    }

    public void setCompositeKey2(CompositeDetail2 compositeKey2) {
        this.compositeKey2 = compositeKey2;
    }

    public Integer getpBuyQty() {
        return pBuyQty;
    }

    public void setpBuyQty(Integer pBuyQty) {
        this.pBuyQty = pBuyQty;
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
        @Column(name = "pno")
        private Integer pNo;
        @Column(name = "memno")
        private Integer memNo;


        public CompositeDetail2(){
            super();
        }
        public CompositeDetail2(Integer pNo,Integer memNo){
            super();
            this.pNo = pNo;
            this.memNo = memNo;

        }

        public Integer getpNo() {
            return pNo;
        }
        public void setpNo(Integer pNo) {
            this.pNo = pNo;
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
            result = prime * result + ((pNo == null) ? 0 : pNo.hashCode());
            result = prime * result + ((memNo == null) ? 0 : memNo.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;

            if (obj != null && getClass() == obj.getClass()) {
               CompositeDetail2 compositeKey2 = (CompositeDetail2) obj;
                if (pNo.equals(compositeKey2.pNo) && memNo.equals(compositeKey2.memNo)) {
                    return true;
                }
            }

            return false;
        }
    }

}


