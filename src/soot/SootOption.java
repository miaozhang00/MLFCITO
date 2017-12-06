package soot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import soot.options.Options;


public class SootOption {

	public static void setOptions(){
	    /**
	     * on windows File.separator = "\"
	     * on windows File.pathSeparator = ";"
	     * on linux File.separator = "/"
	     * on linux File.pathSeparator = ":"
	     * */
	    
		List<String> processdir= new ArrayList<String>();
		processdir.add(System.getProperty("user.dir")+File.separator+"input");
		String JAVA_HOME = "D:\\Java\\Java1.7x64\\jdk";
		String CLASS_PATH = ".;"+JAVA_HOME+"\\lib\\dt.jar;" +JAVA_HOME+"\\lib\\tools.jar;"+JAVA_HOME+"\\jre\\lib\\rt.jar;";
		System.setProperty("java.class.path", CLASS_PATH);
		
		/**
		 * 
		 Application classes vs. library classes

            Classes that Soot actually processes are called application classes. 
            This is opposed to library classes, which Soot does not process but only uses for type resolution. 
            Application classes are usually those explicitly stated on the command line or those classes that 
            reside in a directory referred to via –process-dir.

            When you use the -app option, however, then Soot also processes all classes referenced by these classes. 
            It will not, however, process any classes in the JDK, i.e. classes in one of the java.* and com.sun.* packages. 
            If you wish to include those too you have to use the special –i option, e.g. -i java. 
            See the guide on command line options for this and other command line options.
            https://github.com/Sable/soot/wiki/Introduction:-Soot-as-a-command-line-tool
		 * */
		
		// Run in application mode
		// 开启这个选项的意思是, 假如process_dir中的类依赖于其他的类, 
		// 那么soot也会试图去分析其他的类, 除非这个类找不到(会报: Warning: *** is a phantom class!)
		Options.v().set_app(true);
		
		// Run in whole-program mode
		// 开启这个选项的意思是: process_dir中的所有类是一个独立的完整的程序, 
		// 可以直接运行的, 换句话说, process_dir中至少有一个类包含main方法, 可以独立运行起来
        Options.v().set_whole_program(true);
        
        // Remember the line number
        Options.v().set_keep_line_number(true);
        
        // Ignore error if a class is not found
        // Allow unresolved classes; may cause errors
        Options.v().set_allow_phantom_refs(true);
        
        // Process all classes found in dir
        Options.v().set_process_dir(processdir);
        
        // Enable jimble transform
        // 至少开启一种转换, 常用是jb
        Options.v().setPhaseOption("jb", "enabled:true");
        
        // Enable jimble to remember the original name
        // jimble分析类, 如果不开这个选项, 无法记住变量名, 分析出来的结果变量名称都是i1, i2, i3之类的
        Options.v().setPhaseOption("jb", "use-original-names:true");
        
        // Use path as the classpath for finding classes.
		// 这个选项主要是通知soot java的系统类在哪个位置, 所以只设置java系统的环境变量就可以, 不用设置input文件夹
        Options.v().set_soot_classpath(CLASS_PATH);
	}
}