package exambyte.services.repositorys;

import exambyte.domain.test.Test;
import java.util.List;

public interface TestRepository {

  List<Test> findAll();

  Test findById(long id);

  void save(Test test);

  void delete(Test test);

}
