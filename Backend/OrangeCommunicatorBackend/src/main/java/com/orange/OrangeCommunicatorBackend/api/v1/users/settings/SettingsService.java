package com.orange.OrangeCommunicatorBackend.api.v1.users.settings;

import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.requestBody.UpdateSettingsRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.responseBody.SettingsResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.support.SettingsExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.support.SettingsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.dbEntities.Settings;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.SettingsRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final UserRepository userRepository;
    private final SettingsRepository settingsRepository;
    private final SettingsMapper settingsMapper;

    public SettingsService(UserRepository userRepository, SettingsRepository settingsRepository,
                           SettingsMapper settingsMapper) {
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
        this.settingsMapper = settingsMapper;
    }


    public SettingsResponseBody getSettings(String username) {
        User user = userRepository.findById(username).
                orElseThrow(UserExceptionSupplier.userNotFoundException(username));
        Settings settings = settingsRepository.findById(username)
                .orElseThrow(SettingsExceptionSupplier.settingsNotFound(username));
        return settingsMapper.toSettingsResponseBody(settings);
    }

    public SettingsResponseBody updateSettings(String username, UpdateSettingsRequestBody updateSettingsRequestBody) {
        User user = userRepository.findById(username).
                orElseThrow(UserExceptionSupplier.userNotFoundException(username));
        Settings settings = settingsRepository.findById(username)
                .orElseThrow(SettingsExceptionSupplier.settingsNotFound(username));
        settingsMapper.toSettings(settings, updateSettingsRequestBody);
        settingsRepository.save(settings);
        return settingsMapper.toSettingsResponseBody(settings);
    }
}