package Message;
/**
 * 消息类型
 * @author huangpeng
 *
 */
public enum MsgType
{
	/////历史Type
	HostInfo1,ContainerInfo,SampleTime,
	AllServiceStateRequest,AllServiceState,AllHostRequest,AllHost,
	AlarmStorageRequest,AlarmStorageResult,
	CurrentInfo,AllHostResult,
	CurrentInfoResult,
	ServiceInvokeInfoRequest,ServiceInvokeInfoResult,
	
	//*******************************新增的需求*************************************//
	////3.1serviceDetailInfoWithCopyRequest
	serviceDetailInfoWithCopyRequest,serviceDetailInfoWithCopy, //3.1.1系统结构概览
	//serviceCallInfoRequest,serviceCallInfo,//3.1.2同3.2.3服务访问量统计
	//serviceRunTimeRequest,serviceRunTime, //3.1.3同3.2.4服务调用时间统计
	////3.2详细信息-服务信息界面
	serviceBussinessTypeRequest,serviceBussinessType, //3.2.1服务（业务）类别（例如情报服务，电抗服务）
	serviceCallInfoRequest,serviceCallInfo, //3.2.3服务访问量统计
	serviceRunTimeRequest,serviceRunTime, //3.2.4服务调用时间统计
	activeServiceRequest,activeService, //3.2.5当前最活跃的服务访问
	serviceDetailInfoListRequest,serviceDetailInfoList, //3.2.6服务详细信息
	SERVICELIST,SERVICELISTRESULT,
	SERVICECOPYLIST,SERVICECOPYLISTRESULT,
	MESSAGELIST,MESSAGELISTRESULT,
	////3.3服务详情信息界面
	singleServiceCallTimesRequest,singleServiceCallTimes, //3.3.1服务平均访问量
	singleServiceRunTimeRequest,singleServiceRunTime, //3.3.2服务平均响应时间
	serviceCopyRequest,serviceCopy, //3.3.3服务拓扑结构,主要请求服务副本的所在IP地址信息
	////3.4节点信息
	NODELIST,NODELISTRESULT,
	////3.5节点详情信息界面
	nodeHistoryCPURAMRequest,nodeHistoryCPURAM, //3.5.1&3.5.2节点历史CPU和内存
	serviceDetailInfoListCombinedRequest,serviceDetailInfoListCombined, //3.5.4节点上服务详细信息
	AlarmListRequest,AlarmListResult, //3.6.1警报列表
	handleAlarmRequest,handleAlarmResult, //警报是否被处理
	////3.7监控参数配置界面
	concurrentNum,
	////zf
	HOSTINFO,HOSTHARDDISK,
	SERVICEDETAIL,NODEDETAIL,
	FILTER,FILTERRESULT,
	FILTERSERVICE,FILTERSERVICERESULT,
	SIZENUM,FILTERSIZE,FILTERNODE,FILTERNODERESULT,CFILTERNODE,CFILTERNODERESULT,
	FILTERWARN, LOGSIZE, FILTERLOGSIZE
}

//1.HostInfo：SCAS向MON发送的主机监控信息
//2.ContainerInfo：SCAS向MON发送的容器监控信息
//3.SampleTime：MON向SCAS发送的采样时间间隔
//4.AllServiceStateRequest：MON向注册中心发送的请求服务状态消息
//5.AllServiceState：注册中心向MON发送的服务状态信息，查库获得
//6.AllHostRequest：MON向注册中心发送的请求所有主机列表的消息
//7.AllHost：注册中心向MON发送的所有主机列表，查库获得
//8.AlarmStorageRequest：MON向注册中心发送的请求警报入库的消息
//9.AlarmStorageResult：注册中心向MON发送的异常信息是否入库成功标识
//10.AlarmListRequest:MON向注册中心请求所有的警报列表
//11.AlarmListResult：注册中心向MON发送的
//10.CurrentInfo：注册中心向MON发送的请求当前最新的主机或容器cpu，内存的列表信息