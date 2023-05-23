package com.example.noticeboard.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "category",
       uniqueConstraints = @UniqueConstraint(name = "uk_parent_id_name", columnNames = {"parent_id", "name"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    public static final int TOTAL_DEPTH = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "depth", nullable = false)
    private int depth;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category")
    private List<PostCategory> postCategories = new ArrayList<>();

    @Builder
    public Category(Category parentCategory, String name, int depth) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.depth = depth;
    }

    public void update(String name) {
        this.name = name;
    }
}
