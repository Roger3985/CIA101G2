package com.ren.productreview.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.admauthority.entity.AdmAuthority;
import com.ren.authorityfunction.entity.AuthorityFunction;
import com.ren.product.entity.Product;
import com.ren.title.entity.Title;
import com.roger.member.entity.Member;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "productreview")
public class ProductReview {
    @EmbeddedId
    private CompositeProductReview compositeProductReview;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "productno", referencedColumnName = "productno", insertable = false, updatable = false)
    private Product product;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;
    @Column(name = "review")
    private String review;
    @NotEmpty(message = "請選擇評分")
    @Min(value = 1, message = "請選擇1~5顆星的評價")
    @Max(value = 5, message = "請選擇1~5顆星的評價")
    @Column(name = "productscore")
    private Integer productScore;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotEmpty(message = "時間請勿空白")
    @Future(message = "評論時間不能為過去時間")
    @Column(name = "reviewtime")
    private Timestamp reviewTime;

    public ProductReview() {
    }

    public ProductReview(CompositeProductReview compositeProductReview) {
        this.compositeProductReview = compositeProductReview;
    }

    public ProductReview(String review, Integer productScore, Timestamp reviewTime) {
        this.review = review;
        this.productScore = productScore;
        this.reviewTime = reviewTime;
    }

    public ProductReview(CompositeProductReview compositeProductReview, String review, Integer productScore, Timestamp reviewTime) {
        this.compositeProductReview = compositeProductReview;
        this.review = review;
        this.productScore = productScore;
        this.reviewTime = reviewTime;
    }

    @Embeddable
    public static class CompositeProductReview implements Serializable {
        private static final long serialVersionUID = 1L;
        @NotEmpty(message = "請選擇商品編號")
        @Column(name = "productno")
        private Integer productNo;
        @NotEmpty(message = "請選擇會員編號")
        @Column(name = "memno")
        private Integer memNo;

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

        public CompositeProductReview() {
        }

        public CompositeProductReview(Integer productNo, Integer memNo) {
            this.productNo = productNo;
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

            if (this == obj) {
                return true;
            }

            if (obj != null && getClass() == obj.getClass()) {
                CompositeProductReview compositeProductReview = (CompositeProductReview) obj;
                if (productNo.equals(compositeProductReview.productNo) && memNo.equals(compositeProductReview.memNo)) {
                    return true;
                }
            }
            return false;
        }
    }

    public CompositeProductReview getCompositeProductReview() {
        return compositeProductReview;
    }

    public void setCompositeProductReview(CompositeProductReview compositeProductReview) {
        this.compositeProductReview = compositeProductReview;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getProductScore() {
        return productScore;
    }

    public void setProductScore(Integer productScore) {
        this.productScore = productScore;
    }

    public Timestamp getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Timestamp reviewTime) {
        this.reviewTime = reviewTime;
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
