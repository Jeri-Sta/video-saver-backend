package br.com.videosaver.video.repository;

import br.com.videosaver.video.model.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<VideoEntity, UUID> {

    List<VideoEntity> findByCategoryId(UUID categoryId);

    List<VideoEntity> findByTitleContaining(String title);
}
