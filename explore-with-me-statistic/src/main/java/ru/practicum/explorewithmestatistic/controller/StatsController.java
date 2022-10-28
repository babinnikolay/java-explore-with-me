package ru.practicum.explorewithmestatistic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.explorewithmestatistic.model.dto.EndpointHit;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmestatistic.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Object> createHit(@RequestBody @Valid EndpointHit endpointHit) {
        log.info("Create hit {}", endpointHit);
        return ResponseEntity.ok(statsService.createHit(endpointHit));
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getHits(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Get hits start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return ResponseEntity.ok(statsService.getHits(start, end, uris, unique));
    }
}
