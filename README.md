<font  size=6><strong>AlphaZ</font>


# 前言
>> 本项目使用gradle管理项目，springboot为基础，muti moudles项目。  



# 注意点
* service层的返回数据为统一的结构，除非是暴露给service内调用，否则一律使用统一数据结构返回
* "WEB-INF\resources\pages"目录下无特殊情况，不应放置非jsp文件pages。
* 除非是简单的CRUD，否则都必须使用sql进行查询。创建使用create开头，查询使用get开头，更新使用update开头，删除使用delete。
* 在开发API的时候，不使用session保存状态，API在我们系统设计中应该是无状态的

# <font color=green size=6><strong>怎么发布</font>
使用 <strong>gradle build</strong>命令进行项目打包，之后会在api与app项目<strong>build/libs</strong>生成相应的jar执行包

