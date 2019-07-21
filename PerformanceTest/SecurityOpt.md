### 安全
#### 代码保护
- 混淆
- 硬编码的密码放在native代码中
#### 网络通信
- 使用HTTPS
- webview要限制网页所拥有的，对文件/javascript的访问权限
#### 加密
- 对称加密使用AES 
#### 组件
- 减少组件的可见性(exported=false/不设置intent filter)
- 设置权限(保护级别为signature，只有同签名的应用才可访问)
- 使用LocalBroadcast
- 尽量避免使用隐式intent，避免被持相同intent filter的其它应用收到
#### 文本输入
- 禁止拷贝文字
- 禁止截屏/录屏(设置WindowManager.LayoutParams.FLAG_SECURE)
#### 存储
- 尽量在/data/data/包名下存储，这是私有目录
- 必须在sd卡存储的数据，敏感的要加密
- 数据库使用参数化查询(SQLiteStatement)，避免SQL注入
#### 日志
- Log在release版本上减少输出
#### Intent
- 对其他应用传来的Intent进行数据校验，防御恶意数据(空数据/异常数据/路径逃逸的数据)
- Intent的URI不应包含敏感数据，会被logcat打印出来，应放在Intent的extra里
#### 类加载
- 从外部加载的类(补丁/插件)，需要校验这些类没有被篡改
