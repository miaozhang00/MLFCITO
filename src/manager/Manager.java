package manager;

import multilevelfeedback.MultiLevelFeedback;
import analyzer.Analyzer;
import analyzer.Loader;
import rpmechanism.TestPriority;
import rpmechanism.CostMatrix;

public class Manager {

    Loader loader; // ���������ĵ���
    Analyzer analyzer; // ���������
    CostMatrix costMatrix; // ���Գɱ�����
    TestPriority testPriority; // �������ȼ��б�
    MultiLevelFeedback multiLevelFeedback; // �༶����

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
