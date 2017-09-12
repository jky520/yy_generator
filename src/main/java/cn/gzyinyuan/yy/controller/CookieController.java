package cn.gzyinyuan.yy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.stream.Stream;

/**
 * Session与Cookies
 * Created by DT人 on 2017/9/12 8:49.
 */
@Controller
public class CookieController {

    @GetMapping("/test/cookie")
    public String cookie(@RequestParam("browser") String browser, HttpServletRequest request, HttpSession session) {
        // 取出session中的browser
        Object sessionBrowser = session.getAttribute("browser");
        if(sessionBrowser == null) {
            System.out.println("不存在session,设置browser = " + browser);
            session.setAttribute("browser",browser);
        } else {
            System.out.println("存在session,browser = " + sessionBrowser.toString());
        }
        Stream.of(request.getCookies()).filter(x -> x.getValue() != null).map(cookie -> cookie.getName() + ":" + cookie.getValue()).forEach(System.out::println);
        /*Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
            for(Cookie cookie : cookies) {
                System.out.println(cookie.getName() + ":" + cookie.getValue());
            }
        }*/
        return "index";
    }
}
