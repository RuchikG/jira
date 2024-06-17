package com.walmart.coffee.squad.test.ElasticSearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

public class ElasticSearchRequest {

    /*
    TODO: "timeStamp" field to be saved on ElasticSearch to sort the events in descending order
     */

    public SearchSourceBuilder buildRequest(String uniqueId) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.idsQuery().addIds(uniqueId));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort(new FieldSortBuilder("timeStamp").order(SortOrder.DESC));
        return searchSourceBuilder;
    }
}
