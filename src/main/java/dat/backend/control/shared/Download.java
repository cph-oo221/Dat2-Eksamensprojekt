package dat.backend.control.shared;

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
        PrintWriter out = response.getWriter();

        response.setContentType("text/plain");

        response.setHeader("Content-Disposition" , "attachment; filename=/"+filename+"\"");

        FileInputStream inputStream = new FileInputStream("C:\\Users\\Sebastian Egeberg\\Desktop\\Programming + skole\\Dat2-Eksamensprojekt\\src\\main\\webapp\\WEB-INF\\sample.txt");

        int in;

        while((in = inputStream.read()) !=-1)
        {
            out.write(in);
        }
        inputStream.close();
        out.close();



//        File f = new File(filename);
//        int length = 0;
//        ServletOutputStream op = response.getOutputStream();
//        ServletContext context  = getServletConfig().getServletContext();
//        String mimetype = context.getMimeType( filename );
//
//        //
//        //  Set the response and go!
//        //
//        //
//        response.setContentType((mimetype != null) ? mimetype : "application/octet-stream" );
//        response.setContentLength( (int) f.length() );
//        response.setHeader( "Content-Disposition", "attachment; filename=\"" + original_filename + "\"" );
//
//        //
//        //  Stream to the requester.
//        //
//       // byte[] bbuf = new byte[1048];
//        InputStream i = new DataInputStream(f.toString());
//        DataInputStream in = new DataInputStream(i);
//
//        while ((in != null) && ((length = in.read(bbuf)) != -1))
//        {
//            op.write(bbuf,0,length);
//        }
//
//        in.close();
//        op.flush();
//        op.close();

    }
}
