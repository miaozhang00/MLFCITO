package rpmechanism;

import java.util.List;

public class TestPriority {

	private int[] testPriority; // 测试优先级数组
	private int num; // 测试优先级数组大小

	public TestPriority() {
		num = CostMatrix.getNum();
		testPriority = new int[num];
	}

	public void initTestPriority() {
		for (int i = 0; i <= num - 1; i++) {
			testPriority[i] = 0;
		}
	}

	public int num(){
		return this.testPriority.length;
	}
	
	public void updateTestPriority(List<String> lastOrderedClass) {
		for (int i = 0; i <= num - 1; i++) {
			for (String lastordered : lastOrderedClass) {
				if (CostMatrix.getNameByNo(i) != lastordered
						&& !TestProfit.isOrdered(i)) {
					testPriority[i] = testPriority[i] + 1;
				}
			}
		}
	}

	public void output() {
		// TODO Auto-generated method stub
		int profit = 0;
		while(num != 0){
			System.out.println("测试优先级为"+(profit+1));
			for(int i = 0; i <= num - 1; i++){
				if(testPriority[i] == profit){
					System.out.print(CostMatrix.getNameByNo(i)+" ");
				}
			}
			profit++;
			num--;
		}
	}

}
