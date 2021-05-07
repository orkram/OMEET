package com.orange.OrangeCommunicatorBackend.dbRepositories;

import com.orange.OrangeCommunicatorBackend.dbEntities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, String> {
}
