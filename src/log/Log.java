package log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import multilevelfeedback.ClassInfo;

public class Log {

    // 运行正常
    public static void logNormal(String s) {
        logTime();
        System.out.println(s);
        System.out.flush();
    }

    // 调试
    public static void logDebug(String s) {
        logTime();
        System.err.println(" DEBUG - " + s);
        System.out.flush();
    }

    // 出现错误
    public static void logError(String s) {
        logTime();
        System.err.println(" ERROR - " + s);
        System.out.flush();
    }

    // 输出信息
    public static void logInfo(String s) {
        logTime();
        System.out.println(" INFO - " + s);
        System.out.flush();
    }

    // 输出当前时间
    private static void logTime() {
        System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    // 输出 Map<String, Integer> 列表
    public static void logInfoMapSI(Map<String, Integer> MapSI) {
        if (MapSI != null) {
            for (Map.Entry<String, Integer> entry : MapSI.entrySet()) {
                System.out.println(entry.getKey() + "---->" + entry.getValue());
            }
        } else
            System.out.println("此集合为空");
    }

    // 输出Map<String, Map<String, Integer>>列表
    public static void logMapS_SI(Map<String, Map<String, Integer>> MapS_SI) {
        for (Map.Entry<String, Map<String, Integer>> entry : MapS_SI.entrySet()) {
            System.out.println("  " + entry.getKey() + " 该待测类的属性/方法依赖列表为  ");
            Map<String, Integer> map = entry.getValue();
            for (Map.Entry<String, Integer> entry2 : map.entrySet()) {
                System.out.println(entry2.getKey() + "---->" + entry2.getValue());
            }
        }

    }

    // 输出List<String>列表
    public static void logInfoListS(List<String> listOfClasses) {
        for (int i = 0; i <= listOfClasses.size() - 1; i++) {
            Log.logInfo(i + "-->" + listOfClasses.get(i));
        }

    }

    // 输出Map<String, String>列表
    public static void logMapS_S(Map<String, String> listOfDepI) {
        for (Map.Entry<String, String> entry : listOfDepI.entrySet()) {
            System.out.println(entry.getKey() + "---->" + entry.getValue());
        }
    }

    // 输出ClassInfo 对象的具体内容
    public static void logCI(ClassInfo sourceClass) {
        Log.logInfo(" 新建的ClassInfo的名称   " + sourceClass.getName());
        Log.logInfo(" 新建的ClassInfo的编号   " + sourceClass.getNo());
        Log.logInfo(" 新建的ClassInfo的父类   " + sourceClass.getParent());
        Log.logInfo("  该类" + sourceClass.getName() + "与其他类的属性依赖数目集合");
        Log.logInfoMapSI(sourceClass.getAttrDeps());
        Log.logInfo("  该类" + sourceClass.getName() + "与其他类的方法依赖数目集合");
        Log.logInfoMapSI(sourceClass.getMethodDeps());
        Log.logInfo("  该类的属性依赖最大值为 --->" + sourceClass.getMaxAttrDep());
        Log.logInfo("  该类的方法依赖最大值为 --->" + sourceClass.getMaxMethodDep());

    }
}
