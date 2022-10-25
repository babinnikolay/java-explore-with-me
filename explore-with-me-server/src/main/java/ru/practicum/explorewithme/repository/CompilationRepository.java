package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.model.Compilation;

import java.util.Collection;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c from Compilation as c where :pinned is null or c.pinned = :pinned")
    Collection<Compilation> findAll(@Param("pinned") Boolean pinned, PageRequest page);
}
