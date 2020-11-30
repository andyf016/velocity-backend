package com.velocitybackend.roomassn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Resident.
 */
@Entity
@Table(name = "resident")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resident implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "residents", allowSetters = true)
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Resident name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getRoom() {
        return room;
    }

    public Resident room(Room room) {
        this.room = room;
        return this;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resident)) {
            return false;
        }
        return id != null && id.equals(((Resident) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resident{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
