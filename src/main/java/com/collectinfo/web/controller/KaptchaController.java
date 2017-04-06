package com.collectinfo.web.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collectinfo.dto.BaseResult;
import com.collectinfo.util.HttpSessionContext;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

@Controller
public class KaptchaController {

	private Properties props = new Properties();

	private Producer kaptchaProducer = null;

	private String sessionKeyValue = null;

	@Value("${security.captcha.switch}")
	private boolean captcha;

	public KaptchaController() {
		// Switch off disk based caching.
		ImageIO.setUseCache(false);

		this.props.put("kaptcha.border", "no");
		this.props.put("kaptcha.textproducer.font.color", "black");
		this.props.put("kaptcha.textproducer.char.space", "4");
		this.props.put("kaptcha.textproducer.char.length", "4");

		Config config = new Config(this.props);
		this.kaptchaProducer = config.getProducerImpl();
		this.sessionKeyValue = Constants.KAPTCHA_SESSION_KEY;
	}

	@RequestMapping(value = "${security.captcha.uri}.jpg", method = RequestMethod.GET)
	public void captcha(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache");

		// return a jpeg
		resp.setContentType("image/jpeg");

		// create the text for the image
		String capText = this.kaptchaProducer.createText();

		// create the image with the text
		BufferedImage bi = this.kaptchaProducer.createImage(capText);

		ServletOutputStream out = resp.getOutputStream();

		// write the data out
		ImageIO.write(bi, "jpg", out);

		// fixes issue #69: set the attributes after we write the image in case
		// the image writing fails.

		// store the text in the session
		req.getSession().setAttribute(this.sessionKeyValue, capText);
	}

	@RequestMapping("${security.captcha.uri}/{kaptcha}")
	@ResponseBody
	public BaseResult<Boolean> captcha(@PathVariable String kaptcha) {
		return new BaseResult<Boolean>(match(kaptcha));
	}

	public boolean match(String kaptcha) {
		String capTxt = (String) HttpSessionContext.current().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		if (captcha && (StringUtils.isEmpty(capTxt) || !capTxt.equalsIgnoreCase(kaptcha))) {
			return false;
		}
		return true;
	}

}