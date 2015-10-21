package com.vk.simpleutil.webview;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import android.support.v4.util.ArrayMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.net.Uri;
import android.text.TextUtils;

import com.vk.simpleutil.library.XSimpleText;

/*
 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.		
 * 
 * FileName：UrlUtils.java
 * 
 * Description：
 * 
 * History：
 * 1.0 sekarao 2014-2-20 Create
 */
@Deprecated
public class UrlUtils {

	private static Pattern VALID_URL = Pattern.compile(
			"(.+)(\\.)(.+)[^\\w]*(.*)", Pattern.CASE_INSENSITIVE);
	private static Pattern VALID_LOCAL_URL = Pattern.compile(
			"(^file://.+)|(.+localhost:?\\d*/.+\\..+)",
			Pattern.CASE_INSENSITIVE);
	private static Pattern VALID_MTT_URL = Pattern.compile("mtt://(.+)",
			Pattern.CASE_INSENSITIVE);
	private static Pattern VALID_QB_URL = Pattern.compile("qb://(.+)",
			Pattern.CASE_INSENSITIVE);
	private static Pattern VALID_PAY_URL = Pattern.compile(
			"(tenpay|alipay)://(.+)", Pattern.CASE_INSENSITIVE);
	private static Pattern VALID_IP_ADDRESS = Pattern.compile(
			"(\\d){1,3}\\.(\\d){1,3}"
					+ "\\.(\\d){1,3}\\.(\\d){1,3}(:\\d{1,4})?(/(.*))?",
			Pattern.CASE_INSENSITIVE);
	// private static Pattern VALID_DOMAIN = Pattern.compile(
	// "^[a-zA-Z0-9]+([\\.\\-\\_]?[a-zA-Z0-9]+)*"
	// + "(\\.{1}(com|cn|ac|edu|gov|mil|arpa|net|org|biz|info|pro|"
	// + "name|coop|mobi|int|us|travel|xxx|idv|co|so|tv|hk|asia|"
	// + "me|cc|tw))$", Pattern.CASE_INSENSITIVE);
	private static String[] SUFFIX = { "gd", "com", "cn", "ac", "edu", "gov",
			"mil", "arpa", "net", "org", "biz", "info", "pro", "name", "coop",
			"mobi", "int", "us", "travel", "xxx", "idv", "co", "so", "tv",
			"hk", "asia", "me", "cc", "tw" };
	/** Regex used to parse content-disposition headers */
	private static final Pattern CONTENT_DISPOSITION_PATTERN = Pattern
			.compile(
					"attachment;\\s*filename\\s*=\\s*(\"?)([^\"]*)\\1\\s*[;\\s*charset=\\s*]*([^\"]*)\\s*$",
					Pattern.CASE_INSENSITIVE);

	private static final Pattern INLINE_CONTENT_DISPOSITION_PATTERN = Pattern
			.compile(
					"inline;\\s*filename\\s*=\\s*(\"?)([^\"]*)\\1\\s*[;\\s*charset=\\s*]*([^\"]*)\\s*$",
					Pattern.CASE_INSENSITIVE);

	private static final String URL_CODING = "utf-8";

	// 存放需要在UA尾部添加“/(alipay)”标记的主机
	private static final String[] alipaySites = new String[] { "alipay.com",
			"115.124.16.81", "110.75.128.59" };

	// 管家体检地址
	private static final String SECURITYURL = "msm.qq.com";

	private static final String TAG = "UrlUtility";

	/**
	 * 判读站点url是否为支付宝的主机。
	 * 
	 * @param url
	 * @return 是否为支付宝的主机。
	 */
	public static boolean isAlipaySite(String url) {
		if (XSimpleText.isEmpty(url)) {
			return false;
		}
		boolean result = false;

		for (String host : alipaySites) {
			if (host != null && url.contains(host)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * url 合法性预处理 过滤非法字符/替换空格/编码escape
	 * 
	 * @param url
	 * @param autoGo
	 * @return
	 */
	public static String prepareUrl(String url) {
		if (url == null || url.length() == 0)
			return url;

		// 1.页面内跳转
		if (url.charAt(0) == '#')
			return url;

		// 2.处理非法字符如回车、把空格替换为20%

		// 添加try{}catch(){}，BUG7068961，levijiang 2011-05-29
		try {
			url = url.replaceAll(" ", "%20");
			url = url.replaceAll("&amp;", "&");
			url = url.replaceAll("\\|", "%7C");
			url = url.replaceAll("\\^", "%5E");
			url = url.replaceAll("<", "%3C");
			url = url.replaceAll(">", "%3E");
			url = url.replaceAll("\\{", "%7B");
			url = url.replaceAll("\\}", "%7D");
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
		}

		if (!UrlUtils.isSmsUrl(url)) {
			url = UrlUtils.escapeAllChineseChar(url);
		}

		return url;
	}

	/**
	 * string url to URL
	 * 
	 * @param url
	 * @return
	 */
	public static URL toURL(String url) throws MalformedURLException {
		URL _URL = new URL(url);

		// 有个别 URL 在 path 和 querystring 之间缺少 / 符号，需补上
		if (_URL.getPath() == null || "".equals(_URL.getPath())) {
			if (_URL.getFile() != null && _URL.getFile().startsWith("?")) {
				// 补斜杠符号
				int idx = url.indexOf('?');
				if (idx != -1) {
					StringBuilder sb = new StringBuilder();
					sb.append(url.substring(0, idx));
					sb.append('/');
					sb.append(url.substring(idx));

					_URL = new URL(sb.toString());

					// System.out.println("toURL : " + _URL.toString());
				}
			}

			// 分支走到这里，没有path也没有file，证明为一个没有/的host，例如:
			// http://m.cnbeta.com(注意：后面没有/)
			if (_URL.getFile() == null || "".equals(_URL.getFile())) {
				StringBuilder sb = new StringBuilder();
				sb.append(url);
				sb.append("/");

				_URL = new URL(sb.toString());
			}

		}
		return _URL;
	}

	/**
	 * 判断url是否为空,
	 * 
	 * @retrun true url is empty
	 */
	public static boolean isEmptyUrl(String url) {
		return (url == null || (url.trim().length() == 0) || url.trim().equals(
				"/"));
	}

	/**
	 * 处理url中的中文字符
	 * 
	 * @param url
	 * @return
	 */
	public static String escapeAllChineseChar(String url) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < url.length(); i++) {
			char c = url.charAt(i);

			if (c >= 0x4E00 && c <= 0x9FFF || c >= 0xFE30 && c <= 0xFFA0) {
				String escapedStr;
				try {
					escapedStr = URLEncoder.encode(String.valueOf(c),
							URL_CODING);

					sb.append(escapedStr);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				sb.append(c);
			}
		}
		// Logger.d("Url", " dest : " + sb.toString());
		return sb.toString();
	}

	/**
	 * url escape
	 * 
	 * @param url
	 * @return
	 */
	public static String escape(String url) {
		if (url == null || url.trim().length() == 0)
			return url;

		try {
			int idx = url.indexOf('?');
			if (idx != -1) {
				String path = url.substring(0, idx + 1);
				String queryString = url.substring(idx + 1);

				String[] nameValues = queryString.split("&");

				StringBuilder sb = new StringBuilder();
				sb.append(path);

				boolean hasParam = false;

				for (String nameValue : nameValues) {
					// System.out.println("nameValue : " + nameValue);
					if (nameValue != null && nameValue.length() > 0) {
						hasParam = true;
						int idxEqual = nameValue.indexOf('=');
						if (idxEqual != -1) {
							sb.append(nameValue.substring(0, idxEqual + 1));

							// 逐字转码
							String value = nameValue.substring(idxEqual + 1);

							// System.out.println("value : " + value);
							if (value != null && value.length() > 0) {
								int i = 0;
								int j = 0; // escape 起点偏移量
								boolean ascii = true;
								for (; i < value.length(); i++) {
									char c = value.charAt(i);
									// System.out.println("c : " + (int) c);
									if (c >= 0x4E00 && c <= 0x9FFF
											|| c >= 0xFE30 && c <= 0xFFA0) {
										// System.out.println("ZH C ：" + c);
										ascii = false;
									} else {
										// escape
										if (!ascii) {
											String escapedStr = URLEncoder
													.encode(value.substring(j,
															i), URL_CODING);
											sb.append(escapedStr);

										}

										// 对 '/' 进行编码，防止 ResolveBase 出错
										if (c == '/') {
											// 二次修改
											// 百度图片的 URL 参数值里面有 %2F 会连接不上
											sb.append(c);
											// sb.append(URLEncoder.encode(String.valueOf(c),
											// URL_ENCODING));
										} else {
											sb.append(c);
										}

										ascii = true;
										j = i;
									}
								}
								// 最后一段
								if (!ascii && j < i) {
									String escapedStr = URLEncoder.encode(
											value.substring(j, i), URL_CODING);
									sb.append(escapedStr);
								}
							}
						} else {
							sb.append(nameValue);
						}
						sb.append('&');
					}
				}

				// 删除最后一个 &
				String escapedUrl = sb.toString();
				if (hasParam) {
					if (escapedUrl.charAt(escapedUrl.length() - 1) == '&')
						escapedUrl = escapedUrl.substring(0,
								escapedUrl.length() - 1);
				}
				return escapedUrl;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * 是否javaScript
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isJavascript(String url) {
		// javascript:
		return (null != url) && (url.length() > 10)
				&& url.substring(0, 11).equalsIgnoreCase("javascript:");
	}

	public static String getJavascriptCommand(String url) {
		int idx1 = url.indexOf(':');
		int idx2 = url.indexOf(';');

		if (idx1 == -1)
			idx1 = 0;
		else
			idx1 = idx1 + 1;

		if (idx2 == -1)
			idx2 = url.length();

		return url.substring(idx1, idx2);
	}

	/**
	 * 是否页面跳转
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isAnchorUrl(String url) {
		if (url == null || url.length() == 0)
			return false;

		return url.startsWith("#");
	}

	// 目前该schema仅从微信
	public static boolean isWxUrl(String url) {
		// 返回来的是mttbrowser://***
		return !XSimpleText.isEmpty(url) && url.startsWith("mttbrowser://");

	}

	// h5qq分享的url
	public static boolean isH5QQUrl(String url) {
		return !XSimpleText.isEmpty(url)
				&& url.startsWith("http://openmobile.qq.com/api/check?page=shareindex.html&style=9");

	}

	public static boolean getQqResult(String url) {
		// 去掉协议头
		url = deletePrefix(url);

		if (!XSimpleText.isEmpty(url)) {
			// 分割参数
			Map<String, String> param_map = new ArrayMap<String, String>();
			String[] params = url.split("&");
			for (String param : params) {
				String[] key_value = param.split("=", 2);
				if (key_value.length == 2)
					param_map.put(key_value[0], key_value[1]);
			}

			// 如果url字段不为null则返回该字段，否则直接返回url
			if (!XSimpleText.isEmpty(param_map.get("result"))) {
				return param_map.get("result").equals("complete");
			}
		}
		return false;
	}

	public static Map<String, String> getWxParamFormUrl(String url) {
		url = deletePrefix(url);

		Map<String, String> paramMap = new ArrayMap<String, String>();
		if (TextUtils.isEmpty(url)) {
			return paramMap;
		}
		String[] params = url.split(",");
		for (String param : params) {
			String[] key_value = param.split("=", 2);
			if (key_value.length == 2)
				paramMap.put(key_value[0], key_value[1]);
		}
		return paramMap;
	}

	/**
	 * 是否是mrvp url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isMRVPUrl(String url) {
		if (XSimpleText.isEmpty(url))
			return false;

		// 1.在Net环境下，直接返回来的是mrvp://***
		if ((null != url) && (url.length() > 7)
				&& url.substring(0, 7).equalsIgnoreCase("mrvp://")) {
			return true;
		}
		// 2. 在wap环境下，
		else if (UrlUtils.isQQCOMDomain(url)) {
			String sPath = UrlUtils.getPath(url);

			if (!XSimpleText.isEmpty(sPath) && sPath.contains("mrvp://"))
				return true;
		}

		return false;
	}

	/**
	 * @return True if the remote url is valid.
	 */
	public static boolean isRemoteUrl(String url) {
		if (url == null || url.length() == 0)
			return false;

		// page:开头是中转特定协议
		return isHttpUrl(url) || isHttpsUrl(url) || isBrokerUrl(url)
				|| isSmsUrl(url) || isAlipayUrl(url) || isRtspUrl(url);
	}

	/**
	 * 判断是否为三星应用链接。
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isSamsungUrl(String url) {
		return !XSimpleText.isEmpty(url) && url.startsWith("samsungapps://");

	}

	public static boolean isSpecialUrl(String url) {
		if (url == null) {
			return false;
		}

		String lowser = url.toLowerCase();
		return isSamsungUrl(lowser) || lowser.startsWith("about:blank")
				|| lowser.startsWith("data:");
	}

	/**
	 * 删除自定义前缀，如dttp://，security://等 levijiang 2011-06-17
	 * 
	 * @param srcUrl
	 * @return
	 */
	public static String deleteCustomPrefix(final String srcUrl) {
		String url = srcUrl;

		if (isDttpUrl(srcUrl) || isSecurityCacheUrl(srcUrl)
				|| isSecurityFileUrl(srcUrl) || isBrokerUrl(url)
				|| isWebkitUrl(url)) {
			url = deletePrefix(srcUrl);
		}

		return url;
	}

	public static String deleteHttpPrefix(String url) {
		if (XSimpleText.isEmpty(url)) {
			return url;
		}

		if (url.startsWith("http://")) {
			return url.substring("http://".length());
		}

		if (url.startsWith("https://")) {
			return url.substring("https://".length());
		}

		return url;
	}

	/**
	 * younggao 2013-1-14 获取url中的协议头
	 * 
	 * @param url
	 * @return
	 */
	public static String getSchema(String url) {
		String prefix = "";

		if (!XSimpleText.isEmpty(url)) {
			int pos = url.indexOf("://");
			if (pos > 0) {
				prefix = url.substring(0, pos + 3);
			}
		}

		return prefix;
	}

	/**
	 * 是否中转url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isBrokerUrl(String url) {
		if (url == null || url.length() == 0)
			return false;

		return url.startsWith("page:") || url.startsWith("hotpre:");
	}

	/**
	 * @return True iff the remote url is valid.
	 */
	public static boolean isLocalUrl(String url) {
		if (url == null || url.length() == 0)
			return false;

		return isFileUrl(url);
	}

	/**
	 * 是否深度阅读url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isReadUrl(String url) {
		if (url == null || url.length() == 0)
			return false;

		return url.startsWith("qb://ext/read");
	}

	/**
	 * URL前缀如果是security://，则说明打开该URL时，页面内容将会使用该URL的安全信息（安全信息缓存在数据库中）
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isSecurityCacheUrl(String url) {
		if (url == null || url.length() == 0)
			return false;

		return url.startsWith("security://");
	}

	/**
	 * 是否文件安全检测url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isSecurityFileUrl(String url) {
		if (url == null || url.length() == 0)
			return false;

		return url.startsWith("securityFile://");
	}

	public static boolean isDataBase64Url(String url) {
		return url != null
				&& url.startsWith("data:text/html; charset=utf-8;base64,");
	}

	/**
	 * @return True iff the url is an http: url.
	 */
	public static boolean isHttpUrl(String url) {
		return (null != url) && (url.length() > 6)
				&& url.substring(0, 7).equalsIgnoreCase("http://");
	}

	/**
	 * @return True iff the url is an https: url.
	 */
	public static boolean isHttpsUrl(String url) {
		return (null != url) && (url.length() > 7)
				&& url.substring(0, 8).equalsIgnoreCase("https://");
	}

	public static boolean isWebUrl(String url) {
		return isHttpUrl(url) || isHttpsUrl(url);
	}

	/**
	 * @return True iff the url is an file: url.
	 */
	public static boolean isFileUrl(String url) {
		return (null != url) && (url.length() > 6)
				&& url.substring(0, 7).equalsIgnoreCase("file://");
	}

	/**
	 * @return True iff the url is an market: url.
	 */
	public static boolean isMarketUrl(String url) {
		return (null != url) && (url.length() > 8)
				&& url.substring(0, 9).equalsIgnoreCase("market://");
	}

	/**
	 * @return True iff the url is an rtsp: url.
	 */
	public static boolean isRtspUrl(String url) {
		return (null != url) && (url.length() > 6)
				&& url.substring(0, 7).equalsIgnoreCase("rtsp://");
	}

	/**
	 * @return True iff the url is an rtsp: url.
	 */
	public static boolean isFtpUrl(String url) {
		return (null != url) && (url.length() > 5)
				&& url.substring(0, 6).equalsIgnoreCase("ftp://");
	}

	/**
	 * @return True if the url is a sms url.
	 */
	public static boolean isSmsUrl(String url) {
		return (null != url) && (url.length() > 4)
				&& url.substring(0, 4).equalsIgnoreCase("sms:");
	}

	/**
	 * @return True if the url is a tel url.
	 */
	public static boolean isTelUrl(String url) {
		return (null != url) && (url.length() > 4)
				&& url.substring(0, 4).equalsIgnoreCase("tel:");
	}

	public static String getTelUrl(String url) {
		if (XSimpleText.isEmpty(url))
			return null;

		String telUrl = null;
		if (UrlUtils.isTelUrl(url)) {
			telUrl = url;
		} else {
			String tmpUrl = UrlUtils.getWtaiUrl(url);
			if (tmpUrl != null) {
				telUrl = "tel:" + tmpUrl;
			}
		}
		return telUrl;
	}

	public static String getMailUrl(String url) {
		if (url == null || "".equalsIgnoreCase(url))
			return null;

		String mailUrl = null;
		if (UrlUtils.isMailUrl(url)) {
			mailUrl = url;
		}

		return mailUrl;
	}

	/**
	 * @return True if the url is a mail url.
	 */
	public static boolean isMailUrl(String url) {
		return (null != url) && (url.length() > 7)
				&& url.substring(0, 7).equalsIgnoreCase("mailto:");
	}

	public static boolean isWtaiUrl(String url) {
		return (null != url) && (url.length() > 13)
				&& url.substring(0, 13).equalsIgnoreCase("wtai://wp/mc;");
	}

	public static String getWtaiUrl(String url) {
		if (isWtaiUrl(url)) {
			int pos = url.indexOf("?", 13);
			if (pos != -1) {
				return url.substring(13, pos);
			} else {
				return url.substring(13);
			}
		}
		return null;
	}

	public static boolean isDttpUrl(String url) {
		if (null == url || url.length() == 0)
			return false;

		return url.toLowerCase().startsWith("dttp://");
	}

	public static boolean isTencentUrl(String url) {
		if (null == url || url.length() == 0)
			return false;

		return url.toLowerCase().startsWith("tencent://");
	}

	public static boolean isWebkitUrl(String url) {
		if (null == url || url.length() == 0)
			return false;

		return url.toLowerCase().startsWith("webkit://");
	}

	/**
	 * 删除url中的前缀，如果没有前缀会导致删除http://协议头
	 * 
	 * @param url
	 * @return
	 */
	public static String deletePrefix(String url) {
		if (XSimpleText.isEmpty(url))
			return null;

		if (url.startsWith("page://")) {
			int index = url.indexOf("http://");
			if (index != -1) {
				return url.substring(index);
			}
		}

		int pos = url.indexOf("://");
		if (pos >= 0) {
			url = url.substring(pos + 3);
		}

		return url;
	}

	/**
	 * Resolve the base of a URL.
	 * 
	 * @param base
	 * @param rel
	 * @return The combined absolute URL
	 */
	public static String resolveBase(String base, String rel) {
		if (XSimpleText.isEmpty(rel))
			return base;

		// # 开头的anchor地址，不做处理
		if (rel.startsWith("#"))
			return rel;

		// # 开头的anchor地址，不做处理
		if (UrlUtils.isMobileQQUrl(rel))
			return rel;

		// rel 为有协议头的绝对地址或 JS 指令，不做处理
		if (isRemoteUrl(rel) || isLocalUrl(rel) || isJavascript(rel)) {
			return rel;
		}

		String protocol;
		int i = base.indexOf(':');
		if (i != -1) {
			protocol = base.substring(0, i + 1);
			base = "http:" + base.substring(i + 1);
		} else
			protocol = null;

		URL url;
		try {
			// Bug fixed:7048264
			// url = new URL(new URL(base), rel);

			// 经过这步的处理, 如果是一个path和file都空的url地址，则已经补了斜杠，则对系统来说是一个可用的url了
			url = toURL(base);

			// 进行特殊字符的替换
			base = url.toString();

			String legalBase = prepareUrl(base);
			String legalRel = prepareUrl(rel);

			// 开始进行resolvebase了
			URI baseURI = new URI(legalBase);
			URI destURI = baseURI.resolve(legalRel);
			url = destURI.toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}

		if (protocol != null) {
			base = url.toString();
			i = base.indexOf(':');
			if (i != -1)
				base = base.substring(i + 1);
			base = protocol + base;

			// 如果url中存在../，则清楚掉，标准浏览器做法
			String trueBase = base.replaceAll("\\.\\.\\/", "");
			return trueBase;
		} else {
			return url.toString();
		}
	}

	/**
	 * 去掉协议头 Strip the scheme prefix
	 * 
	 * @param url
	 * @return The URL with no scheme prefix
	 * @exception MalformedURLException
	 */
	public static String stripSchemePrefix(String url) {
		int idx = url.indexOf("://");
		if (idx != -1) {
			return url.substring(idx + 3);
		}
		return url;
	}

	/**
	 * Strip the anchor tag from the URL.
	 * 
	 * @param url
	 * @return The URL with no anchor tag.
	 * @exception MalformedURLException
	 */
	public static String stripAnhcor(String url) throws MalformedURLException {
		int anchorIndex = url.indexOf('#');
		if (anchorIndex != -1) {
			return url.substring(0, anchorIndex);
		}
		return url;
	}

	/**
	 * Strip the query string from a URL.
	 * 
	 * @param url
	 *            The URL to examine.
	 * @return The URL with no query string.
	 * @exception MalformedURLException
	 */
	static public URL stripQuery(URL url) throws MalformedURLException {
		String file = url.getFile();
		int i = file.indexOf("?");
		if (i == -1)
			return url;
		file = file.substring(0, i);
		return new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
	}

	/**
	 * 根据url判断是否后面跟了一个文件名，如果跟了，则认为是下载，否则认为不是下载url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isDownLoadUrl(String url) {
		// 如果url不是http开头也认为是非下载地址
		if (XSimpleText.isEmpty(url) || !url.startsWith("http://"))
			return false;

		String filename = null;

		// 转成UTF-8编码
		String decodedUrl = Uri.decode(url);
		if (!XSimpleText.isEmpty(url)) {
			int queryIndex = decodedUrl.indexOf('?');

			// If there is a query string strip it, same as desktop browsers
			if (queryIndex > 0) {
				decodedUrl = decodedUrl.substring(0, queryIndex);
			}

			if (!decodedUrl.endsWith("/")) {
				int index = decodedUrl.lastIndexOf('/') + 1;
				if (index > 0) {
					filename = decodedUrl.substring(index);
				}
			}
		}

		// Finally, if couldn't get filename from URI, get a generic filename
		if (XSimpleText.isEmpty(filename)) {
			return false;
		}

		return false;

	}

	public static String getDefaultExtensionByMimeType(String mimeType) {
		String extension = null;

		if (TextUtils.isEmpty(mimeType)) {
			return null;
		}

		if (mimeType != null && mimeType.toLowerCase().startsWith("text/")) {
			if (mimeType.equalsIgnoreCase("text/html")) {
				extension = ".html";
			} else {
				extension = ".txt";
			}
		} else if (mimeType != null
				&& mimeType.toLowerCase().startsWith("image/")) {
			if (mimeType.equalsIgnoreCase("image/png")) {
				extension = ".png";
			} else if (mimeType.equalsIgnoreCase("image/jpeg")) {
				extension = ".jpeg";
			} else if (mimeType.equalsIgnoreCase("image/jpg")) {
				extension = ".jpg";
			} else if (mimeType.equalsIgnoreCase("image/gif")) {
				extension = ".gif";
			}
		} else if (mimeType != null
				&& mimeType.toLowerCase().startsWith("video/")) {
			if (mimeType.equalsIgnoreCase("video/flv")) {
				extension = ".flv";
			}
		} else {
			extension = ".bin";
		}

		return extension;
	}

	/**
	 * 获取 url host 比如 http://localhost/images/ab.jpg 提取 host 段： localhost
	 * 
	 * @param url
	 * @return
	 */
	public static String getHost(String url) {
		if (url == null || url.length() == 0)
			return null;

		int start = 0;
		int protoIdx = url.indexOf("://");
		if (protoIdx != -1)
			start = protoIdx + 3;

		String host = null;
		int slashIdx = url.indexOf('/', start);
		if (slashIdx != -1) {
			host = url.substring(start, slashIdx);
		} else {
			// 找不到path，则去找query（问号），针对3g.sina.com.cn?vt=4&pos=101这样的url地址
			int queryIdx = url.indexOf('?', start);

			if (queryIdx != -1) {
				host = url.substring(start, queryIdx);
			} else {
				host = url.substring(start);
			}
		}

		// 去除端口号
		int colonPos = host.indexOf(":");
		if (colonPos >= 0) {
			host = host.substring(0, colonPos);
		}

		return host;
	}

	/**
	 * 获取域名 比如 http://3g.qq.com/images/ab.jpg 提取 :http://3g.qq.com
	 * 
	 * @param url
	 * @return
	 */
	public static String getDomain(String url) {
		if (XSimpleText.isEmpty(url)) {
			return null;
		}
		String host = getHost(url);
		// System.out.println("host : " + host);
		if (host != null && !"".equals(host)) {
			return "http://" + host;
		}
		return null;
	}

	/**
	 * 获取顶级域名 比如 http://3g.qq.com/images/ab.jpg 提取 root 段：qq.com
	 * 
	 * @param url
	 * @return
	 */
	public static String getRootDomain(String url) {
		String rootDomain = null;
		String host = getHost(url);
		// System.out.println("host : " + host);
		if (host != null && !"".equals(host)) {
			char dot = '.';
			String domainSuffix = null;
			int lastIdx = host.lastIndexOf(dot);
			if (lastIdx != -1) {
				domainSuffix = host.substring(lastIdx + 1);
				String hostWithoutSuffix = host.substring(0, lastIdx);

				// 兼容两级域名后缀，如 .com.cn
				if (domainSuffix != null && domainSuffix.equalsIgnoreCase("cn")) {
					lastIdx = hostWithoutSuffix.lastIndexOf(dot);
					if (lastIdx != -1) {
						String mainDomainSuffix = hostWithoutSuffix
								.substring(lastIdx + 1);
						if (mainDomainSuffix != null
								&& mainDomainSuffix.length() > 0) {
							if (mainDomainSuffix.equalsIgnoreCase("com")
									|| mainDomainSuffix.equalsIgnoreCase("edu")
									|| mainDomainSuffix.equalsIgnoreCase("gov")) {
								domainSuffix = mainDomainSuffix.concat(
										String.valueOf(dot)).concat(
										domainSuffix);
								hostWithoutSuffix = hostWithoutSuffix
										.substring(0, lastIdx);
							}
						}
					}
				}

				String domainName = null;
				int domainIdx = hostWithoutSuffix.lastIndexOf(dot);
				if (domainIdx != -1)
					domainName = hostWithoutSuffix.substring(domainIdx + 1);
				else
					domainName = hostWithoutSuffix;

				if (domainName != null && domainName.length() > 0) {
					rootDomain = domainName.concat(String.valueOf(dot)).concat(
							domainSuffix);
				}
			}
		}
		return rootDomain;
	}

	public static boolean isQQCOMDomain(String url) {
		String domain = getRootDomain(url);
		if (!TextUtils.isEmpty(domain)) {
			return domain.equals("qq.com");
		}
		return false;
	}

	public static String getPathAndQuery(String url) {
		if (XSimpleText.isEmpty(url))
			return "";

		try {
			URL urlpaser = new URL(url);
			return urlpaser.getPath() + urlpaser.getQuery();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 获取 url path 比如 http://localhost/images/ab.jpg 提取 path 段： images/ab.jpg
	 * 
	 * @param url
	 * @return
	 */
	public static String getPath(String url) {
		if (url == null || url.length() == 0)
			return null;

		int start = 0;
		int protoIdx = url.indexOf("://");
		if (protoIdx != -1)
			start = protoIdx + 3;

		String path = null;
		int slashIdx = url.indexOf('/', start);
		if (slashIdx != -1) {
			int questionIdx = url.indexOf('?', slashIdx);
			if (questionIdx != -1) {
				path = url.substring(slashIdx + 1, questionIdx);
			} else {
				path = url.substring(slashIdx + 1);
			}
		}
		return path;
	}

	public static String getStringAfterHost(String url) {
		if (url == null || url.length() == 0)
			return null;

		int start = 0;
		int protoIdx = url.indexOf("://");
		if (protoIdx != -1)
			start = protoIdx + 3;

		String path = null;
		int slashIdx = url.indexOf('/', start);
		if (slashIdx != -1) {
			path = url.substring(slashIdx + 1);
		}
		return path;
	}

	/**
	 * 
	 * 大多数JDBC
	 * Driver采用本地编码格式来传输中文字符，例如中文字符“0x4175”会被转成“0x41”和“0x75”进行传输。因此需要对JDBC
	 * Driver返回的字符以及要发给JDBC Driver的字符进行转换 判断string是否是native，以便将native转换成unicode
	 * 
	 * @param content
	 * @return
	 */
	private static boolean isNativeString(String content) {
		if (XSimpleText.isEmpty(content)) {
			return false;
		}
		char[] charData = content.toCharArray();
		int length = charData.length;
		byte b = -1;
		for (int i = 0; i < length; i++) {
			b = (byte) (charData[i] >> 8 & 0xff);
			if (b != 0) {
				return false;
			}
		}
		return true;
	}

	public static boolean urlSupportedByX5CoreSp(final String url) {
		return (isHttpUrl(url) || isHttpsUrl(url)
				|| url.startsWith("about:blank") || isJavascript(url));
	}

	/**
	 * 判断URL是否是一个有效的格式
	 */
	public static boolean isCandidateUrl(final String aUrl) {
		if (aUrl == null || aUrl.length() == 0 || aUrl.startsWith("data:")) {
			return false;
		}
		String url = aUrl.trim();

		Matcher validUrl = VALID_URL.matcher(url);
		Matcher validLocal = VALID_LOCAL_URL.matcher(url);
		Matcher validIp = VALID_IP_ADDRESS.matcher(url);
		Matcher validMtt = VALID_MTT_URL.matcher(url);
		Matcher validQb = VALID_QB_URL.matcher(url);
		Matcher validPay = VALID_PAY_URL.matcher(url);

		return validUrl.find() || validLocal.find() || validIp.find()
				|| validMtt.find() || validQb.find() || validPay.find();
	}

	/**
	 * 判断URL是否是一个有效IP的格式
	 */
	public static boolean isIpUrl(final String aUrl) {
		if (aUrl == null || aUrl.length() == 0) {
			return false;
		}
		String url = aUrl.trim();

		Matcher validIp = VALID_IP_ADDRESS.matcher(url);

		return validIp.find();
	}

	/**
	 * 判断是否是内网IP
	 */
	public static boolean isInnerIP(String ipAddress) {
		if (TextUtils.isEmpty(ipAddress) || !UrlUtils.isIpUrl(ipAddress))
			return false;

		// 私有IP： A类 10.0.0.0 - 10.255.255.255
		// B类 172.16.0.0 - 172.31.255.255
		// C类 192.168.0.0 - 192.168.255.255
		// 当然，还有127这个网段是环回地址

		boolean isInnerIp = false;

		try {
			long ipNum = getIpNum(ipAddress);
			long aBegin = getIpNum("10.0.0.0");
			long aEnd = getIpNum("10.255.255.255");
			long bBegin = getIpNum("172.16.0.0");
			long bEnd = getIpNum("172.31.255.255");
			long cBegin = getIpNum("192.168.0.0");
			long cEnd = getIpNum("192.168.255.255");

			isInnerIp = isInner(ipNum, aBegin, aEnd)
					|| isInner(ipNum, bBegin, bEnd)
					|| isInner(ipNum, cBegin, cEnd)
					|| ipAddress.equals("127.0.0.1")
					|| ipAddress.equals("1.1.1.1");
		} catch (Exception e) {
			// 解析出错时，判定为非内网地址
			e.printStackTrace();
		}

		return isInnerIp;

	}

	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}

	/**
	 * 获取IP数
	 */
	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);

		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	/**
	 * younggao 2013-1-11 是否废弃的协议头
	 */

	public static boolean isDeprecatedSechema(String url) {
		return isWebkitUrl(url) || isMttUrl(url) || isBrokerUrl(url)
				|| isDttpUrl(url) || isTencentUrl(url);
	}

	/**
	 * 判断URL是否有一个有效的协议头
	 */
	public static boolean hasValidProtocal(final String url) {
		if (url == null || url.length() == 0) {
			return false;
		}

		// 2013-06-29, modified by p_edenwang
		// Remove trim and toLowerCase operation for url.
		// Spaces and uppercase will not affect the order of "://" & "."
		// This modification will save 20+ms when loading www.sohu.com/?fr=wap.
		int pos1 = url.indexOf("://");
		int pos2 = url.indexOf('.');

		// 检测"wap.fchgame.com/2/read.jsp?url=http://www.zaobao.com/zg/zg.shtml"类型网址
		if (pos1 > 0 && pos2 > 0 && pos1 > pos2) {
			return false;
		}

		return url.contains("://");
	}

	/**
	 * 根据输入，得到一个有效URL 如果输入无法被解析为一个URL，返回NULL
	 */
	public static String resolvValidUrl(final String aUrl) {
		if (aUrl == null || aUrl.length() == 0) {
			return null;
		}

		String url = aUrl.trim();

		if (isJavascript(url) || UrlUtils.isSpecialUrl(url)) {
			return url;
		} else if (isCandidateUrl(url)) {
			if (hasValidProtocal(url)) {
				return url;
			} else {
				return "http://" + url;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get sms uri from url
	 */
	public static Uri getSmsUriFromUrl(String url) {
		if (isSmsUrl(url)) {
			String smstoUrl = url;
			smstoUrl = smstoUrl.replaceFirst("sms:", "smsto:");

			int index = smstoUrl.indexOf('?');
			if (index > -1) {
				return Uri.parse(smstoUrl.substring(0, index));
			} else {
				return Uri.parse(smstoUrl);
			}
		}

		return null;
	}

	/**
	 * Get sms text from url.
	 */
	public static String getSmsTextFromUrl(String url) {
		if (isSmsUrl(url)) {
			int index = url.indexOf('?');
			if (index > -1 && index < (url.length() - 1)) {
				String[] params = url.substring(index + 1).split("&");
				for (String param : params) {
					if (param.startsWith("body=")) {
						int i = param.indexOf('=');
						if (i > -1 && i < (param.length() - 1)) {
							return param.substring(i + 1);
						}
					}
				}
			} else {
				return null;
			}
		}

		return null;
	}

	/**
	 * 判断当前url是否为支付宝地址。
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isAlipayUrl(String url) {
		boolean result = false;

		if (XSimpleText.isEmpty(url))
			return result;

		url = url.toLowerCase().trim();
		if (url.startsWith("alipay://securitypay/?")) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断当前url是否为银联地址。
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUpPayUrl(String url) {
		if (url == null)
			return false;
		boolean result = false;
		url = url.toLowerCase().trim();
		if (url.startsWith("uppay://")) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断当前url是否为支付宝地址。
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isMobileQQUrl(String url) {
		boolean result = false;
		url = url.toLowerCase().trim();
		if (url.contains("mqqapi://")) {
			result = true;
		}
		return result;
	}

	/**
	 * 内核支持data:text/html协议
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isDataUrl(String url) {
		if (url == null || url.length() == 0)
			return false;

		return url.substring(0, 5).equalsIgnoreCase("data:");
	}

	public static boolean urlSupportedByX5Core(String url) {
		return (isHttpUrl(url) || isHttpsUrl(url) || isFileUrl(url)
				|| isFtpUrl(url) || url.startsWith("about:blank")
				|| isJavascript(url) || isDataUrl(url));
	}

	public static String resolvValidSqlUrl(String url) {
		if (XSimpleText.isEmpty(url)) {
			return url;
		}

		url = url.replaceAll("\'", "\'\'");

		return url;
	}

	/**
	 * 插件相应函数
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isPluginUrl(String url) {
		boolean result = false;
		if (url != null) {
			url = url.toLowerCase().trim();
			if (url.startsWith("qb://player/")
					|| url.startsWith("qb://addon/")
					|| (url.startsWith("qb://app/") && !url
							.startsWith("qb://app/id"))) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 解析键值对
	 * 
	 * @param url
	 * @param key
	 * @return
	 */
	public static String getDataFromQbUrl(String url, String key) {
		String result = "";
		if (url == null) {
			return result;
		}
		url = url.substring(url.indexOf('?') + 1);
		String paramaters[] = url.split("&");
		try {
			for (String param : paramaters) {
				String values[] = param.split("=");
				String keyvalue = values[0];
				if (keyvalue.equalsIgnoreCase(key)) {
					result = values[1];
					break;
				}
			}
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	/**
	 * 替换某个url中的某个key的值
	 * 
	 * @param url
	 *            ，给定url
	 * @param key
	 *            ，指定key值,只处理参数的情况，不处理url常规字符
	 * @param value
	 *            ,替换的值
	 */
	public static String replaceValueByKey(String url, String key, String value) {
		if (XSimpleText.isEmpty(url) || XSimpleText.isEmpty(key))
			return url;

		StringBuilder newUrl = new StringBuilder("");

		// 分隔符，默认添加一个?
		String split = "?";

		int index = url.indexOf('?');
		if (index != -1) {
			newUrl.append(url.substring(0, index));
		} else {
			split = "";
		}
		url = url.substring(index + 1);
		String paramaters[] = url.split("&");

		int length = paramaters.length;
		for (int i = 0; i < length; i++) {
			String values[] = paramaters[i].split("=");
			String keyValue = values[0];
			if (keyValue.equalsIgnoreCase(key) && values.length == 2) {
				values[1] = value;
			}
			newUrl.append(i == 0 ? split : "&").append(values[0]).append("=")
					.append(values.length == 2 ? values[1] : "");
		}
		return newUrl.toString();
	}

	/**
	 * 将url中的中文按照utl8编码方式解码，解码失败则返回原url。
	 * 
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
		if (XSimpleText.isEmpty(str))
			return null;

		String result = str;
		try {
			result = URLDecoder.decode(str, URL_CODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
		return result;
	}

	public static boolean isDomain(String url) {
		boolean ret = false;
		String str = resolvValidUrl(url);
		String lowcaseStr = url.toLowerCase();
		if (null != str) {
			int index = url.lastIndexOf(".");
			if (index > 0)// 一开始就是"."的字符串也不应该是域名
			{
				for (int i = 0; i < SUFFIX.length; i++) {
					if (lowcaseStr.endsWith(SUFFIX[i])) {
						ret = true;
						break;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 获取url中的参数键值对
	 * 
	 * @param url
	 * @return
	 */
	public static ArrayMap<String, String> getUrlParam(String url) {
		String[] nameValues = null;

		if (XSimpleText.isEmpty(url))
			return null;

		ArrayMap<String, String> map = new ArrayMap<String, String>();

		int idx = url.indexOf('?');
		if (idx != -1) {
			String queryString = url.substring(idx + 1);
			nameValues = queryString.split("&");
			if (nameValues != null && nameValues.length > 0) {
				for (String param : nameValues) {
					idx = param.indexOf('=');
					if (idx != -1) {
						String key = param.substring(0, idx);
						// key = key.toLowerCase();
						String value = param.substring(idx + 1, param.length());
						try {
							value = URLDecoder.decode(value, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						map.put(key, value);
					}
				}
			}
		}
		return map;
	}

	/**
	 * younggao 2012-7-31 去除url中的个人信息
	 * 
	 * @param url
	 * @return
	 */
	public static String removeSid(String url) {
		if (XSimpleText.isEmpty(url))
			return "";

		if (!XSimpleText.isEmpty(url)
				&& (url.contains("?sid=") || url.contains("&sid="))) {
			int index = url.indexOf("sid=");
			if (index != -1) {
				String front = url.substring(0, index);
				String end = url.substring(index + 4);

				if (!XSimpleText.isEmpty(end) && end.indexOf("&") > 0) {
					String last = end.substring(end.indexOf("&") + 1);
					url = front + last;
				} else {
					url = url.substring(0, index - 1);
				}
			}
		}
		return url;
	}

	/**
	 * 这个函数专门用于拍立得的二维码扫描的url解析 获取特殊的拍立得url中的参数键值对
	 * 
	 * @param url
	 * @return
	 */
	public static ArrayMap<String, String> getPaiLideUrlParam(String url,
			String start) {
		if (XSimpleText.isEmpty(url))
			return null;

		ArrayMap<String, String> map = new ArrayMap<String, String>();
		int idx = url.indexOf(start);
		if (idx != -1) {
			String queryString = url.substring(idx + start.length(),
					url.length());

			int titleIndex = queryString.indexOf('/');
			String appId = null;
			String title = null;

			if (titleIndex != -1) {
				appId = queryString.substring(0, titleIndex);
				title = queryString.substring(titleIndex + 1,
						queryString.length());
			} else {
				appId = queryString;
			}

			if (!XSimpleText.isEmpty(appId)) {
				String value = appId;
				try {
					value = URLDecoder.decode(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				map.put("appid", value);
			}

			if (!XSimpleText.isEmpty(title)) {
				String value = title;
				try {
					value = URLDecoder.decode(value, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				map.put("title", value);
			}
		}
		return map;
	}

	public static boolean isMttUrl(String url) {
		if (null == url || url.length() == 0)
			return false;

		return url.toLowerCase().startsWith("mtt://");
	}

	/**************************************** move from PunyCode @kenlai ****************************************/

	/*
	 * Copyright (C) 2005-2010 TENCENT Inc.All Rights Reserved.
	 * FileName：PunyCode.java Description： History： 1.0 karreyguan 2011-3-22
	 * Create
	 */

	/* Punycode parameters */
	static int TMIN = 1;
	static int TMAX = 26;
	static int BASE = 36;
	static int INITIAL_N = 128;
	static int INITIAL_BIAS = 72;
	static int DAMP = 700;
	static int SKEW = 38;
	static char DELIMITER = '-';

	/**
	 * Decode a punycoded string.
	 * 
	 * @param input
	 *            Punycode string
	 * @return Unicode string.
	 * @throws Exception
	 */
	public static String punyCodedecode(String input) throws Exception {
		int n = INITIAL_N;
		int i = 0;
		int bias = INITIAL_BIAS;
		StringBuilder output = new StringBuilder();
		int d = input.lastIndexOf(DELIMITER);
		if (d > 0) {
			for (int j = 0; j < d; j++) {
				char c = input.charAt(j);
				if (!isBasic(c)) {
					throw new Exception("BAD_INPUT");
				}
				output.append(c);
			}
			d++;
		} else {
			d = 0;
		}
		while (d < input.length()) {
			int oldi = i;
			int w = 1;
			for (int k = BASE;; k += BASE) {
				if (d == input.length()) {
					throw new Exception("BAD_INPUT");
				}
				int c = input.charAt(d++);
				int digit = codepoint2digit(c);
				if (digit > (Integer.MAX_VALUE - i) / w) {
					throw new Exception("OVERFLOW");
				}
				i = i + digit * w;
				int t;
				if (k <= bias) {
					t = TMIN;
				} else if (k >= bias + TMAX) {
					t = TMAX;
				} else {
					t = k - bias;
				}
				if (digit < t) {
					break;
				}
				w = w * (BASE - t);
			}
			bias = adapt(i - oldi, output.length() + 1, oldi == 0);
			if (i / (output.length() + 1) > Integer.MAX_VALUE - n) {
				throw new Exception("OVERFLOW");
			}
			n = n + i / (output.length() + 1);
			i = i % (output.length() + 1);
			output.insert(i, (char) n);
			i++;
		}
		return output.toString();
	}

	public static int adapt(int delta, int numpoints, boolean first) {
		if (first) {
			delta = delta / DAMP;
		} else {
			delta = delta / 2;
		}
		delta = delta + (delta / numpoints);
		int k = 0;
		while (delta > ((BASE - TMIN) * TMAX) / 2) {
			delta = delta / (BASE - TMIN);
			k = k + BASE;
		}
		return k + ((BASE - TMIN + 1) * delta) / (delta + SKEW);
	}

	public static boolean isBasic(char c) {
		return c < 0x80;
	}

	public static int codepoint2digit(int c) throws Exception {
		if (c - '0' < 10) {
			// '0'..'9' : 26..35
			return c - '0' + 26;
		} else if (c - 'a' < 26) {
			// 'a'..'z' : 0..25
			return c - 'a';
		} else {
			throw new Exception("BAD_INPUT");
		}
	}

	/**************************************** move from CommonUtils @kenlai ****************************************/

	/**
	 * 获得符合统计规则的域名
	 * 
	 * @param url
	 * @return
	 */
	public static String getStatDomain(String url) {
		url = UrlUtils.getHost(UrlUtils.resolvValidUrl(url));
		if (url != null) {
			// 对qq.com的域名，需要对第四级域名中最后的数字进行删除合并处理
			// url = url.replaceAll(":\\d+", "");//删除端口号
			if (url.indexOf("qq.com") != -1) {
				url = url.replaceAll("(?<=(\\D))\\d*(?=(\\.\\w*){3})", "");// 删除三级域名负载均衡的数字
			}
			return url;
		}
		return null;
	}

	public static String getDomainIp(String url) {
		String domainIp = "";
		try {
			URI uri = new URI(url);
			InetAddress address = InetAddress.getByName(uri.getHost());
			domainIp = address.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error error) {
			error.printStackTrace();
		}
		return domainIp;
	}

	/**
	 * 管家体检URL
	 */
	public static boolean isQQSecurityUrl(String url) {
		String host = getHost(url);
		return SECURITYURL.equalsIgnoreCase(host);
	}

}
