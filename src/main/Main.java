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
		// ѡ�����ģʽ�����ģʽ
		//Scanner input = new Scanner(System.in);
		//System.out.println("ѡ�����ģʽ�������� 0");
		//System.out.println("ѡ�����ģʽ�������� 1");
		//int in = input.nextInt();
		//System.out.println("�������" + in);
		int in = 0;//test
		//ִ��
		if (in == 0)
			loadrun(outputDirectory);
		else{		
			analysisrun(inputDirectory, outputDirectory);
		}		
	}

	private static void analysisrun(String inputDirectory, String outputDirectory)
			throws Exception {
		Manager mgr = new Manager();
		// ����Ƿ����ļ�
		mgr.analyze(); // ��������ϵ����ȡ������Ը��ӶȺͷ������Ӷ�
		mgr.getCostMatrix(); // ��ʼ�����Դ��۾���
		mgr.initTestPriority(); // ��ʼ����������
		mgr.genCITO(); // �����༯�ɲ�������
		mgr.outputResults(); // ������Խ��
		mgr.outputCost();  //���������Դ���
	}

	private static void loadrun(String outputDirectory) {
		Manager mgr = new Manager();
		// ����Ǽ����ļ�
		Log.logNormal("  ��ʼִ��Load");
		mgr.load(); // ������������ϵ���ļ�����ȡ������Ը��ӶȺͷ������Ӷ�
		Log.logNormal("  Loadִ�����");
		Log.logInfo("  ���ص��������   "+mgr.numOfLoadClass());
		mgr.getCostMatrix(mgr.numOfLoadClass()); // ��ʼ�����Դ��۾���
		mgr.initTestPriority(); // ��ʼ����������
		mgr.genCITO(); // �����༯�ɲ�������
		mgr.outputResults(); // ������Խ��
		mgr.outputCost();  //���������Դ���
	}
}
