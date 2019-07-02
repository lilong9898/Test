package com.lilong.httpstest;

/**
 * http报文格式：
 * https://www.runoob.com/http/http-tutorial.html
 *
 * 从tcp层看http请求过程：
 *      浏览器                                                                                     服务器
 *
 *      (三次握手, 不携带应用层数据)
 *      -----------------tcp flag: SYN, seqNumber = xx, ackNumber = 0，len = 0----------------------->
 *      <----------------tcp flag: SYN + ACK, seqNumber = yy, ackNumber = xx + 1, len = 0-------------
 *      -----------------tcp flag: ACK, seqNumber = xx + 1, ackNumber = yy + 1, len = 0-------------->
 *
 *      (浏览器发出http请求报文，应用层协议是http)
 *      ----tcp flag: PUSH + ACK, seqNumber = xx + 1, ackNumber = yy + 1, len = http请求报文长度------->
 *
 *      (服务器发出http响应报文分段后的各个tcp包，浏览器每收到一个包都会响应一个ack包)
 *      <----------tcp flag: ACK, seqNumber = yy + 1, ackNumber = zz, len = http响应报文的第一段长度-----
 *      -----------tcp flag: ACK, seqNumber = ww, ackNumber = yy + 1 + 目前收到的量, len = 0----------->
 *      <----------tcp flag: ACK, seqNumber = yy + 1 + 目前传输过去的量, ackNumber = zz, len = http响应报文的第二段长度-----
 *      -----------tcp flag: ACK, seqNumber = ww, ackNumber = yy + 1 + 目前收到的量, len = 0----------->
 *      ................................
 *      <----------tcp flag: ACK + PUSH, seqNumber = yy + 1 + 目前传输过去的量, ackNumber = zz, len = http响应报文的最后一段长度-----
 *      -----------tcp flag: ACK, seqNumber = ww, ackNumber = yy + 1 + 目前收到的量, len = 0----------->
 *
 *      (浏览器收到带PUSH位的tcp包，就知道这是http响应报文在tcp层的最后一个包了，会将之前所有tcp包中的应用层数据提取出来，组装成http响应报文)
 *
 * 从tcp和tls层看https请求过程：
 *      浏览器                                                                                      服务器
 *
 *      (三次握手，不携带应用层数据，略)
 *
 *      (后面的tcp包都携带应用层数据，应用层协议是tls，所以按tls层来看，标注的是tls层的handshake protocol)
 *      ----------Client Hello, [浏览器支持的tls版本，支持的加密(对称/非对称)算法，签名算法]----------->
 *      <---------Server Hello, [服务器从浏览器支持的加密算法中，选中的那个算法]-----------------------
 *      <---------Certificate,  [服务器的证书]----------------------------------------------------
 *
 *      (大多数网站使用DH非对称加密算法，比RSA更复杂，
 *       但优点在于，RSA的私钥就是证书私钥，只有一个而且是固定的，一旦泄露，就可被用来破解之前的所有通信
 *       DH在一次通信中用到3个私钥，除了一个固定的证书私钥，还有每次通信的时候浏览器和服务器临时生成的私钥
 *       与之同时生成的是公钥, 假设浏览器临时生成的私钥Cprivate, 临时生成的公钥Cpublic, 服务器临时生成的私钥Sprivate, 临时生成的公钥Spublic，
 *       )
 *      <---------Server Key Exchange，[如果是DH才有这次通信，携带Spublic]---------------------------
 *      <---------Server Hello Done--------------------------------------------------------------
 *
 *      (浏览器验证Certificate中服务器传来的证书：
 *       浏览器用内置的CA机构公钥解密证书中的被加密的签名得到签名1，再用证书中指定的签名算法对证书内容运算得到签名2，
 *       如果签名1==签名2，说明证书的内容没在通信中被改过，
 *       再看证书内容中的网站身份，能确定是真网站，自此证书验证通过
 *
 *       防冒充的原理：
 *       (1) 截获真网站的证书，改动其中的证书内容，会导致签名1!=签名2
 *       (2) 截获真网站的证书，改动其中被加密的签名，会导致它无法解密，因为只有CA机构私钥加密的内容才能被浏览器内置的CA机构公钥解密
 *       (3) 向CA机构申请一张冒充的证书，证书里的网站身份无法冒充，因为CA机构会审核
 *      )
 *
 *       (浏览器生成对称加密密钥，
 *       如果是RSA，浏览器生成一个对称加密密钥K，用证书中的公钥加密后放入Client Key Exchange中
 *       如果是DH，浏览器根据Cprivate和Spublic生成K，再将Cpublic用证书中的公钥加密后放入Client Key Exchange中)
 *       ----------Client Key Exchange, [如果是RSA，encrypted K，如果是DH，encrypted Cpublic]------------>
 *
 *       (如果是RSA，服务器根据encrypted K用证书的私钥解密得到K
 *        如果是DH，服务器根据encrypted Cpublic用证书的私钥解密得到Cpublic, 再联合Sprivate生成K，
 *        也就是说K没有出现在通信中，因此证书私钥即使泄露，也无从得知当时的Sprivate和Cprivate，还是得不到K
 *       )
 *
 *       ----------Change Cipher Spec, [告诉服务器我方之后的通信都用K加密，进入加密通信阶段]------------------->
 *       ----------Encrypted Handshake Message, [用K加密的，浏览器看到的所有通信内容的摘要]------------------->
 *       (服务器校验这个摘要)
 *
 *       <---------Change Cipher Spec, [告诉浏览器我方之后的通信都用K加密，进入加密通信阶段]--------------------
 *       <---------Encrypted Handshake Message, [用K加密的，服务器看到的所有通信内容的摘要]--------------------
 *       (浏览器校验这个摘要)
 *
 *       (两边都校验成功，tls handshake成功，https连接建立)
 *       ----------Application Data, [tls层的encrypted application data字段里就是K加密后的http请求报文]------>
 *       <---------Application Data, [tls层的encrypted application data字段里就是K加密后的http响应报文]-------
 * */
public class Doc {
}
