package com.community.api.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdminActionHistory is a Querydsl query type for AdminActionHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdminActionHistory extends EntityPathBase<AdminActionHistory> {

    private static final long serialVersionUID = 711815098L;

    public static final QAdminActionHistory adminActionHistory = new QAdminActionHistory("adminActionHistory");

    public final com.community.api.model.base.QBaseTime _super = new com.community.api.model.base.QBaseTime(this);

    public final NumberPath<Integer> actionType = createNumber("actionType", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDt = _super.createdDt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDt = _super.updatedDt;

    public final StringPath username = createString("username");

    public QAdminActionHistory(String variable) {
        super(AdminActionHistory.class, forVariable(variable));
    }

    public QAdminActionHistory(Path<? extends AdminActionHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdminActionHistory(PathMetadata metadata) {
        super(AdminActionHistory.class, metadata);
    }

}

