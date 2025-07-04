package exambyte.adapters.db;

import exambyte.domain.test.Test;
import exambyte.services.repositorys.TestRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TestRepositoryImpl implements TestRepository {

  private TestRepoDAO repo;

  public TestRepositoryImpl(TestRepoDAO repo) {
    this.repo = repo;
  }

  @Override
  public List<Test> findAll() {
    return repo.findAll();
  }

  @Override
  public Test findById(long id) {
    return repo.findById(id);
  }

  @Override
  public void save(Test test) {
    repo.save(test);
  }

  @Override
  public void delete(Test test) {
    repo.delete(test);
  }
}
