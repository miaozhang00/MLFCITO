package rpmechanism;

import java.util.ArrayList;
import java.util.List;

import multilevelfeedback.ClassInfo;

public class TestProfit {

    static private double[] listOfProfit; // 测试收益数组；
    static private double[] listOfCost; // 测试成本数组；
    private int num; // 待测系统的类的数目，数组大小
    private double maxprofit; // 最大测试收益
    private double sumOfCost; // 总的测试成本
    static private boolean[] orderedClass; // 已排序完成的类

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
        // 如果上次测试的类为空，表明是第一次测试，需要计算测试收益、测试成本
        // 否则，根据上次测试的类，对测试收益和测试成本进行动态调整
        if (lastOrderedClass.size() == 0)
            calProfitandCost();
        else
            updateProfitandCost(lastOrderedClass);
    }

    private void updateProfitandCost(List<String> lastOrderedClass) {
        // 根据上次测试的类，对测试收益和测试成本进行动态调整
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
        // 初始时，计算测试收益和测试成本
        for (int i = 0; i <= num - 1; i++) {
            for (int j = 0; j <= num - 1 && j != i; j++) {
                listOfCost[i] = CostMatrix.getCostMatrix()[i][j];
                listOfProfit[i] = CostMatrix.getCostMatrix()[j][i] - CostMatrix.getCostMatrix()[i][j];
            }
        }
    }

    // 获取测试收益最大的类
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

    // 获取测试成本为0的类
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
