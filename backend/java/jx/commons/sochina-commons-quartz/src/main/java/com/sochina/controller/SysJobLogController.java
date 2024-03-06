package com.sochina.controller;

import com.sochina.base.utils.web.AjaxResult;
import com.sochina.entity.SysJobLog;
import com.sochina.mvc.utils.poi.ExcelUtil;
import com.sochina.service.ISysJobLogService;
import com.sochina.util.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/monitor/jobLog")
public class SysJobLogController extends BaseController {
    private final ISysJobLogService jobLogService;

    public SysJobLogController(ISysJobLogService iSysJobLogService) {
        this.jobLogService = iSysJobLogService;
    }

    /**
     * 查询定时任务调度日志列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysJobLog sysJobLog) {
        startPage();
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        return getDataTable(list);
    }

    /**
     * 导出定时任务调度日志列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysJobLog sysJobLog) {
        List<SysJobLog> list = jobLogService.selectJobLogList(sysJobLog);
        ExcelUtil<SysJobLog> util = new ExcelUtil<SysJobLog>(SysJobLog.class);
        util.exportExcel(response, list, "调度日志");
    }

    /**
     * 根据调度编号获取详细信息
     */
    @GetMapping(value = "/{jobLogId}")
    public AjaxResult getInfo(@PathVariable Long jobLogId) {
        return success(jobLogService.selectJobLogById(jobLogId));
    }

    /**
     * 删除定时任务调度日志
     */
    @DeleteMapping("/{jobLogIds}")
    public AjaxResult remove(@PathVariable Long[] jobLogIds) {
        return AjaxResult.toAjax(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 清空定时任务调度日志
     */
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        jobLogService.cleanJobLog();
        return success();
    }
}
