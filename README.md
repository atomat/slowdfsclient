# slowdfs client
slowdfs的客户端SDK，Java版（JavaSE-1.7）及以上。

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
	 * @param iConnectTimeout
	 *            ConnectTimeout单位毫秒
	 * @param iSocketTimeout
	 *            SocketTimeout单位毫秒
	 * @return 文件上传信息
	 * @throws Exception
	 */
	public static Map<String, Object> fileUploadToHosts(String[] hosts, String groupId, String srcPathFile,
			String fileName, int iConnectTimeout, int iSocketTimeout) throws Exception
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
	 * @param iConnectTimeout
	 *            ConnectTimeout单位毫秒
	 * @param iSocketTimeout
	 *            SocketTimeout单位毫秒
	 * @throws Exception
	 */
	public static void fileDownloadFromHosts(String[] hosts, String downloadUrl, String pathFile, int iConnectTimeout,
			int iSocketTimeout) throws Exception
```
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
```
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
	public static void deleteFile(String slowdfsHost, String groupId, String fileId)
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
上传文件返回值说明：
```
{
	"result": "succ",  // 文件上传请求的成功失败标志。succ-成功，err-失败
	"uploadfiles": [{  // 文件上传结果，数组形式，支持上传多个文件
		"groupId": "default",  // 文件所属组
		"originalFileName": "testa.zip",  // 原始文件名
		"prefix": "zip",  // 文件名后缀
		"fileSize": 1001014,  // 文件大小
		"fileMD5Value": "65b58ce5a3803e69f3d548c86d65bf35",  // 文件内容的MD5值
		"fileId": "a05b46178c89e988d902de9f7312fc20",  // 文件ID（全局唯一）
		"fileName": "a05b46178c89e988d902de9f7312fc20.zip", // 在服务器端的文件名
		"downloadUrl": "/download/default/a05b46178c89e988d902de9f7312fc20.zip",  // 文件下载URL
		"storePathFile": "/6/b5/65b58ce5a3803e69f3d548c86d65bf35.zip",  // 文件在服务器端的存储路径
		"dateTime": "20180707 23:58:13.445 +0800",  // 文件上传时间
		"uploadStatus": true,  // 文件上传结果，boolean类型。true-上传成功，false-上传失败
		"msg": ""  // 当文件上传失败时，msg中存放的是失败原因
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
