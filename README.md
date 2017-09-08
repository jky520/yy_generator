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
        
    3、自己存入数据库的试图路径去resources/template/menu.sql.vm修改
    4、生成的sql脚本去数据库执行就会自动加入到菜单管理里面去
    5、不同的项目需要修改模板文件头部的部分包引用路劲（这里要根据生成到的项目来改），如下：
        import cn.gzyinyuan.yy.common.utils.PageUtils;
        import cn.gzyinyuan.yy.common.utils.Query;
        import cn.gzyinyuan.yy.common.utils.R;
        
        