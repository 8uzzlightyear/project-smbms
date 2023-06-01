# project-smbms
## A JavaWeb project practice from Kuangshen
## 2023年5月30日
ajax旧密码验证

密码修改 完成

查询用户数量 完成
## 2023年5月31日
用户管理

搜索用户，实现分页

用户编码存在性判断，选择用户列表，添加用户




# 遇到的问题
# 编码问题

index.jsp文件中的中文显示乱码

“设置了过滤器后，在jsp页面内部使用到的字符编码，并不受过滤器的影响。所以，为了保持字符编码的一致性，JSP页面也需要通过指定字符编码的方式来确保输出的中文字符编码正确”

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
```

# 数据库连接问题

## 不同版本的sql包Driver

`Class.forName("com.mysql.cj.jdbc.Driver");`是用于加载MySQL数据库的JDBC驱动程序的语句。在早期版本的MySQL中，需要使用`com.mysql.jdbc.Driver`作为驱动程序的名称，而在新的版本中推荐使用`com.mysql.cj.jdbc.Driver`。其中的`cj`指的是Connector/J，是MySQL官方提供的Java语言实现的JDBC Connector。***新版本不需要显式加载驱动***

# sql语句

```Java
String sql = "select * from smbms.smbms_user where userCode=?";
```
如果不是第一次连接查询，像smbms.smbms_user 这样一定要指定具体的数据库。

# 密码修改无法提交

![[Pasted image 20230530135015.png]]

在去除 js 文件中判断旧密码的代码后点击保存按钮仍无反应，可能的原因是浏览器将原来的 js 文件缓存了，需要清除浏览器缓存。

