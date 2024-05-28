package com.elice.tripnote.domain.post.repository;


import com.elice.tripnote.domain.link.likePost.entity.QLikePost;
import com.elice.tripnote.domain.link.reportPost.entity.QReportPost;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.post.entity.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory query;


    private final QPost post = QPost.post;
    private final QLikePost likePost = QLikePost.likePost;
    private final QReportPost reportPost = QReportPost.reportPost;


    public Page<PostResponseDTO> customFindNotDeletedPosts(int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount = query
                .from(post)
                .where(post.isDeleted.isFalse())
                .fetch().size();


        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .where(post.isDeleted.isFalse())
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindPosts(int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount =query
                .from(post)
                .fetch().size();


        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }


    public Page<PostResponseDTO> customFindNotDeletedPostsByMemberId(Long memberId, int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount = query
                .from(post)
                .where(post.member.id.eq(memberId)
                        .and(post.isDeleted.isFalse()))
                .fetch().size();


        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .where(post.member.id.eq(memberId)
                        .and(post.isDeleted.isFalse()))
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindNotDeletedPostsWithLikesByMemberId(Long memberId, int page, int size){

        long totalCount = query
                .from(post)
                .join(post.likePosts, likePost)
                .where(post.member.id.eq(memberId)
                        .and(likePost.likedAt.isNotNull())
                        .and(post.isDeleted.isFalse()))
                .fetch().size();

        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted
                ))
                .from(post)
                .where(post.member.id.eq(memberId)
                        .and(likePost.likedAt.isNotNull())
                        .and(post.isDeleted.isFalse()))
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);

    }

    public PostDetailResponseDTO customFindPost(Long postId){

        return query
                .select(Projections.constructor(PostDetailResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.likes,
                        post.report,
                        post.isDeleted,
                        likePost.likedAt,
                        reportPost.reportedAt
                ))
                .from(post)
                .leftJoin(post.likePosts, likePost)
                .leftJoin(post.reportPosts, reportPost)
                .where(post.id.eq(postId)
                        .and(post.isDeleted.isFalse()))
                .fetchOne();

    }




}
