package com.example.noticeboard.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String filename;
    private String contentType;
    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public PostFile(Post post, String filename, String contentType) {
        this.post = post;
        this.filename = filename;
        this.contentType = contentType;
        this.isDeleted = false;
    }

    public void delete() {
        this.isDeleted = true;
        this.post = null;
    }
}
