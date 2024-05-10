package com.roger.clicklike.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roger.columnarticle.entity.ColumnArticle;
import com.roger.member.entity.Member;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "clicklike")
public class ClickLike implements Serializable {

    @EmbeddedId
    private CompositeClickLike compositeClickLike;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno", insertable = false, updatable = false)
    private Member member;

    // private Integer memNo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "artno", referencedColumnName = "artno", insertable = false, updatable = false)
    private ColumnArticle columnArticle;

    // private Integer artNo;


    public CompositeClickLike getCompositeClickLike() {
        return compositeClickLike;
    }

    public void setCompositeClickLike(CompositeClickLike compositeClickLike) {
        this.compositeClickLike = compositeClickLike;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public ColumnArticle getColumnArticle() {
        return columnArticle;
    }

    public void setColumnArticle(ColumnArticle columnArticle) {
        this.columnArticle = columnArticle;
    }

    @Embeddable
    public static class CompositeClickLike implements Serializable {

        private static final long serialVersionUID = 1L;

        @Column(name = "memno")
        private Integer memNo;

        @Column(name = "artno")
        private Integer artNo;

        public CompositeClickLike() {
        }

        public CompositeClickLike(Integer memNo, Integer artNo) {
            this.memNo = memNo;
            this.artNo = artNo;
        }

        public Integer getMemNo() {
            return memNo;
        }

        public void setMemNo(Integer memNo) {
            this.memNo = memNo;
        }

        public Integer getArtNo() {
            return artNo;
        }

        public void setArtNo(Integer artNo) {
            this.artNo = artNo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositeClickLike that)) return false;
            return Objects.equals(getMemNo(), that.getMemNo()) && Objects.equals(getArtNo(), that.getArtNo());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getMemNo(), getArtNo());
        }
    }

}
