package com.anchor.domain.mentor.api.controller;

import com.anchor.domain.mentor.api.controller.request.MailDto;
import com.anchor.domain.mentor.api.controller.request.MentoringStatusInfo;
import com.anchor.domain.mentor.api.controller.request.RandomCodeMaker;
import com.anchor.domain.mentor.api.service.MailService;
import com.anchor.domain.mentor.api.service.MentorService;
import com.anchor.global.auth.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mentors")
@RequiredArgsConstructor
@RestController
public class MentorController {

  private final MentorService mentorService;
  private final MailService mailService;
  private final HttpSession session;

  /*
   * 설계 변경으로 주석 처리
   */
//  @GetMapping("/me/schedule")
//  public ResponseEntity<MentoringUnavailableTimes> getUnavailableTimes(HttpSession httpSession) {
//    SessionUser user = (SessionUser) httpSession.getAttribute("user");
//    MentoringUnavailableTimes result = mentorService.getUnavailableTimes(user.getMentorId());
//    result.addLinks(Link.builder()
//        .setLink("self", "/me/schedule")
//        .build());
//    return ResponseEntity.ok(result);
//  }
//
//  @PostMapping("/me/schedule")
//  public ResponseEntity<String> registerUnavailableTimes(
//      @Valid @RequestBody MentoringUnavailableTimeInfo mentoringUnavailableTimeInfo, HttpSession httpSession) {
//    SessionUser user = (SessionUser) httpSession.getAttribute("user");
//    mentorService.setUnavailableTimes(user.getMentorId(), mentoringUnavailableTimeInfo.getDateTimeRanges());
//    log.info("IsEmpty: {}", mentoringUnavailableTimeInfo.getDateTimeRanges()
//        .isEmpty());
//    return ResponseEntity.ok()
//        .build();
//  }

  @PostMapping("/me/applied-mentorings")
  public ResponseEntity<String> changeMentoringStatus(
      @RequestBody MentoringStatusInfo mentoringStatusInfo,
      HttpSession httpSession) {
    SessionUser user = (SessionUser) httpSession.getAttribute("user");
    mentorService.changeMentoringStatus(user.getMentorId(), mentoringStatusInfo.getRequiredMentoringStatusInfos());
    return ResponseEntity.ok()
        .build();
  }

  @PostMapping("/register/email/send")
  public ResponseEntity emailSend(String receiver) {
    String emailCode = RandomCodeMaker.makeRandomCode();
    session.setAttribute("ecode", emailCode);
    MailDto mailDto = MailDto.builder()
        .receiver(receiver)
        .title("Anchor-Service: 이메일 인증키입니다.")
        .content(emailCode)
        .build();
    mailService.sendMail(mailDto);
    return new ResponseEntity("success", HttpStatus.OK);
  }

  @PostMapping("/register/email/auth")
  public ResponseEntity emailVerify(String userEmailCode) {
    String emailCode = (String) session.getAttribute("ecode");
    log.info("auth session email code===" + emailCode);
    if (userEmailCode.equals(emailCode)) {
      return new ResponseEntity("success", HttpStatus.OK);
    } else {
      return new ResponseEntity("fail", HttpStatus.OK);
    }
  }

}
