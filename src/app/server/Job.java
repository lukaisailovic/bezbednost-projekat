package app.server;

public class Job {
    private final int length;
    private final int startPos;

    public Job(int length, int startPos) {
        this.length = length;
        this.startPos = startPos;
    }

    public int getLength() {
        return length;
    }

    public int getStartPos() {
        return startPos;
    }

    @Override
    public String toString() {
        return "Job{" +
                "length=" + length +
                ", startPos=" + startPos +
                '}';
    }
}
