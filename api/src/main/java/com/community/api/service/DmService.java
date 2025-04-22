package com.community.api.service;

import com.community.api.common.exception.AuthenticationErrorCode;
import com.community.api.common.exception.DmErrorCode;
import com.community.api.model.Dm;
import com.community.api.model.dto.DmDto;
import com.community.api.model.dto.ReadDmListDto;
import com.community.api.repository.DmCustomRepository;
import com.community.api.repository.DmRepository;
import com.community.api.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DmService {
    @PersistenceContext
    EntityManager em;
    private final DmRepository dmRepository;
    private final DmCustomRepository dmCustomRepository;
    private final UserRepository userRepository;

    public Dm sendDm(String sender, DmDto dmDto) {
        if (sender.equals(dmDto.receiver())) {
            throw DmErrorCode.DM_CANNOT_SEND_TO_SELF.defaultException();
        }
        userRepository.findByUsername(dmDto.receiver()).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);


        Dm dm = Dm.builder()
                .title(dmDto.title())
                .content(dmDto.content())
                .receiver(dmDto.receiver())
                .sender(sender)
                .isChecked(true)
                .build();

        dmRepository.save(dm);
        return dm;
    }

    public Page<ReadDmListDto> getDmList(String username, Pageable pageable) {
        return dmCustomRepository.getList(username, pageable);
    }

    @Transactional
    public Dm getDmContent(Long id) {
        Dm dm = dmRepository.findById(id).orElseThrow(DmErrorCode.No_EXIST_DM::defaultException);

        dm.setChecked(true);
        em.flush();
        em.clear();
        return dm;
    }

    public void deleteMessage(List<Long> idList) {
        dmRepository.deleteAllByIds(idList);
    }

}
