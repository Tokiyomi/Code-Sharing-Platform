package platform;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CodeRepository extends JpaRepository<Code, String> {
    @Override
    List<Code> findAll();

    @Override
    Code getById(String s);

    @Query("SELECT c FROM Code c WHERE c.is_secret = FALSE")
    List<Code> findAllNoSecrets();

    @Override
    <S extends Code> S save(S entity);

    @Override
    long count();

    @Override
    Optional<Code> findById(String s);

    @Override
    void deleteById(String s);
}
