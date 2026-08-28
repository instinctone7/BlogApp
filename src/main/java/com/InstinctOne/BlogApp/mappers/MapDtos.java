package com.InstinctOne.BlogApp.mappers;

import com.InstinctOne.BlogApp.dtos.RegisterVerify;
import com.InstinctOne.BlogApp.dtos.TagDto;
import com.InstinctOne.BlogApp.dtos.UserDto;
import com.InstinctOne.BlogApp.entities.Category;
import com.InstinctOne.BlogApp.entities.Tag;
import com.InstinctOne.BlogApp.entities.User;
import com.InstinctOne.BlogApp.entities.UserToken;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface MapDtos {

    UserDto mapUserToDto(User user);
    RegisterVerify mapTokenToURL(UserToken userToken);
    TagDto mapTagToTagDto(Tag tag);
    TagDto.CategoryDto mapCategoryToDto(Category category);
}
