package com.hrms.auth.repository;

import com.hrms.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByWorkEmail(String workEmail);
	Optional<User> findByWorkEmail(String workEmail);
	 Optional<User> findByEmployeeId(Long employeeId);      // ← ADD THIS LINE
}
