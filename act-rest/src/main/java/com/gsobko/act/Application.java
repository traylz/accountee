package com.gsobko.act;

public class Application {

    public static void main(String[] args) throws Exception {
        AccounteeRestApp accounteeRestApp =  new AccounteeRestApp();
        accounteeRestApp.runAsServer();
    }

}
