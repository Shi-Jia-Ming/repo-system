package cn.edu.hit.spat.system.controller;

import cn.edu.hit.spat.common.authentication.ShiroHelper;
import cn.edu.hit.spat.common.controller.BaseController;
import cn.edu.hit.spat.common.entity.GwarbmsConstant;
import cn.edu.hit.spat.common.utils.DateUtil;
import cn.edu.hit.spat.common.utils.GwarbmsUtil;
import cn.edu.hit.spat.system.entity.Order;
import cn.edu.hit.spat.system.entity.User;
import cn.edu.hit.spat.system.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author MrBird
 */
@Controller("systemView")
@RequiredArgsConstructor
public class ViewController extends BaseController {

    private final IUserService userService;
    private final ShiroHelper shiroHelper;
    private final IUserDataPermissionService userDataPermissionService;

    @GetMapping("login")
    @ResponseBody
    public Object login(HttpServletRequest request) {
        if (GwarbmsUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(GwarbmsUtil.view("login"));
            return mav;
        }
    }

    @GetMapping("unauthorized")
    public String unauthorized() {
        return GwarbmsUtil.view("error/403");
    }


    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @GetMapping("index")
    public String index(Model model) {
        AuthorizationInfo authorizationInfo = shiroHelper.getCurrentUserAuthorizationInfo();
        User user = super.getCurrentUser();
        User currentUserDetail = userService.findByName(user.getUsername());
        currentUserDetail.setPassword("It's a secret");
        model.addAttribute("user", currentUserDetail);
        model.addAttribute("permissions", authorizationInfo.getStringPermissions());
        model.addAttribute("roles", authorizationInfo.getRoles());
        return "index";
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "layout")
    public String layout() {
        return GwarbmsUtil.view("layout");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "password/update")
    public String passwordUpdate() {
        return GwarbmsUtil.view("system/user/passwordUpdate");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "user/profile")
    public String userProfile() {
        return GwarbmsUtil.view("system/user/userProfile");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "user/avatar")
    public String userAvatar() {
        return GwarbmsUtil.view("system/user/avatar");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "user/profile/update")
    public String profileUpdate() {
        return GwarbmsUtil.view("system/user/profileUpdate");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "system/user")
    @RequiresPermissions("user:view")
    public String systemUser() {
        return GwarbmsUtil.view("system/user/user");
    }


    /* DELETE */
    // TODO

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "system/user/add")
    @RequiresPermissions("user:add")
    public String systemUserAdd() {
        return GwarbmsUtil.view("system/user/userAdd");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "system/user/detail/{username}")
    @RequiresPermissions("user:view")
    public String systemUserDetail(@PathVariable String username, Model model) {
        resolveUserModel(username, model, true);
        return GwarbmsUtil.view("system/user/userDetail");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "system/user/update/{username}")
    @RequiresPermissions("user:update")
    public String systemUserUpdate(@PathVariable String username, Model model) {
        resolveUserModel(username, model, false);
        return GwarbmsUtil.view("system/user/userUpdate");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "system/role")
    @RequiresPermissions("role:view")
    public String systemRole() {
        return GwarbmsUtil.view("system/role/role");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "system/menu")
    @RequiresPermissions("menu:view")
    public String systemMenu() {
        return GwarbmsUtil.view("system/menu/menu");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "system/dept")
    @RequiresPermissions("dept:view")
    public String systemDept() {
        return GwarbmsUtil.view("system/dept/dept");
    }

    @RequestMapping(GwarbmsConstant.VIEW_PREFIX + "index")
    public String pageIndex() {
        return GwarbmsUtil.view("index");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "404")
    public String error404() {
        return GwarbmsUtil.view("error/404");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "403")
    public String error403() {
        return GwarbmsUtil.view("error/403");
    }

    @GetMapping(GwarbmsConstant.VIEW_PREFIX + "500")
    public String error500() {
        return GwarbmsUtil.view("error/500");
    }

    private void resolveUserModel(String username, Model model, Boolean transform) {
        User user = userService.findByName(username);
        String deptIds = userDataPermissionService.findByUserId(String.valueOf(user.getUserId()));
        user.setDeptIds(deptIds);
        model.addAttribute("user", user);
        if (transform) {
            String sex = user.getSex();
            if (User.SEX_MALE.equals(sex)) {
                user.setSex("男");
            } else if (User.SEX_FEMALE.equals(sex)) {
                user.setSex("女");
            } else {
                user.setSex("保密");
            }
        }
        if (user.getLastLoginTime() != null) {
            model.addAttribute("lastLoginTime", DateUtil.getDateFormat(user.getLastLoginTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        }
    }
}
