package com.ren.authorityfunction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ren.admauthority.entity.AdmAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "authorityfunction")
public class AuthorityFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authfuncno")
    private Integer authFuncNo;
    @Column(name = "authfuncinfo")
    private String authFuncInfo;
    @JsonBackReference
    @OneToMany(mappedBy = "authorityFunction", cascade = CascadeType.ALL)
    private Set<AdmAuthority> admAuthorities;

    public AuthorityFunction() {

    }

    public AuthorityFunction(Integer authFuncNo, String authFuncInfo, Set<AdmAuthority> admAuthorities) {
        this.authFuncNo = authFuncNo;
        this.authFuncInfo = authFuncInfo;
        this.admAuthorities = admAuthorities;
    }

    public Integer getAuthFuncNo() {
        return authFuncNo;
    }

    public void setAuthFuncNo(Integer authFuncNo) {
        this.authFuncNo = authFuncNo;
    }

    public String getAuthFuncInfo() {
        return authFuncInfo;
    }

    public void setAuthFuncInfo(String authFuncInfo) {
        this.authFuncInfo = authFuncInfo;
    }

    public Set<AdmAuthority> getAdmAuthorities() {
        return admAuthorities;
    }

    public void setAdmAuthorities(Set<AdmAuthority> admAuthorities) {
        this.admAuthorities = admAuthorities;
    }
}
