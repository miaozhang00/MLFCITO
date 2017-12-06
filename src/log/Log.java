package log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import multilevelfeedback.ClassInfo;

public class Log {
	
	// ��������
	public static void logNormal(String s){
		logTime();
		System.out.println(s);
		System.out.flush();
	}

	// ����
	public static void logDebug(String s){
		logTime();
		System.err.println(" DEBUG - "+s);
		System.out.flush();
	}
	
	// ���ִ���
	public static void logError(String s){
		logTime();
		System.err.println(" ERROR - "+s);
		System.out.flush();
	}
	
	// �����Ϣ
	public static void logInfo(String s){
		logTime();
		System.out.println(" INFO - "+s);
		System.out.flush();
	}
	
	// �����ǰʱ��
	private static void logTime() {
		System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date()));
	}

	// ��� Map<String, Integer> �б�
	public static void logInfoMapSI(Map<String, Integer> MapSI) {
		if(MapSI != null){
			for(Map.Entry<String, Integer> entry: MapSI.entrySet()){
				System.out.println(entry.getKey()+"---->"+entry.getValue());
			}
		}
		else System.out.println("�˼���Ϊ��");
	}

	// ���Map<String, Map<String, Integer>>�б�
	public static void logMapS_SI(Map<String, Map<String, Integer>> MapS_SI) {
		for(Map.Entry<String, Map<String, Integer>> entry: MapS_SI.entrySet()){
			System.out.println("  "+entry.getKey()+" �ô����������/���������б�Ϊ  ");
			Map<String, Integer> map = entry.getValue();
			for(Map.Entry<String, Integer> entry2: map.entrySet()){
				System.out.println(entry2.getKey()+"---->"+entry2.getValue());
			}
		}
		
	}

	// ���List<String>�б�
	public static void logInfoListS(List<String> listOfClasses) {
		for (int i = 0; i <= listOfClasses.size() - 1; i++) {
			Log.logInfo(i+"-->"+listOfClasses.get(i));
		}
		
	}

	// ���Map<String, String>�б�
	public static void logMapS_S(Map<String, String> listOfDepI) {
		for(Map.Entry<String, String> entry: listOfDepI.entrySet()){
			System.out.println(entry.getKey()+"---->"+entry.getValue());
		}	
	}

	// ���ClassInfo ����ľ�������
	public static void logCI(ClassInfo sourceClass) {
		Log.logInfo(" �½���ClassInfo������   "+sourceClass.getName());
		Log.logInfo(" �½���ClassInfo�ı��   "+sourceClass.getNo());
		Log.logInfo(" �½���ClassInfo�ĸ���   "+sourceClass.getParent());
		Log.logInfo("  ����" + sourceClass.getName() + "�������������������Ŀ����");
		Log.logInfoMapSI(sourceClass.getAttrDeps());
		Log.logInfo("  ����" + sourceClass.getName() + "��������ķ���������Ŀ����");
		Log.logInfoMapSI(sourceClass.getMethodDeps());
		Log.logInfo("  ����������������ֵΪ --->" + sourceClass.getMaxAttrDep());
		Log.logInfo("  ����ķ����������ֵΪ --->" + sourceClass.getMaxMethodDep());	
		
	}
}
