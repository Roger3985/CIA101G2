//package com.ren.elasticsearch.service;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.core.IndexRequest;
//import co.elastic.clients.elasticsearch.core.SearchRequest;
//import co.elastic.clients.elasticsearch.core.SearchResponse;
//import co.elastic.clients.elasticsearch.core.search.Hit;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ElasticsearchService {
//
//    @Autowired
//    private ElasticsearchClient client;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    public void indexEntity(Object entity, String indexName, String id) throws IOException {
//        IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
//                .index(indexName)
//                .id(id)
//                .document(objectMapper.convertValue(entity, Map.class))
//        );
//
//        client.index(request);
//    }
//
//    public List<Object> searchEntities(String query, String[] indices) throws IOException {
//        SearchRequest searchRequest = SearchRequest.of(s -> s
//                .index(indices)
//                .query(q -> q
//                        .multiMatch(m -> m
//                                .query(query)
//                                .fields("admNo", "admPwd", "admName", "admStat", "admEmail", "admHireDate", "admPhoto", "admSalt", "admLogin", "admLogout", "admActiveTime",
//                                        "title.titleNo", "title.titleName",
//                                        "compositeAdmAuthority.titleNo", "compositeAdmAuthority.authFuncNo",
//                                        "authorityFunction.authFuncNo", "authorityFunction.authFuncInfo")
//                        )
//                )
//        );
//
//        SearchResponse<Map> response = client.search(searchRequest, Map.class);
//        List<Object> entities = new ArrayList<>();
//        for (Hit<Map> hit : response.hits().hits()) {
//            entities.add(hit.source());
//        }
//        return entities;
//    }
//}
