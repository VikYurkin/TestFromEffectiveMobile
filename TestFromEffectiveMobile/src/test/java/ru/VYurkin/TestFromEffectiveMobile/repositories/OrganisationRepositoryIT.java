package ru.VYurkin.TestFromEffectiveMobile.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.VYurkin.TestFromEffectiveMobile.models.Organisation;

import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts={"/sql/clearDbs.sql", "/sql/organisation/addOrganisation.sql"})
public class OrganisationRepositoryIT {

    @Autowired
    private OrganisationRepository organisationRepository;


    @Test
    public void shouldFindByNameAndIsActive(){
        //given

        Optional<Organisation> organisation = organisationRepository.findByNameAndIsActive("Org1", true);

        //then
        Assertions.assertTrue(organisation.isPresent());
        Assertions.assertEquals(1, organisation.get().getOrganisationId());
    }

    @Test
    public void shouldFindByName(){
        //given
        Optional<Organisation> organisation = organisationRepository.findByName("Org2");

        //then
        Assertions.assertTrue(organisation.isPresent());
        Assertions.assertEquals(2, organisation.get().getOrganisationId());
    }


}
