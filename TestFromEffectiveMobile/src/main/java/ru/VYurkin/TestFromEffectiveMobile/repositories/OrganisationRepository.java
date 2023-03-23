package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;

import java.util.Optional;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {
    Optional<Organisation> findByNameAndIsActive(String name, boolean isActive);
    Optional<Organisation> findByName(String name);
}
