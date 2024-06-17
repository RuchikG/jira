package com.walmart.coffee.squad.test;

import com.walmart.coffee.squad.test.ElasticSearch.ElasticSearchQueries;
import com.walmart.coffee.squad.test.ElasticSearch.ElasticSearchRequest;
import com.walmart.coffee.squad.test.model.ResponseDocument;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
public class CronJob {

    @Autowired
    ElasticSearchQueries elasticSearchQueries;

    @Autowired
    ElasticSearchRequest elasticSearchRequest;

    @Autowired
    AsyncExecutor asyncExecutor;

    @Scheduled(cron = "*/15 * * * * ?")
    public void run() {
        List<String> uniqueIds = elasticSearchQueries.getUniqueIdFromElasticSearch();
        submitTask(uniqueIds);
    }

    public void submitTask(List<String> uniqueIds) {
        Assert.notNull(uniqueIds, "UniqueIds Cannot be Null");
        for (String uniqueId: uniqueIds) {
            Runnable task = () -> flow(uniqueId);
            asyncExecutor.executeRunnableTask(task);
        }
    }

    public void flow(String uniqueId) {
        SearchSourceBuilder queryRequest = elasticSearchRequest.buildRequest(uniqueId);
        ResponseDocument responseDocument = elasticSearchQueries.getDocsFromES(queryRequest);
//        consolidateEvent();
//        createDTOforJira();
    }
}
