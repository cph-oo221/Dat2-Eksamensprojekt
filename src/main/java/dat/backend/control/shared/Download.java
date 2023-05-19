package dat.backend.control.shared;

import dat.backend.model.config.Env;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;

@WebServlet(name = "Download", value = "/download")
public class Download extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String filename = request.getParameter("filename");
        String original_filename = filename;

        doDownload(request, response, filename, original_filename);

    }
    protected void doDownload(HttpServletRequest request, HttpServletResponse response, String filename, String original_filename) throws IOException
    {
        filename = "scadfil.scad";
        PrintWriter out = response.getWriter();
        ServletContext context  = getServletConfig().getServletContext();
        String mimetype = context.getMimeType( filename );

        response.setContentType((mimetype != null) ? mimetype : "application/x-openscsad" );

        response.setHeader("Content-Disposition" , "attachment; filename=/"+filename+"\"");

        FileInputStream inputStream = new FileInputStream(Env.SCADPATH);

        int in;

        while((in = inputStream.read()) !=-1)
        {
            out.write(in);
        }
        inputStream.close();
        out.close();
    }
}