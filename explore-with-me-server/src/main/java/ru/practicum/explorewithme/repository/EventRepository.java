package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.model.dto.FilterEventAdminRequest;
import ru.practicum.explorewithme.model.dto.FilterEventOpenRequest;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long userId, PageRequest page);

    Optional<Event> findByInitiatorIdAndId(Long userId, Long id);

    Optional<Event> findByIdAndState(Long id, EventState state);

    Boolean existsByCategoryId(Long id);

    @Query("select e from Event as e " +
            "where (:#{#filter.users} is null or e.initiator.id in :#{#filter.users})" +
            " and (:#{#filter.categories} is null or e.category.id in :#{#filter.categories})" +
            " and (:#{#filter.states} is null or e.state in :#{#filter.states})" +
            " and (cast( :#{#filter.rangeStart} as date) is null or e.eventDate >= :#{#filter.rangeStart})" +
            " and (cast(:#{#filter.rangeEnd} as date) is null or e.eventDate <= :#{#filter.rangeEnd})")
    List<Event> findAllByFilter(@Param("filter") FilterEventAdminRequest filter, PageRequest page);

    @Query("select e from Event as e " +
            "where (:#{#filter.text} is null or (lower(e.annotation) LIKE lower(:#{#filter.text})) " +
                "or (lower(e.description) LIKE lower(:#{#filter.text})) " +
            " and (:#{#filter.categories} is null or e.category.id in :#{#filter.categories})" +
            " and (:#{#filter.paid} is null or e.paid = :#{#filter.paid})" +
            " and (cast( :#{#filter.rangeStart} as date) is null or e.eventDate >= :#{#filter.rangeStart})" +
            " and (cast(:#{#filter.rangeEnd} as date) is null or e.eventDate <= :#{#filter.rangeEnd})" +
            " and (e.state = :#{#filter.state})" +
            " and (:#{#filter.onlyAvailable} is null or e.available = :#{#filter.onlyAvailable}))")
    List<Event> findAllByFilter(@Param("filter") FilterEventOpenRequest filter, PageRequest page);
}
