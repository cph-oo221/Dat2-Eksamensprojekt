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
    private String SCADPATH = Env.SCADPATH;

    @Override
    public void init() throws ServletException
    {
        this.connectionPool = ApplicationStart.getConnectionPool();
        String deployed = System.getenv("DEPLOYED");
        if (deployed != null)
        {
            SCADPATH = System.getenv("SCADPATH");
        }
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

        String filename = "View"+ idReceipt +".scad";

        doDownload(response, filename);
    }

    protected void doDownload(HttpServletResponse response, String filename) throws IOException
    {
        PrintWriter out = response.getWriter();
        ServletContext context  = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(filename);

        response.setContentType((mimetype != null) ? mimetype : "application/x-openscad" );

        response.setHeader("Content-Disposition" , "attachment; filename=/"+filename+"\"");

        FileInputStream inputStream = new FileInputStream(SCADPATH + filename);

        int in;

        while((in = inputStream.read()) !=-1)
        {
            out.write(in);
        }
        inputStream.close();
        out.close();
    }
}