package maxon.guessnumber;

/**
 * The stopwatch.
 *
 * @author yfwz100
 */
public class Stopwatch {

    private long lastTimestamp;
    private long lastInterval;

    public void start() {
        lastTimestamp = System.currentTimeMillis();
    }

    public long stop() {
        return lastInterval = System.currentTimeMillis() - lastTimestamp;
    }

    public long getLastInterval() {
        if (lastInterval < 0) {
            throw new RuntimeException("No last timestamp recorded.");
        } else {
            return lastInterval;
        }
    }
}
