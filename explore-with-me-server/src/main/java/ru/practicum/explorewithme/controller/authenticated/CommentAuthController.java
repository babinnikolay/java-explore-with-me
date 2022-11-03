package ru.practicum.explorewithme.controller.authenticated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.dto.CommentDto;
import ru.practicum.explorewithme.service.CommentService;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users/{userId}/comments")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CommentAuthController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Object> createComment(@PathVariable Long userId,
                                                @Valid @RequestBody CommentDto commentDto)
            throws NotFoundException, BadRequestException {
        log.info("Create comment userId={}, commentDto={}", userId, commentDto);
        return ResponseEntity.ok(commentService.createComment(userId, commentDto));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable Long userId,
                                                @PathVariable Long commentId,
                                                @Valid @RequestBody CommentDto commentDto)
            throws NotFoundException, BadRequestException {
        log.info("Update comment userId={}, commentId={}, commentDto={}", userId, commentId, commentDto);
        return ResponseEntity.ok(commentService.updateComment(userId, commentId, commentDto));
    }

    @GetMapping
    public ResponseEntity<Object> getComments(@PathVariable Long userId) throws NotFoundException {
        log.info("Get all comments userId={}", userId);
        return ResponseEntity.ok(commentService.getAllComments(userId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Object> getComment(@PathVariable Long userId, @PathVariable Long commentId)
            throws NotFoundException, BadRequestException {
        log.info("Get comment userId={}, commentId={}", userId, commentId);
        return ResponseEntity.ok(commentService.getComment(userId, commentId));
    }
}
