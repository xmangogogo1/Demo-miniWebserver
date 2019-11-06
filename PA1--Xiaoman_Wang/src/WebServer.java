import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Use socket to implement a web server. Multi-thread to increase its usability
class WebServer
{
    public static String WEBROOT = "./";
    public static String defaultPage = "SCU.htm";

    public static void main (String [] args) throws IOException
    {
        System.out.println ("Starting the server...\n");
        ServerSocket server = new ServerSocket (Integer.valueOf(args[3]));
        while (true)
        {
            //blocking until there comes client socket
            Socket sk = server.accept ();
            System.out.println ("Accepting Connection...\n");
            WEBROOT = args[1];
            //assign a thread pool with total number of 3 threads for the server to process the request
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
            //start the server thread with the socket
            fixedThreadPool.execute(new WebThread(sk));
            System.out.println(fixedThreadPool.toString());

        }
    }
}


//Multi thread to serve multiple clients
class WebThread extends Thread {
    private Socket socket;

    public WebThread (Socket skt) {
        this.socket = skt;
    }

    public void run () {
        InputStream in = null;
        OutputStream out = null;

        try{
            in = socket.getInputStream();
            out = socket.getOutputStream();

            //Receive the requests from the client
            Request rq = new Request(in);
            //parse the request

            String sURL = rq.parse();
            if(sURL.equals("/") || sURL.equals("/index.html")) {
                sURL = WebServer.defaultPage;
            }
            // System.out.println("after parsing sURL= " + sURL);


            Response rp = new Response(out);

            String method = rq.parseMethod();
            rp.Send(method,sURL);

        } catch (IOException e) {
            System.out.println (e.toString ());

        } finally {
            System.out.println ("closing the connection...\n");
            //release the resource
            try{
                if (in != null) in.close ();
                if (out != null) out.close ();
                if (socket != null) socket.close ();
            } catch (IOException e) {
            }
        }
    }
}