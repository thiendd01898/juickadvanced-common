package com.juickadvanced.parsers;

import com.juickadvanced.data.bnw.BNWMessage;
import com.juickadvanced.data.bnw.BnwMessageID;
import com.juickadvanced.data.point.PointMessage;
import com.juickadvanced.data.point.PointMessageID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by san on 8/14/14.
 */
public class BnwSerializer {
    public static JSONObject messageToJSON(BNWMessage pm) throws JSONException {
        BnwMessageID mid = pm.getMID();
        JSONObject jo = new JSONObject();
        jo.put("body", pm.Text);
        if (pm.getRID() != 0) {
            jo.put("parent_mid", "bnw-"+pm.getMID().getId());
        } else {
            jo.put("mid", "bnw-"+pm.getMID().getId());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (DevJuickComMessages.sdftz != null) {
            DevJuickComMessages.sdftz.initSDFTZ(sdf, "GMT");
        }
        jo.put("timestamp", sdf.format(pm.Timestamp));
        if (pm.getRID() != 0) {
            jo.put("rid", pm.getRID());
            jo.put("rid_s", pm.getRIDString());
        }
        if (pm.getReplyTo() != 0) {
            jo.put("replyto", pm.getReplyTo());
        }
        JSONObject user = new JSONObject();
        jo.put("user", user);
        JSONArray arr = new JSONArray();
        for (String tag : pm.tags) {
            arr.put(tag);
        }
        if (arr.length() > 0) {
            jo.put("tags", arr);
        }
        user.put("uname", pm.User.UName);
        jo.put("mb","bnw");
        return jo;
    }
}
