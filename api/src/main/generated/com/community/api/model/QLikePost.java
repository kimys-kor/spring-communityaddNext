package com.community.api.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikePost is a Querydsl query type for LikePost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikePost extends EntityPathBase<LikePost> {

    private static final long serialVersionUID = -1654022174L;

    public static final QLikePost likePost = new QLikePost("likePost");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final StringPath username = createString("username");

    public QLikePost(String variable) {
        super(LikePost.class, forVariable(variable));
    }

    public QLikePost(Path<? extends LikePost> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikePost(PathMetadata metadata) {
        super(LikePost.class, metadata);
    }

}

