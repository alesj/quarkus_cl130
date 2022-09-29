package com.alesj.qcl.app;

import io.vertx.core.buffer.Buffer;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.security.KeyStore;
import java.util.Arrays;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.impl.KeyStoreHelper;

/**
 * @author Ales Justin
 */
public class Main {

    private static final int MAGIC = 0xfeedfeed;
    private static final int VERSION_1 = 0x01;
    private static final int VERSION_2 = 0x02;

    public static void main(String[] args) throws Exception {
        byte[] original = Files.readAllBytes(Path.of("/Users/alesj/projects/redhat/quarkus/integration-tests/grpc-mutual-auth/src/main/resources/tls/ca.jks"));
        System.out.println("original = " + Arrays.toString(original));
        Buffer buffer1 = Buffer.buffer(original);

        KeyStore ks = KeyStoreHelper.loadKeyStore("JKS", null, "123456", () -> buffer1, null);
        KeyStoreHelper helper = new KeyStoreHelper(ks, "123456", null);
        System.out.println("helper = " + helper);

        byte[] bytes = Main.class.getClassLoader().getResourceAsStream("tls/ca.jks").readAllBytes();
        System.out.println("bytes = " + Arrays.toString(bytes));
        Buffer buffer2 = Buffer.buffer(bytes);

        ks = KeyStoreHelper.loadKeyStore("JKS", null, "123456", () -> buffer2, null);
        helper = new KeyStoreHelper(ks, "123456", null);
        System.out.println("helper = " + helper);
    }
}
