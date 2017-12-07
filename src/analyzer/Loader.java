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

    static private List<String> listOfClasses; // ��������б�
    static private Map<String, String> listOfDepI; // �̳й�ϵ�б�
    // ���������б�������������ֵ������������������������<��������������ֵ>
    static private Map<String, Map<String, Integer>> listOfA;
    // ���������б�������������ֵ������������������������<��������������ֵ>
    static private Map<String, Map<String, Integer>> listOfM;

    public Loader() {
        listOfClasses = new ArrayList<String>();
        listOfDepI = new HashMap<String, String>();
        listOfA = new HashMap<String, Map<String, Integer>>();
        listOfM = new HashMap<String, Map<String, Integer>>();

    }

    public void load() {
        // ѡ������ص��ļ�(ANT\ATM\BCEL\DNS\SPM)
        // Scanner input = new Scanner(System.in);
        // System.out.println("ѡ������ص��ļ���");
        // String fileName = input.next();
        // System.out.println("ѡ����ļ���"+fileName);

        String fileName = "ANT";
        // ����(ANT\ATM\BCEL\DNS\SPM�µ�)classes�ļ�\inherited�ļ�\attribute�ļ�\method�ļ�
        // Log.logInfo(" ��ʼ�������ļ� ");
        loadClasses(fileName);
        // Log.logInfo(" �������ļ���� ");

        // Log.logInfo(" ��ʼ���ؼ̳��ļ� ");
        loadInherited(fileName);
        // Log.logInfo(" ���ؼ̳��ļ���� ");
        Log.logMapS_S(listOfDepI);

        // Log.logInfo(" ��ʼ���������ļ� ");
        loadAttribute(fileName);
        // Log.logInfo(" ���������ļ���� ");
        // Log.logMapS_SI(listOfA);

        // Log.logInfo(" ��ʼ���ط����ļ� ");
        loadMethod(fileName);
        // Log.logInfo(" ���ط����ļ���� ");
        // Log.logMapS_SI(listOfM);
    }

    // ��ȡclasses�ļ�
    private void loadClasses(String fileName) {
        String classFileName = System.getProperty("user.dir") + File.separator + "input" + File.separator + "input_"
                + fileName + File.separator + "classes";
        BufferedReader br;
        try {
            FileReader classFile = new FileReader(classFileName);
            br = new BufferedReader(classFile);
            String line = null;
            do {
                line = br.readLine(); // һ�ζ�һ��
                if (line == null)
                    continue;
                String[] info = line.split("\t"); // ��Tab��������
                int index = Integer.valueOf(info[0]).intValue() - 1; // ��ȡ��ı�ţ�Դ�ļ���1��ʼ��ţ�
                String className = info[1]; // ��ȡ�������
                listOfClasses.add(index, className);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ��ȡinherited�ļ�
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
                String childCName = String.valueOf(Integer.parseInt(info[0]) - 1); // ��ȡ��������
                String ParentCName = String.valueOf(Integer.parseInt(info[1]) - 1); // ��ȡ��������
                listOfDepI.put(childCName, ParentCName);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ��ȡAttribute�ļ�
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
                String srcClassName = String.valueOf(Integer.parseInt(info[0]) - 1); // Դ��
                String desClassName = String.valueOf(Integer.parseInt(info[1]) - 1); // Ŀ����(��Դ����������)
                int attrs = Integer.valueOf(info[2]).intValue(); // ����������ֵ
                addAttrDeps(srcClassName, desClassName, attrs);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ��ȡMethod�ļ�
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
                String srcClassName = String.valueOf(Integer.parseInt(info[0]) - 1); // Դ��
                String desClassName = String.valueOf(Integer.parseInt(info[1]) - 1); // Ŀ����(��Դ�෽������)
                int methods = Integer.valueOf(info[2]).intValue(); // ����������ֵ
                addMethodDeps(srcClassName, desClassName, methods);
            } while (line != null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAttrDeps(String srcClassName, String desClassName, int attrs) {
        // ������������б����src,����Ҫ���src
        if (listOfA.containsKey(srcClassName)) {
            listOfA.get(srcClassName).put(desClassName, attrs);
        } else // ˵���ǵ�һ�α�����src�������������½������������б�
        {
            Map<String, Integer> srcAttr = new HashMap<String, Integer>();
            srcAttr.put(desClassName, attrs);
            listOfA.put(srcClassName, srcAttr);
        }
    }

    private void addMethodDeps(String srcClassName, String desClassName, int methods) {
        // ������������б����src������Ҫ���src
        if (listOfM.containsKey(srcClassName)) {
            listOfM.get(srcClassName).put(desClassName, methods);
        } else // ˵���ǵ�һ�α�����src�ķ����������½��䷽�������б�
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
