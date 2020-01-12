package com.dayu.management;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class TestMain {


    public static void main(String[] args) throws IOException {
        String tpl = "%s,Test%08d,,,,摄像机,人脸;人体;机动车;非机动车,671,172.16.129.18,8080,admin,admin,AV01;AV02;AV03\n";
        BloomFilter<String> filter = BloomFilter.create((Funnel<String>) (s, primitiveSink) -> primitiveSink.putString(s, Charset.forName("utf-8")), 10000000);
        Writer writer = Files.asCharSink(new File("/media/leus/data/project/1w.csv"), Charset.forName("utf-8")).openBufferedStream();
        int b4 = 6101;
        for (int i = 0; i < 10000 * 1; i++) {
            String gid = String.format("%04d%02d%02d%016d", b4, (int) (Math.random() * 100), (int) (Math.random() * 100), (long) (Math.random() * Math.pow(10.0, 16.0)));
            if (filter.mightContain(gid)) {
                i--;
                continue;
            }
            filter.put(gid);
            String v = String.format(tpl, gid, i);
            writer.write(v);
        }
        writer.flush();
    }

}
