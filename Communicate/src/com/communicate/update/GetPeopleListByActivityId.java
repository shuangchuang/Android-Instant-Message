package com.communicate.update;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.communicate.entity.PeopleItem;

public class GetPeopleListByActivityId {
	private List<PeopleItem> list ;
	
	public List<PeopleItem> getPeopleList(String activityId){
		
		list = new ArrayList<PeopleItem>();
		
		try{
            // ��ȡ���ݿ�����*/
			GetConnection co = new GetConnection();
			Connection conn = co.getConn();
			String query=("SELECT `username` FROM `activity_enter` WHERE `activity_id` ='" + activityId + "'");// ����SQL��� 
			Statement stmt=conn.createStatement();// ִ��SQL���
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next())
			{
				getPeopleByUsername(rs.getString("username"));
			}
			rs.close();
			stmt.close();
			conn.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	private void getPeopleByUsername(String username){
		try{
            // ��ȡ���ݿ�����*/
			GetConnection co = new GetConnection();
			Connection conn = co.getConn();
			String query=("SELECT * FROM `us_er` WHERE `username` ='" + username + "'");// ����SQL��� 
			Statement stmt=conn.createStatement();// ִ��SQL���
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next())
			{
				String username2 = rs.getString("username");
				String realName = rs.getString("name");
				String occupation = rs.getString("occupation");
				int age = rs.getInt("age");
				String nativePlace =rs.getString("native_place");
				String email = rs.getString("email");
				String contactWay = rs.getString("contact_way");
		        String location = rs.getString("location");
		        String education = rs.getString("education");
		        String sex = rs.getString("sex");
		        String bloodyType = rs.getString("bloody_type");
		        String constellation = rs.getString("constellation");
		        String signature = rs.getString("signature");
		        String image = rs.getString("head_image");
		        
		        PeopleItem peopleItem = new PeopleItem();
		        peopleItem.setName(username2);
		        peopleItem.setRealName(realName);
		        peopleItem.setOccupation(occupation);
		        peopleItem.setAge(age);
		        peopleItem.setNativePlace(nativePlace);
		        peopleItem.setEmail(email);
		        peopleItem.setContactWay(contactWay);
		        peopleItem.setLocation(location);
		        peopleItem.setEducation(education);
		        peopleItem.setSex(sex);
		        peopleItem.setBloodyType(bloodyType);
		        peopleItem.setConstellation(constellation);
		        if(signature != null)
		        	peopleItem.setIntroduce(signature);
		        if(image != null)
		        	peopleItem.setImage(image);
		    
				list.add(peopleItem);	
			}
			rs.close();
			stmt.close();
			conn.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
