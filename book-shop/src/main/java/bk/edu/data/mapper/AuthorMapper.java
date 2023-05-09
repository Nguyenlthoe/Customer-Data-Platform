package bk.edu.data.mapper;

import bk.edu.data.entity.AuthorEntity;
import bk.edu.data.req.AuthorRequest;
import bk.edu.data.response.dto.AuthorDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorMapper {
    public AuthorDto authorEntityToDto(AuthorEntity authorEntity){
        return new AuthorDto(authorEntity.getAuthorId(), authorEntity.getName());
    }

    public AuthorEntity authorRequestToDto(AuthorRequest authorRequest){
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setName(authorRequest.getName());
        authorEntity.setDescription(authorRequest.getDescription());
        return authorEntity;
    }

    public List<AuthorDto> listAuthorEntityToDto(List<AuthorEntity> authorEntities){
        List<AuthorDto> authors = new ArrayList<>();
        authorEntities.forEach(authorEntity -> {
            authors.add(authorEntityToDto(authorEntity));
        });
        return authors;
    }
}
