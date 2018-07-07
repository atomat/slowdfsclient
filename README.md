# slowdfs client
slowdfs的客户端SDK，Java版（JavaSE-1.7）。

## 使用说明
将slowdfsclient.jar放到相应的lib目录下。
主要方法在ClientUtil类中。
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



## 依赖
* commons-io 2.2
* httpclient 4.5.5
* jackson 1.9.13
