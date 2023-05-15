package io.playdata.security.controller;

import io.playdata.security.model.Account;
import io.playdata.security.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "main";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/join")
    public String showJoinPage() {
        return "join";
    }
    @PostMapping("/join")
    public String join(Account account) {
        // 가입 로직을 수행하고 결과를 처리하는 부분을 구현하세요.
        // account 객체에는 사용자로부터 입력받은 값들이 들어 있습니다.
        // 필요에 따라서 AccountService 등의 서비스 클래스를 사용할 수도 있습니다.
        // 예를 들면:
        // accountService.join(account);
        loginService.join(account);
        // 가입이 성공적으로 완료되었다면 다음 페이지로 이동하도록 설정합니다.
        return "redirect:/login";
    }

    @GetMapping("/basic")
    public String basic(Model model) {
        model.addAttribute("grade", "basic");
        return "movie";
    }

    @GetMapping("/gold")
    public String gold(Model model) {
        model.addAttribute("grade", "gold");
        return "movie";
    }
}