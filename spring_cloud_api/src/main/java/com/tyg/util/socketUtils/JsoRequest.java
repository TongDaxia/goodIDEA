package com.tyg.util.socketUtils;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


public class JsoRequest {
    private static Logger logger = Logger.getLogger(JsoRequest.class);
    
    public static final short CMD = 1000;

    public static final int SERVER_SEQ = 0;

    public static final int ARG_COUNT = 1;

    private SocketServer socket;

    private static final String ISO_8859_1 = "ISO-8859-1";

    // public static String host = "10.15.107.172";
    // public static int port = 6100;

    public JsoRequest(String host, int port) throws UnknownHostException, IOException {
        socket = new SocketServer(host, port);
        socket.setConnectTimeout(10000);
        socket.setReceiveTimeout(10000);
    }

    // public JsoRequest getInstance() throws UnknownHostException, IOException{
    // if(this==null){
    // return new JsoRequest();
    // }
    // return this;
    // }

    /*
     * 返回一个二维结果集，列头是ColumnInfo jarname:jar包名称，***.jar
     * funname:接口Request名称，gw.sh.func.***Request
     * arg:json字符串，{"objs":"SH600000.stk"
     * ,"fids":"C2,C3,C6,C9,C12,C15,C18","trade_dt":"20120810000000","unit":"2"}
     * charset:GB2312,GBK,UTF-8,ISO_8859_1其中一种，默认GB2312，可为空
     */
    public List<List<List<Object>>> getJsoRequestResult(String jarname, String funname, String arg)
            throws Exception {
    	List<List<List<Object>>> resultList = new ArrayList<List<List<Object>>>();
    	long start = System.currentTimeMillis();
        try {
            socket.connect();
            StringBuffer error = new StringBuffer();
            this.sendDatagram(jarname, funname, arg, error);
            BinaryReadStream2 stream2 = this.receiveDatagram();
            if (check(stream2)) {
                AtomicReference<String> str = new AtomicReference<String>();
                AtomicLong outlen = new AtomicLong();
                stream2.ReadString(str, 0, outlen);
                byte[] stream2Byte = str.get().getBytes(ISO_8859_1);
                resultList = DataParserProxy.parserResult(ByteBuffer.wrap(stream2Byte));
//                char resultType = (char) (stream2Byte[0] & 0x3F);
//                switch (resultType) {
//                case '5':
//                    long s = System.currentTimeMillis();
//                    resultList = PackUtils.restoreResultSet(stream2Byte, 0, charset);
//                    error.append("TIME CONSUMING：" + (System.currentTimeMillis() - s)).append("/n");
//                    break;
//                default:
//                    break;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            socket.closeForce();
        } 
        socket.close();
        dolog(start, socket.socketAddress.toString(), jarname, funname, arg);
        return resultList;
    }

    public void sendDatagram(String filename, String funname, String arg, StringBuffer error) throws IOException {
        BinaryWriteStream3 writestream = new BinaryWriteStream3();
        writestream.Write(CMD);
        writestream.Write(SERVER_SEQ);
        writestream.Write(StringUtils.trim(filename), StringUtils.trim(filename).length());
        error.append(" JSONAME: ").append(StringUtils.trim(filename)).append("\n");
        writestream.Write(StringUtils.trim(funname), StringUtils.trim(funname).length());
        error.append(" FUNNAME: ").append(StringUtils.trim(funname)).append("\n");
        if (!StringUtils.isBlank(arg)) {
			String param = StringUtils.trim(arg);
			String[] paramArray = param.split(";");
			if(paramArray.length==1){
				writestream.Write(ARG_COUNT);
                param = new String(param.getBytes("UTF-8"), "ISO-8859-1");
				writestream.Write(param, param.length());
			}
			else{
				writestream.Write(paramArray.length);
				for(String pm :paramArray){
                    param = new String(pm.getBytes("UTF-8"), "ISO-8859-1");
					writestream.Write(param, param.length());
				}
			}
			error.append(" ARG: ").append(StringUtils.trim(arg)).append("\n");
        } else {
            error.append(" NOARGS ").append("\n");
            writestream.Write(0);
        }
        writestream.Write(StringUtils.trim("app"), StringUtils.trim("app").length()); // 占位用
		writestream.Write(StringUtils.trim("123456"), StringUtils.trim("123456").length());
        writestream.Flush();
        socket.send(writestream.getData());
        writestream.clear();
        writestream = null;
    }

    public BinaryReadStream2 receiveDatagram() throws IOException {
        AtomicReference<byte[]> bytes = new AtomicReference<byte[]>();
        int len = socket.read(bytes, 4);
        byte[] bs = bytes.get();
        len = ByteUtils.ntohl(ArrayUtils.subarray(bs, 0, 4));
        logger.info("tota data length:" + len);
        byte[] bs2 = ByteUtils.intToBytes(len);
        AtomicReference<byte[]> data = new AtomicReference<byte[]>();
        byte[] dataall = ArrayUtils.addAll(bs2, new byte[len - bs2.length]);
        data.set(dataall);
        socket.read(data, 4, len - 4);
        byte[] databyte = data.get();
        return new BinaryReadStream2(databyte);
    }

    boolean check(BinaryReadStream2 stream2) throws Exception {
        AtomicReference<Short> s = new AtomicReference<Short>();
        if (!stream2.ReadShort(s)) {
            throw new Exception("read cmd error");
        }
        AtomicInteger seq = new AtomicInteger();
        if (!stream2.ReadInt(seq)) {
            throw new Exception("read seq error");
        }
        AtomicInteger ret = new AtomicInteger();
        if (!stream2.ReadInt(ret)) {
            throw new Exception("read ret error");
        }
        if (ret.get() != 0) {
            switch (ret.get()) {
            case 1:
                throw new Exception(" no idle process ");
            case 2:
                throw new Exception(" script error ");
            case 3:
                throw new Exception(" script timeout ");
            case 4:
                throw new Exception(" script auto end ");
            default:
                throw new Exception(" unknown error RET = " + s.get());
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
//        	String[] serverList = {
//            		"114.80.137.195",
//            		"114.80.137.197",
//            		"114.80.170.177",
//            		"114.80.170.179",
//            		"114.80.170.180",
//            		"114.80.170.171",
//            		"114.80.170.174",
//            		"114.80.231.2",
//            		"114.80.231.3",
//            		"114.80.231.9",
//            		"114.80.231.11",
//            		"114.80.231.12",
////            		"222.73.172.150",
////            		"222.73.172.151",
//            		"123.129.231.81",
//            		"123.129.231.82",
//            		"123.129.231.88",
//            		"123.129.231.90",
//            		"123.129.231.91",
//            		"123.129.231.86",
//            		"123.129.231.89"
//            };
        	
//        	String[] serverList = {
//            		"10.15.107.172",
//            		"10.15.107.163",
//            		"10.15.107.165",
//            		"10.15.87.27",
//            		"10.15.89.158",
//            };
        	
//        	for(String str : serverList){
//        		logger.info("jso server:" + str);
//        		
//        		JsoRequest jso = new JsoRequest(str, 7000);
//        		List<List<Object>> result = jso.getJsoRequestSingleResult(jarname, funname, arg, charset);
//                JsoConsole.debugResult(result);
//        	}
        	
//			String jarname = "javadataproxy.jar";
//			String funname = "gw/UIMain";
			//String arg = "get;/bond/treasuryfutureScreen/getDeliverableBondsResult.dzh?contractCode=SFTF1506.cmdty";
			//String arg = "get;/stock/html5_orr2/solr_report/getReportDataForTargetSearch112.mod?gpdm=000001&page=1&rqState=0&pagesize=5&Accept=json";
//			String arg = "get;/stock/Stock/mdi_106/executivesaggregate/getSHMAggregate.mod?rangeList=100096&sdate=20150303&edate=20150403&shType=0&cType=0&stockcode=";
			
//            String jarname = "libNews2.so";
//            String funname = "NewsContents";
//            String arg = "408401349;1;3,4,5,8,11,13,15,16,18,28";
//          String jarname = "libbigfile.so";
//          String funname = "fun_bigfile";
//          String jarname = "jso-ui-bigfile.jar";
//          String funname = "gw.sh.func.BigfileRequest";
//          String arg = "191;SZ300003.stk;65;20150908145000;20150908150000;";
        	
//			String jarname = "jso-all-treequery.jar";
//			String funname = "gw.sh.func.QueryLeafNodeRequestSO";
//			String arg = "41522;100097;0;1;;;";
        	
//          String jarname = "libiqquery.so";
//          String funname = "fun_iq_query";
//          String arg = "6332,6341;0;0;1;call SP_KY_HGHYBBTJ('73991','月报','2009-03-06','2015-03-05')";
        	
//        	String jarname = "jso-ui-zhpnews.jar";
//        	String funname = "gw.sh.func.MyOptionalStocksNewsRequest";
//        	String arg = "{reload:1}";
        	//String arg = "{fids:'C1,C2,C3,C4,C16,C31,CC98,CC99,C8',dcategory:1,bdate:2014906000000,edate:20150305235959,sort:'C16:-1',skip:0,amount:50,menuid:'000001007'}";
        	
//            String jarname = "jso-ui-hbzhp.jar";
//            String funname = "gw.sh.func.HbzhpRequest";
//        	String arg = "";
        	
        	String jarname = "jso-all-treequery.jar";
        	String funname = "gw.sh.func.QueryChildTreeRequest";
      		String arg = "{'did':'41522','node':'112310','returntype':'1','querydepth':-1,'nodetype':0,'result':0, 'encoding':'GB2312'}";
        	

            JsoRequest jso = new JsoRequest("10.15.97.38", 7000);
//            JsoRequest jso = new JsoRequest("10.10.55.41", 7000);
        	List<List<List<Object>>> result = jso.getJsoRequestResult(jarname, funname, arg);
            for (List<List<Object>> resultin : result) {
                JsoConsole.debugResult(resultin);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("jso socket error!");
        }
    }
    
    public static JSONObject getJSONObjectFromresult(List<List<List<Object>>> result) throws Exception {
    	JSONObject data = new JSONObject();
    	if(result == null) {
    		return data;
    	}
    	for(int i =0;i<result.size();i++) {
			JSONArray dataresult = new JSONArray();
			if(result.get(i) != null) {
				for(int j =0;j<result.get(i).size();j++) {
					JSONObject resultJsonj = new JSONObject();
					if(result.get(i).get(j)!=null) {
						String val = result.get(i).get(j).toString();
						val = val.substring(0,val.length()-1);
						val = val.substring(1,val.length());
						if(j == 0) {
							resultJsonj.put("head", val);
						}else {
							
							resultJsonj.put("row", val);
						}
					}
					dataresult.add(resultJsonj);
				}
			}
			logger.info(dataresult);
			data.put("return"+ i, dataresult);
		}
    	return data;
    }
    
    public JSONArray getJSONArrayFromResult(List<List<List<Object>>> result, List<String> itcode2List) {
    	JSONArray data = new JSONArray();
    	if(result == null) {
    		return data;
    	}
    	for(int i =0;i<result.size();i++) {
			if(result.get(i) != null) {
				for(int j =0;j<result.get(i).size();j++) {
					JSONObject resultJsonj = new JSONObject();
					if(result.get(i).get(j)!=null) {
						String val = result.get(i).get(j).toString();
						val = val.substring(0,val.length()-1);
						val = val.substring(1,val.length());
						if(j == 0) {
							resultJsonj.put("head", val);
						}else {
							JSONObject obj = JSONObject.fromObject(val).getJSONArray("pathDetail").getJSONObject(0);
							data.add(obj);
							itcode2List.add(obj.getString("code"));
						}
					}
				}
			}
		}
    	return data;
    }
    
    private void dolog(long start, String url, String jarName, String funcName, String arg) {
    	JSONObject logJson = new JSONObject();
		logJson.put("cost", System.currentTimeMillis()-start);
		logJson.put("url", url);
		logJson.put("jarName", jarName);
		logJson.put("funcName", funcName);
		logJson.put("param", arg);
		

	}
}
