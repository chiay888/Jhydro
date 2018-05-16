package com.jzp.jhydro;




import java.io.IOException;
import java.net.URISyntaxException;

public final class Main {


    public static void main(String[] args) {
//        try {
//            //HttpClientSynchronousExamples.start();
//            HttpClientAsynchronousExamples.start();
//        } catch (URISyntaxException |  InterruptedException e) {
//            throw new RuntimeException("Unable to run Java 9 Http Client examples", e);
//        }

        try{
            HydroService service = new HydroService("zxzuyqstzon8wl5dtof64dudac","wnfyyg1dn8w2d7cbqxjxoboujw");
            String result = service.whiteList("acbccdas");

        }catch(Exception e){
            throw new RuntimeException("Unable to run Java 9 Http Client examples", e);
        }
    }
}

