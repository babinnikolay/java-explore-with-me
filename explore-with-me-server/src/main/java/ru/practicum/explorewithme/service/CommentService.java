package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.PublicationStatus;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.dto.CommentDto;
import ru.practicum.explorewithme.model.dto.FilterCommentAdminRequest;
import ru.practicum.explorewithme.model.mapper.CommentMapper;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    public CommentDto createComment(Long userId, CommentDto commentDto) throws NotFoundException, BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User %s not found", userId)));
        Event event = eventRepository.findById(commentDto.getEvent()).orElseThrow(() ->
                new NotFoundException(String.format("Event %s not found", commentDto.getEvent())));
        if (event.getState() != PublicationStatus.PUBLISHED) {
            throw new BadRequestException(String.format("Event %s not published", event.getId()));
        }
        Comment comment = commentMapper.toComment(commentDto);
        comment.setCreated(LocalDateTime.now());
        comment.setUser(user);
        comment.setEvent(event);
        comment.setStatus(PublicationStatus.PENDING);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(savedComment);
    }

    public CommentDto updateComment(Long userId, Long commentId, CommentDto commentDto)
            throws NotFoundException, BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User %s not found", userId)));
        Event event = eventRepository.findById(commentDto.getEvent()).orElseThrow(() ->
                new NotFoundException(String.format("Event %s not found", commentDto.getEvent())));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment %s not found", commentId)));
        if (!userId.equals(comment.getUser().getId())) {
            throw new BadRequestException(String.format("User %s not owner %s comment", userId, commentId));
        }
        comment.setUser(user);
        comment.setEvent(event);
        if (commentDto.getStatus() != PublicationStatus.PUBLISHED) {
            comment.setStatus(commentDto.getStatus());
        }
        if (!comment.getText().equals(commentDto.getText())) {
            comment.setStatus(PublicationStatus.PENDING);
        }
        comment.setText(commentDto.getText());
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(savedComment);
    }

    public List<CommentDto> getAllComments(Long userId) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User %s not found", userId));
        }
        List<Comment> comments = commentRepository.findAllByUserId(userId);
        return comments.stream().map(commentMapper::toCommentDto).collect(Collectors.toList());
    }

    public CommentDto getComment(Long userId, Long commentId) throws NotFoundException, BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User %s not found", userId))
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment %s not found", commentId)));
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(String.format("User %s not owner comment %s", userId, commentId));
        }
        return commentMapper.toCommentDto(comment);
    }

    public CommentDto updateAdminComment(Long commentId, CommentDto commentDto) throws NotFoundException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException(String.format("Comment %s not found", commentId)));
        if (commentDto.getStatus() != null) {
            comment.setStatus(commentDto.getStatus());
        }
        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
        }
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDto(savedComment);
    }

    public List<CommentDto> getAllAdminComments(FilterCommentAdminRequest filter) {
        PageRequest page = PageRequest.of(filter.getFrom(), filter.getSize());
        if (filter.getText() != null) {
            filter.setText(("%" + filter.getText() + "%"));
        }
        List<Comment> comments = commentRepository.findAllByFilter(filter, page);
        return comments.stream().map(commentMapper::toCommentDto).collect(Collectors.toList());
    }
}
