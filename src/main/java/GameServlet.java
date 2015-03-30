import logic.Logic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GameServlet extends HttpServlet {
    private Logic logic = new Logic();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if(session == null) {
            session = req.getSession(true);
        }

        String sessionId = session.getId();

        String method = req.getParameter("method");
        switch(method) {
            case "refresh":
                resp.getWriter().print("{\"timerstatus\":" + logic.getTimerStatus(sessionId) +
                        ", \"time\":" + logic.getTime(sessionId) + "}");
                //System.out.println("Timer status = " + logic.getTimerStatus(sessionId));
                //System.out.println("Time = " + logic.getTime(sessionId));
                break;
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

        switch (methodname) {
            case "start":
                String name = req.getParameter("name");
                Boolean isSingle = Boolean.valueOf(req.getParameter("issingle"));
                logic.addPlayer(sessionId, name, isSingle);
                String x = "{ \"package\":\""+ logic.getPackageName(sessionId)
                        +"\",\"theme\":\"" + logic.getTheme(sessionId) + "\"," +
                        "\"question\":\"" + logic.getQuestion(sessionId) + "\", \"points\": " +
                        logic.getPoints(sessionId) + "}";
                //System.out.println(x);
                resp.getWriter().print(x);
                break;
            case "end":
                //end game
                break;
            case "answer":
                String answer = req.getParameter("text");
                boolean isRight = logic.checkAnswer(sessionId, answer);
                String s = "{\"isright\":" + isRight + ", \"rightanswer\":\"" +
                        logic.getRightAnswer(sessionId) + "\", \"total\":\"" + Integer.toString(logic.getUserPoints(sessionId)) + "\"}";
                resp.getWriter().print(s);
                System.out.println(s);
                //{isRight: true/false,
                // rightanswer: "sdffghgf"}
                break;
            case "rbclick":
                logic.rbClick(sessionId);
                break;
            case "question":
                String x1 = "{\"package\":\""+ logic.getPackageName(sessionId)
                        +"\",\"theme\":\"" + logic.getTheme(sessionId) + "\"," +
                        "\"question\":\"" + logic.getQuestion(sessionId) + "\", \"points\": " +
                        logic.getPoints(sessionId) + "}";
                //System.out.println(x1);
                resp.getWriter().print(x1);
            default:
                //something else?
        }
    }
}