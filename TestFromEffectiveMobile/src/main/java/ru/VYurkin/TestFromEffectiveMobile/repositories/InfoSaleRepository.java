package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.VYurkin.TestFromEffectiveMobile.models.product.InfoSale;

@Repository
public interface InfoSaleRepository extends JpaRepository<InfoSale, Long> {
}
