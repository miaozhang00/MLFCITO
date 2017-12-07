package analyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootOption;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.internal.JDynamicInvokeExpr;
import soot.jimple.internal.JInterfaceInvokeExpr;
import soot.jimple.internal.JSpecialInvokeExpr;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JVirtualInvokeExpr;

public class Analyzer {

    static private Collection<SootClass> listOfClasses; // 待测类的列表
    static private Map<String, String> listOfDepI; // 继承关系列表
    static private Map<String, List<String>> listOfM; // 方法依赖列表
    static private Map<String, List<String>> listOfA; // 属性依赖列表
    static private Map<String, String> listOfMI; // 方法调用列表（用于方法三）

    public void analysis() throws Exception {

        loadClassAndAnalysis(); // 加载类并分析
        listOfClasses = Scene.v().getApplicationClasses(); // 获取所有的待测类
        findDepI(listOfClasses);
        for (SootClass sClass : listOfClasses) {
            analysisMethod(sClass);
            analysisAttr(sClass);
        }

    }

    public static void loadClassAndAnalysis() throws Exception {
        SootOption.setOptions();
        try {
            Scene.v().loadNecessaryClasses();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("class can not be loaded");
        }
    }

    // 查找所有的继承关系
    public Map<String, String> findDepI(Collection<SootClass> scs) {
        for (SootClass childClass : scs) {
            SootClass superClass = childClass.getSuperclass();
            if (superClass.isApplicationClass()) {
                listOfDepI.put(childClass.getName(), superClass.getName());
            }
        }
        return listOfDepI;
    }

    // 分析方法依赖
    private void analysisMethod(SootClass sClass) {
        // 保存所有方法的invokeM
        List<String> allInvokeM = new ArrayList<String>();
        // 获取sClass的所有方法
        Collection<SootMethod> listOfm = sClass.getMethods();
        // 分析sClass的每个方法是否调用了其他类的方法
        for (SootMethod m : listOfm) {
            if (!m.isConcrete()) {
                allInvokeM.addAll(analysismethod(sClass, m));
            }
        }
        listOfM.put(sClass.getName(), allInvokeM);
    }

    // 分析属性依赖
    private void analysisAttr(SootClass sClass) {
        // 保存所有的AccessA
        List<String> allAccessA = new ArrayList<String>();
        // 增加Field中的属性依赖
        allAccessA.addAll(analysisField(sClass));
        // 增加参数调用的属性依赖
        allAccessA.addAll(analysisPara(sClass));
        // 增加返回值调用的属性依赖
        allAccessA.addAll(analysisRet(sClass));
        listOfA.put(sClass.getName(), allAccessA);
    }

    // 分析类的某一方法对其他类方法的依赖
    private List<String> analysismethod(SootClass sClass, SootMethod sMethod) {
        // invokeM存储sMethod方法依赖的类的列表
        List<String> invokeM = new ArrayList<String>();
        Body body = sMethod.retrieveActiveBody();
        Collection<Unit> units = body.getUnits();
        for (Unit unit : units) {
            for (ValueBox valueBox : unit.getUseBoxes()) {
                Value useValue = valueBox.getValue();
                // 判断useValue是不是调用语句
                switch (isInvokeExpr(useValue)) {
                case 1:
                    invokeM.addAll(analysisVirtualInvoke(useValue, sClass, sMethod));
                case 2:
                    invokeM.addAll(analysisInterInvoke(useValue, sClass, sMethod));
                case 3:
                    invokeM.addAll(analysisDynamInvoke(useValue, sClass, sMethod));
                case 4:
                    invokeM.addAll(analysisSpecInvoke(useValue, sClass, sMethod));
                case 5:
                    invokeM.addAll(analysisStatInvoke(useValue, sClass, sMethod));
                default:
                    break;
                }
            }
        }
        return invokeM;
    }

    // 判断useValue是哪种调用语句
    private int isInvokeExpr(Value useValue) {
        int no = 0;
        if (useValue instanceof JVirtualInvokeExpr)
            no = 1;
        if (useValue instanceof JInterfaceInvokeExpr)
            no = 2;
        if (useValue instanceof JDynamicInvokeExpr)
            no = 3;
        if (useValue instanceof JSpecialInvokeExpr)
            no = 4;
        if (useValue instanceof JStaticInvokeExpr)
            no = 5;
        return no;
    }

    // 分析VirtualInvoke，返回virtualInvoke调用信息列表——vInvokeM
    private List<String> analysisVirtualInvoke(Value useValue, SootClass sClass, SootMethod sMethod) {
        List<String> vInvokeM = new ArrayList<String>();
        JVirtualInvokeExpr jVirtualInvokeExpr = (JVirtualInvokeExpr) useValue;
        for (ValueBox vInvokeValue : jVirtualInvokeExpr.getUseBoxes()) {
            Value vInvoke = vInvokeValue.getValue();
            String vInvokeClassName = vInvoke.getType().toString();
            if (getIndex(vInvokeClassName) != listOfClasses.size() && vInvokeClassName != sClass.getName()) {
                if (listOfDepI.get(sClass.getName()) != vInvokeClassName
                        && listOfDepI.get(vInvokeClassName) != sClass.getName()) {
                    vInvokeM.add(vInvokeClassName);
                    listOfMI.put(sMethod.getDeclaration(), jVirtualInvokeExpr.getMethod().toString());
                }
            }
        }
        return vInvokeM;
    }

    // 分析InterfaceInvoke，返回InterfaceInvoke调用信息列表——iInvokeM
    private List<String> analysisInterInvoke(Value useValue, SootClass sClass, SootMethod sMethod) {
        List<String> iInvokeM = new ArrayList<String>();
        JInterfaceInvokeExpr jInterInvokeExpr = (JInterfaceInvokeExpr) useValue;
        for (ValueBox iInvokeValue : jInterInvokeExpr.getUseBoxes()) {
            Value iInvoke = iInvokeValue.getValue();
            String iInvokeClassName = iInvoke.getType().toString();
            if (getIndex(iInvokeClassName) != listOfClasses.size() && iInvokeClassName != sClass.getName()) {
                if (listOfDepI.get(sClass.getName()) != iInvokeClassName
                        && listOfDepI.get(iInvokeClassName) != sClass.getName()) {
                    iInvokeM.add(iInvokeClassName);
                    listOfMI.put(sMethod.getDeclaration(), jInterInvokeExpr.getMethod().toString());
                }
            }
        }
        return iInvokeM;
    }

    // 分析DynamicInvoke，返回dynamicInvoke调用信息列表——dInvokeM
    private List<String> analysisDynamInvoke(Value useValue, SootClass sClass, SootMethod sMethod) {
        List<String> dInvokeM = new ArrayList<String>();
        JDynamicInvokeExpr jDynamInvokeExpr = (JDynamicInvokeExpr) useValue;
        for (ValueBox dInvokeValue : jDynamInvokeExpr.getUseBoxes()) {
            Value dInvoke = dInvokeValue.getValue();
            String dInvokeClassName = dInvoke.getType().toString();
            if (getIndex(dInvokeClassName) != listOfClasses.size() && dInvokeClassName != sClass.getName()) {
                if (listOfDepI.get(sClass.getName()) != dInvokeClassName
                        && listOfDepI.get(dInvokeClassName) != sClass.getName()) {
                    dInvokeM.add(dInvokeClassName);
                    listOfMI.put(sMethod.getDeclaration(), jDynamInvokeExpr.getMethod().toString());
                }
            }
        }
        return dInvokeM;
    }

    // 分析SpecialInvoke，返回SpecialInvoke调用列表——sInvokeM
    private List<String> analysisSpecInvoke(Value useValue, SootClass sClass, SootMethod sMethod) {
        List<String> sInvokeM = new ArrayList<String>();
        JSpecialInvokeExpr jSpecInvokeExpr = (JSpecialInvokeExpr) useValue;
        for (ValueBox sInvokeValue : jSpecInvokeExpr.getUseBoxes()) {
            Value sInvoke = sInvokeValue.getValue();
            String sInvokeClassName = sInvoke.getType().toString();
            if (getIndex(sInvokeClassName) != listOfClasses.size() && sInvokeClassName != sClass.getName()) {
                if (listOfDepI.get(sClass.getName()) != sInvokeClassName
                        && listOfDepI.get(sInvokeClassName) != sClass.getName()) {
                    sInvokeM.add(sInvokeClassName);
                    listOfMI.put(sMethod.getDeclaration(), jSpecInvokeExpr.getMethod().toString());
                }
            }
        }
        return sInvokeM;
    }

    // 分析StaticInvoke，返回staticInvoke调用信息列表——stInvokeM
    private List<String> analysisStatInvoke(Value useValue, SootClass sClass, SootMethod sMethod) {
        List<String> stInvokeM = new ArrayList<String>();
        JStaticInvokeExpr jStatInvokeExpr = (JStaticInvokeExpr) useValue;
        for (ValueBox stInvokeValue : jStatInvokeExpr.getUseBoxes()) {
            Value stInvoke = stInvokeValue.getValue();
            String stInvokeClassName = stInvoke.getType().toString();
            if (getIndex(stInvokeClassName) != listOfClasses.size() && stInvokeClassName != sClass.getName()) {
                if (listOfDepI.get(sClass.getName()) != stInvokeClassName
                        && listOfDepI.get(stInvokeClassName) != sClass.getName()) {
                    stInvokeM.add(stInvokeClassName);
                    listOfMI.put(sMethod.getDeclaration(), jStatInvokeExpr.getMethod().toString());
                }
            }
        }
        return stInvokeM;
    }

    private List<String> analysisField(SootClass sClass) {
        List<String> accessField = new ArrayList<String>();
        Collection<SootField> listOfa = sClass.getFields();
        for (SootField sField : listOfa) {
            if (sField != null && sField.getType() instanceof RefType) {
                RefType type = (RefType) sField.getType();
                if (type.getSootClass().isApplicationClass() && type.toString() != sClass.getName()
                        && listOfDepI.get(sClass.getName()) != type.toString()
                        && listOfDepI.get(type.toString()) != sClass.getName()) {
                    accessField.add(type.getSootClass().getName());
                }
            }
        }
        return accessField;
    }

    private List<String> analysisPara(SootClass sClass) {
        List<String> accessPara = new ArrayList<String>();
        // 获取sClass的所有方法
        Collection<SootMethod> listOfm = sClass.getMethods();
        // 分析sClass的每个方法的参数调用
        for (SootMethod m : listOfm) {
            if (!m.isConcrete()) {
                // 分析方法m中的参数调用
                accessPara.addAll(analysisParainM(sClass, m));
            }
        }
        return accessPara;
    }

    private List<String> analysisRet(SootClass sClass) {
        List<String> accessRet = new ArrayList<String>();
        // 获取sClass的所有方法
        Collection<SootMethod> listOfm = sClass.getMethods();
        // 分析sClass的每个方法的返回值调用
        for (SootMethod m : listOfm) {
            Type retType = m.getReturnType();
            for (SootClass sc : listOfClasses) {
                if (retType.toString() == sc.getName() && retType.toString() != sClass.getName()
                        && listOfDepI.get(sClass.getName()) != retType.toString()
                        && listOfDepI.get(retType.toString()) != sClass.getName())
                    accessRet.add(retType.toString());
            }
        }
        return accessRet;
    }

    private List<String> analysisParainM(SootClass sClass, SootMethod sMethod) {
        List<String> accessPinM = new ArrayList<String>();
        Body body = sMethod.retrieveActiveBody();
        List<?> types = sMethod.getParameterTypes();
        int size_args = types.size();
        for (int i = 0; i < size_args; i++) {
            Object Paratype = types.get(i);
            for (SootClass sc : listOfClasses) {
                if (Paratype.toString() == sc.getName() && Paratype.toString() != sClass.getName()
                        && listOfDepI.get(sClass.getName()) != Paratype.toString()
                        && listOfDepI.get(Paratype.toString()) != sClass.getName())
                    accessPinM.add(Paratype.toString());
                for (SootField sf : sc.getFields()) {
                    if (Paratype.toString() == sf.getName())
                        accessPinM.add(sc.getName());
                }
            }
        }
        return accessPinM;
    }

    public static SootClass[] getListOfClass() {
        SootClass[] classes = new SootClass[listOfClasses.size()];
        int i = 0;
        for (SootClass sc : listOfClasses) {
            classes[i] = sc;
            i++;
        }
        return classes;
    }

    public static int getIndex(String cname) {
        SootClass[] classes = getListOfClass();
        for (int i = 0; i <= classes.length - 1; i++) {
            if (classes[i].getName() == cname)
                return i;
        }
        return classes.length;
    }

    public static Map<String, String> getListOfDepI() {
        return listOfDepI;
    }

    public static Map<String, List<String>> getListOfM() {
        return listOfM;
    }

    public static Map<String, List<String>> getListOfA() {
        return listOfA;
    }

    public Map<String, String> getListOfMI() {
        return listOfMI;
    };

    // 1.测试analysis()
    public void TC_analysis() {
        // 统计待测系统共有多少个类
        System.out.println("该待测系统共有" + listOfClasses.size() + "个类");
        // 输出待测类的名称
        for (SootClass sootClass : Scene.v().getApplicationClasses()) {
            String className = sootClass.getName();
            System.out.println("分析类" + className);
        }
    }

    // TestCode:输出继承关系列表listOfDepI
    public static void TC_DepI(Map<String, String> DepI) {
        Set<String> keySet = DepI.keySet();
        for (String childCName : keySet) {
            String superCName = DepI.get(childCName);
            System.out.println("子代" + childCName + "继承于" + superCName);
        }
        System.out.println("共计" + DepI.size() + "个继承关系");
    }

}
