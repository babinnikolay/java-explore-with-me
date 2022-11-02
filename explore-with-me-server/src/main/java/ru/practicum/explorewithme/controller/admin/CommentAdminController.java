package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.dto.CommentDto;
import ru.practicum.explorewithme.model.dto.FilterCommentAdminRequest;
import ru.practicum.explorewithme.service.CommentService;

@Controller
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentAdminController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable Long commentId, @RequestBody CommentDto commentDto)
            throws NotFoundException {
        log.info("Admin update comment commentId={}, commentDto={}", commentId, commentDto);
        return ResponseEntity.ok(commentService.updateAdminComment(commentId, commentDto));
    }

    @GetMapping
    public ResponseEntity<Object> getAllComments(@ModelAttribute FilterCommentAdminRequest filter) {
        log.info("Admin get all comments filter={}", filter);
        return ResponseEntity.ok(commentService.getAllAdminComments(filter));
    }
}
