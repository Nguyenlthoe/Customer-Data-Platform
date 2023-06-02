package bk.edu.service;

import bk.edu.data.entity.AuthorEntity;
import bk.edu.data.mapper.AuthorMapper;
import bk.edu.data.req.AuthorRequest;
import bk.edu.exception.ServiceAdminRequestInvalid;
import bk.edu.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public AuthorEntity updateAuthor(AuthorRequest authorRequest, int authorId){
      AuthorEntity authorEntity = authorRepository.findByAuthorId(authorId);
      if(authorEntity == null){
          throw new ServiceAdminRequestInvalid("Author not found");
      }
      if(authorRequest.getName() != null){
          authorEntity.setName(authorRequest.getName());
      }
      if(authorRequest.getDescription() != null){
          authorEntity.setName(authorRequest.getDescription());
      }
      authorRepository.saveAndFlush(authorEntity);

      return authorEntity;
    };

    public List<AuthorEntity> getAllAuthor() {
        return authorRepository.findAll();
    }

    public AuthorEntity getDetailAuthor(int id) {
        return authorRepository.findByAuthorId(id);
    }
}
