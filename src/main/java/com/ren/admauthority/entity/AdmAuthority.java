package com.Entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;


import java.io.Serializable;

@Entity
@Table(name = "admauthority")
public class AdmAuthority {
    @EmbeddedId
    private CompositeAdmAuthority compositeAdmAuthority;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "titleno", referencedColumnName = "titleno", insertable = false, updatable = false)
    private Title title;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "authfuncno", referencedColumnName = "authfuncno", insertable = false, updatable = false)
    private AuthorityFunction authorityFunction;
    @Embeddable
    public static class CompositeAdmAuthority implements Serializable {
        private static final long serialVersionUID = 1L;
        @Column(name = "titleno")
        private Integer titleNo;
        @Column(name = "authfuncno")
        private Integer authFuncNo;

        public Integer getTitleNo() {
            return titleNo;
        }

        public void setTitleNo(Integer titleNo) {
            this.titleNo = titleNo;
        }

        public Integer getAuthFuncNo() {
            return authFuncNo;
        }

        public void setAuthFuncNo(Integer authFuncNo) {
            this.authFuncNo = authFuncNo;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((titleNo == null) ? 0 : titleNo.hashCode());
            result = prime * result + ((authFuncNo == null) ? 0 : authFuncNo.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (obj != null && getClass() == obj.getClass()) {
                CompositeAdmAuthority compositeAdmAuthority = (CompositeAdmAuthority) obj;
                if (titleNo.equals(compositeAdmAuthority.titleNo) && authFuncNo.equals(compositeAdmAuthority.authFuncNo)) {
                    return true;
                }
            }
            return false;
        }
    }

    public AdmAuthority() {

    }

    public AdmAuthority(CompositeAdmAuthority compositeAdmAuthority, Title title, AuthorityFunction authorityFunction) {
        this.compositeAdmAuthority = compositeAdmAuthority;
        this.title = title;
        this.authorityFunction = authorityFunction;
    }

    public CompositeAdmAuthority getCompositeAdmAuthority() {
        return compositeAdmAuthority;
    }

    public void setCompositeAdmAuthority(CompositeAdmAuthority compositeAdmAuthority) {
        this.compositeAdmAuthority = compositeAdmAuthority;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public AuthorityFunction getAuthorityFunction() {
        return authorityFunction;
    }

    public void setAuthorityFunction(AuthorityFunction authorityFunction) {
        this.authorityFunction = authorityFunction;
    }
}
