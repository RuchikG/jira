package com.walmart.coffee.squad.test;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class CronJob {


    private static final String INDEX_NAME = "index";

    private static final String DISTINCT_VALUES = "DISTINCT_VALUES";

    @Scheduled(cron = "*/15 * * * * ?")
    public void run() {
        List<String> uniqueIds = getUniqueIdFromElasticSearch();
//        orderEventsByTimeStamp();
//        getLatestEvent();
//        createDTOforJira();
    }

    public List<String> getUniqueIdFromElasticSearch() {
        RestHighLevelClient client = null;//getClient();

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


}
