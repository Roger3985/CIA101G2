package com.ren.admauthority.service;

import com.Entity.*;
import com.ren.admauthority.dao.AdmAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmAuthorityServiceImpl implements AdmAuthorityService_interface {

    @Autowired
    private AdmAuthorityRepository admAuthorityRepository;

    @Override
    public AdmAuthority addAdmAuthority(AdmAuthority admAuthority) {
        return admAuthorityRepository.save(admAuthority);
    }

    @Override
    public AdmAuthority getOneAdmAuthority(Integer titleNo) {
        return admAuthorityRepository.findById(titleNo).orElse(null);
    }

    @Override
    public List<AdmAuthority> getAll() {
        return admAuthorityRepository.findAll();
    }

    @Override
    public AdmAuthority updateAdmAuthority(AdmAuthority admAuthority) {
        return admAuthorityRepository.save(admAuthority);
    }

    @Override
    public void deleteAdmAuthority(Integer titleNo) {
        admAuthorityRepository.deleteById(titleNo);
    }

}
