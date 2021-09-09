<%--
  Created by IntelliJ IDEA.
  User: rsh
  Date: 2021/7/23
  Time: 16:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>词云图</title>
    <script src="JS/echarts.min.js" type="text/javascript"></script>
    <script src="JS/echarts-wordcloud.min.js" type="text/javascript"></script>
</head>
<body>
<div id="main" style="margin: 0 auto"></div>

</body>
</html>
<script type="text/javascript">
    const main = document.getElementById('main');
    main.style.width = 80 + "%";
    main.style.height = main.offsetWidth + "px";

    const chart = echarts.init(document.getElementById('main'));

    const maskImage = new Image();
    maskImage.src = './images/cloud4.png';

    chart.setOption({
        series: [{
            type: 'wordCloud',

            // 要绘制的“云”的形状。可以是表示为回调函数或关键字present的任何极性方程。
            // circle（圆形，默认）、
            // cardioid（苹果形或心形曲线，最著名的极坐标方程）、
            // diamond（正方形的别名）、
            // triangle-forward、
            // triangle（三角形直立的别名）、
            // pentagon（五边形）
            // star（星形）

            // shape: 'diamond',

            // A silhouette image which the white area will be excluded from drawing texts.
            // The shape option will continue to apply as the shape of the cloud to grow.

            maskImage: maskImage,

            // Folllowing left/top/width/height/right/bottom are used for positioning the word cloud
            // Default to be put in the center and has 75% x 80% size.

            left: 'center',
            top: 'center',
            width: '100%',
            height: '100%',
            right: null,
            bottom: null,

            // Tools.Test size range which the value in data will be mapped to.
            // Default to have minimum 12px and maximum 60px size.

            sizeRange: [12, 60],

            // Tools.Test rotation range and step in degree. Tools.Test will be rotated randomly in range [-90, 90] by rotationStep 45

            rotationRange: [-90, 90],
            rotationStep: 45,

            // size of the grid in pixels for marking the availability of the canvas
            // the larger the grid size, the bigger the gap between words.

            gridSize: 8,

            // set to true to allow word being draw partly outside of the canvas.
            // Allow word bigger than the size of the canvas to be drawn
            drawOutOfBound: false,

            // If perform layout animation.
            // NOTE disable it will lead to UI blocking when there is lots of words.
            layoutAnimation: true,

            // Global text style
            textStyle: {
                fontFamily: 'sans-serif',
                fontWeight: 'bold',
                // Color can be a callback function or a color string
                color: function () {
                    // Random color
                    return 'rgb(' + [
                        Math.round(Math.random() * 160),
                        Math.round(Math.random() * 160),
                        Math.round(Math.random() * 160)
                    ].join(',') + ')';
                }
            },
            emphasis: {
                focus: 'self',

                textStyle: {
                    shadowBlur: 10,
                    shadowColor: '#333'
                }
            },

            // Data is an array. Each array item must have name and value property.
            data: [
                <c:forEach var="U" items="${data}">
                {
                    name: '${U.key}',
                    value: ${U.value},
                },
                </c:forEach>
            ]
        }]
    });
</script>
