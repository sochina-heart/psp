package com.sochina.util;

import com.sochina.entity.SysJob;
import org.quartz.JobExecutionContext;

public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
