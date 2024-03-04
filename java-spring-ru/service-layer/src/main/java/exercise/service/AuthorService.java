package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository repository;

    @Autowired
    private AuthorMapper authorMapper;

    public List<AuthorDTO> getAll() {
        var authors = repository.findAll();
        return authors.stream()
                .map(authorMapper::map)
                .toList();
    }

    public AuthorDTO create(AuthorCreateDTO authorData) {
        var author = authorMapper.map(authorData);
        repository.save(author);
        return authorMapper.map(author);
    }

    public AuthorDTO findById(Long id) {
        var author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return authorMapper.map(author);
    }

    public AuthorDTO update(AuthorUpdateDTO authorData, Long id) {
        var author = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        authorMapper.update(authorData, author);
        repository.save(author);
        return authorMapper.map(author);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
