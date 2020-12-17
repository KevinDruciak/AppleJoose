ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9", "b55b025e438fa8a98e32482b5f768ff5"];

let data = document.getElementById("data")
let data2 = []
let tempString = data.innerText.substring(1, data.innerText.length-1)
let stringArray = tempString.split(", ")
index = 0
stringArray.forEach(myFunction);
function myFunction(item, index) {
    let stringArray2 = item.split("=")
    data2.push([stringArray2[0], parseInt(stringArray2[1])])
}

const topicChartData = {
    type: "bar",
    title: {
        text: "Your Favorite Topics",
        adjustLayout: true
    },
    tooltip: {
        text: 'Topic: %kt<br>Frequency: %v'
    },
    scaleX: {
        label: {
            text: 'Favorite Topics'
        },
        item: {
            angle: '-45'
        }
    },
    scaleY: {
        label: {
            text: 'Frequency'
        }
    },
    series: [{
        values: data2
    }],
    plotarea: {
        margin: 'dynamic'
    }
};

zingchart.render({
    id: "chart-three",
    data: topicChartData,
    height: "100%",
    width: "100%"
});