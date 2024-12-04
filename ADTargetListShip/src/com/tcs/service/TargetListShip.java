package com.tcs.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class TargetListShip extends HttpServlet {

	static Logger log = LogManager.getLogger(TargetListShip.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject jsonRes = null;
		try {
			jsonRes = new JSONObject("{\"status\":\"HTTP method GET is not supported by this URL\"}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.print(jsonRes);
		out.flush();
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		JSONObject json1 = new JSONObject();
		try {

			Properties prop = new Properties();
			try {
				prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("Web.properties"));
			} catch (FileNotFoundException ex) {
				System.out.println("Properties File Not Found : " + ex.getMessage());
				log.error("Properties File Not Found : " + ex.getMessage());
				ex.printStackTrace();
			} catch (IOException e) {
				System.out.println("Exception while reading Properties file:" + e.getMessage());
				log.error("Exception while reading Properties file:" + e.getMessage());
				e.printStackTrace();
			}
//			String csvLogFilePath = prop.getProperty("csvLogFilePath");
//			fileCreate(csvLogFilePath);
			
			if (request.getHeader("Subscription-Key") == null || !request.getHeader("Subscription-Key").equals(prop.getProperty("Subscription-Key"))){
				JSONObject jsonRes = new JSONObject("{\"status\":\"Unauthorized. Access token is missing or invalid.\"}");
				out.print(jsonRes);
				out.flush();
			} else {
			
				StringBuffer jb = new StringBuffer();
				String line = null;
	
				BufferedReader reader = request.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
				System.out.println("App Request to Java: " + jb.toString());
				log.info("App Request to Java: " + jb.toString());
	
				
				
				
				JSONObject jsonRes = new JSONObject("{\"status\":\"success\"}");
				out.print(jsonRes);
				out.flush();
			}
			
		}
		catch (Exception e) {
			out.print("PI Server Error: "+json1);
			out.flush();
			log.error(e);
			System.out.println(e);
		}
	}

	public void fileCreate(String poStatusPath) {
		String filepath = String.valueOf(poStatusPath) + LocalDate.now() + ".csv";
		File file = new File(filepath);

		try {
			boolean result = file.createNewFile();
			if (result) {

				System.out.println("file created " + file.getCanonicalPath());
				log.info("file created " + file.getCanonicalPath());
				FileWriter fw = new FileWriter(filepath, true);
				// String csvHeader = "Date,PoornataId,PI Request,Java Request,Status,Status
				// Code,ED_PORTAL Response,Java Response,RespTime";
				String csvHeader = "Date,GTIN_Number,App to Java,Java to PI,Status,Status Code,PI to Java,Java to App,RespTime";
				fw.write(String.valueOf(csvHeader) + "\n");
				fw.close();
			} else {
				System.out.println("File already exist at location: " + file.getCanonicalPath());
				log.info("File already exist at location: " + file.getCanonicalPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void csvLogStatusDetails(String poStatusPath, String poRequestData) throws IOException {
		String filepath = String.valueOf(poStatusPath) + LocalDate.now() + ".csv";
		File file = new File(filepath);
		FileWriter fw = new FileWriter(filepath, true);
		fw.write(String.valueOf(poRequestData) + "\n");
		fw.close();
	}

}
