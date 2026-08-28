package com.InstinctOne.BlogApp.services;

import com.InstinctOne.BlogApp.dtos.PostRequest;
import com.InstinctOne.BlogApp.dtos.PostResponse;
import com.InstinctOne.BlogApp.entities.Category;
import com.InstinctOne.BlogApp.entities.Post;
import com.InstinctOne.BlogApp.entities.Tag;
import com.InstinctOne.BlogApp.entities.User;
import com.InstinctOne.BlogApp.enums.PostStatus;
import com.InstinctOne.BlogApp.exceptions.CategoryNotFound;
import com.InstinctOne.BlogApp.exceptions.PostNotFound;
import com.InstinctOne.BlogApp.exceptions.TagNotFound;
import com.InstinctOne.BlogApp.exceptions.UserNotFound;
import com.InstinctOne.BlogApp.repositories.CategoryRepository;
import com.InstinctOne.BlogApp.repositories.PostRepository;
import com.InstinctOne.BlogApp.repositories.TagRepository;
import com.InstinctOne.BlogApp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final TagRepository tagRepository;

	public PostService(PostRepository postRepository,
					   UserRepository userRepository,
					   CategoryRepository categoryRepository,
					   TagRepository tagRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
		this.tagRepository = tagRepository;
	}

	public PostResponse createPost(PostRequest request) {
		Post post = new Post();
		populatePost(post, request);
		postRepository.save(post);
		return toResponse(post);
	}

	public List<PostResponse> getAllPosts() {
		return postRepository.findAll()
				.stream()
				.map(this::toResponse)
				.toList();
	}

	public PostResponse getPostById(Long id) {
		return toResponse(findPostById(id));
	}

	public PostResponse updatePost(Long id, PostRequest request) {
		Post post = findPostById(id);
		populatePost(post, request);
		postRepository.save(post);
		return toResponse(post);
	}

	public void deletePost(Long id) {
		Post post = findPostById(id);
		postRepository.delete(post);
	}

	private void populatePost(Post post, PostRequest request) {
		post.setTitle(request.title());
		post.setContent(request.content());
		post.setStatus(request.status() == null ? PostStatus.DRAFT : request.status());
		post.setReadingTime(request.readingTime());
		post.setAuthor(findUserById(request.userId()));
		post.setCategory(findCategoryByName(request.category()));
		post.setTags(resolveTags(request.tags()));
	}

	private Post findPostById(Long id) {
		return postRepository.findById(id)
				.orElseThrow(() -> new PostNotFound("Post with id " + id + " not found"));
	}

	private User findUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFound("User with id " + id + " not found"));
	}

	private Category findCategoryByName(String name) {
		Category category = categoryRepository.findByName(name);
		if (category == null) {
			throw new CategoryNotFound("Category " + name + " doesnt exist");
		}
		return category;
	}

	private PostResponse toResponse(Post post) {
		return new PostResponse(
				post.getId(),
				post.getTitle(),
				post.getContent(),
				post.getStatus(),
				post.getReadingTime(),
				new PostResponse.AuthorResponse(
						post.getAuthor().getId(),
						post.getAuthor().getEmail(),
						post.getAuthor().getName()
				),
				new PostResponse.CategoryResponse(
						post.getCategory().getId(),
						post.getCategory().getName()
				),
				mapTags(post.getTags()),
				post.getCreatedAt(),
				post.getUpdatedAt()
		);
	}

	private Set<PostResponse.TagResponse> mapTags(Set<Tag> tags) {
		if (tags == null || tags.isEmpty()) {
			return Set.of();
		}
		Set<PostResponse.TagResponse> responses = new LinkedHashSet<>();
		for (Tag tag : tags) {
			responses.add(new PostResponse.TagResponse(tag.getId(), tag.getName()));
		}
		return responses;
	}

	private Set<Tag> resolveTags(List<String> tagNames) {
		if (tagNames == null || tagNames.isEmpty()) {
			return Set.of();
		}
		Set<Tag> tags = new LinkedHashSet<>();
		for (String tagName : tagNames) {
			Tag tag = tagRepository.findByName(tagName);
			if (tag == null) {
				throw new TagNotFound("Tag " + tagName + " is not found");
			}
			tags.add(tag);
		}
		return tags;
	}
}
