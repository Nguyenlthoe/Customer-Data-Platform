package bk.edu.service;

import bk.edu.data.entity.AuthorEntity;
import bk.edu.data.mapper.AuthorMapper;
import bk.edu.data.req.AuthorRequest;
import bk.edu.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AuthorMapper authorMapper;

    public AuthorEntity createAuthor(AuthorRequest authorRequest){
        AuthorEntity authorEntity = authorMapper.authorRequestToDto(authorRequest);
        authorRepository.saveAndFlush(authorEntity);
        return authorEntity;
    }

    public Page<AuthorEntity> getListAuthor(Pageable pageable){
        Page<AuthorEntity> authorEntities = authorRepository.findAll(pageable);
        return authorEntities;
    }
}
