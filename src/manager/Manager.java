package manager;

import multilevelfeedback.MultiLevelFeedback;
import analyzer.Analyzer;
import analyzer.Loader;
import rpmechanism.TestPriority;
import rpmechanism.CostMatrix;

public class Manager {

    Loader loader; // 加载输入文档类
    Analyzer analyzer; // 程序分析类
    CostMatrix costMatrix; // 测试成本矩阵
    TestPriority testPriority; // 测试优先级列表
    MultiLevelFeedback multiLevelFeedback; // 多级反馈

    public Manager() {
        this.loader = new Loader();
        this.analyzer = new Analyzer();
    }

    public void load() {
        loader.load();
    }

    public int numOfLoadClass() {
        return loader.getNumOfClasses();
    }

    public void analyze() throws Exception {
        analyzer.analysis();
    }

    public void getCostMatrix() {
        this.costMatrix = new CostMatrix();
        this.costMatrix.initCostMatrix();
    }

    public void getCostMatrix(int numOfLoadClass) {
        this.costMatrix = new CostMatrix(numOfLoadClass);
        this.costMatrix.initCostMatrix();
    }

    public void initTestPriority() {
        this.testPriority = new TestPriority();
        this.testPriority.initTestPriority();
    }

    public void genCITO() {
        this.multiLevelFeedback = new MultiLevelFeedback();
        multiLevelFeedback.process(testPriority);
    }

    public void outputResults() {
        testPriority.output();
    }

    public double outputCost() {
        System.out.println(multiLevelFeedback.getSumCost());
        return multiLevelFeedback.getSumCost();
    }
}
