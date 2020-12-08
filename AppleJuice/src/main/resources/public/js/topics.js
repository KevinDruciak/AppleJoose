ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9", "b55b025e438fa8a98e32482b5f768ff5"];

let topics_data = document.getElementById("topics")
let topics_data2 = []
let topics_tempString = topics_data.innerText.substring(1, topics_data.innerText.length-1)
let topics_stringArray = topics_tempString.split(", ")
topics_index = 0
topics_stringArray.forEach(topics_myFunction);
function topics_myFunction(topics_item, topics_index) {
    let topics_stringArray2 = topics_item.split("=")
    topics_data2.push([topics_stringArray2[0], parseInt(topics_stringArray2[1])])
}

const topicChartData = {
    type: "bar",
    title: {
        text: "Favorite Topics For All Users",
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
        values: topics_data2
    }],
    plotarea: {
        margin: 'dynamic'
    }
};

zingchart.render({
    id: "topics-chart",
    data: topicChartData,
    height: "100%",
    width: "100%"
});