package com.elice.tripnote.comment.entity;


import com.elice.tripnote.global.entity.BaseTimeEntity;
import com.elice.tripnote.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // 사용하는 db에 따라 @Lob를 사용할 때는 큰 주의가 필요하다. postgre와 oracle은 특히 주의해야 한다.
    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int report;

    // USER 객체가 생성 되면 주석을 풀 예정입니다.
    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


    @Builder
    private Comment(String content, int report, Post post) {
        this.content = content;
        this.report = report;
        this.post = post;
    }

    public CommentDTO toDTO() {

        return CommentDTO.builder().id(id).content(content).report(report).build();

    }
}
