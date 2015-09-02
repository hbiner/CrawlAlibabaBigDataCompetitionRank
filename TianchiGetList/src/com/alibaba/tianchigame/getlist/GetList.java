package com.alibaba.tianchigame.getlist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.alibaba.tianchigame.model.CalcInfo;
import com.alibaba.tianchigame.model.School;
import com.alibaba.tianchigame.model.Team;
import com.google.gson.Gson;

public class GetList {
	
	private static String New_List_File="e:\\tianchi\\7-03结果详细.xls";
	private static String Last_List_File="e:\\tianchi\\7-02结果详细.xls";
	private static int get_old_num=201;
	private static CalcInfo calc=new CalcInfo();
	private static HashMap<String, Integer> school=new HashMap<String, Integer>();
	public static void main(String[] args) throws Exception {
		
		getNewlist();
	}
	public static void getNewlist()throws Exception{
		HSSFWorkbook hwb = new HSSFWorkbook();
		HSSFSheet sheet = hwb.createSheet();
		// 在当前工作簿里，生成一个工作表
		HSSFRow row = sheet.createRow(0);
		//写入头信息
		writeHead(sheet, 0);
		for (int i = 1; i <=10; i++) {
			Gson g=new Gson();
			Team[] teams=g.fromJson(getJson(i), Team[].class);
			for (int j = 0; j < teams.length; j++) {
				countTeam(teams[j]);
				writeTeamInfo(teams[j], (i-1)*20+j+1, sheet);
			}
		}
		
		writeTail(210,sheet);
		hwb.write(new FileOutputStream(new File(New_List_File),true));
	}
	/**
	 * 将队伍信息构建成一行记录
	 * @param t
	 * @param rownum
	 * @param sheet
	 * @throws Exception
	 */
	public static void writeTeamInfo(Team t,int rownum,HSSFSheet sheet) throws Exception{
		ArrayList<String> teaminfo=getTeamItem(t);
		HSSFRow row=sheet.createRow(rownum);
		for (int i = 0; i < teaminfo.size(); i++) {
			HSSFCell cell=row.createCell(i);
			cell.setCellValue(teaminfo.get(i));
		}
	}
	/**
	 * 对学校队伍进行计数。
	 * @param t
	 */
	public static void countTeam(Team t){
		Integer num=school.get(t.getUniversity());
		if (num==null) {
			num=1;
			school.put(t.getUniversity(), num);
		}else {
			school.put(t.getUniversity(), num+1);
		}
	}
	/**
	 * 获取昨天队伍排名信息。
	 * @param path
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Team> getoldlist(String path,int num) throws Exception {
		POIFSFileSystem fs=new POIFSFileSystem(new FileInputStream(path));
		HSSFWorkbook wb=new HSSFWorkbook(fs);
		HSSFSheet sheet=wb.getSheetAt(0);
		Map<String, Team> teams=new HashMap<String, Team>();
		for (int i = 1; i <= num; i++) {
			HSSFRow row=sheet.getRow(i);
			if (row==null) {
				break;
			}
			Team t=new Team();
			HSSFCell cell0 = row.getCell(0);
				
			t.setRank(Integer.parseInt(cell0.getStringCellValue()));
			HSSFCell cell1 = row.getCell(1);
			t.setTeamName(cell1.getStringCellValue());
			HSSFCell cell2 = row.getCell(2);
			t.setUniversity(cell2.getStringCellValue());
			HSSFCell cell6 = row.getCell(6);
			t.setId(cell6.getStringCellValue());
			teams.put(t.getId(), t);
		}
		return teams;
	}
	/**
	 *  将队伍的数据转换到list中。可以直接循环使用
	 * @param t
	 * @return
	 * @throws Exception 
	 */
	public static ArrayList<String> getTeamItem(Team t) throws Exception{
		ArrayList<String> list=new ArrayList<String>(15);
		list.add(t.getRank()+"");
		list.add(t.getTeamName());
		list.add(t.getUniversity());
		list.add(hadleDouble(t.getScore())+"");
		list.add(hadleDouble(t.getPrecision())+"");
		list.add(hadleDouble(t.getRecall())+"");
		list.add(t.getId());
		list.add(t.getDateString());
		list.add(t.getCount()+"");
		list.add(t.getHit()+"");
		Map<String, Team> teams=getoldlist(Last_List_File, get_old_num);
		String content=null;
		Team old=teams.get(t.getId());
		if (old!=null) {
			if (old.getRank()>t.getRank()) {
				content="升"+(old.getRank()-t.getRank());
				calc.setUpnum(calc.getUpnum()+1);
				calc.setUpsum(calc.getUpsum()+old.getRank()-t.getRank());
			}else if (old.getRank()<t.getRank()) {
				content="降"+(t.getRank()-old.getRank());
				calc.setLownum(calc.getLownum()+1);
				calc.setLowsum(calc.getLowsum()+t.getRank()-old.getRank());
			}else {
				content="—";
			}
		}else {
			content="升>"+(200-t.getRank());
			calc.setNewin(calc.getNewin()+1);
			calc.setUpnum(calc.getUpnum()+1);
			calc.setUpsum(calc.getUpsum()+200-t.getRank());
		}
		list.add(content);
		return list;
	}
	/**
	 * 先写入文件头格式
	 * 默认是rowinde是0
	 * @param sheet
	 */
	public static void writeHead(HSSFSheet sheet,int rowindex){
		String[] header={"排名","队伍名","学校","得分","准确率","回收率","队伍编号","提交时间","提交数据条数","命中条数","排名变化"};
		HSSFRow row = sheet.createRow(rowindex);
		for (int i = 0; i < header.length; i++) {
			HSSFCell cell=row.createCell(i);
			cell.setCellValue(header[i]);
		}
	}
	/**
	 * 获取json数据。
	 */
	public static String getJson(int index) throws Exception {
		URL url = new URL(
				"http://102.alibaba.com/competition/addDiscovery/queryTotalRank.json?pageIndex="
						+ index + "&pageSize=20");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		InputStream urlStream = con.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				urlStream, "UTF-8"));
		String tmp = "";
		StringBuffer sb = new StringBuffer();
		while ((tmp = reader.readLine()) != null) {
			sb.append(tmp);
		}
		String answer = sb.toString();
		answer = answer.substring(answer.indexOf("["),
				answer.lastIndexOf("]") + 1);
		return answer;
	}
	/**
	 * 写入尾信息。
	 */
	public static void writeTail(int indexrow,HSSFSheet sheet){
		String info="本次排名中，成绩提升："+calc.getUpnum()+"人，平均提升："+
				(int)(calc.getAvgup()*100)/100.0+"名次，成绩下降："+calc.getLownum()+
				"人，平均下降："+(int)(calc.getAvglow()*100)/100.0+
				"名次，新入200名："+calc.getNewin()+"人";
		HSSFRow row= sheet.createRow(indexrow);
		row.createCell(0).setCellValue(info);
		System.out.println(info);
		
		ArrayList<School> list=new ArrayList<School>();
		for (String sk : school.keySet()) {
			School s=new School();
			s.setSchoolname(sk);
			s.setTeamsum(school.get(sk));
			list.add(s);
		}
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			HSSFRow r=sheet.createRow(indexrow+2+i);
			HSSFCell rank=r.createCell(0);
			rank.setCellValue(i+1);
			HSSFCell name=r.createCell(1);
			name.setCellValue(list.get(i).getSchoolname());
			HSSFCell num=r.createCell(2);
			num.setCellValue(list.get(i).getTeamsum()+"");
		}
		
	}
	public static double hadleDouble(double num){
		num=((int)(num*1000000))/1000000.0;
		return num;
	}
}
