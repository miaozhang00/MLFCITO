package multilevelfeedback;

import java.util.ArrayList;
import java.util.List;

import rpmechanism.TestPriority;
import rpmechanism.TestProfit;

public class MultiLevelFeedback {
	
	private int num; // 待测系统的类的数目
	private TestProfit testProfit; // 测试收益类，用于计算更新测试收益
	private TestPriority testPriority; //测试优先级

	public MultiLevelFeedback() {		
		this.testProfit = new TestProfit();
		this.testPriority = null;
	}

	public void process(TestPriority testPriority) {
		// 计算每个类的测试收益
		// 选择类放入类集成测试序列（测试收益最大、测试成本为0）
		// 更改测试收益值、测试优先级、测试成本
		// 重复上述过程，直到排序完毕
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
	
	public double getSumCost(){
		return testProfit.getSumCost();
	}

}
