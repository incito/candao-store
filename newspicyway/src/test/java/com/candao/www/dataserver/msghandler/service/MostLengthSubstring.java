package com.candao.www.dataserver.msghandler.service;

/**
 * Created by ytq on 2016/4/1.
 */
public class MostLengthSubstring {
    public String execute(String s1, String s2) {
        char[] c = this.execute(s1.toCharArray(), s2.toCharArray());
        if (c != null)
            return new String(c);
        else
            return null;
    }

    public char[] execute(char[] a, char[] b) {
        int aStartPos = 0;
        int mostLength = 0;
        for (int i = 0; i < a.length - mostLength; i++) {
            for (int j = 0; j < a.length - mostLength; j++) {
                if (a[i] == b[j]) {
                    int pos = 1;
                    while ((i + pos) < a.length && (j + pos) < b.length) {
                        if (a[i + pos] == b[j + pos])
                            pos++;
                        else
                            break;
                    }
                    if (pos > mostLength) {
                        mostLength = pos;
                        aStartPos = i;
                    }
                }
            }
        }
        if (mostLength > 0) {
            char[] t = new char[mostLength];
            for (int i = 0; i < mostLength; i++) {
                t[i] = a[aStartPos + i];
            }
            return t;
        } else
            return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.print(new MostLengthSubstring().execute("13343a", "221433b"));
    }
}