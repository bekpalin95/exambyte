package exambyte.adapters.db;

import exambyte.domain.user.Studi;
import exambyte.services.repositorys.StudiRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class StudiRepositoryImpl implements StudiRepository {

  private StudiRepoDAO repo;

  public StudiRepositoryImpl(StudiRepoDAO repo) {
    this.repo = repo;
  }

  @Override
  public List<Studi> findAll() {
    return repo.findAll();
  }

  @Override
  public Studi findById(int id) {
    return repo.findById(id);
  }

  @Override
  public Studi findByUsername(String username) {
    return repo.findByUsername(username);
  }

  @Override
  public void save(Studi studi) {
    repo.save(studi);
  }
}
