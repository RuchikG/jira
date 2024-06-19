package com.walmart.coffee.squad.test.ElasticSearch;

import com.walmart.coffee.squad.test.constants.Constants;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchRequest {

    /*
    TODO: "timeStamp" field to be saved on ElasticSearch to sort the events in descending order
     */

    public SearchRequest buildRequestForGetDocs(String uniqueId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Constants.INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termsQuery(Constants.PULL_REQUEST_NUMBER, uniqueId));
        searchSourceBuilder.sort(new FieldSortBuilder("pull_request.updated_at").order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    public SearchRequest buildRequestToGetUniqueRecords() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(Constants.INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.aggregation(AggregationBuilders.terms(Constants.DISTINCT_VALUES).field(Constants.PULL_REQUEST_NUMBER).size(1000));
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
}
