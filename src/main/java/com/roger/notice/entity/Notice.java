package com.roger.notice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.roger.member.entity.Member;
import com.roger.notice.entity.uniqueAnnotation.Create;
import com.roger.notice.entity.uniqueAnnotation.ValidNotContent;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotNull(message = "會員通知編號: 請勿空白")
    private Member member; // 定義多對一關係(現在表示:通知\(多)，會員(單))，該字段關聯到 MemberVO 實體類，使用 memNo 列作為外來鍵。 需要再生成getter and setter

    @Column(name = "notcontent")
    @NotEmpty(message = "最新通知內容: 請勿空白!")
    @ValidNotContent(groups = Create.class)
    // @Pattern(regexp = "^[a-zA-Z0-9_\\u4e00-\\u9fa5,，]{1,30}$", message = "最新會員通知內容: 只能是中、英文字母、數字和_，, 且長度必需在1到30之間")
    private String notContent;

    @Column(name = "nottime")
    @NotNull(message = "最新通知時間: 請勿空白!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
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
