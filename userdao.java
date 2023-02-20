package UserDAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ConnUtil.ConnectionFactory;
import bean.User;

public class userdao implements UserdaoI {
	 ConnectionFactory connectionFactory = new ConnectionFactory();

    public void insert()  {
    	String sql=" INSERT INTO [dbo].[Touristshop] (序次,產業類別,品牌名稱,營業地址,緯度,經度) values(?,?,?,?,?,?)" ; 
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	URL url = new URL("https://data.chiayi.gov.tw/opendata/api/getResource?oid=0a9be9b0-5da6-4df6-96f9-57b26da97e6c&rid=c649107a-a300-4932-a5c9-0b292229ac58");
			InputStream openStream = url.openStream();
			InputStreamReader isr=new InputStreamReader(openStream);
			BufferedReader bufferedReader = new BufferedReader(isr);
			String str="";
			int count = 0;
			while((str=bufferedReader.readLine())!=null) {
				String[] split = str.split(",");
				if(count++<1) {		
					continue;
				}				
				pstmt.setString(1, split[0]);
				pstmt.setString(2, split[1]);
				pstmt.setString(3, split[2]);
				pstmt.setString(4, split[3]);
				pstmt.setString(5, split[4]);
				pstmt.setString(6, split[5]);					
				pstmt.execute();				
			}
			System.out.println("css檔案成功進入SQL");
        } catch (MalformedURLException e) {		
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
    }   
    public ArrayList<User> findUser() {
		ArrayList<User> list = new ArrayList<>();
		ConnectionFactory connectionFactory = new ConnectionFactory();
	 	String sql="SELECT * From [dbo].[Touristshop]";
		try (Connection conn = connectionFactory.getConnection()){
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			FileWriter fis = new FileWriter("list3.csv");
			BufferedWriter br = new BufferedWriter(fis);
			br.append("序次");
			br.append(".");
			br.append("產業類別");
			br.append(".");
			br.append("品牌名稱");
			br.append(".");
			br.append("營業地址");
			br.append(".");
			br.append("緯度");
			br.append(".");
			br.append("經度");
			br.append("\n");				
			while(rs.next()) {					
				br.append(rs.getString(1));	
				br.append(".");
				br.append(rs.getString(2));
				br.append(".");
				br.append(rs.getString(3));
				br.append(".");
				br.append(rs.getString(4));
				br.append(".");
				br.append(rs.getString(5));
				br.append(".");
				br.append(rs.getString(6));	
				br.append("\n");
			}			
		pstmt.execute();
		br.close();		
		System.out.println("csv檔成功匯出");
		} 
		catch (SQLException e) {				
				e.printStackTrace();
			} catch (IOException e) {			
			e.printStackTrace();
		}			
		return list;				
    }	
}
