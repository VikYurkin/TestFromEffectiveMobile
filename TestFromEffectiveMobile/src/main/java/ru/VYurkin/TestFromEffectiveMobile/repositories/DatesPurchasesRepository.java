package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.VYurkin.TestFromEffectiveMobile.models.user.DatesPurchases;

@Repository
public interface DatesPurchasesRepository extends JpaRepository<DatesPurchases, Long> {
}
