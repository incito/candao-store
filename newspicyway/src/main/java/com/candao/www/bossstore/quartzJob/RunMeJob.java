package com.candao.www.bossstore.quartzJob;

public class RunMeJob {
	private RunMeTask runMeTask;
    private RunCurrMonTask runCurrMonTask;
    private RunFlowDataTask runFlowDataTask;
    private RunTableTask runTableTask;
    private RunTableDetailTask runTableDetailTask;
    private RunOnDutyManagerTask runOnDutyManagerTask;
    private RunServiceTask serviceTask;
    private RunTableOrderTask tableOrderTask;
    
	public void setRunMeTask(RunMeTask runMeTask) {
		this.runMeTask = runMeTask;
	}

    public void setRunCurrMonTask(RunCurrMonTask runCurrMonTask) {
        this.runCurrMonTask = runCurrMonTask;
    }

    public void setRunFlowDataTask(RunFlowDataTask runFlowDataTask) {
        this.runFlowDataTask = runFlowDataTask;
    }

    public void setRunTableTask(RunTableTask runTableTask) {
        this.runTableTask = runTableTask;
    }

    public void setRunTableDetailTask(RunTableDetailTask runTableDetailTask) {
        this.runTableDetailTask = runTableDetailTask;
    }

    public void setRunOnDutyManagerTask(RunOnDutyManagerTask runOnDutyManagerTask) {
        this.runOnDutyManagerTask = runOnDutyManagerTask;
    }
    
    public void setServiceTask(RunServiceTask serviceTask) {
		this.serviceTask = serviceTask;
	}

	public void setTableOrderTask(RunTableOrderTask tableOrderTask) {
		this.tableOrderTask = tableOrderTask;
	}

	protected void executeInternal(){
		System.out.println("------------------this is ok-------------");
		try {
			runMeTask.cuttingpayment();
            runCurrMonTask.cuttingpayment();
            runFlowDataTask.cuttingpayment();
            runTableTask.cuttingpayment();
            runTableDetailTask.cuttingpayment();
            runOnDutyManagerTask.cuttingpayment();
            serviceTask.cuttingpayment();
            tableOrderTask.cuttingpayment();
		} catch (Exception e) {
			e.printStackTrace();
        }

	}
}