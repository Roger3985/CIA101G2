package com.chihyun.servicepicture.entity;

import com.chihyun.servicerecord.entity.ServiceRecord;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "servicepicture")
public class ServicePicture {
    @Id
    @Column(name = "servicepicno")
    private Integer servicePicNo;
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "recordno", referencedColumnName = "recordno")
    private ServiceRecord serviceRecord;
    @Column(name = "servicepic", columnDefinition = "longblob")
    private byte[] servicePic;

    public Integer getServicePicNo() {
        return servicePicNo;
    }

    public void setServicePicNo(Integer servicePicNo) {
        this.servicePicNo = servicePicNo;
    }

    public ServiceRecord getServiceRecord() {
        return serviceRecord;
    }

    public void setServiceRecord(ServiceRecord serviceRecord) {
        this.serviceRecord = serviceRecord;
    }

    public byte[] getServicePic() {
        return servicePic;
    }

    public void setServicePic(byte[] servicePic) {
        this.servicePic = servicePic;
    }
}
