package analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import log.Log;

public class Loader {

    static private List<String> listOfClasses; // 待测类的列表
    static private Map<String, String> listOfDepI; // 继承关系列表
    // 属性依赖列表，键——类名，值——该类属性依赖的其他类<类名，属性依赖值>
    static private Map<String, Map<String, Integer>> listOfA;
    // 方法依赖列表，键——类名，值——该类属性依赖的其他类<类名，方法依赖值>
    static private Map<String, Map<String, Integer>> listOfM;

    public Loader() {
        listOfClasses = new ArrayList<String>();
        listOfDepI = new HashMap<String, String>();
        listOfA = new HashMap<String, Map<String, Integer>>();
        listOfM = new HashMap<String, Map<String, Integer>>();

    }

    public void load() {
        // 选择待加载的文件(ANT\ATM\BCEL\DNS\SPM)
        // Scanner input = new Scanner(System.in);
        // System.out.println("选择待加载的文件：");
        // String fileName = input.next();
        // System.out.println("选择的文件是"+fileName);

        String fileName = "ANT";
        // 加载(ANT\ATM\BCEL\DNS\SPM下的)classes文件\inherited文件\attribute文件\method文件
        // Log.logInfo(" 开始加载类文件 ");
        loadClasses(fileName);
        // Log.logInfo(" 加载类文件完毕 ");

        // Log.logInfo(" 开始加载继承文件 ");
        loadInherited(fileName);
        // Log.logInfo(" 加载继承文件完毕 ");
        Log.logMapS_S(listOfDepI);

        // Log.logInfo(" 开始加载属性文件 ");
        loadAttribute(fileName);
        // Log.logInfo(" 加载属性文件完毕 ");
        // Log.logMapS_SI(listOfA);

        // Log.logInfo(" 开始加载方法文件 ");
        loadMethod(fileName);
        // Log.logInfo(" 加载方法文件完毕 ");
        // Log.logMapS_SI(listOfM);
    }

    // 读取classes文件
    private void loadClasses(String fileName) {
        String classFileName = System.getProperty("user.dir") + File.separator + "input" + File.separator + "input_"
                + fileName + File.separator + "classes";
        BufferedReader br;
        try {
            FileReader classFile = new FileReader(classFileName);
            br = new BufferedReader(classFile);
            String line = null;
            do {
                line = br.readLine(); // 一次读一行
                if (line == null)
                    continue;
                String[] info = line.split("\t"); // 以Tab键来区分
                int index = Integer.valueOf(info[0]).intValue() - 1; // 获取类的编号（源文件从1开始编号）
                String className = info[1]; // 获取类的名称
                listOfClasses.add(index, className);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取inherited文件
    private void loadInherited(String fileName) {
        String inheriFileName = System.getProperty("user.dir") + File.separator + "input" + File.separator + "input_"
                + fileName + File.separator + "inherited";
        BufferedReader br;
        try {
            FileReader inheriFile = new FileReader(inheriFileName);
            br = new BufferedReader(inheriFile);
            String line = null;
            do {
                line = br.readLine();
                if (line == null) {
                    continue;
                }
                String[] info = line.split("\t");
                String childCName = String.valueOf(Integer.parseInt(info[0]) - 1); // 获取子类名称
                String ParentCName = String.valueOf(Integer.parseInt(info[1]) - 1); // 获取父类名称
                listOfDepI.put(childCName, ParentCName);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取Attribute文件
    private void loadAttribute(String fileName) {
        String attrFileName = System.getProperty("user.dir") + File.separator + "input" + File.separator + "input_"
                + fileName + File.separator + "attribute";
        BufferedReader br;
        try {
            FileReader attrFile = new FileReader(attrFileName);
            br = new BufferedReader(attrFile);
            String line = null;
            do {
                line = br.readLine();
                if (line == null) {
                    continue;
                }
                String[] info = line.split("\t");
                String srcClassName = String.valueOf(Integer.parseInt(info[0]) - 1); // 源类
                String desClassName = String.valueOf(Integer.parseInt(info[1]) - 1); // 目标类(被源类属性依赖)
                int attrs = Integer.valueOf(info[2]).intValue(); // 属性依赖的值
                addAttrDeps(srcClassName, desClassName, attrs);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取Method文件
    private void loadMethod(String fileName) {
        String methodFileName = System.getProperty("user.dir") + File.separator + "input" + File.separator + "input_"
                + fileName + File.separator + "method";
        BufferedReader br;
        try {
            FileReader methodFile = new FileReader(methodFileName);
            br = new BufferedReader(methodFile);
            String line = null;
            do {
                line = br.readLine();
                if (line == null) {
                    continue;
                }
                String[] info = line.split("\t");
                String srcClassName = String.valueOf(Integer.parseInt(info[0]) - 1); // 源类
                String desClassName = String.valueOf(Integer.parseInt(info[1]) - 1); // 目标类(被源类方法依赖)
                int methods = Integer.valueOf(info[2]).intValue(); // 方法依赖的值
                addMethodDeps(srcClassName, desClassName, methods);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAttrDeps(String srcClassName, String desClassName, int attrs) {
        // 如果属性依赖列表包含src,不需要添加src
        if (listOfA.containsKey(srcClassName)) {
            listOfA.get(srcClassName).put(desClassName, attrs);
        } else // 说明是第一次遍历到src的属性依赖，新建其属性依赖列表
        {
            Map<String, Integer> srcAttr = new HashMap<String, Integer>();
            srcAttr.put(desClassName, attrs);
            listOfA.put(srcClassName, srcAttr);
        }
    }

    private void addMethodDeps(String srcClassName, String desClassName, int methods) {
        // 如果方法依赖列表包含src，不需要添加src
        if (listOfM.containsKey(srcClassName)) {
            listOfM.get(srcClassName).put(desClassName, methods);
        } else // 说明是第一次遍历到src的方法依赖，新建其方法依赖列表
        {
            Map<String, Integer> srcMethod = new HashMap<String, Integer>();
            srcMethod.put(desClassName, methods);
            listOfM.put(srcClassName, srcMethod);
        }
    }

    public static List<String> getListOfClasses() {
        return listOfClasses;
    }

    public static int getIndex(String CName) {
        int i = 0;
        for (String className : listOfClasses) {
            if (className == CName)
                return i;
            else
                i++;
        }
        return i = listOfClasses.size();
    }

    public static Map<String, String> getListOfDepI() {
        return listOfDepI;
    }

    public static Map<String, Map<String, Integer>> getListOfA() {
        return listOfA;
    }

    public static Map<String, Map<String, Integer>> getListOfM() {
        return listOfM;
    }

    public int getNumOfClasses() {
        return listOfClasses.size();
    }
}
