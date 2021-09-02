package servlet;

import Tools.Keyword;
import Tools.Read;
import Tools.Segmenter;
import Tools.TFIDF;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "segServlet", urlPatterns = "/segServlet")
public class SegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String filePath = session.getAttribute("filePath").toString();

        String content = Read.readPdf(filePath);

        TFIDF tfidf = new TFIDF();
        List<Keyword> list = tfidf.analyze(content, -1);

        HashMap<String,Double> hashMap = new HashMap<>();
        for (Keyword word : list)
            hashMap.put(word.getName(),word.getTfidfvalue());

        //Segmenter segmenter = new Segmenter();
        //HashMap<String, Integer> hashMap = segmenter.getWordFreq(filePath);

        req.setAttribute("data", hashMap);
        req.getRequestDispatcher("/wordcloud.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}
