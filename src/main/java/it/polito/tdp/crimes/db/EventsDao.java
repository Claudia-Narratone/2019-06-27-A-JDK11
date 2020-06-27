package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getOffenseCategories(){
		String sql="SELECT distinct offense_category_id AS categoria FROM EVENTS ORDER BY categoria ASC ";
		List<String> list = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("categoria"));
			}
			conn.close();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> getYears(){
		String sql="SELECT DISTINCT year(reported_date) AS anno FROM events ORDER BY anno ASC";
		List<Integer> list = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getInt("anno"));
			}
			conn.close();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List<Adiacenza> getAdiacenze(String categoria, Integer anno){
		String sql="SELECT e1.offense_type_id AS v1, e2.offense_type_id AS v2, COUNT(DISTINCT e1.district_id) AS peso " + 
				"FROM EVENTS AS e1, EVENTS AS e2 " + 
				"WHERE e2.offense_category_id=? " + 
				"AND e1.offense_category_id=? " + 
				"AND year(e1.reported_date)=? " + 
				"AND year(e2.reported_date)=? " + 
				"AND e1.offense_type_id!=e2.offense_type_id " + 
				"AND e1.district_id=e2.district_id " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id";
		
		List<Adiacenza> result=new ArrayList<Adiacenza>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setString(2, categoria);
			st.setInt(3, anno);
			st.setInt(4, anno);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(new Adiacenza(res.getString("v1"), res.getString("v2"), res.getInt("peso")));
			}
			conn.close();
			return result;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
}
