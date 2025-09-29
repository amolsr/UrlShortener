package com.urlShortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.urlShortener.models.Link;

import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Boolean existsByShortId(String shortId);
    Optional<Link> findByShortId(String shortId);
    
    @Modifying
    @Transactional
    @Query("UPDATE Link l SET l.clickCount = l.clickCount + 1 WHERE l.shortId = :shortId")
    int incrementClickCountByShortId(@Param("shortId") String shortId);
}

