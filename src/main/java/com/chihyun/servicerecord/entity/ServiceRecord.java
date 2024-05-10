package com.chihyun.servicerecord.entity;

import com.chihyun.servicepicture.entity.ServicePicture;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ren.administrator.entity.Administrator;
import com.roger.member.entity.Member;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "servicerecord")
public class ServiceRecord {
	@Id
    @Column(name = "recordno")
	private Integer recordNo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "admno", referencedColumnName = "admno")
    private Administrator administrator;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "memno", referencedColumnName = "memno")
    private Member member;
    @Column(name = "recordtime")
    private Timestamp recordTime;
    @Column(name = "recordcontent")
    private String recordContent;
    @Column(name = "speaker")
    private Byte speaker;
    @JsonBackReference
    @OneToMany(mappedBy = "serviceRecord", cascade = CascadeType.ALL)
    private Set<ServicePicture> ServicePictures;

    public Integer getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(Integer recordNo) {
        this.recordNo = recordNo;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Timestamp getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Timestamp recordTime) {
        this.recordTime = recordTime;
    }

    public String getRecordContent() {
        return recordContent;
    }

    public void setRecordContent(String recordContent) {
        this.recordContent = recordContent;
    }

    public Byte getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Byte speaker) {
        this.speaker = speaker;
    }

    public Set<ServicePicture> getServicePictures() {
        return ServicePictures;
    }

    public void setServicePictures(Set<ServicePicture> servicePictures) {
        ServicePictures = servicePictures;
    }
}
