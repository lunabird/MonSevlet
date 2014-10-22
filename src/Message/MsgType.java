package Message;
/**
 * ��Ϣ����
 * @author huangpeng
 *
 */
public enum MsgType
{
	/////��ʷType
	HostInfo1,ContainerInfo,SampleTime,
	AllServiceStateRequest,AllServiceState,AllHostRequest,AllHost,
	AlarmStorageRequest,AlarmStorageResult,
	CurrentInfo,AllHostResult,
	CurrentInfoResult,
	ServiceInvokeInfoRequest,ServiceInvokeInfoResult,
	
	//*******************************����������*************************************//
	////3.1serviceDetailInfoWithCopyRequest
	serviceDetailInfoWithCopyRequest,serviceDetailInfoWithCopy, //3.1.1ϵͳ�ṹ����
	//serviceCallInfoRequest,serviceCallInfo,//3.1.2ͬ3.2.3���������ͳ��
	//serviceRunTimeRequest,serviceRunTime, //3.1.3ͬ3.2.4�������ʱ��ͳ��
	////3.2��ϸ��Ϣ-������Ϣ����
	serviceBussinessTypeRequest,serviceBussinessType, //3.2.1����ҵ����������鱨���񣬵翹����
	serviceCallInfoRequest,serviceCallInfo, //3.2.3���������ͳ��
	serviceRunTimeRequest,serviceRunTime, //3.2.4�������ʱ��ͳ��
	activeServiceRequest,activeService, //3.2.5��ǰ���Ծ�ķ������
	serviceDetailInfoListRequest,serviceDetailInfoList, //3.2.6������ϸ��Ϣ
	SERVICELIST,SERVICELISTRESULT,
	SERVICECOPYLIST,SERVICECOPYLISTRESULT,
	MESSAGELIST,MESSAGELISTRESULT,
	////3.3����������Ϣ����
	singleServiceCallTimesRequest,singleServiceCallTimes, //3.3.1����ƽ��������
	singleServiceRunTimeRequest,singleServiceRunTime, //3.3.2����ƽ����Ӧʱ��
	serviceCopyRequest,serviceCopy, //3.3.3�������˽ṹ,��Ҫ������񸱱�������IP��ַ��Ϣ
	////3.4�ڵ���Ϣ
	NODELIST,NODELISTRESULT,
	////3.5�ڵ�������Ϣ����
	nodeHistoryCPURAMRequest,nodeHistoryCPURAM, //3.5.1&3.5.2�ڵ���ʷCPU���ڴ�
	serviceDetailInfoListCombinedRequest,serviceDetailInfoListCombined, //3.5.4�ڵ��Ϸ�����ϸ��Ϣ
	AlarmListRequest,AlarmListResult, //3.6.1�����б�
	handleAlarmRequest,handleAlarmResult, //�����Ƿ񱻴���
	////3.7��ز������ý���
	concurrentNum,
	////zf
	HOSTINFO,HOSTHARDDISK,
	SERVICEDETAIL,NODEDETAIL,
	FILTER,FILTERRESULT,
	FILTERSERVICE,FILTERSERVICERESULT,
	SIZENUM,FILTERSIZE,FILTERNODE,FILTERNODERESULT,CFILTERNODE,CFILTERNODERESULT,
	FILTERWARN, LOGSIZE, FILTERLOGSIZE
}

//1.HostInfo��SCAS��MON���͵����������Ϣ
//2.ContainerInfo��SCAS��MON���͵����������Ϣ
//3.SampleTime��MON��SCAS���͵Ĳ���ʱ����
//4.AllServiceStateRequest��MON��ע�����ķ��͵��������״̬��Ϣ
//5.AllServiceState��ע��������MON���͵ķ���״̬��Ϣ�������
//6.AllHostRequest��MON��ע�����ķ��͵��������������б����Ϣ
//7.AllHost��ע��������MON���͵����������б������
//8.AlarmStorageRequest��MON��ע�����ķ��͵����󾯱�������Ϣ
//9.AlarmStorageResult��ע��������MON���͵��쳣��Ϣ�Ƿ����ɹ���ʶ
//10.AlarmListRequest:MON��ע�������������еľ����б�
//11.AlarmListResult��ע��������MON���͵�
//10.CurrentInfo��ע��������MON���͵�����ǰ���µ�����������cpu���ڴ���б���Ϣ