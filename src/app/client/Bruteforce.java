package app.client;


import app.shared.Parameters;
import core.SHA1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class Bruteforce {

    private final String target;
    private BlockingQueue<String> queue;
    private final char[] charset;
    private final int startPos;
    private int checked = 0;
    private final AtomicLong hashrate;


    public Bruteforce(BlockingQueue<String> queue, int startPos, String target, AtomicLong hashrate){
        this.queue = queue;
        this.startPos = startPos;
        this.charset = Parameters.CHARSET.toCharArray();
        this.target = target;
        this.hashrate = hashrate;
    }

    public int getCharsetLength(){
        return this.charset.length;
    }

    public int getChecked() {
        return checked;
    }

    public void generate(String str, int length){
        if (length == 0) {
            this.handleGeneratedString(str);
            checked++;
            return;
        }
        if (!str.startsWith(String.valueOf(charset[startPos])) && !str.equals("")){
            this.handleGeneratedString(str);
            checked++;
            return;
        }
        for (int i = 0; i < getCharsetLength(); i++){
            generate(str + charset[i],length -1);
        }
    }

    private void handleGeneratedString(String str){
        String hash = SHA1.hash(str);
        this.hashrate.incrementAndGet();
        if (hash.equals(target)){
            this.queue.add(hash+","+str);
        }
    }



}
