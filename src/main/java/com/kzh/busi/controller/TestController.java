package com.kzh.busi.controller;

import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.service.sys.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by gang on 2019/5/16.
 */
@Controller
@RequestMapping(value = "/front/test")
public class TestController {
    @RequestMapping(value = "/websocket")
    public String websocket() {
        return "front/test/socket";
    }

    @RequestMapping(value = "/init")
    @ResponseBody
    public Object init() {
        User user = new User();
        return new Result(true, "成功");
    }
}
