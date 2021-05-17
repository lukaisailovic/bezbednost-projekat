package app.shared;

public class Job {

    private static final String DELIMITER = ",";

    private final int length;
    private final int startPos;
    private final String hash;

    public Job(int length, int startPos, String hash) {
        this.length = length;
        this.startPos = startPos;
        this.hash = hash;
    }

    public int getLength() {
        return length;
    }

    public int getStartPos() {
        return startPos;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "Job{" +
                "length=" + length +
                ", startPos=" + startPos +
                ", hash='" + hash + '\'' +
                '}';
    }

    public String serialize(){
        return this.length+DELIMITER+this.startPos+DELIMITER+hash;
    }
    public static Job deserialize(String data){
        String[] parts = data.split(DELIMITER);
        return new Job(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]), parts[2]);
    }
}
