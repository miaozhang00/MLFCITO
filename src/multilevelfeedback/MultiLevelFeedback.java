package multilevelfeedback;

import java.util.ArrayList;
import java.util.List;

import rpmechanism.TestPriority;
import rpmechanism.TestProfit;

public class MultiLevelFeedback {

    private int num; // ����ϵͳ�������Ŀ
    private TestProfit testProfit; // ���������࣬���ڼ�����²�������
    private TestPriority testPriority; // �������ȼ�

    public MultiLevelFeedback() {
        this.testProfit = new TestProfit();
        this.testPriority = null;
    }

    public void process(TestPriority testPriority) {
        // ����ÿ����Ĳ�������
        // ѡ��������༯�ɲ������У�����������󡢲��Գɱ�Ϊ0��
        // ���Ĳ�������ֵ���������ȼ������Գɱ�
        // �ظ��������̣�ֱ���������
        List<String> lastOrderedClass = new ArrayList<String>();
        this.testPriority = testPriority;
        this.num = testPriority.num();
        while (testProfit.numOfOrderedClass() != num) {
            testProfit.calProfit(lastOrderedClass);
            lastOrderedClass = choice();
            updateTestPriority(lastOrderedClass);
            calCostComplexity(lastOrderedClass);
        }
    }

    private List<String> choice() {
        List<String> choice = new ArrayList<String>();
        if (testProfit.getMaxProfit() != null) {
            choice.add(0, testProfit.getMaxProfit());
        }
        if (testProfit.getZeroCost() != null) {
            choice.addAll(testProfit.getZeroCost());
        }
        return choice;
    }

    private void updateTestPriority(List<String> lastOrderedClass) {
        testPriority.updateTestPriority(lastOrderedClass);
    }

    private void calCostComplexity(List<String> lastOrderedClass) {
        testProfit.calCostComplexity(lastOrderedClass);

    }

    public double getSumCost() {
        return testProfit.getSumCost();
    }

}
