<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="bootstrap/bootstrap.jsp"%>
<html>
    <head>
        <title>秒杀页</title>
        <style>
            . {
                margin: 0;
                padding: 0;
            }
            .redColor {
                color: #ff4400;
            }
            #countDownBox {
                border-radius: 5em;
                text-align: center;
                line-height: 80px;
                width: auto;
                height: 80px;
                background-color: #ff4400;
            }
            .time {
                background-color: rgba(100,100,100.5,0.5);
                font-size: 35px;
                color: #f0f8ff;
                border-radius: 5px;
                border: 1px solid rgba(0,0,100.5,0.5);
            }
            #noStart {
                margin: 0 auto;
                width: 200px;
                height: 30px;
                text-align: center;
                line-height: 30px;
                background-color: rgba(0,0,0,0.7);
                border-radius: 5em;
            }
            #start {
                margin: 0 auto;
                width: 200px;
                height: 30px;
                text-align: center;
                line-height: 30px;
                background-color: rgba(0,0,0,0.7);
                border-radius: 5em;
            }
            #endBox {
                margin: 0 auto;
                width: 200px;
                height: 30px;
                text-align: center;
                line-height: 30px;
                background-color: rgba(0,0,0,0.7);
                border-radius: 5em;
            }
            #message {
                color: #a9a9a9;
            }
            #seckillBtn {
                display: block;
                margin: 10px auto;
            }
        </style>

        <script type="text/javascript">

            //更新给定id中的内容为value（更新时间）
            function setTime(id,value) {
                if(value < 10){
                    value = "0" + value;
                }
                $("#"+id).text(value);
            }

            //刷新并更改时间（倒计时实现）
            var day,hour,minute,second;
            function flushTimeAndSetTime(time) {
                //     一分钟   一小时  一天
                //  1000  *  60  *  60  *  24
                day = Math.floor(time / 1000 / 60 / 60 / 24);
                hour = Math.floor(time / 1000 / 60 / 60 % 24);
                minute = Math.floor(time / 1000 / 60 % 60);
                second = Math.floor(time / 1000 % 60);
                setTime("day", day);
                setTime("hour", hour);
                setTime("minute", minute);
                setTime("second", second);
            }
            //秒杀的方法
            function startSeckill() {
                $("#message").parent().attr("id","start");
                $("#message").text("秒杀正在进行中……");
                $.post('/seckillProject/getSeckillURL/' + ${item.id},{},
                function (data) {
                    if(data['success']){//判断服务器给的数据是否可用
                        var seckillURL = data['data'];
                        if(seckillURL['used']){//判断服务器给的url状态是否可用
                            //md5加密的url
                            var md5URL = seckillURL['md5url'];
                            //更改按钮样式
                            $('#seckillBtn').removeClass('disabled');
                            $('#seckillBtn').removeClass('btn-default');
                            $('#seckillBtn').addClass('btn-danger');
                            //为按钮添加点击事件（只能点一次）
                            $('#seckillBtn').one('click',function () {
                                //点击一次后设置该按钮为不可点击
                                $(this).addClass('disabled');
                                $(this).addClass('btn-default');
                                $(this).removeClass('btn-danger');
                                //发送请求，秒杀商品
                                $.post('/seckillProject/startSeckillItem/' + md5URL + '/' + seckillURL['itemId'],
                                    {},
                                function (data) {
                                    if(!data['success']){
                                        var message = data['message'];
                                        if(message == 'noLogin'){
                                            alert("未登录，请登录后再操作");
                                            location.href = "/seckillProject/index.jsp";
                                        }else if(message == 'understock'){
                                            alert("库存不足");
                                        }
                                    }else{
                                        //可用的响应
                                        alert("成功购买到一件商品");
                                        //转去支付页面
                                        var order = data['data'];
                                        location.replace("/seckillProject/orderPay/" + order['orderCode']);
                                    }
                                },
                                'json'
                                );
                            });
                            // alert(seckillURL);
                        }
                    }else{
                        if("noLogin" == data['message']){
                            alert("未登录，请登录后再操作");
                            location.href = "/seckillProject/index.jsp";
                        }
                    }
                },
                'json'
                );
            }
            //秒杀结束时调用的方法
            function endSeckill() {
                $("#message").parent().attr("id","endBox");
                $("#message").text("秒杀已结束");
                // alert("秒杀结束");
                $('#seckillBtn').remove();
            }
            //请求服务器的时间
            var timeLong ;
            $.ajax({
                url: "/seckillProject/getTime",
                type: "POST",
                dataType: "json",
                success: function(data) {
                    if(data['success']) {//判断服务器的数据是否可用
                        timeLong = data.data;
                    }
                }
            });
            //每1000毫秒执行一次该方法
            var startTime = new Date(${item.startTime.time});//开始时间
            var endTime = new Date(${item.endTime.time});//结束时间
            var flag = 0;
            var begin = window.setInterval(function () {
                // (秒杀结束的时间还没到)当结束时间大于当前服务器时间时，更新时间
                if(timeLong <= endTime.getTime()) {
                    var time = startTime.getTime() - timeLong;
                    if(time <= 0){//秒杀开始的时间到了
                        //结束时间减去当前系统时间
                        time = endTime - timeLong;
                        //刷新时间倒计时（活动还有多久结束的倒计时）
                        flushTimeAndSetTime(time);
                        if(flag === 0){
                            $('#timeText1').text("本场秒杀活动剩余");
                            $('#timeText2').text("秒结束");
                            //开始秒杀（调用秒杀的函数）
                            startSeckill();
                            // alert("开始秒杀");
                            flag++;
                        }
                    }else{
                        //刷新并更改时间
                       flushTimeAndSetTime(time);
                    }
                }else{//秒杀结束的时间到了
                    //调用秒杀结束的方法
                    endSeckill();
                    //清除掉循环执行
                     clearInterval(begin);
                }
                //方法每执行一次 服务器拿到的时间增加1秒
                timeLong = timeLong + 1000;
            },1000);
        </script>
    </head>
    <body>
    <div class="container">
        <div class="panel panel-danger">
            <!-- Default panel contents -->
            <div class="panel-heading">商品详情</div>
            <!-- Table -->
            <table class="table">
                <thread>
                    <tr>
                        <td>名称:</td>
                        <td>${item.name}</td>
                    </tr>
                </thread>
                <tbody>
                    <tr>
                        <td class="redColor">秒杀价：￥${item.price}</td>
                        <td>库存：${item.no}</td>
                        <td>
                            开始时间：
                        <fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                        </td>
                        <td>
                            结束时间：
                            <fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div id="noStart"><span id="message">活动未开始</span></div>
        <button id="seckillBtn" type="button"  class="btn btn-default disabled">购买</button>
        <div id="countDownBox">
            <span id="timeText1">本场剩余</span>
            <span id="day" class="time">00</span>
            <span>天</span>
            <span id="hour" class="time">00</span>
            <span>时</span>
            <span id="minute" class="time">00</span>
            <span>分</span>
            <span id="second" class="time">00</span>
            <span id="timeText2">秒开始</span>
        </div>
    </div>
    </body>
</html>
