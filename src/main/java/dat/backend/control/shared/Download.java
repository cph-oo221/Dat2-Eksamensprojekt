package dat.backend.control.shared;

import dat.backend.model.config.ApplicationStart;
import dat.backend.model.config.Env;
import dat.backend.model.exceptions.DatabaseException;
import dat.backend.model.persistence.ConnectionPool;
import dat.backend.model.utilities.Model3D;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet(name = "Download", value = "/download")
public class Download extends HttpServlet
{
    private ConnectionPool connectionPool;

    @Override
    public void init() throws ServletException
    {
        this.connectionPool = ApplicationStart.getConnectionPool();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        int idReceipt = Integer.parseInt(request.getParameter("idReceipt"));

        Model3D model3D = new Model3D(idReceipt, connectionPool);
        try
        {
            model3D.generate3D();
        }
        catch (DatabaseException e)
        {
            request.setAttribute("errormessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

        doDownload(response, "View"+ idReceipt +".scad");

    }
    protected void doDownload(HttpServletResponse response, String filename) throws IOException
    {
        PrintWriter out = response.getWriter();
        ServletContext context  = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(filename);

        response.setContentType((mimetype != null) ? mimetype : "application/x-openscsad" );

        response.setHeader("Content-Disposition" , "attachment; filename=/"+filename+"\"");

        FileInputStream inputStream = new FileInputStream(Env.SCADPATH + filename);

        int in;

        while((in = inputStream.read()) !=-1)
        {
            out.write(in);
        }
        inputStream.close();
        out.close();
    }
}