package br.com.videosaver.video.model;

import br.com.videosaver.category.model.CategoryEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "video")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class VideoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String title;

    String description;

    String url;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id")
    CategoryEntity category;

}
