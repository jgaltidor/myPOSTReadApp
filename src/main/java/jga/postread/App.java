package jga.postread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
      }
      catch (IOException ioe) {
        return newFixedLengthResponse(
            "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
      }
      catch (ResponseException re) {
        return newFixedLengthResponse(
            "SERVER INTERNAL ERROR: ResponseException: " + re.getMessage());
      }
    }
    // get the POST body
    String postBody = files.get("postData");
    System.out.println("postBody: " + postBody);
    JSONParser parser = new JSONParser();
    JSONObject json;
    try {
      json = (JSONObject) parser.parse(postBody);
    }
    catch(ParseException e) {
      return newFixedLengthResponse(
          "SERVER INTERNAL ERROR: ParseException: " + e.getMessage());
    }
    String username = (String) json.get("username");
    String password = (String) json.get("password");
    System.out.println("username: " + username);
    System.out.println("password: " + password);
    StringBuffer rsb = new StringBuffer("<html><body>")
      .append("<p>postBody: ").append(postBody).append("</p>")
      .append("</body></html>\n");
    return newFixedLengthResponse(rsb.toString());
  }
}