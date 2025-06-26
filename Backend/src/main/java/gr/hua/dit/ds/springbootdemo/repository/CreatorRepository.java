package gr.hua.dit.ds.springbootdemo.repository;

import gr.hua.dit.ds.springbootdemo.entity.Creator;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "creator")
@Hidden
public interface CreatorRepository extends JpaRepository<Creator, Integer> {
    Optional<Creator> findByUserId(Long userId);
    //Optional<Creator> findByContactEmail(String email);
} 