package com.foodie.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 100)
    private String designation;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @OneToMany(mappedBy = "recordedByAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Expense> recordedExpenses = new ArrayList<>();

    @OneToMany(mappedBy = "generatedByAdmin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Report> generatedReports = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
