package cn.gzyinyuan.yy.controller;

import cn.gzyinyuan.yy.service.SysGeneratorService;
import cn.gzyinyuan.yy.utils.PageUtils;
import cn.gzyinyuan.yy.utils.Query;
import cn.gzyinyuan.yy.utils.R;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * Created by DT人 on 2017/9/8 14:31.
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {

    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(query);
        int total = sysGeneratorService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables");
        tableNames = JSON.parseArray(tables).toArray(tableNames);

       /*byte[] data = sysGeneratorService.generatorCode(tableNames);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"yiyuan.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());*/
        String rs = sysGeneratorService.generatorCode(tableNames);
        response.getWriter().write("<script>alert('生成代码成功！');</script>");
    }
}
