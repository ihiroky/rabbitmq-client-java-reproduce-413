import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 *
 */
public class Util {

    private static final Logger LOG = LoggerFactory.getLogger(Util.class);

    private static final long MAX_WAIT_TIME_MILLIS = 60 * 1000L;

    public static void retryUntilConnected(int count, InetSocketAddress endpoint, Callable<Void> callable) {
        retryUntilConnected(count, endpoint.getHostString(), endpoint.getPort(), callable);
    }

    public static void retryUntilConnected(int count, String host, int port, Callable<Void> callable) {
        var random = new Random();
        long waitTimeMillis = 1000;
        int c = 0;
        for (; c < count; c++) {
            LOG.info("[execute] Connect to {}:{}.", host, port);
            try {
                callable.call();
                break;
            } catch (ConnectException ce) {
                LOG.warn("[execute] Failed to connect to {}:{}.", host, port);
                long fluctuation = (long) (waitTimeMillis * (random.nextFloat() - 0.5f));
                long actualWaitTimeMillis = waitTimeMillis + fluctuation;
                try {
                    LOG.info("[execute] Wait {} ms.", actualWaitTimeMillis);
                    Thread.sleep(actualWaitTimeMillis);
                    waitTimeMillis += waitTimeMillis;
                    if (waitTimeMillis > MAX_WAIT_TIME_MILLIS) {
                        waitTimeMillis = MAX_WAIT_TIME_MILLIS;
                    }
                } catch (InterruptedException ie) {
                    LOG.info("[execute]", ie);
                }
            } catch (Exception e) {
                LOG.error("[execute] Unexpected exception.", e);
                break;
            }
        }
        if (c == count) {
            LOG.error("[retryUntilConnected] Reach retry limitation. Give up to connect.");
        }
    }
}
