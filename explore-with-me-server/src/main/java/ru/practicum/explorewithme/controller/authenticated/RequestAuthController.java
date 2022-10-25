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

import javax.validation.constraints.NotNull;

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
        return requestService.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> createRequest(@PathVariable Long userId, @RequestParam Long eventId)
            throws NotFoundException, BadRequestException {
        log.info("Create request userId={}, eventId={}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId)
            throws NotFoundException {
        log.info("Cancel request userId={}, requestId={}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> getEventRequests(@PathVariable @NotNull Long userId,
                                                   @PathVariable @NotNull Long eventId) {
        log.info("Get event {} requests for user {}", eventId, userId);
        return requestService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> confirmRequest(@PathVariable @NotNull Long userId,
                                                 @PathVariable @NotNull Long eventId,
                                                 @PathVariable @NotNull Long reqId)
            throws NotFoundException, BadRequestException {
        log.info("Confirm request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> rejectRequest(@PathVariable @NotNull Long userId,
                                                 @PathVariable @NotNull Long eventId,
                                                 @PathVariable @NotNull Long reqId)
            throws NotFoundException, BadRequestException {
        log.info("Reject request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return requestService.rejectRequest(userId, eventId, reqId);
    }
}
