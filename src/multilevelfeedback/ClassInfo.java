package multilevelfeedback;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import log.Log;
import analyzer.Analyzer;
import analyzer.Loader;
import soot.SootClass;

public class ClassInfo {
	private int no; //����ı��
	private SootClass sClass;  //����
	private String sCName; //��������
	private Map<String, Integer> attrDeps; //������������������Ŀ����
	private Map<String, Integer> methodDeps; //�������෽��������Ŀ����
	private String parentCName; //����ĸ���
	private int maxAttrDep; //����������Ŀ���ֵ
	private int maxMethodDep; //����������Ŀ���ֵ
	
	//ΪLoadд�Ĺ��췽�������ڴ洢load�Ľ��
	public ClassInfo(String sCName){
		this.sCName = sCName;
		this.no = Loader.getIndex(this.sCName);
		genAttrDepsforLoad();
		genMethodDepsforLoad();
		Log.logInfo("  ���Ҹ���");
		getParentClassforLoad();
		setMaxAttrDep();
		setMaxMethodDep();
	}
	
	//ΪAnalyzeд�Ĺ��췽�������ڴ洢analyze�Ľ��
	public ClassInfo(SootClass sClass){
		this.sClass = sClass;
		this.sCName = sClass.getName();
		this.no = Analyzer.getIndex(this.sCName);
		genAttrDeps();
		genMethodDeps();
		getParentClass();
		setMaxAttrDep();
		setMaxMethodDep();
	}
	
	private void genAttrDepsforLoad() {
		//Log.logInfo(" ClassInfo-->genAttrDepsforLoad()");
		// �жϸ����Ƿ�ͬ����������������
		if (Loader.getListOfA().containsKey(String.valueOf(this.no))) {
			// ��ȡ������������������Ŀ����
			this.attrDeps = Loader.getListOfA().get(String.valueOf(this.no));
		} else
			this.attrDeps = null;
	}
	
	private void genMethodDepsforLoad() {
		//Log.logInfo("  ClassInfo -->genMethodDepsforLoad()");
		// �жϸ����Ƿ�ͬ�������з�������
		if(Loader.getListOfM().containsKey(String.valueOf(this.no))){
			// ��ȡ�������෽��������Ŀ����
			this.methodDeps = Loader.getListOfM().get(String.valueOf(this.no));
		}else 
			this.methodDeps = null;	
	}

	private void getParentClassforLoad() {
		if (Loader.getListOfDepI() != null) {
			Log.logInfo("ִ�����ɸ��෽��");
			Log.logInfo(" ����ı��Ϊ  "+ this.no);
			Log.logInfo(" ����ĸ���Ϊ  "+ Loader.getListOfDepI().get(this.no)); 
			if (Loader.getListOfDepI().containsKey(this.no)) {
				
				this.parentCName = Loader.getListOfDepI().get(this.no);
			}
			else this.parentCName = null;
		} else
			this.parentCName = null;
	}
	
	private void genAttrDeps() {
		List<String> des = Analyzer.getListOfA().get(this.sCName);
		for (String desA : des) {
			if(!this.attrDeps.containsKey(desA))
				this.attrDeps.put(desA, numOfAttr(this.sCName,desA));			
		}	
	}
	
	private void genMethodDeps() {
		List<String> des = Analyzer.getListOfM().get(this.sCName);
		for (String desM : des) {
			if(!this.methodDeps.containsKey(desM))
				this.methodDeps.put(desM, numOfMethod(this.sCName,desM));			
		}	
	}
	
	private void getParentClass(){
		if(Analyzer.getListOfDepI().get(sCName) != null)
			this.parentCName = Analyzer.getListOfDepI().get(sCName);
		else this.parentCName = null;		
	}
	
	private Integer numOfAttr(String sCName, String desAName){
		int num = 0;
		for(String candidateA:Analyzer.getListOfA().get(sCName)){
			if(candidateA==desAName) num++;			
		}
		return new Integer(num);
	}
	
	private Integer numOfMethod(String sCName, String desMName){
		int num = 0;
		for(String candidateM:Analyzer.getListOfM().get(sCName)){
			if(candidateM==desMName) num++;			
		}
		return new Integer(num);
	}
	
	public Map<String, Integer> getAttrDeps(){
		return this.attrDeps;
	}
	
	public Map<String, Integer> getMethodDeps(){
		return this.methodDeps;
	}
	
	private void setMaxAttrDep() {
		maxAttrDep = 0;
		if (getAttrDeps() != null) {
			Set<Entry<String, Integer>> entrySet = getAttrDeps().entrySet();
			for (Entry<String, Integer> entry : entrySet) {
				if (maxAttrDep < entry.getValue().intValue())
					maxAttrDep = entry.getValue().intValue();
			}
		}
	}
	
	public int getMaxAttrDep(){
		return maxAttrDep;
	}
	
	private void setMaxMethodDep() {
		maxMethodDep = 0;
		if (getMethodDeps() != null) {
			Set<Entry<String, Integer>> entrySet = getMethodDeps().entrySet();
			for (Entry<String, Integer> entry : entrySet) {
				if (maxMethodDep < entry.getValue().intValue())
					maxMethodDep = entry.getValue().intValue();
			}
		}
	}

	public int getMaxMethodDep(){
		return maxMethodDep;
	}
	
	// �ж��Ƿ�������des
	public boolean isDepend(int desNo) {
		String desCName = String.valueOf(desNo);
		boolean depend = false;
		if (attrDeps != null && methodDeps != null) {
			if (attrDeps.containsKey(desCName)
					|| methodDeps.containsKey(desCName))
				depend = true;
			else
				depend = false;
		}
		if (attrDeps == null && methodDeps != null) {
			if (methodDeps.containsKey(desCName))
				depend = true;
			else
				depend = false;
		}
		if (attrDeps != null && methodDeps == null) {
			if (attrDeps.containsKey(desCName))
				depend = true;
			else
				depend = false;
		}
		if (attrDeps == null && methodDeps == null) {
			depend = false;
		}
		return depend;
	}

	// �ж��Ƿ���src����
	public boolean isDependBy(ClassInfo srClass) {
		boolean dependby = false;
		
		//ClassInfo srClass = new ClassInfo(srCName);
		if (srClass.getAttrDeps().containsKey(this.sCName)
				|| srClass.getMethodDeps().containsKey(this.sCName)) {
			dependby = true;
		}
		return dependby;
	}
	
	// �ж��Ƿ�̳���des
	public boolean isInherited(int desNo) {
		if (this.parentCName == String.valueOf(desNo))
			return true;
		else
			return false;
	}
	
	// ͳ��ͬ��des�������������Ŀ
	public int getNumOfAttr(int desNo) {
		String desCName = String.valueOf(desNo);
		if (attrDeps != null) {
			if (attrDeps.containsKey(desCName))
				return attrDeps.get(desCName).intValue();
			else
				return 0;
		} else
			return 0;
	}

	// ͳ��ͬ��des��ķ���������Ŀ
	public int getNumOfMethod(int desNo) {
		String desCName = String.valueOf(desNo);
		if (methodDeps != null) {
			if (methodDeps.containsKey(desCName))
				return methodDeps.get(desCName).intValue();
			else
				return 0;
		} else
			return 0;
	}

	//��ȡ�������
	public String getName() {
		return this.sCName;
	}

	//��ȡ��ı��
	public int getNo(){
		return this.no;
	}

	//��ȡ��ĸ���
	public String getParent() {
		return this.parentCName;
	}
}
