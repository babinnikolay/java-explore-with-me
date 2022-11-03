package ru.practicum.explorewithme.controller.authenticated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.service.RequestService;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestAuthController {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public ResponseEntity<Object> getRequests(@PathVariable Long userId) throws NotFoundException {
        log.info("Get requests userId={}", userId);
        return ResponseEntity.ok(requestService.getRequests(userId));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> createRequest(@PathVariable Long userId, @RequestParam Long eventId)
            throws NotFoundException, BadRequestException {
        log.info("Create request userId={}, eventId={}", userId, eventId);
        return ResponseEntity.ok(requestService.createRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId)
            throws NotFoundException {
        log.info("Cancel request userId={}, requestId={}", userId, requestId);
        return ResponseEntity.ok(requestService.cancelRequest(userId, requestId));
    }
}
