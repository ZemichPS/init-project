package by.zemich.userms.dao.repositories;

import by.zemich.userms.dao.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, PagingAndSortingRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
