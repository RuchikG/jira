package com.walmart.coffee.squad.test.restservices;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.walmart.coffee.squad.test.entity.JiraPayloadDTO;
import com.walmart.coffee.squad.test.entity.JiraTransitionDTO;
import com.walmart.coffee.squad.test.enums.JiraStatus;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.client.methods.HttpPost;
import org.springframework.stereotype.Service;

import java.util.Base64;

import static com.walmart.coffee.squad.test.constants.Constants.*;

@Service
public class JiraServiceImpl implements JiraService {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public void updateJiraStory(String issueId, JiraPayloadDTO jiraPayload, JiraStatus status) {
        String url = JIRA_BASE_URL + issueId + JIRA_TRANSITION_ENDPOINT;
        String jiraAuth = jiraAuthorization();

        String transitionId = String.valueOf(status.getValue());
        JiraTransitionDTO jiraTransitionDTO = new JiraTransitionDTO(transitionId);
        jiraPayload.setTransition(jiraTransitionDTO);

        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("accept", "*/*");
        postRequest.addHeader("content-type", "application/json");
        postRequest.addHeader(AUTHORIZATION, jiraAuth);
        StringEntity stringEntity;
        try {
            stringEntity = new StringEntity(objectMapper.writeValueAsString(jiraPayload));
            postRequest.setEntity(stringEntity);
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpResponse response1 = httpclient.execute(postRequest);
                StatusLine responseStatus = response1.getStatusLine();
                int statusCode = responseStatus != null ? responseStatus.getStatusCode() : 0;
                if (statusCode == 204) {
                    log.info("Successfully updated Jira: " + issueId);
                } else {
                    log.info("Failed while updating Jira: " + issueId);
                }
            } catch (Exception e) {
                log.info("Failed while updating processing update for Jira: " + issueId);
            }
        } catch (Exception e) {
            log.info("Failed while updating processing update for Jira: " + issueId);
        }
    }

    private String jiraAuthorization() {
        String valueToEncode = JIRA_USERNAME + ":" + JIRA_PASSWORD;
        String basicAuthenticationHeader = "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
        return basicAuthenticationHeader;
    }
}
