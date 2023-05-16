package com.example.noticeboard.domain;

import com.example.noticeboard.dto.request.ReqUpdatePostDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "is_deleted = false")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "body")
    private String body;

    @Column(name = "public_state", nullable = false)
    private boolean publicState;

    private String password;

    @Column(name = "comment_active_state", nullable = false)
    private boolean commentActiveState;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post")
    private List<PostFile> postFiles = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostCategory> postCategories = new ArrayList<>();

    @Builder
    public Post(User user, String title, String body,
                boolean publicState, String password, boolean commentActiveState) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.publicState = publicState;
        this.password = password;
        this.commentActiveState = commentActiveState;
        this.viewCount = 0;
        this.isDeleted = false;
    }

    public boolean isPasswordEqual(String password) {
        return this.password.equals(password);
    }

    public boolean hasPassword() {
        return !StringUtils.isBlank(this.password);
    }

    public void update(ReqUpdatePostDto reqUpdatePostDto) {
        this.publicState = reqUpdatePostDto.getIsPublic();
        this.password = reqUpdatePostDto.getPassword();
        this.title = reqUpdatePostDto.getTitle();
        this.body = reqUpdatePostDto.getBody();
    }

    public boolean isSamePost(Long postId) {
        return postId != null && this.id == postId;
    }
}
