# 通用代码生成器使用注意事项（*）
    1、去application.xml中修改数据库名与登录名与密码，如下
        url: jdbc:mysql://localhost:3306/数据库名?useUnicode=true&characterEncoding=UTF-8
        username: root(数据库用户名)
        password: 123456(密码)
    2、在generator.properties中修改生成代码所在项目根路径（只要到项目目录即可）、以及项目的包名、作者、邮箱、表的前缀
        baseDirect=D:/sts/workspace/yiyuan（根据自己的项目情况来）
        package=cn.gzyinyuan.yy.mudules.mall.shops(表示在该包下生成controller、dao、entity、service,可以根据的需求来改)
        author=自己名字
        email=自己的邮箱
        tablePrefix=t_(默认表名前缀为"t_",例如sys_)
        
    3、需要自己设置视图模块包，如：modules/mall
    4、生成的sql脚本去数据库执行就会自动加入到菜单管理里面去
    5、不同的项目需要修改模板文件头部的部分包引用路劲（这里要根据生成到的项目来改），如下：
        import cn.gzyinyuan.yy.common.utils.PageUtils;
        import cn.gzyinyuan.yy.common.utils.Query;
        import cn.gzyinyuan.yy.common.utils.R;
        
# Session与Cookie基础
    由于http协议是无状态的协议，为了能够记住请求的状态，于是引入了Session
    和Cookie的机制。我们应该有一个很明确的概念，那就是Session是存在于服务
    器端的，在单体式应用中，他是由tomcat管理的，存在于tomcat的内存中，当
    我们为了解决分布式场景中的session共享问题时，引入了redis，其共享内存，
    以及支持key自动过期的特性，非常契合session的特性，我们在企业开发中最
    常用的也就是这种模式。但是只要你愿意，也可以选择存储在JDBC，Mongo中，
    这些，spring都提供了默认的实现，在大多数情况下，我们只需要引入配置即
    可。而Cookie则是存在于客户端，更方便理解的说法，可以说存在于浏览器。
    Cookie并不常用，至少在我不长的web开发生涯中，并没有什么场景需要我过
    多的关注Cookie。http协议允许从服务器返回Response时携带一些Cookie，并
    且同一个域下对Cookie的数量有所限制，之前说过Session的持久化依赖于服务
    端的策略，而Cookie的持久化则是依赖于本地文件。虽然说Cookie并不常用，
    但是有一类特殊的Cookie却是我们需要额外关注的，那便是与Session相关的
    sessionId，他是真正维系客户端和服务端的桥梁。
        
        