package app.shared;

public class Job {

    private static final String DELIMITER = ",";

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

    public String serialize(){
        return this.length+DELIMITER+this.startPos;
    }
    public static Job deserialize(String data){
        String[] parts = data.split(DELIMITER);
        return new Job(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
    }
}
