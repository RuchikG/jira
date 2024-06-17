package com.walmart.coffee.squad.test.restservices;

import org.springframework.http.ResponseEntity;

public interface JiraService {

    public ResponseEntity updateJiraStory(String clientName, String consumerId, String scopeRegex, String team);
}
