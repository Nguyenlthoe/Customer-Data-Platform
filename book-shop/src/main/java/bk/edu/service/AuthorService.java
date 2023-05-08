package bk.edu.service;

import bk.edu.data.entity.AuthorEntity;
import bk.edu.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    public AuthorEntity createAuthor(String name, String description){
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(name);
        authorEntity.setDescription(description);
        authorRepository.saveAndFlush(authorEntity);
        return authorEntity;
    }

    public Page<AuthorEntity> getListAuthor(Pageable pageable){
        Page<AuthorEntity> authorEntities = authorRepository.findAll(pageable);
        return authorEntities;
    }
}
