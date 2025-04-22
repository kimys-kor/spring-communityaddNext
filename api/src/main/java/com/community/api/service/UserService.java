package com.community.api.service;

import com.community.api.common.exception.CommonException;
import com.community.api.model.User;
import com.community.api.model.base.UserRole;
import com.community.api.model.base.UserStatus;
import com.community.api.model.dto.*;
import com.community.api.repository.UserCustomRepository;
import com.community.api.common.exception.AuthenticationErrorCode;
import com.community.api.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    @PersistenceContext
    EntityManager em;

    @Value("${key.signupPoint}")
    private String signupPoint;

    @Value("${key.loginPoint}")
    private String loginPoint;

    @Value("${key.savePostPoint}")
    private String savePostPoint;

    @Value("${key.saveCommentPoint}")
    private String saveCommentPoint;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;


    public User findByUsername(String username) {
        User byId = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));
        return byId;
    }

    public User findByUsernameSafe(String username) {
        if (username == null) {
            return null;
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));
    }

    public void updateLastLogin(Long userId, LocalDateTime time) {
        userRepository.updateLastLogin(userId, time);
    }

    // 관리자 페이지 유저 리스트
    public Page<UserReadDto> findAll(String keyword, Pageable pageable) {
        Page<UserReadDto> pageObject = userCustomRepository.findAll(keyword, pageable);
        return pageObject;
    }

    // 마스터 페이지 관리자 리스트
    public Page<UserReadDto> findAllAdmin(String keyword, Pageable pageable) {
        Page<UserReadDto> pageObject = userCustomRepository.findAllAdmin(keyword, pageable);
        return pageObject;
    }

    // 관리자 페이지 유저 상세
    public Map<String,Object> findById(Long userId) {
        UserDetailDto userDetailDto = userCustomRepository.findById(userId);


        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("userDetail", userDetailDto);
        return result;
    }

    public User join(JoinRequestDto joinRequestDto) {
        Optional<User> findUser = userRepository.findByUsername(joinRequestDto.username());
        if (!findUser.isEmpty()) {
            throw AuthenticationErrorCode.USER_ALREADY_EXIST.defaultException();
        }

        User user = User.builder()
                .status(UserStatus.NORMAL)
                .username(joinRequestDto.username())
                .password(passwordEncoder.encode(joinRequestDto.password()))
                .fullName(joinRequestDto.fullName())
                .phoneNum(joinRequestDto.phoneNum())
                .nickname(joinRequestDto.nickname())
                .point(Integer.parseInt(signupPoint))
                .exp(1)
                .role(UserRole.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    public User saveAdmin(JoinRequestDto joinRequestDto) {
        Optional<User> findUser = userRepository.findByUsername(joinRequestDto.username());
        if (!findUser.isEmpty()) {
            throw AuthenticationErrorCode.USER_ALREADY_EXIST.defaultException();
        }

        User user = User.builder()
                .status(UserStatus.NORMAL)
                .username(joinRequestDto.username())
                .password(passwordEncoder.encode(joinRequestDto.password()))
                .fullName(joinRequestDto.fullName())
                .phoneNum(joinRequestDto.phoneNum())
                .nickname(joinRequestDto.nickname())
                .point(Integer.parseInt(signupPoint))
                .exp(1)
                .role(UserRole.ROLE_ADMIN)
                .build();

        return userRepository.save(user);
    }

    

    public List countAllByCreatedDtBetween() {
        LocalDate today = LocalDate.now();

        List data = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate day = today.minusDays(i);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
            String formattedDay = day.format(formatter);

            LocalDateTime start = day.atStartOfDay();
            LocalDateTime end = day.atTime(LocalTime.MAX);
            Integer count = userRepository.countAllByCreatedDtBetween(start, end);

            Map<String, Object> map = new HashMap<>();
            map.put("x", formattedDay);
            map.put("y", count);
            data.add(map);
        }

        return data;
    }

    // 유저 비밀번호 업데이트
    @Transactional
    public String updatePassword(Long userId) {
        Random random = new Random();
        int temp = 0;
        String tempNum = "";
        int size    = 6;
        String resultNum = "";

        for (int i=0; i<size; i++) {
            temp = random.nextInt(9);
            tempNum =  Integer.toString(temp);
            resultNum += tempNum;
        }
        String encPassword = passwordEncoder.encode(resultNum);
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));
        user.setPassword(encPassword);
        em.flush();
        em.clear();
        return resultNum;
    }

    @Transactional
    public void addPoint(Long userId, Integer point) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));
        user.setPoint(user.getPoint()+point);
        em.flush();
        em.clear();
    }

    // 유저 포인트 추가
    @Transactional
    public void addPointExp(Long userId, String actionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("없는 회원입니다."));

        int pointsToAdd;
        switch (actionType) {
            case "signup":
                pointsToAdd = Integer.parseInt(signupPoint);
                break;
            case "login":
                pointsToAdd = Integer.parseInt(loginPoint);
                break;
            case "savePost":
                pointsToAdd = Integer.parseInt(savePostPoint);
                break;
            case "saveComment":
                pointsToAdd = Integer.parseInt(saveCommentPoint);
                break;
            default:
                throw new IllegalArgumentException("Invalid action type");
        }

        int expToAdd = pointsToAdd / 10;

        user.setPoint(user.getPoint() + pointsToAdd);
        user.setExp(user.getExp() + expToAdd);

        em.flush();
        em.clear();
    }



    // 유저 내정보 수정
    @Transactional
    public void updateMyInfo(String username, UserUpdateDto userUpdateDto) {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.setPhoneNum(userUpdateDto.getPhoneNum());
        user.setFullName(userUpdateDto.getFullName());
        user.setNickname(userUpdateDto.getNickname());
        em.flush();
        em.clear();
    }

    // 유저 탈퇴
    @Transactional
    public void updateWithdrawl(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow();
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (matches) {
            user.setStatus(UserStatus.DELETED);
        } else {
            throw new CommonException(AuthenticationErrorCode.AUTHENTICATION_FAILED);
        }
        em.flush();
        em.clear();
    }

    // 유저 비번 수정
    @Transactional
    public void updateMyPW(String username, UserPWUpdateDto userPWUpdateDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CommonException(AuthenticationErrorCode.USER_NOT_EXIST));

        boolean matches = passwordEncoder.matches(userPWUpdateDto.getOldpassword(), user.getPassword());
        if (matches) {
            user.setPassword(passwordEncoder.encode(userPWUpdateDto.getNewpassword()));
        } else {
            throw new CommonException(AuthenticationErrorCode.AUTHENTICATION_FAILED);
        }
        em.flush();
        em.clear();
    }

    // 어드민이상 유저정보 수정
    @Transactional
    public void updateUserInfo(UserUpdateAdminDto userUpdateAdminDto) {
        User user = userRepository.findByUsername(userUpdateAdminDto.getUsername()).orElseThrow();

        user.setPassword(passwordEncoder.encode(userUpdateAdminDto.getPassword()));
        user.setPhoneNum(userUpdateAdminDto.getPhoneNum());
        user.setFullName(userUpdateAdminDto.getFullName());
        user.setNickname(userUpdateAdminDto.getNickname());

        em.flush();
        em.clear();
    }


    @Transactional
    public void setBlock(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(AuthenticationErrorCode.USER_NOT_EXIST::defaultException);
        if (user.getStatus().equals(UserStatus.DELETED)) {
            throw AuthenticationErrorCode.USER_NOT_EXIST.defaultException();
        } else if (user.getStatus().equals(UserStatus.BLOCKED)) {
            user.setStatus(UserStatus.NORMAL);
            em.flush();
            em.clear();
        } else {
            user.setStatus(UserStatus.BLOCKED);
            em.flush();
            em.clear();
        }
    }

   
}
