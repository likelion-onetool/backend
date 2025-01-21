package com.onetool.server.api.counting;

import com.onetool.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "service_counting")
public class ServiceCounting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "total_files")
    @ColumnDefault("0")
    private Long totalFiles;

    @Column(name = "total_user")
    @ColumnDefault("0")
    private Long totalUser;

    @Column(name = "total_sales")
    @ColumnDefault("0")
    private Long totalSales;
}
