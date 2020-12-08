ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9", "b55b025e438fa8a98e32482b5f768ff5"];

let news_data = document.getElementById("news")
let news_data2 = []
let news_tempString = news_data.innerText.substring(1, news_data.innerText.length-1)
let news_stringArray = news_tempString.split(", ")
news_index = 0
news_stringArray.forEach(news_myFunction);
function news_myFunction(news_item, news_index) {
    let news_stringArray2 = news_item.split("=")
    news_data2.push([news_stringArray2[0], parseInt(news_stringArray2[1])])
}

const newsChartData = {
    type: "bar",
    title: {
        text: "Favorite News Sources For All Users",
        adjustLayout: true
    },
    tooltip: {
        text: 'Source: %kt<br>Frequency: %v'
    },
    scaleX: {
        label: {
            text: 'Favorite News Sources'
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
        values: news_data2
    }],
    plotarea: {
        margin: 'dynamic'
    }
};
zingchart.render({
    id: "news-chart",
    data: newsChartData,
    height: "100%",
    width: "100%"
});