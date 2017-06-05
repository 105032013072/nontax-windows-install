package com.bosssoft.install.nontax.windows.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;

import com.bosssoft.platform.installer.core.IContext;
import com.bosssoft.platform.installer.core.InstallException;
import com.bosssoft.platform.installer.core.action.IAction;

public class CreateStartFile implements IAction{
    
	transient Logger logger=Logger.getLogger(getClass());
	
	public void execute(IContext context, Map params) throws InstallException {
		String environments=params.get("environments").toString();
		String servers=params.get("servers").toString();
		String batFile=params.get("targetDir").toString()+"/"+"start.bat";
		createBatFile(batFile,environments,servers,context);
		
	}


	private void createBatFile(String createFilePath, String environments, String servers, IContext context) {
		File f=new File(createFilePath);
		
        try {
        	//如果文件存在就删除，重建
    		if(f.exists()) f.delete();
    		f.createNewFile();
    		
        	BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f,true)));
        	//设置环境变量
        	String[] es=environments.split(",");
        	for (String en : es) {
        		String content="set "+en+"="+context.getStringValue(en)+System.getProperty("line.separator");
        		out.write(content);
    		}
        	
        	//设置启动的服务
        	String[] ss=servers.split(",");
		    for (String server : ss) {
		    	String serverpath=context.getStringValue(server);
		    	
		    	String folderpath=serverpath.substring(0,serverpath.lastIndexOf("/"));
		    	String serverName=serverpath.substring(serverpath.lastIndexOf("/")+1,serverpath.length());
		    	
				//String content="cd "+folderpath+System.getProperty("line.separator")+"start "+serverName+System.getProperty("line.separator");
				StringBuffer content=new StringBuffer();
				content.append("cd "+folderpath+System.getProperty("line.separator"));
				content.append(folderpath.substring(0, 2)+System.getProperty("line.separator"));//截取工作磁盘名
				content.append("start "+serverName+System.getProperty("line.separator"));
				out.write(content.toString());
			}
		    out.close();
        	
        } catch (Exception e) {
			throw new InstallException("Faild to create start File",e);
		} 
		
	}
	public void rollback(IContext context, Map params) throws InstallException {
		// TODO Auto-generated method stub
		
	}

}
