package com.ylli.urlShortener.repositories;

import com.ylli.urlShortener.models.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Boolean existsByShortId(String shortId);
    Optional<Link> findByShortId(String shortId);
}

