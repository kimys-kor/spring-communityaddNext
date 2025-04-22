package com.community.api.service;

import com.community.api.model.dto.ReadPostListDto;
import com.community.api.repository.ReportInformationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportInformationService {

    @PersistenceContext
    EntityManager em;
    private final ReportInformationRepository reportInformationRepository;

//    public Page<ReadPostListDto> getList(int typ, String keyword, Pageable pageable) {
//        return postCustomRepository.getList(typ, keyword, pageable);
//    }
}
