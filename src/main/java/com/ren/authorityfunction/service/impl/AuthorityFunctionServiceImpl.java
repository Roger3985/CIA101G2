package com.ren.authorityfunction.service.impl;

import com.ren.authorityfunction.entity.AuthorityFunction;
import com.ren.authorityfunction.dao.AuthorityFunctionDAO_interface;
import com.ren.authorityfunction.dao.AuthorityFunctionJDBCDAOImpl;
import com.ren.authorityfunction.dao.AuthorityFunctionRepository;
import com.ren.authorityfunction.service.AuthorityFunctionService_interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityFunctionServiceImpl implements AuthorityFunctionService_interface {

    @Autowired
    private AuthorityFunctionRepository authorityFunctionRepository;

    @Override
    public AuthorityFunction addAuthorityFunction(AuthorityFunction authorityFunction) {
        return authorityFunctionRepository.save(authorityFunction);
    }

    @Override
    public AuthorityFunction getOneAuthorityFunction(Integer authFuncNo) {
        return authorityFunctionRepository.findById(authFuncNo).orElse(null);
    }

    @Override
    public List<AuthorityFunction> getAll() {
        return authorityFunctionRepository.findAll();
    }

    @Override
    public AuthorityFunction updateAuthorityFunction(AuthorityFunction authorityFunction) {
        return authorityFunctionRepository.save(authorityFunction);
    }

    @Override
    public void deleteAuthorityFunction(Integer authFuncNo) {
        authorityFunctionRepository.deleteById(authFuncNo);
    }
}
