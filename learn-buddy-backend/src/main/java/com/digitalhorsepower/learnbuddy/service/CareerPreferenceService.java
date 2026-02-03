package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.dto.CareerPreferenceDTO;
import com.digitalhorsepower.learnbuddy.entity.UserCareerPreference;
import com.digitalhorsepower.learnbuddy.repository.UserCareerPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CareerPreferenceService {

    private final UserCareerPreferenceRepository careerPreferenceRepository;

    public UserCareerPreference saveOrUpdateCareerPreference(Long userId, CareerPreferenceDTO preferenceDTO) {
        UserCareerPreference preference = careerPreferenceRepository.findByUserId(userId)
                .orElse(new UserCareerPreference());

        preference.setUserId(userId);
        preference.setExpectedPosition(preferenceDTO.getExpectedPosition());
        preference.setExpectedIndustry(preferenceDTO.getExpectedIndustry());
        preference.setExpectedLocation(preferenceDTO.getExpectedLocation());
        preference.setUpdatedAt(LocalDateTime.now());

        return careerPreferenceRepository.save(preference);
    }

    public CareerPreferenceDTO getCareerPreference(Long userId) {
        return careerPreferenceRepository.findByUserId(userId)
                .map(preference -> {
                    CareerPreferenceDTO dto = new CareerPreferenceDTO();
                    dto.setExpectedPosition(preference.getExpectedPosition());
                    dto.setExpectedIndustry(preference.getExpectedIndustry());
                    dto.setExpectedLocation(preference.getExpectedLocation());
                    return dto;
                })
                .orElse(new CareerPreferenceDTO());
    }
}