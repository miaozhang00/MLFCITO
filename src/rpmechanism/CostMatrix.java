package rpmechanism;

import java.util.ArrayList;
import java.util.List;

import analyzer.Analyzer;
import analyzer.Loader;
import log.Log;
import multilevelfeedback.ClassInfo;

public class CostMatrix {

	static private double[][] costMatrix; // 测试成本矩阵
	private int num; // 测试成本矩阵大小
	static private List<ClassInfo> listOfSrc; // 类信息列表

	//为analyze设计的构造函数
	public CostMatrix() {
		num = Analyzer.getListOfClass().length;
		costMatrix = new double[num][num];
		for (int i = 0; i <= num - 1; i++) {
			costMatrix[i][i] = 0;
		}
		genListOfClassInfo();
	}

	//为load设计的构造函数
	public CostMatrix(int numberOfClasses) {
		num = numberOfClasses;
		costMatrix = new double[num][num];
		for (int i = 0; i <= num - 1; i++) {
			costMatrix[i][i] = 0;
		}
		genListOfClassInfoForLoad();
	}
	
	public static double[][] getCostMatrix(){		
		return costMatrix;
	}
	
	public static int getNum(){
		return listOfSrc.size();
	}
	public static String getNameByNo(int index){
		return listOfSrc.get(index).getName();
	}
	
	public static ClassInfo getCIByNo(int index){
		return listOfSrc.get(index);
	}
	
	public static ClassInfo getCIByName(String cName){
		for(ClassInfo ci:listOfSrc){
			if(ci.getName() == cName)
				return ci;
		}
		return null;
	}
	
	public static int getNoByName(String lastordered) {
		for(ClassInfo ci : listOfSrc){
			if(ci.getName() == lastordered)
				return ci.getNo();
		}
		return listOfSrc.size();
	}
	 
	public void initCostMatrix() {
		for (int i = 0; i <= num - 1; i++) {
			ClassInfo sourceClass = listOfSrc.get(i);
			Log.logCI(sourceClass);
			for (int j = 0; j <= num - 1 && j != i; j++) {
				//String desClass = listOfSrc.get(j).getNo();
				if (sourceClass.isDepend(j)) {
					int attr = sourceClass.getNumOfAttr(j);
					int meth = sourceClass.getNumOfMethod(j);
					costMatrix[i][j] = calCost(attr, meth);
				} else {
					if (sourceClass.isInherited(j))
						costMatrix[i][j] = 1;
					else
						costMatrix[i][j] = 0;
				}
			}
		}
	}
	
	private List<ClassInfo> genListOfClassInfo() {
		this.listOfSrc = new ArrayList<ClassInfo>();
		for (int i = 0; i <= num - 1; i++) {
			ClassInfo sourceClass = new ClassInfo(Analyzer.getListOfClass()[i]);
			this.listOfSrc.add(sourceClass);
		}
		return this.listOfSrc;
	}

	private List<ClassInfo> genListOfClassInfoForLoad() {
		this.listOfSrc = new ArrayList<ClassInfo>();
		//Log.logInfo("  为Load设计的CostMatrix的构造函数，需要genListOfClassInfoForLoad()");
		//Log.logInfo(" 加载的类列表");
		//Log.logInfoListS(Loader.getListOfClasses());
		for (int i = 0; i <= num - 1; i++) {
			//Log.logInfo("  创建ClassInfo对象  "+Loader.getListOfClasses().get(i));
			ClassInfo sourceClass = new ClassInfo(Loader.getListOfClasses().get(i));	
			this.listOfSrc.add(sourceClass);
		}
		return this.listOfSrc;
	}
	
	private double calCost(int attr, int method) {
		double attr_cost = attr / getMaxAttr();
		double method_cost = method / getMaxMethod();
		double cost = Math.sqrt(attr_cost * attr_cost / 2 + method_cost
				* method_cost / 2);
		return cost;
	}

	private int getMaxAttr() {
		int maxattr = 0;
		for (ClassInfo cinfo : listOfSrc) {
			if (maxattr < cinfo.getMaxAttrDep())
				maxattr = cinfo.getMaxAttrDep();
		}
		return maxattr;
	}

	private int getMaxMethod() {
		int maxmethod = 0;
		for (ClassInfo cinfo : listOfSrc) {
			if (maxmethod < cinfo.getMaxMethodDep())
				maxmethod = cinfo.getMaxMethodDep();
		}
		return maxmethod;
	}

}
