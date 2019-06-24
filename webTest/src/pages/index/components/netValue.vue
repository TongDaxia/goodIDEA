<template>
    <div class="netValue pannel">
        <!--净值折线图-->
        <div class="netValue-echart">
            <div class="netValue-echart-head">
                <div class="chart-num">
                    <p class="first">本基金
                        <span :style="chartData.rangeProceedsPF | formatStyle">
                                        {{chartData.rangeProceedsPF | noEmpty('%')}}
                                        </span>
                    </p>
                    <p class="last" v-if="chartData.label">{{chartData.label}}
                        <span :style="chartData.rangeProceedsCom | formatStyle">
                                        {{chartData.rangeProceedsCom |  noEmpty('%')}}
                                        </span>
                    </p>
                </div>
                <div v-if="from != 1 && isCurrencyType == 2"><span class="time-option">近一年</span></div>
                <div @click="$emit('switchTimeDown')" v-else>
                    <span class="time-option after" :class="{ 'menu-active': showTimeDown}">{{selTime.k}}</span>
                </div>
            </div>
            <!--折线图-->
            <div class="echart-box">
                <div id="box"></div>
                <p class="no-netValue" v-if="nochart">本产品暂未披露净值信息</p>
            </div>
        </div>
        <!--净值表格-->
        <div class="netValue-table">
            <div class="netValue-table-title">
                <a :href="'netValue_table_detail' | detailLink({isCurrencyType})" target="_blank">
                    <span>净值</span>
                </a>
            </div>
            <table class="detail-table" v-if="JSON.stringify(netValueTable) !== '{}'">
                <colgroup>
                    <col :width="Math.floor(100 / netValueTable.name.length) + '%'"
                         v-for="n in netValueTable.name.length"/>
                </colgroup>
                <tr><th v-for="n in netValueTable.name">{{n}}</th></tr>
                <tr v-for="row in netValueTable.list">
                    <template  v-for="(d,i) in row">
                        <td v-if="netValueTable['name'][i].includes('增长率')" :style="d | formatStyle">{{d}}</td>
                        <td v-else>{{d}}</td>
                    </template>
                </tr>
            </table>
            <p class="no-data" v-else>暂无相关信息</p>
        </div>
        <!--主要指标-->
        <div class="main-quota" v-if="isCurrencyType!= 2&&from != 1">
            <p class="main-quota-title">主要指标<span>(近一年)</span></p>
            <div class="main-quota-detail" v-if="JSON.stringify(mainQuota) !== '{}'">
                <div class="left">
                    <p>基金收益
                        <span>{{mainQuota.proceedsPastYear | noEmpty('%')}}</span>
                    </p>
                    <p>Jensen
                        <span>{{mainQuota.jensen | noEmpty('%')}}</span>
                    </p>
                    <p>Beta
                        <span>{{mainQuota.beta | noEmpty}}</span>
                    </p>
                </div>
                <div class="right">
                    <p>Sharpe
                        <span>{{mainQuota.sharpe | noEmpty}}</span>
                    </p>
                    <p>Treynor
                        <span>{{mainQuota.treynor | noEmpty('%')}}</span>
                    </p>
                    <p>年化波动率
                        <span>{{mainQuota.vix | noEmpty('%')}}</span>
                    </p>
                </div>
            </div>
            <p class="no-data" v-else>暂无相关信息</p>
        </div>
        <!--区间收益-->
        <div class="interval-income">
            <p class="interval-income-title">区间收益<span v-if="income.endDate">(截止日期：{{income.endDate}})</span>
            </p>
            <table class="detail-table" v-if="income.list && income.list.length">
                <colgroup>
                    <col :width="Math.floor(100 / income.name.length) + '%'"
                         v-for="n in income.name.length"/>
                </colgroup>
                <tr><th v-for="n in income.name">{{n}}</th></tr>
                <tr v-for="row in income.list">
                    <template v-for="(d,i) in row">
                        <td v-if="i===0">{{d}}</td>
                        <td :style="d | formatStyle" v-else>{{d}}</td>
                    </template>
                </tr>
            </table>
            <p class="no-data" v-else>暂无相关信息</p>
        </div>
    </div>
</template>

<script>
    import echarts from 'echarts/dist/echarts.common';
    import common from '@utils/commons';
    let param = common.getParams();
    export default {
        props: ['isCurrencyType','selTime','showTimeDown'],
         data(){
             return {
                 from:param.from,
                 nochart:false,//无折线图数据
                 chartData:{},//净值折线图数据
                 netValueTable:{},//净值列表数据
                 mainQuota:{},//主要指标数据
                 income:{},//区间收益数据
             }
         },
        methods:{
            loadData(){},
            //获取净值折线图
            getChartData(){
                return new Promise(resolve => {
                    const req ={...param, range:this.selTime.v,...JSON.parse(localStorage.getItem("localReq"))};
                    this.$get('/finchinaAPP/nonStandard/getChart.action', req).then(res => {
                        this.nochart = false;
                        this.chartData = res.data;
                        this.initChart(res.data.timeList,res.data.privateGrowthList,res.data.compareGrowthList);
                        resolve();
                    }).catch(err=>{
                        this.nochart = true;
                        this.chartData = {};
                        this.initChart([],[],[]);
                        resolve();
                    });
                })
            },
            //获取净值列表
            getNetValueTable(){
                return new Promise(resolve => {
                    const req = {...param, pagesize: 5, skip: 0,...JSON.parse(localStorage.getItem("localReq"))};
                    this.$get('/finchinaAPP/nonStandard/getnetValueList.action', req).then((res) => {
                        this.netValueTable = res.data;
                        resolve();
                    }).catch(err => {
                        resolve();
                    });
                })
            },
            //获取主要指标
            getMainQuota(){
                return new Promise(resolve => {
                    this.$get('/finchinaAPP/nonStandard/getIndicators.action',
                        {...param,...JSON.parse(localStorage.getItem("localReq"))}).then(res => {
                        this.mainQuota= res.data;
                        resolve();
                    }).catch(err=>{
                        resolve();
                    });
                })
            },
            //获取区间收益
            getIntervalIncome(){
                return new Promise(resolve => {
                    this.$get('/finchinaAPP/nonStandard/getGrowths.action',
                        {...param,...JSON.parse(localStorage.getItem("localReq"))}).then(res => {
                        this.income = res.data;
                        resolve();
                    }).catch(err=>{
                        resolve();
                    });
                })
            },
            //渲染折线图
            initChart() {
                if (!this.myCharts) this.myCharts = echarts.init(document.getElementById('box'));
                const option = {
                    tooltip: {trigger: 'axis',textStyle:{fontSize:12}},
                    grid: {top: '3%', left: '5%', right: '10%',
                        bottom: '0%', containLabel: true},
                    xAxis: [{
                        type: 'category',
                        boundaryGap: false,
                        axisLabel: {textStyle: {fontSize: 10, color:'#8c8c8c'}},//字体
                        axisTick: {show: true, lineStyle:{color:'#dfdfdf'}},//刻度
                        axisLine:{lineStyle:{color:'#dfdfdf'},onZero:false},//轴线
                        splitLine:{show: false},
                        data: this.chartData.timeList
                    }],
                    yAxis: [{
                        type: 'value',
                        axisLabel: {textStyle: {fontSize: 10, color:'#8c8c8c'},formatter: '{value}%'},
                        axisTick: {show: true,lineStyle:{color:'#dfdfdf'}},
                        axisLine:{lineStyle:{color:'#dfdfdf'}},
                        splitLine:{show: false},
                    }],
                    series: [{
                        name: '本基金',
                        type: 'line',
                        showSymbol: false,
                        connectNulls: true,
                        itemStyle: {normal: {color: '#FF7500'}},
                        lineStyle: {normal: {width: 1}},// 线条
                        data: this.chartData.privateGrowthList
                    }, {
                        name: this.chartData.label,
                        type: 'line',
                        showSymbol: false,
                        connectNulls: true,
                        itemStyle: {normal: {color: '#43C1F6'}},
                        lineStyle: {normal: {width: 1}},
                        data: this.chartData.compareGrowthList
                    }]
                };
                this.myCharts.setOption(option, true);
            },
        }
    }
</script>

<style scoped lang="less">
    /*净值*/
    .netValue {
        position: relative;
        margin-top: 0 !important;
        /*折线图区域*/
        .netValue-echart{
            position: relative;
            .netValue-echart-head {
                padding: 0.29rem 0.32rem;
                background: #fff;
                position: relative;
                z-index: 2;
                display: flex;
                justify-content: space-between;
                .chart-num{
                    .first,.last{
                        color: #333;
                        font-size: 0.22rem;
                        display: inline-block;
                        vertical-align: top;
                        &:before {
                            content: '';
                            width: 0.12rem;
                            height: 0.12rem;
                            border-radius: 0.12rem;
                            display: inline-block;
                            vertical-align: top;
                            margin-top: 0.06rem;
                            margin-right: 0.1rem;
                        }
                        span{
                            font-size: 0.2rem;
                        }
                    }
                    .last:before {
                        background: #4D99F5;
                    }
                    .first{
                        margin-right: 0.14rem;
                        &:before {
                            background: #F79066;;
                        }
                    }
                }
                .time-option {
                    color: #FF7500;
                    font-size: 0.24rem;
                    &.after:after {
                        content: '';
                        display: inline-block;
                        vertical-align: top;
                        margin-top: 0.06rem;
                        border: 0.1rem solid transparent;
                        border-top-color: #FF7500;
                        /*transition: all 0.3s;*/
                    }
                    &.menu-active {
                        color: #ff7500;
                        &:after {
                            transform-origin: center 0.04rem;
                            transform: rotate(180deg);
                        }
                    }
                }
            }
            .echart-box {
                width: 100%;
                height: 3.73rem;
                position: relative;
                .no-netValue{
                    font-size: 0.28rem;
                    color: #666666;
                    line-height: 4.3rem;
                    text-align: center;
                    width: 100%;
                    background: #fff;
                    position: absolute;
                    top: 0;
                }
                #box{
                    width: 100%;
                    height: 3.73rem;
                }
            }
        }
        /*净值列表*/
        .netValue-table-title {
            padding: 0.48rem 0.32rem 0 0.32rem;
            a {
                display: flex;
                align-items: center;
                position: relative;
                justify-content: space-between;
                -webkit-tap-highlight-color: rgba(255, 255, 255, 0);
                -webkit-user-select: none;
                -moz-user-focus: none;
                -moz-user-select: none;
                &:after{
                    content: "";
                    position: absolute;
                    display: inline-block;
                    width: 0.135rem;
                    height: 0.135rem;
                    border: 0.03rem solid;
                    border-color: transparent #8C8C8C #8C8C8C transparent;
                    transform: rotate(-45deg);
                    right: 0.1rem;
                }
                span {
                    color: #333;
                    font-size: 0.28rem;
                    line-height: 0.28rem;
                }
            }
        }
        /*主要指标*/
        .main-quota{
            .main-quota-title {
                font-size: 0.28rem;
                color: #333333;
                padding: 0.47rem 0.32rem 0 0.32rem;
                line-height: 0.28rem;
                span {color: #8C8C8C;font-size: 0.24rem}
            }
            .main-quota-detail {
                display: flex;
                div {
                    width: 100%;
                    margin: 0.36rem 0.32rem 0 0.32rem;
                    &.left{
                        margin-right: 0.53rem;
                    }
                    &.right{
                        margin-left: 0.53rem;
                    }
                    p {
                        font-size: 0.26rem;
                        color: #8c8c8c;
                        margin-bottom: 0.3rem;
                        line-height: 0.26rem;
                        &:last-child{
                            margin-bottom: 0;
                        }
                    }
                }
                span {color: #111;float: right;}
            }
        }
        /*区间收益*/
        .interval-income{
            .interval-income-title {
                font-size: 0.28rem;
                color: #333333;
                padding: 0.48rem 0.32rem 0 0.32rem;
                line-height: 0.28rem;
                span {color: #8C8C8C;font-size: 0.24rem}
            }
        }
    }
</style>