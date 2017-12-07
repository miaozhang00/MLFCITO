package rpmechanism;

import java.util.ArrayList;
import java.util.List;

import multilevelfeedback.ClassInfo;

public class TestProfit {

    static private double[] listOfProfit; // �����������飻
    static private double[] listOfCost; // ���Գɱ����飻
    private int num; // ����ϵͳ�������Ŀ�������С
    private double maxprofit; // ����������
    private double sumOfCost; // �ܵĲ��Գɱ�
    static private boolean[] orderedClass; // ��������ɵ���

    public TestProfit() {
        num = CostMatrix.getNum();
        sumOfCost = 0.0;
        listOfProfit = new double[num];
        listOfCost = new double[num];
        orderedClass = new boolean[num];
        for (int i = 0; i <= num - 1; i++) {
            orderedClass[i] = false;
        }
    }

    public void calProfit(List<String> lastOrderedClass) {
        // ����ϴβ��Ե���Ϊ�գ������ǵ�һ�β��ԣ���Ҫ����������桢���Գɱ�
        // ���򣬸����ϴβ��Ե��࣬�Բ�������Ͳ��Գɱ����ж�̬����
        if (lastOrderedClass.size() == 0)
            calProfitandCost();
        else
            updateProfitandCost(lastOrderedClass);
    }

    private void updateProfitandCost(List<String> lastOrderedClass) {
        // �����ϴβ��Ե��࣬�Բ�������Ͳ��Գɱ����ж�̬����
        for (int i = 0; i <= num - 1; i++) {
            ClassInfo candidate = CostMatrix.getCIByNo(i);
            if (orderedClass[i] == false) {
                for (String lastordered : lastOrderedClass) {
                    int j = CostMatrix.getNoByName(lastordered);
                    ClassInfo cj = CostMatrix.getCIByName(lastordered);
                    // if (candidate.isDepend(lastordered)) {
                    listOfProfit[i] = listOfProfit[i] + CostMatrix.getCostMatrix()[i][j];
                    listOfCost[i] = listOfCost[i] - CostMatrix.getCostMatrix()[i][j];
                    // }
                    if (candidate.isDependBy(cj)) {
                        listOfProfit[i] = listOfProfit[i] - CostMatrix.getCostMatrix()[j][i];
                    }
                }
            }
        }
    }

    private void calProfitandCost() {
        // ��ʼʱ�������������Ͳ��Գɱ�
        for (int i = 0; i <= num - 1; i++) {
            for (int j = 0; j <= num - 1 && j != i; j++) {
                listOfCost[i] = CostMatrix.getCostMatrix()[i][j];
                listOfProfit[i] = CostMatrix.getCostMatrix()[j][i] - CostMatrix.getCostMatrix()[i][j];
            }
        }
    }

    // ��ȡ��������������
    public String getMaxProfit() {
        double maxp = 0.0;
        int cursor = 0;
        for (int i = 1; i <= num - 1; i++) {
            if (orderedClass[i] == false) {
                maxp = listOfProfit[i];
                cursor = i;
            }
        }
        for (int j = cursor + 1; j <= num - 1; j++) {
            if (listOfProfit[j] > maxp && orderedClass[j] == false) {
                maxp = listOfProfit[j];
                cursor = j;
            }
        }
        maxprofit = maxp;
        String maxProfitClass = CostMatrix.getNameByNo(cursor);
        orderedClass[cursor] = true;
        return maxProfitClass;
    }

    // ��ȡ���Գɱ�Ϊ0����
    public List<String> getZeroCost() {
        List<String> listOfZeroCostClass = new ArrayList<String>();
        for (int i = 1; i <= num - 1; i++) {
            if (orderedClass[i] == false && listOfCost[i] == 0)
                listOfZeroCostClass.add(CostMatrix.getNameByNo(i));
            orderedClass[i] = true;
        }
        return listOfZeroCostClass;
    }

    public int numOfOrderedClass() {
        int num = 0;
        for (int i = 1; i <= num - 1; i++) {
            if (orderedClass[i] == true)
                num++;
        }
        return num;
    }

    public static boolean isOrdered(int i) {
        return orderedClass[i];
    }

    public void calCostComplexity(List<String> lastOrderedClass) {
        for (String lastordered : lastOrderedClass) {
            int i = CostMatrix.getNoByName(lastordered);
            sumOfCost = sumOfCost + listOfCost[i];
        }
    }

    public double getSumCost() {
        return sumOfCost;
    }
}
