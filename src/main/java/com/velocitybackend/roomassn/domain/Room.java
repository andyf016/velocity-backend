package com.velocitybackend.roomassn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @ManyToOne
    @JsonIgnoreProperties(value = "rooms", allowSetters = true)
    private Facility facility;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public Room number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Facility getFacility() {
        return facility;
    }

    public Room facility(Facility facility) {
        this.facility = facility;
        return this;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return id != null && id.equals(((Room) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", number=" + getNumber() +
            "}";
    }
}
