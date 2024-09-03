package br.com.videosaver.video.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class VideoInput {

    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotBlank
    String url;
    UUID category;
}
