package org.bookstore.repository;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.bookstore.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(@NotNull String email);

    boolean existsByEmail(String email);
}
