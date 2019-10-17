package com.kzh.busi.controller;

import com.kzh.sys.app.AppConstant;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.tree.BaseTreeNode;
import com.kzh.sys.service.sys.LogService;
import com.kzh.sys.service.sys.LoginService;
import com.kzh.sys.service.sys.MenuService;
import com.kzh.sys.service.sys.UserService;
import com.kzh.sys.util.AES;
import com.kzh.sys.util.SysUtil;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class);
    @Resource
    private AuthenticationManager myAuthenticationManager;
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;
    @Resource
    private LoginService loginService;
    @Resource
    private LogService logService;

    //进入登录页
    @RequestMapping(value = "/home")
    public String loginEntry() {
        return "login";
    }

    //登陆认证
    @RequestMapping(value = "/go")
    @ResponseBody
    public Object go(HttpServletRequest request, HttpSession session, String j_username, String j_password) {
        if (SysUtil.isEmpty(j_username)) {
            return new Result(false, "用户名不能为空");
        }
        if (SysUtil.isEmpty(j_password)) {
            return new Result(false, "密码不能为空");
        }
        j_password = AES.encrypt(j_password, AppConstant.encryptKey);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(j_username, j_password);
        try {
            //调用loadUserByUsername
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) myAuthenticationManager.authenticate(authRequest);
            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 这个非常重要，否则验证后将无法登陆
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            User user = (User) authentication.getPrincipal();
            //todo 账号可用行检查，比如公司关闭了，员工账号如何处理
            session.setAttribute("user", user);
            logService.save(user.getId(), "系统", "登录", "登录", null);
            return startPage(session);
        } catch (DisabledException ex) {
            logger.error("登陆失败", ex);
            return new Result(false, "账号未启用");
        } catch (AuthenticationException ex) {
            logger.error("登陆失败", ex);
            return new Result(false, "登陆失败请检查用户名密码");
        }
    }

    //登陆认证
    @RequestMapping(value = "/domain")
    @ResponseBody
    public Object domain(HttpServletRequest request, HttpSession session, String j_username, String j_password) {
        if (SysUtil.isEmpty(j_username)) {
            return new Result(false, "用户名不能为空");
        }
        if (SysUtil.isEmpty(j_password)) {
            return new Result(false, "密码不能为空");
        }
        Result resultDomain = loginService.checkDomain(j_username, j_password);
        if (!resultDomain.isSuccess()) {
            return resultDomain;
        } else {
            loginService.getDomainUser(j_username, j_password);
        }
        return go(request, session, j_username, j_password);
    }

    private Result startPage(HttpSession session) {
        BaseTreeNode treeNode = menuService.getAceResourceTree(session, null);
        String firstMenu = MenuService.getFirstMenu(treeNode);
        if (SysUtil.isEmpty(firstMenu)) {
            return new Result(true, "登陆成功", "/busi/home");
        } else {
            return new Result(true, "登陆成功", firstMenu);
        }
    }
}
