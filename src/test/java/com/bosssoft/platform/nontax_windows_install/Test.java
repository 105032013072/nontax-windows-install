package com.bosssoft.platform.nontax_windows_install;

import java.io.File;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;



public class Test {

	public static void main(String[] args) {
		String conftemp="E:/eclipse/workspace/nontax-windows-install/target/classes//option_comps/appframe/bosssoft_templet.vm";
		File f=new File(conftemp);
		System.out.println(f.getParent());
		System.out.println(f.getAbsolutePath());
		System.out.println(f.getName());
		
		
		Properties p = new Properties();
        // 初始化默认加载路径为：D:/template
        p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, f.getParent());
        p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        // 初始化Velocity引擎，init对引擎VelocityEngine配置了一组默认的参数 
        Velocity.init(p);
		
		Template t = Velocity.getTemplate(f.getName()); 
		VelocityContext vc = new VelocityContext();

	    vc.put("IP", "127.0.0.1");
		StringWriter writer = new StringWriter();
		t.merge(vc, writer); 
		System.out.println(writer.toString());
	}

}
