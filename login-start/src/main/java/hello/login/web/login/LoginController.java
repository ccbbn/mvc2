package hello.login.web.login;



import hello.login.domain.login.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

//    @PostMapping("/login")//검증 실패하면 바인딩 리절트가 하게
    public String login(@Validated @ModelAttribute LoginForm loginForm , BindingResult bindingResult ,
                        HttpServletRequest request){
        // 1. 검증
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        // 2. 검증 통과, 회원 가져오기
        Member loginMember =
        loginService.login(loginForm.getLoginId(), loginForm.getPassword());


        if(loginMember == null) {
            bindingResult.reject("loginFail","아이디 또는 비번이 틀림");
            return "login/loginForm";
        }

        //3. 로그인 성공 -> 세션생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관, 쿠키도 포함됨
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";

    }


    @PostMapping("/login")
    public String loginV4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;

    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);// 로그아웃하면서 남아있는 세션을 지워버림
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
