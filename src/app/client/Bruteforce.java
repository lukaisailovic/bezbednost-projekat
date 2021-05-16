package app.client;


import app.shared.Parameters;

public class Bruteforce {

    private final String target;
    private final char[] charset;
    private final int startPos;
    private int checked = 0;

    public Bruteforce(String hash, int startPos)  {
        this.target = hash;
        this.charset = Parameters.CHARSET.toCharArray();
        this.startPos = startPos;
    }

    public int getCharsetLength(){
        return this.charset.length;
    }

    public int getChecked() {
        return checked;
    }

    public void generate(String str, int length){
        if (length == 0) {
            System.out.println(str);
            checked++;
            return;
        }
        if (!str.startsWith(String.valueOf(charset[startPos])) && !str.equals("")){
            System.out.println(str);
            checked++;
            return;
        }
        for (int i = 0; i < getCharsetLength(); i++){
            generate(str + charset[i],length -1);
        }
    }



}
