package com.cuecolab.cuecolab.backend.entities;


import com.cuecolab.cuecolab.backend.enums.DestinationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "destination_entity")
public class DestinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID destinationId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DestinationType destinationType;

    @NotNull
    private String destinationUserName;

    @NotNull
    private String destinationUserEmail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @NotNull
    private String destinationAccessToken;

    @NotNull
    private String destinationRefreshToken;
}
