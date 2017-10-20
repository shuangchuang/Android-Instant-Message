package com.example.wtr.im.util;

import com.example.wtr.im.bean.PeopleItem;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wtr on 2017/6/29.
 */

//Asmack相关工具
public class XMPPUtil {

    //注册
    public static int register(XMPPConnection xmppConnection, String username, String password) {
        Registration reg = new Registration();
        reg.setType(IQ.Type.SET); //设置类型
        reg.setTo(xmppConnection.getServiceName()); //发送到哪
        reg.setUsername(username); //设置用户名
        reg.setPassword(password); //设置密码
        // 这边addAttribute不能为空，否则出错。
        reg.addAttribute("android", "geolo_createUser_android");
        PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));  //创建包过滤器
        PacketCollector collector =xmppConnection.createPacketCollector(filter);   //创建包收集器，用来获取返回结果
        xmppConnection.sendPacket(reg);  //发送包
        IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());  //获取返回结果
        // Stop queuing results停止请求results（是否成功的结果）
        collector.cancel();
        if (result == null) {
            return 0;   //服务器没结果
        } else if (result.getType() == IQ.Type.RESULT) {
            return 1;  //注册成功
        } else {
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                return 2;   //账号已存在
            } else {
                return 3;  //注册失败
            }
        }
    }

    //搜索用户
    public static List<PeopleItem> searchUsers(XMPPConnection mXMPPConnection, String userName) {
        List<PeopleItem> listUser=new ArrayList<PeopleItem>();
        try{
            UserSearchManager search = new UserSearchManager(mXMPPConnection);
            Form searchForm = search.getSearchForm("search."+mXMPPConnection.getServiceName());
            Form answerForm = searchForm.createAnswerForm();
            answerForm.setAnswer("Username", true);   //把某个字段设成true就会在那个字段里搜索关键字，search字段设置要搜索的关键字，什么不输入不会返回。
            answerForm.setAnswer("search", userName);
            ReportedData data = search.getSearchResults(answerForm,"search."+mXMPPConnection.getServiceName());
            Iterator<ReportedData.Row> it = data.getRows();
            ReportedData.Row row=null;
            while(it.hasNext()){
                row=it.next();
                PeopleItem peopleItem = new PeopleItem();
                peopleItem.setName(row.getValues("Username").next().toString());
                listUser.add(peopleItem);
            }
        }catch(Exception e){

        }
        return listUser;
    }

    //发送好友申请
    public static void applyForFriend(XMPPConnection xmppConnection, String username, String message) {
        Presence subscription=new Presence(Presence.Type.subscribe);
        subscription.setTo(username+"@"+xmppConnection.getServiceName());  //接收方
        subscription.setFrom(xmppConnection.getUser());  //发送方
        subscription.setStatus(message);  //消息
        xmppConnection.sendPacket(subscription);   //发送
    }

    //发送同意好友申请
    public static void agreeApplyForFriend(XMPPConnection xmppConnection, String username){
        //发送同意添加
        Presence subscription=new Presence(Presence.Type.subscribed);
        subscription.setTo(username+"@"+xmppConnection.getServiceName());
        xmppConnection.sendPacket(subscription);
        //再发送到之前的发送方，让其同意
        applyForFriend(xmppConnection,username,Const.AGREE_FRIEND);
    }

    //返回所有用户信息
    public static List<RosterEntry> getAllEntries(XMPPConnection xmppConnection) {
        Roster roster = xmppConnection.getRoster();
        List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
        Collection<RosterEntry> rosterEntry = roster.getEntries();
        Iterator<RosterEntry> i = rosterEntry.iterator();
        while (i.hasNext()){
            RosterEntry rosterentry=  (RosterEntry) i.next();
            EntriesList.add(rosterentry);
        }
        return EntriesList;
    }

    //判断是否为好友
    public static boolean isFriend(XMPPConnection xmppConnection,String username){
        Roster roster = xmppConnection.getRoster();
        List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
        Collection<RosterEntry> rosterEntry = roster.getEntries();
        Iterator<RosterEntry> i = rosterEntry.iterator();
        while (i.hasNext()){
            RosterEntry rosterentry=  (RosterEntry) i.next();
           if(rosterentry.getUser().split("@")[0].equals(username)){
               return true;
           }
        }
        return false;
    }

    //发送消息
    public static void sendMessage(XMPPConnection xmppConnection,String message,String toUser){
        if(xmppConnection==null||!xmppConnection.isConnected()){
            return;
        }
        ChatManager chatmanager = xmppConnection.getChatManager();
        Chat chat =chatmanager.createChat(toUser + "@" + xmppConnection.getServiceName(), null);
        if (chat != null) {
            try{
                chat.sendMessage(message);
            }
            catch (XMPPException e) {

            }
        }
    }
}
