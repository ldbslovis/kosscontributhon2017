package com.ankus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



/**
* <pre>
* 1. 패키지명 : com.ankus
* 2. 타입명 : MnetCrawler.java
* 3. 작성일 : 2017. 11. 20. 오전 1:26:25
* 4. 작성자 : mypc
* 5. 설명 : 엠넷 차트 크롤러
* </pre>
*/
public class MnetCrawler {
	public static void main(String[] args){
		try {
			final String url = "http://www.mnet.com/chart/TOP100/";
			final int ok = 200;
			String currentURL;
			int page = 801;
			int year = 2011;
			int flag =1;
			int status = ok;
			Connection.Response response = null;
			Document doc = null;
			String fileName = "D:\\mnetchart2.txt";
			String information = "";
			File file = new File(fileName);
			FileWriter fw = new FileWriter(file,true);
			while(status ==ok){
				flag=1;
				while(flag!=3){
					currentURL=url+String.format("%d", year)+String.format("%04d",page)+"00?pNum="+String.format("%d", flag);
				
					response = Jsoup.connect(currentURL).timeout(10*5000)
							.userAgent("Mozilla/5.0")
							.execute();
					status = response.statusCode();
					if(status==ok){
						doc = response.parse();
						Elements main = doc.select("div.fix").select("div#content").select("div.MnetMusicList").select("div.MMLTable");//
						Boolean lml = main.select("div.MMLITitleSong_Box").first().getElementsByTag("a").isEmpty();
						if( lml!=true){
							for(int p=0;p<50;p++){
								information += (p+1+(flag-1)*50)+"��"+year+String.format("%04d",page)+"��"+main.select("div.MMLITitleSong_Box").get(p).select("a.MMLI_Song").text()+"��"+
												main.select("div.MMLITitle_Info").get(p).select("a.MMLIInfo_Artist").text()+"��"+
												main.select("div.MMLITitle_Info").get(p).select("a.MMLIInfo_Album").text()+"\r\n";
							}
							fw.write(information);
							information="";
						}	
					}
					System.out.println(year+"-"+page+"-"+flag);
					flag++;
				}
				page++;
				
				if((page/10)%10==3){
					if((page/100)==12){
						year++;
						page = 101;
					}else{
						page = ((page/100)+1)*100+1;
					}
				}
				if(year==2017&&page==1111){
					break;
				}
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
