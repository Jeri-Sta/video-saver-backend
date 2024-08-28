package br.com.videosaver.video.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class VideoCreateInput {

    @NotBlank
    String title;
    @NotBlank
    String description;
    @NotBlank
    String url;
}
