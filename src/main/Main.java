package main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import log.Log;
import manager.Manager;

public class Main {

	public static void main(String[] args) throws Exception {
		String inputDirectory = System.getProperty("user.dir")+File.separator+"input";
		String outputDirectory = System.getProperty("user.dir")+File.separator+"output";
		// 选择加载模式或分析模式
		//Scanner input = new Scanner(System.in);
		//System.out.println("选择加载模式：请输入 0");
		//System.out.println("选择分析模式：请输入 1");
		//int in = input.nextInt();
		//System.out.println("输入的是" + in);
		int in = 0;//test
		//执行
		if (in == 0)
			loadrun(outputDirectory);
		else{		
			analysisrun(inputDirectory, outputDirectory);
		}		
	}

	private static void analysisrun(String inputDirectory, String outputDirectory)
			throws Exception {
		Manager mgr = new Manager();
		// 如果是分析文件
		mgr.analyze(); // 分析类间关系，获取类间属性复杂度和方法复杂度
		mgr.getCostMatrix(); // 初始化测试代价矩阵
		mgr.initTestPriority(); // 初始化测试收益
		mgr.genCITO(); // 生成类集成测试序列
		mgr.outputResults(); // 输出测试结果
		mgr.outputCost();  //输出总体测试代价
	}

	private static void loadrun(String outputDirectory) {
		Manager mgr = new Manager();
		// 如果是加载文件
		Log.logNormal("  开始执行Load");
		mgr.load(); // 加载描述类间关系的文件，获取类间属性复杂度和方法复杂度
		Log.logNormal("  Load执行完毕");
		Log.logInfo("  加载的类的总数   "+mgr.numOfLoadClass());
		mgr.getCostMatrix(mgr.numOfLoadClass()); // 初始化测试代价矩阵
		mgr.initTestPriority(); // 初始化测试收益
		mgr.genCITO(); // 生成类集成测试序列
		mgr.outputResults(); // 输出测试结果
		mgr.outputCost();  //输出总体测试代价
	}
}
