package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CodeService {

    @Autowired
    CodeRepository repository;

    // save
    void save(Code code) {
        repository.save(code);
    }

    // find by id
    Optional<Code> findById(String id) {
        return repository.findById(id);
    }
    Code getById(String id) { return repository.getById(id);}

    // find all
    List<Code> findAll() {
        return repository.findAll();//Sort.by("load_date").descending()
    }

    List<Code> findAllNoSecrets() {return repository.findAllNoSecrets();};

    // count
    long count() {
        return repository.count();
    }

    // delete
    void deleteById(String id) {repository.deleteById(id);}
}
