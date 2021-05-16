package app.client;


import app.shared.Parameters;

import java.util.concurrent.BlockingQueue;

public class Bruteforce {

    private String target = null;
    private BlockingQueue<String> queue = null;
    private final char[] charset;
    private final int startPos;
    private int checked = 0;

    public Bruteforce(String hash, int startPos)  {
        this.target = hash;
        this.charset = Parameters.CHARSET.toCharArray();
        this.startPos = startPos;
    }
    public Bruteforce(BlockingQueue<String> queue, int startPos){
        this.queue = queue;
        this.startPos = startPos;
        this.charset = Parameters.CHARSET.toCharArray();
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
        if (this.target != null){
            System.out.println("----------");
            System.out.println("TARGET: " + target);
            System.out.println("CURRENT: "+ str);
            System.out.println("----------");
        }
        if (this.queue != null){
            this.queue.add(str);
        }
    }



}
