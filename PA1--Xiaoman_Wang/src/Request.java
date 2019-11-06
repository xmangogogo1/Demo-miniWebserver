import java.io.*;


//Get the HTTP request from client, send back the files to client
public class Request{
    InputStream in = null;
    StringBuffer requestStrB = null;

    //get inputstream from client's request
    public Request(InputStream input){
        this.in = input;
        requestStrB = new StringBuffer(2048);
    }

    //parse the client request
    public String parse() {
        //read data from socket
        int i;
        byte[] buffer = new byte[2048];

        try {
            //inputstream size 2kb
            i = in.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }

        for (int j = 0; j < i; j++) {
            requestStrB.append((char) buffer[j]);
        }

        System.out.print(requestStrB.toString());
        return getUri(requestStrB.toString());
    }

    //fetch method
    public String parseMethod() {
        if(requestStrB.toString().startsWith("GET")){
            return "GET";
        }
        if(requestStrB.toString().startsWith("POST")){
            return "POST";
        }
        if(requestStrB.toString().startsWith("PUT")){
            return "PUT";
        }
        return null;
    }

    //fetch URI
    private String getUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

}