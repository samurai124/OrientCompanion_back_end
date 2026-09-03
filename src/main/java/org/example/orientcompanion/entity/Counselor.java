package org.example.orientcompanion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.orientcompanion.entity.Field;
import org.example.orientcompanion.enums.Role;

@Entity
@Table(name = "counselors")
@DiscriminatorValue("COUNSELOR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Counselor extends User {

    @Lob
    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_field_id")
    private Field specialtyField;

    @Override
    public Role getRole() {
        return Role.COUNSELOR;
    }
}