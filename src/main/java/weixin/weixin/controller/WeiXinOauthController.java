package weixin.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.*;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import fixed.util.Util;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Javen
 * 2015年12月5日下午2:20:44
 *
 */
public class WeiXinOauthController extends Controller {
	static Log log = Log.getLog(WeiXinOauthController.class);
	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
		
		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}
	public void indexAjax() {
		int  subscribe=0;
		//用户同意授权，获取code
		String code=getPara("code");
		String state=getPara("state");
		if (code!=null) {
			String appId=ApiConfigKit.getApiConfig().getAppId();
			String secret=ApiConfigKit.getApiConfig().getAppSecret();
			//通过code换取网页授权access_token
			SnsAccessToken snsAccessToken=SnsAccessTokenApi.getSnsAccessToken(appId,secret,code);
//			String json=snsAccessToken.getJson();
			String token=snsAccessToken.getAccessToken();
			String openId=snsAccessToken.getOpenid();
			//拉取用户信息(需scope为 snsapi_userinfo)
			ApiResult apiResult=SnsApi.getUserInfo(token, openId);

			log.warn("getUserInfo:"+apiResult.getJson());
			if (apiResult.isSucceed()) {
				JSONObject jsonObject= JSON.parseObject(apiResult.getJson());
				String nickName=jsonObject.getString("nickname");
				//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
				int sex=jsonObject.getIntValue("sex");
				String city=jsonObject.getString("city");//城市
				String province=jsonObject.getString("province");//省份
				String country=jsonObject.getString("country");//国家
				String headimgurl=jsonObject.getString("headimgurl");
				String unionid=jsonObject.getString("unionid");

				Map<String,Object> user= new HashMap<String,Object>();
				user.put("openId",openId);
				user.put("nickName",nickName);
				user.put("sex",sex);
				user.put("city",city);
				user.put("province",province);
				user.put("country",country);
				user.put("headimgurl",headimgurl);
				user.put("unionid",unionid);
				renderJson(Util.getResultMap(Util.state_ok,"有数据",user,Util.level_info,Util.model_dev));
			}

		}else {
			renderJson(Util.getResultMap(Util.state_err,"无数据",null,Util.level_info,Util.model_dev));
		}

	}

	public void index() {
		int  subscribe=0;
		//用户同意授权，获取code
		String code=getPara("code");
		String state=getPara("state");
		if (code!=null) {
			String appId=ApiConfigKit.getApiConfig().getAppId();
			String secret=ApiConfigKit.getApiConfig().getAppSecret();
			//通过code换取网页授权access_token
			SnsAccessToken snsAccessToken=SnsAccessTokenApi.getSnsAccessToken(appId,secret,code);
//			String json=snsAccessToken.getJson();
			String token=snsAccessToken.getAccessToken();
			String openId=snsAccessToken.getOpenid();
			//拉取用户信息(需scope为 snsapi_userinfo)
			ApiResult apiResult=SnsApi.getUserInfo(token, openId);
			
			log.warn("getUserInfo:"+apiResult.getJson());
			if (apiResult.isSucceed()) {
				JSONObject jsonObject= JSON.parseObject(apiResult.getJson());
				String nickName=jsonObject.getString("nickname");
				//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
				int sex=jsonObject.getIntValue("sex");
				String city=jsonObject.getString("city");//城市
				String province=jsonObject.getString("province");//省份
				String country=jsonObject.getString("country");//国家
				String headimgurl=jsonObject.getString("headimgurl");
				String unionid=jsonObject.getString("unionid");
				//获取用户信息判断是否关注
				ApiResult userInfo = UserApi.getUserInfo(openId);
				log.warn(JsonKit.toJson("is subsribe>>"+userInfo));
				if (userInfo.isSucceed()) {
					String userStr = userInfo.toString();
					subscribe= JSON.parseObject(userStr).getIntValue("subscribe");
				}
				
//				Users.me.save(openId, WeiXinUtils.filterWeixinEmoji(nickName), unionid, headimgurl, country, city, province, sex);
			}
			
			setSessionAttr("openId", openId);
			if (subscribe==0) {
				redirect("http://192.168.1.106:8081");
			}else {
				//根据state 跳转到不同的页面
				if (state.equals("2222")) {
					redirect("http://www.cnblogs.com/zyw-205520/");
				}else {
					redirect("/login");
				}
			}
			
			
		}else {
			renderText("code is  null");
		}
	}
	
	/**
	 * PC扫码登陆回调 
	 * 获取AccessToken以及用户信息跟微信公众号授权用户用户信息一样
	 */
	public void webCallBack() {
		//用户同意授权，获取code
		String code=getPara("code");
		String state=getPara("state");
		if (code!=null) {
			System.out.println("code>"+code+" state>"+state);
			String appId= PropKit.get("webAppId");
			String secret= PropKit.get("webAppSecret");
			//通过code换取网页授权access_token
			SnsAccessToken snsAccessToken=SnsAccessTokenApi.getSnsAccessToken(appId,secret,code);
			String json=snsAccessToken.getJson();
System.out.println("通过code获取access_token>>"+json);			
			String token=snsAccessToken.getAccessToken();
			String openId=snsAccessToken.getOpenid();
			//拉取用户信息(需scope为 snsapi_userinfo)
			ApiResult apiResult=SnsApi.getUserInfo(token, openId);
			
log.warn("getUserInfo:"+apiResult.getJson());
			if (apiResult.isSucceed()) {
				JSONObject jsonObject= JSON.parseObject(apiResult.getJson());
				String nickName=jsonObject.getString("nickname");
				//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
				int sex=jsonObject.getIntValue("sex");
				String city=jsonObject.getString("city");//城市
				String province=jsonObject.getString("province");//省份
				String country=jsonObject.getString("country");//国家
				String headimgurl=jsonObject.getString("headimgurl");
				String unionid=jsonObject.getString("unionid");
			}
			renderText("通过code获取access_token>>"+json+"  \n"+"getUserInfo:"+apiResult.getJson());
		}
		
	}
	
}
