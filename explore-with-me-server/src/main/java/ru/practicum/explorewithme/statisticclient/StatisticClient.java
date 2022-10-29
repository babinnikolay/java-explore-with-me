package ru.practicum.explorewithme.statisticclient;


import ru.practicum.explorewithme.model.dto.EndpointHit;
import ru.practicum.explorewithme.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticClient {
    void hit(EndpointHit endpointHit);

    List<ViewStats> stats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
