package com.hotnews.service;

import com.alibaba.fastjson.JSONObject;
import com.hotnews.util.HotNewsUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.aspectj.apache.bcel.generic.MULTIANEWARRAY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Author: XianDaLi
 * Date: 2020/8/7 17:40
 * Remark:
 */
@Service
public class QiniuService {
	private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

	//构造一个带指定 Region 对象的配置类
	Configuration cfg = new Configuration(Region.region2());
	//...其他参数参考类注释
	UploadManager uploadManager = new UploadManager(cfg);

	//设置好账号的ACCESS_KEY和SECRET_KEY
	String accessKey = "8GAIbJV2aW5OJWfkkvTFBLkg_FS9vte8nVyoknKw";
	String secretKey = "8A8oFOLT0RPA6YZHy-lBVZsaj05nuLFZgNc4cXa1";
	//要上传的空间
	String bucket = "lxd-work-space";
	String key = null;

	//密钥配置
	Auth auth = Auth.create(accessKey, secretKey);

//	private static String QINIU_IMAGE_DOMAIN = "http://7xsetu.com1.z0.glb.clouddn.com/";
	private static String QINIU_IMAGE_DOMAIN = "http://qeoq8ylwr.bkt.clouddn.com/";

	//简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public String getUpToken() {
		return auth.uploadToken(bucket);
	}

	public String saveImage(MultipartFile file) throws IOException {
		try {
			int dotPos = file.getOriginalFilename().lastIndexOf(".");
			if (dotPos < 0) {
				return null;
			}
			String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
			if (!HotNewsUtil.isFileAllowed(fileExt)) {
				return null;
			}

			String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
			//调用put方法上传
			Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
			//打印返回的信息
			if (res.isOK() && res.isJson()) {
				return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
			} else {
				logger.error("七牛异常:" + res.bodyString());
				return null;
			}
		} catch (QiniuException e) {
			// 请求失败时打印的异常的信息
			logger.error("七牛异常:" + e.getMessage());
			return null;
		}
	}

}
