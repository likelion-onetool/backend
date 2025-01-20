package com.onetool.server.blueprint.repository;

import com.onetool.server.blueprint.BlueprintFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlueprintFileRepository extends JpaRepository<BlueprintFile, Long> {
}
