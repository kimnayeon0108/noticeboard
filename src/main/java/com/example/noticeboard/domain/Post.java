package com.example.noticeboard.domain;

import com.example.noticeboard.dto.PostRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@NoArgsConstructor
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String body;
    private boolean publicState;
    private String password;
    private boolean commentActiveState;
    private int viewCount;
    private boolean isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<PostFile> postFiles = new ArrayList<>();

    @Builder
    public Post(Category category, User user, String title, String body,
                boolean publicState, String password, boolean commentActiveState) {
        this.category = category;
        this.user = user;
        this.title = title;
        this.body = body;
        this.publicState = publicState;
        this.password = password;
        this.commentActiveState = commentActiveState;
        this.viewCount = 0;
    }

    public boolean isPasswordEqual(String password) {
        return this.password.equals(password);
    }

    public boolean hasPassword() {
        return this.password != null;
    }

    public void update(PostRequest postRequest, Category category) {
        this.publicState = postRequest.isPublic();
        this.password = postRequest.getPassword();
        this.category = category;
        this.title = postRequest.getTitle();
        this.body = postRequest.getBody();
    }

    public boolean isSamePost(Long postId) {
        return this.id == postId;
    }
}
