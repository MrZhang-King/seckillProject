<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="bootstrap/bootstrap.jsp"%>
<html>
    <head>
        <title>秒杀商品页</title>
        <style>
            td {
                text-align: center;
            }
            td>a {
                color: #f40;
            }
            .redColor {
                color: #f40;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="panel panel-danger">
                <!-- Default panel contents -->
                <div class="panel-heading">秒杀列表</div>
                <!-- Table -->
                <table class="table">
                    <thread>
                        <tr>
                            <td>名称</td>
                            <td>秒杀价格</td>
                            <td>库存</td>
                            <td>开始时间</td>
                            <td>结束时间</td>
                            <td>商品详情</td>
                        </tr>
                    </thread>
                    <tbody>
                    <c:forEach items="${items}" var="item">
                        <tr>
                            <td>${item.name}</td>
                            <td class="redColor">￥${item.price}</td>
                            <td>${item.no}</td>
                            <td>
                                <fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </td>
                            <td>
                                <fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </td>
                            <td><a href="getItemById/${item.id}">进入秒杀</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
