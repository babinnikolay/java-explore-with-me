package ru.practicum.explorewithme.controller.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.dto.FilterEventOpenRequest;
import ru.practicum.explorewithme.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventOpenController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Object> getEvents(@Valid @ModelAttribute FilterEventOpenRequest filter,
                                            HttpServletRequest request) {
        log.info("Get events request filter={}, request={}", filter, request);
        return ResponseEntity.ok(eventService.getEvents(filter, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEvent(@NotNull @PathVariable Long id,
                                           HttpServletRequest request) throws NotFoundException {
        log.info("Get event request id={}, request={}", id, request);
        return ResponseEntity.ok(eventService.getEvent(id, request));
    }
}
