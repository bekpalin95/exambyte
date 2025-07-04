package exambyte.adapters.db;

import exambyte.domain.test.Test;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TestRepoDAO extends CrudRepository<Test, Long> {

  List<Test> findAll();

  Test findById(long id);

  Test save(Test test);

  Test findByName(String name);

}
