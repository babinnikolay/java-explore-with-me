package ru.practicum.explorewithme.statisticclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.model.dto.EndpointHit;

@Service
@Slf4j
public class StatisticClientHTTP implements StatisticClient {
    private final RestTemplate restTemplate;

    public StatisticClientHTTP(@Value("${stats.server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build();
    }

    @Override
    public void hit(EndpointHit endpointHit) {
        log.info("Hit this {} server {}", endpointHit, restTemplate.getUriTemplateHandler());
        restTemplate.postForObject("/hit", endpointHit, String.class);
    }
}
