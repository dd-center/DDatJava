package moe.vtbs.job;

import moe.vtbs.DDatJava;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Deprecated
public class JobSocket extends WebSocketClient {
    public JobSocket(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        DDatJava.LOGGER.debug("socket opened, code " + handshakedata.getHttpStatus());
    }

    @Override
    public void onMessage(String message) {
        DDatJava.LOGGER.debug("received job, starting a processing thread");
        new Thread(new JobProcessor(message)).start();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        DDatJava.LOGGER.warn("socket closed with exit code " + code + " additional info: " + reason
        + ", remote connection: " + remote);
    }

    @Override
    public void onError(Exception ex) {
        DDatJava.LOGGER.error("an error occurred in websocket:" + ex);
        ex.printStackTrace();
    }
}
