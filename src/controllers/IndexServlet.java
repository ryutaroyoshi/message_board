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

        //開くページ数を取得（デフォルトは１ページ目）
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch(NumberFormatException e) {}

        //最大件数と開始位置を指定してメッセージを取得
        List<Message> messages = em.createNamedQuery("getAllMessages",Message.class)
                                   .setFirstResult(15 * (page - 1))
                                   .setMaxResults(15)
                                   .getResultList();

        //全件数を取得
        long messages_count = (long)em.createNamedQuery("getMessagesCount",Long.class)
                                      .getSingleResult();
        /*JPQL(Message.java)の文につけたgetAllMessagesをcreateNameQueryメソッドの引数に指定してあげることで、データベースへの問い合わせを実行できる。
        その問い合わせ結果をgetResultList()メソッドを使ってリスト形式で取得する*/
        /*
        List<Message> messages = em.createNamedQuery("getAllMessages",Message.class).getResultList();
        response.getWriter().append(Integer.valueOf(messages.size()).toString());
        */
        em.close();

        request.setAttribute("messages", messages);
        request.setAttribute("message_count", messages_count);
        request.setAttribute("page", page);

        //フラッシュメッセージがセッションスコープにセットされていたら
        //リクエストスコープに保存する（セッションスコープからは削除）
        if(request.getSession().getAttribute("flash") != null) {
            request.setAttribute("flash", request.getSession().getAttribute("flash"));
            request.getSession().removeAttribute("flash");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/index.jsp");
        rd.forward(request, response);
    }

}
