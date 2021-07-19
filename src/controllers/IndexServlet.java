package controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public IndexServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        /*JPQL(Message.java)の文につけたgetAllMessagesをcreateNameQueryメソッドの引数に指定してあげることで、データベースへの問い合わせを実行できる。
        その問い合わせ結果をgetResultList()メソッドを使ってリスト形式で取得する*/
        List<Message> messages = em.createNamedQuery("getAllMessages",Message.class).getResultList();
        response.getWriter().append(Integer.valueOf(messages.size()).toString());

        em.close();

        request.setAttribute("messages", messages);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/index.jsp");
        rd.forward(request, response);
    }

}
