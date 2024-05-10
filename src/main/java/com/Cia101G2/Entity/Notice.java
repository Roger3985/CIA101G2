package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "notice")
public class Notice implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "motno")
    private Integer motNo;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno")
    private Member member; // 定義多對一關係(現在表示:通知\(多)，會員(單))，該字段關聯到 MemberVO 實體類，使用 memNo 列作為外來鍵。 需要再生成getter and setter

    @Column(name = "notcontent")
    private String notContent;

    @Column(name = "nottime")
    private Timestamp notTime;

    @Column(name = "notstat")
    private Byte notStat;

    public Integer getMotNo() {
        return motNo;
    }

    public void setMotNo(Integer motNo) {
        this.motNo = motNo;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getNotContent() {
        return notContent;
    }

    public void setNotContent(String notContent) {
        this.notContent = notContent;
    }

    public Timestamp getNotTime() {
        return notTime;
    }

    public void setNotTime(Timestamp notTime) {
        this.notTime = notTime;
    }

    public Byte getNotStat() {
        return notStat;
    }

    public void setNotStat(Byte notStat) {
        this.notStat = notStat;
    }
}
