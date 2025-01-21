package com.onetool.server.api.blueprint.repository;

import com.onetool.server.api.blueprint.BlueprintFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlueprintFileRepository extends JpaRepository<BlueprintFile, Long> {
}
