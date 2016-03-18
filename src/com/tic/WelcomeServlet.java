package com.tic;

import com.data.EMF;
import com.data.Player;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by drodrigues on 1/31/16.
 */
@Controller
public class WelcomeServlet extends HttpServlet {

    private static final String QUERY = "SELECT p FROM Player p ORDER BY totalWins desc";
    private static final Logger log = Logger.getLogger(WelcomeServlet.class.getName());


    @RequestMapping(value="/welcome", method= RequestMethod.GET)
    public ModelAndView welcomeScreen(HttpSession session, ModelMap map){
        User user = UserServiceFactory.getUserService().getCurrentUser();

        if(session.getAttribute("isLoggedIn") == null){
            return new ModelAndView("redirect:/");
        }

        EntityManager manager = EMF.get().createEntityManager();

        Query query = manager.createQuery(QUERY);
        query.setMaxResults(10);
        List<Player> list = query.getResultList();
        /*List<String> list = new ArrayList<>();
        list.add("Tom");
        list.add("Bobby");
        list.add("Sandra");
        list.add("Topeka");
        list.add("Irving");
        */
        map.put("players", list);
        return new ModelAndView("welcome");
    }

    @RequestMapping(value="/welcome/{name}", method=RequestMethod.GET)
    public String welcomeUser(@PathVariable String name, ModelMap map){
        map.put("userName", name);
        return "hello";
    }


}
