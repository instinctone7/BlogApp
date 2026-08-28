package com.InstinctOne.BlogApp.controllers;

import com.InstinctOne.BlogApp.dtos.TagDto;
import com.InstinctOne.BlogApp.dtos.TagRequest;
import com.InstinctOne.BlogApp.services.TagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/create")
    public ResponseEntity<TagDto> createTags(@RequestBody @Valid TagRequest request){
        TagDto response = tagService.createTag(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> deleteTag(@RequestBody TagRequest request){
        tagService.deleteTag(request);
        return ResponseEntity.noContent().build();
    }
}
