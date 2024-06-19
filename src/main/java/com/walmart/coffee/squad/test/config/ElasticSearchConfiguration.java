package com.walmart.coffee.squad.test.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.apache.http.client.config.RequestConfig.Builder;

@Configuration
@Slf4j
public class ElasticSearchConfiguration {

    private volatile static RestHighLevelClient elasticRestClient;
    
    @PostConstruct
    private void restHighLevelClient() throws Exception {
        try {

        	List<String> esHosts = Arrays.asList("es-stg-1-1395146024.prod.us.walmart.net:9200,es-stg-2-1395146027.prod.us.walmart.net:9200,es-stg-3-1395146030.prod.us.walmart.net:9200".split(","));
        	log.info("ElasticSearchConfiguration: connecting to hosts :{}",esHosts);
            if (!esHosts.isEmpty()) {
            	this.elasticRestClient = getRestHighLevelClient(esHosts);
            }
        } catch (Exception e) {
            log.info("Error while instantiating the ElasticSearchClient", e);
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    private void shutdown() {
    	try {
            if (Objects.nonNull(this.elasticRestClient)) {
                log.info(
                        "[ElasticSearchConfiguration] Closing Elastic Search connection.");
                this.elasticRestClient.close();
            }
        } catch (IOException e) {
            log.error("[ElasticSearchConfiguration][shutdown] failed : " + e.getMessage());
        }
    }

    private RestHighLevelClient getRestHighLevelClient(List<String> esHosts) throws Exception {
    	log.info("ElasticSearchConfiguration:getRestHighLevelClient :: initializing");
        RestHighLevelClient client;
        if (!CollectionUtils.isEmpty(esHosts)) {
            HttpHost[] httpHostList = new HttpHost[esHosts.size()];
            for (int i = 0; i < esHosts.size(); i++) {
                String[] hostPort = esHosts.get(i).split(":");
                httpHostList[i] = new HttpHost(hostPort[0].trim(), Integer.parseInt(hostPort[1].trim()), "https");
            }
            RestClientBuilder builder = RestClient.builder(httpHostList)
                    .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                        @Override
                        public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                            return requestConfigBuilder.setConnectTimeout(10000)
                                    .setSocketTimeout(100000);
                        }
                    }).setFailureListener(new RestClient.FailureListener() {
                        @Override
                        public void onFailure(Node node) {
                            log.error("ElasticSearchConfiguration FailureListener node :{}", node);
                            super.onFailure(node);
                        }
                    }).setHttpClientConfigCallback(getHttpClientConfigCallback());

            client = new RestHighLevelClient(builder);
            log.info("ElasticSearchConfiguration:getRestHighLevelClient Configuration completed");
        } else {
            throw new Exception("ElasticSearchConfiguration:getRestHighLevelClient Unable to initialize ElasticSearch Client");
        }
        return client;
    }

    private RestClientBuilder.HttpClientConfigCallback getHttpClientConfigCallback() {
        return httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.setDefaultCredentialsProvider(getCredentialsProvider());
            return httpAsyncClientBuilder;
        };
    }

    private CredentialsProvider getCredentialsProvider() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("SVC-DL-ES-READWRITE", "uK6H5G9XnT3mQLp"));
        return credentialsProvider;
    }

    public static RestHighLevelClient getElasticRestClient() {
        return elasticRestClient;
    }
}