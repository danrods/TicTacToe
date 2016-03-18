package com.tic;


import com.data.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by drodrigues on 1/31/16.
 */
@Controller
public class PlayServlet extends HttpServlet {

    public static final String PLAY = "/play";
    public static final String MAKE_MOVE ="/play/makeMove";
    public static final String FIND_MOVE = "/play/findMove";
    public static final String PLAY_PAGE="playPage";
    private static final Logger log = Logger.getLogger(PlayServlet.class.getName());


    @RequestMapping(value=PLAY, method=RequestMethod.GET)
    public ModelAndView welcomeGet(HttpSession session, ModelMap map){
        return welcomeScreen(session, map);
    }

    @RequestMapping(value=PLAY, method= RequestMethod.POST)
    public ModelAndView welcomeScreen(HttpSession session, ModelMap map){

        User user = UserServiceFactory.getUserService().getCurrentUser();

        if(session.getAttribute("isLoggedIn") == null){
            return new ModelAndView("redirect:/");
        }

        EntityManager manager = EMF.get().createEntityManager();
        EntityTransaction trans = manager.getTransaction();
      try{
        trans.begin();
        Player gamer = manager.find(Player.class, user.getEmail());
        Game newGame = new Game();
          map.put("playerImg", newGame.getPlayerImg());
          map.put("computerImg", newGame.getComputerImg());
          map.put("isAIStart", newGame.getComputerImg().equals(Cell.CellType.O_Cell.toString()));

        if(gamer == null){
            log.log(Level.INFO, "Successfully created new Player");
            gamer = new Player(user.getEmail(), newGame);
            manager.persist(gamer);
        }
          else{
            replaceBoard(manager, newGame);

            log.log(Level.SEVERE, "Deleting board!");
        }

        trans.commit();
        log.log(Level.INFO, "Successfully created New Game!");

      }
      catch(Exception e){
          e.printStackTrace();
          if (trans.isActive()){
              trans.rollback();
              log.log(Level.SEVERE, "Rolled Back");
          }

      }
      finally{
          manager.close();
      }

        return new ModelAndView(PLAY_PAGE);
    }

    @RequestMapping(value=MAKE_MOVE, method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> makeMove(@RequestParam String selectedBlock, HttpSession session, ModelMap map) {

        log.log(Level.INFO, "Found Selected Block : " + selectedBlock);

        User user = UserServiceFactory.getUserService().getCurrentUser();
        EntityManager manager = EMF.get().createEntityManager();
        EntityTransaction trans = manager.getTransaction();

        try{
            trans.begin();
            Player gamer = manager.find(Player.class, user.getEmail());
            log.log(Level.INFO, "Found Gamer? : " + gamer + " from datastore");

            if(gamer == null){
                log.log(Level.SEVERE, "Gamer not found!");
                trans.rollback();
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
            Board board = gamer.getCurrentGame().getBoard();
            //log.log(Level.INFO, "Found Board : " + board + " from DataStore");

            int block = Integer.parseInt(selectedBlock);
            JSONObject json = board.makeMove(block);

            log.log(Level.SEVERE, "Found JSON : " + json);

            manager.merge(board);
            trans.commit();

            if(json != null){
                int val = json.getInt("win");
                if(val == 1) finishGame(manager, true);
                else if(val == 2) finishGame(manager, false);

                return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
            }
            else{

                JSONObject obj = new JSONObject();

                try {
                    obj.put("err", "taken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return new ResponseEntity<String>(obj.toString(), HttpStatus.BAD_REQUEST);
            }


        }
        catch(NumberFormatException e){
            log.log(Level.SEVERE, "Error Parsing request :  "  + selectedBlock, e);
            if (trans.isActive()){
                trans.rollback();
                log.log(Level.SEVERE, "Rolled Back");
            }

            JSONObject obj = new JSONObject();

            try {
                obj.put("err", "exception");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            return new ResponseEntity<String>(obj.toString(), HttpStatus.BAD_REQUEST);

        }
        catch(Exception e){
            e.printStackTrace();

            JSONObject obj = new JSONObject();

            try {
                obj.put("err", "exception");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            return new ResponseEntity<String>(obj.toString(), HttpStatus.BAD_REQUEST);
        }
        finally{
            manager.close();
        }

    }


    @RequestMapping(value=FIND_MOVE, method= RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> findMove(HttpSession session, ModelMap map) {




        User user = UserServiceFactory.getUserService().getCurrentUser();
        EntityManager manager = EMF.get().createEntityManager();
        EntityTransaction trans = manager.getTransaction();

        try{
            trans.begin();
            Player gamer = manager.find(Player.class, user.getEmail());
           // log.log(Level.INFO, "Found Gamer? : " + gamer + " from datastore");

            if(gamer == null){
                //log.log(Level.SEVERE, "Gamer not found!");
                trans.rollback();
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
            Board board = gamer.getCurrentGame().getBoard();
            //log.log(Level.INFO, "Found Board : " + board + " from DataStore");

            JSONObject json = board.findMove();
            log.log(Level.INFO, "Found JSON : " + json);

            trans.commit();

            if(json != null){
                int val = json.getInt("win");
                if(json!= null && (val == 1 || val == 2)) finishGame(manager, false);

                return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }

        }
        catch(Exception e){
            e.printStackTrace();
            if (trans.isActive()){
                trans.rollback();
                log.log(Level.SEVERE, "Rolled Back");
            }

            JSONObject obj = new JSONObject();

            try {
                obj.put("err", "exception");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            return new ResponseEntity<String>(obj.toString(), HttpStatus.BAD_REQUEST);
        }
        finally{
            manager.close();
        }

    }


    private void replaceBoard(EntityManager manager, Game newGame){
        User user = UserServiceFactory.getUserService().getCurrentUser();

        Player gamer = manager.find(Player.class, user.getEmail());
        Game game = gamer.getCurrentGame();
        gamer.setCurrentGame(newGame);

        manager.remove(game);
        manager.merge(gamer);


    }

    private synchronized void finishGame(EntityManager manager, boolean isWin){
        User user = UserServiceFactory.getUserService().getCurrentUser();
        Player gamer = manager.find(Player.class, user.getEmail());

        if(isWin) gamer.addWin();
        else gamer.addLoss();

        manager.merge(gamer);
    }



}
