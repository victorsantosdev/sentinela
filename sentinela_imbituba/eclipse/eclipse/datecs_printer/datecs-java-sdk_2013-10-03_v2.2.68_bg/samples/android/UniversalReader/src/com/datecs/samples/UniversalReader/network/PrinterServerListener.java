package com.datecs.samples.UniversalReader.network;

import java.net.Socket;

public interface PrinterServerListener {
    public void onConnect(Socket socket);
}
