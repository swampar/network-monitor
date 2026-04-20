import java.net.InetAddress;
import java.io.FileWriter;
import java.time.LocalDateTime;

public class Main {

    static class MonitorThread extends Thread {
        private String host;

        public MonitorThread(String host) {
            this.host = host;
        }

        public void run() {
            try {
                InetAddress inet = InetAddress.getByName(host);

                while (true) {
                    long start = System.currentTimeMillis();
                    boolean reachable = inet.isReachable(5000);
                    long latency = System.currentTimeMillis() - start;

                    String timestamp = LocalDateTime.now().toString();
                    String result = timestamp + " | " + host + " -> " + latency + " ms";

                    // Print output
                    System.out.println(result);

                    // Log to file (CSV format)
                    FileWriter writer = new FileWriter("log.csv", true);
                    writer.write(host + "," + latency + "," + timestamp + "\n");
                    writer.close();

                    // Alert system
                    if (latency > 100) {
                        System.out.println("⚠️ ALERT: High latency detected for " + host);
                    }

                    // If host unreachable
                    if (!reachable) {
                        System.out.println("❌ Host not reachable: " + host);
                    }

                    Thread.sleep(3000); // 3 seconds delay
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("🚀 Network Monitoring Tool Started...\n");

        // Add multiple hosts
        new MonitorThread("google.com").start();
        new MonitorThread("facebook.com").start();
        new MonitorThread("youtube.com").start();
        new MonitorThread("amazon.com").start();
    }
}