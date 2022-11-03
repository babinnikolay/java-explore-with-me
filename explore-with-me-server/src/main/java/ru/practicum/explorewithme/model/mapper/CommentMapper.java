package ru.practicum.explorewithme.model.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.dto.CommentDto;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final ModelMapper mapper;

    public Comment toComment(CommentDto commentDto) {
        return mapper.map(commentDto, Comment.class);
    }

    public CommentDto toCommentDto(Comment comment) {
        TypeMap<Comment, CommentDto> typeMap = mapper.getTypeMap(Comment.class,
                CommentDto.class);
        if (typeMap == null) {
            typeMap = mapper.createTypeMap(Comment.class, CommentDto.class);
            typeMap.addMappings(m -> m.skip(CommentDto::setEvent));
        }
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        commentDto.setEvent(comment.getEvent().getId());
        return commentDto;
    }
}
