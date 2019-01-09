package com.communicate.update;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.communicate.entity.PeopleItem;

public class GetUserByBloodyType {
private List<PeopleItem> list ;
	
	public List<PeopleItem> getUserByBloodyType(String bloodyType){
		
		list = null;
		list = new ArrayList<PeopleItem>();
		
		try{
            // ��ȡ���ݿ�����*/
			GetConnection co = new GetConnection();
			Connection conn = co.getConn();
			String query=("SELECT * FROM `us_er` WHERE `bloody_type` ='" + bloodyType + "'");// ����SQL��� 
			Statement stmt=conn.createStatement();// ִ��SQL���
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next())
			{
				String username = rs.getString("username");
				String realName = rs.getString("name");
				String occupation = rs.getString("occupation");
				int age = rs.getInt("age");
				String nativePlace =rs.getString("native_place");
				String email = rs.getString("email");
				String contactWay = rs.getString("contact_way");
		        String location = rs.getString("location");
		        String education = rs.getString("education");
		        String sex = rs.getString("sex");
		        String bloodyType2 = rs.getString("bloody_type");
		        String constellation = rs.getString("constellation");
		        String signature = rs.getString("signature");
		        String image = rs.getString("head_image");
		        
		        PeopleItem peopleItem = new PeopleItem();
		        peopleItem.setName(username);
		        peopleItem.setRealName(realName);
		        peopleItem.setOccupation(occupation);
		        peopleItem.setAge(age);
		        peopleItem.setNativePlace(nativePlace);
		        peopleItem.setEmail(email);
		        peopleItem.setContactWay(contactWay);
		        peopleItem.setLocation(location);
		        peopleItem.setEducation(education);
		        peopleItem.setSex(sex);
		        peopleItem.setBloodyType(bloodyType2);
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
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
