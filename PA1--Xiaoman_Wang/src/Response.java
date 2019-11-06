import java.io.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

//read the file out after getting the client request，adding HTTP response header and then send back to client
public class Response{
    OutputStream out = null;
    FileOutputStream fOut = null;

    //get the output stream
    public Response(OutputStream output) {
        this.out = output;

    }

    //send the file
    public void Send(String method,String ref) throws IOException {
        //outputstream size : 20MB
        byte[] bytes = new byte[204800];
        FileInputStream fis = null;
        String errorMessage = "";

        try {
            //construct the file
            File file = new File(WebServer.WEBROOT, ref);

            if (method == null || method.isEmpty()) {
                errorMessage = "HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 21\r\n" +
                        "\r\n" +
                        "<h1>Bad Request</h1>";
                out.write(errorMessage.getBytes());
                out.flush();
            } else if (!method.equals("GET")) {

                errorMessage = "HTTP/1.1 403 Request Forbidden\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 27\r\n" +
                        "\r\n" +
                        "<h1>Request Forbidden</h1>";
                out.write(errorMessage.getBytes());
                out.flush();


            } else {
                if (file.exists()) {
                    //build the input stream
                    fis = new FileInputStream(file);

                    //read file and get length
                    int len = fis.read(bytes, 0, 204800);

                    String sBody = new String(bytes, 0);
                    //String sBody = String.valueOf(bytes);

                    //build the output message
                    String sendMessage = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + contentType(ref) + "\r\n" +
                            "Content-Length: " + len + "\r\n" +
                            "Date: " + LocalDate.now() + "\r\n" +
                            "\r\n" ;

                    //output the file header
                    out.write(sendMessage.getBytes());
                    //output the body
                    out.write(bytes);
                    out.flush();

                    } else {
                        // if file cannot find
                        errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                                "Content-Type: text/html\r\n" +
                                "Content-Length: 23\r\n" +
                                "\r\n" +
                                "<h1>File Not Found</h1>";
                        out.write(errorMessage.getBytes());
                        out.flush();
                    }
            }
        } catch(Exception e){
                // throw exception if cannot instantiate the File object
                System.out.println(e.toString());
        } finally{
                if (fis != null)
                    fis.close();

        }
    }

    //fetch all files for the indexl page
    public File[] getAllFiles(String path) {
        int nums = 0;
        List<File> list = new LinkedList<>();
        File dir = new File(path);
        File files[] = dir.listFiles();
        for(int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                list.add(files[i]);
            } else {
                //System.out.println(files[i].getAbsolutePath());

                nums++;
            }
        }

        File temp;
        while(!list.isEmpty()) {
            temp = (File)list.remove(0);
            if(temp.isDirectory()) {
                files = temp.listFiles();
                if(files != null){
                    for(int i = 0; i < files.length;i++) {
                        if(files[i].isDirectory()){
                            list.add(files[i]);
                        } else {
                          //  System.out.println(files[i].getAbsolutePath());
                            nums++;
                        }
                    }
                }
            } else {
               // System.out.println(temp);
                nums++;
            }
        }
        return files;
    }

    private String contentType(String fileName) {

        if(fileName.endsWith(".htm") || fileName.endsWith(".html")|| fileName.endsWith(".txt") || fileName.equals("/")) {
            return "text/html";
        }
        if(fileName.endsWith(".jpg") || fileName.endsWith(".JPEG")) {
            return "image/jpeg";
        }
        if(fileName.endsWith(".gif") ||fileName.endsWith(".GIF") ) {
            return "image/gif";
        }
        if(fileName.endsWith(".png") || fileName.endsWith(".PNG")) {
            return "image/png";
        }
        if(fileName.endsWith(".css")) {
            return "text/css";
        }
        if(fileName.endsWith(".js")) {
            return "text/javascript";
        }
        return "application/octet-stream";

    }
}