package com.elice.tripnote.domain.member.controller;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.MemberRequestDTO;
import com.elice.tripnote.domain.member.entity.MemberResponseDTO;
import com.elice.tripnote.domain.member.entity.PasswordDTO;
import com.elice.tripnote.domain.member.service.KakaoService;
import com.elice.tripnote.domain.member.service.MemberService;
import com.elice.tripnote.global.annotation.AdminRole;
import com.elice.tripnote.global.annotation.MemberRole;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController implements SwaggerMemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;


    @MemberRole
    @GetMapping("/test1")
    public String test2() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return email + " | completed test1.";
    }

    // 회원가입
    @Override
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody MemberRequestDTO memberRequestDTO) {
        memberService.signup(memberRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/{email}")
    public ResponseEntity<MemberResponseDTO> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(memberService.getMemberResponseDTOByEmail(email));
    }

    // 이메일 중복검사 (이메일이 이미 존재하면 true 반환, 사용가능하면 false 반환)
    @Override
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
        return ResponseEntity.ok().body(memberService.checkEmailDuplicate(email));
    }

    // 닉네임 중복검사 (닉네임이 이미 존재하면 true 반환, 사용가능하면 false 반환)
    @Override
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestParam String nickname) {
        return ResponseEntity.ok().body(memberService.checkNicknameDuplicate(nickname));
    }

    // (로그인중) 닉네임 변경
    @Override
    @MemberRole
    @PatchMapping("/update-nickname")
    public ResponseEntity<Void> updateNickname(@RequestParam String newNickname) {
        memberService.updateNickname(newNickname);
        return ResponseEntity.ok().build();
    }

    // (로그인중) 비밀번호 변경 (비밀번호는 노출을 피해야 하기 때문에 RequestBody 형식으로 보냄)
    @Override
    @MemberRole
    @PatchMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordDTO newPasswordDTO) {
        memberService.updatePassword(newPasswordDTO);
        return ResponseEntity.ok().build();
    }

    // (로그인중) 회원 삭제
    @Override
    @MemberRole
    @DeleteMapping("/delete-member")
    public ResponseEntity<Void> deleteMember() {
        memberService.deleteMember();
        return ResponseEntity.ok().build();
    }

    // (로그인중) (내정보 변경 시) 비밀번호 검증
    @Override
    @MemberRole
    @GetMapping("/validate-password")
    public ResponseEntity<Boolean> validatePassword(@RequestBody PasswordDTO validatePasswordDTO) {
        return ResponseEntity.ok().body(memberService.validatePassword(validatePasswordDTO));
    }

    @Override
    @AdminRole
    @GetMapping("/admin/members")
    public ResponseEntity<Page<MemberResponseDTO>> getMembers(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok().body(memberService.findMembers(pageable));
    }

    // 로그인중인 회원 정보 리턴
    @Override
    @MemberRole
    @GetMapping()
    public ResponseEntity<MemberResponseDTO> getMemberByToken() {
        return ResponseEntity.ok().body(memberService.getMemberResponseDTO());
    }


    /*
    카카오 로그인 api
     */


    /**
     * 카카오 로그인 api
     *
     * @return
     * @throws IOException
     */
    @Override
    @GetMapping("/kakao/login")
    public ResponseEntity<Void> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        String accessToken = kakaoService.getAccessToken(code);
        System.out.println("------------------------- kakao access token: " + accessToken + " -------------------------");
        kakaoService.kakaoLogin(accessToken);
        return ResponseEntity.ok().build();
    }

    /**
     * 카카오 로그아웃
     *
     * @return 로그아웃 유저 id
     * @throws IOException
     */
    @Override
    @GetMapping("/kakao/logout")
    public ResponseEntity<Long> kakaoLogout(HttpServletResponse response) throws IOException {
        Long kakaoId = kakaoService.logout();

        log.info("로그아웃이 완료되었습니다.");
        return ResponseEntity.ok(kakaoId);
    }

    /**
     * 회원 탈퇴(카카오 연결 끊기)
     *
     * @param response
     * @return 탈퇴 유저 id
     * @throws IOException
     */
    @Override
    @GetMapping("/kakao/unlink")
    public ResponseEntity<Long> kakaoUnlink(HttpServletResponse response) throws IOException {
        //유저 아이디로 카카오 아이디 받아오기
        Long kakaoId = kakaoService.unlink();
        log.info("회원 탈퇴가 완료되었습니다.");
        return ResponseEntity.ok(kakaoId);

    }

    /**
     * 회원 탈퇴(앱이 아닌 경로로 연결 끊기)
     *
     * @param kakaoId
     * @param referrerType
     * @param authorizationHeader
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    @GetMapping("/kakao/disconnect")
    public ResponseEntity<Long> kakaoDisconnect(
            @RequestParam("user_id") String kakaoId,
            @RequestParam("referrer_type") String referrerType,
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletResponse response) throws IOException {

//            System.out.println("카카오 헤더: " + authorizationHeader);
//            System.out.println("카카오 유저 아이디: " + kakaoId);
//            System.out.println("referrer type: " + referrerType);
        log.info("탈퇴하는 user id: {}", kakaoId);

        Long logout_kakaoId = kakaoService.disconnect(Long.parseLong(kakaoId));
        log.info("회원 탈퇴가 완료되었습니다.");
        return ResponseEntity.ok(logout_kakaoId);
    }


}
