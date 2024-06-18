package com.walmart.coffee.squad.test.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PullRequest {
    private String number;
    private String state;
    private String title;
    private User user;
    private String body;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("closed_at")
    private String closedAt;
    private Boolean merged;
    @JsonProperty("merged_at")
    private String mergedAt;
    @JsonProperty("requested_reviewers")
    private List<Reviewer> reviewers;
    private List<Label> labels;
    private Boolean draft;
}
