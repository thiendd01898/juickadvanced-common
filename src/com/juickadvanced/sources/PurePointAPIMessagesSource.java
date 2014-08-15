package com.juickadvanced.sources;

import com.juickadvanced.IHTTPClientService;
import com.juickadvanced.RESTResponse;
import com.juickadvanced.Utils;
import com.juickadvanced.data.MessageID;
import com.juickadvanced.data.juick.JuickMessage;
import com.juickadvanced.data.juick.JuickMessageID;
import com.juickadvanced.data.point.PointMessageID;
import com.juickadvanced.parsers.DevJuickComMessages;
import com.juickadvanced.parsers.JuickParser;
import com.juickadvanced.parsers.PointNetParser;
import com.juickadvanced.parsers.URLParser;
import com.juickadvanced.protocol.JuickHttpAPI;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by san on 8/8/14.
 */
public class PurePointAPIMessagesSource extends PureMessageSource {
    public URLParser urlParser;
    int lastMessageDownloaded;

    public PurePointAPIMessagesSource(IHTTPClientService httpClientService) {
        super(httpClientService);
    }

    public static ArrayList<JuickMessage> parseJSONpure(String jsonStr, boolean storeSource) {
        ArrayList<JuickMessage> messages = new ArrayList<JuickMessage>();
        if (jsonStr != null) {
            try {
                JSONArray json = new JSONArray(jsonStr);
                int cnt = json.length();
                for (int i = 0; i < cnt; i++) {
                    JSONObject jsonObject = json.getJSONObject(i);
                    JuickMessage msg = JuickParser.initFromJSON(jsonObject);
                    if (msg.User != null && msg.User.UName != null) {
                        msg.User.UName = msg.User.UName.trim();
                    }
                    msg.Text = DevJuickComMessages.unjuick(msg.Text);
                    messages.add(msg);
                    if (!storeSource)
                        msg.source = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        HashMap<Integer, JuickMessage> replies = new HashMap<Integer, JuickMessage>();
        for (JuickMessage message : messages) {
            if (message.getRID() > 0) {
                replies.put(message.getRID(), message);
            }
        }
        for (JuickMessage message : messages) {
            if (message.getRID() > 0 && message.getReplyTo() > 0) {
                JuickMessage repliedTo = replies.get(message.getReplyTo());
                if (repliedTo != null && !message.Text.startsWith("@")) {
                    message.Text = "@"+repliedTo.User.UName+" "+message.Text;
                }
            }
        }
        return messages;
    }

    public void init(String url) {
        urlParser = new URLParser(url);
    }

    @Override
    public void getFirst(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        lastMessageDownloaded = 0;
        fetchURLAndProcess(notification, cont);
    }

    protected void fetchURLAndProcess(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        Map<String,String> argsMap = urlParser.getArgsMap();
        if (lastMessageDownloaded == 0) {
            argsMap.remove("before");
        } else {
            argsMap.put("before", ""+lastMessageDownloaded);
        }
        final String jsonStr = httpClientService.getJSON(urlParser.getFullURL(), notification).getResult();
        if (jsonStr != null) {
            ArrayList<JuickMessage> messages = new PointNetParser().parseAPIMessageListPure(jsonStr);
            for (int i = 0; i < messages.size(); i++) {
                JuickMessage message = messages.get(i);
                if (((PointMessageID) message.getMID()).uid == lastMessageDownloaded) {
                    messages.remove(i);
                    i--;
                }
            }
            if (messages.size() > 0) {
                PointMessageID pointMid = (PointMessageID)messages.get(messages.size() - 1).getMID();
                lastMessageDownloaded = pointMid.uid;
            }
            cont.apply(messages);
        } else {
            // error (notified via Notification)
            cont.apply(new ArrayList<JuickMessage>());
        }
    }

    @Override
    public void getNext(Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        fetchURLAndProcess(notification, cont);
    }

    @Override
    public void getChildren(MessageID parent, Utils.Notification notification, Utils.Function<Void, ArrayList<JuickMessage>> cont) {
        ArrayList<JuickMessage> download = new PureChildrenDownloader().download(parent, httpClientService, notification, false);
        cont.apply(download);
    }

    /**
    * Created by san on 8/9/14.
    */
    public static class PureChildrenDownloader {
        public ArrayList<JuickMessage> download(final MessageID mid, IHTTPClientService client, final Utils.Notification notifications, boolean storeSource) {
            String midString = ((PointMessageID)mid).getId();
            String url = "http://point.im/api/post/" + midString;
            RESTResponse jsonWithRetries = client.getJSON(url, notifications);
            if (jsonWithRetries.errorText != null) {
                return new ArrayList<JuickMessage>();
            } else {
                String result = jsonWithRetries.result;
                ArrayList<JuickMessage> messages = new PointNetParser().parseAPIPostAndReplies(result);
                return messages;
            }
        }

    }
}
