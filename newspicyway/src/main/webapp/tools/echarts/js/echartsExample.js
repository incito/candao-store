var myChart;
var domGraphic = document.getElementById('graphic');
var domMain = document.getElementById('main');
var domMessage = document.getElementById('wrong-message');
var needRefresh = false;

var curTheme;

function requireCallback (ec, defaultTheme) {
    echarts = ec;
    refresh();
    window.onresize = myChart.resize;
}

function focusGraphic() {
    domGraphic.className = 'col-md-8 ani';
    if (needRefresh) {
        myChart.showLoading();
        setTimeout(refresh, 1000);
    }
}

function refresh(isBtnRefresh){
    if (isBtnRefresh) {
        needRefresh = true;
        focusGraphic();
        return;
    }
    needRefresh = false;
    if (myChart && myChart.dispose) {
        myChart.dispose();
    }
    myChart = echarts.init(domMain, curTheme);
    window.onresize = myChart.resize;
    myChart.setOption(option, true);
    domMessage.innerHTML = '';
}

function needMap() {
    var href = location.href;
    return href.indexOf('map') != -1
           || href.indexOf('mix3') != -1
           || href.indexOf('mix5') != -1
           || href.indexOf('dataRange') != -1;

}

var echarts;
require.config({
    paths: {
        echarts: global_Path+'/tools/echarts/js'
    }
});
launchExample();

var isExampleLaunched;
function launchExample() {
    if (isExampleLaunched) {
        return;
    }

    // 按需加载
    isExampleLaunched = 1;
    require(
        [
            'echarts',
//            'theme/' + hash.replace('-en', ''),
            'echarts/chart/line',
            'echarts/chart/bar'
//            'echarts/chart/scatter',
//            'echarts/chart/k',
/*            'echarts/chart/pie',
            'echarts/chart/radar',
            'echarts/chart/force',
            'echarts/chart/chord',
            'echarts/chart/gauge',
            'echarts/chart/funnel',
            'echarts/chart/eventRiver',
            'echarts/chart/venn',
            'echarts/chart/treemap',
            needMap() ? 'echarts/chart/map' : 'echarts'*/
        ],
        requireCallback
    );
}
