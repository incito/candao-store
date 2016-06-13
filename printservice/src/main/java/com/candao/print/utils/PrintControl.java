package com.candao.print.utils;

/**
 * 打印控制类
 * 
 * @author 004
 *
 */
public class PrintControl {

	public static final int STATUS_OK = 1;
	public static final int STATUS_STOPPRINT = 2;
	public static final int STATUS_CLEAR_STOPPRINT_END = 3;
	public static final int STATUS_COVEROPEN = 4;
	public static final int STATUS_PAPEREND = 5;
	public static final int STATUS_CUTTER_ERROR = 6;
	public static final int STATUS_PAPERNEAREND = 7;
	public static final int STATUS_PRINTING = 8;
	public static final int STATUS_PRINT_DONE = 9;
	public static final int STATUS_PRINT_UNDONE = 10;
	public static final int STATUS_OFFLINE = 11;
	public static final int STATUS_ABNORMAL = 12;
	public static final int STATUS_DEVTIMEOUT = 13;
	public static final int STATUS_PRNCTRL = 14;
	public static final int STATUS_NOPRNCTRL = 15;

	public static final int OPERATION_OK = 1;
	public static final int OPERATION_ERROR = 0;
	public static final int OPERATION_TIMEOUT = -1;
	
	public static void main(String[] args) {
//		new PrintControl().printerIsReady(iDevNum, iTimeOut);
	}

	public int printerIsReady(int iDevNum, int iTimeOut) {

        int iStartTime = 0;
        int iEndTime = 0;
        int iState = -1;
        int iReadNum = 0;

        if (iTimeOut < 1000)
        {
            iTimeOut = 1000;
        }
        long timeMillis = System.currentTimeMillis();
        iStartTime = GetTickCount();
        iEndTime = iStartTime + iTimeOut;

        while (iStartTime < iEndTime)
        {
        //Send check command (1D 61 FF)
        //Chinese note:发送查询指令
            //Get the printer's status
            //Chinese note:获取设备状态
            iState = ReadDeviceStatus(iDevNum, new byte[2]);

            switch (iState)
            {
                case STATUS_OFFLINE:
                {
                    iReadNum++;

                    if (iReadNum > 2)
                    {
                        return iState;
                    }
                    else
                    {
                        continue;
                    }
                }
                case STATUS_ABNORMAL:
                case STATUS_OK:
                    return iState;
                case STATUS_COVEROPEN:
                case STATUS_PAPEREND:
                case STATUS_CUTTER_ERROR:
                    iStartTime = GetTickCount();
                    continue;
                case STATUS_STOPPRINT:
                    //Clearing stop printing status data bit (1B 41)
                    //Chinese note:清除禁止打印状态
                    if (ClearStopPrint(iDevNum) == OPERATION_ERROR)
                    {
                        return STATUS_OFFLINE;
                    }

                    iStartTime = GetTickCount();
                    continue;
                case STATUS_CLEAR_STOPPRINT_END:
                    //Clearing stop printing end status data bit (10 06 07 08 04)
                    //Chinese note:清除已完成清除禁止打印状态标志
                    if (ClearStopPrintEnd(iDevNum) == OPERATION_ERROR)
                    {
                        return STATUS_OFFLINE;
                    }

                    iStartTime = GetTickCount();
                    continue;
                case STATUS_PRINT_UNDONE:
                    //Reset printer status(10 06 07 08 08)
                    //Chinese note:复位打印机状态
                    if (ResetDevStatus(iDevNum) == OPERATION_ERROR)
                    {
                        return STATUS_OFFLINE;
                    }

                    iStartTime = GetTickCount();
                    continue;
                case STATUS_PRINTING:
                    return STATUS_OK;
                default:
                    return STATUS_ABNORMAL;
            }
        }

        //Time out
        if (iStartTime >= iEndTime)
        {
            return iState;
        }

        return STATUS_OK;
	}

	public int ReadDeviceStatus(int iDevNum, byte[] cReadBuf) {
		boolean bIsAbnormity, bIsPaperEnd, bIsPaperNearEnd, bIsCoverOpen, bIsCutError, bIsPrinting, bIsStopPrint,
				bIsClearStopPrintEnd, bIsPrintUndone;// IsPaperNearEndIsPrinting

//		byte[] cReadBuf = new byte[100];
		int iReadLen = cReadBuf.length;

		// Read printer status data
		// Chinese note: 读打印机状态数据
//		for (byte p : cReadBuf) {
//			if (ByReadASBStatus(iDevNum, p, 100, iReadLen) != 0) {
//				return STATUS_OFFLINE;
//			}
//		}

		if (iReadLen <= 0) {
			return STATUS_OFFLINE;
		}

		if (iReadLen % 4 != 0) {
			return STATUS_ABNORMAL;
		}

//		bIsPaperEnd := cReadBuf[2 + ((dReadLen div 4 - 1) * 4)] and $0C;
//		bIsPaperNearEnd := cReadBuf[2 + ((dReadLen div 4 - 1) * 4)] And $03;
//		bIsCoverOpen := cReadBuf[0 + ((dReadLen div 4 - 1) * 4)] And $20;
//		bIsCutError := cReadBuf[1 + ((dReadLen div 4 - 1) * 4)] And $08;
//		bIsAbnormity := cReadBuf[0 + ((dReadLen div 4 - 1) * 4)] And $10;
//		bIsPrinting := cReadBuf[2 + ((dReadLen div 4 - 1) * 4)] And $40;
//		bIsStopPrint := cReadBuf[3 + ((dReadLen div 4 - 1) * 4)] And $40;
//		bIsClearStopPrintEnd := cReadBuf[3 + ((dReadLen div 4 - 1) * 4)] And $20;
//		bIsPrintUndone := cReadBuf[2 + ((dReadLen div 4 - 1) * 4)] And $20;

		
		// Parse status
		// Chinese note: 解析具体状态
		if ((cReadBuf[0 + ((iReadLen / 4 - 1) * 4)] & 0x10) == 0) {
			bIsAbnormity = true;
		} else {
			bIsAbnormity = false;
		}

		if ((cReadBuf[2 + ((iReadLen / 4 - 1) * 4)] & 0x0C) == 0) {
			bIsPaperEnd = false;
		} else {
			bIsPaperEnd = true;
		}

		if ((cReadBuf[2 + ((iReadLen / 4 - 1) * 4)] & 0x03) == 0) {
			bIsPaperNearEnd = false;
		} else {
			bIsPaperNearEnd = true;
		}

		if ((cReadBuf[0 + ((iReadLen / 4 - 1) * 4)] & 0x20) == 0) {
			bIsCoverOpen = false;
		} else {
			bIsCoverOpen = true;
		}

		if ((cReadBuf[1 + ((iReadLen / 4 - 1) * 4)] & 0x08) == 0) {
			bIsCutError = false;
		} else {
			bIsCutError = true;
		}

		if ((cReadBuf[3 + ((iReadLen / 4 - 1) * 4)] & 0x40) == 0) {
			bIsStopPrint = false;
		} else {
			bIsStopPrint = true;
		}

		if ((cReadBuf[2 + ((iReadLen / 4 - 1) * 4)] & 0x40) == 0) {
			bIsPrinting = false;
		} else {
			bIsPrinting = true;
		}

		if ((cReadBuf[3 + ((iReadLen / 4 - 1) * 4)] & 0x20) == 0) {
			bIsClearStopPrintEnd = false;
		} else {
			bIsClearStopPrintEnd = true;
		}

		if ((cReadBuf[2 + ((iReadLen / 4 - 1) * 4)] & 0x20) == 0) {
			bIsPrintUndone = false;
		} else {
			bIsPrintUndone = true;
		}

		// Status is abnormal
		// Chinese note: 状态数据异常
		if (bIsAbnormity == true) {
			return STATUS_ABNORMAL;
		}

		// Paper is end
		// Chinese note: 缺纸
		if (bIsPaperEnd == true) {
			return STATUS_PAPEREND;
		}

		// Cover is opened
		// Chinese note: 上盖打开
		if (bIsCoverOpen == true) {
			return STATUS_COVEROPEN;
		}

		// Cutter is error
		// Chinese note: 切刀错误
		if (bIsCutError == true) {
			return STATUS_CUTTER_ERROR;
		}

		// Printer is stopped printing
		// Chinese note: 打印机禁止打印
		if (bIsStopPrint == true) {
			return STATUS_STOPPRINT;
		}

		// Clear status flag of "Printer is stopped printing" completely
		// Chinese note: 清除打印机禁止打印状态完成状态
		if (bIsClearStopPrintEnd == true) {
			return STATUS_CLEAR_STOPPRINT_END;
		}

		// Job printing is undone
		// Chinese note: 打印作业未完成
		if (bIsPrintUndone == true) {
			return STATUS_PRINT_UNDONE;
		}

		// Job printing is printing
		// Chinese note: 打印作业正在打印
		if (bIsPrinting == true) {
			return STATUS_PRINTING;
		}

		return STATUS_OK;
	}

	/*******************************************************************
	 ** Function Name: ClearStopPrintEnd(int iDevNum) Function: Clear status flag
	 * of clearing status flag of "Printer is stopped printing" completely.
	 * Chinese note: 清除已完成清除禁止打印状态标志 Parameter: iDevNum: Discriminating number
	 * of device. Chinese note: 设备编号
	 **
	 ** Return Value: Success : OPERATION_OK Failure : OPERATION_ERROR
	 ********************************************************************/
	private int ClearStopPrint(int iDevNum) {
		byte[] cTempBuf = new byte[5];
		int iWriteLen = -1;

		cTempBuf[0] = 0x1b;
		cTempBuf[1] = 0x41;

		if (ByWritePort(iDevNum, cTempBuf, 2, iWriteLen) != 0) {
			return OPERATION_ERROR;
		}

		if (iWriteLen != 2) {
			return OPERATION_ERROR;
		}

		return OPERATION_OK;
	}

	/*******************************************************************
	 ** Function Name: ResetDevStatus(int iDevNum) Function: Reset printer status
	 * Chinese note: 下发设备状态复位指令 Parameter: iDevNum: Discriminating number of
	 * device. Chinese note: 设备编号
	 **
	 ** Return Value: Success : OPERATION_OK Failure : OPERATION_ERROR
	 ********************************************************************/
	private int ResetDevStatus(int iDevNum) {
		byte[] cTempBuf = new byte[6];
		int iWriteLen = -1;

		cTempBuf[0] = 0x10;
		cTempBuf[1] = 0x06;
		cTempBuf[2] = 0x07;
		cTempBuf[3] = 0x08;
		cTempBuf[4] = 0x08;

		if (ByWritePort(iDevNum, cTempBuf, 5, iWriteLen) != 0) {
			return OPERATION_ERROR;
		}

		if (iWriteLen != 5) {
			return OPERATION_ERROR;
		}

		return OPERATION_OK;
	}

	/*******************************************************************
	 ** Function Name: ClearStopPrint(int iDevNum) Function: Clear status flag of
	 * "Printer is stopped printing". Chinese note: 清除禁止打印状态标志 Parameter:
	 ** iDevNum: Discriminating number of device. Chinese note: 设备编号
	 **
	 ** Return Value: Success : OPERATION_OK Failure : OPERATION_ERROR
	 ********************************************************************/
	private int ClearStopPrintEnd(int iDevNum) {
		byte[] cTempBuf = new byte[6];
		int iWriteLen = -1;

		cTempBuf[0] = 0x10;
		cTempBuf[1] = 0x06;
		cTempBuf[2] = 0x07;
		cTempBuf[3] = 0x08;
		cTempBuf[4] = 0x04;

		if (ByWritePort(iDevNum, cTempBuf, 5, iWriteLen) != 0) {
			return OPERATION_ERROR;
		}

		if (iWriteLen != 5) {
			return OPERATION_ERROR;
		}

		return OPERATION_OK;
	}

	public native int ByWritePort(int iDevNum, byte[] cTempBuf, int i, int iWriteLen);

	public native int ByReadASBStatus(int iDevNum, byte p, int i, int iReadLen);

	public native int GetTickCount();
}
