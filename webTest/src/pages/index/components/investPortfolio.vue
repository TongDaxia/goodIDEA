<template>
    <div class="investPortfolio pannel">
        <div class="head-title">
            <a :href="'investment_portfolio_detail' | detailLink({reportDate})" target="_blank"></a>
            <div class="title-box">
                <p>
                    <span class="title">投资组合</span>
                    <span class="time">{{pieData.reportDate}}</span>
                </p>
                <p>
                    <span class="label-type">股票/债券/基金投资</span>
                    <span class="btn"></span>
                </p>
            </div>
        </div>
        <div class="investPortfolio-detail">
            <div class="detail-top" v-if="pieData.list">
                <div class="pie" id="pieChart"></div>
                <table class="detail-table">
                    <tr><th>分类</th><th>市值(万元)</th><th  @click="showDes=!showDes">占比(%)<span class="ques">?</span></th></tr>
                    <tr v-for="row in pieData.list"><td class="type" v-for="col in row">{{col}}</td></tr>
                </table>
                <p class="des" v-show="showDes">{{pieData.ratioMsg}}</p>
            </div>
            <p class="no-data" style="height: 1.2rem;" v-show="!pieData.list && !noTab">暂无相关信息</p>
            <div class="detail-bottom" :class="{'top-border': !pieData.list}" v-if="noTab">
                <div class="table-box">
                    <table class="detail-table" v-if="actTabData.title">
                        <tr><th v-for="t in actTabData.title">{{t}}</th></tr>
                        <tr v-for="row in actTabData.list"><td v-for="col in row">{{col}}</td></tr>
                    </table>
                    <p class="no-data" v-if="!actTabData.title">暂无相关信息</p>
                </div>
                <p class="group-type">
                    <span v-for="(d,i) in ['重仓股票','重仓债券','重仓基金','行业分布']"
                          :class="{'active':actTab==i}" @click="switchTab(i)">{{d}}</span>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
    import echarts from 'echarts/dist/echarts.common';
    import common from '@utils/commons';
    let param = common.getParams();

    export default {
        data() {
            return {
                actTab:0,
                pieData:{},
                actTabData:{},
                tab0:{},
                tab1:{},
                tab2:{},
                tab3:{},
                showDes:false,
                noTab: 4,
            }
        },
        computed:{
            reportDate(){
                const str = [];
                for(let i = 0;i<4;i++){
                    str.push(this[`tab${i}`]['reportDate'] || '');
                }
                return str.join(',');
            }
        },
        methods: {
            async loadData(){
                this.$get('/finchinaAPP/nonStandard/getInvestPortfolioPercent.action', param).then(res => {
                    this.pieData = res.data;
                    const v = res.data.list.map(d => d[2]);
                    this.$nextTick(()=>{this.initThreeChart(v);})
                });
                const type = JSON.parse(localStorage.getItem("localReq")).type;
                if(type === '206' || type === '201'){this.actTab = 1;}//债券型、货币型  重仓债券
                if(type === '207'){this.actTab = 2;}//FOF型  重仓基金3
                try{
                    const {data} = await
                        this.$get('/finchinaAPP/nonStandard/getInvestPortfolioDetailsForMain.action',
                            {...param,type:this.actTab});
                    this.actTabData = data;
                    this[`tab${this.actTab}`] = data;
                } catch(e){
                    this.noTab -= 1;
                }
                //加载余下三个tab数据
                setTimeout(()=>{
                    for(let i = 0;i<4;i++){
                        if( i !== this.actTab){
                            this.getTabData(i);
                        }
                    }
                },100)
            },
            getTabData(type){
                this.$get('/finchinaAPP/nonStandard/getInvestPortfolioDetailsForMain.action', {...param,type}).then(res => {
                    this[`tab${type}`] = res.data;
                }).catch(err=>{
                    this.noTab -= 1;
                });
            },
            switchTab(i){
                this.actTab = i;
                this.actTabData = this[`tab${i}`];
            },
            initThreeChart(d){
                if (!this.myThreeCharts) this.myThreeCharts = echarts.init(document.getElementById('pieChart'));
                const option = {
                    series: [{
                        type: 'pie',
                        silent: true,
                        radius: ['27%', '85%'],
                        center: ['44%', '55%'],
                        color: ['#E7554D', '#60C3D2', '#EDB965', '#32B16C'],
                        data: d,
                        label:false,
                    }]
                };
                this.myThreeCharts.setOption(option, true);
            },
        },
    }
</script>

<style scoped lang="less">
    .investPortfolio{
        padding-bottom: 0 !important;
        .no-data{
            padding: 0;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            align-content: center;
        }
        .head-title{
            .time,.label-type{
                color: #8C8C8C;
                font-size: 0.26rem;
                line-height: 0.26rem;
            }
        }
        .detail-top{
            display: flex;
            padding: 0 0.32rem;
            margin-top: 0.3rem;
            border-top: 0.01rem solid #eee;
            height: 3.24rem;
            position: relative;
            .pie{
                width: 1.9rem;
                height: 3.24rem;
            }
            .detail-table{
                padding: 0;
                width: calc(100% - 1.9rem);
                td,th:first-child {
                    text-align: right;
                }
                td:last-child{
                    padding-right: 0.32rem;
                }
                td:first-child{
                    color: #8C8C8C;
                    &:before{
                        content: '';
                        width: 0.16rem;
                        height: 0.16rem;
                        border-radius: 0.16rem;
                        display: inline-block;
                        vertical-align: top;
                        margin-top: 0.06rem;
                        margin-right: 0.16rem;
                        &:nth-child(1){background:#E7554D};
                        &:nth-child(2){background:#60C3D2};
                        &:nth-child(3){background:#EDB965};
                        &:nth-child(5){background:#32B16C};
                    }
                }
                tr:nth-child(2) td:before {background:#E7554D};
                tr:nth-child(3) td:before {background:#60C3D2};
                tr:nth-child(4) td:before {background:#EDB965};
                tr:nth-child(5) td:before {background:#32B16C};
            }
            .ques{
                color: #fff;
                background: #D0D0D0;
                width: 0.28rem;
                height: 0.28rem;
                -webkit-border-radius: 0.24rem;
                border-radius: 0.24rem;
                display: inline-block;
                text-align: center;
                font-weight: normal;
                margin-left: 0.08rem;
            }
            .des{
                position: absolute;
                right: 0.8rem;
                font-size: 0.24rem;
                background: #bdbdbd;
                padding: 0.1rem;
                color: #fff;
                top: 0.25rem;
                &:after{
                    content: '';
                    border: 0.13rem solid transparent;
                    border-left-color: #bdbdbd;
                    position: absolute;
                    right: -0.25rem;
                }
            }
        }
        .detail-bottom{
            border-top: 0.01rem solid #eee;
            &.top-border{
                margin-top: 0.3rem;
            }
            .table-box{
                height: 3.84rem;
            }
            .more{
                font-size: 0.24rem;
                color: #1482F0;
                line-height: 0.24rem;
                text-align: center;
                padding: 0.32rem 0 0.24rem 0;
                &:after{
                    content: '';
                    display: inline-block;
                    width: 0.13rem;
                    height: 0.13rem;
                    border: 0.025rem solid;
                    border-color: transparent transparent #1482F0 #1482F0;
                    transform: rotate(-45deg);
                    margin: 0 0 0.05rem 0.1rem;
                }
            }
            .group-type{
                display: flex;
                span{
                    flex: 1;
                    font-size: 0.26rem;
                    color: #8C8C8C;
                    line-height: 0.26rem;
                    font-weight: 600;
                    border: 0.01rem solid #eee;
                    border-left: none;
                    background: #FBFBFB;
                    padding: 0.24rem 0;
                    text-align: center;
                    &:last-child{
                        border-right: none;
                    }
                    &.active{color: #1482F0;background: #fff}
                }
            }
        }
    }
</style>