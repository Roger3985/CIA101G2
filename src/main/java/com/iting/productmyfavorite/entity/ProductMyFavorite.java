package com.iting.productmyfavorite.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.product.entity.Product;
import com.roger.member.entity.Member;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "productmyfavorite")
public class ProductMyFavorite {
    @EmbeddedId
    private CompositeProductMyFavorite compositeProductMyFavorite;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "pno", referencedColumnName = "pno", insertable = false, updatable = false)
    private Product product;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;
    @Embeddable
    public static class CompositeProductMyFavorite implements Serializable {
        private static final long serialVersionUID = 1L;
        @Column(name = "pno")
        private Integer pNo;
        @Column(name = "memno")
        private Integer memNo;

        public Integer getpNo() {
            return pNo;
        }

        public void setpNo(Integer pNo) {
            this.pNo = pNo;
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
            result = prime * result + ((pNo == null) ? 0 : pNo.hashCode());
            result = prime * result + ((memNo == null) ? 0 : memNo.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (obj != null && getClass() == obj.getClass()) {
                CompositeProductMyFavorite compositeProductMyFavorite = (CompositeProductMyFavorite) obj;
                if (pNo.equals(compositeProductMyFavorite.pNo) && memNo.equals(compositeProductMyFavorite.memNo)) {
                    return true;
                }
            }
            return false;
        }
    }

    public CompositeProductMyFavorite getCompositeProductMyFavorite() {
        return compositeProductMyFavorite;
    }

    public void setCompositeProductMyFavorite(CompositeProductMyFavorite compositeProductMyFavorite) {
        this.compositeProductMyFavorite = compositeProductMyFavorite;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
