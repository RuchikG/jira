package com.walmart.coffee.squad.test;

import java.io.IOException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.walmart.coffee.squad.test.config.ElasticSearchConfiguration;
import com.walmart.coffee.squad.test.dto.ESEntityDTO;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ESIndexDao {

    private RestHighLevelClient elasticSearchClient;

    @Autowired
    private ElasticSearchConfiguration elasticSearchConfiguration;

    @PostConstruct
    public void init(){
        elasticSearchClient = elasticSearchConfiguration.getElasticRestClient();
    }


    public void publishDataToES(ESEntityDTO esEntityDTO, String indexName) throws ElasticsearchStatusException, JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        RestHighLevelClient client;
        client = elasticSearchClient;
        try {
            IndexRequest request = new IndexRequest(indexName)
                    .id(uuid.toString())
                    .source(JsonUtil.getMapperWithNoRootValue().writeValueAsString(esEntityDTO), XContentType.JSON);
            request.opType(DocWriteRequest.OpType.INDEX);
            client.index(request, RequestOptions.DEFAULT);
        } catch ( IOException e) {
            log.error("DeliveryLookupIndexDao:addDeliveryBatchInfoEntity:: insert document failed for index = '{}', Exception = '{}' for event: {}",
                    indexName, e.getMessage(), esEntityDTO, e);
        }
    }
}
