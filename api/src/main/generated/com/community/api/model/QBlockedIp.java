package com.community.api.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlockedIp is a Querydsl query type for BlockedIp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlockedIp extends EntityPathBase<BlockedIp> {

    private static final long serialVersionUID = 1315469960L;

    public static final QBlockedIp blockedIp = new QBlockedIp("blockedIp");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ipAddress = createString("ipAddress");

    public QBlockedIp(String variable) {
        super(BlockedIp.class, forVariable(variable));
    }

    public QBlockedIp(Path<? extends BlockedIp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlockedIp(PathMetadata metadata) {
        super(BlockedIp.class, metadata);
    }

}

