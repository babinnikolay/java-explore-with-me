package ru.practicum.explorewithme.statisticclient;


import ru.practicum.explorewithme.model.dto.EndpointHit;

public interface StatisticClient {
    void hit(EndpointHit endpointHit);
}
