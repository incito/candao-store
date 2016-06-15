package com.candao.print.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 打印控制类
 *
 * @author 004
 */
public class PrintControl {

    /**
     * 正常
     */
    public static final int STATUS_OK = 1;
    /**
     * 禁止打印
     */
    public static final int STATUS_STOPPRINT = 2;
    /**
     * 清除禁止打印完成
     */
    public static final int STATUS_CLEAR_STOPPRINT_END = 3;
    /**
     * 上盖打开
     */
    public static final int STATUS_COVEROPEN = 4;
    /**
     * 缺纸
     */
    public static final int STATUS_PAPEREND = 5;
    /**
     * 切刀有错
     */
    public static final int STATUS_CUTTER_ERROR = 6;
    /**
     * 纸将尽
     */
    public static final int STATUS_PAPERNEAREND = 7;
    /**
     * 正在打印
     */
    public static final int STATUS_PRINTING = 8;
    /**
     * 打印完成
     */
    public static final int STATUS_PRINT_DONE = 9;
    /**
     * 打印未正常完成
     */
    public static final int STATUS_PRINT_UNDONE = 10;
    /**
     * 离线
     */
    public static final int STATUS_OFFLINE = 11;
    /**
     * 状态数据异常
     */
    public static final int STATUS_ABNORMAL = 12;
    /**
     * 超时
     */
    public static final int STATUS_DEVTIMEOUT = 13;
    public static final int STATUS_PRNCTRL = 14;
    public static final int STATUS_NOPRNCTRL = 15;
    /**
     * 未连接
     */
    public static final int STATUS_DISCONNECTE = 16;

    /**
     * 指令正常执行完成
     */
    public static final int OPERATION_OK = 1;
    /**
     * 指令执行出错
     */
    public static final int OPERATION_ERROR = 0;
    /**
     *
     */
    public static final int OPERATION_TIMEOUT = -1;

    public static int printerIsReady(int iTimeOut, OutputStream socketOut, InputStream inputStream) throws IOException {

        long iStartTime = 0;
        long iEndTime = 0;
        int iState = -1;
        int iReadNum = 0;

        if (iTimeOut < 1000) {
            iTimeOut = 1000;
        }

        iStartTime = GetTickCount();
        iEndTime = iStartTime + iTimeOut;

        // Send check command (1D 61 FF)
        // Chinese note:发送查询指令
        socketOut.write(new byte[]{0x1D, 0x61, (byte) 0xFF});
        while (iStartTime < iEndTime) {
            // Get the printer's status
            // Chinese note:获取设备状态
            byte[] cReadBuf = new byte[inputStream.available()];
            inputStream.read(cReadBuf);
            iState = ReadDeviceStatus(cReadBuf);

            switch (iState) {
                case STATUS_OFFLINE: {
                    iReadNum++;

                    if (iReadNum > 2) {
                        return iState;
                    } else {
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
                    // Clearing stop printing status data bit (1B 41)
                    // Chinese note:清除禁止打印状态
                    if (ClearStopPrint(socketOut) == OPERATION_ERROR) {
                        return STATUS_OFFLINE;
                    }

                    iStartTime = GetTickCount();
                    continue;
                case STATUS_CLEAR_STOPPRINT_END:
                    // Clearing stop printing end status data bit (10 06 07 08 04)
                    // Chinese note:清除已完成清除禁止打印状态标志
                    if (ClearStopPrintEnd(socketOut) == OPERATION_ERROR) {
                        return STATUS_OFFLINE;
                    }

                    iStartTime = GetTickCount();
                    continue;
                case STATUS_PRINT_UNDONE:
                    // Reset printer status(10 06 07 08 08)
                    // Chinese note:复位打印机状态
                    if (ResetDevStatus(socketOut) == OPERATION_ERROR) {
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

        // Time out
        if (iStartTime >= iEndTime) {
            return iState;
        }

        return STATUS_OK;
    }

    private static int ReadDeviceStatus(byte[] cReadBuf) throws IOException {
        boolean bIsAbnormity, bIsPaperEnd, bIsPaperNearEnd, bIsCoverOpen, bIsCutError, bIsPrinting, bIsStopPrint,
                bIsClearStopPrintEnd, bIsPrintUndone;// IsPaperNearEndIsPrinting

        // byte[] cReadBuf = new byte[100];

        int iReadLen = cReadBuf.length;

        // Read printer status data
        // Chinese note: 读打印机状态数据
        // for (byte p : cReadBuf) {
        // if (ByReadASBStatus(iDevNum, p, 100, iReadLen) != 0) {
        // return STATUS_OFFLINE;
        // }
        // }

        if (iReadLen <= 0) {
            return STATUS_OFFLINE;
        }

        if (iReadLen % 4 != 0) {
            return STATUS_ABNORMAL;
        }

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
     * * Function Name: CheckJob Function: Check result of printing job Chinese
     * note:查询打印作业打印结果 Parameter: iDevNum: Discriminating number of device.
     * Chinese note:设备编号 iTimeOut: Set time out of operation Chinese note:操作超时时间
     * * Return Value: Success : STATUS_OK Failure : STATUS_OFFLINE,
     * STATUS_ABNAMAL, etc. Remark: iTimeOut parameter setting depend on
     * printing speed and printing length. Chinese note:
     * iTimeOut参数值的设置与打印速度及票面的打印长度有关。
     *
     * @param socketOut
     * @param inputStream
     * @throws IOException
     ********************************************************************/
    public static int CheckJob(int iTimeOut, InputStream inputStream) throws IOException {
        if (iTimeOut < 1000) {
            iTimeOut = 1000;
        }
        // 定义变量并初始化
        int iReturnValue = STATUS_PRINT_DONE; // 函数返回值
        long iStartTime = 0; // 计时起始时间
        long iEndTime = 0; // 计时终止时间
        // byte[] cReadBuf = new byte[100]; //接收缓冲区
        int iReadLen = 0; // 读取数据长度
        boolean bIsAbnormal = false; // 出错标志
        boolean bIsPrinting = false; // 打印过程
        boolean bIsPrintingStart = false; // 启动打印标志
        boolean bIsStopPrint = false; // 禁止打印标志
        boolean bIsPrintUndone = false; // 打印未完成标志
        int ii = 0; // 循环变量

        int iReadValue = 0; // 临时变量，用于记录ByReadASBStatus的返回值

        // Timeout operation
        // Chinese note:超时操作
        iStartTime = GetTickCount();
        iEndTime = iStartTime + iTimeOut;
        while (iStartTime < iEndTime) {
            // Read device automatic status back data
            // Chinese note:读取自动状态返回数据

            byte[] cReadBuf = new byte[inputStream.available()];
            inputStream.read(cReadBuf);
            iReadLen = cReadBuf.length;
            iReadValue = ReadDeviceStatus(cReadBuf);

            // for (ii = 0; ii < 100; ii++)
            // {
            // cReadBuf[ii] = 0;
            // }
            //
            // fixed (byte* p = cReadBuf)
            // {
            // iReadValue = ByReadASBStatus(iDevNum, p, 100, ref iReadLen);
            // }

            if ((iReadValue == 0) && (iReadLen > 0)) {
                // Chinese note: 检查返回数据长度是否合法
                if (iReadLen % 4 != 0) {
                    iReturnValue = STATUS_ABNORMAL;
                    return iReturnValue;
                }
            }

            for (ii = 0; ii < (iReadLen / 4); ii++) {
                // Parse status
                // Chinese note:解析具体状态
                if ((cReadBuf[0 + (ii * 4)] & 0x10) == 0x01) {
                    bIsAbnormal = true;
                } else {
                    bIsAbnormal = false;
                }

                if ((cReadBuf[2 + (ii * 4)] & 0x40) == 0x00) {
                    bIsPrinting = false;
                } else {
                    bIsPrinting = true;
                }

                if ((cReadBuf[3 + (ii * 4)] & 0x40) == 0x00) {
                    bIsStopPrint = false;
                } else {
                    bIsStopPrint = true;
                }

                if ((cReadBuf[2 + (ii * 4)] & 0x20) == 0x00) {
                    bIsPrintUndone = false;
                } else {
                    bIsPrintUndone = true;
                }

                // Printer status is abnormal
                // Chinese note:状态是否异常
                if (bIsAbnormal) {
                    iReturnValue = STATUS_ABNORMAL;
                    return iReturnValue;
                }

                // Printer is stopped printing status
                // Chinese note:是否出现禁止打印状态
                if (bIsStopPrint) {
                    iReturnValue = STATUS_STOPPRINT;
                    return iReturnValue;
                }

                // Printer is start printing status
                // Chinese note:启动打印状态
                if (bIsPrinting) {
                    bIsPrintingStart = true;
                }

                // Printer is printing status
                // Chinese note:正在打印状态是否结束
                if (bIsPrintingStart) {
                    if (bIsPrintUndone) // 非正常完成
                    {
                        iReturnValue = STATUS_PRINT_UNDONE;
                        return iReturnValue;
                    }
                    if (!bIsPrinting) // 打印完成
                    {
                        return iReturnValue;
                    }
                }
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iStartTime = GetTickCount();
        }

        // Checking timeout
        // Chinese note:超时判断
        if (iStartTime >= iEndTime) {
            iReturnValue = STATUS_DEVTIMEOUT;
        }
        return iReturnValue;

    }

    /*******************************************************************
     * * Function Name: ClearStopPrintEnd(int iDevNum) Function: Clear status flag
     * of clearing status flag of "Printer is stopped printing" completely.
     * Chinese note: 清除已完成清除禁止打印状态标志 Parameter: iDevNum: Discriminating number
     * of device. Chinese note: 设备编号
     * *
     * * Return Value: Success : OPERATION_OK Failure : OPERATION_ERROR
     *
     * @param socketOut
     ********************************************************************/
    private static int ClearStopPrint(OutputStream socketOut) {
        byte[] cTempBuf = new byte[5];
        int iWriteLen = -1;

        cTempBuf[0] = 0x1b;
        cTempBuf[1] = 0x41;

        if (ByWritePort(cTempBuf, 2, iWriteLen, socketOut) != 0) {
            return OPERATION_ERROR;
        }

        // if (iWriteLen != 2) {
        // return OPERATION_ERROR;
        // }

        return OPERATION_OK;
    }

    /*******************************************************************
     * * Function Name: ResetDevStatus(int iDevNum) Function: Reset printer status
     * Chinese note: 下发设备状态复位指令 Parameter: iDevNum: Discriminating number of
     * device. Chinese note: 设备编号
     * *
     * * Return Value: Success : OPERATION_OK Failure : OPERATION_ERROR
     *
     * @param socketOut
     ********************************************************************/
    private static int ResetDevStatus(OutputStream socketOut) {
        byte[] cTempBuf = new byte[6];
        int iWriteLen = -1;

        cTempBuf[0] = 0x10;
        cTempBuf[1] = 0x06;
        cTempBuf[2] = 0x07;
        cTempBuf[3] = 0x08;
        cTempBuf[4] = 0x08;

        if (ByWritePort(cTempBuf, 5, iWriteLen, socketOut) != 0) {
            return OPERATION_ERROR;
        }

        // if (iWriteLen != 5) {
        // return OPERATION_ERROR;
        // }

        return OPERATION_OK;
    }

    /*******************************************************************
     * * Function Name: ClearStopPrint(int iDevNum) Function: Clear status flag of
     * "Printer is stopped printing". Chinese note: 清除禁止打印状态标志 Parameter:
     * * iDevNum: Discriminating number of device. Chinese note: 设备编号
     * *
     * * Return Value: Success : OPERATION_OK Failure : OPERATION_ERROR
     *
     * @param socketOut
     ********************************************************************/
    private static int ClearStopPrintEnd(OutputStream socketOut) {
        byte[] cTempBuf = new byte[6];
        int iWriteLen = -1;

        cTempBuf[0] = 0x10;
        cTempBuf[1] = 0x06;
        cTempBuf[2] = 0x07;
        cTempBuf[3] = 0x08;
        cTempBuf[4] = 0x04;

        if (ByWritePort(cTempBuf, 5, iWriteLen, socketOut) != 0) {
            return OPERATION_ERROR;
        }

        // if (iWriteLen != 5) {
        // return OPERATION_ERROR;
        // }

        return OPERATION_OK;
    }

    private static int ByWritePort(byte[] cTempBuf, int i, int iWriteLen, OutputStream socketOut) {
        try {
            socketOut.write(cTempBuf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static long GetTickCount() {
        return System.currentTimeMillis();
    }
}
