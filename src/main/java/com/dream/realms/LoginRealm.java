/**
 *
 */
package com.dream.realms;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dream.bean.Role;
import com.dream.bean.User;
import com.dream.service.RoleService;
import com.dream.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  Class Name: LoginRealm.java
 *  Description:
 *  @author  dragon  DateTime 2018年11月10日 上午10:02:04
 *  @company bvit
 *  @email  a501226107@qq.com
 *  @version 1.0
 */
@Component
public class LoginRealm extends AuthorizingRealm {
	@Autowired
	UserService us;
	@Autowired
	RoleService rs;
	/* (non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//1.将token转换为UsernamePasswordToken
		UsernamePasswordToken user=(UsernamePasswordToken)token;
		//2.从user中获取user
		String username = user.getUsername();
		String password=new String(user.getPassword());
		//3.调用数据库的方法，从数据库中查询username对应的用户记录
		//User del = us.selectOne(new EntityWrapper<User>().eq("username",username).eq("del", 0));
		User temp=new User();
		temp.setUsername(username);
		User del=us.login(temp);
		//4.如果用户不存在,则可以抛出UnkownAccountException 异常
		if(del==null) {
			throw new UnknownAccountException("用户名不存在");
		}
		//5.用户锁定
		if(del.getLocked()=="1"){
			throw new LockedAccountException("用户被锁定");
		}
		//根据用户的情况，来构建AuthenticationInfo对象并返回，通常使用的实例为
		//SimpleAuthenticationInfo
		//以下信息是从数据库获取的
		Object hashedCredentials=del.getPassword();
		//为什么使用盐 值？如果密码一致，加密的结果也一样
		//使用盐值加密，使用唯一的字符串
		ByteSource bytes=ByteSource.Util.bytes(del.getSalt());
		String realmName=getName();
		System.out.println("用户输入的密码"+password+"数据库中的密码"+del.getPassword());
		System.out.println("用户的角色"+del.getRoles());
		//SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, realmName);
		//获取角色权限
//		Set<Role> roles = del.getRoles();
//		for (Role role : roles) {
//			Integer id = role.getId();
//			//根据用户角色的id从数据库获得对应的权限
//			Role roleWithPermission = rs.getRoleWithPermission(id);
//			//给用户设置角色附带的权限，存在没有权限的角色，需要做空指针判断
//			if(roleWithPermission!=null){
//				role.setPermissions(roleWithPermission.getPermissions());
//			}
//		}
//		System.out.println("用户的权限");
//		for (Role role : roles) {
//			System.out.println("角色："+role.getName()+"具有的权限是：--//"+role.getPermissions());
//		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(del, hashedCredentials, bytes, realmName);
		return info;
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
		return info;
	}
}
