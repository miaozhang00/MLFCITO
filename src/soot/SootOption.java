package soot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import soot.options.Options;

public class SootOption {

    public static void setOptions() {
        /**
         * on windows File.separator = "\" on windows File.pathSeparator = ";"
         * on linux File.separator = "/" on linux File.pathSeparator = ":"
         */

        List<String> processdir = new ArrayList<String>();
        processdir.add(System.getProperty("user.dir") + File.separator + "input");
        String JAVA_HOME = "D:\\Java\\Java1.7x64\\jdk";
        String CLASS_PATH = ".;" + JAVA_HOME + "\\lib\\dt.jar;" + JAVA_HOME + "\\lib\\tools.jar;" + JAVA_HOME
                + "\\jre\\lib\\rt.jar;";
        System.setProperty("java.class.path", CLASS_PATH);

        /**
         * 
         * Application classes vs. library classes
         * 
         * Classes that Soot actually processes are called application classes.
         * This is opposed to library classes, which Soot does not process but
         * only uses for type resolution. Application classes are usually those
         * explicitly stated on the command line or those classes that reside in
         * a directory referred to via �Cprocess-dir.
         * 
         * When you use the -app option, however, then Soot also processes all
         * classes referenced by these classes. It will not, however, process
         * any classes in the JDK, i.e. classes in one of the java.* and
         * com.sun.* packages. If you wish to include those too you have to use
         * the special �Ci option, e.g. -i java. See the guide on command line
         * options for this and other command line options.
         * https://github.com/Sable/soot/wiki/Introduction:-Soot-as-a-command-line-tool
         */

        // Run in application mode
        // �������ѡ�����˼��, ����process_dir�е�����������������,
        // ��ôsootҲ����ͼȥ������������, ����������Ҳ���(�ᱨ: Warning: *** is a phantom class!)
        Options.v().set_app(true);

        // Run in whole-program mode
        // �������ѡ�����˼��: process_dir�е���������һ�������������ĳ���,
        // ����ֱ�����е�, ���仰˵, process_dir��������һ�������main����, ���Զ�����������
        Options.v().set_whole_program(true);

        // Remember the line number
        Options.v().set_keep_line_number(true);

        // Ignore error if a class is not found
        // Allow unresolved classes; may cause errors
        Options.v().set_allow_phantom_refs(true);

        // Process all classes found in dir
        Options.v().set_process_dir(processdir);

        // Enable jimble transform
        // ���ٿ���һ��ת��, ������jb
        Options.v().setPhaseOption("jb", "enabled:true");

        // Enable jimble to remember the original name
        // jimble������, ����������ѡ��, �޷���ס������, ���������Ľ���������ƶ���i1, i2, i3֮���
        Options.v().setPhaseOption("jb", "use-original-names:true");

        // Use path as the classpath for finding classes.
        // ���ѡ����Ҫ��֪ͨsoot java��ϵͳ�����ĸ�λ��, ����ֻ����javaϵͳ�Ļ��������Ϳ���, ��������input�ļ���
        Options.v().set_soot_classpath(CLASS_PATH);
    }
}