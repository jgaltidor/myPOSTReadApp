package jga.postread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD
{
  public App() throws IOException {
    super(8080);
    start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
  }

  public static void main(String[] args) {
    try {
      new App();
    } catch (IOException ioe) {
      System.err.println("Couldn't start server:\n" + ioe);
    }
  }
 
  @Override
  public Response serve(IHTTPSession session) {
    
    Map<String, String> files = new HashMap<String, String>();
    Method method = session.getMethod();
    if (Method.PUT.equals(method) || Method.POST.equals(method)) {
        try {
            session.parseBody(files);
        } catch (IOException ioe) {
            return newFixedLengthResponse(
              "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
        } catch (ResponseException re) {
          return newFixedLengthResponse(
              "SERVER INTERNAL ERROR: ResponseException: " + re.getMessage());
        }
    }
    // get the POST body
    String postBody = files.get("postData");
    System.out.println("postBody: " + postBody);
    StringBuffer rsb = new StringBuffer("<html><body>")
      .append("<p>postBody: ").append(postBody).append("</p>")
      .append("</body></html>\n");
    return newFixedLengthResponse(rsb.toString());
  }
}