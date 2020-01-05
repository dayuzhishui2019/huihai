package com.dayu.management;

import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

public class Tests {

    @SneakyThrows
    public static void main(String[] args) throws IOException {

        String line = ",丈八路口东向西抓拍1,,610103,61010300,摄像机,人脸;人体;机动车;非机动车,172.16.129.18,8080,admin,admin,AV01;AV02;AV03" + "\n";
        File file = new File("/media/leus/data/project/设备台账100w.csv");
        Writer writer = Files.asCharSink(file, Charset.forName("utf8"), FileWriteMode.APPEND).openBufferedStream();

        for (int i = 0; i < 1000000; i++) {
            writer.write(line);
        }
        writer.flush();

    }
}
