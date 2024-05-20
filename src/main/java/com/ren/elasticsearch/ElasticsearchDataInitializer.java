//package com.ren.elasticsearch;
//
//import com.ren.elasticsearch.service.ElasticsearchSetupService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ElasticsearchDataInitializer implements CommandLineRunner {
//
//    @Autowired
//    private ElasticsearchSetupService elasticsearchSetupService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        elasticsearchSetupService.createIndices();
//        elasticsearchSetupService.insertData();
//    }
//}
