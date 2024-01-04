package com.anchor.domain.mentoring.domain.repository;

import com.anchor.domain.mentoring.domain.MentoringApplication;
import java.time.LocalDateTime;
import java.util.Optional;

public interface QMentoringApplicationRepository {

  Optional<MentoringApplication> findAppliedMentoringByTimeAndUserId(LocalDateTime startDateTime,
      LocalDateTime endDateTime, Long userId);
}
