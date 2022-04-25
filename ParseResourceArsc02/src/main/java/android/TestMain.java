package android;

import android.arsc.ArscParser;
import android.arsc.ArscWriter;
import android.arsc.Pkg;
import android.axml.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestMain {

    private final static String FILE_PATH = "ParseResourceArsc02/res/resources.arsc";
    private final static String ARSC_FILE_PATH = "ParseResourceArsc02/res/source_01.arsc";

    public static void main(String[] args) throws IOException {
        byte[] data = Util.readFile(new File(FILE_PATH));
        List<Pkg> pkgs = new ArscParser(data).parse();
        // ArscDumper.dump(pkgs);
        byte[] data2 = new ArscWriter(pkgs).toByteArray();
        // ArscDumper.dump(new ArscParser(data2).parse());
        Util.writeFile(data2, new File(ARSC_FILE_PATH));
    }
}
