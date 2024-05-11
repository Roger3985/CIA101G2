package com.ren.title.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ren.admauthority.entity.AdmAuthority;
import com.ren.administrator.entity.Administrator;

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

//    // enum常量定義
//    public enum Position {
//        PARTJOB(4),
//        FULLTIME(3),
//        MANAGER(2),
//        BOSS(1);
//
//        private final int value;
//
//        Position(int value) {
//            this.value = value;
//        }
//
//        public int getValue() {
//            return value;
//        }
//    }
//
//    private Position position;

    public Title() {

    }

    public Title(Integer titleNo) {
        this.titleNo = titleNo;
    }

    public Title(String titleName) {
        this.titleName = titleName;
    }

    public Title(Integer titleNo, String titleName) {
        this.titleNo = titleNo;
        this.titleName = titleName;
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
