package com.Cia101G2.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "title")
public class Title {
    @Id
    @Column(name = "titleno")
    private Integer titleNo;
    @Column(name = "titlename")
    private String titleName;
    @JsonBackReference
    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL)
    private Set<AdmAuthority> admAuthorities;
    @JsonBackReference
    @OneToMany(mappedBy = "title", cascade = CascadeType.ALL)
    private Set<Administrator> administrators;

    public Title() {

    }

    public Title(Integer titleNo, String titleName, Set<AdmAuthority> admAuthorities, Set<Administrator> administrators) {
        this.titleNo = titleNo;
        this.titleName = titleName;
        this.admAuthorities = admAuthorities;
        this.administrators = administrators;
    }

    public Integer getTitleNo() {
        return titleNo;
    }

    public void setTitleNo(Integer titleNo) {
        this.titleNo = titleNo;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Set<AdmAuthority> getAdmAuthorities() {
        return admAuthorities;
    }

    public void setAdmAuthorities(Set<AdmAuthority> admAuthorities) {
        this.admAuthorities = admAuthorities;
    }

    public Set<Administrator> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(Set<Administrator> administrators) {
        this.administrators = administrators;
    }
}
