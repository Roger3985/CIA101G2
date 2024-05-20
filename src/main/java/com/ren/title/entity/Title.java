package com.ren.title.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ren.admauthority.entity.AdmAuthority;
import com.ren.administrator.entity.Administrator;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "title")
public class Title {
    @Id
    @Column(name = "titleno")
    private Integer titleNo;
    @NotEmpty(message="職位名稱: 請勿空白")
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
