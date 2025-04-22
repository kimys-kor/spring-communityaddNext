package com.community.api.repository;

import com.community.api.model.ImgFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgFileRepository extends JpaRepository<ImgFile, Long> {


    ImgFile findByIdEquals(@Param("id") int id);
}

