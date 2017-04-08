package com.collectinfo.web.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import com.collectinfo.constant.CommonStoreKeyConstant;
import com.collectinfo.constant.SystemConstant;
import com.collectinfo.domain.db.User;
import com.collectinfo.domain.redis.RedisCommonStore;
import com.collectinfo.dto.BaseResult;
import com.collectinfo.dto.MenuDTO;
import com.collectinfo.repository.jpa.api.UserRepository;
import com.collectinfo.repository.redis.api.RedisCommonStoreRepository;
import com.collectinfo.service.MailService;
import com.collectinfo.service.MenuService;
import com.collectinfo.service.UserService;
import com.collectinfo.util.AssertUtil;
import com.collectinfo.util.UserSessionContext;

@Controller
public class IndexController extends BaseController implements ErrorController {

	@Value("${templates.rootpath.pc}")
	private String pcRootPath;

	@Value("${templates.rootpath.mobile}")
	private String mobileRootPath;

	@Value("${system.title}")
	private String systemTitle;

	@Value("${security.logout.uri}")
	private String logoutUri;

	@Value("${security.login.uri}")
	private String loginUri;

	@Value("${security.register.uri}")
	private String registerUri;

	@Value("${security.findPassword.uri}")
	private String findPasswordUri;

	@Value("${security.login.check}")
	private String loginCheckUri;

	@Value("${server.context-path}")
	private String contextPath;

	@Value("${error.uri}")
	private String errorUri;

	@RequestMapping({ "${security.login.uri}", "${security.login.uri}/", "${security.login.uri}/index.html" })
	public String loginIndex(HttpServletRequest request, Model model) {
		setCommonConfigs(request, model);
		model.addAttribute("registerUri", registerUri);
		model.addAttribute("findPasswordUri", findPasswordUri);
		return loginUri + "/index";
	}

	@RequestMapping({ "${security.register.uri}", "${security.register.uri}/", "${security.register.uri}/index.html" })
	public String registerIndex(HttpServletRequest request, Model model) {
		setCommonConfigs(request, model);
		model.addAttribute("loginUri", loginUri);
		model.addAttribute("findPasswordUri", findPasswordUri);
		return registerUri + "/index";
	}

	@RequestMapping({ "${security.findPassword.uri}", "${security.findPassword.uri}/",
			"${security.findPassword.uri}/index.html" })
	public String findPasswordIndex(HttpServletRequest request, Model model) {
		setCommonConfigs(request, model);
		model.addAttribute("loginUri", loginUri);
		model.addAttribute("registerUri", registerUri);
		return findPasswordUri;
	}

	@Autowired
	private RedisCommonStoreRepository redisCommonStoreRepository;

	@Autowired
	private MenuService menuService;

	private static final String ATTR_MENU_CACHE = "ATTR_MENU_CACHE";

	@RequestMapping("**/*.html")
	public String index(Device device, HttpServletRequest request, Model model) {
		setCommonConfigs(request, model);
		String rootPath = "";
		// String rootPath = pcRootPath;
		// if (device.isMobile()) {
		// rootPath = mobileRootPath;
		// }
		model.addAttribute("date", DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA).format(new Date()));
		model.addAttribute("user", UserSessionContext.current());
		model.addAttribute("view", rootPath + request.getRequestURI().replace(".html", ""));
		model.addAttribute("logoutUri", logoutUri);
		return "main";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/menus")
	@ResponseBody
	public BaseResult<?> menus(HttpServletRequest request) {
		RedisCommonStore config = redisCommonStoreRepository.findOne(CommonStoreKeyConstant.NEED_REFRESH_MENU);
		List<MenuDTO> menus = (List<MenuDTO>) request.getServletContext().getAttribute(ATTR_MENU_CACHE);
		if (menus == null || !Boolean.FALSE.toString().equals(config.getKey())) {
			menus = menuService.findByUser(UserSessionContext.current());
			request.getServletContext().setAttribute(ATTR_MENU_CACHE, menus);
			config.setValue(Boolean.FALSE.toString());
			redisCommonStoreRepository.save(config);
		}
		return new BaseResult<>(menus);
	}

	@Value("${client.role}")
	private String clientRole;

	@Value("${superadmin.role}")
	private String superAdminRole;

	@RequestMapping("${security.entry.uri}")
	public String entry() {
		User user = UserSessionContext.current();
		String url = "redirect:#contextPath#/info/list.html";
		if (user.hasRole(superAdminRole)) {
			url = "redirect:#contextPath#/user/list.html";
		}
		return url.replace("#contextPath#", contextPath).replaceAll("\\/{2}+", "/");
	}

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Value("${security.entry.uri}")
	private String entryUri;

	@Autowired
	private KaptchaController kaptcha;

	@RequestMapping("${security.register.uri}/save")
	@ResponseBody
	public BaseResult<String> doRegister(@RequestBody User user) {
		AssertUtil.isNotEmptyForBusiness(user.getUsername(), "用户名不能为空");
		AssertUtil.isNotEmptyForBusiness(user.getPassword(), "密码不能为空");
		AssertUtil.isNotEmptyForBusiness(user.getConfirmPassword(), "确认密码不能为空");
		AssertUtil.isNotEmptyForBusiness(user.getEmail(), "邮箱不能为空");
		AssertUtil.isEqualsForBusiness(user.getPassword(), user.getConfirmPassword(), "两次密码不同");
		AssertUtil.isTrueForBusiness(kaptcha.match(user.getCaptcha()), "验证码不正确");
		user.setUsername(new String(Base64Utils.decodeFromString(user.getUsername())));
		user.setPassword(new String(Base64Utils.decodeFromString(user.getPassword())));
		AssertUtil.isTrueForBusiness(user.getPassword().length() > 5, "密码必须6位或以上");
		user.setConfirmPassword(new String(Base64Utils.decodeFromString(user.getConfirmPassword())));
		user.setEmail(new String(Base64Utils.decodeFromString(user.getEmail())));
		UserSessionContext.set(user);
		userService.createClient(user);
		refreshUserContext(user);
		return new BaseResult<String>(entryUri);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	@RequestMapping("${security.findPassword.uri}/reset")
	@ResponseBody
	public BaseResult<String> resetPassword(@RequestBody User user) {
		user.setUsername(new String(Base64Utils.decodeFromString(user.getUsername())));
		user.setEmail(new String(Base64Utils.decodeFromString(user.getEmail())));
		AssertUtil.isTrueForBusiness(new EmailValidator().isValid(user.getEmail(), null), "邮箱格式不正确");
		User old = (User) userService.loadUserByUsername(user.getUsername());
		AssertUtil.notNullForBusiness(old, "用户不存在");
		AssertUtil.isEqualsForBusiness(user.getEmail(), old.getEmail(), "邮箱不存在");
		UserSessionContext.set(old);
		String password = StringUtils.randomAlphanumeric(6);
		old.setPassword(passwordEncoder.encode(password));
		userRepository.save(old);
		if (logger.isDebugEnabled()) {
			logger.debug(user.getUsername() + "的新密码是: " + password);
		}
		mailService.sendRestPassword(user.getEmail(), password);
		return new BaseResult<String>(loginUri);
	}

	private void setCommonConfigs(HttpServletRequest request, Model model) {
		model.addAttribute("title", systemTitle);
	}

	@RequestMapping("${error.uri}")
	public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
		setCommonConfigs(request, model);
		int status = response.getStatus();
		if (Boolean.TRUE.equals(request.getAttribute(SystemConstant.ERROR_500_KEY))) {
			status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
		switch (status) {
		case HttpServletResponse.SC_NOT_FOUND:
		case HttpServletResponse.SC_FORBIDDEN: {
			return errorUri + "/404";
		}
		case HttpServletResponse.SC_INTERNAL_SERVER_ERROR: {
			model.addAttribute("errorId", request.getAttribute(SystemConstant.ERROR_CODE_ID_KEY));
			model.addAttribute("errorMsg", request.getAttribute(SystemConstant.ERROR_MSG_KEY));
			return errorUri + "/500";
		}
		default: {
			return errorUri + "/401";
		}
		}
	}

	@Override
	public String getErrorPath() {
		return errorUri;
	}

}
