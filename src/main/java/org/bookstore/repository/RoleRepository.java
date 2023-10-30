package org.bookstore.repository;

import org.bookstore.model.Role;
import org.bookstore.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(RoleName name);
}
