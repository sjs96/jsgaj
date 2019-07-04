package fixed.jurisdiction;

/**
 * 鉴权模式封装
 */
public class AuthMode {

    public int authCode = 0; //各种鉴权模式枚举
    public String authId = null; //单条鉴权标识（单角色、单权限）
    public String[] authIds = null; //多条鉴权标识（多角色、多权限）
}
