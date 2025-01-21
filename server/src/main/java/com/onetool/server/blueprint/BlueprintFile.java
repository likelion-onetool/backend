package com.onetool.server.blueprint;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class BlueprintFile {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty private String changedFileName;
    @NotEmpty private String fileName;
    @NotEmpty private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blueprint_id")
    private Blueprint blueprint;

    private BlueprintFile(String changedFileName, String fileName, String fileUrl) {
        this.changedFileName = changedFileName;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public void update(Blueprint blueprint) {
        this.blueprint = blueprint;
        blueprint.getBlueprintFiles().add(this);
    }

    public static BlueprintFile of(String changedFileName, String fileName, String fileUrl) {
        return new BlueprintFile(changedFileName, fileName, fileUrl);
    }
}
