<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="bootstrap/bootstrap.jsp"%>
<html>
    <head>
        <title>支付页面</title>
        <style>
            . {
                margin: 0;
                padding: 0;
            }
            /*.redColor {*/
            /*    color: #ff4400;*/
            /*}*/
            #countDownBox {
                border-radius: 5em;
                text-align: center;
                line-height: 60px;
                width: 300px;
                height: 60px;
                margin: 20px auto;
                background-color: #ff4400;
            }
            .time {
                margin: 10px auto;
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
            /*#start {*/
            /*    margin: 0 auto;*/
            /*    width: 200px;*/
            /*    height: 30px;*/
            /*    text-align: center;*/
            /*    line-height: 30px;*/
            /*    background-color: rgba(0,0,0,0.7);*/
            /*    border-radius: 5em;*/
            /*}*/
            /*#endBox {*/
            /*    margin: 0 auto;*/
            /*    width: 200px;*/
            /*    height: 30px;*/
            /*    text-align: center;*/
            /*    line-height: 30px;*/
            /*    background-color: rgba(0,0,0,0.7);*/
            /*    border-radius: 5em;*/
            /*}*/
            #message {
                color: #a9a9a9;
            }
            #payBtn {
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
            var minute,second;
            function flushTimeAndSetTime(time) {
                //     一分钟   一小时  一天
                //  1000  *  60  *  60  *  24
                minute = Math.floor(time / 1000 / 60 % 60);
                second = Math.floor(time / 1000 % 60);
                setTime("minute", minute);
                setTime("second", second);
            }

            //请求服务器时间
            $.post(
                "/seckillProject/getTime",
                {},
                function(data) {
                    if(data['success']) {//判断服务器的数据是否可用
                        var timeLong = data.data;
                        var createTime = new Date(${order.createTime.time});
                        var noPayTime = createTime.getTime() + (5 * 60 *  1000);
                        var begin = window.setInterval(function () {
                            var leadTime = noPayTime - timeLong;
                            if(leadTime <= 0){
                                $('#payBtn').addClass('disabled');
                                clearInterval(begin);
                                return ;
                            }
                            //刷新时间倒计时（活动还有多久结束的倒计时）
                            flushTimeAndSetTime(leadTime);
                            timeLong = timeLong + 1000;
                        },1000);
                    }
                },
            "json"
            );


            $(function () {
                $('#payBtn').one('click',function () {
                    $(this).addClass('disabled');
                    $.post('/seckillProject/payMoney/${order.orderCode}',
                        {},
                    function (data) {
                        if(data['success']){
                            alert("支付成功");
                        }else{

                            if(data['message'] == "noLogin"){
                                alert("未登录，请登录后再支付");
                                location.href = "/seckillProject/index.jsp";
                            }else if(data['message'] == "received"){
                                alert("该订单已支付，不要重复支付");
                            }else if(data['message'] == "timeOut"){
                                alert("该订单已失效");
                            }
                        }
                    },
                    'json'
                    );
                });
            });
        </script>

    </head>
    <body>
    <div class="container">
        <div id="countDownBox">
            <span id="timeText1">支付剩余时间</span>
            <span id="minute" class="time">00</span>
            <span>分</span>
            <span id="second" class="time">00</span>
            <span id="timeText2">秒</span>
        </div>
        <div class="panel panel-danger">
            <!-- Default panel contents -->
            <div class="panel-heading">订单详情</div>
            <!-- Table -->
            <table class="table">
                <thread>
                    <tr>
                        <td>商品名称:</td>
                        <td>${item.name}</td>
                    </tr>
                </thread>
                <tbody>
                <tr>
                    <td class="redColor">订单号：￥${order.orderCode}</td>
                    <td>应付金额：${item.price}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div id="noStart"><span id="message">请尽快完成支付</span></div>
            <button id="payBtn" type="submit"  class="btn btn-default">支付</button>
    </div>
    </body>
</html>
