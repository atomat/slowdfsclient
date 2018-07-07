# slowdfs client
slowdfs的客户端SDK，Java版（JavaSE-1.7）。

## 使用说明
将slowdfsclient.jar放到相应的lib目录下。
常用方法是ClientUtil类中的fileUploadToHosts和fileDownloadFromHosts。
```
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
			String fileName)
```
```
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
	public static void fileDownloadFromHosts(String[] hosts, String downloadUrl, String pathFile)
```
代码样例
```
public static void main(String[] args) throws Exception {
		String[] hosts = new String[] { "http://127.0.0.1:8080", "http://127.0.0.1:18080" };
		Map<String, Object> map = fileUploadToHosts(hosts, "public", "E:\\tmp/summer.zip", "summer.zip");
		System.out.println(map);
		ClientUtil.fileDownloadFromHosts(hosts,"/download/public/96b12b3f5a545865ee7a4e338d494914.zi","e:/tmp/abc.zip");
	}
```
******
上述方法调用的是：
```
/**
	 * 文件上传
	 * 
	 * @param slowdfsHost
	 *            eg. http://127.0.0.1:8080
	 * @param url
	 *            eg. /slowdfs/upload
	 * @param groupId
	 *            文件所属组
	 * @param srcPathFile
	 *            待上传文件所在的路径(带文件名的全路径)
	 * @param fileName
	 *            文件名称
	 * @param iConnectTimeout
	 * @param iSocketTimeout
	 * @return
	 * @throws Exception
	 */
	public static String fileUpload(String slowdfsHost, String url, String groupId, String srcPathFile, String fileName,int iConnectTimeout, int iSocketTimeout) 
```
返回值说明：
```
{
	"result": "succ",  // 文件上传请求的成功失败标志。succ-成功，err-失败
	"uploadfiles": [{  // 文件上传结果，数组形式，支持上传多个文件
		"groupId": "default",  // 
		"originalFileName": "testa",
		"prefix": "",
		"fileSize": 1001014,
		"fileMD5Value": "65b58ce5a3803e69f3d548c86d65bf35",
		"fileId": "a05b46178c89e988d902de9f7312fc20",
		"fileName": "a05b46178c89e988d902de9f7312fc20",
		"downloadUrl": "/download/default/a05b46178c89e988d902de9f7312fc20",
		"storePathFile": "/6/b5/65b58ce5a3803e69f3d548c86d65bf35",
		"dateTime": "20180707 23:58:13.445 +0800",
		"uploadStatus": true,
		"msg": ""
	}]
}
```

```
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
	public static String fileDownload(String slowdfsHost, String webContextPath, String downloadUrl, String pathFile,int iConnectTimeout, int iSocketTimeout) 
```

## 依赖
* commons-io 2.2
* httpclient 4.5.5
* jackson 1.9.13
