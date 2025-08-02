package com.ansysan.project.authentication_service.repository;

import com.ansysan.project.authentication_service.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
}