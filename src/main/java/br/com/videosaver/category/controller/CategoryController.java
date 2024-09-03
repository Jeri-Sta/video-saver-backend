package br.com.videosaver.category.controller;

import br.com.videosaver.category.model.CategoryInput;
import br.com.videosaver.category.model.CategoryOutput;
import br.com.videosaver.category.service.CategoryService;
import br.com.videosaver.video.model.VideoInput;
import br.com.videosaver.video.model.VideoOutput;
import br.com.videosaver.video.service.VideoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
@Setter(onMethod_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryController {

    CategoryService categoryService;
    VideoService videoService;

    @GetMapping
    public List<CategoryOutput> listAll() {
        return categoryService.listAll();
    }

    @GetMapping("/{id}")
    public CategoryOutput retrieve(@PathVariable @NotNull UUID id) {
        return categoryService.retrieve(id);
    }

    @GetMapping("/{id}/videos")
    public List<VideoOutput> listVideosByCategory(@PathVariable @NotNull UUID id) {
        return videoService.listVideosByCategory(id);
    }

    @PostMapping
    public ResponseEntity<CategoryOutput> create(@RequestBody @Valid CategoryInput input, UriComponentsBuilder uri) {
        CategoryOutput categoryOutput = categoryService.create(input);
        URI address = uri.path("categorias/{id}").buildAndExpand(categoryOutput.getId()).toUri();
        return ResponseEntity.created(address).body(categoryOutput);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryOutput> update(@RequestBody @Valid CategoryInput input, @PathVariable @NotNull UUID id, UriComponentsBuilder uri) {
        CategoryOutput categoryOutput = categoryService.update(id, input);
        URI address = uri.path("categorias/{id}").buildAndExpand(categoryOutput.getId()).toUri();
        return ResponseEntity.created(address).body(categoryOutput);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @NotNull UUID id) {
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }
}
