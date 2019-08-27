package com.tyg.util.socketUtils;

import java.util.List;




public class JsoConsole {
	/**
	 * 输出二维结果集仅在控制台打印，支持多个
	 */
	public static void debugResult(List<List<Object>>... resultLists) {
		//int n=0;
		for(List<List<Object>> resultList:resultLists){
			//System.out.println("-----------result["+(n)+"]-------------");n=n++;
			for (int i = 0, len = resultList.size(); i < len; i++) {
				for (int j = 0, jlen = resultList.get(i).size(); j < jlen; j++) {
					if (i == 0) {
						Object obj = resultList.get(0).get(j);
						if(obj instanceof ColumnInfo){
							System.out.print(((ColumnInfo) (obj)).getColumnName());
						}else{
							System.out.print(obj);
						}
					} else {
						System.out.print(resultList.get(i).get(j));
					}
					System.out.print("\t");
				}
				System.out.print("\n");
			}
		}
	}
}


