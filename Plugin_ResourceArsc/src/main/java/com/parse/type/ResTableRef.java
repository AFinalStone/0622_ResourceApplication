package com.parse.type;

/**
 * @author syl
 * @time 2022/4/28 10:39
 */
public class ResTableRef {

    public int ident;

    public static int getSize() {
        return 4;
    }

    @Override
    public String toString() {
        return "ResTableRef{" +
                "ident=" + ident +
                '}';
    }
}
