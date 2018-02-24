/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.gj.controller.planillas.mantenimiento;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import org.zkoss.zhtml.Messagebox;


@WebServlet(name = "Upload", urlPatterns = {"/Upload"})
public class Upload extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    private static final long serialVersionUID = 529869125345702992L;
    String atributo;

    public Upload() {
        super();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        atributo=request.getParameter("documento");
        /*
         response.setContentType("text/html;charset=UTF-8");
         PrintWriter out = response.getWriter();
         try {
         ///TODO output your page here. You may use following sample code. 
         out.println("<!DOCTYPE html>");
         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet Upload</title>");            
         out.println("</head>");
         out.println("<body>");
         out.println("<h1>Servlet Upload at " + request.getContextPath() + "</h1>");
         out.println("</body>");
         out.println("</html>");
         } finally {
         out.close();
         }
         */
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    /*  
     @Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException {
     processRequest(request, response);
     }
     */
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        // Messagebox.show("GUARDANDO>>>>>>>>>>>>>>>>>>>>");
        System.out.println("GUARDANDO>>>>>>>>>>>>>>>>>>>>");
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        StringBuffer jb = new StringBuffer();
        StringBuffer jb2 = new StringBuffer();
        System.out.println("BUFFER>>>>>>>>>>>>>>>>>>>>" + jb);
        //  Messagebox.show("BUFFER>>>>>>>>>>>>>>>>>>>>" + jb);
        String line = null;
        String line2 = null;

        BufferedReader reader = request.getReader();

        //Messagebox.show("READER>>>>>>>>>>>>>>>>>>>>"+reader);
        System.out.println("READER>>>>>>>>>>>>>>>>>>>>" + reader);
        while ((line = reader.readLine()) != null) {
            if (line.contains("tipnumdoc")) {
                break;
            } else {
                jb.append(line);
            }
        }

        while ((line2 = reader.readLine()) != null) {
            jb2.append(line2);

        }

        String nomarchivo = jb2.toString();
        String nomarchivo2 = "";
        for (int i = 0; i < nomarchivo.length(); i++) {
            if (String.valueOf(nomarchivo.charAt(i)).equals("-")) {
                break;
            } else {
                nomarchivo2 = nomarchivo2.concat(String.valueOf(nomarchivo.charAt(i)));
            }
        }
        
        String x = atributo;

        String img64 = jb.toString();
        //Messagebox.show("IMAGEN>>>>>>>>>>>>>>>>>>>>"+img64);
        System.out.println("IMAGEN>>>>>>>>>>>>>>>>>>>>" + img64);
        img64 = img64.substring(img64.indexOf("imagen_foto") + 12);
        // Messagebox.show("IMAGEN DEPURADA>>>>>>>>>>>>>>>>>>>>"+img64);
        System.out.println("IMAGEN DEPURADA>>>>>>>>>>>>>>>>>>>>" + img64);
        if (img64 != null && img64.startsWith("data:image/jpeg;base64,")) {
            //Remove Content-type declaration
            img64 = img64.substring(img64.indexOf(',') + 1);
        } else {
            response.setStatus(403);
            out.print("Formato de imagen no correcto!");
            return;
        }
        try {
            byte[] imagedata = DatatypeConverter.parseBase64Binary(img64);
            InputStream stream = new ByteArrayInputStream(imagedata);
            BufferedImage bfi = ImageIO.read(stream);
            //String path = getServletConfig().getServletContext().getRealPath("foto.jpeg");
            //String path = "D:/prueba_"+Math.random()+".jpeg";
            String path = "D:\\GJ_CARPETAS\\PLANILLAS\\FOTO\\" + nomarchivo2.trim() + ".JPEG";
            // Messagebox.show("RUTA>>>>>>>>>>>>>>>>>>>>"+path);
            System.out.println("RUTA>>>>>>>>>>>>>>>>>>>>" + path);
            File outputfile = new File(path);
            outputfile.createNewFile();
            ImageIO.write(bfi, "JPEG", outputfile);
            bfi.flush();
            response.setStatus(200);
            Messagebox.show("Por fin se pudo grabar la imagen");
            //out.print("Por fin se pudo grabar la imagen");
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(500);
            out.print("Error durante el guardado de la imagen: " + e.getMessage());
            // Messagebox.show("Error durante el guardado de la imagen: " + e.getMessage());
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    /*
     @Override
     public String getServletInfo() {
     return "Short description";
     }// </editor-fold>
     */
}
