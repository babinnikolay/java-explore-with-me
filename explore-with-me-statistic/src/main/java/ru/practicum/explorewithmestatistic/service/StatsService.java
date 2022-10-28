package ru.practicum.explorewithmestatistic.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithmestatistic.model.dto.EndpointHit;
import ru.practicum.explorewithmestatistic.model.dto.ViewStats;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmestatistic.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;

    public EndpointHit createHit(EndpointHit endpointHit) {
        return statsRepository.save(endpointHit);
    }

    public Collection<ViewStats> getHits(LocalDateTime start,
                                         LocalDateTime end,
                                         List<String> uris,
                                         Boolean unique) {
        return statsRepository.findAllByStartEndTime(start, end, uris, unique);
    }
}
