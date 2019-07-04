package config;

import cn.dreampie.quartz.QuartzPlugin;
import com.jfinal.config.*;
import com.jfinal.core.Const;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.tx.TxByActionKeyRegex;
import com.jfinal.plugin.activerecord.tx.TxByActionKeys;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import fixed.Controller.*;
import fixed.interceptor.SessionInterceptor;
import fixed.interceptor.TokenInterceptor;
import fixed.websocket.WebSocket;
import specific.Controller.base.*;
import fixed.Controller.DirectoriesController;
import test.Test;
import weixin.weixin.controller.WeiXinOauthController;
import weixin.weixin.controller.WeixinApiController;
import weixin.weixin.controller.WeixinMsgController;
import weixin.weixin.controller.WeixinPayController;

/**
 * Created by jaer on 2016/12/16.
 */
public class AppConfig extends JFinalConfig{

    /**
     * 如果要支持多公众账号，只需要在此返回各个公众号对应的 ApiConfig 对象即可 可以通过在请求 url 中挂参数来动态从数据库中获取
     * ApiConfig 属性值
     */
    public ApiConfig getApiConfig() {
        ApiConfig ac = new ApiConfig();

        // 配置微信 API 相关常量
        ac.setToken(PropKit.get("token"));
        ac.setAppId(PropKit.get("appId"));
        ac.setAppSecret(PropKit.get("appSecret"));

        /**
         * 是否对消息进行加密，对应于微信平台的消息加解密方式： 1：true进行加密且必须配置 encodingAesKey
         * 2：false采用明文模式，同时也支持混合模式
         */
        ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
        ac.setEncodingAesKey(PropKit.get("encodingAesKey",
                "setting it in config file"));
        return ac;
    }

    /**
     * 如果生产环境配置文件存在，则优先加载该配置，否则加载开发环境配置文件
     *
     * @param pro
     *            生产环境配置文件
     * @param dev
     *            开发环境配置文件
     */
    public void loadProp(String pro, String dev) {
        try {
            PropKit.use(pro);
        } catch (Exception e) {
            PropKit.use(dev);
        }
    }
    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        PropKit.use("a_little_config.txt");
        me.setDevMode(true);
        me.setViewType(ViewType.FREE_MARKER);
        me.setBaseUploadPath(PropKit.get("defaultDir"));
        me.setMaxPostSize(10* Const.DEFAULT_MAX_POST_SIZE);
     //   me.setJsonFactory(new MixedJsonFactory());
    }
    public void configEngine(Engine me) {}
    public void configRoute(Routes me) {
        //固定
        me.add("/user", UserController.class);
        me.add("/department", DepartmentController.class);
        me.add("/post", PostController.class);
        me.add("/menu", MenuController.class);
        me.add("/upload", UploadController.class);
        me.add("/execl", ExeclController.class);
        me.add("/role", RoleController.class);
        me.add("/jurisdiction", JurisdictionController.class);
        me.add("/notice", NoticeController.class);
        me.add("/tableSeeting", TableSettingController.class);
        me.add("/directories", DirectoriesController.class);
        me.add("/messageInfo", MessageInfoController.class);
        me.add("/message", MessageController.class);
        me.add("/option", OptionController.class);
        me.add("/test", Test.class);
        //微信
        me.add("/weixin_msg", WeixinMsgController.class);
        me.add("/weixin_api", WeixinApiController.class);
        me.add("/weixin_oauth", WeiXinOauthController.class);
        me.add("/weixin_pay", WeixinPayController.class);
        //私有
        me.add("/files", FilesController.class);
        me.add("/filesOperate", FilesOperateController.class);
        me.add("/filesRemark", FilesRemarkController.class);
        me.add("/filesState", FilesStateController.class);
        me.add("/set", SetController.class);


    }

    public static C3p0Plugin createC3p0Plugin() {
        String jdbcUrl = PropKit.get("jdbcUrl");
        String user = PropKit.get("user");
        String password = PropKit.get("password");
        String driverClassName = PropKit.get("driverClassName");
        System.out.println(jdbcUrl);
        System.out.println(user);
        System.out.println(password);
        System.out.println(driverClassName);
        return new C3p0Plugin(jdbcUrl,user,password,driverClassName);
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        // 配置C3p0数据库连接池插件
        C3p0Plugin C3p0Plugin = createC3p0Plugin();
        me.add(C3p0Plugin);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(C3p0Plugin);
        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());
        arp.addSqlTemplate("fixed.sql");
        arp.addSqlTemplate("specific.sql");

     //   arp.setDialect(new SqlServerDialect());
        arp.setDialect(new MysqlDialect());
        arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
        me.add(arp);
        //Redis缓存
        RedisPlugin bbsRedis = new RedisPlugin("bbs", "127.0.0.1");
        me.add(bbsRedis);
        //WebSocket 连接
        WebSocket.socket();

        QuartzPlugin quartzPlugin = new QuartzPlugin();
        quartzPlugin.setJobs("system-quartz.properties");
        me.add(quartzPlugin);
    }
    /**
     * 配置全局拦截器
     */
    public void configInterceptor(Interceptors me) {
        me.add(new SessionInterceptor());
        me.add(new TokenInterceptor());
        me.add(new TxByMethodRegex("(.*save.*|.*update.*)"));
        me.add(new TxByMethods("save", "update"));
        me.add(new TxByActionKeyRegex("/trans.*"));
        me.add(new TxByActionKeys("/tx/save", "/tx/update"));
    }
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("basePath"));
    }

    @Override
    public void afterJFinalStart() {
        ApiConfigKit.putApiConfig(getApiConfig());
    }
}