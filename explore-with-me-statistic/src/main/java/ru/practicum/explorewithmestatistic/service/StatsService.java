package ru.practicum.explorewithmestatistic.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithmestatistic.model.dto.EndpointHit;
import ru.practicum.explorewithmestatistic.model.dto.ViewStats;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmestatistic.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;

    public ResponseEntity<Object> createHit(EndpointHit endpointHit) {
        EndpointHit savedHit = statsRepository.save(endpointHit);
        return ResponseEntity.ok(savedHit);
    }

    public ResponseEntity<Object> getHits(LocalDateTime start,
                                          LocalDateTime end,
                                          List<String> uris,
                                          Boolean unique) {
        Collection<ViewStats> stats = statsRepository.findAllByStartEndTime(start, end, uris, unique);
        return ResponseEntity.ok(stats);
    }
}
