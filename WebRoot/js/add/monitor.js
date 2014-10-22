 /*chart colors default */
			var $chrt_border_color = "#efefef";
			var $chrt_grid_color = "#DDD"
			var $chrt_main = "#E24913";
			/* red       */
			var $chrt_second = "#6595b4";
			/* blue      */
			var $chrt_third = "#FF9F01";
			/* orange    */
			var $chrt_fourth = "#7e9d3a";
			/* green     */
			var $chrt_fifth = "#BD362F";
			/* dark red  */
			var $chrt_mono = "#000";
			var serviceVisitTimes = [12,21,2,15,6,8,11,5,4,18];
			var serviceCallTime = [2,1,2,5,6,8,1,0,0,0];
			var nodeServiceAmount = [{"IP":"192.168.0.201","amount":6},{"IP":"192.168.0.202,192","amount":0},{"IP":"192.168.0.205","amount":0}];
			var cpu_rom = [{"IP":"192.168.0.201","cpu":"48.5%","ram":"88.6%"},{"IP":"192.168.0.202","cpu":"12%","ram":"55%"},{"IP":"192.168.0.203","cpu":"56%","ram":"59%"}];
			$(document).ready(function() {
				// DO NOT REMOVE : GLOBAL FUNCTIONS!
				$.ajax({
					type:"post",
					url:"./newServlet/yxjk_3_1_littlePic_Servlet",
					success:function(data){

						var serNum = "";
						for(var i=0 ; i<data.serDetail.length; i++){
							serNum +=data.serDetail[i]+",";
						}
						var sysNum = "";
						for(var i=0 ; i<data.sysDetail.length; i++){
							sysNum +=data.serDetail[i]+",";
						}
						var nodeNum = "";
						for(var i=0 ; i<data.nodeDetail.length; i++){
							nodeNum +=data.serDetail[i]+",";
						}
						$("#servicenum").append("<h5 style='font-family:Microsoft YaHei'> 服务总数 <span class='txt-color-blue'>" + data.serviceAmount + "</span></h5>")
										.append("<div class='sparkline txt-color-blue hidden-mobile hidden-md hidden-sm'>" + serNum + "</div>");
						$("#systemnum").append("<h5 style='font-family:Microsoft YaHei'> 系统总数 <span class='txt-color-blue'>" + data.sysAmount + "</span></h5>")
										.append("<div class='sparkline txt-color-blue hidden-mobile hidden-md hidden-sm'>" + sysNum + "</div>");
						$("#nodenum").append("<h5 style='font-family:Microsoft YaHei'> 节点总数 <span class='txt-color-blue'>" + data.nodeAmount + "</span></h5>")
										.append("<div class='sparkline txt-color-blue hidden-mobile hidden-md hidden-sm'>" + nodeNum + "</div>");
						pageSetUp();
					}
				});
				$.post("./newServlet/yxjk_3_1_jkgl_Servlet",{},function(text){
					//得到图形BarChar1
					showBarChar1(text.nodeServiceAmount);
					
					//得到图形BarChar(服务访问量统计)
					showBarChar(text.serviceCallQuantity,text.serviceCallQuantity_ordTime);
					//获得AreaGraph的data，并得到图形服务调用时间统计
					//var data_area = getAreaGraph(text.serviceRunTime);
					showAreaGraph(text.serviceRunTime,text.serviceRunTime_ordTime);
					//显示CPU和内存表
					showAreaGraph2(text.cpu_ram);
					//显示xy轴的坐标
					//setTimeout("showXY()", 1500);
					
	           }
	           );
				pageSetUp();
				Topology.init();
				setTimeout("showXY()", 500);
				
			});
		
			//给AreaGraph(即服务调用时间统计)喂数据
			function showAreaGraph(serviceVisitTimes,ordTime){
				var data = new Array();
				
				for (i = 0 ; i < serviceVisitTimes.length; i++) {

					data.push({
					x : i+"",
					y : serviceVisitTimes[i]
					});
				};
					// area graph 服务访问量
				if ($('#area-graph').length) {
					//var data_area = getAreaGraph(totalString);
					//alert(data_area[0].x);
					Morris.Area({
						element : 'area-graph',
						data : data,
						xkey : 'x',
						ykeys : ['y'],
						labels : ['调用数']
					});
				}
				//$("#area-graph svg text tspan").eq(4+serviceVisitTimes.length).html("0~"+ordTime[0]);
				for (i = 0 ; i <= serviceVisitTimes.length; i++) {
					
					$("#area-graph svg text tspan").eq(4+serviceVisitTimes.length-i).html(ordTime[i]+"~"+ordTime[i+1]);
				};
			}
			//给AreaGraph2（cpu和内存）喂数据
			function showAreaGraph2(serviceVisitTimes){
				// area graph 服务访问量
				// area graph 1
				var data = new Array();
				//alert(serviceVisitTimes);
				for (var i = 1 ; i <= serviceVisitTimes.length; i++) {
					//alert(serviceVisitTimes[i-1].cpu);
					//alert(parseInt(serviceVisitTimes[i-1].cpu));
					data.push({
					x : i+'',
					CPU : serviceVisitTimes[i-1].cpu,
					rom : serviceVisitTimes[i-1].ram
					});
				};
				if ($('#area-graph-1').length) {
					//alert("area-graph-1"+$('#area-graph-1'));
					Morris.Area({
						element : 'area-graph-1',
						
						data : data,
						xkey : 'x',
						ykeys : ['CPU', 'rom'],
						labels : ['CPU', 'rom']
					
					});
					//alert("siblings('selector')");
					
				}
				for (i = 0 ; i < serviceVisitTimes.length; i++) {
					
					$("#area-graph-1 svg text tspan").eq(4+serviceVisitTimes.length-i).html(serviceVisitTimes[i].IP);
				};
		}

			//给bar-char（即服务访问量统计表）喂数据
			function showBarChar(serviceRunTime,ordTime){
				if ($("#bar-chart").length) {
					//alert(totalString.sysList.length);
					var data1 = [];
					for (var i = 0; i <= 9; i += 1)
						data1.push([i, serviceRunTime[i]]);
					
					var ds = new Array();

					ds.push({
						data : data1,
						bars : {
							show : true,
							barWidth : 0.2,
							order : 1,
						}
					});
					//Display graph
					$.plot($("#bar-chart"), ds, {
						colors : [$chrt_second, $chrt_fourth, "#666", "#BBB"],
						grid : {
							show : true,
							hoverable : true,
							clickable : true,
							tickColor : $chrt_border_color,
							borderWidth : 0,
							borderColor : $chrt_border_color,
						},
						legend : true,
						tooltip : true,
						tooltipOpts : {
							content : "<b>%x</b> = <span>%y</span>",
							defaultTheme : false
						}

					});

				}
				for (var i = 0; i <= 9; i += 1){
					//alert(ordTime[i]);
					$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(i).html(ordTime[i]);
				}
			}


			//给bar-char-1（即节点服务数表）喂数据,并设置横坐标的显示
			function showBarChar1(serviceRunTime){
				/* bar chart */
				
				//alert(activeService.length);
				if ($("#bar-chart-1").length) {

					var data1 = [];
					//var index = 1;
					for (var i = 0; i < serviceRunTime.length; i += 1){
						//alert(parseInt(activeService[i][index]));
						data1.push([i, serviceRunTime[i].amount]);
						//$("#bar-chart-1 div.tickLabels div.xAxis div.tickLabel").eq(i).html(serviceRunTime[i].IP);
					}
						
					var ds = new Array();
					//alert(data1.toString());
					ds.push({
						data : data1,
						bars : {
							show : true,
							barWidth : 0.2,
							order : 1,
						}
					});

					//Display graph
					$.plot($("#bar-chart-1"), ds, {
						
						colors : [$chrt_second, $chrt_fourth, "#666", "#BBB"],
						grid : {
							show : true,
							hoverable : true,
							clickable : true,
							tickColor : $chrt_border_color,
							borderWidth : 0,
							borderColor : $chrt_border_color,
						},
						legend : true,
						tooltip : true,
						tooltipOpts : {
							content : "<b>%x</b> = <span>%y</span>",
							defaultTheme : false
						}

					});

				}
				for (var i = 0; i < 10; i += 1){
						if(i% (10/serviceRunTime.length)==0){
							$("#bar-chart-1 div.tickLabels div.xAxis div.tickLabel").eq(i).html(serviceRunTime[i/(10/serviceRunTime.length)].IP);
						}else{
							$("#bar-chart-1 div.tickLabels div.xAxis div.tickLabel").eq(i).html(" ");
						}
					}

			}
			//表在布局的时候乱了，用比较坑爹的方法设置个表头的名称
			function showXY(){
				//设置h2里的值
				$("#wid-id-1 header h2").html("服务访问量统计");
				$("#wid-id-3 header h2").html("节点服务数");
				$("#wid-id-2 header h2").html("服务调用时间统计");
				$("#wid-id-4 header h2").html("系统概览");
				$("#wid-id-5 header h2").html("CPU和内存占用率（%）");
				//设置服务访问量的横坐标
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(0).html("0-1min");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(1).html("1-2min");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(2).html("2-3min");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(3).html("30-40ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(4).html("40-50ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(5).html("50-60ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(6).html("60-70ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(7).html("70-80ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(8).html("80-90ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(9).html("90-100ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(10).html("100-110ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(11).html("110-120ms");
//				$("#bar-chart div.tickLabels div.xAxis div.tickLabel").eq(12).html("120-130ms");


				//设置CPU和内存的横坐标
				/*$("#area-graph-1 svg text tspan").eq(5).html("40-50ms");
				$("#area-graph-1 svg text tspan").eq(6).html("30-40ms");
				$("#area-graph-1 svg text tspan").eq(7).html("20-30ms");
				$("#area-graph-1 svg text tspan").eq(8).html("10-20ms");
				$("#area-graph-1 svg text tspan").eq(9).html("0-10ms");*/
				//设置CPU和内存的纵坐标
//				$("#area-graph-1 svg text tspan").eq(0).html("0");
//				$("#area-graph-1 svg text tspan").eq(1).html("25");
//				$("#area-graph-1 svg text tspan").eq(2).html("50");
//				$("#area-graph-1 svg text tspan").eq(3).html("75");
//				$("#area-graph-1 svg text tspan").eq(4).html("100");
				
				//设置服务访问量的横坐标
//				$("#area-graph svg text tspan").eq(5).html("90-100ms");
//				$("#area-graph svg text tspan").eq(6).html("80-90ms");
//				$("#area-graph svg text tspan").eq(7).html("70-80ms");
//				$("#area-graph svg text tspan").eq(8).html("60-70ms");
//				$("#area-graph svg text tspan").eq(9).html("50-60ms");
//				$("#area-graph svg text tspan").eq(10).html("40-50ms");
//				$("#area-graph svg text tspan").eq(11).html("30-40ms");
//				$("#area-graph svg text tspan").eq(12).html("20-30ms");
//				$("#area-graph svg text tspan").eq(13).html("10-20ms");
//				$("#area-graph svg text tspan").eq(14).html("0-10ms");

			}