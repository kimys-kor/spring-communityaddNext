package com.community.api.service;

import com.community.api.common.exception.AdminErrorCode;
import com.community.api.model.ApprovedIp;
import com.community.api.model.BlockedIp;
import com.community.api.model.dto.SaveIpDto;
import com.community.api.repository.ApprovedIpRepository;
import com.community.api.repository.BlockedIpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IpService {

    private final ApprovedIpRepository approvedIpRepository;
    private final BlockedIpRepository blockedIpRepository;

    public boolean saveIp(SaveIpDto saveIpDto) {
        if (saveIpDto.type().equals("approved")) {
            Optional<ApprovedIp> findIp = approvedIpRepository.findByIpAddressEquals(saveIpDto.ipAddress());
            if (findIp.isEmpty()) {
                ApprovedIp approvedIp = ApprovedIp.builder()
                        .ipAddress(saveIpDto.ipAddress())
                        .build();
                approvedIpRepository.save(approvedIp);
                return true;
            } else {
                throw AdminErrorCode.ALREADY_EXIST_IP.defaultException();
            }
        } else if (saveIpDto.type().equals("blocked")) {
            Optional<BlockedIp> findIp = blockedIpRepository.findByIpAddressEquals(saveIpDto.ipAddress());
            if (findIp.isEmpty()) {
                BlockedIp blockedIp = BlockedIp.builder()
                        .ipAddress(saveIpDto.ipAddress())
                        .build();
                blockedIpRepository.save(blockedIp);
                return true;
            } else {
                throw AdminErrorCode.ALREADY_EXIST_IP.defaultException();
            }
        } else {
            return false;
        }
    }

    public boolean findIp(String type, String ipAddress) {
        if (type.equals("approved")) {
            Optional<ApprovedIp>  approvedIp = approvedIpRepository.findByIpAddressEquals(ipAddress);
            if (approvedIp.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            Optional<BlockedIp>  blockedIp = blockedIpRepository.findByIpAddressEquals(ipAddress);
            if (blockedIp.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public List<BlockedIp> findAllBlockedIp() {
        return blockedIpRepository.findAll();
    }
    public List<ApprovedIp> findAllApprovedIp() {
        return approvedIpRepository.findAll();
    }

    public void deleteIp(Long ipId) {
            BlockedIp  approvedIp = blockedIpRepository.findById(ipId).orElseThrow(AdminErrorCode.NO_EXIST_IP::defaultException);
            blockedIpRepository.delete(approvedIp);
    }
}
