package com.walmart.coffee.squad.test.restservices;

import com.walmart.coffee.squad.test.entity.JiraPayloadDTO;
import com.walmart.coffee.squad.test.enums.JiraStatus;

public interface JiraService {

    public void updateJiraStory(String issueId, JiraPayloadDTO jiraPayload, JiraStatus status);
}
