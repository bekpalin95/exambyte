package exambyte.adapters.db;

import exambyte.domain.user.Studi;
import exambyte.domain.user.UserAntwort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface StudiRepoDAO extends CrudRepository<Studi, Integer> {

  List<Studi> findAll();

  Studi findById(int id);

  Studi save(Studi studi);

  Studi findByUsername(String username);
}
