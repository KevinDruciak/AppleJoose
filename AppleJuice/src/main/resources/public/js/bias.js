ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9", "b55b025e438fa8a98e32482b5f768ff5"];

let bias_data = [];

let x = document.getElementById("x").innerText;
let y = document.getElementById("y").innerText;
// console.log(x);
// console.log(y);

let newX = x.substring(1, x.length - 1);
let newY = y.substring(1, y.length - 1);

let arrayX = newX.split(", ");
let arrayY = newY.split(", ");

for (let i = 0; i < arrayX.length; i++) {
    bias_data.push([arrayX[i], parseFloat(arrayY[i])])
}

const biasChartData = {
    type: "line",
    title: {
        text: "Daily Average Bias Ratings",
        adjustLayout: true
    },
    tooltip: {
        text: 'Date: %kt<br>Bias Rating: %vv'
    },
    scaleX: {
        label: {
            text: 'Date'
        },
        item: {
            angle: '-45'
        }
    },
    scaleY: {
        label: {
            text: 'Bias Rating'
        }
    },
    series: [{
        values: bias_data
    }],
    plotarea: {
        margin: 'dynamic'
    }

}
zingchart.render({
    id: "bias-chart",
    data: biasChartData,
    height: "100%",
    width: "100%"
});