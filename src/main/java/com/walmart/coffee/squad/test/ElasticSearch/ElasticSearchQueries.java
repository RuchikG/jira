package com.walmart.coffee.squad.test.ElasticSearch;

import com.walmart.coffee.squad.test.Common.JsonUtil;
import com.walmart.coffee.squad.test.model.ElasticSearchDocument;
import com.walmart.coffee.squad.test.model.ResponseDocument;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class ElasticSearchQueries {

    private static final String INDEX_NAME = "index";

    private static final String DISTINCT_VALUES = "DISTINCT_VALUES";

    /*
    TODO: Add RestHighLevelClient Here
     */
    private RestHighLevelClient client = null;//getClient();

    public List<String> getUniqueIdFromElasticSearch() {


        Assert.notNull(client, "RestHighLevelClient Cannot be Null");

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.aggregation(AggregationBuilders.terms(DISTINCT_VALUES).field("id.keyword"));
        searchRequest.source(searchSourceBuilder);
        Map<String, Object> map = null;

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            if (searchResponse.getHits().getTotalHits().value > 0) {
                SearchHit[] searchHits = searchResponse.getHits().getHits();
                for (SearchHit searchHit : searchHits) {
                    map = searchHit.getSourceAsMap();
                }
            }

            Aggregations aggregations = searchResponse.getAggregations();
            List<String> list = new ArrayList<>();
            Terms aggTerms = aggregations.get(DISTINCT_VALUES);
            List<? extends Terms.Bucket> buckets = aggTerms.getBuckets();
            for (Terms.Bucket bucket : buckets) {
                list.add(bucket.getKeyAsString());
            }
            return list;
        } catch (Exception ex) {
            log.error("Exception Occurred while fetching the Unique Records from ElasticSearch", ex);
        }
        return null;
    }

    public ResponseDocument getDocsFromES(SearchSourceBuilder queryRequest) {
        SearchResponse searchResponse = null;
        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        searchRequest.source(queryRequest);
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            log.info("ElasticSearchQueries:getDocsFromES:: Completed");

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
            List<ElasticSearchDocument> documentList = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                ElasticSearchDocument document = JsonUtil.getMapper().convertValue(hit.getSourceAsMap(), ElasticSearchDocument.class);
                documentList.add(document);
            }
            documentDTO.setData(documentList);
            documentDTO.setNumDocuments((long) response.getHits().getTotalHits().value);
        }
        return documentDTO;
    }
}
