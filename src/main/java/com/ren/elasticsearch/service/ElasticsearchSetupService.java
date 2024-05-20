//package com.ren.elasticsearch.service;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch._types.ElasticsearchException;
//import co.elastic.clients.elasticsearch.core.IndexRequest;
//import co.elastic.clients.elasticsearch.core.IndexResponse;
//import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
//import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
//import co.elastic.clients.json.JsonData;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//public class ElasticsearchSetupService {
//
//    @Autowired
//    private ElasticsearchClient client;
//
//    public void createIndices() throws IOException {
//        createIndex("administrators");
//        createIndex("admauthorities");
//        createIndex("authorityfunctions");
//        createIndex("titles");
//    }
//
//    private void createIndex(String indexName) throws IOException {
//        CreateIndexRequest request = CreateIndexRequest.of(c -> c.index(indexName));
//        CreateIndexResponse createIndexResponse = client.indices().create(request);
//    }
//
//    public void insertData() throws IOException {
//        insertDocument("administrators", "1", Map.of("admNo", 1, "admName", "John Doe", "admEmail", "john.doe@example.com"));
//        insertDocument("admauthorities", "1", Map.of("titleNo", 1, "authFuncNo", 1));
//        insertDocument("authorityfunctions", "1", Map.of("authFuncNo", 1, "authFuncInfo", "Function Info"));
//        insertDocument("titles", "1", Map.of("titleNo", 1, "titleName", "Manager"));
//    }
//
//    private void insertDocument(String indexName, String id, Map<String, Object> document) throws IOException {
//        IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
//                .index(indexName)
//                .id(id)
//                .document(document)
//        );
//        IndexResponse response = client.index(request);
//    }
//}
