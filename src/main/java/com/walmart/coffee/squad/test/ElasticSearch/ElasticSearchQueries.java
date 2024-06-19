package com.walmart.coffee.squad.test.ElasticSearch;

import com.walmart.coffee.squad.test.JsonUtil;
import com.walmart.coffee.squad.test.config.ElasticSearchConfiguration;
import com.walmart.coffee.squad.test.constants.Constants;
import com.walmart.coffee.squad.test.dto.ESEntityDTO;
import com.walmart.coffee.squad.test.dto.ResponseDocument;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;


@Component
@Slf4j
public class ElasticSearchQueries {

    @Autowired
    ElasticSearchRequest elasticSearchRequest;

    @Autowired
    private ElasticSearchConfiguration elasticSearchConfiguration;

    private RestHighLevelClient client;

    @PostConstruct
    public void init(){
        client = elasticSearchConfiguration.getElasticRestClient();
    }

    public List<String> getUniqueIdFromElasticSearch() {
        Assert.notNull(client, "RestHighLevelClient Cannot be Null");
        SearchRequest searchRequest = elasticSearchRequest.buildRequestToGetUniqueRecords();

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("ElasticSearchQueries:getUniqueIdFromElasticSearch :: Search Response-{}", searchResponse);
            Aggregations aggregations = searchResponse.getAggregations();
            List<String> uniqueIdsList = new ArrayList<>();
            Terms aggTerms = aggregations.get(Constants.DISTINCT_VALUES);
            List<? extends Terms.Bucket> buckets = aggTerms.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                uniqueIdsList.add(bucket.getKeyAsString());
            }
            log.info("ElasticSearchQueries:getUniqueIdFromElasticSearch :: UniqueIds List-{}", uniqueIdsList);
            return uniqueIdsList;
        } catch (Exception ex) {
            log.error("Exception Occurred while fetching the Unique Records from ElasticSearch", ex);
        }
        return null;
    }

    public ResponseDocument getDocsFromES(String uniqueId) {
        SearchResponse searchResponse = null;
        SearchRequest searchRequest = elasticSearchRequest.buildRequestForGetDocs(uniqueId);
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("ElasticSearchQueries:getDocsFromES :: Search Response-{}", searchResponse);
        } catch (Exception ex) {
            log.error("ElasticSearchQueries:getDocsFromES:: got the Exception while fetching results", ex);
        }
        return prepareResponse(searchResponse);
    }

    private ResponseDocument prepareResponse(SearchResponse response) {
        Assert.notNull(response, "Response cannot be null");
        ResponseDocument documentDTO = new ResponseDocument();
        if(response.getHits().getHits() == null || response.getHits().getHits().length == 0) {
            documentDTO.setData(new ArrayList<>());
            documentDTO.setNumDocuments(0L);
        } else {
            SearchHit[] searchHits = response.getHits().getHits();
            List<ESEntityDTO> documentList = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                ESEntityDTO document = JsonUtil.getMapper().convertValue(hit.getSourceAsMap(), ESEntityDTO.class);
                documentList.add(document);
            }
            documentDTO.setData(documentList);
            documentDTO.setNumDocuments((long) response.getHits().getTotalHits().value);
        }
        return documentDTO;
    }
}
