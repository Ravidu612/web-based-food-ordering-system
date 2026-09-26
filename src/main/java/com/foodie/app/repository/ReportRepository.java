package com.foodie.app.repository;

import com.foodie.app.entity.Report;
import com.foodie.app.entity.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByReportTypeOrderByGeneratedAtDesc(ReportType reportType);
    List<Report> findAllByOrderByGeneratedAtDesc();
}
