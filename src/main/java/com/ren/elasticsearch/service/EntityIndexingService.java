//package com.ren.elasticsearch.service;
//
//import com.ren.admauthority.dao.AdmAuthorityRepository;
//import com.ren.admauthority.entity.AdmAuthority;
//import com.ren.administrator.dao.AdministratorRepository;
//import com.ren.administrator.entity.Administrator;
//import com.ren.authorityfunction.dao.AuthorityFunctionRepository;
//import com.ren.authorityfunction.entity.AuthorityFunction;
//import com.ren.title.dao.TitleRepository;
//import com.ren.title.entity.Title;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//public class EntityIndexingService {
//
//    @Autowired
//    private ElasticsearchService elasticsearchService; // 注入 Elasticsearch 服務
//
//    @Autowired
//    private AdministratorRepository administratorRepository; // 注入 JPA Repository
//
//    @Autowired
//    private AdmAuthorityRepository admAuthorityRepository; // 注入 JPA Repository
//
//    @Autowired
//    private AuthorityFunctionRepository authorityFunctionRepository; // 注入 JPA Repository
//
//    @Autowired
//    private TitleRepository titleRepository; // 注入 JPA Repository
//
//    // 保存 Administrator 並索引到 Elasticsearch
//    public void saveAdministrator(Administrator administrator) throws IOException {
//        // 保存到數據庫
//        administratorRepository.save(administrator);
//
//        // 將實體索引到 Elasticsearch 中
//        elasticsearchService.indexEntity(administrator, "administrators", administrator.getAdmNo().toString());
//    }
//
//    // 保存 AdmAuthority 並索引到 Elasticsearch
//    public void saveAdmAuthority(AdmAuthority admAuthority) throws IOException {
//        admAuthorityRepository.save(admAuthority);
//        elasticsearchService.indexEntity(admAuthority, "admauthorities", admAuthority.getCompositeAdmAuthority().toString());
//    }
//
//    // 保存 AuthorityFunction 並索引到 Elasticsearch
//    public void saveAuthorityFunction(AuthorityFunction authorityFunction) throws IOException {
//        authorityFunctionRepository.save(authorityFunction);
//        elasticsearchService.indexEntity(authorityFunction, "authorityfunctions", authorityFunction.getAuthFuncNo().toString());
//    }
//
//    // 保存 Title 並索引到 Elasticsearch
//    public void saveTitle(Title title) throws IOException {
//        titleRepository.save(title);
//        elasticsearchService.indexEntity(title, "titles", title.getTitleNo().toString());
//    }
//}
//
