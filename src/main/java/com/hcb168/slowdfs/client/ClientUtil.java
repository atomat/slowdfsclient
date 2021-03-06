package com.hcb168.slowdfs.client;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

import com.hcb168.slowdfs.util.HttpUtil;
import com.hcb168.slowdfs.util.MyUtil;

public class ClientUtil {
	/**
	 * 文件上传
	 * 
	 * @param slowdfsHost
	 *            eg. http://127.0.0.1:8080
	 * @param webContextPath
	 *            slowdfs的context path
	 * @param uploadUrl
	 *            eg. /slowdfs/upload
	 * @param groupId
	 *            文件所属组
	 * @param srcPathFile
	 *            待上传文件所在的路径(带文件名的全路径)
	 * @param fileName
	 *            文件名称
	 * @param iConnectTimeout
	 *            ConnectTimeout单位毫秒
	 * @param iSocketTimeout
	 *            SocketTimeout单位毫秒
	 * @return
	 * @throws Exception
	 */
	public static String fileUpload(String slowdfsHost, String webContextPath, String uploadUrl, String groupId,
			String srcPathFile, String fileName, int iConnectTimeout, int iSocketTimeout) throws Exception {
		String hostUrl = slowdfsHost + webContextPath + uploadUrl + "/" + groupId;
		File file = new File(srcPathFile);
		if (!file.exists()) {
			throw new Exception("文件不存在：" + srcPathFile);
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = "";
		try {
			HttpPost httpPost = new HttpPost(hostUrl);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(iConnectTimeout)
					.setSocketTimeout(iSocketTimeout).setConnectionRequestTimeout(1000).build();
			httpPost.setConfig(requestConfig);

			MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
			mEntityBuilder.setCharset(CharsetUtils.get("UTF-8"));// 设置请求的编码格式
			mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 设置浏览器兼容模式
			mEntityBuilder.addBinaryBody("files", file, ContentType.DEFAULT_BINARY, fileName);
			httpPost.setEntity(mEntityBuilder.build());
			response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				result = EntityUtils.toString(resEntity);
				EntityUtils.consume(resEntity);
			} else {
				result = "{\"result\":\"err\",\"msg\":\"statusCode=" + statusCode + "\"}";
			}
		} catch (Exception e) {
			throw e;
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
			response = null;
			httpClient = null;
		}
		return result;
	}

	/**
	 * 
	 * @param slowdfsHost
	 *            slowdfs主机地址
	 * @param uploadUrl
	 *            上传文件接口
	 * @param groupId
	 *            文件所属组
	 * @param srcPathFile
	 *            待上传文件所在的路径(带文件名的全路径)
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws Exception
	 */
	public static String fileUpload(String slowdfsHost, String uploadUrl, String groupId, String srcPathFile,
			String fileName) throws Exception {
		return fileUpload(slowdfsHost, "/slowdfs", uploadUrl, groupId, srcPathFile, fileName, 5 * 1000, 5 * 1000);
	}

	/**
	 * 
	 * @param slowdfsHost
	 *            slowdfs主机地址
	 * @param groupId
	 *            文件所属组
	 * @param srcPathFile
	 *            待上传文件所在的路径(带文件名的全路径)
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws Exception
	 */
	public static String fileUpload(String slowdfsHost, String groupId, String srcPathFile, String fileName)
			throws Exception {
		return fileUpload(slowdfsHost, "/upload", groupId, srcPathFile, fileName);
	}

	/**
	 * 随机选择一个集群节点上传文件
	 * 
	 * @param hosts
	 *            slowdfs集群各节点
	 * @param groupId
	 *            文件所属组
	 * @param srcPathFile
	 *            待上传文件所在的路径(带文件名的全路径)
	 * @param fileName
	 *            文件名
	 * @param iConnectTimeout
	 *            ConnectTimeout单位毫秒
	 * @param iSocketTimeout
	 *            SocketTimeout单位毫秒
	 * @return 文件上传信息
	 * @throws Exception
	 */
	public static Map<String, Object> fileUploadToHosts(String[] hosts, String groupId, String srcPathFile,
			String fileName, int iConnectTimeout, int iSocketTimeout) throws Exception {
		Exception exception = new Exception("failed");

		hosts = MyUtil.randomizeArray(hosts);
		for (String host : hosts) {
			try {
				String jsonResult = fileUpload(host, "/slowdfs", "/upload", groupId, srcPathFile, fileName,
						iConnectTimeout, iSocketTimeout);
				Map<String, Object> map = MyUtil.getMapByJsonStr(jsonResult);
				String result = (String) map.get("result");
				if ("succ".equals(result)) {
					@SuppressWarnings("unchecked")
					ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) map.get("uploadfiles");
					Map<String, Object> mapFileInfo = list.get(0);
					boolean uploadStatus = (boolean) mapFileInfo.get("uploadStatus");
					if (uploadStatus) {
						return mapFileInfo;
					} else {
						throw new Exception((String) mapFileInfo.get("msg"));
					}
				}
			} catch (Exception e) {
				exception = e;
			}
		}
		throw exception;
	}

	/**
	 * 随机选择一个集群节点上传文件
	 * 
	 * @param hosts
	 *            slowdfs集群各节点
	 * @param groupId
	 *            文件所属组
	 * @param srcPathFile
	 *            待上传文件所在的路径(带文件名的全路径)
	 * @param fileName
	 *            文件名
	 * @return 文件上传信息
	 * @throws Exception
	 */
	public static Map<String, Object> fileUploadToHosts(String[] hosts, String groupId, String srcPathFile,
			String fileName) throws Exception {
		return fileUploadToHosts(hosts, groupId, srcPathFile, fileName, 5 * 1000, 5 * 1000);
	}

	/**
	 * 下载文件
	 * 
	 * @param slowdfsHost
	 *            eg. http://127.0.0.1:8080
	 * @param webContextPath
	 *            slowdfs的context path
	 * @param downloadUrl
	 *            文件下载url
	 * @param pathFile
	 *            放置下载文件的目标路径(带文件名的全路径)
	 * @param iConnectTimeout
	 * @param iSocketTimeout
	 * @throws Exception
	 */
	public static String fileDownload(String slowdfsHost, String webContextPath, String downloadUrl, String pathFile,
			int iConnectTimeout, int iSocketTimeout) throws Exception {
		String url = slowdfsHost + webContextPath + downloadUrl;

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpGet httpGet = new HttpGet(url); // 使用Get方法提交
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(iConnectTimeout)
					.setSocketTimeout(iSocketTimeout).setConnectionRequestTimeout(1000).build();
			httpGet.setConfig(requestConfig);

			response = httpClient.execute(httpGet);

			FileOutputStream fos = null;
			try {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
					HttpEntity resEntity = response.getEntity();
					fos = new FileOutputStream(pathFile);
					IOUtils.copy(resEntity.getContent(), fos);
					fos.flush();
					EntityUtils.consume(resEntity);
					return "{\"result\":\"succ\",\"msg\":\"statusCode=" + statusCode + "\"}";
				} else {
					return EntityUtils.toString(response.getEntity());
				}
			} catch (Exception e) {
				throw e;
			} finally {
				IOUtils.closeQuietly(fos);
				fos = null;
			}
		} catch (Exception e) {
			new File(pathFile).delete();
			throw e;
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
			response = null;
			httpClient = null;
		}
	}

	/**
	 * 
	 * @param slowdfsHost
	 *            eg. http://127.0.0.1:8080
	 * @param downloadUrl
	 *            文件下载url
	 * @param pathFile
	 *            放置下载文件的目标路径(带文件名的全路径)
	 * @return
	 * @throws Exception
	 */
	public static String fileDownload(String slowdfsHost, String downloadUrl, String pathFile) throws Exception {
		return fileDownload(slowdfsHost, "/slowdfs", downloadUrl, pathFile, 5 * 1000, 5 * 1000);
	}

	/**
	 * 随机选择一个集群节点下载文件
	 * 
	 * @param hosts
	 *            slowdfs集群各节点
	 * @param downloadUrl
	 *            文件下载url
	 * @param pathFile
	 *            放置下载文件的目标路径(带文件名的全路径)
	 * @param iConnectTimeout
	 *            ConnectTimeout单位毫秒
	 * @param iSocketTimeout
	 *            SocketTimeout单位毫秒
	 * @throws Exception
	 */
	public static void fileDownloadFromHosts(String[] hosts, String downloadUrl, String pathFile, int iConnectTimeout,
			int iSocketTimeout) throws Exception {
		Exception exception = new Exception("failed");

		hosts = MyUtil.randomizeArray(hosts);
		for (String host : hosts) {
			try {
				String jsonResult = fileDownload(host, "/slowdfs", downloadUrl, pathFile, iConnectTimeout,
						iSocketTimeout);
				Map<String, String> map = MyUtil.getMapByJsonStr(jsonResult);
				String result = map.get("result");
				if ("succ".equals(result)) {
					return;
				} else {
					throw new Exception((String) map.get("msg"));
				}
			} catch (Exception e) {
				exception = e;
			}
		}
		throw exception;
	}

	/**
	 * 随机选择一个集群节点下载文件
	 * 
	 * @param hosts
	 *            slowdfs集群各节点
	 * @param downloadUrl
	 *            文件下载url
	 * @param pathFile
	 *            放置下载文件的目标路径(带文件名的全路径)
	 * @throws Exception
	 */
	public static void fileDownloadFromHosts(String[] hosts, String downloadUrl, String pathFile) throws Exception {
		fileDownloadFromHosts(hosts, downloadUrl, pathFile, 5 * 1000, 5 * 1000);
	}

	/**
	 * 删除指定文件，slowdfs集群会自动同步删除操作
	 * 
	 * @param slowdfsHost
	 *            slowdfs服务器地址
	 * @param webContextPath
	 *            slowdfs的context path
	 * @param groupId
	 *            文件所属组
	 * @param fileId
	 *            文件ID
	 * @throws Exception
	 */
	public static void deleteFile(String slowdfsHost, String webContextPath, String groupId, String fileId)
			throws Exception {
		String url = slowdfsHost + webContextPath + "/deletefile/" + groupId + "/" + fileId;
		try {
			String jsonResult = HttpUtil.GetQuickly(url);
			Map<String, String> map = MyUtil.getMapByJsonStr(jsonResult);
			String result = map.get("result");
			if ("succ".equals(result)) {
				return;
			} else {
				throw new Exception((String) map.get("msg"));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除指定文件，slowdfs集群会自动同步删除操作
	 * 
	 * @param slowdfsHost
	 *            slowdfs服务器地址
	 * @param groupId
	 *            文件所属组
	 * @param fileId
	 *            文件ID
	 * @throws Exception
	 */
	public static void deleteFile(String slowdfsHost, String groupId, String fileId) throws Exception {
		deleteFile(slowdfsHost, "/slowdfs", groupId, fileId);
	}
}
