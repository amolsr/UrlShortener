package com.urlShortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.urlShortener.models.Link;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Boolean existsByShortId(String shortId);
    Optional<Link> findByShortId(String shortId);
}

