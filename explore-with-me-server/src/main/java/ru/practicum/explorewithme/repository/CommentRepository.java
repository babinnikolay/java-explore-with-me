package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.PublicationStatus;
import ru.practicum.explorewithme.model.dto.FilterCommentAdminRequest;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserId(Long userId);

    List<Comment> findAllByEventIdAndStatus(Long eventId, PublicationStatus status);

    @Query("select c from Comment as c " +
            "where (:#{#filter.text} is null or lower(c.text) LIKE lower(:#{filter.text})) " +
            "and (cast(:#{#filter.start} as date) is null or c.created >= :#{#filter.start}) " +
            "and (cast(:#{#filter.end} as date) is null or c.created <= :#{#filter.end}) " +
            "and (:#{#filter.events} is null or c.event.id in :#{#filter.events}) " +
            "and (:#{#filter.users}) is null or c.user.id in :#{#filter.users} " +
            "and (:#{#filter.status} is null or c.status = :#{#filter.status}) ")
    List<Comment> findAllByFilter(@Param("filter") FilterCommentAdminRequest filter, PageRequest page);
}
