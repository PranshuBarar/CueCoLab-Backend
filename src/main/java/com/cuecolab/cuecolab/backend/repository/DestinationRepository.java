package com.cuecolab.cuecolab.backend.repository;

import com.cuecolab.cuecolab.backend.entities.DestinationEntity;
import com.cuecolab.cuecolab.backend.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, UUID>  {

}
