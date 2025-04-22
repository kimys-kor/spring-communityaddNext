package com.community.api.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDm is a Querydsl query type for Dm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDm extends EntityPathBase<Dm> {

    private static final long serialVersionUID = 1882814420L;

    public static final QDm dm = new QDm("dm");

    public final com.community.api.model.base.QBaseTime _super = new com.community.api.model.base.QBaseTime(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isChecked = createBoolean("isChecked");

    public final StringPath receiver = createString("receiver");

    public final StringPath sender = createString("sender");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDt = _super.updatedDt;

    public QDm(String variable) {
        super(Dm.class, forVariable(variable));
    }

    public QDm(Path<? extends Dm> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDm(PathMetadata metadata) {
        super(Dm.class, metadata);
    }

}

