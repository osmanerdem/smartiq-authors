package com.smartiq.authors.repository;

import com.smartiq.authors.model.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

    List<Text> findAllByAuthorId(Long authorId);

}