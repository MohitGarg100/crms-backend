package com.crms.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.crms.repository.PasswordResetTokenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {
	
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Scheduled(fixedRate = 3600000)
	@Transactional
	public void deleteExpiredTokens() {
		
		passwordResetTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
	}

}
