package exambyte.services.repositorys;

import exambyte.domain.user.Studi;
import java.util.List;

public interface StudiRepository {

  List<Studi> findAll();

  Studi findById(int id);

  Studi findByUsername(String username);

  void save(Studi studi);

}
