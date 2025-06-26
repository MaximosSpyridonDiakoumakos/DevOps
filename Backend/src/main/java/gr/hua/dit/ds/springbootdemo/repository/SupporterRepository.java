package gr.hua.dit.ds.springbootdemo.repository;

import gr.hua.dit.ds.springbootdemo.entity.Supporter;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "supporter")
@Hidden
public interface SupporterRepository extends JpaRepository<Supporter, Integer> {
    //Optional<Supporter> findByEmail(String email);
    Optional<Supporter> findByUserId(Long userId);
} 