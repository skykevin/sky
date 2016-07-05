
package cn.healthytime.weixin.api.device;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.net.HttpRequest;

import com.google.gson.Gson;
import com.sys.util.LogPrintUtil;

import cn.healthytime.weixin.api.bean.device.qrcode.DeviceCode;
import cn.healthytime.weixin.api.bean.device.qrcode.RequestCreateQRcode;
import cn.healthytime.weixin.api.bean.device.qrcode.ResponseDeviceList;
import cn.vsapp.consult.service.weixin.QrCodeSample;

/**
 * @ClassName: CreateQrcodeService
 * @Description: 获取设备二维码接口
 * @author jxzhong
 * @version 1.0
 * @date 2016年2月1日 下午3:36:54
 */
public class CreateQrcodeService {

	private final static Logger logger = Logger.getLogger(CreateQrcodeService.class);
	/**
	 * 获取设备二维码服务路径
	 */
	public final static String CREATE_URL = "https://api.weixin.qq.com/device/create_qrcode";


	/**
	 * @Description:创建设备二维码
	 * @author jxzhong
	 * @version 1.0
	 * @date 2016年2月1日 下午3:43:18
	 * @param codelist
	 * @param token
	 * @return
	 * @return Ticket
	 */
	public List<DeviceCode> createTicket(List<String> codelist, String token) {
		List<DeviceCode> dlist = new ArrayList<DeviceCode>();
		try {
			String tourl = CREATE_URL + "?access_token=" + token;
			RequestCreateQRcode qr=new RequestCreateQRcode();
			qr.setDevice_id_list(codelist);
			qr.setDevice_num(codelist.size());
			String result = new HttpRequest().post(new Gson().toJson(qr), tourl, null);
			if (StringUtils.isNotBlank(result) && result.contains("errcode")) {
				ResponseDeviceList rd = new Gson().fromJson(result, ResponseDeviceList.class);
				if (rd != null && "0".equals(rd.getErrcode())) {// 创建成功
					dlist = rd.getCode_list();
				}
			}
		} catch (Exception e) {
			LogPrintUtil.printExceptionLog(logger, e);
		}
		return dlist;
	}

	public static void main(String[] args) {
		String device_id="10002";
		List<String> codelist = new ArrayList<String>();
		codelist.add(device_id);
		String token = "S3opPT8Fx-nlVspP3KLrZddKWExOw-aREtWpyRHErOCHNlic86xakjRIMdXo3RNcluRXc4e2DD4YFuNhbxVWng48Rvy6oQvaYBNUQiVsxUtMddcbbGNz1syMpInRSeUXCCEjABAQQS";
		List<DeviceCode> c = new CreateQrcodeService().createTicket(codelist, token);
		System.out.println(c.get(0).getTicket());
		QrCodeSample.generationTwoCode(c.get(0).getTicket(), device_id+".png", "C:/Users/zhong/Desktop/do/图片/");
	}

}
