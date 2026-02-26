package com.crms.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.crms.entity.StudentProfile;
import com.crms.entity.User;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
	
	Optional<StudentProfile> findByUser(User user);
	
	boolean existsByUser(User user);

}
