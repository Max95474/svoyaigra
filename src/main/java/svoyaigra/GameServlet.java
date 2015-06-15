package svoyaigra;

import model.Game;
import model.Tour;
import service.QuestionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class GameServlet extends HttpServlet {
    private SvoyaIgraLogic logic = new SvoyaIgraLogic();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //why not use getSession() (without parameter)?
        HttpSession session = req.getSession(false);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if(session == null) {
            session = req.getSession(true);
        }

        String sessionId = session.getId();

        String methodName = req.getParameter("method");
        System.out.println("GET methodName = " + methodName);
        switch(methodName) {
            case "refresh":
                resp.getWriter().print("{\"timerstatus\":" + logic.getGameSession(sessionId).getTimerType() +
                        ", \"time\":" + logic.getGameSession(sessionId).getTime() + "}");
                //System.out.println("Timer status = " + logic.getTimerStatus(sessionId));
                //System.out.println("Time = " + logic.getTime(sessionId));
                break;
            case "packages":
                System.out.println("in packages-list");
                QuestionService qs = QuestionService.getInstance();
                Game game = qs.getGamesList().get(qs.getGamesList().size() - 1);
                ArrayList<Tour> tourList = qs.getToursList(game);
                String response = "{\"packages\": [";
                for(int i = 0; i < tourList.size(); i++) {
                    response += "\"" + tourList.get(i).getTitle().replaceAll("\"", "") + "\"";
                    if(i != tourList.size() - 1) response += ",";
                }
                response += "]}";
                System.out.println(response);
                resp.getWriter().print(response);
                break;
            default:
                System.out.println("Unknown GET methodName: " + methodName);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        //new player
        if (session == null) {
            System.err.println("new player!");
            session = req.getSession(true);
           // return;
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String sessionId = session.getId();
        String methodname = req.getParameter("method");
        System.out.println("POST method = " + methodname);

        switch (methodname) {
//            case "start":
//                System.out.println("POST method = " + methodname + " name = " + req.getParameter("name"));
            case "start":
                String name = req.getParameter("name");
                Boolean isSingle = Boolean.valueOf(req.getParameter("issingle"));
                logic.addPlayer(sessionId, name, SvoyaIgraLogic.SINGLE, 0/*package number*/);
                String x = "{ \"package\":\""+ logic.getGameSession(sessionId).getPackage()
                        +"\",\"theme\":\"" + logic.getGameSession(sessionId).getCurrentTheme() + "\"," +
                        "\"question\":\"" + logic.getGameSession(sessionId).getCurrentQuestion() + "\", \"points\": " +
                        logic.getGameSession(sessionId).getPoints() + "}";
                //System.out.println(x);
                resp.getWriter().print(x);
                break;
            case "end":
                //end game
                break;
            case "answer":
                String answer = req.getParameter("text");
                boolean isRight;
                try {
                    isRight = logic.getGameSession(sessionId).checkAnswer(answer);
                } catch(GameStoppedException ex) {
                    ex.printStackTrace();
                    //handle exception
                    return;
                }
                String s = "{\"isright\":" + isRight + ", \"rightanswer\":\"" +
                        logic.getGameSession(sessionId).getRightAnswer() + "\", \"total\":\"" +
                        Integer.toString(logic.getGameSession(sessionId).getPoints()) + "\"}";
                resp.getWriter().print(s);
                System.out.println(s);
                //{isRight: true/false,
                // rightanswer: "sdffghgf"}
                break;
            case "rbclick":
                logic.getGameSession(sessionId).answerTime();
                break;
            case "question":
                String x1 = "{\"package\":\""+ logic.getGameSession(sessionId).getPackage().getName()
                        +"\",\"theme\":\"" + logic.getGameSession(sessionId).getCurrentTheme() + "\"," +
                        "\"question\":\"" + logic.getGameSession(sessionId).getCurrentQuestion() +
                        "\", \"points\": " + logic.getGameSession(sessionId).getPoints() + "}";
                //System.out.println(x1);
                resp.getWriter().print(x1);
            default:
                System.out.println("Unknown POST method: " + methodname);
        }
    }
}