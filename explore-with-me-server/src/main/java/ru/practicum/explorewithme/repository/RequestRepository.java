package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.explorewithme.model.EventRequest;

import java.util.List;

public interface RequestRepository extends CrudRepository<EventRequest, Long> {
    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    List<EventRequest> findAllByRequesterId(Long userId);

    @Query("select r from EventRequest as r where r.event.id = :eventId and r.event.initiator.id = :userId")
    List<EventRequest> findAllByRequesterIdAndEventId(Long userId, Long eventId);
}
