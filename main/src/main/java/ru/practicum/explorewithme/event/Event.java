package ru.practicum.explorewithme.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.compilation.Compilation;
import ru.practicum.explorewithme.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;


@Data
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;

    @Column
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date")
    @NotNull
    private LocalDateTime eventDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    @NotNull
    private Location location;

    @Column
    @NotNull
    private Boolean paid;

    @Column(name = "participant_limit")
    @NotNull
    private Integer participantLimit;

    @Column(name = "request_moderation")
    @NotNull
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    @NotNull
    private State state;

    @Column
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "initiator_id")
    @NotNull
    private User initiator;

    @Column(name = "created_on")
    @NotNull
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "events_compilations",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"))
    private Collection<Compilation> compilations;
}
