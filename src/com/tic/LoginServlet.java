package com.tic;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by drodrigues on 1/31/16.
 */
@Controller
public class LoginServlet extends HttpServlet {

    private static final String LOGIN_SUCCESS = "/login";
    private static final String LOGIN_PAGE = "/";

    private static final String LOGIN_URL="loginURL";
    private static final String LOGOUT_URL="logoutURL";
    private static final String IS_AUTH = "isLoggedIn";
    private static final String USER = "user";

    @RequestMapping(value=LOGIN_PAGE, method= RequestMethod.GET)
    public ModelAndView loginScreen(HttpSession session, ModelMap map){

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if(user != null){
            //return new ModelAndView("redirect:"+LOGIN_SUCCESS);
        }

        if(session.getAttribute(LOGIN_URL) == null){
            session.setAttribute(LOGIN_URL, userService.createLoginURL(LOGIN_SUCCESS));
        }
        session.removeAttribute(USER);
        session.removeAttribute(IS_AUTH);

        return new ModelAndView("login");
    }


    @RequestMapping(value=LOGIN_SUCCESS, method= RequestMethod.GET)
    public ModelAndView login(HttpSession session, ModelMap map){

        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null){

            if(session.getAttribute(LOGOUT_URL) == null){
                session.setAttribute(LOGOUT_URL, userService.createLogoutURL(LOGIN_PAGE));
            }

            session.setAttribute(IS_AUTH, true);
            session.setAttribute(USER, user);
            return new ModelAndView("redirect:/welcome");
        }
        else{
            session.removeAttribute(IS_AUTH);
            session.removeAttribute(USER);
            return new ModelAndView("redirect:/");
        }

    }

}
