package com.gpsolutions.vaadincourse.repository;

import com.gpsolutions.vaadincourse.dbo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
