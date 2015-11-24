package com.eastsoft.testframe.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eastsoft.testframe.server.DisplayResultActor;

/**
 * <p>
 * 描述: 生成控制台表格
 * </p>
 * <p/>
 * <p>
 * Create Date: 2009-9-18 12:52:01
 * <p>
 *
 * @author betafox
 * @version 1.0
 */
public class ConsoleTable {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(ConsoleTable.class);
	private List<List> rows = new ArrayList<List>();

	private int colum;

	private int[] columLen;

	private static int margin = 2;

	private boolean printHeader = false;

	public ConsoleTable(int colum, boolean printHeader) {
		this.printHeader = printHeader;
		this.colum = colum;
		this.columLen = new int[colum];
	}

	public void appendRow() {
		List row = new ArrayList(colum);
		rows.add(row);
	}

	public ConsoleTable appendColum(Object value) {
		if (value == null) {
			value = "NULL";
		}
		List row = rows.get(rows.size() - 1);
		row.add(value);
		int len=0;
		try {
			len = value.toString().getBytes("GBK").length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (columLen[row.size() - 1] < len)
			columLen[row.size() - 1] = len;
		return this;
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();

		int sumlen = 0;
		for (int len : columLen) {
			sumlen += len;
		}
		if (printHeader)
			buf.append("|")
					.append(printChar('+', sumlen + margin * 4 * colum
							+ (colum - 1))).append("|\n");
		else
			buf.append("|")
					.append(printChar('+', sumlen + margin * 4 * colum
							+ (colum - 1))).append("|\n");
		for (int ii = 0; ii < rows.size(); ii++) {
			List row = rows.get(ii);
			for (int i = 0; i < colum; i++) {
				String o = "";
				if (i < row.size())
					o = row.get(i).toString();
				buf.append('|').append(printChar(' ', margin * 2)).append(o)
						.append(printChar(' ', margin * 2));
//				buf.append(printChar(' ', columLen[i] - o.getBytes().length
//						+ margin));
			}
			buf.append("|\n");
			if (printHeader && ii == 0)
				buf.append("|")
						.append(printChar('+', sumlen + margin * 4 * colum
								+ (colum - 1))).append("|\n");
			else
				buf.append("|")
						.append(printChar('+', sumlen + margin * 4 * colum
								+ (colum - 1))).append("|\n");
		}
		return buf.toString();
	}

	private String printChar(char c, int len) {
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < len; i++) {
			buf.append(c);
		}
		return buf.toString();
	}

	public static String printResult(String [] array){
		if(array.length<8){
			System.out.println("-------printResult:data error------");
			return "";
		}
		ConsoleTable t = new ConsoleTable(8, true);
		t.appendRow();
		t.appendColum("测试总数").appendColum("总耗时")
				.appendColum("最大耗时").appendColum("最小耗时").appendColum("平均时间").appendColum("成功数").appendColum("失败数").appendColum("成功率");

		t.appendRow();
		t.appendColum(array[0]).appendColum(array[1]).appendColum(array[2])
				.appendColum(array[3]).appendColum(array[4]).appendColum(array[5]).appendColum(array[6]).appendColum(array[7]);
		return t.toString();
	}

	public static void main(String[] args) {
		ConsoleTable t = new ConsoleTable(4, true);
		t.appendRow();
		t.appendColum("序号").appendColum("姓名").appendColum("性别")
				.appendColum("年龄");

		t.appendRow();
		t.appendColum("1").appendColum("张三").appendColum("男")
				.appendColum("11");

		t.appendRow();
		t.appendColum("22").appendColum("zhang强");
		System.out.println(t.toString());
	}

}
