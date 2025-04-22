package com.community.api.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QImgFile is a Querydsl query type for ImgFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImgFile extends EntityPathBase<ImgFile> {

    private static final long serialVersionUID = 1411928948L;

    public static final QImgFile imgFile = new QImgFile("imgFile");

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath origFileName = createString("origFileName");

    public final DateTimePath<java.time.LocalDateTime> uploadDate = createDateTime("uploadDate", java.time.LocalDateTime.class);

    public QImgFile(String variable) {
        super(ImgFile.class, forVariable(variable));
    }

    public QImgFile(Path<? extends ImgFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImgFile(PathMetadata metadata) {
        super(ImgFile.class, metadata);
    }

}

