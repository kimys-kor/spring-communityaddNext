package com.community.api.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QApprovedIp is a Querydsl query type for ApprovedIp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApprovedIp extends EntityPathBase<ApprovedIp> {

    private static final long serialVersionUID = 1687260809L;

    public static final QApprovedIp approvedIp = new QApprovedIp("approvedIp");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath ipAddress = createString("ipAddress");

    public QApprovedIp(String variable) {
        super(ApprovedIp.class, forVariable(variable));
    }

    public QApprovedIp(Path<? extends ApprovedIp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApprovedIp(PathMetadata metadata) {
        super(ApprovedIp.class, metadata);
    }

}

