package com.bosssoft.install.nontax.windows.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.bosssoft.install.nontax.windows.util.InstallUtil;
import com.bosssoft.platform.installer.core.IContext;
import com.bosssoft.platform.installer.core.InstallException;
import com.bosssoft.platform.installer.core.action.IAction;

/**
 * 为组件配置数据源
 * @author Windows
 *
 */
public class InitDataSource implements IAction{
	transient Logger logger = Logger.getLogger(getClass());
	
	public void execute(IContext context, Map params) throws InstallException {
		String appsvrType = context.getStringValue("APP_SERVER_TYPE");
		String deployApp=null;
		if(appsvrType.toLowerCase().indexOf("tomcat")!=-1){
			deployApp=context.getStringValue("AS_TOMCAT_HOME")+"/webapps/";
			
		}else if(appsvrType.toLowerCase().indexOf("jboss")!=-1){
			
		}else if(appsvrType.toLowerCase().indexOf("weblogic")!=-1){
			deployApp=context.getStringValue("AS_WL_DOMAIN_HOME")+"/autodeploy/";
		}
		
		String initFiles=params.get("INIT_FILES").toString();
		String[] files=initFiles.split(",");
		//读取应用中的数据源配置文件的位置，以及需要初始化的变量
		for (String ifile : files) {
			Properties p=new Properties();
			try{
				InputStream fis=new FileInputStream(ifile);
				p.load(fis);
				fis.close();
				String dbConfig=p.getProperty("DB_CONFIG");
				String dbVariables=p.getProperty("DB_variables");
				String cVariables=p.getProperty("CONTEXT_variables");
				doinit(deployApp,dbConfig,dbVariables,cVariables,context);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
	}
		
		
		
		private void doinit(String deployApp,String dbConfig, String dbVariables,String cVariables, IContext context) {
		String filepath=deployApp+dbConfig;
		String fileContent=InstallUtil.readFile(filepath);
		
		String[] dbvs=dbVariables.split(",");
		String[] cvs=cVariables.split(",");
		//变量替换
		
		for(int i=0;i<dbvs.length;i++){
			String variable=dbvs[i];
			String cvariable=cvs[i];
			Properties p=new Properties();
			try{
				InputStream fis=new FileInputStream(filepath);
				p.load(fis);
				fis.close();
			String source=variable+"="+p.getProperty(variable);
			String target=variable+"="+context.getStringValue(cvariable);
			fileContent=fileContent.replace(source, target);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		//重新写入
		try {
			BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath)));
		   out.write(fileContent);
		   out.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}

	public void rollback(IContext context, Map params) throws InstallException {
		
		
	}

}
