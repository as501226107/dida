/**
 *
 */
package com.dream.realms;

import com.dream.bean.Permission;
import com.dream.bean.User;
import com.dream.service.PermissionService;
import com.dream.service.RoleService;
import com.dream.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class Name: LoginRealm.java
 *  Description:
 *  @author  dragon  DateTime 2018年11月10日 上午10:02:04
 *  @company bvit
 *  @email  a501226107@qq.com
 *  @version 1.0
 */
@Component
public class LoginRealmNew extends AuthorizingRealm {
	@Autowired
	UserService us;
	@Autowired
	RoleService rs;
	@Autowired
	PermissionService ps;
	/* (non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("Shrio用户认证--登录..........");
		// 获取登录的用户信息
		UsernamePasswordToken passwordToken = (UsernamePasswordToken) token;// 获取令牌
		// 在Conroller已经登录验证
		if (passwordToken.getUsername() != null && passwordToken.getUsername().length() > 0) {
			// 表示登录成功
			return new SimpleAuthenticationInfo(passwordToken.getUsername(), passwordToken.getPassword(), getName());
		} else {
			// 登录失败....
			return null;
		}
	}
	public static void main(String[] args) {
		String algorithmName="MD5";
		String source="123";
		String salt="你好";
		Integer iterations=1024;
		SimpleHash simpleHash = new SimpleHash(algorithmName, source, salt,iterations );
		System.out.println(simpleHash);
	}
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//1.获取用户权限
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		//2.获取session
		Session session = SecurityUtils.getSubject().getSession();
		User user=(User)session.getAttribute("user");
		List<Permission> permissions = ps.getPermissions(user.getId());
		ArrayList<String> a =new ArrayList<>();
		for (Permission s : permissions) {
			a.add(s.getPercode());
		}
		System.out.println("权限："+a);
		//3.设置权限
		info.addStringPermissions(a);
		return info;
	}
}
