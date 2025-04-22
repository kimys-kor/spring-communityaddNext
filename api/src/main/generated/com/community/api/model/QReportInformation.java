package com.community.api.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReportInformation is a Querydsl query type for ReportInformation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportInformation extends EntityPathBase<ReportInformation> {

    private static final long serialVersionUID = -1190038355L;

    public static final QReportInformation reportInformation = new QReportInformation("reportInformation");

    public final StringPath accountNumber = createString("accountNumber");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final StringPath date = createString("date");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final NumberPath<Integer> reportTyp = createNumber("reportTyp", Integer.class);

    public final StringPath siteName = createString("siteName");

    public final StringPath siteUrl = createString("siteUrl");

    public QReportInformation(String variable) {
        super(ReportInformation.class, forVariable(variable));
    }

    public QReportInformation(Path<? extends ReportInformation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReportInformation(PathMetadata metadata) {
        super(ReportInformation.class, metadata);
    }

}

