package fi.solita.clamav;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClamAVProxy {

  @Value("${clamd.host}")
  private String hostname;

  @Value("${clamd.port}")
  private int port;

  @Value("${clamd.timeout}")
  private int timeout;

  /**
   * @return Clamd status.
   */
  @RequestMapping("/")
  public String ping() throws IOException {
    ClamAVClient a = new ClamAVClient(hostname, port, timeout);
    return "Clamd responding: " + a.ping() + "\n";
  }

  @RequestMapping(value="/file", method=RequestMethod.POST, consumes = "application/octet-stream")
  public @ResponseBody String handleFileDirect(InputStream content) throws IOException{
    ClamAVClient a = new ClamAVClient(hostname, port, timeout);
    byte[] r = a.scan(content);
    return "Everything ok : " + ClamAVClient.isCleanReply(r) + "\n";
  }
}
