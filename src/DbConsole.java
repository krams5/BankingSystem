
import org.h2.tools.Server;
public class DbConsole {
        public static void main(String[] args) throws Exception {
            Server web = Server.createWebServer("-webPort", "8082").start();
            System.out.println("H2 console running at http://localhost:8082");
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://localhost:8082"));
            } catch (Exception e) {
                System.out.println("Open http://localhost:8082 in your browser.");
            }
        }
    }

