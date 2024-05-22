package com.elice.tripnote.comment.repository;


import com.elice.tripnote.comment.entity.Comment;
import com.elice.tripnote.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost_Id(Long postId);
    Page<Comment> findByPost_Id(Long postId, Pageable pageable);
//    List<User> findByUser_Id(Long userId);
//    Page<User> findByUser_Id(Long userId, Pageable pageable);
    Page<Comment> findAll(Pageable pageable);
}
