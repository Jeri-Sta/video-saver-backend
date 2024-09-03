package br.com.videosaver.video.model;

import br.com.videosaver.category.model.CategoryOutput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class VideoOutput {

        UUID id;
        String title;
        String description;
        String url;
        CategoryOutput category;
}
