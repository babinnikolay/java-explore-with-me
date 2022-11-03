package ru.practicum.explorewithme.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String annotation;
    @OneToOne
    @JoinColumn(name = "category")
    private Category category;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    @OneToOne
    @JoinColumn(name = "initiator")
    private User initiator;
    private Float locationLat;
    private Float locationLon;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private PublicationStatus state;
    private String title;
    private boolean available;
    @Transient
    private int views;
    @OneToMany
    @JoinColumn(name = "event_request_id")
    private List<EventRequest> requests = new ArrayList<>();
}
