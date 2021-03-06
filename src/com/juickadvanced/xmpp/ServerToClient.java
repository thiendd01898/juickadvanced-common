package com.juickadvanced.xmpp;

import com.juickadvanced.xmpp.messages.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: san
 * Date: 11/16/12
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerToClient {

    public String sessionId;
    public long timestamp;
    String errorMessage;
    public boolean haveMoreMessages;
    public boolean logreq;

    ArrayList<ContactOffline> contactOffline;
    ArrayList<ContactOnline> contactOnline;
    ArrayList<TimestampedMessage> incomingMessages;
    PingFromServer pingFromServer;
    PongFromServer pongFromServer;
    NewInfoNotification newInfoNotification;

    public final static String NO_SUCH_SESSION = "No such session";
    public final static String NON_LOCAL_SESSION = "Non-local session passed in JA request";
    public final static String NETWORK_CONNECT_ERROR = "Connect error: ";
    public final static String USAGE_NOT_PROVED = "Your account is not proved with given details.";
    public final static String USAGE_CANNOT_BE_PROVED_NOW = "Your account could not be proved due to error on the remote site.";

    /**
     * error constructor
     */
    public ServerToClient(String sessionId, String errorMessage) {
        this.sessionId = sessionId;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ArrayList<ContactOffline> getContactOffline() {
        return contactOffline;
    }

    public void setContactOffline(ArrayList<ContactOffline> contactOffline) {
        this.contactOffline = contactOffline;
    }

    public ArrayList<ContactOnline> getContactOnline() {
        return contactOnline;
    }

    public void setContactOnline(ArrayList<ContactOnline> contactOnline) {
        this.contactOnline = contactOnline;
    }

    public ArrayList<TimestampedMessage> getIncomingMessages() {
        return incomingMessages;
    }

    public void setIncomingMessages(ArrayList<TimestampedMessage> incomingMessages) {
        this.incomingMessages = incomingMessages;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PingFromServer getPingFromServer() {
        return pingFromServer;
    }

    public void setPingFromServer(PingFromServer pingFromServer) {
        this.pingFromServer = pingFromServer;
    }

    public PongFromServer getPongFromServer() {
        return pongFromServer;
    }

    public void setPongFromServer(PongFromServer pongFromServer) {
        this.pongFromServer = pongFromServer;
    }

    public boolean isHaveMoreMessages() {
        return haveMoreMessages;
    }

    public NewInfoNotification getNewInfoNotification() {
        return newInfoNotification;
    }

    public void setNewInfoNotification(NewInfoNotification newInfoNotification) {
        this.newInfoNotification = newInfoNotification;
    }


    public boolean getLogreq() {
        return logreq;
    }

    public void setLogreq(boolean logreq) {
        this.logreq = logreq;
    }
}
