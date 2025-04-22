package com.community.api.service;

import com.community.api.common.exception.AdminErrorCode;
import com.community.api.common.exception.BannerErrorCode;
import com.community.api.common.exception.BoardErrorCode;
import com.community.api.model.Banner;
import com.community.api.model.dto.SaveBannerDto;
import com.community.api.model.dto.UpdateBannerDto;
import com.community.api.repository.BannerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerService {

    @PersistenceContext
    EntityManager em;
    private final BannerRepository bannerRepository;
    private final ImgFileService imgFileService;

    @Transactional
    public Banner createBanner(SaveBannerDto saveBannerDto) {
        Banner banner = Banner.builder()
                .partnerName(saveBannerDto.getPartnerName())
                .partnerUrl(saveBannerDto.getPartnerUrl())
                .thumbNail(saveBannerDto.getThumbNail())
                .build();
        return bannerRepository.save(banner);

    }

    @Transactional
    public void updateBanner(UpdateBannerDto dto) {
        Banner banner = bannerRepository.findById(dto.getId()).orElseThrow(BannerErrorCode.BANNER_NOT_EXIST::defaultException);
        imgFileService.deleteFile(banner.getThumbNail(), "banner");

        banner.setPartnerName(dto.getPartnerName());
        banner.setPartnerUrl(dto.getPartnerUrl());
        banner.setThumbNail(dto.getThumbNail());
        System.out.println(dto.getThumbNail()+"hihihi");
        em.flush();
        em.clear();

    }

    // 배너 클릭
    @Transactional
    public void clickBanner(Long bannerId) {
        Banner banner = bannerRepository.findById(bannerId).orElseThrow(BannerErrorCode.BANNER_NOT_EXIST::defaultException);
        banner.setClickNum(banner.getClickNum()+1);
        em.flush();
        em.clear();
    }

    @Transactional
    public boolean deleteBannerById(Long id) {
        Optional<Banner> banner = bannerRepository.findById(id);
        if (banner.isPresent()) {
            bannerRepository.delete(banner.get());
            return true;
        }
        return false;
    }

    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    public List<Banner> get3Banners() {
        List<Banner> banners = bannerRepository.findAll();

        if (banners.size() <= 3) {
            return banners;
        }

        Collections.shuffle(banners);
        return banners.subList(0, 3);
    }


}