package com.InstinctOne.BlogApp.services;

import com.InstinctOne.BlogApp.dtos.TagDto;
import com.InstinctOne.BlogApp.dtos.TagRequest;
import com.InstinctOne.BlogApp.entities.Tag;
import com.InstinctOne.BlogApp.exceptions.TagNotFound;
import com.InstinctOne.BlogApp.mappers.MapDtos;
import com.InstinctOne.BlogApp.repositories.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final MapDtos mapDtos;

    public TagService(TagRepository tagRepository, MapDtos mapDtos) {
        this.tagRepository = tagRepository;
        this.mapDtos = mapDtos;
    }

    public TagDto createTag(TagRequest request) {
        Tag tag = new Tag();
        tag.setName(request.name());
        tagRepository.save(tag);
        return mapDtos.mapTagToTagDto(tag);
    }


    public void deleteTag(TagRequest request) {
        Tag tag = tagRepository.findByName(request.name());
        if (tag == null){
            throw new TagNotFound("Tag "+request.name()+" is not found");
        }
        tagRepository.delete(tag);
    }
}
