package com.rakib.ms_team_demo.teams;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class TeamsWebHookIntegration {

    private static final String url = "{connection-url}";
    private static final Logger LOG = LoggerFactory.getLogger(TeamsWebHookIntegration.class);

    public static void createWebHook(String title, String message) {
        List<Sections> sectionsList = new ArrayList<>();
        Sections sections = new Sections();
        sections.setActivityTitle(title);
        sections.setFacts(Collections.singletonList(Facts.builder().name("Action").value(message).build()));
        sections.setMarkdown(true);
        sectionsList.add(sections);
        sendWebHook(sectionsList, sections.getActivityTitle());
    }

    private static void sendWebHook(List<Sections> sectionsList, String summary) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> map = new HashMap<>();
        map.put("@type", "MessageCard");
        map.put("@context", "http://schema.org/extensions");
        map.put("@themeColor", "0076D 7");
        map.put("summary", summary);
        map.put("sections", sectionsList);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        LOG.info("SendWebHook summary = {}, response = {}", summary, response.getStatusCode());
    }


}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Sections {
    private String activityTitle;
    private List<Facts> facts;
    private boolean markdown;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Facts {
    private String name;
    private String value;
}

